const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const sdkRoot = process.env.DEVECO_SDK_HOME || 'C:/Program Files/Huawei/DevEco Studio/sdk';
const ts = require(path.join(sdkRoot, 'default/openharmony/ets/build-tools/ets-loader/node_modules/typescript'));
const sourceRoot = path.resolve(__dirname, '../entry/src/main/ets');

// Execute the real non-UI ArkTS source. Only OS capabilities are replaced.
function loadSource(relativePath, stubs = {}, cache = new Map()) {
  const file = path.resolve(sourceRoot, relativePath.endsWith('.ets') ? relativePath : relativePath + '.ets');
  if (cache.has(file)) return cache.get(file);
  const output = ts.transpileModule(fs.readFileSync(file, 'utf8'), {
    compilerOptions: { target: ts.ScriptTarget.ES2020, module: ts.ModuleKind.CommonJS }
  }).outputText;
  const module = { exports: {} };
  cache.set(file, module.exports);
  const requireSource = (name) => {
    if (Object.prototype.hasOwnProperty.call(stubs, name)) return stubs[name];
    if (name.startsWith('.')) return loadSource(path.relative(sourceRoot, path.resolve(path.dirname(file), name)), stubs, cache);
    throw new Error('Unmocked OS boundary: ' + name);
  };
  vm.runInNewContext(output, { module, exports: module.exports, require: requireSource, Error, JSON, String, Number,
    encodeURIComponent, console }, { filename: file });
  return module.exports;
}
function dataFactory(data = {}) { return { id: 'sample', deviceId: 1, data, createdAt: '2026-11-08', updatedAt: '' }; }
function cameraFactory(overrides = {}) { return { pushName: 'rtmp://camera.example/live/barn1', url: '', ...overrides }; }

for (const [key, field, expected] of [
  ['temp', 'temperature', '0°C'], ['humidity_percent', 'humidity', '0%'],
  ['co2_ppm', 'co2', '0 ppm'], ['nh3_ppm', 'nh3', '0 ppm'],
  ['pm2.5', 'pm25', '0 μg/m³'], ['pm2_5', 'pm25', '0 μg/m³'],
  ['pm25_ug_m3', 'pm25', '0 μg/m³'], ['pm10_ug_m3', 'pm10', '0 μg/m³']
]) {
  test(`environment preserves zero for ${key}`, () => {
    const parser = loadSource('services/EnvironmentParser');
    const reading = parser.parseEnvironment(dataFactory({ [key]: 0 }));
    assert.equal(reading[field], expected);
    assert.equal(parser.hasEnvironmentReadings(dataFactory({ [key]: 0 })), true);
  });
}
test('environment tolerates null latest record', () => {
  const parser = loadSource('services/EnvironmentParser');
  assert.equal(parser.parseEnvironment(null).temperature, '--°C');
  assert.equal(parser.hasEnvironmentReadings(null), false);
});
test('environment rejects unrelated values when choosing a dashboard sensor', () => {
  const parser = loadSource('services/EnvironmentParser');
  assert.equal(parser.hasEnvironmentReadings(dataFactory({ battery: 82, temperature: '  ' })), false);
});
test('environment uses creation time when update time is absent', () => {
  const parser = loadSource('services/EnvironmentParser');
  assert.equal(parser.parseEnvironment(dataFactory()).updatedAt, '2026-11-08');
});

const urlStubs = { '@kit.ArkTS': { url: { URL: { parseURL: value => new URL(value) } } } };
for (const [name, camera, expected] of [
  ['exact backend HLS endpoint', cameraFactory({ url: 'https://video.example/live/barn1.m3u8' }), 'https://video.example/live/barn1.m3u8'],
  ['documented SRS fallback', cameraFactory(), 'http://camera.example:8080/live/barn1.flv'],
  ['RTMPS does not downgrade encryption', cameraFactory({ pushName: 'rtmps://video.example:443/live/barn1' }), 'https://video.example/live/barn1.flv'],
  ['nonmedia protocol', cameraFactory({ pushName: 'javascript:alert(1)' }), ''],
  ['credentials in URL', cameraFactory({ pushName: 'rtmp://user:secret@video.example/live/barn1' }), ''],
  ['missing stream path', cameraFactory({ pushName: 'rtmp://video.example/live' }), ''],
  ['invalid camera', cameraFactory({ pushName: 'not a url' }), ''],
  ['encoded stream name', cameraFactory({ pushName: 'rtmp://camera.example:1935/live/barn%201' }), 'http://camera.example:8080/live/barn%201.flv']
]) {
  test('playback URL: ' + name, () => {
    const resolver = loadSource('services/StreamResolver', urlStubs);
    assert.equal(resolver.resolvePlaybackUrl(camera.pushName, camera.url), expected);
  });
}

function apiFixture(responseFactory) {
  const state = { token: 'session-a', clearCount: 0, redirectCount: 0, destroyed: 0, requests: [] };
  const auth = { get token() { return state.token; }, async clearSession() { state.token = ''; state.clearCount++; } };
  const api = loadSource('services/ApiClient', {
    '@kit.NetworkKit': { http: { RequestMethod: { GET:'GET', POST:'POST', PUT:'PUT' }, HttpDataType: { STRING: 0 },
      createHttp: () => ({ request: async (url, options) => { state.requests.push({ url, options }); return responseFactory(state); },
        destroy: () => state.destroyed++ }) } },
    '@kit.ArkUI': { router: { clear() {}, replaceUrl: async () => { state.redirectCount++; } } },
    './AuthStore': { AuthStore: { getInstance: () => auth } }
  });
  return { client: api.ApiClient.getInstance(), state };
}
function responseFactory(data = {}, code = 200, httpCode = 200) {
  return { responseCode: httpCode, result: JSON.stringify({ code, message:'result', data }) };
}
test('API sends satoken with encoded explicit query parameters', async () => {
  const fixture = apiFixture(() => responseFactory({ records: [] }));
  await fixture.client.get('/device/page', { type: 2, pageNum: 1, pageSize: 100, unexpected: 'ignored' });
  assert.match(fixture.state.requests[0].url, /pageNum=1&pageSize=100&type=2$/);
  assert.equal(fixture.state.requests[0].options.header.satoken, 'session-a');
  assert.equal(fixture.state.destroyed, 1);
});
for (const [name, body] of [['HTTP 401', () => responseFactory(null, 200, 401)], ['business 401', () => responseFactory(null, 401)]]) {
  test('API clears expired session for ' + name, async () => {
    const fixture = apiFixture(body);
    await assert.rejects(() => fixture.client.get('/user'), /登录状态已失效/);
    assert.equal(fixture.state.token, '');
    assert.equal(fixture.state.redirectCount, 1);
    assert.equal(fixture.state.destroyed, 1);
  });
}
test('late unauthorized response cannot clear a newly replaced session', async () => {
  const fixture = apiFixture(state => { state.token = 'session-b'; return responseFactory(null, 401); });
  await assert.rejects(() => fixture.client.get('/user'));
  assert.equal(fixture.state.token, 'session-b');
  assert.equal(fixture.state.redirectCount, 0);
});
test('permission rejection keeps session intact', async () => {
  const fixture = apiFixture(() => responseFactory(null, 403));
  await assert.rejects(() => fixture.client.get('/admin/check'));
  assert.equal(fixture.state.clearCount, 0);
});
test('empty successful response is supported for mutation endpoints', async () => {
  const fixture = apiFixture(() => responseFactory(null));
  assert.equal(await fixture.client.post('/order/complete?id=1'), undefined);
});
test('malformed JSON always releases HTTP request', async () => {
  const fixture = apiFixture(() => ({ responseCode: 200, result: '<html>error</html>' }));
  await assert.rejects(() => fixture.client.get('/user'));
  assert.equal(fixture.state.destroyed, 1);
});
test('network failure always releases HTTP request', async () => {
  const fixture = apiFixture(() => { throw new Error('socket closed'); });
  await assert.rejects(() => fixture.client.get('/user'), /网络连接失败/);
  assert.equal(fixture.state.destroyed, 1);
});

test('all registered pages exist', () => {
  const config = JSON.parse(fs.readFileSync(path.resolve(sourceRoot, '../resources/base/profile/main_pages.json'), 'utf8'));
  for (const page of config.src) assert.equal(fs.existsSync(path.join(sourceRoot, page + '.ets')), true, page);
});

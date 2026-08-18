export class ApiConfig {
  // Keep this aligned with Android's ApiConfig during migration.
  // Replace it with an HTTPS endpoint before a production release.
  static readonly BASE_URL: string = 'http://124.71.97.178:8049'
  static readonly CONNECT_TIMEOUT_MS: number = 30000
  static readonly READ_TIMEOUT_MS: number = 30000
}

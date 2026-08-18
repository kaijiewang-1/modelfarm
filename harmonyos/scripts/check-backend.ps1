param(
  [string]$BaseUrl = $(if ($env:MODEL_FARM_BASE_URL) { $env:MODEL_FARM_BASE_URL } else { 'http://124.71.97.178:8049' }),
  [string]$Phone = $env:MODEL_FARM_TEST_PHONE,
  [string]$Password = $env:MODEL_FARM_TEST_PASSWORD
)

$ErrorActionPreference = 'Stop'

if ([string]::IsNullOrWhiteSpace($Phone) -or [string]::IsNullOrWhiteSpace($Password)) {
  throw 'Set MODEL_FARM_TEST_PHONE and MODEL_FARM_TEST_PASSWORD before running this check.'
}

$BaseUrl = $BaseUrl.TrimEnd('/')

function Invoke-BackendApi {
  param(
    [string]$Method,
    [string]$Path,
    [hashtable]$Headers = @{},
    [object]$Body = $null
  )

  $request = @{
    Method = $Method
    Uri = "$BaseUrl$Path"
    Headers = $Headers
    ContentType = 'application/json'
  }
  if ($null -ne $Body) {
    $request.Body = ($Body | ConvertTo-Json -Compress)
  }
  return Invoke-RestMethod @request
}

function Assert-ApiSuccess {
  param([object]$Response, [string]$Name)
  if ($null -eq $Response -or $Response.code -ne 200) {
    $message = if ($null -ne $Response) { $Response.message } else { 'empty response' }
    throw "$Name failed: $message"
  }
  return $Response.data
}

$login = Assert-ApiSuccess (Invoke-BackendApi -Method POST -Path '/user/login' -Body @{ phone = $Phone; password = $Password }) 'login'
$token = [string]$login.satoken.tokenValue
if ([string]::IsNullOrWhiteSpace($token)) {
  throw 'login returned no satoken tokenValue'
}
$headers = @{ satoken = $token }

try {
  $stats = Assert-ApiSuccess (Invoke-BackendApi -Method GET -Path '/enterprise/count' -Headers $headers) 'enterprise stats'
  $farms = @(Assert-ApiSuccess (Invoke-BackendApi -Method GET -Path '/enterprise/farms' -Headers $headers) 'farms')
  if ($farms.Count -eq 0) { throw 'no farms returned for test account' }
  $sites = @(Assert-ApiSuccess (Invoke-BackendApi -Method GET -Path "/farm/sites?farmId=$($farms[0].id)" -Headers $headers) 'farm sites')
  if ($sites.Count -eq 0) { throw 'no farm sites returned for first farm' }
  $cameras = @(Assert-ApiSuccess (Invoke-BackendApi -Method GET -Path "/farmSite/camera?farmSiteId=$($sites[0].id)" -Headers $headers) 'cameras')
  $camera = $cameras | Where-Object { $_.type -eq 1 } | Select-Object -First 1
  if ($null -eq $camera) { throw 'no camera returned for first farm site' }
  $ticket = Assert-ApiSuccess (Invoke-BackendApi -Method POST -Path '/live/ticket' -Headers $headers -Body @{ deviceId = [int]$camera.id }) 'live ticket'

  [pscustomobject]@{
    baseUrl = $BaseUrl
    login = 'ok'
    enterpriseId = $login.enterpriseId
    stats = $stats
    farmCount = $farms.Count
    siteCount = $sites.Count
    cameraId = $camera.id
    cameraName = $camera.name
    ticketStreamId = $ticket.streamId
    ticketWsUrl = $ticket.wsUrl
    ticketExpiresAt = $ticket.expiresAt
  } | ConvertTo-Json -Depth 6
}
finally {
  try { Invoke-BackendApi -Method POST -Path '/user/logout' -Headers $headers | Out-Null } catch { }
}

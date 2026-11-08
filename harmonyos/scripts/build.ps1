param(
  [string]$DevEcoHome = 'C:\Program Files\Huawei\DevEco Studio',
  [switch]$Clean
)
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$hvigor = Join-Path $DevEcoHome 'tools/hvigor/bin/hvigorw.bat'
if (-not (Test-Path -LiteralPath $hvigor)) { throw "DevEco Hvigor not found: $hvigor" }
$previousSdk = $env:DEVECO_SDK_HOME
try {
  $env:DEVECO_SDK_HOME = Join-Path $DevEcoHome 'sdk'
  Push-Location $projectRoot
  try {
    if ($Clean) {
      & $hvigor --mode module -p product=default clean
      if ($LASTEXITCODE -ne 0) { throw 'Hvigor clean failed' }
    }
    & $hvigor --mode module -p product=default assembleHap
    if ($LASTEXITCODE -ne 0) { throw 'HarmonyOS build failed' }
    $hap = Join-Path $projectRoot 'entry/build/default/outputs/default/entry-default-unsigned.hap'
    if (Test-Path -LiteralPath $hap) { Get-Item -LiteralPath $hap | Select-Object FullName,Length,LastWriteTime }
  } finally { Pop-Location }
} finally { $env:DEVECO_SDK_HOME = $previousSdk }

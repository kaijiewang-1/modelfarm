import { DeviceData } from '../models/Models'

export interface EnvironmentReading {
  temperature: string
  humidity: string
  co2: string
  nh3: string
  pm25: string
  pm10: string
  updatedAt: string
}

function firstValue(data: Record<string, string | number> | undefined, keys: string[]): string {
  if (!data) {
    return '--'
  }
  for (let index: number = 0; index < keys.length; index++) {
    const value: string | number | undefined = data[keys[index]]
    if (value !== undefined && value !== null && String(value).trim().length > 0) {
      return String(value).trim()
    }
  }
  return '--'
}

export function hasEnvironmentReadings(deviceData: DeviceData): boolean {
  return !!deviceData && firstValue(deviceData.data, [
    'temperature', 'temp', 'humidity', 'hum', 'humidity_percent',
    'co2', 'co2_ppm', 'nh3', 'nh3_ppm', 'pm25', 'pm2.5', 'pm2_5', 'pm25_ug_m3', 'pm10', 'pm10_ug_m3'
  ]) !== '--'
}

export function parseEnvironment(deviceData: DeviceData): EnvironmentReading {
  if (!deviceData) {
    return emptyEnvironment()
  }
  return {
    temperature: `${firstValue(deviceData.data, ['temperature', 'temp'])}°C`,
    humidity: `${firstValue(deviceData.data, ['humidity', 'humidity_percent', 'hum'])}%`,
    co2: `${firstValue(deviceData.data, ['co2', 'co2_ppm'])} ppm`,
    nh3: `${firstValue(deviceData.data, ['nh3', 'nh3_ppm'])} ppm`,
    pm25: `${firstValue(deviceData.data, ['pm25', 'pm2.5', 'pm2_5', 'pm25_ug_m3'])} μg/m³`,
    pm10: `${firstValue(deviceData.data, ['pm10', 'pm10_ug_m3'])} μg/m³`,
    updatedAt: deviceData.updatedAt || deviceData.createdAt || '暂无数据'
  }
}

export function emptyEnvironment(): EnvironmentReading {
  return {
    temperature: '--°C', humidity: '--%', co2: '-- ppm', nh3: '-- ppm',
    pm25: '-- μg/m³', pm10: '-- μg/m³', updatedAt: '暂无数据'
  }
}

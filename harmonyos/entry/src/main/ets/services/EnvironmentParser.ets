import { DeviceData } from '../models/Models'

export interface EnvironmentReading {
  temperature: string
  humidity: string
  light: string
  airQuality: string
  windSpeed: string
  waterLevel: string
  updatedAt: string
}

function firstValue(data: Record<string, string | number>, keys: string[]): string {
  for (let index: number = 0; index < keys.length; index++) {
    const key: string = keys[index]
    const value: string | number | undefined = data[key]
    if (value !== undefined && value !== null && String(value).length > 0) {
      return String(value)
    }
  }
  return '--'
}

export function parseEnvironment(deviceData: DeviceData): EnvironmentReading {
  return {
    temperature: `${firstValue(deviceData.data, ['temperature', 'temp'])}°C`,
    humidity: `${firstValue(deviceData.data, ['humidity', 'hum'])}%`,
    light: `${firstValue(deviceData.data, ['light', 'illumination', 'lux'])} lx`,
    airQuality: firstValue(deviceData.data, ['airQuality', 'air_quality', 'aqi']),
    windSpeed: `${firstValue(deviceData.data, ['windSpeed', 'wind_speed'])} m/s`,
    waterLevel: `${firstValue(deviceData.data, ['waterLevel', 'water_level'])} cm`,
    updatedAt: deviceData.updatedAt || deviceData.createdAt
  }
}

export function emptyEnvironment(): EnvironmentReading {
  return {
    temperature: '--°C',
    humidity: '--%',
    light: '-- lx',
    airQuality: '--',
    windSpeed: '-- m/s',
    waterLevel: '-- cm',
    updatedAt: '暂无数据'
  }
}

package com.example.modelfarm.utils;

import java.util.Map;

/**
 * 设备数据解析器
 * 用于解析设备数据并提取环境监控信息
 */
public class DeviceDataParser {
    
    /**
     * 解析设备数据获取温度
     */
    public static String parseTemperature(Map<String, Object> data) {
        return formatMetric(data, "--°C", "°C", "temperature", "temp");
    }
    
    /**
     * 解析设备数据获取湿度
     */
    public static String parseHumidity(Map<String, Object> data) {
        return formatMetric(data, "--%", "%", "humidity", "humidity_percent");
    }
    
    /**
     * 解析设备数据获取光照
     */
    public static String parseLight(Map<String, Object> data) {
        if (data == null) return "-- lux";
        
        Object light = data.get("light");
        if (light != null) {
            return light.toString() + " lux";
        }
        
        // 尝试其他可能的光照字段名
        light = data.get("illumination");
        if (light != null) {
            return light.toString() + " lux";
        }
        
        return "-- lux";
    }
    
    /**
     * 解析设备数据获取土壤湿度
     */
    public static String parseSoilMoisture(Map<String, Object> data) {
        if (data == null) return "--%";
        
        Object moisture = data.get("soil_moisture");
        if (moisture != null) {
            return moisture.toString() + "%";
        }
        
        // 尝试其他可能的土壤湿度字段名
        moisture = data.get("moisture");
        if (moisture != null) {
            return moisture.toString() + "%";
        }
        
        return "--%";
    }
    
    /**
     * 解析设备数据获取pH值
     */
    public static String parsePH(Map<String, Object> data) {
        if (data == null) return "--";
        
        Object ph = data.get("ph");
        if (ph != null) {
            return ph.toString();
        }
        
        return "--";
    }
    
    /**
     * 解析设备数据获取二氧化碳浓度
     */
    public static String parseCO2(Map<String, Object> data) {
        return formatMetric(data, "-- ppm", " ppm", "co2", "co2_ppm");
    }

    /**
     * 解析设备数据获取氨气浓度
     */
    public static String parseNH3(Map<String, Object> data) {
        return formatMetric(data, "-- ppm", " ppm", "nh3", "nh3_ppm");
    }

    /**
     * 解析设备数据获取 PM2.5 浓度
     */
    public static String parsePM25(Map<String, Object> data) {
        return formatMetric(data, "-- μg/m³", " μg/m³", "pm25", "pm2.5", "pm2_5", "pm25_ug_m3");
    }

    /**
     * 解析设备数据获取 PM10 浓度
     */
    public static String parsePM10(Map<String, Object> data) {
        return formatMetric(data, "-- μg/m³", " μg/m³", "pm10", "pm10_ug_m3");
    }

    /**
     * 判断数据中是否包含环境监控页支持的任一读数
     */
    public static boolean hasEnvironmentReadings(Map<String, Object> data) {
        return findFirstValue(data,
            "temperature", "temp", "humidity", "humidity_percent",
            "co2", "co2_ppm", "nh3", "nh3_ppm",
            "pm25", "pm2.5", "pm2_5", "pm25_ug_m3",
            "pm10", "pm10_ug_m3") != null;
    }
    
    /**
     * 检查设备数据是否有效
     */
    public static boolean isValidData(Map<String, Object> data) {
        return data != null && !data.isEmpty();
    }
    
    /**
     * 获取设备数据的时间戳
     */
    public static String getDataTimestamp(Map<String, Object> data) {
        if (data == null) return "未知时间";
        
        Object timestamp = data.get("timestamp");
        if (timestamp != null) {
            return timestamp.toString();
        }
        
        Object time = data.get("time");
        if (time != null) {
            return time.toString();
        }
        
        return "未知时间";
    }

    private static String formatMetric(
        Map<String, Object> data,
        String placeholder,
        String unit,
        String... keys
    ) {
        Object value = findFirstValue(data, keys);
        return value == null ? placeholder : value.toString() + unit;
    }

    private static Object findFirstValue(Map<String, Object> data, String... keys) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        for (String key : keys) {
            Object value = data.get(key);
            if (value != null && !value.toString().trim().isEmpty()) {
                return value;
            }
        }
        return null;
    }
}

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
        if (data == null) return "--°C";
        
        Object temp = data.get("temperature");
        if (temp != null) {
            return temp.toString() + "°C";
        }
        
        // 尝试其他可能的温度字段名
        temp = data.get("temp");
        if (temp != null) {
            return temp.toString() + "°C";
        }
        
        return "--°C";
    }
    
    /**
     * 解析设备数据获取湿度
     */
    public static String parseHumidity(Map<String, Object> data) {
        if (data == null) return "--%";
        
        Object humidity = data.get("humidity");
        if (humidity != null) {
            return humidity.toString() + "%";
        }
        
        return "--%";
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
        if (data == null) return "-- ppm";
        
        Object co2 = data.get("co2");
        if (co2 != null) {
            return co2.toString() + " ppm";
        }
        
        return "-- ppm";
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
}

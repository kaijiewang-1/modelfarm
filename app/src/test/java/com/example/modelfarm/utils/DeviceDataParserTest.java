package com.example.modelfarm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DeviceDataParserTest {

    @Test
    public void parseEnvironmentPayload_withBackendFields_formatsEachMetric() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("temperature", 29.5);
        data.put("humidity_percent", 94.2);
        data.put("co2_ppm", 1508.0);
        data.put("nh3_ppm", 314.0);
        data.put("pm25_ug_m3", 13.0);
        data.put("pm10_ug_m3", 29.0);

        // Act / Assert
        assertEquals("29.5°C", DeviceDataParser.parseTemperature(data));
        assertEquals("94.2%", DeviceDataParser.parseHumidity(data));
        assertEquals("1508.0 ppm", DeviceDataParser.parseCO2(data));
        assertEquals("314.0 ppm", DeviceDataParser.parseNH3(data));
        assertEquals("13.0 μg/m³", DeviceDataParser.parsePM25(data));
        assertEquals("29.0 μg/m³", DeviceDataParser.parsePM10(data));
    }

    @Test
    public void parseEnvironmentPayload_withLegacyBackendFields_usesAliases() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("temp", "20");
        data.put("humidity", "60");
        data.put("co2", "800");
        data.put("nh3", "12");
        data.put("pm25", "18");
        data.put("pm10", "30");

        // Act / Assert
        assertEquals("20°C", DeviceDataParser.parseTemperature(data));
        assertEquals("60%", DeviceDataParser.parseHumidity(data));
        assertEquals("800 ppm", DeviceDataParser.parseCO2(data));
        assertEquals("12 ppm", DeviceDataParser.parseNH3(data));
        assertEquals("18 μg/m³", DeviceDataParser.parsePM25(data));
        assertEquals("30 μg/m³", DeviceDataParser.parsePM10(data));
    }

    @Test
    public void parseEnvironmentPayload_withMissingValues_returnsVisiblePlaceholders() {
        // Arrange
        Map<String, Object> data = new HashMap<>();

        // Act / Assert
        assertEquals("--°C", DeviceDataParser.parseTemperature(data));
        assertEquals("--%", DeviceDataParser.parseHumidity(data));
        assertEquals("-- ppm", DeviceDataParser.parseCO2(data));
        assertEquals("-- ppm", DeviceDataParser.parseNH3(data));
        assertEquals("-- μg/m³", DeviceDataParser.parsePM25(data));
        assertEquals("-- μg/m³", DeviceDataParser.parsePM10(data));
    }

    @Test
    public void hasEnvironmentReadings_withKnownMetric_returnsTrue() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("pm25", 13);

        // Act
        boolean result = DeviceDataParser.hasEnvironmentReadings(data);

        // Assert
        assertTrue(result);
    }

    @Test
    public void hasEnvironmentReadings_withUnrelatedMetric_returnsFalse() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("battery", 80);

        // Act
        boolean result = DeviceDataParser.hasEnvironmentReadings(data);

        // Assert
        assertFalse(result);
    }
}

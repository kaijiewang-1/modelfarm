package com.example.modelfarm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StreamUrlResolverTest {

    @Test
    public void resolve_withCompleteRtmpPushName_returnsTrimmedUrl() {
        // Arrange
        String pushName = "  rtmp://1.95.8.122:1935/live/chicken138  ";

        // Act
        String result = StreamUrlResolver.resolve(pushName);

        // Assert
        assertEquals("rtmp://1.95.8.122:1935/live/chicken138", result);
    }

    @Test
    public void resolve_withSecureRtmpPushName_returnsUrl() {
        // Arrange
        String pushName = "rtmps://stream.example.com/live/camera-1";

        // Act
        String result = StreamUrlResolver.resolve(pushName);

        // Assert
        assertEquals(pushName, result);
    }

    @Test
    public void resolve_withLegacyHttpUrl_rejectsUrl() {
        // Arrange
        String pushName = "http://1.95.8.122/live/chicken138.flv";

        // Act
        String result = StreamUrlResolver.resolve(pushName);

        // Assert
        assertNull(result);
    }

    @Test
    public void resolve_withMissingStreamPath_rejectsUrl() {
        // Arrange
        String pushName = "rtmp://1.95.8.122:1935";

        // Act
        String result = StreamUrlResolver.resolve(pushName);

        // Assert
        assertNull(result);
    }

    @Test
    public void resolve_withMalformedValue_rejectsUrl() {
        // Arrange
        String pushName = "not a stream url";

        // Act
        String result = StreamUrlResolver.resolve(pushName);

        // Assert
        assertNull(result);
    }
}

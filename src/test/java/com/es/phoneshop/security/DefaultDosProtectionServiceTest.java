package com.es.phoneshop.security;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest {
    private final DefaultDosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();

    private final String ip = "[0.0.0.0.0.0.0.1]";

    @Test
    public void testIsAllowed() {
        boolean result = dosProtectionService.isAllowed(ip);

        assertTrue(result);
    }

    @Test
    public void testIsAllowedTooManyRequests() {
        boolean result = true;

        for (int i = 0; i < 50; ++i) {
            result = dosProtectionService.isAllowed(ip);
        }

        assertFalse(result);
    }

    @Test
    public void testIsAllowedAwaitMinute() {
        for (int i = 0; i < 20; ++i) {
            dosProtectionService.isAllowed(ip);
        }

        await()
                .pollDelay(1, TimeUnit.MINUTES)
                .atMost(2, TimeUnit.MINUTES)
                .until(() -> dosProtectionService.isAllowed(ip));
    }
}
package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;

    private static final long TIME_FRAME_MILLIS = 60000;

    private final Map<String, RequestInfo> countMap = new ConcurrentHashMap<>();

    private static final class DefaultDosProtectionServiceHolder {
        private static final DefaultDosProtectionService instance = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.DefaultDosProtectionServiceHolder.instance;
    }

    private DefaultDosProtectionService() {

    }

    @Override
    public boolean isAllowed(String ip) {
        long now = System.currentTimeMillis();

        RequestInfo info = countMap.get(ip);

        if (info == null) {
            info = new RequestInfo(1L, now);
        } else {
            if (now - info.getTimestamp() > TIME_FRAME_MILLIS) {
                info.setTimestamp(now);
                info.setCount(1L);
                return true;
            } else {
                if (info.getCount() > THRESHOLD) {
                    return false;
                } else {
                    info.incCount();
                }
            }
            return true;
        }

        countMap.put(ip, info);

        return true;
    }
}

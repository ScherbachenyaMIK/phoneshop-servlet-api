package com.es.phoneshop.util;

import jakarta.servlet.http.HttpServletRequest;

public class IdParser {
    public static Long parseId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}

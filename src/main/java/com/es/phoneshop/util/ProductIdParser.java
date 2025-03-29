package com.es.phoneshop.util;

import jakarta.servlet.http.HttpServletRequest;

public class ProductIdParser {
    public static Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}

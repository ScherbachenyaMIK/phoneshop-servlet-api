package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig config;
    @Mock
    private DosProtectionService dosProtectionService;

    private final DosFilter dosFilter = new DosFilter();

    @Before
    public void setUp() throws Exception {
        when(request.getRemoteAddr()).thenReturn("[0.0.0.0.0.0.0.1]");

        dosFilter.init(config);

        Field service = DosFilter.class
                .getDeclaredField("dosProtectionService");
        service.setAccessible(true);
        service.set(dosFilter, dosProtectionService);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(anyString())).thenReturn(true);

        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterTooManyRequests() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(anyString())).thenReturn(false);

        dosFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(429);
    }

    @After
    public void tearDown() {
        dosFilter.destroy();
    }
}
package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import java.lang.reflect.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemoDataServletContextListenerTest {
    @Mock
    private ProductDao arrayListProductDao;
    @Mock
    private ServletContextEvent event;
    @Mock
    private ServletContext context;
    private ProductDemoDataServletContextListener listener = new ProductDemoDataServletContextListener();

    @Test
    public void testContextInitialized() throws IllegalAccessException, NoSuchFieldException {
        Field field = ProductDemoDataServletContextListener.class
                .getDeclaredField("arrayListProductDao");
        field.setAccessible(true);
        field.set(listener, arrayListProductDao);

        when(event.getServletContext()).thenReturn(context);
        when(context.getInitParameter("insertDemoData")).thenReturn("true");

        listener.contextInitialized(event);

        verify(arrayListProductDao, times(13)).save(any());
    }
}
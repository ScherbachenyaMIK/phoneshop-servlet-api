package com.es.phoneshop.model.product;

import com.es.phoneshop.web.ProductDemoDataServletContextListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArrayListProductDaoTest
{
    static {
        ProductDemoDataServletContextListener listener = new ProductDemoDataServletContextListener();

        ServletContextEvent event = mock(ServletContextEvent.class);
        ServletContext context = mock(ServletContext.class);

        when(event.getServletContext()).thenReturn(context);
        when(context.getInitParameter("insertDemoData")).thenReturn("true");

        listener.contextInitialized(event);
    }

    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();

    }

    @Test
    public void testGetProduct() {
        Long id = 4L;
        Product expected = new Product(
                4L,
                "iphone",
                "Apple iPhone",
                new BigDecimal(200),
                Currency.getInstance("USD"),
                10,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg",
                List.of(new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(295)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(250)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(225)))
        );


        Product result = productDao.getById(id);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCode(), result.getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductThrowsNoSuchElementException() {
        Long id = 15L;

        productDao.getById(id);
    }

    @Test
    public void testFindProductsWithNoFilters() {
        List<Product> result = productDao.findProducts(null, SortingField.none, SortingOrder.none);

        assertFalse(result.isEmpty());
        result.forEach(item ->
                assertTrue(item.getStock() > 0 && !Objects.isNull(item.getPrice()))
                );
    }

    @Test
    public void testSaveProduct() {
        int listSize = productDao.findProducts(null, SortingField.none, SortingOrder.none).size();
        //Product must have non-null price and positive stock
        Product newProduct = new Product(
                "iphone10",
                "Apple iPhone 10",
                new BigDecimal(2000),
                Currency.getInstance("USD"),
                8,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg",
                List.of(new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2095)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2100)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2045)))
        );

        productDao.save(newProduct);
        List<Product> result = productDao.findProducts(null, SortingField.none, SortingOrder.none);

        assertEquals(listSize + 1, result.size());
    }

    @Test
    public void testUpdateProduct() {
        int listSize = productDao.findProducts(null, SortingField.none, SortingOrder.none).size();
        //Product must have non-null price and positive stock
        Product updateProduct = new Product(
                4L,
                "iphone10",
                "Apple iPhone 10",
                new BigDecimal(2000),
                Currency.getInstance("USD"),
                8,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg",
                List.of(new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2095)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2100)), new PriceHistory(Date.from(Instant.now()), BigDecimal.valueOf(2045)))
        );

        productDao.save(updateProduct);
        List<Product> result = productDao.findProducts(null, SortingField.none, SortingOrder.none);
        Product updatedProduct = productDao.getById(4L);

        assertEquals(listSize, result.size());
        assertEquals(updateProduct.getCode(), updatedProduct.getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProduct() {
        int listSize = productDao.findProducts(null, SortingField.none, SortingOrder.none).size();
        Long idToDelete = 7L;

        productDao.delete(idToDelete);
        List<Product> result = productDao.findProducts(null, SortingField.none, SortingOrder.none);

        assertEquals(listSize - 1, result.size());

        productDao.getById(7L);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProductThrowsNoSuchElementException() {
        Long idToDelete = 20L;

        productDao.delete(idToDelete);
    }

    @Test
    public void testFindProductsWithFilter() {
        String query = "SaMSung   S             III";
        List<Product> result = productDao.findProducts(query, SortingField.none, SortingOrder.none);

        result.forEach(item -> {
            assertTrue(item.getStock() > 0 && !Objects.isNull(item.getPrice()));
            assertTrue(
                    item.getDescription().toLowerCase().contains("samsung")
                    || item.getDescription().toLowerCase().contains("s")
                    || item.getDescription().toLowerCase().contains("iii")
            );
            }
        );
        assertEquals("Samsung Galaxy S III", result.get(0).getDescription());
    }

    @Test
    public void testFindProductsWithFilterReturnEmpty() {
        String query = "abacaba";
        List<Product> result = productDao.findProducts(query, SortingField.none, SortingOrder.none);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindProductsWithSortingDescriptionAsc() {
        List<Product> result = productDao.findProducts(
                null,
                SortingField.description,
                SortingOrder.asc
        );

        assertTrue(result.get(0).getDescription().startsWith("Apple"));
    }

    @Test
    public void testFindProductsWithSortingPriceDesc() {
        List<Product> result = productDao.findProducts(
                "Samsung",
                SortingField.price,
                SortingOrder.desc
        );

        assertTrue(result.get(0).getPrice().compareTo(result.get(1).getPrice()) >= 0);
    }
}

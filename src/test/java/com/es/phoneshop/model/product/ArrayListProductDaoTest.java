package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
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
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"
        );


        Product result = productDao.getProduct(id);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCode(), result.getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductThrowsNoSuchElementException() {
        Long id = 15L;

        productDao.getProduct(id);
    }

    @Test
    public void testFindProductsWithNoFilters() {
        List<Product> result = productDao.findProducts(null);

        assertFalse(result.isEmpty());
        result.forEach(item ->
                assertTrue(item.getStock() > 0 && !Objects.isNull(item.getPrice()))
                );
    }

    @Test
    public void testSaveProduct() {
        int listSize = productDao.findProducts(null).size();
        //Product must have non-null price and positive stock
        Product newProduct = new Product(
                "iphone10",
                "Apple iPhone 10",
                new BigDecimal(2000),
                Currency.getInstance("USD"),
                8,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"
        );

        productDao.save(newProduct);
        List<Product> result = productDao.findProducts(null);

        assertEquals(listSize + 1, result.size());
    }

    @Test
    public void testUpdateProduct() {
        int listSize = productDao.findProducts(null).size();
        //Product must have non-null price and positive stock
        Product updateProduct = new Product(
                4L,
                "iphone10",
                "Apple iPhone 10",
                new BigDecimal(2000),
                Currency.getInstance("USD"),
                8,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"
        );

        productDao.save(updateProduct);
        List<Product> result = productDao.findProducts(null);
        Product updatedProduct = productDao.getProduct(4L);

        assertEquals(listSize, result.size());
        assertEquals(updateProduct.getCode(), updatedProduct.getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProduct() {
        int listSize = productDao.findProducts(null).size();
        Long idToDelete = 7L;

        productDao.delete(idToDelete);
        List<Product> result = productDao.findProducts(null);

        assertEquals(listSize - 1, result.size());

        productDao.getProduct(7L);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProductThrowsNoSuchElementException() {
        int listSize = productDao.findProducts(null).size();
        Long idToDelete = 20L;

        productDao.delete(idToDelete);
    }

    @Test
    public void testFindProductsWithFilter() {
        String query = "SaMSung   S             III";
        List<Product> result = productDao.findProducts(query);

        result.forEach(item -> {
            assertTrue(item.getStock() > 0 && !Objects.isNull(item.getPrice()));
            assertTrue(
                    item.getDescription().toLowerCase().matches(".*\\bsamsung\\b.*")
                    || item.getDescription().toLowerCase().matches(".*\\bs\\b.*")
                    || item.getDescription().toLowerCase().matches(".*\\biii\\b.*")
            );
            }
        );
        assertEquals("Samsung Galaxy S III", result.get(0).getDescription());
    }

    @Test
    public void testFindProductsWithFilterReturnEmpty() {
        String query = "abacaba";
        List<Product> result = productDao.findProducts(query);

        assertTrue(result.isEmpty());
    }
}

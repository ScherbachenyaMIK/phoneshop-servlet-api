package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class ProductDemoDataServletContextListener implements ServletContextListener {
    private final ProductDao arrayListProductDao = ArrayListProductDao.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);

        if (Boolean.parseBoolean(
                sce.getServletContext().getInitParameter("insertDemoData"))
        ) {
            getSampleProducts().forEach(arrayListProductDao::save);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private List<Product> getSampleProducts(){
        Currency usd = Currency.getInstance("USD");

        return List.of(
                new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(95)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(110)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(105)))),
                new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(195)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(210)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(205)))),
                new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(295)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(310)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(305)))),
                new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(295)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(250)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(225)))),
                new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(1200)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(1100)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(1005)))),
                new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(320)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(310)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(315)))),
                new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(420)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(400)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(410)))),
                new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(120)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(120)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(120)))),
                new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(80)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(75)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(73)))),
                new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(150)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(160)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(165)))),
                new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(65)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(70)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(65)))),
                new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(75)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(80)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(85)))),
                new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", List.of(new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 3)), BigDecimal.valueOf(130)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3 * 2)), BigDecimal.valueOf(140)), new PriceHistory(Date.from(Instant.now().minusSeconds(2592000L * 3)), BigDecimal.valueOf(160))))
        );
    }
}

package com.es.phoneshop.history;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecentlyViewedService {
    List<Product> getRecentlyViewedProducts(HttpServletRequest request);

    void addToRecentlyViewed(List<Product> recentlyViewed, Product product);
}

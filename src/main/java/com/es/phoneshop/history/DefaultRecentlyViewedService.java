package com.es.phoneshop.history;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedService implements RecentlyViewedService {
    private static final String RECENTLY_VIEWED_ATTRIBUTE = "recentlyViewed";

    private final ReadWriteLock lock;

    private static final class DefaultRecentlyViewedServiceHolder {
        private static final DefaultRecentlyViewedService instance = new DefaultRecentlyViewedService();
    }

    public static DefaultRecentlyViewedService getInstance() {
        return DefaultRecentlyViewedService.DefaultRecentlyViewedServiceHolder.instance;
    }

    private DefaultRecentlyViewedService() {
        lock = new ReentrantReadWriteLock();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> getRecentlyViewedProducts(HttpServletRequest request) {
        lock.readLock().lock();
        try {
            List<Product> recentlyViewed = (List<Product>) request.getSession()
                    .getAttribute(RECENTLY_VIEWED_ATTRIBUTE);

            if (recentlyViewed == null) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    recentlyViewed = (List<Product>) request.getSession()
                            .getAttribute(RECENTLY_VIEWED_ATTRIBUTE);
                    if (recentlyViewed == null) {
                        request.getSession().setAttribute(
                                RECENTLY_VIEWED_ATTRIBUTE,
                                recentlyViewed = new LinkedList<>()
                        );
                    }
                } finally {
                    lock.writeLock().unlock();
                    lock.readLock().lock();
                }
            }

            return recentlyViewed;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addToRecentlyViewed(List<Product> recentlyViewed, Product product) {
        lock.writeLock().lock();
        try {
            recentlyViewed.remove(product);

            recentlyViewed.add(0, product);

            if (recentlyViewed.size() > 3) {
                recentlyViewed.remove(3);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}

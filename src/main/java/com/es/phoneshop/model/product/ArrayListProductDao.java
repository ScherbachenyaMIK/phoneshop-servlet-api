package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayListProductDao implements ProductDao {
    private List<Product> data;

    private long maxId;

    private final ReadWriteLock lock;

    public ArrayListProductDao() {
        this.data = new ArrayList<>();
        maxId = 1L;
        lock = new ReentrantReadWriteLock();
        this.getSampleProducts();
    }

    @Override
    public Product getProduct(Long id) {
        lock.readLock().lock();
        try {
            return data.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(NoSuchElementException::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query) {
        List<String> keyWords = Optional.ofNullable(query)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(q -> !q.isEmpty())
                .map(q -> Arrays.stream(query.split("\\s+"))
                        .map(String::toLowerCase)
                        .toList())
                .orElse(List.of());

        lock.readLock().lock();
        try {
            return data.stream()
                    .filter(item ->
                            item.getStock() > 0 && !Objects.isNull(item.getPrice())
                    )
                    .filter(item ->
                            keyWords.isEmpty() ||
                            keyWords.stream()
                                    .anyMatch(word ->
                                            item.getDescription()
                                                    .toLowerCase()
                                                    .matches(".*\\b" + word + "\\b.*")
                    ))
                    .sorted(Comparator.comparingDouble(item -> {
                                long matches = keyWords.stream()
                                        .filter(item.getDescription().toLowerCase()::contains)
                                        .count();
                                long totalWords = item.getDescription()
                                        .split("\\s+")
                                        .length;
                                return -(double) matches / totalWords;
                            }
                    ))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        Long updatedId = product.getId();

        try {
            if (updatedId != null && contains(updatedId)) {
                lock.writeLock().lock();
                IntStream.range(0, data.size())
                        .filter(i -> updatedId.equals(data.get(i).getId()))
                        .findAny()
                        .ifPresent(i -> data.set(i, product));
            } else {
                lock.writeLock().lock();
                product.setId(maxId++);
                data.add(product);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        if (contains(id)) {
            lock.writeLock().lock();
            try {
                data = data.stream()
                        .filter(item -> !id.equals(item.getId()))
                        .toList();
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    private boolean contains(Long id) {
        lock.readLock().lock();
        try {
            return data.stream()
                    .anyMatch(item -> id.equals(item.getId()));
        } finally {
            lock.readLock().unlock();
        }
    }

    private void getSampleProducts(){
        Currency usd = Currency.getInstance("USD");
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}

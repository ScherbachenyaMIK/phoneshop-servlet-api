package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    private static final class ArrayListProductDaoHolder {
        private static final ProductDao instance = new ArrayListProductDao();
    }

    public static ProductDao getInstance() {
        return ArrayListProductDaoHolder.instance;
    }

    private ArrayListProductDao() {
        this.data = new ArrayList<>();
        maxId = 1L;
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Product getProduct(Long id) {
        lock.readLock().lock();
        try {
            return data.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() ->
                            new NoSuchElementException("Product with id " + id + " not found.")
                    );
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortingField field, SortingOrder order) {
        List<String> keyWords = Optional.ofNullable(query)
                .map(String::toLowerCase)
                .map(String::trim)
                .filter(q -> !q.isEmpty())
                .map(q -> Arrays.stream(q.split("\\s+"))
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
                                                    .contains(word)
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
                    .sorted(prepareComparator(field, order))
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
            throw new NoSuchElementException("Product with id " + id + " not found.");
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

    private Comparator<Product> prepareComparator(SortingField field, SortingOrder order) {
        Comparator<Product> comparator;

        switch (field) {
            case description -> comparator = Comparator.comparing(
                    Product::getDescription
            );
            case price -> comparator = Comparator.comparing(
                    Product::getPrice
            );
            default -> {
                return Comparator.comparingInt(x -> 0);
            }
        }

        if (order == SortingOrder.desc) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}

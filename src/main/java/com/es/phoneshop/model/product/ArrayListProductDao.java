package com.es.phoneshop.model.product;

import com.es.phoneshop.common.GenericArrayListDao;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericArrayListDao<Product> implements ProductDao {
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with id %d not found.";

    private static final class ArrayListProductDaoHolder {
        private static final ProductDao instance = new ArrayListProductDao();
    }

    public static ProductDao getInstance() {
        return ArrayListProductDaoHolder.instance;
    }

    private ArrayListProductDao() {
        maxId = 1L;
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
    public List<Product> findProductsAdvanced(String query, BigDecimal minPrice,
                                              BigDecimal maxPrice, SearchingCriteria criteria) {
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
                    .filter(item -> {
                        if (criteria == SearchingCriteria.anyWord) {
                            return keyWords.isEmpty() ||
                                    keyWords.stream()
                                            .anyMatch(word ->
                                                    item.getDescription()
                                                            .toLowerCase()
                                                            .contains(word));
                        } else {
                            return keyWords.isEmpty() ||
                                    keyWords.stream()
                                            .allMatch(word ->
                                                    item.getDescription()
                                                            .toLowerCase()
                                                            .contains(word));
                        }
                    })
                    .filter(item -> {
                        if (minPrice.compareTo(BigDecimal.valueOf(-1)) == 0) {
                            return true;
                        } else {
                            return item.getPrice().compareTo(minPrice) >= 0;
                        }
                    })
                    .filter(item -> {
                        if (maxPrice.compareTo(BigDecimal.valueOf(-1)) == 0) {
                            return true;
                        } else {
                            return item.getPrice().compareTo(maxPrice) <= 0;
                        }
                    })
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
    protected Long getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Product entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected String getNotFoundMessage() {
        return PRODUCT_NOT_FOUND_MESSAGE;
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

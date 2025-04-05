package com.es.phoneshop.common;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public abstract class GenericArrayListDao<T> {
    protected List<T> data = new ArrayList<>();

    protected long maxId;

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected abstract Long getId(T entity);

    protected abstract void setId(T entity, Long id);

    protected abstract String getNotFoundMessage();

    public T getById(Long id) {
        lock.readLock().lock();
        try {
            return data.stream()
                    .filter(item -> id.equals(getId(item)))
                    .findAny()
                    .orElseThrow(() ->
                            new NoSuchElementException(String.format(getNotFoundMessage(), id))
                    );
        } finally {
            lock.readLock().unlock();
        }
    }

    public void save(T entity) {
        lock.writeLock().lock();
        try {
            Long id = getId(entity);
            if (id != null && contains(id)) {
                IntStream.range(0, data.size())
                        .filter(i -> id.equals(getId(data.get(i))))
                        .findFirst()
                        .ifPresent(i -> data.set(i, entity));
            } else {
                setId(entity, maxId++);
                data.add(entity);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(Long id) {
        if (contains(id)) {
            lock.writeLock().lock();
            try {
                data = data.stream()
                        .filter(item -> !id.equals(getId(item)))
                        .toList();
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new NoSuchElementException(String.format(getNotFoundMessage(), id));
        }
    }

    protected boolean contains(Long id) {
        lock.readLock().lock();
        try {
            return data.stream()
                    .anyMatch(item -> id.equals(getId(item)));
        } finally {
            lock.readLock().unlock();
        }
    }
}

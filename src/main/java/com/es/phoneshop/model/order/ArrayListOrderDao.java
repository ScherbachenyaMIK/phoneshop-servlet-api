package com.es.phoneshop.model.order;

import com.es.phoneshop.common.GenericArrayListDao;

public class ArrayListOrderDao extends GenericArrayListDao<Order> implements OrderDao {
    private static final String ORDER_NOT_FOUND_MESSAGE = "Order with id %d not found.";

    private static final class ArrayListOrderDaoHolder {
        private static final OrderDao instance = new ArrayListOrderDao();
    }

    public static OrderDao getInstance() {
        return ArrayListOrderDao.ArrayListOrderDaoHolder.instance;
    }

    private ArrayListOrderDao() {
        maxId = 1L;
    }

    @Override
    protected Long getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Order entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected String getNotFoundMessage() {
        return ORDER_NOT_FOUND_MESSAGE;
    }
}

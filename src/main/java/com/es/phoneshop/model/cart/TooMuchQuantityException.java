package com.es.phoneshop.model.cart;

public class TooMuchQuantityException extends Exception {
    String productCode;

    int stock;

    int quantity;

    public TooMuchQuantityException(String productCode, int stock, int quantity) {
        this.productCode = productCode;
        this.stock = stock;
        this.quantity = quantity;
    }

    @Override
    public String getMessage() {
        return "For product " + productCode + " the stock is " + stock + " but requested " + quantity;
    }
}

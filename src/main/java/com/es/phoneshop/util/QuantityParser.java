package com.es.phoneshop.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public class QuantityParser {
    public static int parseQuantity(String quantity, Locale locale) throws IllegalArgumentException {
        if (quantity.isEmpty()) {
            throw new IllegalArgumentException("Quantity must not be empty");
        }

        ParsePosition pos = new ParsePosition(0);
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            Number number = numberFormat.parse(quantity, pos);
            if (pos.getIndex() < quantity.length()) {
                throw new ParseException(quantity, pos.getIndex());
            }

            if (number.intValue() < 1) {
                throw new IllegalArgumentException("Quantity must be a positive number");
            }

            if (Math.ceil(number.doubleValue()) != number.intValue()) {
                throw new IllegalArgumentException("Quantity must be an integer");
            }

            return number.intValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Quantity must be an integer", e);
        }
    }
}

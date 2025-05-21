package com.es.phoneshop.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public class PriceParser {
    public static BigDecimal parsePrice(String price, Locale locale) throws IllegalArgumentException {
        ParsePosition pos = new ParsePosition(0);
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        try {
            Number number = numberFormat.parse(price, pos);

            if (pos.getIndex() < price.length()) {
                throw new ParseException(price, pos.getIndex());
            }

            BigDecimal decimal = new BigDecimal(number.toString());

            if (decimal.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Price must be a non-negative number");
            }

            return decimal;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Price must be an integer", e);
        }
    }
}

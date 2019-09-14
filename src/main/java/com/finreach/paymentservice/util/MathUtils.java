package com.finreach.paymentservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static Double round(Double d) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(d));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (NumberFormatException e) {
            return 0d;
        }
    }
}

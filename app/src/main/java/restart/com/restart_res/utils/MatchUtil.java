package restart.com.restart_res.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/4/2.
 */

public class MatchUtil {
    public static float add(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.add(b2).floatValue();
    }

    public static float sub(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Double.toString(f1));
        BigDecimal b2 = new BigDecimal(Double.toString(f2));
        return b1.subtract(b2).floatValue();
    }
}

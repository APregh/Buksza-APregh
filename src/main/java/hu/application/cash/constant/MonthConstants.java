package hu.application.cash.constant;

import javax.enterprise.inject.Vetoed;

/**
 * Osztály, amely a hónapokat tárolja.
 *
 * @author Pregh András
 */
@Vetoed
public class MonthConstants {

    public static final String JANUARY = "Január";

    public static final String FEBRUARY = "Február";

    public static final String MARCH = "Március";

    public static final String APRIL = "Április";

    public static final String MAY = "Május";

    public static final String JUNE = "Június";

    public static final String JULY = "Július";

    public static final String AUGUST = "Augusztus";

    public static final String SEPTEMBER = "Szeptember";

    public static final String OCTOBER = "Október";

    public static final String NOVEMBER = "November";

    public static final String DECEMBER = "December";

    /**
     * Hónap lekérdezése a hónap száma alapján.
     *
     * @param i a hónap száma.
     * @return a lekérdezendő hónap
     */
    public static String get(int i) {
        switch (i) {
            case 1:
                return JANUARY;
            case 2:
                return FEBRUARY;
            case 3:
                return MARCH;
            case 4:
                return APRIL;
            case 5:
                return MAY;
            case 6:
                return JUNE;
            case 7:
                return JULY;
            case 8:
                return AUGUST;
            case 9:
                return SEPTEMBER;
            case 10:
                return OCTOBER;
            case 11:
                return NOVEMBER;
            case 12:
                return DECEMBER;
            default:
                return null;
        }
    }

    private MonthConstants() {

    }
}

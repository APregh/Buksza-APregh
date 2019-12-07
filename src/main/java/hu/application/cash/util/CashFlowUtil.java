package hu.application.cash.util;

import java.util.Map;

import javax.enterprise.inject.Vetoed;

import hu.application.cash.constant.MonthConstants;

/**
 * Util osztály, amely a cash-flow-riport-hoz nyújt segítséget.
 *
 * @author Pregh András
 */
@Vetoed
public class CashFlowUtil {

    /**
     * A kiadást alakítja át megjelenítési formára.
     *
     * @param amount a kiadás
     * @return a kiadásnál használt megjelenítési forma
     */
    public static String toCostLabel(Long amount) {
        return "- " + amount + " Ft";
    }

    /**
     * A bevételt alakítja át megjelenítési formára.
     *
     * @param amount a bevétel
     * @return a bevételnél használt megjelenítési forma
     */
    public static String toIncomeLabel(Long amount) {
        return "+ " + amount + " Ft";
    }

    /**
     * A végösszeget alakítja át megjelenítési formára.
     *
     * @param amount a végösszeg
     * @return a végösszegnél használt megjelenítési forma
     */
    public static String toTotalLabel(Long amount) {
        return amount < 0 ? toCostLabel(Math.abs(amount)) : toIncomeLabel(amount);
    }

    /**
     * Dátum megjelenítési formára alakítás.
     *
     * @param year év
     * @param i    a hónap száma
     * @return dátum megjelenítési forma
     */
    public static String toDateLabel(int year, int i) {
        return year + ". " + MonthConstants.get(i);
    }

    /**
     * Dátum megjelenítési formára alakítás.
     *
     * @param year  év
     * @param month a hónap
     * @return dátum megjelenítési forma
     */
    public static String toDateLabel(int year, String month) {
        return year + ". " + month;
    }

    /**
     * A bevétel megszámolása a paraméterül adott map alapján.
     *
     * @param incomeMap map, amely a bevételeket tartalmazza hónapokra bontva.
     * @return a bevételek összege
     */
    public static Long countIncomeAmount(Map<Integer, Long> incomeMap) {
        Long incomeAmount = 0L;
        for (int i = 1; i <= 12; i++) {
            incomeAmount += incomeMap.get(i);
        }
        return incomeAmount;
    }

    /**
     * A kiadások megszámolása a paraméterül adott map alapján
     *
     * @param costMap map, amely a kiadásokat tartalmazza hónapokra bontva
     * @return a kiadások összege
     */
    public static Long countCostAmount(Map<Integer, Long> costMap) {
        Long costAmount = 0L;
        for (int i = 1; i <= 12; i++) {
            costAmount += Math.abs(costMap.get(i));
        }
        return costAmount;
    }

    private CashFlowUtil() {

    }
}

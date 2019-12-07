package hu.application.cash.model.entity.enums;

/**
 * Enum osztály a tranzakciók típusának reprezentálásához.
 *
 * @author Pregh András
 */
public enum TransactionType {

    /**
     * Bevétel
     */
    INCOME,

    /**
     * Kiadás
     */
    COST;

    /**
     * A {@link TransactionType} enumot magyar {@link String}-re convertálja át.
     *
     * @param transactionType a {@link TransactionType}
     * @return a {@link TransactionType} magyar megfelelője.
     */
    public static String convert(TransactionType transactionType) {
        switch (transactionType) {
            case COST:
                return "Kiadás";
            case INCOME:
                return "Bevétel";
            default:
                return "";
        }
    }

    /**
     * {@link String}-et {@link TransactionType}-ra convertálja át.
     *
     * @param transactionType a {@link TransactionType} magyar megfelelője.
     * @return {@link TransactionType}
     */
    public static TransactionType convert(String transactionType) {
        if (transactionType.equals("Kiadás")) {
            return TransactionType.COST;
        }
        if (transactionType.equals("Bevétel")) {
            return TransactionType.INCOME;
        }
        return null;
    }
}

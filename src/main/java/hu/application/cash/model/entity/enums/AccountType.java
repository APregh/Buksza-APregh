package hu.application.cash.model.entity.enums;

/**
 * Enum osztály a fiókok típusának reprezentálásához.
 *
 * @author Pregh András
 */
public enum AccountType {

    /**
     * Készpénz
     */
    CASH,

    /**
     * Bankkártya
     */
    CREDIT_CARD,

    /**
     * Paypal
     */
    PAYPAL;

    /**
     * {@link AccountType} enumból magyar {@link String}-et készít.
     *
     * @param accountType {@link AccountType}
     * @return az {@link AccountType} magyar megfelelője
     */
    public static String convert(AccountType accountType) {
        switch (accountType) {
            case CASH:
                return "készpénz";
            case CREDIT_CARD:
                return "bankkártya";
            case PAYPAL:
                return "paypal";
            default:
                return "készpénz";
        }
    }

    /**
     * String-ből {@link AccountType} enumot készít.
     *
     * @param accountType {@link AccountType} magyar megfelelője {@link String}-ként.
     * @return {@link AccountType}
     */
    public static AccountType convert(String accountType) {
        if (accountType.equals(convert(AccountType.CASH))) {
            return AccountType.CASH;
        }
        if (accountType.equals(convert(AccountType.CREDIT_CARD))) {
            return AccountType.CREDIT_CARD;
        }
        if (accountType.equals(convert(AccountType.PAYPAL))) {
            return AccountType.PAYPAL;
        }
        return AccountType.CASH;
    }

}

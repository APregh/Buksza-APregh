package hu.application.cash.constant;

import javax.enterprise.inject.Vetoed;

/**
 * Osztály, amelyben az FXMl URL-k kerülnek tárolásra.
 *
 * @author Pregh András
 */
@Vetoed
public class FxmlUrlConstants {

    private static final String FXML_URL = "/view/fxml";

    /**
     * A home.fxml elérési útja.
     */
    public static final String HOME_FXML_URL = FXML_URL + "/home.fxml";

    /**
     * Az account.fxml elérési útja.
     */
    public static final String ACCOUNT_FXML_URL = FXML_URL + "/account.fxml";

    /**
     * A transaction.fxml elérési útja.
     */
    public static final String TRANSACTION_FXML_URL = FXML_URL + "/transaction.fxml";

    /**
     * A recommendtransaction.fxml elérési útja.
     */
    public static final String RECOMMEND_TRANSACTION_FXML_URL = FXML_URL + "/recommendtransaction.fxml";


    /**
     * A transaction-edit.fxml elérési útja.
     */
    public static final String TRANSACTION_EDIT_FXML_URL = FXML_URL + "/transaction-edit.fxml";

    /**
     * A riport.fxml elérési útja.
     */
    public static final String RIPORT_FXML_URL = FXML_URL + "/cash-flow-riport.fxml";

    /**
     * A transaction-category.fxml elérési útja.
     */
    public static final String TRANSACTION_CATEGORY_FXML_URL = FXML_URL + "/transaction-category.fxml";

    private FxmlUrlConstants() {

    }
}

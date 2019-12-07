package hu.application.cash.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import hu.application.cash.constant.FxmlUrlConstants;

/**
 * Controller osztály, amely a home.fxml-t vezérli.
 *
 * @author Pregh András
 */
public class HomeController extends BaseController implements Initializable {

    @Inject
    private EntityManager entityManager;

    /**
     * Ez az a controller, amely az elinduláskor vár minket, így az entityManager itt lévő Injectálásával, az indulás
     * előtt felszedi a szükséges adatokat a persistence.xml-ből, nem pedig később egy funkció végrehajtása előtt.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManager.clear();
    }

    /**
     * Betölti az account.fxml-t.
     */
    @FXML
    private void accountButtonClick() {
        changeScreen(FxmlUrlConstants.ACCOUNT_FXML_URL);
    }

    /**
     * Betölti a transaction.fxml-t.
     */
    @FXML
    private void transactionButtonClick() {
        changeScreen(FxmlUrlConstants.TRANSACTION_FXML_URL);
    }

    /**
     * Betölti a cash-flow-riport.fxml-t.
     */
    @FXML
    private void riportButtonClick() {
        changeScreen(FxmlUrlConstants.RIPORT_FXML_URL);
    }

    /**
     * Bezárja az alkalmazást.
     */
    @FXML
    private void exitButtonClick() {
        closeStage();
    }


}

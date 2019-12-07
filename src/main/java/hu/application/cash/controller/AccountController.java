package hu.application.cash.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;

import hu.application.cash.constant.FxmlUrlConstants;
import hu.application.cash.model.entity.Account;
import hu.application.cash.model.entity.enums.AccountType;
import hu.application.cash.model.pojo.AccountPojo;
import hu.application.cash.service.AccountService;

/**
 * Controller osztály, amely a fiókok kezelését végzi el.
 * Ez az osztály vezéri az account.fxml-t.
 *
 * @author Pregh András
 */
public class AccountController extends BaseController implements Initializable {

    private Account selectedAccount;

    @Inject
    private AccountService accountService;

    @FXML//fx:id hivatkozás
    private Button deleteButton;

    @FXML
    private TextField accountNameInput;

    @FXML
    private TextField amountInput;

    @FXML
    private ChoiceBox<String> accountTypeInput;

    @FXML
    private TableView<AccountPojo> accountTable;

    @FXML
    private TableColumn<AccountPojo, String> accountNameCol;

    @FXML
    private TableColumn<AccountPojo, String> accountTypeCol;

    @FXML
    private TableColumn<AccountPojo, Double> accountAmountCol;

    /**
     * Inicializálja az account.fxml oldalon lévő mezőket és elemeket.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountService.refreshAccounts();
        initAccountTypeBox();
        initAccountTableColumns();
        initAccountTable();
        deleteButton.setVisible(false);
    }

    /**
     * Fiók mentése.
     * Ha van ki választva fiók, akkor azon fiók szerkesztése történik meg.
     * Ha nincs fiók kiválasztva, akkor új fiók mentése történik meg.
     */
    @FXML
    private void saveAccount() {
        Account account = selectedAccount != null ? selectedAccount : new Account();
        account.setAccountName(accountNameInput.getText());
        account.setAmount(Long.valueOf(amountInput.getText()));
        account.setAccountType(AccountType.convert(accountTypeInput.getSelectionModel().getSelectedItem()));
        accountService.save(account);
        initAccountTable();
        clearEditPane();
    }

    /**
     * Fiók kiválasztása.
     * Ha van már kiválasztva fiók és ugyan azt választjuk ki, akkor törlődik a kiválasztás.
     * Ha van már kiválasztva fiók és nem ugyan azt választjuk ki, akkor a másik kerül kiválasztásra.
     */
    @FXML
    private void selectAccount() {
        if (accountTable.getSelectionModel().getSelectedItem() != null) {
            Account account = accountService.findById(accountTable.getSelectionModel().getSelectedItem().getId());
            if (selectedAccount != null && selectedAccount.getId().compareTo(account.getId()) == 0) {
                accountTable.getSelectionModel().clearSelection();
                clearEditPane();
            } else {
                selectedAccount = account;
                fillEditPane(selectedAccount);
                deleteButton.setVisible(true);
            }

        }
    }

    /**
     * Kiválasztott fiók törlése.
     */
    @FXML
    private void deleteAccount() {
        if (selectedAccount != null) {
            ButtonType yesButton = new ButtonType("Igen");
            ButtonType cancelButton = new ButtonType("Mégsem");
            Optional<ButtonType> result =
                    confirmationAlert(yesButton, cancelButton, "Fiók törlése", "Biztosan törölni szeretné a fiókot?").showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                accountService.delete(selectedAccount);
                initAccountTable();
                clearEditPane();
            }
        }
    }

    /**
     * Vissza a fő oldalra.
     */
    @FXML
    private void backToHome() {
        changeScreen(FxmlUrlConstants.HOME_FXML_URL);
    }

    private void fillEditPane(Account account) {
        accountNameInput.setText(account.getAccountName());
        accountTypeInput.getSelectionModel().select(getAccountTypeIndex(account.getAccountType()));
        amountInput.setText(String.valueOf(account.getAmount()));
    }

    private void clearEditPane() {
        selectedAccount = null;
        accountNameInput.clear();
        accountTypeInput.getSelectionModel().clearSelection();
        amountInput.clear();
        deleteButton.setVisible(false);
    }

    private int getAccountTypeIndex(AccountType accountType) {
        return accountTypeInput.getItems().indexOf(AccountType.convert(accountType));
    }

    private void initAccountTypeBox() {
        for (AccountType accountType : AccountType.values()) {
            accountTypeInput.getItems().add(AccountType.convert(accountType));
        }
    }

    private void initAccountTable() {
        accountTable.getItems().clear();
        for (Account account : accountService.findAll()) {
            accountTable.getItems().add(toAccountPojo(account));
        }
    }

    private void initAccountTableColumns() {
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        accountAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private AccountPojo toAccountPojo(Account account) {
        AccountPojo accountPojo = new AccountPojo();
        accountPojo.setId(account.getId());
        accountPojo.setAccountName(account.getAccountName());
        accountPojo.setAccountType(AccountType.convert(account.getAccountType()));
        accountPojo.setAmount(account.getAmount() + " Ft");//fgv ami feldarabolja
        return accountPojo;
    }
}

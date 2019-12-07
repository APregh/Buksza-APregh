package hu.application.cash.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import hu.application.cash.constant.FxmlUrlConstants;
import hu.application.cash.model.entity.Account;
import hu.application.cash.model.entity.Transaction;
import hu.application.cash.model.entity.TransactionCategory;
import hu.application.cash.model.entity.enums.TransactionType;
import hu.application.cash.service.AccountService;
import hu.application.cash.service.TransactionCategoryService;
import hu.application.cash.service.TransactionService;

/**
 * Controller osztály, amely a transaction-edit.fxml-t vezérli.
 *
 * @author Pregh András
 */
public class TransactionEditController extends BaseController implements Initializable {

    @Inject
    private EntityManager entityManager;

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionCategoryService transactionCategoryService;

    @FXML
    private Button transactionDeleteButton;

    @FXML
    private TextField transactionNameInput;

    @FXML
    private ChoiceBox<Account> accountInputBox;

    @FXML
    private ChoiceBox<TransactionCategory> transactionCategoryInputBox;

    @FXML
    private ChoiceBox<String> transactionTypeInputBox;

    @FXML
    private TextField amountInput;

    @FXML
    private DatePicker paymentDeadlineInput;

    @FXML
    private ChoiceBox<String> repeatInputBox;

    /**
     * Inicializálja az transaction-edit.fxml oldalon lévő mezőket és elemeket.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAccountBox();
        initTransactionCategoryBox();
        initTypeBox();
        initRepeatInputBox();
        initInputFields();
    }

    /**
     * Tranzakció(k) mentése.
     * Ha az ismétlődés ki van választva, akkor az adott értékekkel hónapokkal eltolva menti le a többit.
     */
    @FXML
    private void saveTransaction() {
        if (Objects.nonNull(accountInputBox.getSelectionModel().getSelectedItem())
                && Objects.nonNull(transactionCategoryInputBox.getSelectionModel().getSelectedItem())
                && Objects.nonNull(transactionTypeInputBox.getSelectionModel().getSelectedItem())
                && StringUtils.isNotBlank(amountInput.getText())
                && StringUtils.isNotBlank(transactionNameInput.getText())
                && Objects.nonNull(paymentDeadlineInput)) {
            Transaction transaction = new Transaction();
            transaction.setTransactionName(transactionNameInput.getText());
            transaction.setAccount(accountInputBox.getSelectionModel().getSelectedItem());
            transaction.setTransactionCategory(transactionCategoryInputBox.getSelectionModel().getSelectedItem());
            transaction.setTransactionType(TransactionType.convert(transactionTypeInputBox.getSelectionModel().getSelectedItem()));
            transaction.setAmount(Long.valueOf(amountInput.getText()));
            transaction.setCreationDate(Date.valueOf(LocalDate.now()));
            transaction.setPaymentDeadline(Date.valueOf(paymentDeadlineInput.getValue()));

            if (Objects.nonNull(TransactionController.getSelectedTransaction())) {
                transaction.setId(TransactionController.getSelectedTransaction().getId());
            }

            transactionService.save(transaction);

            if (StringUtils.isNotBlank(repeatInputBox.getSelectionModel().getSelectedItem())) {
                int repeatNumber = repeatInputBox.getSelectionModel().getSelectedIndex() + 1;
                for (int i = 0; i < repeatNumber; i++) {
                    transaction.setId(null);
                    transaction.setCreationDate(Date.valueOf(transaction.getCreationDate().toLocalDate().plusMonths(1)));
                    transaction.setPaymentDeadline(Date.valueOf(transaction.getPaymentDeadline().toLocalDate().plusMonths(1)));
                    entityManager.clear(); // az entityManager memóriájának üritése, hogy ne akadjanok össze az adatok
                    transactionService.save(transaction);
                }
            }

            successAlert("Tranzakció mentés", "A tranzakció(k) mentése sikeres volt!").showAndWait();

            if (Objects.isNull(TransactionController.getSelectedTransaction())) {
                clearFields();
            }
        }
    }

    /**
     * A kiválasztott tranzakció törlése. Ez a {@link TransactionController} osztálytól kerül lekérdezésre.
     */
    @FXML
    private void deleteTransaction() {
        Transaction selectedTransaction = TransactionController.getSelectedTransaction();
        ButtonType yesButton = new ButtonType("Igen");
        ButtonType cancelButton = new ButtonType("Mégsem");
        Optional<ButtonType> result =
                confirmationAlert(yesButton, cancelButton, "Tranzakció törlés", "Biztosan törölni szeretné a tranzakciót?").showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            transactionService.delete(selectedTransaction);
            backToTransaction();
        }
    }

    /**
     * Kategória létrehozó oldal megnyitása
     */
    @FXML
    private void openCategoryCreatePage() {
        openScreen(FxmlUrlConstants.TRANSACTION_CATEGORY_FXML_URL);
        initTransactionCategoryBox();
    }

    /**
     * Vissza a transaction.fxml oldalra.
     */
    @FXML
    private void backToTransaction() {
        changeScreen(FxmlUrlConstants.TRANSACTION_FXML_URL);
    }

    private void initInputFields() {
        Transaction transaction = TransactionController.getSelectedTransaction();
        if (Objects.nonNull(transaction)) {
            transactionNameInput.setText(transaction.getTransactionName());
            accountInputBox.getSelectionModel().select(transaction.getAccount());
            transactionCategoryInputBox.getSelectionModel().select(transaction.getTransactionCategory());
            transactionTypeInputBox.getSelectionModel().select(TransactionType.convert(transaction.getTransactionType()));
            amountInput.setText(String.valueOf(transaction.getAmount()));
            paymentDeadlineInput.setValue(transaction.getPaymentDeadline().toLocalDate());
            transactionDeleteButton.setVisible(true);
        } else {
            transactionDeleteButton.setVisible(false);
        }
    }

    private void initRepeatInputBox() {
        for (int i = 0; i < 12; i++) {
            repeatInputBox.getItems().add(i + 1 + " hónap");
        }
    }

    private void initTransactionCategoryBox() {
        transactionCategoryInputBox.getItems().clear();
        List<TransactionCategory> mainCategories = transactionCategoryService.findAllByParentNull();
        for (TransactionCategory transactionCategory : mainCategories) {
            List<TransactionCategory> transactionCategories = transactionCategoryService.findAllByParentId(transactionCategory.getId());
            transactionCategoryInputBox.getItems().add(transactionCategory);
            transactionCategoryInputBox.getItems().addAll(transactionCategories);
            entityManager.clear();
        }
    }

    private void initTypeBox() {
        transactionTypeInputBox.getItems().clear();
        for (TransactionType transactionType : TransactionType.values()) {
            transactionTypeInputBox.getItems().add(TransactionType.convert(transactionType));
        }
    }

    private void initAccountBox() {
        accountInputBox.getItems().clear();
        accountInputBox.getItems().addAll(accountService.findAll());
    }

    private void clearFields() {
        transactionNameInput.setText(null);
        accountInputBox.getSelectionModel().clearSelection();
        transactionCategoryInputBox.getSelectionModel().clearSelection();
        transactionTypeInputBox.getSelectionModel().clearSelection();
        amountInput.setText(null);
        paymentDeadlineInput.setValue(null);
        repeatInputBox.getSelectionModel().clearSelection();
    }
}

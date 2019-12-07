package hu.application.cash.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import hu.application.cash.constant.FxmlUrlConstants;
import hu.application.cash.model.entity.Account;
import hu.application.cash.model.entity.Transaction;
import hu.application.cash.model.entity.TransactionCategory;
import hu.application.cash.model.entity.enums.TransactionType;
import hu.application.cash.model.pojo.TransactionPojo;
import hu.application.cash.service.AccountService;
import hu.application.cash.service.TransactionCategoryService;
import hu.application.cash.service.TransactionService;
import hu.application.cash.util.TransactionExcelExportUtil;

/**
 * Controller osztály, amely a transaction.fxml-t vezérli.
 *
 * @author Pregh András
 */
public class TransactionController extends BaseController implements Initializable {

    private static Transaction selectedTransaction;

    @Inject
    private EntityManager entityManager;

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionCategoryService transactionCategoryService;

    /**
     * Betölti a cash-flow-riport.fxml-t.
     */
    @FXML
    private void riportButtonClick() {
        changeScreen(FxmlUrlConstants.RIPORT_FXML_URL);
    }

    @FXML
    private TableView<TransactionPojo> transactionTable;

    @FXML
    private TableColumn<TransactionPojo, String> transactionNameCol;

    @FXML
    private TableColumn<TransactionPojo, String> accountNameCol;

    @FXML
    private TableColumn<TransactionPojo, String> transactionCategoryCol;

    @FXML
    private TableColumn<TransactionPojo, String> transactionTypeCol;

    @FXML
    private TableColumn<TransactionPojo, String> amountCol;

    @FXML
    private TableColumn<TransactionPojo, String> creationDateCol;

    @FXML
    private TableColumn<TransactionPojo, String> paymentDeadlineCol;

    @FXML
    private ChoiceBox<TransactionCategory> categoryFilterBox;

    @FXML
    private ChoiceBox<String> typeFilterBox;

    @FXML
    private ChoiceBox<Account> accountFilterBox;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button editTransactionButton;

    @FXML
    private PieChart transactionPieChart;

    /**
     * Inicializálja a transaction.fxml oldalon lévő mezőket és elemeket.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTransactionCategoryFilterBox();
        initTypeFilerBox();
        initAccountFilterBox();
        initTransactionTableColumns();
        initTransactionTable();
        initPieChart();
        editTransactionButton.setVisible(false);
        setSelectedTransaction(null);
    }

    /**
     * A kiválasztott {@link Transaction} beállítása.
     *
     * @param transaction a kiválasztott {@link Transaction}
     */
    public static void setSelectedTransaction(Transaction transaction) {
        selectedTransaction = transaction;
    }

    /**
     * A kiválasztott {@link Transaction} lekérdezése.
     * Erre a szerkesztés során, a transaction-edit.fxml-ben van szükség.
     *
     * @return a kiválasztott {@link Transaction}
     */
    public static Transaction getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * A szűrt tranzakciók exportálása excel-be.
     *
     * @throws IOException
     */
    @FXML
    private void exportTransactions() throws IOException {
        TransactionExcelExportUtil.generateTransactionXls(new ArrayList<>(transactionTable.getItems()));
        successAlert("Tranzakciók excel exportálás", "A tranzakciók excel exportálása sikeres volt!").showAndWait();
        //generateRecommend(new ArrayList<>(transactionTable.getItems()));
        //System.out.println(transactionTable.getItems());
    }

    /**
     * Tranzakció kiválasztása.
     * Ha már van egy tranzakció kiválasztva és ugyan arra kattintunk, akkor nem lesz kiválasztva.
     * Ha már van egy tranzakció kiválasztva és más tranzakcióra kattintunk, akkor az utóbbi kerül kiválasztásra.
     */
    @FXML
    private void selectTransaction() {
        if (Objects.nonNull(transactionTable.getSelectionModel().getSelectedItem())) {
            TransactionPojo transactionPojo = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null && selectedTransaction.getId().compareTo(transactionPojo.getId()) == 0) {
                setSelectedTransaction(null);
                transactionTable.getSelectionModel().clearSelection();
                editTransactionButton.setVisible(false);
            } else {
                setSelectedTransaction(transactionService.findById(transactionTable.getSelectionModel().getSelectedItem().getId()));
                editTransactionButton.setVisible(true);
            }
        }
    }

    /**
     * Tranzakciók szűrése a szűrési mezőkben megadott paraméterek szerint.
     * A szűrési paraméterek: fiók, kategória, tranzakció típusa, fizetési határidő -től és -ig
     */
    @FXML
    private void filtering() {
        Long accountId = accountFilterBox.getSelectionModel().getSelectedItem() != null
                ? accountFilterBox.getSelectionModel().getSelectedItem().getId()
                : null;
        Long categoryId = categoryFilterBox.getSelectionModel().getSelectedItem() != null
                ? categoryFilterBox.getSelectionModel().getSelectedItem().getId()
                : null;
        TransactionType transactionType = typeFilterBox.getSelectionModel().getSelectedItem() != null
                ? TransactionType.convert(typeFilterBox.getSelectionModel().getSelectedItem())
                : null;
        Date fromDate = dateFrom.getValue() != null ? Date.valueOf(dateFrom.getValue()) : null;
        Date toDate = dateTo.getValue() != null ? Date.valueOf(dateTo.getValue()) : null;
        List<Transaction> filteredTransactions = transactionService.findAllByFilters(accountId, categoryId, transactionType, fromDate, toDate);
        transactionTable.getItems().clear();
        for (Transaction transaction : filteredTransactions) {
            transactionTable.getItems().add(toTransactionPojo(transaction));
        }
        initPieChart();
    }

    /**
     * Tranzakciók szűrése a szűrési mezőkben megadott paraméterek szerint.
     * A szűrési paraméterek: fiók, kategória, tranzakció típusa, fizetési határidő -től és -ig
     */
    @FXML
    private void filteringByRecommendation() {
        Long accountId = accountFilterBox.getSelectionModel().getSelectedItem() != null
                ? accountFilterBox.getSelectionModel().getSelectedItem().getId()
                : null;
        Long categoryId = categoryFilterBox.getSelectionModel().getSelectedItem() != null
                ? categoryFilterBox.getSelectionModel().getSelectedItem().getId()
                : null;
        TransactionType transactionType = TransactionType.convert("Kiadás");
        //TransactionType transactionType = typeFilterBox.getSelectionModel().getSelectedItem() != null
        //      ? TransactionType.convert(typeFilterBox.getSelectionModel().getSelectedItem())
        //    : null;
        Date fromDate = dateFrom.getValue() != null ? Date.valueOf(dateFrom.getValue()) : null;
        Date toDate = dateTo.getValue() != null ? Date.valueOf(dateTo.getValue()) : null;
        List<Transaction> filteredTransactions = transactionService.findAllByFilters(accountId, categoryId, transactionType, fromDate, toDate);
        transactionTable.getItems().clear();
        for (Transaction transaction : filteredTransactions) {
            transactionTable.getItems().add(toTransactionPojo(transaction));
        }
        initPieChart();
    }

    /**
     * A kiválasztott tranzakció szerkesztése oldalra ugrás.
     */
    @FXML
    private void goToTransactionEdit() {
        changeScreen(FxmlUrlConstants.TRANSACTION_EDIT_FXML_URL);
    }

    /**
     * Új tranzakció létrehozása oldalra navigál.
     * Mivel ez ugyan az mint a szerkesztés oldal, ezért ha mondjuk lenne kiválasztva tranzakció, akkor azt szerkesztené,
     * ezért a kiválasztott tranzakciót nullázni kell.
     */
    @FXML
    private void goToNewTransaction() {
        setSelectedTransaction(null);
        changeScreen(FxmlUrlConstants.TRANSACTION_EDIT_FXML_URL);
    }

    /**
     * Vissza a főoldalra.
     */
    @FXML
    private void backToHome() {
        changeScreen(FxmlUrlConstants.HOME_FXML_URL);
    }

    private void initTransactionCategoryFilterBox() {
        categoryFilterBox.getItems().clear();
        categoryFilterBox.getItems().add(null);
        List<TransactionCategory> mainCategories = transactionCategoryService.findAllByParentNull();
        for (TransactionCategory transactionCategory : mainCategories) {
            List<TransactionCategory> transactionCategories = transactionCategoryService.findAllByParentId(transactionCategory.getId());
            categoryFilterBox.getItems().add(transactionCategory);
            categoryFilterBox.getItems().addAll(transactionCategories);
            entityManager.clear();
        }
    }

    private void initTypeFilerBox() {
        typeFilterBox.getItems().clear();
        typeFilterBox.getItems().add(null);
        for (TransactionType transactionType : TransactionType.values()) {
            typeFilterBox.getItems().add(TransactionType.convert(transactionType));
        }
    }

    private void initAccountFilterBox() {
        accountFilterBox.getItems().clear();
        accountFilterBox.getItems().add(null);
        accountFilterBox.getItems().addAll(accountService.findAll());
    }

    private void initTransactionTable() {
        transactionTable.getItems().clear();
        for (Transaction transaction : transactionService.findAll()) {
            transactionTable.getItems().add(toTransactionPojo(transaction));
        }
    }

    private void initTransactionTableColumns() {
        transactionNameCol.setCellValueFactory(new PropertyValueFactory<>("transactionName"));
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        transactionCategoryCol.setCellValueFactory(new PropertyValueFactory<>("transactionCategory"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        paymentDeadlineCol.setCellValueFactory(new PropertyValueFactory<>("paymentDeadline"));
    }

    private void initPieChart() {
        transactionPieChart.getData().clear();
        List<TransactionPojo> transactionPojos = transactionTable.getItems();

        List<Long> filteredTransactionCategoryIds = new ArrayList<>();
        for (TransactionPojo transactionPojo : transactionPojos) {
            if (filteredTransactionCategoryIds.stream().noneMatch(categoryId -> categoryId.compareTo(transactionPojo.getTransactionCategoryId()) == 0)) {
                filteredTransactionCategoryIds.add(transactionPojo.getTransactionCategoryId());
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Long categoryId : filteredTransactionCategoryIds) {
            PieChart.Data data = new PieChart.Data(
                    getTransactionCategoryNameById(transactionPojos, categoryId),
                    (double) sumByCategoryId(transactionPojos, categoryId)
            );
            pieChartData.add(data);
        }
        transactionPieChart.getData().addAll(pieChartData);
    }


    private Long sumByCategoryId(List<TransactionPojo> transactionPojos, Long categoryId) {
        Long sum = 0L;
        for (TransactionPojo transactionPojo : transactionPojos) {
            if (transactionPojo.getTransactionCategoryId().compareTo(categoryId) == 0) {
                sum += transactionService.findById(transactionPojo.getId()).getAmount();
                entityManager.clear();
            }
        }
        return sum;
    }

    private String getTransactionCategoryNameById(List<TransactionPojo> transactionPojos, Long categoryId) {
        return transactionPojos.stream().filter(t -> t.getTransactionCategoryId().compareTo(categoryId) == 0).findFirst().get().getTransactionCategory();
    }

    private TransactionPojo toTransactionPojo(Transaction transaction) {
        TransactionPojo transactionPojo = new TransactionPojo();
        transactionPojo.setId(transaction.getId());
        transactionPojo.setTransactionName(transaction.getTransactionName());
        transactionPojo.setAccountName(transaction.getAccount().getAccountName());
        transactionPojo.setTransactionCategory(transaction.getTransactionCategory().getCategoryName());
        transactionPojo.setTransactionCategoryId(transaction.getTransactionCategory().getId());
        transactionPojo.setTransactionType(TransactionType.convert(transaction.getTransactionType()));
        transactionPojo.setAmount(transaction.getAmount() + " Ft");
        transactionPojo.setCreationDate(transaction.getCreationDate());
        transactionPojo.setPaymentDeadline(transaction.getPaymentDeadline());
        return transactionPojo;
    }

    //public class GenerateRecommendList {
        /**
         * Létrehozza a tranzakciók oldalon lévő adatokból a megfelelő excelt.
         *
         * @param transactions a tranzakciók listája
         * @throws IOException
         */
        public  void generateRecommend(List<TransactionPojo> transactions) throws IOException {
            for (TransactionPojo transaction : transactions) {


                System.out.println(transaction.getTransactionName());
                System.out.println(transaction.getAccountName());
                System.out.println(transaction.getTransactionCategory());
                System.out.println(transaction.getTransactionType());
                System.out.println(transaction.getAmount());
            }
        }
    //}
}
package hu.application.cash.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import hu.application.cash.constant.FxmlUrlConstants;
import hu.application.cash.constant.MonthConstants;
import hu.application.cash.model.entity.Account;
import hu.application.cash.model.entity.enums.TransactionType;
import hu.application.cash.service.AccountService;
import hu.application.cash.service.TransactionService;
import hu.application.cash.util.CashFlowExcelExportUtil;
import hu.application.cash.util.CashFlowUtil;

/**
 * Controller osztály, amely a cash-flow-riport.fxml-t vezérli.
 *
 * @author Pregh András
 */
public class CashFlowRiportController extends BaseController implements Initializable {

    @FXML
    private Label dateLabel1;

    @FXML
    private Label dateLabel2;

    @FXML
    private Label dateLabel3;

    @FXML
    private Label dateLabel4;

    @FXML
    private Label dateLabel5;

    @FXML
    private Label dateLabel6;

    @FXML
    private Label dateLabel7;

    @FXML
    private Label dateLabel8;

    @FXML
    private Label dateLabel9;

    @FXML
    private Label dateLabel10;

    @FXML
    private Label dateLabel11;

    @FXML
    private Label dateLabel12;

    @FXML
    private Label incomeLabel1;

    @FXML
    private Label incomeLabel2;

    @FXML
    private Label incomeLabel3;

    @FXML
    private Label incomeLabel4;

    @FXML
    private Label incomeLabel5;

    @FXML
    private Label incomeLabel6;

    @FXML
    private Label incomeLabel7;

    @FXML
    private Label incomeLabel8;

    @FXML
    private Label incomeLabel9;

    @FXML
    private Label incomeLabel10;

    @FXML
    private Label incomeLabel11;

    @FXML
    private Label incomeLabel12;

    @FXML
    private Label incomeAllLabel;

    @FXML
    private Label costLabel1;

    @FXML
    private Label costLabel2;

    @FXML
    private Label costLabel3;

    @FXML
    private Label costLabel4;

    @FXML
    private Label costLabel5;

    @FXML
    private Label costLabel6;

    @FXML
    private Label costLabel7;

    @FXML
    private Label costLabel8;

    @FXML
    private Label costLabel9;

    @FXML
    private Label costLabel10;

    @FXML
    private Label costLabel11;

    @FXML
    private Label costLabel12;

    @FXML
    private Label costAllLabel;

    @FXML
    private Label allLabel;

    @FXML
    private Label allLabel1;

    @FXML
    private Label allLabel2;

    @FXML
    private Label allLabel3;

    @FXML
    private Label allLabel4;

    @FXML
    private Label allLabel5;

    @FXML
    private Label allLabel6;

    @FXML
    private Label allLabel7;

    @FXML
    private Label allLabel8;

    @FXML
    private Label allLabel9;

    @FXML
    private Label allLabel10;

    @FXML
    private Label allLabel11;

    @FXML
    private Label allLabel12;

    @FXML
    private ChoiceBox<Account> accountBox;

    @FXML
    private TextField yearTextField;

    @FXML
    private LineChart<String, Long> lineChart;
    /**
     * Betölti a transaction.fxml-t.
     */
    @FXML
    private void transactionButtonClick() {
        changeScreen(FxmlUrlConstants.TRANSACTION_FXML_URL);
    }
    @FXML
    private void recommendTransactionButtonClick() {
        changeScreen(FxmlUrlConstants.RECOMMEND_TRANSACTION_FXML_URL);
        //filteringByRecommendation();
    }

    @Inject
    private TransactionService transactionService;

    @Inject
    private AccountService accountService;

    /**
     * Inicializálja az cash-flow-riport.fxml oldalon lévő mezőket és elemeket.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearTextField.setEditable(false);
        yearTextField.setText(String.valueOf(LocalDate.now().getYear()));
        initAccountBox();
        filtering();
    }


    /**
     * Fiók és év szerinti szűrés a kimutatásra.
     * A szűrt adatokból kerül majd a diagram összeállításra.
     */
    @FXML
    private void filtering() {
        Long accountId = getAccountId();
        int year = getYear();

        Map<Integer, Long> costMap = transactionService.sumAmountByAccountAndTransactionTypeAndYear(accountId, TransactionType.COST, year);
        Map<Integer, Long> incomeMap = transactionService.sumAmountByAccountAndTransactionTypeAndYear(accountId, TransactionType.INCOME, year);

        Map<Integer, Long> totalMap = calculateTotalAmount(costMap, incomeMap);

        setDateLabels();
        setIncomeLabels(incomeMap);
        setCostLabels(costMap);
        setTotalLabels(totalMap);

        lineChart.getData().clear();
        setDiagram(toNegativeValues(costMap), TransactionType.convert(TransactionType.COST));
        setDiagram(incomeMap, TransactionType.convert(TransactionType.INCOME));
        setDiagram(totalMap, "Egyenleg");
    }

    /**
     * A kimutatás exportálása excel-be.
     *
     * @throws IOException
     */
    @FXML
    private void exportCashFlowRiport() throws IOException {
        Long accountId = getAccountId();
        int year = getYear();

        Map<Integer, Long> incomeMap = transactionService.sumAmountByAccountAndTransactionTypeAndYear(accountId, TransactionType.INCOME, year);
        Map<Integer, Long> costMap = transactionService.sumAmountByAccountAndTransactionTypeAndYear(accountId, TransactionType.COST, year);

        Map<Integer, Long> totalMap = calculateTotalAmount(costMap, incomeMap);

        CashFlowExcelExportUtil.generateCashFlowXls(incomeMap, costMap, totalMap, year);
        successAlert("Cash Flow Riport excel exportálás", "A cash flow riport excel exportálása sikeres volt!").showAndWait();
    }

    /**
     * Az év szűrőmező csökkentése 1-el.
     */
    @FXML
    private void yearMinus() {
        int year = getYear() - 1;
        yearTextField.setText(String.valueOf(year));
    }

    /**
     * Az év szűrőmező növelése 1-el.
     */
    @FXML
    private void yearPlus() {
        int year = getYear() + 1;
        yearTextField.setText(String.valueOf(year));
    }

    /**
     * Vissza a főoldalra.
     */
    @FXML
    private void backToHome() {
        changeScreen(FxmlUrlConstants.HOME_FXML_URL);
    }


    /**
     * A diagramokat kirajzoló metódus
     */
    private void setDiagram(Map<Integer, Long> map, String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        series.getData().add(new XYChart.Data("Jan", map.get(1)));
        series.getData().add(new XYChart.Data("Feb", map.get(2)));
        series.getData().add(new XYChart.Data("Már", map.get(3)));
        series.getData().add(new XYChart.Data("Ápr", map.get(4)));
        series.getData().add(new XYChart.Data("Máj", map.get(5)));
        series.getData().add(new XYChart.Data("Jún", map.get(6)));
        series.getData().add(new XYChart.Data("Júl", map.get(7)));
        series.getData().add(new XYChart.Data("Aug", map.get(8)));
        series.getData().add(new XYChart.Data("Szep", map.get(9)));
        series.getData().add(new XYChart.Data("Okt", map.get(10)));
        series.getData().add(new XYChart.Data("Nov", map.get(11)));
        series.getData().add(new XYChart.Data("Dec", map.get(12)));

        lineChart.getData().add(series);
    }

    private void setDateLabels() {
        int year = getYear();
        dateLabel1.setText(CashFlowUtil.toDateLabel(year, MonthConstants.JANUARY));
        dateLabel2.setText(CashFlowUtil.toDateLabel(year, MonthConstants.FEBRUARY));
        dateLabel3.setText(CashFlowUtil.toDateLabel(year, MonthConstants.MARCH));
        dateLabel4.setText(CashFlowUtil.toDateLabel(year, MonthConstants.APRIL));
        dateLabel5.setText(CashFlowUtil.toDateLabel(year, MonthConstants.MAY));
        dateLabel6.setText(CashFlowUtil.toDateLabel(year, MonthConstants.JUNE));
        dateLabel7.setText(CashFlowUtil.toDateLabel(year, MonthConstants.JULY));
        dateLabel8.setText(CashFlowUtil.toDateLabel(year, MonthConstants.AUGUST));
        dateLabel9.setText(CashFlowUtil.toDateLabel(year, MonthConstants.SEPTEMBER));
        dateLabel10.setText(CashFlowUtil.toDateLabel(year, MonthConstants.OCTOBER));
        dateLabel11.setText(CashFlowUtil.toDateLabel(year, MonthConstants.NOVEMBER));
        dateLabel12.setText(CashFlowUtil.toDateLabel(year, MonthConstants.DECEMBER));
    }

    private void setIncomeLabels(Map<Integer, Long> incomeMap) {
        incomeLabel1.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(1)));
        incomeLabel2.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(2)));
        incomeLabel3.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(3)));
        incomeLabel4.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(4)));
        incomeLabel5.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(5)));
        incomeLabel6.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(6)));
        incomeLabel7.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(7)));
        incomeLabel8.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(8)));
        incomeLabel9.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(9)));
        incomeLabel10.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(10)));
        incomeLabel11.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(11)));
        incomeLabel12.setText(CashFlowUtil.toIncomeLabel(incomeMap.get(12)));
        incomeAllLabel.setText(CashFlowUtil.toIncomeLabel(CashFlowUtil.countIncomeAmount(incomeMap)));
    }

    private void setCostLabels(Map<Integer, Long> costMap) {
        costLabel1.setText(CashFlowUtil.toCostLabel(costMap.get(1)));
        costLabel2.setText(CashFlowUtil.toCostLabel(costMap.get(2)));
        costLabel3.setText(CashFlowUtil.toCostLabel(costMap.get(3)));
        costLabel4.setText(CashFlowUtil.toCostLabel(costMap.get(4)));
        costLabel5.setText(CashFlowUtil.toCostLabel(costMap.get(5)));
        costLabel6.setText(CashFlowUtil.toCostLabel(costMap.get(6)));
        costLabel7.setText(CashFlowUtil.toCostLabel(costMap.get(7)));
        costLabel8.setText(CashFlowUtil.toCostLabel(costMap.get(8)));
        costLabel9.setText(CashFlowUtil.toCostLabel(costMap.get(9)));
        costLabel10.setText(CashFlowUtil.toCostLabel(costMap.get(10)));
        costLabel11.setText(CashFlowUtil.toCostLabel(costMap.get(11)));
        costLabel12.setText(CashFlowUtil.toCostLabel(costMap.get(12)));
        costAllLabel.setText(CashFlowUtil.toCostLabel(CashFlowUtil.countCostAmount(costMap)));
    }

    private void setTotalLabels(Map<Integer, Long> totalMap) {
        allLabel1.setText(CashFlowUtil.toTotalLabel(totalMap.get(1)));
        allLabel2.setText(CashFlowUtil.toTotalLabel(totalMap.get(2)));
        allLabel3.setText(CashFlowUtil.toTotalLabel(totalMap.get(3)));
        allLabel4.setText(CashFlowUtil.toTotalLabel(totalMap.get(4)));
        allLabel5.setText(CashFlowUtil.toTotalLabel(totalMap.get(5)));
        allLabel6.setText(CashFlowUtil.toTotalLabel(totalMap.get(6)));
        allLabel7.setText(CashFlowUtil.toTotalLabel(totalMap.get(7)));
        allLabel8.setText(CashFlowUtil.toTotalLabel(totalMap.get(8)));
        allLabel9.setText(CashFlowUtil.toTotalLabel(totalMap.get(9)));
        allLabel10.setText(CashFlowUtil.toTotalLabel(totalMap.get(10)));
        allLabel11.setText(CashFlowUtil.toTotalLabel(totalMap.get(11)));
        allLabel12.setText(CashFlowUtil.toTotalLabel(totalMap.get(12)));
        //allLabel.setText(CashFlowUtil.toTotalLabel(CashFlowUtil.countIncomeAmount(totalMap)));
    }

    private int getYear() {
        return StringUtils.isNotBlank(yearTextField.getText())
                ? Integer.parseInt(yearTextField.getText())
                : LocalDate.now().getYear();
    }

    private Long getAccountId() {
        Account selectedAccount = accountBox.getSelectionModel().getSelectedItem();
        return selectedAccount != null
                ? selectedAccount.getId()
                : null;
    }

    private Map<Integer, Long> calculateTotalAmount(Map<Integer, Long> costMap, Map<Integer, Long> incomeMap) {
        Map<Integer, Long> totalMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            totalMap.put(i, incomeMap.get(i) - costMap.get(i));
        }
        return totalMap;
    }

    private Map<Integer, Long> toNegativeValues(Map<Integer, Long> costMap) {
        for (int i = 1; i <= 12; i++) {
            //costMap.put(i, Math.negateExact(costMap.get(i)));
        }
        return costMap;
    }

    private void initAccountBox() {
        accountBox.getItems().clear();
        accountBox.getItems().add(null);
        accountBox.getItems().addAll(accountService.findAll());
    }
}

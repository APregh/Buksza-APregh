package hu.application.cash.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import hu.application.cash.model.entity.TransactionCategory;
import hu.application.cash.service.TransactionCategoryService;

/**
 * Controller osztály, amely a transaction-category.fxml-t vezérli.
 *
 * @author Pregh András
 */
public class TransactionCategoryController extends BaseController implements Initializable {

    @FXML
    private TextField categoryNameInput;

    @FXML
    private ChoiceBox<TransactionCategory> parentBox;

    @FXML
    private Button closeButton;

    @Inject
    private EntityManager entityManager;

    @Inject
    private TransactionCategoryService transactionCategoryService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initParentBox();
    }

    /**
     * Létrehozza az új kategóriát.
     */
    @FXML
    private void createCategory() {
        if (StringUtils.isNotBlank(categoryNameInput.getText())) {
            TransactionCategory transactionCategory = new TransactionCategory();
            transactionCategory.setCategoryName(categoryNameInput.getText());

            TransactionCategory parent = parentBox.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(parent) && Objects.isNull(parent.getParent())) {
                transactionCategory.setParent(parent);
            }
            if (Objects.isNull(parent) || Objects.nonNull(transactionCategory.getParent())) {
                transactionCategoryService.save(transactionCategory);
                initParentBox();
                clearInputFields();
            }
        }
    }

    /**
     * Bezárja az aktuális ablakot.
     */
    @FXML
    private void closeScreen() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void clearInputFields() {
        categoryNameInput.setText(null);
        parentBox.getSelectionModel().clearSelection();
    }

    private void initParentBox() {
        parentBox.getItems().clear();
        List<TransactionCategory> mainCategories = transactionCategoryService.findAllByParentNull();
        for (TransactionCategory transactionCategory : mainCategories) {
            List<TransactionCategory> transactionCategories = transactionCategoryService.findAllByParentId(transactionCategory.getId());
            parentBox.getItems().add(transactionCategory);
            parentBox.getItems().addAll(transactionCategories);
            entityManager.clear();
        }
    }
}

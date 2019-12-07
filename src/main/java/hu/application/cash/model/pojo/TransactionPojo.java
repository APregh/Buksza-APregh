package hu.application.cash.model.pojo;

import java.util.Date;

import hu.application.cash.model.entity.Transaction;

/**
 * POJO osztály a {@link Transaction} entitás táblázatban való megjelenítéséhez.
 *
 * @author Pregh András
 */
public class TransactionPojo {

    /**
     * A {@link Transaction} elsődleges kulcsa
     */
    private Long id;

    /**
     * A tranzakció neve.
     */
    private String transactionName;

    /**
     * A fiók neve, amelyhez a tranzakció tartozik.
     */
    private String accountName;

    /**
     * A kategória magyar szövegként, amelyhez a tranzakció tartozik.
     */
    private String transactionCategory;

    /**
     * A kategória azonosítója.
     */
    private Long transactionCategoryId;

    /**
     * A tranzakció típusa magyar szövegként, amelyhez a tranzakció tartozik.
     */
    private String transactionType;

    /**
     * A tranzakció összege szövegként.
     */
    private String amount;

    /**
     * Létrehozás dátuma.
     */
    private Date creationDate;

    /**
     * Fizetési határidő.
     */
    private Date paymentDeadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Long getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(Long transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPaymentDeadline() {
        return paymentDeadline;
    }

    public void setPaymentDeadline(Date paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }
}

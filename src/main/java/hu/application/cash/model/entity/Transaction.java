package hu.application.cash.model.entity;

import java.sql.Date;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import hu.application.cash.model.entity.enums.TransactionType;

/**
 * Entitás osztály a tranzakciók reprezentálásához.
 *
 * @author Pregh András
 */
@Vetoed
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    /**
     * Az adatbázisban lévő azonosító, az elsődleges kulcs.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * A tranzakció neve.
     */
    @NotNull
    @Column(name = "TRANSACTION_NAME", nullable = false)
    private String transactionName;

    /**
     * A fiók, amelyhez a tranzakció tartozik.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    /**
     * A tranzakció típusa.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE", nullable = false)
    private TransactionType transactionType;

    /**
     * A tranzakció kategóriája.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_CATEGORY_ID", nullable = false)
    private TransactionCategory transactionCategory;

    /**
     * A tranzakcióban lévő pénzmozgás összege.
     */
    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    /**
     * A tranzakció létrehozásának dátuma.
     */
    @NotNull
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    /**
     * A fizetési határidő.
     */
    @NotNull
    @Column(name = "PAYMENT_DEADLINE", nullable = false)
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
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

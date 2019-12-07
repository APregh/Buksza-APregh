package hu.application.cash.model.entity;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import hu.application.cash.model.entity.enums.AccountType;

/**
 * Entitás osztály a fiókok reprezentálásához.
 *
 * @author Pregh András
 */
@Vetoed
@Entity
@Table(name = "ACCOUNT")
public class Account {

    /**
     * Az adatbázisban lévő azonosító, az elsődleges kulcs.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * A fiók neve.
     */
    @NotNull
    @Column(name = "ACCOUNT_NAME", nullable = false)
    private String accountName;

    /**
     * A fiókhoz tartozó összeg.
     */
    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    /**
     * A fiók típusa.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private AccountType accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**
     * Azért csak a nevet adjuk vissza, mert a ChoiceBox-nál ezt kell megjeleníteni.
     *
     * @return
     */
    @Override
    public String toString() {
        return this.accountName;
    }
}

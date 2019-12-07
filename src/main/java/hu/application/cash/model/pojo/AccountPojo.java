package hu.application.cash.model.pojo;

import hu.application.cash.model.entity.Account;

/**
 * POJO osztály, a {@link Account} entitás TableView-ban való megjelenítéséhez.
 *
 * @author Pregh András
 */
public class AccountPojo {

    /**
     * A fiók elsődleges kulcsa.
     */
    private Long id;

    /**
     * A fiók neve.
     */
    private String accountName;

    /**
     * A fiók típusa magyar szövegként.
     */
    private String accountType;

    /**
     * A fiók összege {@link String}-ként.
     */
    private String amount;

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

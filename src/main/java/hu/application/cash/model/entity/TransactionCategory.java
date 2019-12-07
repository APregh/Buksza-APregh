package hu.application.cash.model.entity;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entitás osztály a tranzakciókhoz tartozó kategóriák reprezentálásához.
 *
 * @author Pregh András
 */
@Vetoed
@Entity
@Table(name = "TRANSACTION_CATEGORY")
public class TransactionCategory {

    /**
     * Az adatbázisban lévő azonosító, az elsődleges kulcs.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * A kategória neve
     */
    @NotNull
    @Column(name = "CATEGORY_NAME", nullable = false)
    private String categoryName;

    /**
     * Szülő kategória.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private TransactionCategory parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public TransactionCategory getParent() {
        return parent;
    }

    public void setParent(TransactionCategory parent) {
        this.parent = parent;
    }

    /**
     * Azért csak a kategória nevét adjuk vissza, mert a ChoiceBox-ban, csak ezt akarjuk megjeleníteni.
     *
     * @return a kategória neve.
     */
    @Override
    public String toString() {
        return this.parent == null ? this.categoryName : "    " + this.categoryName;
    }
}

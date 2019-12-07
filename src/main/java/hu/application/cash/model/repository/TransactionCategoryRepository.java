package hu.application.cash.model.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import hu.application.cash.model.entity.TransactionCategory;

/**
 * Repository interface a {@link TransactionCategory} entitáshoz.
 *
 * @author Pregh András
 */
@Repository
public interface TransactionCategoryRepository extends EntityRepository<TransactionCategory, Long> {

    /**
     * Az összes {@link TransactionCategory} lekérdezése, ahol a szülő null.
     *
     * @return azon tranzakció kategóriák, ahol a szülő null.
     */
    @Query("SELECT t FROM TransactionCategory t WHERE t.parent = null")
    List<TransactionCategory> findAllByParentNull();

    /**
     * Az összes {@link TransactionCategory} lekérdezése szülő azonosító alapján.
     *
     * @param parentId a szülő kategória azonosítója
     * @return tranzakció kategóriák szülő azonosító alapján.
     */
    @Query("SELECT t FROM TransactionCategory t WHERE t.parent.id = ?1")
    List<TransactionCategory> findAllByParentId(Long parentId);
    
}

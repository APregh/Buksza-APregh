package hu.application.cash.model.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import hu.application.cash.model.entity.Transaction;

/**
 * Repository interface a {@link Transaction} entitás kezeléséhez.
 *
 * @author Pregh András
 */
@Repository
public interface TransactionRepository extends EntityRepository<Transaction, Long> {

    /**
     * Megszámolja a tranzakciókat tranzakció kategória azonosítója alapján
     *
     * @param transactionCategoryId a tranzakció kategória azonosítója
     * @return a tranzakciók darabszáma
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transactionCategory.id = ?1")
    long countByTransactionCategoryId(Long transactionCategoryId);
}

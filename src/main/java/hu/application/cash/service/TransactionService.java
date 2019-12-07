package hu.application.cash.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import hu.application.cash.model.entity.Account_;
import hu.application.cash.model.entity.Transaction;
import hu.application.cash.model.entity.TransactionCategory_;
import hu.application.cash.model.entity.Transaction_;
import hu.application.cash.model.entity.enums.TransactionType;
import hu.application.cash.model.repository.TransactionRepository;

/**
 * Szolgáltatás a {@link Transaction} entitás kezeléséhez.
 *
 * @author Pregh András
 */
@ApplicationScoped
public class TransactionService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Inject
    private TransactionRepository transactionRepository;

    /**
     * Tranzakció mentése {@link TransactionRepository}-n keresztül az adatbázisba.
     *
     * @param transaction a menteni kívánt tranzakció.
     * @return a mentett tranzakció.
     */
    @Transactional
    public Transaction save(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        logger.info("A tranzakció mentése sikeres volt!");
        return savedTransaction;
    }

    /**
     * Tranzakció törlése {@link TransactionRepository}-n keresztül az adatbázisból.
     *
     * @param transaction a törlendő tranzakció.
     */
    @Transactional
    public void delete(Transaction transaction) {
        //transactionRepository.remove(transaction);
        transactionRepository.attachAndRemove(transaction);
        logger.info("A [{}] azonosítójú tranzakció törlése sikeres volt!", transaction.getId());
    }

    /**
     * Tranzakció lekérdezése azonosító alapján {@link TransactionRepository}-n keresztül az adatbázisból.
     *
     * @param transactionId a lekérdezendó tranzakció azonosítója
     * @return a lekérdezendő tranzakció
     */
    public Transaction findById(Long transactionId) {
        Transaction transaction = transactionRepository.findBy(transactionId);
        logger.info("A [{}] azonosítójú tranzakció lekérdezése sikeres volt.", transaction.getId());
        return transaction;
    }

    /**
     * Az összes tranzakció lekérdezése {@link TransactionRepository}-n keresztül az adatbázisból.
     *
     * @return az összes tranzakció
     */
    public List<Transaction> findAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        logger.info("A tranzakciók lekérdezése sikeres volt. A lekérdezett elemek száma: [{}]", transactions.size());
        return transactions;
    }

    /**
     * Megszámolja a tranzakciókat tranzakció kategória azonosítója alapján
     *
     * @param transactionCategoryId a tranzakció kategória azonosítója
     * @return a tranzakciók darabszáma
     */
    public long countByTransactionCategoryId(Long transactionCategoryId) {
        long count = transactionRepository.countByTransactionCategoryId(transactionCategoryId);
        logger.info("A tranzakciók darabszáma: [{0}] a [{1}] tranzakció azonosító alapján", count, transactionCategoryId);
        return count;
    }

    /**
     * Lekérdezi a tranzakciókhoz tartazó összegeket összeadva a {@link Transaction} azonosító és {@link TransactionType} alapján,
     * az {@link EntityManager}-t használva.
     *
     * @param accountId       a fiók azonosító
     * @param transactionType a tranzakció típusa
     * @return a tranzakciókhoz tartazó összegeket összeadva
     */
    public Long sumAmountByAccountAndTransactionType(Long accountId, TransactionType transactionType) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cqp = builder.createQuery(Long.class);
        Root<Transaction> transactionRoot = cqp.from(Transaction.class);

        cqp.select(builder.sum(transactionRoot.get(Transaction_.AMOUNT))).where(
                builder.equal(transactionRoot.get(Transaction_.ACCOUNT).get(Account_.ID), accountId),
                builder.equal(transactionRoot.get(Transaction_.TRANSACTION_TYPE), transactionType),
                builder.lessThanOrEqualTo(transactionRoot.get(Transaction_.PAYMENT_DEADLINE),new Date())//aktuális egyenleg miatt
        );

        TypedQuery<Long> query = entityManager.createQuery(cqp);

        return query.getSingleResult() != null ? query.getSingleResult() : 0L;
    }

    /**
     * Lekérdezi a tranzakciókhoz tartazó összegeket összeadva a {@link Transaction} azonosító, a {@link TransactionType}
     * és az év alapján, az {@link EntityManager}-t használva.
     *
     * @param accountId       a fiók azonosító
     * @param transactionType a tranzakció típusa
     * @param year            az év
     * @returna tranzakciókhoz tartazó összegeket összeadva
     */
    public Map<Integer, Long> sumAmountByAccountAndTransactionTypeAndYear(Long accountId, TransactionType transactionType, int year) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cqp = builder.createQuery(Long.class);
        Root<Transaction> transactionRoot = cqp.from(Transaction.class);

        Map<Integer, Long> map = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(accountId)) {
                predicates.add(builder.equal(transactionRoot.get(Transaction_.ACCOUNT).get(Account_.ID), accountId));
            }
            predicates.add(builder.equal(transactionRoot.get(Transaction_.TRANSACTION_TYPE), transactionType));
            predicates.add(builder.equal(builder.function("YEAR", Integer.class, transactionRoot.get(Transaction_.PAYMENT_DEADLINE)), year));
            predicates.add(builder.equal(builder.function("MONTH", Integer.class, transactionRoot.get(Transaction_.PAYMENT_DEADLINE)), i));

            cqp.select(builder.sum(transactionRoot.get(Transaction_.AMOUNT))).where(predicates.toArray(new Predicate[]{}));

            TypedQuery<Long> query = entityManager.createQuery(cqp);

            Long value = query.getSingleResult() != null ? query.getSingleResult() : 0L;

            map.put(i, value);
            entityManager.clear();
        }

        return map;
    }

    /**
     * lekérdezi az összes {@link Transaction} entitást paraméterül adott szűrési feltételek alapján az {@link EntityManager}-t használva
     *
     * @param accountId       a fiók azonosítója
     * @param categoryId      a kategória azonosítója
     * @param transactionType a tranzakció típusa
     * @param fromDate        fizetési határidő-től
     * @param toDate          fizetési határidő-ig
     * @return a feltételeknek megfelelő tranzakció lista
     */
    public List<Transaction> findAllByFilters(Long accountId, Long categoryId, TransactionType transactionType, Date fromDate, Date toDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> cqp = builder.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = cqp.from(Transaction.class);

        transactionRoot.fetch(Transaction_.TRANSACTION_CATEGORY);
        transactionRoot.fetch(Transaction_.ACCOUNT);

        cqp.select(transactionRoot);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(accountId)) {
            predicates.add(builder.equal(transactionRoot.get(Transaction_.ACCOUNT).get(Account_.ID), accountId));
        }
        if (Objects.nonNull(categoryId)) {
            predicates.add(builder.equal(transactionRoot.get(Transaction_.transactionCategory).get(TransactionCategory_.ID), categoryId));
        }
        if (Objects.nonNull(transactionType)) {
            predicates.add(builder.equal(transactionRoot.get(Transaction_.TRANSACTION_TYPE), transactionType));
        }
        if (Objects.nonNull(fromDate)) {
            predicates.add(builder.greaterThanOrEqualTo(transactionRoot.get(Transaction_.PAYMENT_DEADLINE), fromDate));
        }
        if (Objects.nonNull(toDate)) {
            predicates.add(builder.lessThanOrEqualTo(transactionRoot.get(Transaction_.PAYMENT_DEADLINE), toDate));
        }

        if (!predicates.isEmpty()) {
            cqp.where(predicates.toArray(new Predicate[]{}));
        }

        TypedQuery<Transaction> query = entityManager.createQuery(cqp);
        logger.info("A tranzakciók lekérdezése sikeres volt. A lekérdezett elemek száma: [{}]", query.getResultList().size());
        return query.getResultList();
    }

}

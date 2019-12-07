package hu.application.cash.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import hu.application.cash.model.entity.TransactionCategory;
import hu.application.cash.model.repository.TransactionCategoryRepository;

/**
 * Szolgáltatás a {@link TransactionCategory} entitás kezeléséhez.
 *
 * @author Pregh András
 */
@ApplicationScoped
public class TransactionCategoryService {

    @Inject
    private Logger logger;

    @Inject
    private TransactionCategoryRepository transactionCategoryRepository;

    /**
     * Az összes tranzakció kategória lekérdezése {@link TransactionCategoryRepository}-n keresztül az adatbázisból.
     *
     * @return az összes tranzakció kategória.
     */
    public List<TransactionCategory> findAll() {
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAll();
        logger.info("A tranzakció kategóriák lekérdezése sikeres volt. A lekérdezett elemek száma: [{}]", transactionCategories.size());
        return transactionCategories;
    }

    /**
     * Lementi a paraméterül adott {@link TransactionCategory} entitást a {@link TransactionCategoryRepository}-n keresztül az adatbázisba.
     *
     * @param transactionCategory a menteni kívánt {@link TransactionCategory}
     * @return az elmentett {@link TransactionCategory}
     */
    public TransactionCategory save(TransactionCategory transactionCategory) {
        TransactionCategory savedTransactionCategory = transactionCategoryRepository.saveAndFlush(transactionCategory);
        logger.info("A tranzakció kategória mentése sikeres volt!");
        return savedTransactionCategory;
    }

    /**
     * Az összes {@link TransactionCategory} lekérdezése, ahol a szülő null.
     *
     * @return azon tranzakció kategóriák, ahol a szülő null.
     */
    public List<TransactionCategory> findAllByParentNull() {
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAllByParentNull();
        logger.info("A tranzakció kategóriák lekérdezése sikeres volt. A lekérdezett elemek száma: [{}]", transactionCategories.size());
        return transactionCategories;
    }

    /**
     * Az összes {@link TransactionCategory} lekérdezése szülő azonosító alapján.
     *
     * @param parentId a szülő kategória azonosítója
     * @return tranzakció kategóriák szülő azonosító alapján.
     */
    public List<TransactionCategory> findAllByParentId(Long parentId) {
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAllByParentId(parentId);
        logger.info("A tranzakció kategóriák lekérdezése sikeres volt. A lekérdezett elemek száma: [{}]", transactionCategories.size());
        return transactionCategories;
    }

}

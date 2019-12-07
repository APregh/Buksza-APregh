package hu.application.cash.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;

import hu.application.cash.model.entity.Account;
import hu.application.cash.model.entity.enums.TransactionType;
import hu.application.cash.model.repository.AccountRepository;

/**
 * Szolgáltatás az {@link Account} entitás kezeléséhez.
 *
 * @author Pregh András
 */
@ApplicationScoped
public class AccountService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private TransactionService transactionService;

    /**
     * Fiók mentése {@link AccountRepository}-n keresztül az adatbázisba.
     *
     * @param account a menteni kívánt fiók.
     * @return a mentett fiók.
     */
    @Transactional
    public Account save(Account account) {
        Account savedAccount = accountRepository.saveAndFlushAndRefresh(account);
        logger.info("A(z) [{}] nevű fiók mentése sikeres volt.", savedAccount.getAccountName());
        return savedAccount;
    }

    /**
     * Az összes {@link Account} lekérdezése {@link AccountRepository}-n keresztül az adatbázisból.
     *
     * @return az összes fiók.
     */
    public List<Account> findAll() {
        List<Account> accounts = accountRepository.findAll();
        logger.info("A fiókok listázása sikeres volt. Listázott elemek száma: [{}]", accounts.size());
        return accounts;
    }

    /**
     * {@link Account} lekérdezése azonosító alapján {@link AccountRepository}-n keresztül az adatbázisból.
     *
     * @param accountId a lekérdezendó fiók azonosítója.
     * @return a lekérdezendő fiók.
     */
    public Account findById(Long accountId) {
        Account account = accountRepository.findBy(accountId);
        logger.info("A(z) [{}] azonosítójú fiók lekérdezése sikeres volt.", accountId);
        return account;
    }

    /**
     * Fiók törlése az adatbázisból {@link AccountRepository}-n keresztül.
     *
     * @param account a törlendő fiók.
     */
    @Transactional
    public void delete(Account account) {
        accountRepository.remove(account);
        logger.info("A(z) [{}] azonosítójú fiók törlése sikeres volt", account.getId());
    }

    /**
     * A fiókok összegének frissítése a tranzakciók alapján.
     */
    @Transactional
    public void refreshAccounts() {
        List<Account> accounts = findAll();
        for (Account account : accounts) {
            Long incomeAmount = transactionService.sumAmountByAccountAndTransactionType(account.getId(), TransactionType.INCOME);
            Long costAmount = transactionService.sumAmountByAccountAndTransactionType(account.getId(), TransactionType.COST);
            account.setAmount(incomeAmount - costAmount);
            save(account);
            entityManager.clear(); // az entityManager memóriájának üritése, hogy ne legyen összeakadás
        }
    }

}

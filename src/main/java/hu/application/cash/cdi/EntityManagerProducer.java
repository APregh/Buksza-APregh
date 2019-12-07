package hu.application.cash.cdi;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Producer osztály, az {@link EntityManager} injectálásához.
 *
 * @author Pregh András
 */
@Dependent
public class EntityManagerProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    private static EntityManager entityManager;

    /**
     * Ha az entityManager null, akkor felszedi a persistence.xml-ből és beállítja.
     * Injectálás során ez a metódus fogja visszaadni az entityManager-t
     *
     * @return {@link EntityManager}
     */
    @Produces
    public static EntityManager produceEntityManager() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }
}

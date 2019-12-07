package hu.application.cash.model.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import hu.application.cash.model.entity.Account;

/**
 * Repository interface a {@link Account} entitás kezeléséhez.
 *
 * @author Pregh András
 */
@Repository
public interface AccountRepository extends EntityRepository<Account, Long> {

}

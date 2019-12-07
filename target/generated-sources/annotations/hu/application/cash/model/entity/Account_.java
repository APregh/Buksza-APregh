package hu.application.cash.model.entity;

import hu.application.cash.model.entity.enums.AccountType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ {

	public static volatile SingularAttribute<Account, Long> amount;
	public static volatile SingularAttribute<Account, String> accountName;
	public static volatile SingularAttribute<Account, AccountType> accountType;
	public static volatile SingularAttribute<Account, Long> id;

	public static final String AMOUNT = "amount";
	public static final String ACCOUNT_NAME = "accountName";
	public static final String ACCOUNT_TYPE = "accountType";
	public static final String ID = "id";

}


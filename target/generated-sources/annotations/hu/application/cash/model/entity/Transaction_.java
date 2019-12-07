package hu.application.cash.model.entity;

import hu.application.cash.model.entity.enums.TransactionType;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, TransactionType> transactionType;
	public static volatile SingularAttribute<Transaction, Long> amount;
	public static volatile SingularAttribute<Transaction, TransactionCategory> transactionCategory;
	public static volatile SingularAttribute<Transaction, Date> paymentDeadline;
	public static volatile SingularAttribute<Transaction, Long> id;
	public static volatile SingularAttribute<Transaction, String> transactionName;
	public static volatile SingularAttribute<Transaction, Date> creationDate;
	public static volatile SingularAttribute<Transaction, Account> account;

	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String AMOUNT = "amount";
	public static final String TRANSACTION_CATEGORY = "transactionCategory";
	public static final String PAYMENT_DEADLINE = "paymentDeadline";
	public static final String ID = "id";
	public static final String TRANSACTION_NAME = "transactionName";
	public static final String CREATION_DATE = "creationDate";
	public static final String ACCOUNT = "account";

}


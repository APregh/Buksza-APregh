package hu.application.cash.model.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransactionCategory.class)
public abstract class TransactionCategory_ {

	public static volatile SingularAttribute<TransactionCategory, TransactionCategory> parent;
	public static volatile SingularAttribute<TransactionCategory, Long> id;
	public static volatile SingularAttribute<TransactionCategory, String> categoryName;

	public static final String PARENT = "parent";
	public static final String ID = "id";
	public static final String CATEGORY_NAME = "categoryName";

}


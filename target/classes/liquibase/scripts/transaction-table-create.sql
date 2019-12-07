CREATE TABLE TRANSACTION
(
    ID                      BIGINT       NOT NULL PRIMARY KEY COMMENT 'Az elsődleges kulcs',
    TRANSACTION_NAME        VARCHAR(255) NOT NULL COMMENT 'A tranzakció neve',
    ACCOUNT_ID              BIGINT       NOT NULL COMMENT 'A fiók azonosítója, amelyhez a tranzakció tartozik',
    TRANSACTION_TYPE        VARCHAR(50)  NOT NULL COMMENT 'A tranzakció típusa',
    TRANSACTION_CATEGORY_ID BIGINT       NOT NULL COMMENT 'A tranzakció kategóriájának azonosítója',
    AMOUNT                  DOUBLE       NOT NULL COMMENT 'A tranzakcióban lévő pénzmozgás összege',
    CREATION_DATE           DATE         NOT NULL COMMENT 'A tranzakció létrehozásának dátuma',
    PAYMENT_DEADLINE        DATE         NOT NULL COMMENT 'A fizetési határidő',

    FOREIGN KEY FK_ACCOUNT_ID (ACCOUNT_ID) REFERENCES ACCOUNT (ID),
    FOREIGN KEY FK_TRANSACTION_CATEGORY_ID (TRANSACTION_CATEGORY_ID) REFERENCES TRANSACTION_CATEGORY (ID)

) DEFAULT CHARSET utf8
  COLLATE utf8_general_ci
  ENGINE = InnoDB COMMENT = 'A tranzakciókat tartalmazó tábla';

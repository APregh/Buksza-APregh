CREATE TABLE TRANSACTION_CATEGORY
(
    ID            BIGINT       NOT NULL PRIMARY KEY COMMENT 'Az elsődleges kulcs',
    CATEGORY_NAME VARCHAR(255) NOT NULL COMMENT 'A kategória neve',
    PARENT_ID     BIGINT       NULL COMMENT 'Szülő kategória azonosítója',

    FOREIGN KEY FK_PARENT_ID (PARENT_ID) REFERENCES TRANSACTION_CATEGORY (ID)

) DEFAULT CHARSET utf8
  COLLATE utf8_general_ci
  ENGINE = InnoDB COMMENT = 'A tranzakciók kategóriáit tartalmazó tábla';

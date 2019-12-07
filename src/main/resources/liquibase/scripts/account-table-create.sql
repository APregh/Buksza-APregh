CREATE TABLE ACCOUNT
(
    ID           BIGINT       NOT NULL PRIMARY KEY COMMENT 'Az elsődleges kulcs',
    ACCOUNT_NAME VARCHAR(255) NOT NULL COMMENT 'A fiók neve',
    AMOUNT       BIGINT       NOT NULL COMMENT 'A fiókhoz tartozó összeg',
    ACCOUNT_TYPE VARCHAR(50)  NOT NULL COMMENT 'A fiók típusa'

) DEFAULT CHARSET utf8
  COLLATE utf8_general_ci
  ENGINE = InnoDB COMMENT = 'A fiókokat tartalmazó tábla';



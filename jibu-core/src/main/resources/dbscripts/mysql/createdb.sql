

-- Run this script to create the Jibu database tables in your database.

-- *****************************************************
-- Create the tables and indices

-- ------------------------------------------------------
CREATE TABLE userbase (
    id                     integer auto_increment NOT NULL PRIMARY KEY,
    username               varchar(128) NOT NULL,
    password               varchar(64) NOT NULL,
    fullname               varchar(128) NOT NULL,
    emailaddress           varchar(128),
    enabled                tinyint(1) default 1 NOT NULL
);
ALTER TABLE userbase ADD CONSTRAINT userbase_username_uq UNIQUE ( username );

-- ------------------------------------------------------
CREATE TABLE roles (
    id                     integer auto_increment NOT NULL PRIMARY KEY,
    name                   varchar(128) NOT NULL,
    lft                    integer NOT NULL,
    rgt                    integer NOT NULL,
    description            varchar(512) NOT NULL
);
ALTER TABLE roles ADD CONSTRAINT roles_name_uq UNIQUE ( name );

-- ------------------------------------------------------
CREATE TABLE user_role_map (
    user_id                integer NOT NULL,
    role_id                integer NOT NULL
);
ALTER TABLE user_role_map ADD CONSTRAINT urmap_user_id_role_id_uq UNIQUE ( user_id,role_id );
ALTER TABLE user_role_map ADD CONSTRAINT urmap_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE user_role_map ADD CONSTRAINT urmap_user_id_fk FOREIGN KEY (user_id) REFERENCES userbase (id);

-- ------------------------------------------------------
CREATE TABLE authorities (
    id                     integer auto_increment NOT NULL PRIMARY KEY,
    name                   varchar(128) NOT NULL,
    value                  varchar(128) NOT NULL
);
ALTER TABLE authorities ADD CONSTRAINT authorities_value_uq UNIQUE ( value );

-- ------------------------------------------------------
CREATE TABLE role_authority_map (
    role_id                integer NOT NULL,
    authority_id           integer NOT NULL
);
ALTER TABLE role_authority_map ADD CONSTRAINT ramap_role_id_authority_id_uq UNIQUE ( role_id,authority_id );
ALTER TABLE role_authority_map ADD CONSTRAINT ramap_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE role_authority_map ADD CONSTRAINT ramap_authority_id_fk FOREIGN KEY (authority_id) REFERENCES authorities (id);

-- ------------------------------------------------------
CREATE TABLE settings(
  id                       integer auto_increment NOT NULL PRIMARY KEY,
  name                     varchar(32) NOT NULL,
  value                    varchar(32) NOT NULL,
  sortindex                integer default 0 NOT NULL,
  enabled                  tinyint(1) default 1 NOT NULL
);
ALTER TABLE settings ADD CONSTRAINT setting_name_sortindex_uq UNIQUE ( name,sortindex );
ALTER TABLE settings ADD CONSTRAINT setting_name_value_uq UNIQUE ( name,value );

-- ------------------------------------------------------
CREATE TABLE user_setting_map(
  user_id                  integer NOT NULL,
  setting_id               integer NOT NULL
);
ALTER TABLE user_setting_map ADD CONSTRAINT usmap_user_id_setting_id_uq UNIQUE ( user_id,setting_id );
ALTER TABLE user_setting_map ADD CONSTRAINT usmap_user_id_fk FOREIGN KEY (user_id) REFERENCES userbase (id);
ALTER TABLE user_setting_map ADD CONSTRAINT usmap_setting_id_fk FOREIGN KEY (setting_id) REFERENCES settings (id);

-- ------------------------------------------------------
CREATE TABLE tokens (
    id                     integer auto_increment NOT NULL PRIMARY KEY,
    value                  varchar(64) NOT NULL,
    type                   varchar(16) NOT NULL,
    expiration             datetime NOT NULL,
    user_id                integer NOT NULL
);
ALTER TABLE tokens ADD CONSTRAINT tokens_value_uq UNIQUE ( value );
ALTER TABLE tokens ADD CONSTRAINT tokens_user_id_fk FOREIGN KEY (user_id) REFERENCES userbase (id);

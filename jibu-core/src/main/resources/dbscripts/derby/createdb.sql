

-- Run this script to create the Gaixie database tables in your database.

-- *****************************************************
-- Create the tables and indices

-- ------------------------------------------------------
CREATE TABLE userbase (
    id                     integer generated by default as identity NOT NULL PRIMARY KEY,
    username               varchar(128) NOT NULL,    
    password               varchar(64) NOT NULL,
    fullname               varchar(128) NOT NULL,
    emailaddress           varchar(128),
    enabled                smallint default 1 NOT NULL
);
ALTER TABLE userbase ADD CONSTRAINT userbase_username_uq UNIQUE ( username );

insert into userbase(username,password,fullname,emailaddress,enabled) 
       values ('admin','e10adc3949ba59abbe56e057f20f883e','Administrator','jibu.gaixie@gmail.com',1);

-- ------------------------------------------------------
CREATE TABLE roles (
    id                     integer generated by default as identity NOT NULL PRIMARY KEY,
    name                   varchar(128) NOT NULL,
    lft                    integer NOT NULL,
    rgt                    integer NOT NULL,
    description            varchar(512) NOT NULL
);
ALTER TABLE roles ADD CONSTRAINT roles_name_uq UNIQUE ( name );

insert into roles(name,lft,rgt,description) 
       values ('ROLE_BASE',1,4,'所有普通用户角色.');
insert into roles(name,lft,rgt,description) 
       values ('ROLE_ADMIN',2,3,'管理员角色，拥有所有权限，不要修改角色名称，无须绑定任何Autority.');

-- ------------------------------------------------------
CREATE TABLE user_role_map (
    user_id                integer NOT NULL,
    role_id                integer NOT NULL
);
ALTER TABLE user_role_map ADD CONSTRAINT user_role_map_ur_id_uq UNIQUE ( user_id,role_id );
ALTER TABLE user_role_map ADD CONSTRAINT urmap_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE user_role_map ADD CONSTRAINT urmap_user_id_fk FOREIGN KEY (user_id) REFERENCES userbase (id);

insert into user_role_map(user_id,role_id) values (1,2);

-- ------------------------------------------------------
CREATE TABLE authorities (
    id                     integer generated by default as identity NOT NULL PRIMARY KEY,
    name                   varchar(128) NOT NULL,
    value                  varchar(128) NOT NULL
);
ALTER TABLE authorities ADD CONSTRAINT authorities_value_uq UNIQUE ( value );

-- ------------------------------------------------------
CREATE TABLE role_authority_map (
    role_id                integer NOT NULL,
    authority_id           integer NOT NULL
);
ALTER TABLE role_authority_map ADD CONSTRAINT role_authority_map_ra_id_uq UNIQUE ( role_id,authority_id );
ALTER TABLE role_authority_map ADD CONSTRAINT rrmap_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE role_authority_map ADD CONSTRAINT rrmap_authority_id_fk FOREIGN KEY (authority_id) REFERENCES authorities (id);

-- ------------------------------------------------------
CREATE TABLE settings(
  id                       integer generated by default as identity NOT NULL PRIMARY KEY,
  name                     varchar(32) NOT NULL,
  value                    varchar(32) NOT NULL,
  sortindex                integer default 0 NOT NULL,
  enabled                  smallint default 1 NOT NULL
);
ALTER TABLE settings ADD CONSTRAINT setting_name_sortindex_uq UNIQUE ( name,sortindex );
ALTER TABLE settings ADD CONSTRAINT setting_name_value_uq UNIQUE ( name,value );

-- ------------------------------------------------------
CREATE TABLE user_setting_map(
  user_id                  integer NOT NULL,
  setting_id               integer NOT NULL
);
ALTER TABLE user_setting_map ADD CONSTRAINT usm_user_id_setting_id_uq UNIQUE ( user_id,setting_id );

-- ------------------------------------------------------
CREATE TABLE tokens (
    id                     integer generated by default as identity NOT NULL PRIMARY KEY,
    value                  varchar(64) NOT NULL,    
    type                   varchar(16) NOT NULL,
    expiration             timestamp NOT NULL,
    user_id                integer NOT NULL
);
ALTER TABLE tokens ADD CONSTRAINT tokens_value_uq UNIQUE ( value );
ALTER TABLE tokens ADD CONSTRAINT tokens_user_id_fk FOREIGN KEY (user_id) REFERENCES userbase (id);
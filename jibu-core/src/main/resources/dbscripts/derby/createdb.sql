

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
       values ('admin','e10adc3949ba59abbe56e057f20f883e','Administrator','admin@xxx.com',1);

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
-- name:  UserServlet.y, location
-- type:  action, data
-- value: jibu.security.user , 1 (Beijing)
-- mask:  4
CREATE TABLE authorities (
    id                     integer generated by default as identity NOT NULL PRIMARY KEY,
    name                   varchar(128) NOT NULL,
    type                   varchar(64) NOT NULL,
    value                  varchar(128) NOT NULL,
    mask                   smallint NOT NULL
);
ALTER TABLE authorities ADD CONSTRAINT authorities_nm_uq UNIQUE ( name,mask );

-- ------------------------------------------------------
CREATE TABLE role_authority_map (
    role_id                integer NOT NULL,
    authority_id           integer NOT NULL
);
ALTER TABLE role_authority_map ADD CONSTRAINT role_authority_map_ra_id_uq UNIQUE ( role_id,authority_id );
ALTER TABLE role_authority_map ADD CONSTRAINT rrmap_role_id_fk FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE role_authority_map ADD CONSTRAINT rrmap_authority_id_fk FOREIGN KEY (authority_id) REFERENCES authorities (id);

insert into userbase(username,password,fullname,emailaddress,enabled) 
       values ('admin','e10adc3949ba59abbe56e057f20f883e','Administrator','jibu.gaixie@gmail.com',true);

insert into roles(name,lft,rgt,description) 
       values ('ROLE_BASE',1,4,'所有普通用户角色.');
insert into roles(name,lft,rgt,description) 
       values ('ROLE_ADMIN',2,3,'管理员角色，拥有所有权限，不要修改角色名称，无须绑定任何Autority.');

insert into user_role_map(user_id,role_id) values (1,2);

insert into authorities (name,value) values ('system.administration.monitor','Monitor.z');
insert into authorities (name,value) values ('system.administration.pm','Role.z');
insert into authorities (name,value) values ('system.setting','Setting.y');


insert into settings (name,value,sortindex,enabled) values ('theme','blue',0,true);
insert into settings (name,value,sortindex,enabled) values ('theme','gray',1,true);
insert into settings (name,value,sortindex,enabled) values ('layout','classic',0,true);
insert into settings (name,value,sortindex,enabled) values ('language','en_US',1,true);
insert into settings (name,value,sortindex,enabled) values ('language','zh_CN',2,true);

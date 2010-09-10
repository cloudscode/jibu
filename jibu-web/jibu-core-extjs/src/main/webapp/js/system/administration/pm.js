Ext.ns('jibu.security');
jibu.security.PM =
    Ext.extend(Ext.Panel,{
                   layout:'hbox',
                   layoutConfig: {
                       align : 'stretch',
                       pack  : 'start'
                   },

                   initComponent:function() {
                       var config = {
                           items:[{
                                      xtype:'system.administration.usergrid',
                                      flex:2
                                  },{
                                      xtype:'system.administration.roletree',
                                      flex:1
                                  },{
                                      xtype:'system.administration.authgrid',
                                      width:280

                                  }]
                       };

                       // apply config
                       Ext.apply(this, Ext.apply(this.initialConfig, config));

                       // call parent
                       jibu.security.PM.superclass.initComponent.apply(this, arguments);

                       this.userPanel = this.items.itemAt(0);
                       this.rolePanel = this.items.itemAt(1);
                       this.authPanel = this.items.itemAt(2);

                       /*
                        * 一个重要的控制变量。
                        * fromwhere =''      : 初始状态。
                        * fromwhere ='user'  : 选定了 user，并加载了此user 拥有的 role 和 authority，可以修改 role和user的绑定关系。
                        * fromwhere ='role'  : 选定了 role，并加载了此role 拥有的 user 和 aurhority，可以修改 role与user， role与authority的绑定关系。
                        * fromwhere ='auth'  : 选定了 authority，并加载了此authority 绑定的 role 和 拥有它的user ，可以修改 role和authority的绑定关系。
                        *
                        */
                       this.fromwhere = '';
                       // 保存所有选中的 User Checkbox，即使再次加载也能保留 check状态
                       this.checkedUserIds = new Array();
                       this.userSM = this.userPanel.getSelectionModel();
                       this.userSM.handleMouseDown = Ext.emptyFn;
                       this.userStore = this.userPanel.getStore();
                       this.userStarBtn = this.userPanel.getTopToolbar().get('user-star-button');

                       this.checkedAuthIds = new Array();
                       this.authSM = this.authPanel.getSelectionModel();
                       this.authSM.handleMouseDown = Ext.emptyFn;
                       this.authStore = this.authPanel.getStore();
                       this.authStarBtn = this.authPanel.getTopToolbar().get('auth-star-button');

                       this.roleSM = this.rolePanel.getSelectionModel();
                       this.roleStarBtn = this.rolePanel.getTopToolbar().get('role-star-button');
                       this.roleRefreshBtn = this.rolePanel.getTopToolbar().get('role-refresh-button');


                       var resetAuthFn = function(authSM,bbar,btn) {

                           authSM.suspendEvents();
                           // 在 clearSection() 之前必须保证 unlock，做完后为初始状态不用置回 lock
                           authSM.unlock();
                           authSM.singleSelect = true;
                           authSM.clearSelections();
                           authSM.resumeEvents();
                           authSM.unlock();
                           bbar.get('auth-delete-button').disable();
                           bbar.get('auth-edit-button').disable();
                           btn.disable();
                       };


                       var resetUserFn = function(userSM,bbar,btn) {

                           userSM.suspendEvents();
                           // 在 clearSection() 之前必须保证 unlock，做完后为初始状态不用置回 lock
                           userSM.unlock();
                           userSM.singleSelect = true;
                           userSM.clearSelections();
                           userSM.resumeEvents();
                           userSM.unlock();
                           bbar.get('user-delete-button').disable();
                           bbar.get('user-edit-button').disable();
                           btn.disable();
                       };

                       var resetRoleFn = function(rp,bbar,btn) {
                           var ck = rp.getChecked();
                           rp.suspendEvents();
                           for(var i =0;i<ck.length;i++){
                               ck[i].getUI().toggleCheck(false);
                               ck[i].attributes.checked=false;
                           }
                           rp.resumeEvents();
                           bbar.get('role-delete-button').disable();
                           bbar.get('role-edit-button').disable();
                           bbar.get('role-add-button').disable();
                           rp.getTopToolbar().get('role-refresh-button').enable();
                           btn.disable();
                       };

                       this.userSM.on('selectionchange',
                                      function(t){
                                          var ubbar = this.userPanel.getBottomToolbar();
                                          if(this.fromwhere=='' || this.fromwhere =='user') {
                                              if (t.getCount()==1) {  // 单选,下面按钮有效.
                                                  ubbar.get('user-delete-button').enable();
                                                  ubbar.get('user-edit-button').enable();
                                                  this.userStarBtn.enable();

                                              } else { // 没有选择或者多选, 下面按钮无效.
                                                  ubbar.get('user-delete-button').disable();
                                                  ubbar.get('user-edit-button').disable();
                                                  this.userStarBtn.disable();
                                              }

                                              this.fromwhere ='';
                                              this.userStarBtn.setIconClass('star-off-icon');
                                              this.checkedAuthIds.length=0;
                                              resetRoleFn(this.rolePanel,this.rolePanel.getBottomToolbar(), this.roleStarBtn);
                                              resetAuthFn(this.authSM, this.authPanel.getBottomToolbar(), this.authStarBtn);

                                          } else { // 通过绑定被 check , disable下面按钮.
                                              ubbar.get('user-delete-button').disable();
                                              ubbar.get('user-edit-button').disable();
                                              this.userStarBtn.disable();
                                          }

                                      },this);

                       this.userSM.on('rowselect',
                                      function(e, rowIndex, record) {
                                          if (this.fromwhere=='role') {
                                              var ck = this.rolePanel.getChecked();
                                              Ext.Ajax.request(
                                                  {
                                                      url:'RoleServlet.z?ci=bindUser',
                                                      params:{
                                                          'user.id':record.data.id,
                                                          'role.id':ck[0].id
                                                      },
                                                      method:'POST',
                                                      success: function(r,a){
                                                      },
                                                      failure: function(r,o){
                                                      },
                                                      scope:this
                                                  }
                                              );
                                          }

                                          // 判断本次选择的 row 是否已经在 bindUserIds，如果不在，放入。
                                          for(var i=0;i<this.checkedUserIds.length;i++) {
                                              if(this.checkedUserIds[i] == record.data.id) {
                                                  return;
                                              }
                                          }
                                          this.checkedUserIds.push(record.data.id);

                                      },
                                      this);

                       this.userSM.on('rowdeselect',
                                      function(e, rowIndex, record) {
                                          if (this.fromwhere=='role') {
                                              var ck = this.rolePanel.getChecked();
                                              Ext.Ajax.request(
                                                  {
                                                      url:'RoleServlet.z?ci=unbindUser',
                                                      params:{
                                                          'user.id':record.data.id,
                                                          'role.id':ck[0].id
                                                      },
                                                      method:'POST',
                                                      success: function(r,a){
                                                      },
                                                      failure: function(r,o){
                                                      },
                                                      scope:this
                                                  }
                                              );
                                          }
                                          for(var i=0;i<this.checkedUserIds.length;i++) {
                                              if(this.checkedUserIds[i] == record.data.id) {
                                                  this.checkedUserIds.remove(record.data.id);
                                                  return;
                                              }
                                          }
                                      },
                                      this);


                       this.userStore.on('load',
                                         function() {
                                             var checkedIds = this.checkedUserIds;
                                             // 防止 userpanel 的 rowselect 事件发 ajax请求
                                             this.rolePanel.suspendEvents();
                                             var records = new Array();
                                             this.userStore.each(
                                                 function(record) {
                                                     for(var i=0;i<checkedIds.length;i++) {
                                                         if(checkedIds[i] == record.data.id) {
                                                             records.push(record);
                                                         }
                                                     }

                                                 });
                                             if (this.fromwhere == 'auth') {
                                                 this.userSM.unlock();
                                                 this.userSM.selectRecords(records, true);
                                                 this.userSM.lock();
                                             } else {
                                                 this.userSM.selectRecords(records, true);
                                             }

                                             this.rolePanel.resumeEvents();
                                         },
                                         this);



                       this.userStarBtn.on('click',
                                           function(n,e){
                                               if (this.fromwhere=='' || this.fromwhere=='user') {
                                                   Ext.Ajax.request(
                                                       {
                                                           url:'RoleServlet.z?ci=userBindCheck',
                                                           params:{
                                                               'username':this.userSM.getSelected().get('username')},
                                                           method:'POST',
                                                           success: function(r,a){
                                                               var data = Ext.util.JSON.decode(r.responseText);
                                                               var ck = this.rolePanel.getChecked();
                                                               //Ext.Msg.alert('信息3',ck.length);
                                                               // 在改变role和auth的选择状态之前，把它们的事件suspend，处理完后再恢复。
                                                               this.rolePanel.suspendEvents();
                                                               this.authSM.suspendEvents();

                                                               for(var i =0;i<ck.length;i++){
                                                                   ck[i].getUI().toggleCheck(false);
                                                                   ck[i].attributes.checked=false;
                                                               }
                                                               var roles = data.roles;
                                                               for(var j =0;j<roles.length;j++){
                                                                   var node = this.rolePanel.getNodeById(roles[j]);
                                                                   node.getUI().toggleCheck(true);
                                                                   node.attributes.checked=true;
                                                               }

                                                               // 将匹配的 auth 选中
                                                               this.checkedAuthIds = data.auths;
                                                               var checkedIds = this.checkedAuthIds;
                                                               var records = new Array();
                                                               this.authStore.each(
                                                                   function(record) {
                                                                       for(var i=0;i<checkedIds.length;i++) {
                                                                           if(checkedIds[i] == record.data.id) {
                                                                               records.push(record);
                                                                           }
                                                                       }

                                                                   });
                                                               this.authSM.unlock();
                                                               this.authSM.singleSelect = false;
                                                               this.authSM.clearSelections();
                                                               this.authSM.selectRecords(records, true);
                                                               this.authSM.lock();

                                                               this.rolePanel.resumeEvents();
                                                               this.authSM.resumeEvents();

                                                               this.userStarBtn.setIconClass('star-icon');
                                                               this.roleRefreshBtn.disable();
                                                               this.fromwhere = 'user';
                                                           },
                                                           failure: function(r,o){
                                                           },
                                                           scope:this
                                                       }
                                                   );
                                               };
                                           },
                                           this);
                       this.authSM.on('selectionchange',
                                      function(t){
                                          var abbar = this.authPanel.getBottomToolbar();
                                          if(this.fromwhere=='' || this.fromwhere =='auth') {
                                              if (t.getCount()==1) {  // 单选,下面按钮有效.
                                                  abbar.get('auth-delete-button').enable();
                                                  abbar.get('auth-edit-button').enable();
                                                  this.authStarBtn.enable();

                                              } else { // 没有选择或者多选, 下面按钮无效.
                                                  abbar.get('auth-delete-button').disable();
                                                  abbar.get('auth-edit-button').disable();
                                                  this.authStarBtn.disable();
                                              }

                                              this.fromwhere ='';
                                              this.authStarBtn.setIconClass('star-off-icon');
                                              this.checkedAuthIds.length=0;
                                              resetRoleFn(this.rolePanel,this.rolePanel.getBottomToolbar(), this.roleStarBtn);
                                              // 在 clearSection() 之前必须保证 unlock，做完后为初始状态不用置回 lock
                                              this.checkedUserIds.length=0;
                                              resetUserFn(this.userSM, this.userPanel.getBottomToolbar(), this.userStarBtn);
                                          } else { // 通过绑定被 check , disable下面按钮.
                                              abbar.get('auth-delete-button').disable();
                                              abbar.get('auth-edit-button').disable();
                                              this.authStarBtn.disable();
                                          }
                                      },this);

                       this.authSM.on('rowselect',
                                      function(e, rowIndex, record) {
                                          if (this.fromwhere=='role') {
                                              var ck = this.rolePanel.getChecked();
                                              Ext.Ajax.request(
                                                  {
                                                      url:'RoleServlet.z?ci=bindAuth',
                                                      params:{
                                                          'authority.id':record.data.id,
                                                          'role.id':ck[0].id
                                                      },
                                                      method:'POST',
                                                      success: function(r,a){
                                                      },
                                                      failure: function(r,o){
                                                      },
                                                      scope:this
                                                  }
                                              );
                                          }
                                          // 判断本次选择的 row 是否已经在 bindAuthIds，如果不在，放入。
                                          for(var i=0;i<this.checkedAuthIds.length;i++) {
                                              if(this.checkedAuthIds[i] == record.data.id) {
                                                  return;
                                              }
                                          }
                                          this.checkedAuthIds.push(record.data.id);

                                      },
                                      this);

                       this.authSM.on('rowdeselect',
                                      function(e, rowIndex, record) {
                                          if (this.fromwhere=='role') {
                                              var ck = this.rolePanel.getChecked();
                                              Ext.Ajax.request(
                                                  {
                                                      url:'RoleServlet.z?ci=unbindAuth',
                                                      params:{
                                                          'authority.id':record.data.id,
                                                          'role.id':ck[0].id
                                                      },
                                                      method:'POST',
                                                      success: function(r,a){
                                                      },
                                                      failure: function(r,o){
                                                      },
                                                      scope:this
                                                  }
                                              );
                                          }
                                          for(var i=0;i<this.checkedAuthIds.length;i++) {
                                              if(this.checkedAuthIds[i] == record.data.id) {
                                                  this.checkedAuthIds.remove(record.data.id);
                                                  return;
                                              }
                                          }
                                      },
                                      this);


                       this.authStore.on('load',
                                         function() {
                                             // 防止 authpanel 的 rowselect 事件发 ajax请求
                                             var checkedIds = this.checkedAuthIds;
                                             this.rolePanel.suspendEvents();
                                             var records = new Array();
                                             this.authStore.each(
                                                 function(record) {
                                                     for(var i=0;i<checkedIds.length;i++) {
                                                         if(checkedIds[i] == record.data.id) {
                                                             records.push(record);
                                                         }
                                                     }

                                                 });
                                             if (this.fromwhere == 'user') {
                                                 this.authSM.unlock();
                                                 this.authSM.selectRecords(records, true);
                                                 this.authSM.lock();
                                             } else {
                                                 this.authSM.selectRecords(records, true);
                                             }

                                             this.rolePanel.resumeEvents();
                                         },
                                         this);



                       this.authStarBtn.on('click',
                                           function(n,e){
                                               if (this.fromwhere=='' || this.fromwhere=='auth') {
                                                   Ext.Ajax.request(
                                                       {
                                                           url:'RoleServlet.z?ci=authBindCheck',
                                                           params:{
                                                               'id':this.authSM.getSelected().get('id')},
                                                           method:'POST',
                                                           success: function(r,a){
                                                               var data = Ext.util.JSON.decode(r.responseText);
                                                               var ck = this.rolePanel.getChecked();
                                                               this.rolePanel.suspendEvents();
                                                               this.userSM.suspendEvents();

                                                               //Ext.Msg.alert('信息3',ck.length);
                                                               for(var i =0;i<ck.length;i++){
                                                                   ck[i].getUI().toggleCheck(false);
                                                                   ck[i].attributes.checked=false;
                                                               }
                                                               var roles = data.roles;
                                                               for(var j =0;j<roles.length;j++){
                                                                   var node = this.rolePanel.getNodeById(roles[j]);
                                                                   node.getUI().toggleCheck(true);
                                                                   node.attributes.checked=true;
                                                               }

                                                               // 将匹配的 auth 选中
                                                               this.checkedUserIds = data.users;
                                                               var records = new Array();
                                                               this.userStore.each(
                                                                   function(record) {
                                                                       for(var i=0;i<this.checkedUserIds.length;i++) {
                                                                           if(this.checkedUserIds[i] == record.data.id) {
                                                                               records.push(record);
                                                                           }
                                                                       }

                                                                   },this);
                                                               this.userSM.unlock();
                                                               this.userSM.singleSelect = false;
                                                               this.userSM.clearSelections();
                                                               this.userSM.selectRecords(records, true);
                                                               this.userSM.lock();

                                                               this.rolePanel.resumeEvents();
                                                               this.userSM.resumeEvents();

                                                               this.authStarBtn.setIconClass('star-icon');
                                                               this.roleRefreshBtn.disable();
                                                               this.fromwhere = 'auth';
                                                           },
                                                           failure: function(r,o){
                                                           },
                                                           scope:this
                                                       }
                                                   );
                                               };
                                           },
                                           this);
                       this.roleStarBtn.on('click',
                                           function(b,e){
                                               if (this.fromwhere=='' || this.fromwhere=='role') {
                                                   var ck = this.rolePanel.getChecked();
                                                   Ext.Ajax.request(
                                                       {
                                                           url:'RoleServlet.z?ci=roleBindCheck',
                                                           params:{
                                                               'id':ck[0].id},
                                                           method:'POST',
                                                           success: function(r,a){
                                                               var resp = Ext.util.JSON.decode(r.responseText);
                                                               this.checkedUserIds = resp.users;
                                                               var recuser = new Array();
                                                               this.userStore.each(
                                                                   function(record) {
                                                                       for(var i=0;i<this.checkedUserIds.length;i++) {
                                                                           if(this.checkedUserIds[i] == record.data.id) {
                                                                               recuser.push(record);
                                                                           }
                                                                       }

                                                                   },this);
                                                               this.checkedAuthIds = resp.auths;
                                                               var recauth = new Array();
                                                               this.authStore.each(
                                                                   function(record) {

                                                                       for(var i=0;i<this.checkedAuthIds.length;i++) {
                                                                           if(this.checkedAuthIds[i] == record.data.id) {
                                                                               recauth.push(record);
                                                                           }
                                                                       }

                                                                   },this);
                                                               this.userSM.suspendEvents();
                                                               this.authSM.suspendEvents();

                                                               this.userSM.singleSelect = false;
                                                               this.userSM.clearSelections();
                                                               this.userSM.selectRecords(recuser, true);

                                                               this.authSM.singleSelect = false;
                                                               this.authSM.clearSelections();
                                                               this.authSM.selectRecords(recauth, true);

                                                               this.userSM.resumeEvents();
                                                               this.authSM.resumeEvents();

                                                               this.roleStarBtn.setIconClass('star-icon');
                                                               this.roleRefreshBtn.disable();
                                                               this.fromwhere = 'role';
                                                           },
                                                           failure: function(r,o){
                                                           },
                                                           scope:this
                                                       }
                                                   );
                                               }
                                           },
                                           this);

                       this.rolePanel.on('checkchange',
                                         function(n,b){
                                             var rbbar = this.rolePanel.getBottomToolbar();
                                             // 如果不是在做 bind 操作，只能单选。
                                             if (this.fromwhere=='' || this.fromwhere=='role') {
                                                 if (b) {
                                                     var ck = this.rolePanel.getChecked();
                                                     for(var i =0;i<ck.length;i++){
                                                         if (n.id != ck[i].id ) {
                                                             ck[i].getUI().toggleCheck(false);
                                                             ck[i].attributes.checked=false;
                                                         }
                                                     }
                                                     rbbar.get('role-add-button').enable();
                                                     rbbar.get('role-delete-button').enable();
                                                     rbbar.get('role-edit-button').enable();
                                                     this.roleStarBtn.enable();
                                                 } else { // 没有选择或者多选, 下面按钮无效.
                                                     rbbar.get('role-add-button').disable();
                                                     rbbar.get('role-delete-button').disable();
                                                     rbbar.get('role-edit-button').disable();
                                                     this.roleStarBtn.disable();
                                                 }


                                                 this.fromwhere ='';
                                                 this.checkedUserIds.length=0;
                                                 this.checkedAuthIds.length=0;
                                                 resetAuthFn(this.authSM, this.authPanel.getBottomToolbar(), this.authStarBtn);
                                                 resetUserFn(this.userSM, this.userPanel.getBottomToolbar(), this.userStarBtn);
                                                 this.roleStarBtn.setIconClass('star-off-icon');
                                             } else {
                                                 rbbar.get('role-delete-button').disable();
                                                 rbbar.get('role-edit-button').disable();
                                                 this.roleStarBtn.disable();
                                             }

                                             if (this.fromwhere =='') {
                                                 this.roleRefreshBtn.enable();
                                             } else {
                                                 this.roleRefreshBtn.disable();
                                             }

                                             if (this.fromwhere=='user' ) {
                                                 var url;
                                                 if(b) {
                                                     url = 'RoleServlet.z?ci=bindUser';
                                                 } else {
                                                     url = 'RoleServlet.z?ci=unbindUser';
                                                 }

                                                 Ext.Ajax.request(
                                                     {
                                                         url:url,
                                                         params:{
                                                             'user.id':this.userSM.getSelected().get('id'),
                                                             'role.id':n.id
                                                         },
                                                         method:'POST',
                                                         success: function(r,a){
                                                             var data = Ext.util.JSON.decode(r.responseText);
                                                             this.checkedAuthIds = data.auths;
                                                             var checkedIds = this.checkedAuthIds;
                                                             var records = new Array();
                                                             this.authStore.each(
                                                                 function(record) {
                                                                     for(var i=0;i<checkedIds.length;i++) {
                                                                         if(checkedIds[i] == record.data.id) {
                                                                             records.push(record);
                                                                         }
                                                                     }

                                                                 });
                                                             this.authSM.unlock();
                                                             this.authSM.singleSelect = false;
                                                             this.authSM.clearSelections();
                                                             this.authSM.selectRecords(records, true);
                                                             this.authSM.lock();

                                                         },
                                                         failure: function(r,o){
                                                         },
                                                         scope:this
                                                     }
                                                 );

                                             }

                                             if (this.fromwhere=='auth') {
                                                 var url;
                                                 if(b) {
                                                     url = 'RoleServlet.z?ci=bindAuth';
                                                 } else {
                                                     url = 'RoleServlet.z?ci=unbindAuth';
                                                 }
                                                 Ext.Ajax.request(
                                                     {
                                                         url:url,
                                                         params:{
                                                             'authority.id':this.authSM.getSelected().get('id'),
                                                             'role.id':n.id
                                                         },
                                                         method:'POST',
                                                         success: function(r,a){
                                                             var data = Ext.util.JSON.decode(r.responseText);
                                                             this.checkedUserIds = data.users;
                                                             var checkedIds = this.checkedUserIds;
                                                             var records = new Array();
                                                             this.userStore.each(
                                                                 function(record) {
                                                                     for(var i=0;i<checkedIds.length;i++) {
                                                                         if(checkedIds[i] == record.data.id) {
                                                                             records.push(record);
                                                                         }
                                                                     }

                                                                 });
                                                             this.userSM.unlock();
                                                             this.userSM.singleSelect = false;
                                                             this.userSM.clearSelections();
                                                             this.userSM.selectRecords(records, true);
                                                             this.userSM.lock();

                                                         },
                                                         failure: function(r,o){
                                                         },
                                                         scope:this
                                                     }
                                                 );

                                             }
                                         },
                                         this);

                   }
               });

Ext.reg('system.administration.pm', jibu.security.PM);
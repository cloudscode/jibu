Ext.ns('jibu.security.role');
jibu.security.role.Form = function() {
    jibu.security.role.Form.superclass.constructor.call(this,{
                                                            frame: true,
                                                            items : [{
                                                                         xtype: 'fieldset',
                                                                         labelWidth: 150,
                                                                         title: 'Role Detail',
                                                                         layout: 'form',
                                                                         collapsible: true,
                                                                         defaults: {width: 210},
                                                                         defaultType: 'textfield',
                                                                         autoHeight: true,
                                                                         items: [{
                                                                                     xtype: 'hidden',
                                                                                     name: 'Role.id',
                                                                                     allowBlank:true
                                                                                 },{
                                                                                     xtype: 'hidden',
                                                                                     name: 'pid',
                                                                                     allowBlank:true
                                                                                 },{
                                                                                     fieldLabel: 'Parent Name',
                                                                                     name: 'pname',
                                                                                     allowBlank:true,
                                                                                     disabled:true
                                                                                 },{
                                                                                     fieldLabel: 'Name',
                                                                                     name: 'Role.name',
                                                                                     allowBlank:false
                                                                                 },{
                                                                                     xtype: 'textarea',
                                                                                     fieldLabel: 'Description',
                                                                                     name: 'Role.description',
                                                                                     allowBlank:false
                                                                                 }]
                                                                     },{
                                                                         buttonAlign:'center',
                                                                         buttons: [{
                                                                                       text: 'Submit',
                                                                                       scope:this,
                                                                                       formBind:true,
                                                                                       handler: this.submitFn
                                                                                   },{
                                                                                       text: 'Cancel',
                                                                                       handler: function() {
                                                                                           Ext.WindowMgr.getActive().close();
                                                                                       }
                                                                                   }]
                                                                     }]

                                                        });
};// FormPanel构造函数结束

//  页面上的字符串在这里定义
Ext.extend(jibu.security.role.Form, Ext.FormPanel, {
               submitFn: function() {
                   var rid = this.getForm().findField("Role.id").getValue();
                   var url;
                   if (rid.length > 0) {
                       url = 'RoleServlet.z?ci=roleUpdate';
                   } else {
                       url = 'RoleServlet.z?ci=roleAdd';
                   }

                   this.getForm().submit(
                       {
                           url: url,
                           method: 'POST',
                           disabled:true,
                           waitMsg: 'Submitting...',
                           success: function(form, action) {
                               Ext.Msg.alert('Success', action.result.message);
                           },
                           failure: function(form, action) {
                           }
                       }
                   );
               }

           });

Ext.reg('system.administration.roleform', jibu.security.role.Form);

jibu.security.role.CheckTree = function(config){

    Ext.apply(this, config);
    this.root = new Ext.tree.TreeNode('root');

    this.listeners = {
        render: function(n) {
            Ext.Ajax.request({
                                 url:'RoleServlet.z?ci=getAllRole',
                                 method:'POST',
                                 success: function(r,a){
                                     //Ext.Msg.alert('信息2',r.responseText);
                                     var data = Ext.util.JSON.decode(r.responseText);
                                     this.buildTree(this.root,data);
                                     this.root.expandChildNodes(true);
                                 },
                                 failure: function(r,o){
                                 },
                                 scope:this
                             });

        }
    };

    this.tbar=['->',{
                   id: 'role-refresh-button',
                   xtype: 'button',
                   iconCls: 'refresh-icon',
                   tooltip: 'Reload role tree',
                   scope: this,
                   handler : this.loadRoleFn
               },{
                   id: 'role-star-button',
                   iconCls: 'star-off-icon',
                   disabled:true,
                   tooltip: 'Load binded data.'
               }];

    this.bbar = [{
                     id:'role-add-button',
                     tooltip:'Add Role',
                     iconCls: 'add-icon',
                     disabled:true,
                     scope: this,
                     handler: this.roleAddFn
                 },{
                     id:'role-edit-button',
                     tooltip: 'Edit Role',
                     iconCls: 'edit-icon',
                     scope: this,
                     disabled:true,
                     handler: this.roleEditFn
                 },{
                     id:'role-delete-button',
                     tooltip:'Delete Role',
                     iconCls: 'delete-icon',
                     disabled:true,
                     scope: this,
                     handler: this.roleDeleteFn
                 }];

    jibu.security.role.CheckTree.superclass.constructor.call(this,{
                                                                 rootVisible:false,
                                                                 cls: 'x-tree-noicon'
                                                             });
};

Ext.extend(jibu.security.role.CheckTree,Ext.tree.TreePanel,{
               buildTree :  function(root,data){
                   var child = null;
                   for(var i =0;i<data.length;i++){
                       child = new Ext.tree.TreeNode(data[i]);
                       root.appendChild(child);
                       if(!data[i].leaf)
                           this.buildTree(child,data[i].children);
                   }
               },

               loadRoleFn : function(btn,event){
                   Ext.Ajax.request({
                                        url:'RoleServlet.z?ci=getAllRole',
                                        method:'POST',
                                        success: function(r,a){
                                            //Ext.Msg.alert('信息2',r.responseText);
                                            var data = Ext.util.JSON.decode(r.responseText);
                                            this.root.removeAll(true);
                                            this.buildTree(this.root,data);
                                            this.root.expandChildNodes(true);
                                        },
                                        failure: function(r,o){
                                        },
                                        scope:this
                                    });

               },

               roleAddFn: function(btn,event){
                   var ck = this.getChecked();
                   var win = new Ext.Window({
                                                title: 'Role Add',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.roleform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        this.items.itemAt(0).getForm().findField("pid").setValue(ck[0].id);
                                                        this.items.itemAt(0).getForm().findField("pname").setValue(ck[0].text);
                                                    }
                                                }
                                            });
                   win.show();

               },
               roleEditFn: function(btn,event){
                   var ck = this.getChecked();
                   var win = new Ext.Window({
                                                title: 'Role Edit',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.roleform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        var data = [{id:'Role.id',value:ck[0].id},
                                                                    {id:'Role.name',value:ck[0].text},
                                                                    {id:'Role.description',value:ck[0].attributes['description']}
                                                                   ];
                                                        this.items.itemAt(0).getForm().setValues(data);
                                                        this.items.itemAt(0).getForm().findField("pname").destroy();
                                                    }
                                                }
                                            });
                   win.show();

               },
               roleDeleteFn: function(btn,event){
                   var ck = this.getChecked();
                   var roleDelAjaxFn = function(btn) {
                       if (btn == 'yes') {
                           Ext.Ajax.request(
                               {
                                   url:'RoleServlet.z?ci=roleDelete',
                                   params:{
                                       'id':ck[0].id
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

                   };

                   Ext.MessageBox.show({
                                           title:'删除确认',
                                           msg: '数据一旦删除后将无法恢复。 <br />是否确定要删除此数据？',
                                           buttons: Ext.MessageBox.YESNO,
                                           fn: roleDelAjaxFn,
                                           icon: Ext.MessageBox.WARNING
                                       });
               }

           });

Ext.reg('system.administration.roletree', jibu.security.role.CheckTree);
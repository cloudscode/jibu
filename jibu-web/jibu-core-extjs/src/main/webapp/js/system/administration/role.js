Ext.ns('jibu.security.role');
jibu.security.role.Form = function() {
    jibu.security.role.Form.superclass.constructor.call(this,{
                                                            frame: true,
                                                            items : [{
                                                                         xtype: 'fieldset',
                                                                         labelWidth: 150,
                                                                         title: this.roleDetailText,
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
                                                                                     fieldLabel: this.parentNameText,
                                                                                     name: 'pname',
                                                                                     allowBlank:true,
                                                                                     disabled:true
                                                                                 },{
                                                                                     fieldLabel: this.nameText,
                                                                                     name: 'Role.name',
                                                                                     allowBlank:false
                                                                                 },{
                                                                                     xtype: 'textarea',
                                                                                     fieldLabel: this.descriptionText,
                                                                                     name: 'Role.description',
                                                                                     allowBlank:false
                                                                                 }]
                                                                     },{
                                                                         buttonAlign:'center',
                                                                         buttons: [{
                                                                                       text: this.submitText,
                                                                                       scope:this,
                                                                                       formBind:true,
                                                                                       handler: this.submitFn
                                                                                   },{
                                                                                       text: this.cancelText,
                                                                                       handler: function() {
                                                                                           Ext.WindowMgr.getActive().close();
                                                                                       }
                                                                                   }]
                                                                     }]

                                                        });
};// FormPanel构造函数结束

//  页面上的字符串在这里定义
Ext.extend(jibu.security.role.Form, Ext.FormPanel, {
               roleDetailText:'Role Detail',
               parentNameText:'Parent Name',
               nameText:'Name',
               descriptionText:'Description',
               submitText:'Submit',
               cancelText:'Cancel',
               waitMsgText:'Submitting...',
               submitFn: function() {
                   var rid = this.getForm().findField("Role.id").getValue();
                   var url;
                   if (rid.length > 0) {
                       url = 'Role.z?ci=roleUpdate';
                   } else {
                       url = 'Role.z?ci=roleAdd';
                   }

                   this.getForm().submit(
                       {
                           url: url,
                           method: 'POST',
                           disabled:true,
                           waitMsg: this.waitMsgText,
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
                                 url:'Role.z?ci=getAllRole',
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
                   tooltip: this.refreshTooltip,
                   scope: this,
                   handler : this.loadRoleFn
               },{
                   id: 'role-star-button',
                   iconCls: 'star-off-icon',
                   disabled:true,
                   tooltip: this.starTooltip
               }];

    this.bbar = [{
                     id:'role-add-button',
                     tooltip:this.addTooltip,
                     iconCls: 'add-icon',
                     disabled:true,
                     scope: this,
                     handler: this.roleAddFn
                 },{
                     id:'role-edit-button',
                     tooltip: this.editTooltip,
                     iconCls: 'edit-icon',
                     scope: this,
                     disabled:true,
                     handler: this.roleEditFn
                 },{
                     id:'role-delete-button',
                     tooltip:this.delTooltip,
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
               refreshTooltip:'Reload role tree',
               starTooltip:'Load binded data.',
               addTooltip:'Add Role',
               editTooltip:'Edit Role',
               delTooltip:'Delete Role',
               roleAddTitle:'Role Add',
               roleEditTitle:'Role Edit',
               delMsgTitle:'Delete',
               delMsgText:'Are you sure you want to permanently delete the data?',
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
                                        url:'Role.z?ci=getAllRole',
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
                                                title: this.roleAddTitle,
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
                                                title: this.roleEditTitle,
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
                                   url:'Role.z?ci=roleDelete',
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
                                           title:this.delMsgTitle,
                                           msg: this.delMsgText,
                                           buttons: Ext.MessageBox.YESNO,
                                           fn: roleDelAjaxFn,
                                           icon: Ext.MessageBox.WARNING
                                       });
               }

           });

Ext.reg('system.administration.roletree', jibu.security.role.CheckTree);
Ext.ns('jibu.security.user');
jibu.security.user.Form = function() {
    jibu.security.user.Form.superclass.constructor.call(this,{
                                                            frame: true,
                                                            items : [{
                                                                         xtype: 'fieldset',
                                                                         labelWidth: 150,
                                                                         title: 'User Detail',
                                                                         layout: 'form',
                                                                         collapsible: true,
                                                                         defaults: {width: 210},
                                                                         defaultType: 'textfield',
                                                                         autoHeight: true,
                                                                         items: [{
                                                                                     xtype: 'hidden',
                                                                                     name: 'User.id',
                                                                                     allowBlank:true
                                                                                 },{
                                                                                     fieldLabel: 'Username',
                                                                                     name: 'User.username',
                                                                                     allowBlank:false
                                                                                 },{
                                                                                     fieldLabel: 'Fullname',
                                                                                     name: 'User.fullname',
                                                                                     allowBlank:false
                                                                                 },{
                                                                                     fieldLabel: 'Email Address',
                                                                                     name: 'User.emailaddress'
                                                                                 },{
                                                                                     fieldLabel: 'Password',
                                                                                     name: 'User.password',
                                                                                     inputType: 'password'
                                                                                 },{
                                                                                     xtype: 'radiogroup',
                                                                                     fieldLabel: 'Enabled',
                                                                                     name: 'User.enabled',
                                                                                     items: [{
                                                                                                 inputValue: 'true',
                                                                                                 boxLabel: 'Yes',
                                                                                                 checked:true,
                                                                                                 name: 'User.enabled'

                                                                                             }, {
                                                                                                 inputValue: 'false',
                                                                                                 boxLabel: 'No',
                                                                                                 name: 'User.enabled'
                                                                                             }],
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
Ext.extend(jibu.security.user.Form, Ext.FormPanel, {
               submitFn: function() {
                   var uid = this.getForm().findField('User.id').getValue();
                   var url;
                   if (uid.length > 0) {
                       url = 'UserServlet.z?ci=userUpdate';
                   } else {
                       url = 'UserServlet.z?ci=userAdd';
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

Ext.reg('system.administration.userform', jibu.security.user.Form);

jibu.security.user.Grid = function(config){
    Ext.apply(this, config);
    this.store = new Ext.data.JsonStore({
                                            // destroy the store if the grid is destroyed
                                            autoDestroy: true,
                                            remoteSort : true,
                                            url: 'UserServlet.z?ci=userFind',
                                            root: 'data',
                                            fields: [
                                                {name: 'id', type: 'int'},
                                                {name: 'username', type: 'string'},
                                                {name: 'password', type: 'string'},
                                                {name: 'fullname', type: 'string'},
                                                {name: 'emailaddress', type: 'string'},
                                                {name: 'enabled', type: 'bool'}],
                                            sortInfo: {field:'username', direction:'ASC'}
                                        });
    this.sm = new Ext.grid.CheckboxSelectionModel({
                                                      singleSelect:true,
                                                      sortable:true,
                                                      header:''
                                                  });
    this.columns = [new Ext.grid.RowNumberer(),
                    {header: 'Username', width: 80, sortable: true, dataIndex: 'username'},
                    {header: 'Full Name', width: 80, sortable: true,  dataIndex: 'fullname'},
                    {header: 'Enabled', width: 50, sortable: true, dataIndex: 'enabled'},
                    this.sm];
    this.tbar = [{
                     xtype : 'combo',
                     name : 'searchType',
                     width: 100,
                     emptyText: 'Select column',
                     store: new Ext.data.ArrayStore({
                                                        fields: ['prop', 'displayText'],
                                                        data: [
                                                            ['User.username', 'Username']
                                                        ]
                                                    }),
                     valueField : 'prop',
                     displayField:'displayText',
                     mode: 'local',
                     scope: this,
                     triggerAction: 'all'
                 },{
                     text: '<b>=</b>'
                 },{
                     xtype: 'field',
                     width: 100,
                     name: 'searchValue',
                     scope: this
                 },{
                     xtype: 'button',
                     tooltip: 'Search',
                     scope: this,
                     iconCls :'search-icon',
                     handler : this.searchUserFn
                 },'->',{
                     id: 'user-star-button',
                     iconCls: 'star-off-icon',
                     disabled:true,
                     tooltip: 'Load binded data.'
                 }];

    // paging bar on the bottom
    this.bbar = new Ext.PagingToolbar(
        {
            pageSize: 30,
            store: this.store,
            displayInfo: false,
            items:[
                '-',
                {
                    iconCls: 'add-icon',
                    tooltip: 'Add User',
                    handler: this.userAddFn
                },{
                    id:'user-edit-button',
                    tooltip: 'Edit User',
                    iconCls: 'edit-icon',
                    scope: this,
                    disabled:true,
                    handler: this.userEditFn
                },{
                    id:'user-delete-button',
                    tooltip: 'Delete User',
                    iconCls: 'delete-icon',
                    scope: this,
                    disabled:true,
                    handler: this.userDeleteFn
                }]
        });

    this.listeners = {
        render: function(n) {
            this.store.load({
                                scope:this,
                                params:{start:0,limit:30}
                            });

        }
    };

    jibu.security.user.Grid.superclass.constructor.call(this,{
                                                            stripeRows: true
                                                        });

};

Ext.extend(jibu.security.user.Grid,Ext.grid.GridPanel,{
               searchUserFn : function(btn,event){
                   var key = this.getTopToolbar().find('name','searchType')[0].getValue();
                   var value = this.getTopToolbar().find('name','searchValue')[0].getValue();
                   var params = {start:0,limit:30};
                   params[key]=value;
                   this.getStore().load({
                                            scope:this,
                                            params:params,
                                            callback:function(records,o,success){
                                            }
                                        });
               },
               userAddFn: function(btn,event){
                   var win = new Ext.Window({
                                                title: 'User Add',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.userform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        this.addPanel = this.items.itemAt(0);
                                                    }
                                                }
                                            });
                   win.show();

               },
               userEditFn: function(btn,event){
                   var record = this.getSelectionModel().getSelected();
                   var win = new Ext.Window({
                                                title: 'User Edit',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.userform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        var data = [{id:'User.id',value:record.get('id')},
                                                                    {id:'User.username',value:record.get('username')},
                                                                    {id:'User.fullname',value:record.get('fullname')},
                                                                    {id:'User.password',value:record.get('password')},
                                                                    {id:'User.emailaddress',value:record.get('emailaddress')},
                                                                    {id:'User.enabled',value:record.get('enabled')}
                                                                   ];
                                                        this.items.itemAt(0).getForm().setValues(data);
                                                        this.items.itemAt(0).getForm().findField('User.username').disable();
                                                    }
                                                }
                                            });
                   win.show();

               },
               userDeleteFn: function(btn,event){
                   var record = this.getSelectionModel().getSelected();
                   var store = this.getStore();
                   var userDelAjaxFn = function(btn) {
                       if (btn == 'yes') {
                           Ext.Ajax.request(
                               {
                                   url:'UserServlet.z?ci=userDelete',
                                   params:{
                                       'id':record.get('id')
                                   },
                                   method:'POST',
                                   success: function(r,a){
                                       store.reload();
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
                                           fn: userDelAjaxFn,
                                           icon: Ext.MessageBox.WARNING
                                       });
               }

           });
Ext.reg('system.administration.usergrid', jibu.security.user.Grid);
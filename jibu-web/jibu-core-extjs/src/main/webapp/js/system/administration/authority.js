Ext.ns('jibu.security.authority');
jibu.security.authority.Form = function() {
    jibu.security.authority.Form.superclass.constructor.call(this,{
                                                                 frame: true,
                                                                 items : [{
                                                                              xtype: 'fieldset',
                                                                              labelWidth: 100,
                                                                              title: 'Authority Detail',
                                                                              layout: 'form',
                                                                              collapsible: true,
                                                                              defaults: {width: 250},
                                                                              defaultType: 'textfield',
                                                                              autoHeight: true,
                                                                              items: [{
                                                                                          xtype: 'hidden',
                                                                                          name: 'Authority.id',
                                                                                          allowBlank:true
                                                                                      },{
                                                                                          fieldLabel: 'Name',
                                                                                          name: 'Authority.name',
                                                                                          allowBlank:false
                                                                                      },{
                                                                                          fieldLabel: 'Value',
                                                                                          name: 'Authority.value',
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
Ext.extend(jibu.security.authority.Form, Ext.FormPanel, {
               submitFn: function() {
                   var authid = this.getForm().findField("Authority.id").getValue();
                   var url;
                   if (authid.length > 0) {
                       url = '/AuthorityServlet.z?ci=authUpdate';
                   } else {
                       url = '/AuthorityServlet.z?ci=authAdd';
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

Ext.reg('system.administration.authform', jibu.security.authority.Form);

jibu.security.authority.Grid = function(config){
    Ext.apply(this, config);
    this.store = new Ext.data.JsonStore({
                                            // destroy the store if the grid is destroyed
                                            autoDestroy: true,
                                            url: '/AuthorityServlet.z?ci=authFind',
                                            root: 'data',
                                            fields: [
                                                {name: 'id', type: 'int'},
                                                {name: 'name', type: 'string'},
                                                {name: 'value', type: 'string'}],
                                            sortInfo: {field:'name', direction:'ASC'}
                                        });
    this.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,sortable:true,header:''});
    this.columns = [new Ext.grid.RowNumberer(),
                    {header: 'Name', width: 140, sortable: true, dataIndex: 'name'},
                    {header: 'Value', width: 100, sortable: true,  dataIndex: 'value'},
                    this.sm];
    this.tbar = [{
                     xtype : 'combo',
                     name : 'searchType',
                     width: 130,
                     emptyText: 'Select a property...',
                     store: new Ext.data.ArrayStore({
                                                        fields: ['prop', 'displayText'],
                                                        data: [
                                                            ['Authority.name', 'Name']
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
                     name: 'searchValue',
                     scope: this
                 },{
                     xtype: 'button',
                     tooltip: 'Search',
                     scope: this,
                     iconCls :'search-icon',
                     handler : this.searchAuthFn
                 },'->',{
                     id: 'auth-star-button',
                     iconCls: 'star-off-icon',
                     disabled:true,
                     tooltip: 'Load binded data.'
                 }];

    // paging bar on the bottom
    this.bbar = [
        {
            iconCls: 'add-icon',
            tooltip: 'Add Authority',
            handler: this.authAddFn
        },{
            id:'auth-edit-button',
            tooltip: 'Edit Authority',
            iconCls: 'edit-icon',
            scope: this,
            disabled:true,
            handler: this.authEditFn
        },{
            id:'auth-delete-button',
            tooltip: 'Delete Authority',
            iconCls: 'delete-icon',
            scope: this,
            disabled:true,
            handler: this.authDeleteFn
        }];

    this.listeners = {
        render: function(n) {
            this.store.load({
                                scope:this,
                                params:{}
                            });

        }
    };

    jibu.security.authority.Grid.superclass.constructor.call(this,{
                                                                 stripeRows: true,
                                                                 border: false
                                                             });

};

Ext.extend(jibu.security.authority.Grid,Ext.grid.GridPanel,{
               searchAuthFn : function(btn,event){
                   var key = this.getTopToolbar().find('name','searchType')[0].getValue();
                   var value = this.getTopToolbar().find('name','searchValue')[0].getValue();
                   var params = {};
                   params[key]=value;
                   this.getStore().load({
                                            Scope:this,
                                            params:params,
                                            callback:function(records,o,success){
                                            }
                                        });
               },
               authAddFn: function(btn,event){
                   var win = new Ext.Window({
                                                title: 'Authority Add',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.authform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        this.addPanel = this.items.itemAt(0);
                                                    }
                                                }
                                            });
                   win.show();

               },
               authEditFn: function(btn,event){
                   var record = this.getSelectionModel().getSelected();
                   var win = new Ext.Window({
                                                title: 'Authority Edit',
                                                width:500,
                                                height:300,
                                                border:false,
                                                plain: true,
                                                layout:'fit',
                                                items:[{
                                                           xtype:'system.administration.authform'
                                                       }],
                                                listeners:{
                                                    show:function(){
                                                        var data = [{id:'Authority.id',value:record.get('id')},
                                                                    {id:'Authority.name',value:record.get('name')},
                                                                    {id:'Authority.value',value:record.get('value')}
                                                                   ];
                                                        this.items.itemAt(0).getForm().setValues(data);
                                                    }
                                                }
                                            });
                   win.show();

               },
               authDeleteFn: function(btn,event){
                   var record = this.getSelectionModel().getSelected();
                   var authDelAjaxFn = function(btn) {
                       if (btn == 'yes') {
                           Ext.Ajax.request(
                               {
                                   url:"/AuthorityServlet.z?ci=authDelete",
                                   params:{
                                       'id':record.get("id")
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
                                           fn: authDelAjaxFn,
                                           icon: Ext.MessageBox.WARNING
                                       });
               }
           });

Ext.reg('system.administration.authgrid', jibu.security.authority.Grid);
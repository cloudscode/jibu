Ext.ns('jibu.security.authority');
jibu.security.authority.Form = function() {
    jibu.security.authority.Form.superclass.constructor.call(this,{
                                                                 frame: true,
                                                                 items : [{
                                                                              xtype: 'fieldset',
                                                                              labelWidth: 100,
                                                                              title: this.authDetailText,
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
                                                                                          fieldLabel: this.nameText,
                                                                                          name: 'Authority.name',
                                                                                          allowBlank:false
                                                                                      },{
                                                                                          fieldLabel: this.valueText,
                                                                                          name: 'Authority.value',
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
Ext.extend(jibu.security.authority.Form, Ext.FormPanel, {
               authDetailText:'Authority Detail',
               nameText:'Name',
               valueText:'Value',
               submitText:'Submit',
               cancelText:'Cancel',
               waitMsgText:'Submitting...',
               submitFn: function() {
                   var authid = this.getForm().findField('Authority.id').getValue();
                   var url;
                   if (authid.length > 0) {
                       url = 'AuthorityServlet.z?ci=authUpdate';
                   } else {
                       url = 'AuthorityServlet.z?ci=authAdd';
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

Ext.reg('system.administration.authform', jibu.security.authority.Form);

jibu.security.authority.Grid = function(config){
    Ext.apply(this, config);
    this.store = new Ext.data.JsonStore({
                                            // destroy the store if the grid is destroyed
                                            autoDestroy: true,
                                            url: 'AuthorityServlet.z?ci=authFind',
                                            root: 'data',
                                            fields: [
                                                {name: 'id', type: 'int'},
                                                {name: 'name', type: 'string'},
                                                {name: 'value', type: 'string'}],
                                            sortInfo: {field:'name', direction:'ASC'}
                                        });
    this.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,sortable:true,header:''});
    this.columns = [new Ext.grid.RowNumberer(),
                    {header: this.nameText,  width: 100, sortable: true, dataIndex: 'name'},
                    {header: this.valueText, width: 100, sortable: true,  dataIndex: 'value'},
                    this.sm];
    this.tbar = [{
                     xtype : 'combo',
                     name : 'searchType',
                     width: 100,
                     emptyText: this.selectColumnText,
                     store: new Ext.data.ArrayStore({
                                                        fields: ['prop', 'displayText'],
                                                        data: [
                                                            ['Authority.name', this.nameText],
                                                            ['Authority.value', this.valueText]
                                                        ]
                                                    }),
                     valueField : 'prop',
                     displayField:'displayText',
                     mode: 'local',
                     scope: this,
                     triggerAction: 'all'
                 },'  ',{
                     xtype:'displayfield',
                     html: '<b>=</b>'
                 },'  ',{
                     xtype: 'field',
                     width: 100,
                     name: 'searchValue',
                     scope: this
                 },{
                     xtype: 'button',
                     tooltip: this.searchTooltip,
                     scope: this,
                     iconCls :'search-icon',
                     handler : this.searchAuthFn
                 },'->',{
                     id: 'auth-star-button',
                     iconCls: 'star-off-icon',
                     disabled:true,
                     tooltip: this.starTooltip
                 }];

    // paging bar on the bottom
    this.bbar = [
        {
            iconCls: 'add-icon',
            tooltip: this.addTooltip,
            handler: this.authAddFn
        },{
            id:'auth-edit-button',
            tooltip: this.editTooltip,
            iconCls: 'edit-icon',
            scope: this,
            disabled:true,
            handler: this.authEditFn
        },{
            id:'auth-delete-button',
            tooltip: this.delTooltip,
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
                                                                 stripeRows: true
                                                             });

};

Ext.extend(jibu.security.authority.Grid,Ext.grid.GridPanel,{
               nameText:'Name',
               valueText:'Value',
               selectColumnText:'Select Within',
               searchTooltip: 'Search',
               starTooltip:'Load binded data.',
               addTooltip:'Add Authority',
               editTooltip:'Edit Authority',
               delTooltip:'Delete Authority',
               authAddTitle:'Authority Add',
               authEditTitle:'Authority Edit',
               delMsgTitle:'Delete',
               delMsgText:'Are you sure you want to permanently delete the data?',
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
                                                title: this.authAddTitle,
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
                                                title: this.authEditTitle,
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
                                   url:'AuthorityServlet.z?ci=authDelete',
                                   params:{
                                       'id':record.get('id')
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
                                           fn: authDelAjaxFn,
                                           icon: Ext.MessageBox.WARNING
                                       });
               }
           });

Ext.reg('system.administration.authgrid', jibu.security.authority.Grid);
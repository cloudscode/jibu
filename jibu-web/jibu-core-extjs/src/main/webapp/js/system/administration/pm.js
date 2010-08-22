Ext.ns('jibu.security.user');

jibu.security.user.SearchGrid = function(config){
    Ext.apply(this, config);
    this.store = new Ext.data.JsonStore({
                                            // destroy the store if the grid is destroyed
                                            autoDestroy: true,
                                            remoteSort : true,
                                            url: '/PMServlet.z?ci=find',
                                            root: 'data',
                                            fields: [
                                                {name: 'id', type: 'int'},
                                                {name: 'username', type: 'string'},
                                                {name: 'password', type: 'string'},
                                                {name: 'fullname', type: 'string'},
                                                {ame: 'emailaddress', type: 'string'},
                                                {name: 'enabled', type: 'bool'}],
                                            sortInfo: {field:'username', direction:'ASC'}
                                        });
    this.sm = new Ext.grid.CheckboxSelectionModel();
    this.columns = [new Ext.grid.RowNumberer(),
                    {header: 'Username', width: 160, sortable: true, dataIndex: 'username'},
                    {header: 'Full Name', width: 160, sortable: true,  dataIndex: 'fullname'},
                    {header: 'Email Address', width: 200, sortable: true,  dataIndex: 'emailaddress'},
                    {header: 'Enabled', width: 75, sortable: true, dataIndex: 'enabled'},
                    this.sm];
    this.tbar = [{
                     xtype : 'combo',
                     name : 'searchType',
                     width: 130,
                     emptyText: 'Select a property...',
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
                     name: 'searchValue',
                     scope: this
                 },{
                     xtype: 'button',
                     text: 'Search',
                     scope: this,
                     iconCls :'search-icon',
                     handler : this.searchUserFun
                 }];

    // paging bar on the bottom
    this.bbar = new Ext.PagingToolbar({
                                          pageSize: 25,
                                          store: this.store,
                                          displayInfo: true,
                                          items:[
                                              '-', 
                                              {
                                                  enableToggle:true,
                                                  text: 'Add User',
                                                  iconCls: 'add-icon',
                                                  toggleHandler: function(btn, pressed){
                                                  }
                                              },{
                                                  enableToggle:true,
                                                  text: 'Delete User',
                                                  iconCls: 'delete-icon',
                                                  toggleHandler: function(btn, pressed){
                                                  }
                                              }]
                                      });

    jibu.security.user.SearchGrid.superclass.constructor.call(this,{
                                                                  stripeRows: true,
                                                                  height: 650,
                                                                  width: 800,
                                                                  border: false
                                                              });

};

Ext.extend(jibu.security.user.SearchGrid,Ext.grid.GridPanel,{
               searchUserFun : function(btn,event){
                   var key = this.getTopToolbar().find('name','searchType')[0].getValue();
                   var value = this.getTopToolbar().find('name','searchValue')[0].getValue();
                   var params = {start:0,limit:25};
                   params[key]=value;
                   this.getStore().load({
                                            scope:this,
                                            params:params,
                                            callback:function(records,o,success){
                                                console.log(success);
                                            }
                                        });
               }
           });

Ext.reg('system.administration.pm', jibu.security.user.SearchGrid);

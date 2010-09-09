Ext.BLANK_IMAGE_URL = "ext/resources/images/default/s.gif";
Ext.ns('jibu.layout.classic');

jibu.layout.classic.HeaderPanel = function(){
    this.bbar = new Ext.Toolbar({
                                    border:true,
                                    items : ['->',{
                                                 text:this.userText,
                                                 tooltip: 'User Setting',
                                                 iconCls :'user',
                                                 handler : function() {
                                                     mainPanel.loadModule('security/userSetting.js', 'User Setting');
                                                 }
                                             },'-',{
                                                 text:this.exitText,
                                                 tooltip: 'Exit',
                                                 iconCls :'exit',
                                                 handler : function() {
                                                     window.location = '/LoginServlet.x?ci=logout';
                                                 }
                                             }]
                                });

    jibu.layout.classic.HeaderPanel.superclass.constructor.call(this, {
                                                                    id:'main-header',
                                                                    cls:"main-header",
                                                                    border:false,
                                                                    region:"north",
                                                                    height:65,
                                                                    margins:"0 0 0 0",
                                                                    el:"header"
                                                                });
};

Ext.extend(jibu.layout.classic.HeaderPanel, Ext.Panel, {
               userText:'User',
               exitText:'Exit'
           });

jibu.layout.classic.NavPanel = function() {
    this.loader =  new Ext.tree.TreeLoader({
                                               preloadChildren: true,
                                               clearOnLoad: false
                                           });
    this.root = new Ext.tree.AsyncTreeNode({
                                               text:'Jibu',
                                               expanded:true,
                                               children:JibuNav.data
                                           });
    this.tbar = [' ',
                 new Ext.form.TextField({
                                            width: 150,
                                            emptyText:'Find a Module',
                                            enableKeyEvents: true,
                                            listeners:{
                                                render: function(f){
                                                    this.filter = new Ext.tree.TreeFilter(this, {
                                                                                              clearBlank: true,
                                                                                              autoClear: true
                                                                                          });
                                                },
                                                keydown: {
                                                    fn: this.filterTree,
                                                    buffer: 350,
                                                    scope: this
                                                },
                                                scope: this
                                            }
                                        }), ' ', ' ',
                 {
                     iconCls:'icon-expand-all',
                     tooltip:this.expandText,
                     handler: function(){this.root.expand(true); },
                     scope: this
                 },'-',{
                     iconCls:'icon-collapse-all',
                     tooltip:this.collapseText,
                     handler: function(){this.root.collapse(true); },
                     scope: this
                 }];

    jibu.layout.classic.NavPanel.superclass.constructor.call(this, {
                                                                 id:'module-tree',
                                                                 region:"west",
                                                                 split:true,
                                                                 header:false,
                                                                 width:220,
                                                                 minSize:175,
                                                                 maxSize:500,
                                                                 collapsible:true,
                                                                 margins:"0 0 5 5",
                                                                 cmargins:"0 0 0 0",
                                                                 rootVisible:false,
                                                                 autoScroll:true,
                                                                 collapseMode:"mini",
                                                                 collapseFirst:false
                                                             });
};

Ext.extend(jibu.layout.classic.NavPanel, Ext.tree.TreePanel, {
               expandText:'Expand All',
               collapseText:'Collapse All',
               allModulesText:'All Modules',
               filterTree: function(t, e){
                   var text = t.getValue();
                   Ext.each(this.hiddenPkgs, function(n){
                                n.ui.show();
                            });
                   if(!text){
                       this.filter.clear();
                       return;
                   }
                   this.expandAll();

                   var re = new RegExp('^' + Ext.escapeRe(text), 'i');
                   this.filter.filterBy(function(n){
                                            return !n.isLeaf() || re.test(n.text);
                                        });

                   // hide empty packages that weren't filtered
                   this.hiddenPkgs = [];
                   var me = this;
                   this.root.cascade(function(n){
                                         if(!n.isLeaf() && n.ui.ctNode.offsetHeight < 3){
                                             n.ui.hide();
                                             me.hiddenPkgs.push(n);
                                         }
                                     });
               }
           });

jibu.layout.classic.MainPanel = function() {
    this.items = [{
                      title: this.centerPanelText,
                      autoScroll:true
                  }];

    jibu.layout.classic.MainPanel.superclass.constructor.call(this, {
                                                                  region:'center',
                                                                  margins:'0 5 5 0',
                                                                  enableTabScroll : true,
                                                                  minTabWidth     : 75,
                                                                  activeTab:0
                                                              });

};

Ext.extend(jibu.layout.classic.MainPanel, Ext.TabPanel, {
               centerPanelText:'Center Panel',
               loadModule : function(moduleName,moduleTitle){
                   var tab;
                   if(!(tab = this.getItem(moduleTitle))){
                       tab = [{
                                  xtype: moduleName,
                                  id:moduleName,
                                  title: moduleTitle,
                                  closable:true,
                                  border:false
                              }];
                       this.add(tab);
                   }
                   this.setActiveTab(moduleName);
               }
           });

Ext.onReady(function() {
                Ext.QuickTips.init();
                Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
                var headerPanel = new jibu.layout.classic.HeaderPanel();
                var navPanel = new jibu.layout.classic.NavPanel();
                mainPanel = new jibu.layout.classic.MainPanel();

                Ext.getCmp('module-tree').on('click', function(node, e){
                                                 if(node.isLeaf()){
                                                     mainPanel.loadModule(node.attributes.url, node.attributes.text);
                                                 }
                                             });

                var viewport = new Ext.Viewport({
                                                    layout:'border',
                                                    items:[
                                                        headerPanel,
                                                        navPanel,
                                                        mainPanel
                                                    ]
                                                });
            });

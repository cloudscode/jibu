Ext.BLANK_IMAGE_URL = "ext/resources/images/default/s.gif";
Ext.ns('jibu.security.main');
HeaderPanel = function() {
    HeaderPanel.superclass.constructor.call(this, {
            id:'main-header',
            cls:"main-header",
            border:false,
            region:"north",
            height:65,
            margins:"0 0 0 0",
            el:"header",
            bbar: new Ext.Toolbar( {
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
                                window.location = 'j_spring_security_logout';
                            }
                        }]
                })
        });
};

//指明NavPanel的父类
jibu.security.main.HeaderPanel=Ext.extend(HeaderPanel, Ext.Panel, {
        userText:'User',
        exitText:'Exit'
    });

NavPanel = function() {
    var buildTree=  function(root,data){
        var child = null;
        for(var i =0;i<data.length;i++){
            child = new Ext.tree.TreeNode(data[i]);
            root.appendChild(child);
            if(!data[i].leaf)
                buildTree(child,data[i].children);
        }       
    }

    // 显式调用父类构造器    
    NavPanel.superclass.constructor.call(this, {
            id:'module-tree',
            region:"west",
            split:true,
            header:false,
            width:255,
            minSize:175,
            maxSize:500,
            collapsible:true,
            margins:"0 0 5 5",
            cmargins:"0 0 0 0",
            rootVisible:false,
            autoScroll:true,
            collapseMode:"mini",
            loader: new Ext.tree.TreeLoader({
                    dataUrl:'/MainServlet.y?ci=loadMenu'
                }),
            root: new Ext.tree.TreeNode('Root'),
            collapseFirst:false,
            listeners: {	
                afterrender: function(n) {
                    Ext.Ajax.request({
                            url:"/MainServlet.y?ci=loadMenu",
                            method:'POST',
                            success: function(r,a){
                                // Ext.Msg.alert('信息2',r.responseText);
                                var data = Ext.util.JSON.decode(r.responseText)
                                buildTree(this.root,data);
                            },
                            failure: function(r,o){
                            },
                            scope:this
                        });

                }
            },
            tbar: [{
                    iconCls:'icon-expand-all',
                    text:this.expandText,
                    handler: function(){this.root.expand(true); },
                    scope: this
                },{
                    iconCls:'icon-collapse-all',
                    text:this.collapseText,
                    handler: function(){this.root.collapse(true); },
                    scope: this
                }]
        });
};

//指明NavPanel的父类
jibu.security.main.NavPanel=Ext.extend(NavPanel, Ext.tree.TreePanel, {
        expandText:'Expand All',
        collapseText:'Collapse All',    
        allModulesText:'All Modules'
    });

MainPanel = function() {
    // 显式调用父类构造器    
    MainPanel.superclass.constructor.call(this, {
            region:'center',
            margins:'0 0 5 0',
            enableTabScroll : true,
            minTabWidth     : 75,
            activeTab:0,
            items:[{
                    title: this.centerPanelText,
                    autoScroll:true
                }]

        });

};

jibu.security.main.MainPanel=Ext.extend(MainPanel, Ext.TabPanel, {
        centerPanelText:'Center Panel',
        loadModule : function(href,tabTitle){
            var tab;
            if(!(tab = this.getItem(tabTitle))){
                tab = new Ext.Panel({
                        id: tabTitle,
                        title: tabTitle,
                        closable:true,
                        autoLoad: {url: href,scripts:true},
                        layout:"fit",
                        border:false
                    });
                this.add(tab);
            }
            this.setActiveTab(tab);
        }
    });

Ext.onReady(function() {
        var headerPanel = new jibu.security.main.HeaderPanel();
        var navPanel = new jibu.security.main.NavPanel();
        mainPanel = new jibu.security.main.MainPanel();

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

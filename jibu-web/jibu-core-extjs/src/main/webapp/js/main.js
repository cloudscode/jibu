Ext.onReady(function() {
        var item1 = new Ext.Panel({
                title: 'Accordion Item 1',
            });
        var item2 = new Ext.Panel({
                title: 'Accordion Item 2',
            });
        var item3 = new Ext.Panel({
                title: 'Accordion Item 3',
            });
        var accordion = new Ext.Panel({
                region:'west',
                margins:'5 0 5 5',
                split:true,
                width: 210,
                layout:'accordion',
                items: [item1, item2, item3]
            });
        var viewport = new Ext.Viewport({
                layout:'border',
                items:[
                       accordion, {
                           region:'center',
                           margins:'5 5 5 0',
                           bodyStyle:'background:#f1f1f1',
                       }]
            });
    });


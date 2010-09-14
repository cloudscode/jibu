Ext.ns('jibu.msg');
jibu.msg.bus = function(all,resp){

    if (resp.getResponseHeader('Content-type')=='application/json;charset=UTF-8'){
        var data = Ext.util.JSON.decode(resp.responseText);
        if (data.message) {
            if (data.success) {
                Ext.get('message-board').update('<span style="color:green;font-weight:bold;">'+data.message+'</span>');
            } else {
                Ext.get('message-board').update('<span style="color:red;font-weight:bold;">'+data.message+'</span>');
            }
        }
    } else {
        Ext.get('message-board').update('<span style="color:red;font-weight:bold;">您的网页已过期,请重新登录!</span>');
    }
};
Ext.Ajax.on('requestcomplete', jibu.msg.bus, this);
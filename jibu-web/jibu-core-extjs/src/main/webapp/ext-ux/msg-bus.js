Ext.ns('jibu.msg');
jibu.msg.bus = function(all,resp){
    if (resp.getResponseHeader('Content-type')=='application/json;charset=UTF-8'){
        var data = Ext.util.JSON.decode(resp.responseText);
        if (data.message) {
            jibu.msg.show(data.message,data.success);
        }
    } else {
        resp.responseText='';
        window.location = 'Login.x?ci=logout&reason=sessionExpired';
    }
};
Ext.Ajax.on('requestcomplete', jibu.msg.bus, this);

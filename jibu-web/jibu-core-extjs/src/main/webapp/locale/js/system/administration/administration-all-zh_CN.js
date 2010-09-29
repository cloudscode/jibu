if(jibu.security.user.Form) {
    Ext.override(jibu.security.user.Form, {
                     userDetailText: '用户信息',
                     usernameText: '用户名',
                     fullnameText:'全名',
                     emailText: '电子邮件',
                     passwordText:'密码',
                     enabledText:'是否有效',
                     submitText:'提交',
                     cancelText:'取消',
                     waitMsg:'提交中...'
                 });
}

if(jibu.security.user.Grid) {
    Ext.override(jibu.security.user.Grid, {
                     usernameText: '用户名',
                     fullnameText:'全名',
                     enabledText:'是否有效',
                     selectColumnText:'选择要查询的列',
                     searchTooltip: '查询',
                     starTooltip:'加载已绑定数据',
                     addTooltip:'增加用户',
                     editTooltip:'修改用户',
                     delTooltip:'删除用户',
                     userAddTitle:'增加用户',
                     userEditTitle:'修改用户',
                     delMsgTitle:'删除',
                     delMsgText:'确定要永久的删除此数据？'
                 });
}

if(jibu.security.role.Form) {
    Ext.override(jibu.security.role.Form, {
                     roleDetailText:'角色信息',
                     parentNameText:'父角色名称',
                     nameText:'角色名',
                     descriptionText:'描述',
                     submitText:'提交',
                     cancelText:'取消',
                     waitMsgText:'提交中...'
                 });
}

if(jibu.security.role.CheckTree) {
    Ext.override(jibu.security.role.CheckTree, {
                     refreshTooltip:'重新加载角色树',
                     starTooltip:'加载已绑定数据',
                     addTooltip:'增加角色',
                     editTooltip:'修改角色',
                     delTooltip:'删除角色',
                     roleAddTitle:'增加角色',
                     roleEditTitle:'修改角色',
                     delMsgTitle:'删除',
                     delMsgText:'确定要永久的删除此数据？'
                 });
}

if(jibu.security.authority.Form) {
    Ext.override(jibu.security.authority.Form, {
                     authDetailText:'权限资源信息',
                     nameText:'名称',
                     valueText:'值',
                     submitText:'提交',
                     cancelText:'取消',
                     waitMsgText:'提交中...'
                 });
}

if(jibu.security.authority.Grid) {
    Ext.override(jibu.security.authority.Grid, {
                     nameText:'名称',
                     valueText:'值',
                     selectColumnText:'选择要查询的列',
                     searchTooltip: '查询',
                     starTooltip:'加载已绑定数据',
                     addTooltip:'增加权限资源',
                     editTooltip:'修改权限资源',
                     delTooltip:'删除权限资源',
                     authAddTitle:'增加权限资源',
                     authEditTitle:'修改权限资源',
                     delMsgTitle:'删除',
                     delMsgText:'确定要永久的删除此数据？'
                 });
}

if(jibu.security.monitor.Panel) {
    Ext.override(jibu.security.monitor.Panel, {
	             maxActive : '最大活动连接',
	             numActive : '当前活动连接',
	             numIdle : '当前空闲连接',
                     numSession: '当前Session',
	             totalMemory : '总内存 (MB)',
                     usedMemory: '已用内存 (MB)'
                 });
}

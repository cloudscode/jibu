if(jibu.security.user.Form) {
    Ext.override(jibu.security.user.Form, {
                     userDetailText: 'User Detail',
                     usernameText: 'Username',
                     fullnameText:'Full Name',
                     emailText: 'E-mail',
                     passwordText:'Password',
                     enabledText:'Enabled',
                     submitText:'Submit',
                     cancelText:'Cancel',
                     waitMsg:'Submitting...'
                 });
}

if(jibu.security.user.Grid) {
    Ext.override(jibu.security.user.Grid, {
                     usernameText: 'Username',
                     fullnameText:'Full Name',
                     enabledText:'Enabled',
                     selectColumnText:'Select Within',
                     searchTooltip: 'Search',
                     starTooltip:'Load binded data.',
                     addTooltip:'Add User',
                     editTooltip:'Edit User',
                     delTooltip:'Delete User',
                     userAddTitle:'User Add',
                     userEditTitle:'User Edit',
                     delMsgTitle:'Delete',
                     delMsgText:'Are you sure you want to permanently delete the data?'
                 });
}

if(jibu.security.role.Form) {
    Ext.override(jibu.security.role.Form, {
                     roleDetailText:'Role Detail',
                     parentNameText:'Parent Name',
                     nameText:'Name',
                     descriptionText:'Description',
                     submitText:'Submit',
                     cancelText:'Cancel',
                     waitMsgText:'Submitting...'
                 });
}

if(jibu.security.role.CheckTree) {
    Ext.override(jibu.security.role.CheckTree, {
                     refreshTooltip:'Reload role tree',
                     starTooltip:'Load binded data.',
                     addTooltip:'Add Role',
                     editTooltip:'Edit Role',
                     delTooltip:'Delete Role',
                     roleAddTitle:'Role Add',
                     roleEditTitle:'Role Edit',
                     delMsgTitle:'Delete',
                     delMsgText:'Are you sure you want to permanently delete the data?'
                 });
}

if(jibu.security.authority.Form) {
    Ext.override(jibu.security.authority.Form, {
                     authDetailText:'Authority Detail',
                     nameText:'Name',
                     valueText:'Value',
                     submitText:'Submit',
                     cancelText:'Cancel',
                     waitMsgText:'Submitting...'
                 });
}

if(jibu.security.authority.Grid) {
    Ext.override(jibu.security.authority.Grid, {
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
                     delMsgText:'Are you sure you want to permanently delete the data?'
                 });
}

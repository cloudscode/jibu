Ext.ns('jibu.security.setting');
jibu.security.setting.Form =
    Ext.extend(Ext.form.FormPanel,{
	           fullName : 'Full Name',
                   email : 'E-mail',
                   userName: 'User Name',
                   password: 'New Password',
                   passwordRepeat: 'Re-enter new password',
	           userInformation:'Account Information',
	           settings: 'Preference Settings',
	           theme : 'Theme',
	           language : 'Language',
	           fl_layout : 'Layout',
	           confirmPassword: 'Passwords do not match',
	           passwordInfo: 'Password',
                   oldPassword: 'Current Password',
                   submitText: 'Submit',
                   waitMsgText: 'Submitting...',
	           initComponent:function() {
	               var config = {
	    	           labelWidth: 150,
	    	           frame : true,
	    	           monitorValid:true,
	    	           style:'padding:1px',
	    	           items: [{
	        	               xtype:'fieldset',
	        	               title: this.userInformation,
	        	               collapsible: true,
		                       autoHeight:true,
		                       defaults: {width: 210},
		                       defaultType: 'textfield',
		                       items :[{
		                                   name: 'User.id',
		                                   xtype:'hidden'
	            	                       },{
		                                   fieldLabel: this.fullName,
		                                   name: 'User.fullname',
		                                   allowBlank:false
		                               },{
		                                   fieldLabel: this.email,
		                                   name: 'User.emailaddress',
		                                   vtype:'email'
		                               },{
		                                   fieldLabel: this.userName,
		                                   name: 'User.username',
		                                   allowBlank:false,
		                                   disabled:true
		                               }
		                              ]
		                   },{
	        	               xtype:'fieldset',
	        	               title: this.passwordInfo,
	        	               collapsible: true,
		                       autoHeight:true,
		                       defaults: {width: 210},
		                       defaultType: 'textfield',
		                       items :[{
		                                   fieldLabel: this.oldPassword,
		                                   name: 'oldpassword',
		                                   allowBlank:false,
		                                   inputType: 'password'
		                               }, {
		                                   fieldLabel: this.password,
                                                   id :'setting_newpassword',
		                                   name: 'User.password',
		                                   inputType: 'password'
		                               }, {
		                                   fieldLabel: this.passwordRepeat,
		                                   name: 'passwordAgain',
		                                   inputType: 'password',
		                                   vtype: 'passwordAgain'
		                               }
		                              ]
		                   },{
		                       xtype:'fieldset',
		                       title: this.settings,
		                       collapsible: true,
		                       autoHeight:true,
		                       defaults: {width: 210},
		                       defaultType: 'textfield',
		                       items :[
		        	           new Ext.form.ComboBox({
			        	                             store:new Ext.data.JsonStore({
			            	                                                              fields:[{name:'id', type:'int'},
			    	        	                                                              {name:'value', type:'string'}],
			            	                                                              url:'SettingServlet.y?ci=settingLoad',
                                                                                                      root:'settings',
			            	                                                              baseParams:{'settings.name' :'language'}
			            	                                                          }),
			                                             displayField:'value',
			           	                             valueField:'id',
			           	                             id:'setting_language',
			                                             hiddenId :'h_setting_language',
			           	                             hiddenName:'settings.id',
			                                             triggerAction:'all',
			                                             fieldLabel: this.language,
			                                             selectOnFocus:true
			        	                         }),
		        	           new Ext.form.ComboBox({
			        	                             store:new Ext.data.JsonStore({
			            	                                                              fields:[{name:'id', type:'int'},
			    	        	                                                              {name:'value', type:'string'}],
			            	                                                              url:'SettingServlet.y?ci=settingLoad',
                                                                                                      root:'settings',
			            	                                                              baseParams:{'settings.name' :'layout'}
			            	                                                          }),
			                                             displayField:'value',
			           	                             valueField:'id',
			           	                             id:'setting_layout',
			                                             hiddenId :'h_setting_layout',
			           	                             hiddenName:'settings.id',
			                                             triggerAction:'all',
			                                             fieldLabel: this.fl_layout,
			                                             selectOnFocus:true
			        	                         }),
		        	           new Ext.form.ComboBox({
			        	                             store:new Ext.data.JsonStore({
			            	                                                              fields:[{name:'id', type:'int'},
			    	        	                                                              {name:'value', type:'string'}],
			            	                                                              url:'SettingServlet.y?ci=settingLoad',
                                                                                                      root:'settings',
			            	                                                              baseParams:{
			        		 	                                                  'settings.name' :'theme'
			            	                                                              }
			            	                                                          }),
			                                             displayField:'value',
			           	                             valueField:'id',
			           	                             id:'setting_theme',
			           	                             hiddenId :'h_setting_theme',
			           	                             hiddenName:'settings.id',
			                                             triggerAction:'all',
			                                             fieldLabel:this.theme,
			                                             selectOnFocus:true
                                                                     /*
		        		                              listeners:{
			        		                      scope: this,
			        		                      'select':function(field,newValue,oldValue){
			        		                      var theme = field.lastSelectionText;
			        		                      if ('gray' == theme){
			        		                      theme = 'xtheme-gray-extend.css';
			        		                      }else{
			        		                      theme = 'xtheme-default.css';
			        		                      }
			        		                      Ext.util.CSS.swapStyleSheet('theme', 'js-lib/ext-ux-js/resources/css/' + theme);
			        		                      }
		        		                              }
                                                                      */
			        	                         })
			               ]
	    		           },{
	    			       buttonAlign:'center',
	    			       buttons: [{
                                                     id:'setting_submitBtn',
	    		                             text: this.submitText,
	    		                             scope:this,
	    		                             formBind:true,
	    		                             handler:function() {
	    			    	                 this.getForm().submit({
	    			                                                   url: 'SettingServlet.y?ci=settingUpdate',
	    			                                                   method: 'POST',
	    			                                                   waitMsg: this.waitMsgText,
	    			                                                   success: function(form, action) {
	    			                                                   },
	    			                                                   failure: function(form, action) {
	    			                                                   }
	    			    	                                       });
	    				             }
	    		                         }]
		    	           }]
	    	       }; // eof config object
		       // apply config
		       Ext.apply(this, Ext.apply(this.initialConfig, config));
		       // call parent
		       jibu.security.setting.Form.superclass.initComponent.apply(this, arguments);

		       //  “再次录入密码”校验器
		       Ext.apply(Ext.form.VTypes, {
			             passwordAgain: function(val, field) {
			                 var password = Ext.getCmp("setting_newpassword").getValue();
			                 if (val != password) {
                                             Ext.getCmp("setting_submitBtn").disable();
			                     return false;
			                 } else {
                                             Ext.getCmp("setting_submitBtn").enable();
			                     return true;
			                 }
			             }

			         });
		       //  处理“再次录入密码”校验未通过时显示的提示信息
		       Ext.apply(Ext.form.VTypes, {
			             passwordAgainText: this.confirmPassword
			         });
		   },// eo funtion initComponent
		   onRender:function(){
	               jibu.security.setting.Form.superclass.onRender.apply(this, arguments);
		       this.form.load({
		                          url: 'SettingServlet.y?ci=formLoad',
 		                          success:function(f,a){
		                              Ext.each(a.result.settings,function(o,i){
		                                           Ext.getCmp('setting_'+o.name).setRawValue(o.value);
		                                           Ext.fly('h_'+Ext.getCmp('setting_'+o.name).getId()).dom.value=o.id;
		                                       });

		                          }
		                      });
		   }

               });

Ext.reg('system.setting', jibu.security.setting.Form);
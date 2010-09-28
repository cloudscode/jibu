Ext.ns('jibu.security.monitor');
jibu.security.monitor.Panel =
    Ext.extend(Ext.Panel,{
	           maxActive : 'Max Active',
	           numActive : 'Current Active',
	           numIdle : 'Current Idle',
                   numSession: 'Current Session',

                   layout:'table',
                   frame: true,
                   layoutConfig: {
                       columns: 2
                   },
	           initComponent:function() {
                       var draw = function(canvas,ymax,data,data2,smax,data3){
                           var ctx = canvas.getContext("2d");
                           // 每次取值后，要使其向左移动，先清空数据区
                           ctx.beginPath();
                           ctx.fillStyle ='#fff';
                           ctx.fillRect(50+1,25-1,canvas.width-(50+25)-2,canvas.height-(2*25));
                           ctx.fill();

                           ctx.beginPath();
                           ctx.lineWidth = 1;
                           ctx.strokeStyle ='#bbb';

                           for (x=50+25;x<625-25;x+=25) {
                               ctx.moveTo(x, 175-25-1);
                               ctx.lineTo(x, 25 );

                           }

                           // 背景 x 轴 (间隔 25)
                           for (y=25;y<(175-25);y+=25) {
                               ctx.moveTo(50+1, y);
                               ctx.lineTo(625-25-1, y );
                           }

                           ctx.stroke();
                           ctx.lineWidth = 1.5;
                           // 单位值在整个 y轴所占的比例。
                           var ydisc = Number(100/ymax).toFixed(0);
                           if(data.length>0) {
                               ctx.beginPath();
                               ctx.strokeStyle ='red';
                               var x = 625-25-(5*data.length)-2;
                               // 不要与左侧Y轴重合，右移3个像素
                               if(data.length==110) {
                                   x = x+3;
                               }
                               // 不要让值为0的数据与x轴重叠，向上移动2个像素
                               var y = 175-25-(data[0]==0 ? 2 : data[0]*ydisc);
                               for(i=0;i<data.length;i++) {
                                   ctx.moveTo(x, y);
                                   // 画实际值
                                   x = 625-25-(5*(data.length-i))+3;
                                   y = 175-25-(data[i]==0 ? 2 : data[i]*ydisc);
                                   ctx.lineTo(x, y);
                               }
                               ctx.closePath();
                               ctx.stroke();
                           }
                           if (data2.length>0){
                               ctx.beginPath();
                               ctx.strokeStyle ='green';
                               var x = 625-25-(5*data2.length)-2;
                               if(data2.length==110) {
                                   x = x+3;
                               }
                               var y = 175-25-(data2[0]==0 ? 2 : data2[0]*ydisc);
                               for(i=0;i<data2.length;i++) {
                                   ctx.moveTo(x, y);
                                   x = 625-25-(5*(data2.length-i))+3;
                                   y = 175-25-(data2[i]==0 ? 2 : data2[i]*ydisc);
                                   ctx.lineTo(x, y);
                               }
                               ctx.closePath();
                               ctx.stroke();
                           }
                           var lydisc = Number(100/smax).toFixed(0);
                           if (data3.length>0){
                               ctx.beginPath();
                               ctx.strokeStyle ='blue';
                               var x = 625-25-(5*data3.length)-2;
                               if(data3.length==110) {
                                   x = x+3;
                               }
                               var y = 175-25-(data3[0]==0 ? 2 : data3[0]*lydisc);
                               for(i=0;i<data3.length;i++) {
                                   ctx.moveTo(x, y);
                                   x = 625-25-(5*(data3.length-i))+3;
                                   y = 175-25-(data3[i]==0 ? 2 : data3[i]*lydisc);
                                   ctx.lineTo(x, y);
                               }
                               ctx.closePath();
                               ctx.stroke();
                           }
                       };

                       var drawLYLable = function(canvas,lCount) {
                           var ctx = canvas.getContext("2d");
                           var lwidth = ctx.measureText(lCount).width;
                           ctx.fillStyle ='#fff';
                           ctx.fillRect(50-4-5-lwidth,50-5,lwidth,10);
                           ctx.fill();

                           ctx.fillStyle ='#000';
                           ctx.fillText(lCount,50-4-5-lwidth,50+3);
                           ctx.stroke();
                       };

                       var drawRYLable = function(canvas,rCount) {
                           var ctx = canvas.getContext("2d");
                           var rwidth = ctx.measureText(rCount).width;
                           ctx.fillStyle ='#fff';
                           ctx.fillRect(600+4+5,50-5,rwidth,10);
                           ctx.fill();

                           ctx.fillStyle ='#000';
                           ctx.fillText(rCount,600+4+5,50+3);
                           ctx.stroke();
                       };

	               var config = {
	    	           items: [{
                                       xtype: 'form',
		                       defaultType: 'textfield',
		                       defaults: {width: 40},
                                       items: [{
                                                   fieldLabel: this.maxActive,
                                                   id:'maxActive',
                                                   readOnly:true
                                               },{
                                                   fieldLabel: this.numActive,
                                                   id:'numActive',
                                                   readOnly:true
                                               },{
                                                   fieldLabel: this.numIdle,
                                                   id:'numIdle',
                                                   readOnly:true
                                               },{
                                                   fieldLabel: this.numSession,
                                                   id:'numSession',
                                                   readOnly:true
                                               }]

                                   },{
                                       xtype: 'panel',
                                       html: "<canvas id=\"cavs_id\"  width=\"625\" height=\"175\">Need html5 support</canvas>",
		                       listeners:{
		                           afterrender:function() {
                                               var actives = new Array();
                                               var idles = new Array();
                                               var sessions = new Array();
                                               var nmax =0;
                                               var canvas = document.getElementById("cavs_id");
                                               // IE excanvas hack
                                               if(Ext.isIE) {
                                                   window.G_vmlCanvasManager.init_(document);
                                               }

                                               if (canvas.getContext) {
                                                   var ctx = canvas.getContext("2d");
                                                   // x 轴
                                                   ctx.moveTo(50, 175-25);
                                                   ctx.lineTo(625 - 25, 175 - 25 );
                                                   // y 轴
                                                   ctx.moveTo(50, 175-25);
                                                   ctx.lineTo(50, 25 );
                                                   // 右侧 Y 轴
                                                   ctx.moveTo(625-25, 175-25);
                                                   ctx.lineTo(625-25, 25 );

                                                   // y 轴刻度 (5个单位)
                                                   var yTickInterval = (175 - 2*25) / 5;
                                                   for (i=0;i<4;i++) {
                                                       ctx.moveTo(50, (175-50)-i*yTickInterval);
                                                       ctx.lineTo(50-4, (175-50)-i*yTickInterval);
                                                       ctx.moveTo(600, (175-50)-i*yTickInterval);
                                                       ctx.lineTo(600+4, (175-50)-i*yTickInterval);
                                                   }

                                                   ctx.stroke();
                                                   ctx.lineWidth = 2;
                                                   ctx.beginPath();
                                                   ctx.strokeStyle ='red';
                                                   ctx.moveTo(200, 175-10);
                                                   ctx.lineTo(220, 175-10 );
                                                   ctx.fillText('Active',225,175-5);
                                                   ctx.stroke();

                                                   ctx.beginPath();
                                                   ctx.strokeStyle ='green';
                                                   ctx.moveTo(300, 175-10);
                                                   ctx.lineTo(320, 175-10 );
                                                   ctx.fillText('Idle',325,175-5);
                                                   ctx.stroke();

                                                   ctx.beginPath();
                                                   ctx.strokeStyle ='blue';
                                                   ctx.moveTo(390, 175-10);
                                                   ctx.lineTo(410, 175-10 );
                                                   ctx.fillText('Session',415,175-5);
                                                   ctx.stroke();

                                                   draw(canvas,0,actives,idles,0,sessions);
                                               }

                                               var intervalID;
                                               function ajax() {
                                                   var cv = document.getElementById("cavs_id");
                                                   if(cv) {
                                                       Ext.Ajax.request({
                                                                            url:'Monitor.z?ci=getCount',
                                                                            method:'POST',
                                                                            success: function(r,a){
                                                                                var data = Ext.util.JSON.decode(r.responseText);
                                                                                Ext.getCmp('maxActive').setValue(data.max);
                                                                                Ext.getCmp('numActive').setValue(data.active);
                                                                                Ext.getCmp('numIdle').setValue(data.idle);
                                                                                Ext.getCmp('numSession').setValue(data.session);
                                                                                actives.push(data.active);
                                                                                idles.push(data.idle);
                                                                                sessions.push(data.session);
                                                                                if(actives.length>110){
                                                                                    actives.shift();
                                                                                    idles.shift();
                                                                                    sessions.shift();
                                                                                }

                                                                                var smax=0;
                                                                                for (i=0; i<sessions.length; ++i) {
                                                                                    smax = Math.max(smax,sessions[i]);
                                                                                }
                                                                                drawRYLable(canvas,smax);

                                                                                if(nmax==0) {
                                                                                    nmax = data.max;
                                                                                    drawLYLable(canvas,nmax);
                                                                                }

                                                                                draw(canvas,data.max,actives,idles,smax,sessions);
                                                                            },
                                                                            failure: function(r,o){
                                                                                if(intervalID) {
                                                                                    clearInterval(intervalID);
                                                                                }
                                                                            },
                                                                            scope:this
                                                                        });
                                                   } else {
                                                       if(intervalID) {
                                                           clearInterval(intervalID);
                                                       }
                                                   }

                                               }
                                               intervalID = setInterval(ajax, 1000);
                                           }
                                       }
                                   }]
                       };

                       // apply config
                       Ext.apply(this, Ext.apply(this.initialConfig, config));

                       // call parent
                       jibu.security.monitor.Panel.superclass.initComponent.apply(this, arguments);

                   }
               });

Ext.reg('system.administration.monitor', jibu.security.monitor.Panel);

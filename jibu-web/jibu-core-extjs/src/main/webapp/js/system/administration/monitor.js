Ext.ns('jibu.security.monitor');
jibu.security.monitor.Panel =
    Ext.extend(Ext.Panel,{
	           maxActive : 'Max Active',
	           numActive : 'Current Active',
	           numIdle : 'Current Idle',
                   numSession: 'Current Session',
	           totalMemory : 'Total Mem (MB)',
                   usedMemory: 'Used Mem (MB)',

                   layout:'table',
                   frame: true,
                   layoutConfig: {
                       columns: 2
                   },
	           initComponent:function() {
                       var draw = function(canvas,ymax,data){
                           var ctx = canvas.getContext("2d");
                           // 每次取值后，要使其向左移动，先清空数据区
                           ctx.beginPath();
                           ctx.fillStyle ='#fff';
                           ctx.fillRect(50+1,25-1,canvas.width-(50+75)-2,canvas.height-(2*25));
                           ctx.fill();

                           ctx.beginPath();
                           ctx.lineWidth = 1;
                           ctx.strokeStyle ='#bbb';

                           for (x=50+25;x<625-75;x+=25) {
                               ctx.moveTo(x, 175-25-1);
                               ctx.lineTo(x, 25 );

                           }

                           // 背景 x 轴 (间隔 25)
                           for (y=25;y<(175-25);y+=25) {
                               ctx.moveTo(50+1, y);
                               ctx.lineTo(625-75-1, y );
                           }

                           ctx.stroke();
                           ctx.lineWidth = 1.5;
                           // 单位值在整个 y轴所占的比例。
                           var ydisc = 100/ymax;
                           if(data.length>0) {
                               ctx.beginPath();
                               ctx.strokeStyle ='red';
                               var x = 625-75-(5*data.length)-2;
                               // 不要与左侧Y轴重合，右移3个像素
                               if(data.length==100) {
                                   x = x+3;
                               }
                               // 不要让值为0的数据与x轴重叠，向上移动2个像素
                               var y = 175-25-(data[0]==0 ? 2 : data[0]*ydisc);
                               for(i=0;i<data.length;i++) {
                                   ctx.moveTo(x, y);
                                   // 画实际值
                                   x = 625-75-(5*(data.length-i))+3;
                                   y = 175-25-(data[i]==0 ? 2 : data[i]*ydisc);
                                   ctx.lineTo(x, y);
                               }
                               ctx.closePath();
                               ctx.stroke();
                           }

                           if (arguments[3]){
                               var data2 = arguments[3];
                               ctx.beginPath();
                               ctx.strokeStyle ='green';
                               var x = 625-75-(5*data2.length)-2;
                               if(data2.length==100) {
                                   x = x+3;
                               }
                               var y = 175-25-(data2[0]==0 ? 2 : data2[0]*ydisc);
                               for(i=0;i<data2.length;i++) {
                                   ctx.moveTo(x, y);
                                   x = 625-75-(5*(data2.length-i))+3;
                                   y = 175-25-(data2[i]==0 ? 2 : data2[i]*ydisc);
                                   ctx.lineTo(x, y);
                               }
                               ctx.closePath();
                               ctx.stroke();
                           }

                           if (arguments[5]){
                               var lydisc = 100/arguments[4];
                               var data3 = arguments[5];
                               ctx.beginPath();
                               ctx.strokeStyle ='blue';
                               var x = 625-75-(5*data3.length)-2;
                               if(data3.length==100) {
                                   x = x+3;
                               }
                               var y = 175-25-(data3[0]==0 ? 2 : data3[0]*lydisc);
                               for(i=0;i<data3.length;i++) {
                                   ctx.moveTo(x, y);
                                   x = 625-75-(5*(data3.length-i))+3;
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
                           ctx.fillRect(550+4+5,50-5,rwidth,10);
                           ctx.fill();

                           ctx.fillStyle ='#000';
                           ctx.fillText(rCount,550+4+5,50+3);
                           ctx.stroke();
                       };

                       var actives = new Array();
                       var idles = new Array();
                       var sessions = new Array();
                       var usedMemory = new Array();
                       var totalMemory = new Array();
                       var nmax =0;

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
                                                var canvas = document.getElementById("cavs_id");
                                                // IE excanvas hack
                                                if(Ext.isIE) {
                                                    window.G_vmlCanvasManager.init_(document);
                                                }

                                                if (canvas.getContext) {
                                                    var ctx = canvas.getContext("2d");
                                                    // x 轴
                                                    ctx.moveTo(50, 175-25);
                                                    ctx.lineTo(625 - 75, 175 - 25 );
                                                    // y 轴
                                                    ctx.moveTo(50, 175-25);
                                                    ctx.lineTo(50, 25 );
                                                    // 右侧 Y 轴
                                                    ctx.moveTo(625-75, 175-25);
                                                    ctx.lineTo(625-75, 25 );

                                                    // y 轴刻度 (5个单位)
                                                    var yTickInterval = (175 - 2*25) / 5;
                                                    for (i=0;i<4;i++) {
                                                        ctx.moveTo(50, (175-50)-i*yTickInterval);
                                                        ctx.lineTo(50-4, (175-50)-i*yTickInterval);
                                                        ctx.moveTo(550, (175-50)-i*yTickInterval);
                                                        ctx.lineTo(550+4, (175-50)-i*yTickInterval);
                                                    }

                                                    ctx.stroke();
                                                    ctx.lineWidth = 2;
                                                    ctx.beginPath();
                                                    ctx.strokeStyle ='red';
                                                    ctx.moveTo(180, 175-10);
                                                    ctx.lineTo(200, 175-10 );
                                                    ctx.fillText('Active',205,175-5);
                                                    ctx.stroke();

                                                    ctx.beginPath();
                                                    ctx.strokeStyle ='green';
                                                    ctx.moveTo(280, 175-10);
                                                    ctx.lineTo(300, 175-10 );
                                                    ctx.fillText('Idle',305,175-5);
                                                    ctx.stroke();

                                                    ctx.beginPath();
                                                    ctx.strokeStyle ='blue';
                                                    ctx.moveTo(370, 175-10);
                                                    ctx.lineTo(390, 175-10 );
                                                    ctx.fillText('Session',395,175-5);
                                                    ctx.stroke();

                                                    draw(canvas,0,actives,idles,0,sessions);
                                                }
                                            }
                                        }
                                    },{
                                        xtype: 'form',
		                        defaultType: 'textfield',
		                        defaults: {width: 40},
                                        items: [{
                                                    fieldLabel: this.totalMemory,
                                                    id:'totalMemory',
                                                    readOnly:true
                                                },{
                                                    fieldLabel: this.usedMemory,
                                                    id:'usedMemory',
                                                    readOnly:true
                                                }]

                                    },{
                                        xtype: 'panel',
                                        html: "<canvas id=\"cavs2_id\"  width=\"625\" height=\"175\">Need html5 support</canvas>",
		                        listeners:{
		                            afterrender:function() {
                                                var canvas = document.getElementById("cavs2_id");
                                                // IE excanvas hack
                                                if(Ext.isIE) {
                                                    window.G_vmlCanvasManager.init_(document);
                                                }

                                                if (canvas.getContext) {
                                                    var ctx = canvas.getContext("2d");
                                                    // x 轴
                                                    ctx.moveTo(50, 175-25);
                                                    ctx.lineTo(625 - 75, 175 - 25 );

                                                    ctx.moveTo(50, 175-25);
                                                    ctx.lineTo(50, 25 );

                                                    // 右侧 Y 轴
                                                    ctx.moveTo(625-75, 175-25);
                                                    ctx.lineTo(625-75, 25 );

                                                    // y 轴刻度 (5个单位)
                                                    var yTickInterval = (175 - 2*25) / 5;
                                                    for (i=0;i<4;i++) {
                                                        ctx.moveTo(50, (175-50)-i*yTickInterval);
                                                        ctx.lineTo(50-4, (175-50)-i*yTickInterval);
                                                        ctx.moveTo(550, (175-50)-i*yTickInterval);
                                                        ctx.lineTo(550+4, (175-50)-i*yTickInterval);
                                                    }

                                                    ctx.stroke();
                                                    ctx.lineWidth = 2;
                                                    ctx.beginPath();
                                                    ctx.strokeStyle ='red';
                                                    ctx.moveTo(280, 175-10);
                                                    ctx.lineTo(300, 175-10 );
                                                    ctx.fillText('Used Memory (MB)',305,175-5);
                                                    ctx.stroke();
                                                    draw(canvas,0,usedMemory);
                                                }
                                            }
                                        }
                                    }],
                           listeners:{
		               afterrender:function() {
                                   var intervalID;
                                   function ajax() {
                                       var cavs = document.getElementById("cavs_id");
                                       var cavs2 = document.getElementById("cavs2_id");
                                       if(cavs && cavs2) {
                                           Ext.Ajax.request({
                                                                url:'Monitor.z?ci=getCount',
                                                                method:'POST',
                                                                success: function(r,a){
                                                                    var data = Ext.util.JSON.decode(r.responseText);
                                                                    Ext.getCmp('maxActive').setValue(data.max);
                                                                    Ext.getCmp('numActive').setValue(data.active);
                                                                    Ext.getCmp('numIdle').setValue(data.idle);
                                                                    Ext.getCmp('numSession').setValue(data.session);
                                                                    Ext.getCmp('totalMemory').setValue(data.totalMemory);
                                                                    Ext.getCmp('usedMemory').setValue(data.usedMemory);
                                                                    actives.push(data.active);
                                                                    idles.push(data.idle);
                                                                    sessions.push(data.session);
                                                                    usedMemory.push(data.usedMemory);
                                                                    totalMemory.push(data.totalMemory);
                                                                    if(actives.length>100){
                                                                        actives.shift();
                                                                        idles.shift();
                                                                        sessions.shift();
                                                                        usedMemory.shift();
                                                                        totalMemory.shift();
                                                                    }

                                                                    var smax=0;
                                                                    for (i=0; i<sessions.length; ++i) {
                                                                        smax = Math.max(smax,sessions[i]);
                                                                    }
                                                                    drawRYLable(cavs,smax);

                                                                    if(nmax==0) {
                                                                        nmax = data.max;
                                                                        drawLYLable(cavs,nmax);
                                                                    }

                                                                    var mmax=0;
                                                                    for (i=0; i<totalMemory.length; ++i) {
                                                                        mmax = Math.max(mmax,totalMemory[i]);
                                                                    }
                                                                    drawRYLable(cavs2,mmax);
                                                                    draw(cavs,data.max,actives,idles,smax,sessions);
                                                                    draw(cavs2,mmax,usedMemory);
                                                                },
                                                                failure: function(r,o){
                                                                    if(intervalID) {
                                                                        clearInterval(intervalID);
                                                                    }
                                                                },
                                                                scope:this
                                                            });
                                       } 

                                   }
                                   intervalID = setInterval(ajax, 2000);

                               }
                           }
                       };

                       // apply config
                       Ext.apply(this, Ext.apply(this.initialConfig, config));

                       // call parent
                       jibu.security.monitor.Panel.superclass.initComponent.apply(this, arguments);

                   }
               });

Ext.reg('system.administration.monitor', jibu.security.monitor.Panel);

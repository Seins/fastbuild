// ;document.write("<script language=javascript src='http://code.jquery.com/jquery-1.11.3.min.js'></script>")
;(function($, window, document,undefined) {
    //定义layout的构造函数
    var layout = function(opt) {
        this.defaults = {
        	'render_id':null,
            'border_color': '#dddddd',
            'panels_weights': [2,5],
            'action_class':'.item',
            'align': 'horizontal',
            'win_width':window.document.body.offsetWidth,//窗口高度
            'panel_css':{
            	'display':'block',
            	'position':'absolute',
            	'top':'0',
            	'height':'100%',
            	'left':'0',
            	'background':'#ffffff',
            	'border-left':'1px solid #dddddd',
				'overflow':'hidden',
				'overflow-y':'auto'
            },
            'root_css':{
            	'position':'relative',
            	'min-height':(window.document.body.clientHeight -69 - 45),
            	'height':(window.document.body.clientHeight -69 - 45),
            	'background':'#efefef',
            	'overflow':'hidden'
            }
        },
        this.options = $.extend({}, this.defaults, opt);
    }
    //定义layout的方法
    layout.prototype = {
    	constructor: function(){
    		if(this.options.align == 'horizontal'){
    			this.hz_init();
    		}else if(this.options.align == 'vertical'){
    			this.vt_init();
    		}else{
    			alert('options error ! unknown align method:'+this.options.align);
    		}
    	},
        hz_init: function() {
            if(this.options.render_id == null){
            	return;
            }
            var c = $(this.options.render_id);
            if(c == undefined || c == null || c.length <= 0){
            	return;
            }
            var childs = c.children();
            var ci = 0;
            var w_count = this.getWeightCount(this.options.panels_weights);
            //设置容器样式
            $(this.options.render_id).css(this.options.root_css);
			//初始化子区域大小
            while(ci  < childs.length){
            	$(childs[ci]).css(this.options.panel_css);
            	$(childs[ci]).css('width',(this.options.panels_weights[ci] / w_count) * this.options.win_width);
            	if(ci > 0)
            		$(childs[ci]).css({
            			'left':this.getAutoLeft(childs,ci)+80,
            			'opacity':'0'
            			// 'display':'none'
            		});
            	// $(child).text(ci);
            	ci++;
            }
            //添加响应监听
            this.hzActionListener();

        },
        vl_init:function(){
        	//console.debug('vl_init() loaded!');
        },
        getAutoLeft:function(elements,index){
        	var countWidth = 0 ;
        	var count = 0;
        	while(count <  index){
        		countWidth += $(elements[count]).width();
        		count++;
        		if(count == index){
        			break;
        		}
        	}
        	return countWidth;
        },
        getWeightCount:function(p_weights){
        	var count = 0;
        	var site = 0;
        	while(site < p_weights.length){
        		count += p_weights[site];
        		site ++;
        	}
        	return count;
        },
        hzActionListener:function(){
			$(this.options.action_class).off("click");
			$(this.options.action_class).on("click",function(){
				var url = $(this).parent().attr("data-url");//数据请求地址
				var ref=$(this).parent().attr('data-ref');//目标ID
				var selfRoot =ly.getLayoutPanel($(this));//当前元素所在容器
				var next = $('[data-id ='+ref+']').parent();//下一个容器
				//隐藏子页面
				next.children().hide();
				//显示对应的页面
				if(next.css('display') != 'none'){
					var left = selfRoot.position().left + selfRoot.width() + 80;
					next.animate({
						'left':left,
						'opacity':'0'
					},300);
					selfRoot.nextAll().fadeOut(300);
				};
				var left = selfRoot.position().left + selfRoot.width();
				next.animate({
					'left':left,
					'opacity':'100'
				},500);
				var data = requestData(url);
				serializationJsonAndAutowireData(data,ref);
				selfRoot.next().fadeIn('normal',function(){
					$('[data-id = '+ref+']').show();
				});
			});
		},
		getLayoutPanel:function(element){
			var parent = element.parent();
			while(typeof(parent.attr("layout-id")) == "undefined" ){
				parent = parent.parent();
				if(parent.localName == "body"){
					parent = undefined;
					break;
				}
			}
			return parent;
		}
    };

	/**
	 * ajax请求数据，post，非异步
	 * @param url 请求地址
	 * @returns {*}
	 */
	function requestData(url){
		//console.info("request data from %o ... ",url);
		var d = null;
		$.ajax({
			url:url,
			async:false,
			dataType:'json',
			type:'POST',
			data:{},
			success:function(data){
				if(data.result == true){
					d = data.data;
				}else{
					alert('请求数据失败:'+data.errorMsg);
				}
			},
			error:function(request,status ,e){
				alert('请求发生错误：'+e);
			}
		});
		return d;
	};
	/**
	 * 序列化数据，并注入ui中
	 * @param dataArray 数据列表
	 * @param tpl 数据ui模版
	 * @param panelId 容器ID
	 */
	function serializationJsonAndAutowireData(dataArray,panelId){
		//console.info("param:%o,%o",dataArray,panelId);
		$('*[data-id = '+panelId+'] *[data-tpl="false"]').remove();
		var tpl = $("[data-id="+panelId+"]").find('[data-tpl="true"]').parent().html();
		var root  = $("[data-id="+panelId+"]").find('[data-tpl="true"]').parent();
		//console.info("tpl:%o,%o",tpl,root);

		var count = 0;
		if(Object.prototype.toString.call(dataArray) ==='[object Array]'){
			while(count < dataArray.length){
				var e = $(tpl);
				var tag = e.attr("data-url");
				if(dataArray[count][tag]){
					e.attr("data-url",dataArray[count][tag]);
				}
				for(var p in dataArray[count]){

					var element = e.find('[name='+p+']');
					if(element[0] != undefined){
						//根据标签类型注入内容，a、img标签 注入属性，p，h等注入文本，特殊字段
						if(element[0].localName == "a"){
							element.attr("href",p);
						}else if(element[0].localName == "img"){
							element.attr("src",p);
						}else{
							element.text( dataArray[count][p]);
						}
					}
					e.attr("data-tpl",'false');
				}
				root.append(e);
				count++;
			}
		}else if(Object.prototype.toString.call(dataArray) ==='[object Object]'){
			var e = $(tpl);
			var tag = e.attr("data-url");
			for(var p in dataArray){
				if(dataArray[tag]){
					//console.info("set tpl attr %o ,value %o",e.attr("data-url") , dataArray[tag]);
					e.attr("data-url",dataArray[tag]);
				}
				var element = e.find('[name='+p+']');
				if(element != undefined){
					//根据标签类型注入内容，a标签 注入属性，p，h等注入文本，特殊字段
					if( element.is('a')){
						element.attr("href",dataArray[p]);
					}else if(element.is('img')){
						element.attr("src",dataArray[p]);
					}else{
						element.text( dataArray[p]);
					}
				}
				e.attr("data-tpl",'false');
			}
			root.append(e);
		}
		ly.hzActionListener();
	};
	var ly = null;
    //在插件中使用layout对象
    $.fn.buildPlugin = function(options) {
        //创建layout的实体
		ly = new layout(options);
        //调用其方法
        return ly.constructor();
    }
})(jQuery, window, document);

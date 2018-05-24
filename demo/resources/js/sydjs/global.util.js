//重写toFixed方法，解决不同浏览器解析差异的BUG
Number.prototype.toFixed = function(exponent) {
    return (parseInt(this * Math.pow(10, exponent) + 0.5) / Math.pow(10, exponent)).toString();
};
// 为js的Date对象加入了格式化方法
Date.prototype.format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)){
         fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o){
         if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
    return fmt;
};

// input[type=file]类型对象,不能设置value		--lvq
var not_include_ids = ["CLOSE_PLAN", "CLOSE_DIAGRAM", "CONSTRUCTION_PLAN", "PROTECTION_PLAN", "ROUTE_PIC_URL", "RECEIPT_URL", "COMPLETE_REPORT_URL", "ATTACH_DISK_URL", "APPLY_MATERIAL_URL", "APPLY_RECEIPT_URL", "LOCAL_PATH"];

function isSetVal(r_id) {
	var ret = true;
	$(not_include_ids).each(function (idx, t_id) {
		if (r_id == t_id) {
			ret = false;
			return false;
		}
	});
	
	return ret;
}

var globalUtil = {
    //全局颜色变量
    RED: '#FF6666',
    ORANGE: '#FF9933',
    BLUE: '#5C7DFE',
    GREEN: '#00FF66',
    
    //calendar event color
    CAL_CREATE: '#3A87AD', // new create
    CAL_SUBMIT: '#6FB7B7', // submit
    CAL_UNEXECUTE: '#EA0000', // unexecute
    CAL_COMPLETE: '#02C874', // complete
    CAL_EXTRA: '#0080FF', // extra event
    CAL_DELAY: '#FF8000', // delay
    CAL_ING: '#FF8000', // executing

    _intervalIdArray: [],
    registInterval: function(intervalId) {
        this._intervalIdArray.push(intervalId);
    },
    closeAllInterval: function() {
        for (var i = 0; i < this._intervalIdArray.length; i++) {
            window.clearInterval(this._intervalIdArray[i]);
        }
        this._intervalIdArray = [];
    },
    
	_functionWrapperObj: {},
    /**
     * 方法计数包装器
     * @param varStr 计数包装器引用对象字符串
     * @param _fn 方法
     */
	setFunctionWrapper: function(varStr, _fn) {
		if (!$.trim(varStr) || typeof _fn != 'function') return;
		if (this._functionWrapperObj[varStr] == undefined) {
			this._functionWrapperObj[varStr] = 0;
		}
		this._functionWrapperObj[varStr]++;
		_fn();
		this._functionWrapperObj[varStr]--;
	},
    /**
     * 在varStr对应的方法计数包装器函数执行完成后执行_fn
     * @param varStr 计数包装器引用对象timer字符串
     * @param _fn 方法
     */
	functionRunOut: function(varStr, _fn) {
		if (!$.trim(varStr) || typeof _fn != 'function') return;
		if (this._functionWrapperObj[varStr] == undefined 
				|| this._functionWrapperObj[varStr] == 0) {
			if (this._functionWrapperObj[varStr+'timer']) {
				window.clearInterval(this._functionWrapperObj[varStr+'timer']);
				this._functionWrapperObj[varStr+'timer'] = null;
			}
			_fn();
		} else if (!this._functionWrapperObj[varStr+'timer']) {
			this._functionWrapperObj[varStr+'timer'] = window.setInterval(function() {
				this.functionRunOut(varStr, _fn);
			}, 200);
		}
	},
    
    getUrlParameter: function(strParame) {
        var args = new Object();
        var query = location.search.substring(1);
        var pairs = query.split("&"); // Break at ampersand 
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1) continue;
            var argname = pairs[i].substring(0, pos);
            var value = pairs[i].substring(pos + 1);
            value = decodeURIComponent(value);
            args[argname] = value;
        }
        return args[strParame];
    },
    
    //获取父节点
    getTreeFatherNode: function(tree, node) {
        if (!node || !tree) {
            return null;
        }
        var treenode = tree.getNodeDom(node);
        var treeitem = $(treenode.target);
        var pnode = {};
        if (treeitem.parent().hasClass("l-tree")) {
            return null;
        }
        if (treeitem.parent().parent("li").length === 0) {
            return null;
        }
        pnode.target = treeitem.parent().parent("li")[0];
        pnode.data = tree.getDataByID($(pnode.target).attr("id"));
        return pnode;
    },
    
    // 得到树的最大层级
    getTreeMaxLevel: function(tree) {
        var maxLevel = 0;
        var setMaxLevel = function(node, level) {
            if (maxLevel < (level + 1)) {
                maxLevel = level + 1;
            }
            var childNodes = node && (node.data || node.children);
            if (childNodes) {
                for (var i = 0; i < childNodes.length; i++) {
                    setMaxLevel(childNodes[i], level + 1);
                }
            }
        };
        if (tree.length && tree.length > 0) {
            $.each(tree,
            function(index, node) {
                setMaxLevel(node, -1);
            });
        } else {
            setMaxLevel(tree, -1);
        }
        return maxLevel;
    },

    //树控件文字默认宽度
    defaultWidth: 50,
    //每个汉字的宽度
    singleWorldWidth: 12,

    // 得到树控件文字宽度
    getTreeMaxWidth: function(tree) {
        var sObj = this;
        var maxLevel = sObj.getTreeMaxLevel(tree);
        var maxWidth = sObj.defaultWidth;

        var setMaxWidth = function(node, level) {
            var nodeWidth = 0;
            if (node.text) {
                nodeWidth = node.text.length * sObj.singleWorldWidth - (maxLevel - level) * 22;
                if (maxWidth < nodeWidth) {
                    maxWidth = nodeWidth;
                }
            }

            var childNodes = node && (node.data || node.children);
            if (childNodes) {
                for (var i = 0; i < childNodes.length; i++) {
                    setMaxWidth(childNodes[i], level + 1);
                }
            }
        };

        setMaxWidth(tree, 0);
        return maxWidth + 22;
    },

    // 得到树控件整体宽度
    getTreeWholeWidth: function(tree, step) {
        var sObj = this;
        step = step || 22;
        var maxLevel = sObj.getTreeMaxLevel(tree);
        var maxWidth = sObj.getTreeMaxWidth(tree);

        return maxWidth + maxLevel * step;
    },
    //使用get方式，同步获取后台数据，返回数据结构为json
    syncGetJson: function(url, params) {
        var data = {};
        $.ajax({
            type: "GET",
            url: globalUtil.getTimestampUrl(url),
            data: params,
            async: false,
            dataType: 'json',
            success: function(json) {
                data = json;
            }
        });
        return data;
    },
    
    _getMainpageObj: function(obj){
    	var tmp = obj.parent;
    	while(tmp!=obj){
    		obj = tmp;
    		tmp = obj.parent;
    	}
    	return tmp;
    },
    
    // 清除页面上的所有弹出框
    clearAllDialog: function() {
        $('.l-window-mask').remove();
        $('.l-dialog').remove();
    },
    
    // loading control with it's url, when some error happens, u need call this function
    errorHandle: function(data) {
        var msg = data && data.error;
        if (msg === '100091') {
			globalUtil.errorMsg(globalErrorMsg['100091']);
			return false;
        }
        if (msg === '100092') {
        	globalUtil.confirmMsg(globalErrorMsg['100092'], '提示',
                function(yes) {
                    if (yes) {
                    	var obj = globalUtil._getMainpageObj(parent);
                    	obj.location.href = global_param.context_name + "/login.html";
                    } else {
                        // 此处删除掉弹出的多余确认框（当将地址粘贴到新的浏览器中时候会出现弹出2个确认框的问题，模态控制会混乱，此处避免此问题）
                    	globalUtil.clearAllDialog();
                    }
                },true);
        	return false;
        }
        return true;
    },

    

    //清除下拉框控件失去焦点后，下拉框不收缩的问题
    clearAllCombo: function() {
        $('.l-box-select').hide();
    },

    _mask: {},
    addMask: function(id) {
        this._mask[id] = $("<div class='l-window-mask' style='display: block;z-index: 9100' id=" + id + "></div>").appendTo('body');

    },
    delMask: function(id) {
        this._mask[id].remove();
    },

    showLoading: function() {
        $("<div class='l-loading-img' style='display:block; z-index: 11000;'></div>").appendTo('body');
        $("<div class='l-loading-text' style='display:block; z-index: 11000;'></div>").appendTo('body');
    },
    closeLoading: function() {
        $(".l-loading-img").remove();
        $(".l-loading-text").remove();
    },
    setLoadingText: function(str) {
        $(".l-loading-text").html(str);
    },

    //focusObj:dialog关闭后需要获取的焦点对象
    _alert: function(msg, type, callBackFunc) {
        var that = this;
        var obj;
        var timestamp = new Date().getTime();
        that.addMask(timestamp);

        if (type === 'success') {
            obj = $.ligerDialog.success(msg, null, null);
            var autoClose = setTimeout(function() { //2秒后自动关闭
                obj.close();
            },
            1000);
            obj.options.onHiddenOrClose = function() {
                that.delMask(timestamp);
                clearTimeout(autoClose); //取消自动关闭时间，避免手动关闭时重复调用onHiddenOrClose方法
                if (callBackFunc && typeof(callBackFunc) == 'function') {
                    callBackFunc();
                }
            };
            return;
        } else if (type === 'error') {
            obj = $.ligerDialog.error(msg, null, callBackFunc);
        } else if (type === 'warn') {
            obj = $.ligerDialog.warn(msg, null, callBackFunc);
        } else if (type === 'alert') {
            obj = $.ligerDialog.alert(msg, null, 'alert', callBackFunc);
        }
        obj.options.onHiddenOrClose = function() {
            that.delMask(timestamp);
        };
    },
    successMsg: function(msg, callBackFunc) {
        this._alert(msg, 'success', callBackFunc);
    },
    warnMsg: function(msg, callBackFunc) {
        this._alert(msg, 'warn', callBackFunc);
    },
    errorMsg: function(msg, callBackFunc) {
        this._alert(msg, 'error', callBackFunc);
    },
    alertMsg: function(msg, callBackFunc) {
        this._alert(msg, 'alert', callBackFunc);
    },
    confirmMsg: function(content, title, callback, isClearAll, resetHeight, resetWidth) {
        var that = this;
        var obj = $.ligerDialog.confirm(content, title, callback);
        that.addMask('confirm');
        if (resetHeight) obj._setHeight(resetHeight);
        if (resetWidth) obj._setWidth(resetWidth);
        obj.options.onHiddenOrClose = function() {
            that.delMask('confirm');
            if (isClearAll) {
                that.clearAllDialog();
            }
        };
    },
    setDialogHeight: function(height) {
        return document.body.offsetHeight < height ? document.body.offsetHeight: height;
    },

    checkSession: function(flag) {
    	var cookieval = globalUtil.getCookieValue('cookie_username') || '';
    	var cookieval2 = globalUtil.getCookieValue('cookie_passwd') || '';
    	flag = flag || '1';
    	
        $.getJSON(this.getTimestampUrl("/sysmanage/authorizedStub.do"),{'cookieval': cookieval, 'cookieval2': cookieval2},
	        function(data) {
        		if(flag==='1'){
        			globalUtil.errorHandle(data);
        		}else{
        			var msg = data && data.error;
        			
        			if (globalUtil.isMobile.any()) {
        				if (msg && msg === '100092') {
        					window.location.href = global_param.context_name + "/m.login.html";
        				} else {
        					window.location.href = global_param.context_name + "/resources/htmlmobile/main_page.html";
        				}
        			} else {
        				var obj = globalUtil._getMainpageObj(parent);
        				if (msg && msg === '100092') {
        					obj.location.href = global_param.context_name + "/login.html";
        				} else {
        					obj.location.href = global_param.context_name + "/resources/html/main_page.html";
        				}
        			}
        		}
	        	
	        });
    },
    
    getCookieValue: function(cookieName){
    	var name = cookieName || 'cookie_username';
    	return $.cookie(name) || '';
    },
    setCookieValue: function(cookieName, cookieVal, expireDay){
    	if(global_param.use_cookie){
    		cookieName = cookieName || 'empty';
        	cookieVal = cookieVal || '';
        	expireDay = expireDay || 7;
        	$.cookie(cookieName, cookieVal, {expires: expireDay});
    	}
    },
    resetCookieValue: function(cookieName,cookieVal, expireDay){
    	$.removeCookie(cookieName);
    	globalUtil.setCookieValue(cookieName, cookieVal, expireDay);
    },
    
    isMobile: {
			Android: function() {
				return navigator.userAgent.match(/Android/i) ? true: false;
			},
			BlackBerry: function() {
				return navigator.userAgent.match(/BlackBerry/i) ? true: false;
			},
			iOS: function() {
				return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true: false;
			},
			Windows: function() {
				return navigator.userAgent.match(/IEMobile/i) ? true: false;
			},
			any: function() {
				return (this.Android() || this.BlackBerry() || this.iOS() || this.Windows());
			}
	},
    
    openTab: function(tabId, tabName, url, removeAll) {
        var $query = window.parent.window.$;
        var tab = $query("#framecenter").ligerGetTabManager();
        if (tab.isTabItemExist(tabId)) {
            tab.removeTabItem(tabId);
        }
        if (removeAll) {
            tab.removeAll();
        }

        if (url.indexOf('?') > -1) {
        	setTimeout(function(){url = url + '&timestamp=' + new Date().getTime();},0);
        } else {
        	setTimeout(function(){url = url + '?timestamp=' + new Date().getTime();},0);
        }

        tab.addTabItem({
            tabid: tabId,
            text: tabName,
            url: url
        });
    },
    selectTabItem: function(tabId) {
    	var $query = window.parent.window.$;
    	$query("#framecenter").ligerGetTabManager().selectTabItem(tabId);
    },
    removeTabItem: function(tabId) {
    	var $query = window.parent.window.$;
    	$query("#framecenter").ligerGetTabManager().removeTabItem(tabId);
    },
    removeOtherTab: function(tabId) {
        var $query = window.parent.window.$;
        tab = $query("#framecenter").ligerGetTabManager();
        tab.removeOther(tabId);
    },
    closeCurrentTab: function() {
        var $query = window.parent.window.$;
        tab = $query("#framecenter").ligerGetTabManager();
        tab.removeSelectedTabItem();
    },
    isEmail: function(value) {
        value = $.trim(value);
        return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(value);
    },
    /**
     * 验证是否为手机号码，验证为11位数字
     * @param value
     * @returns
     */
    isPhone: function(value) {
        value = $.trim(value);
        return /^[1][345789]\d{9}$/.test(value);
    },
    /**
     * 验证输入的内容是否中文
     * @param value
     * @returns
     */
    isChinese: function(value) {
        value = $.trim(value);
        return /^[\u0391-\uFFE5]+$/.test(value);
    },
    /**
     * 验证是否为身份证号，只验证了位数和出生日期
     * @param value
     * @returns {Boolean}
     */
    isIdCard: function(value) {
        value = $.trim(value);
        value = value.toUpperCase(); //转为大写
        if (value.length == 15) {
            if (!/^[0-9]{15}$/.test(value)) {
                return false;
            }
            var year = value.substring(6, 8);
            var month = value.substring(8, 10);
            var day = value.substring(10, 12);
            var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
            // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
            if (temp_date.getYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
                return false;
            } else {
                return true;
            }
        } else if (value.length == 18) {
            if (!/^[0-9]{17}([0-9]|X)$/.test(value)) {
                return false;
            }
            var year = value.substring(6, 10);
            var month = value.substring(10, 12);
            var day = value.substring(12, 14);
            var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
            // 这里用getFullYear()获取年份，避免千年虫问题   
            if (temp_date.getFullYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    },
    /**
     * 根据身份证号，获取周岁年龄
     * @param value
     * @returns {String}
     */
    getAgeByIdCard: function(value,timeStr) {
    	var age="";
    	var birthday = "";  
        if(value != null && value != ""){  
            if(value.length == 15){  
                birthday = "19"+value.substr(6,6);  
            } else if(value.length == 18){  
                birthday = value.substr(6,8);  
            }  
            birthday = birthday.replace(/(.{4})(.{2})/,"$1-$2-");  
          	//获取年龄
            var myDate = new Date(Date.parse(timeStr.replace(/-/g,   "/")));
            var month = myDate.getMonth() + 1;
            var day = myDate.getDate();
            age = myDate.getFullYear() - birthday.substring(0, 4) - 1;
            if (birthday.substring(5, 7) < month || (birthday.substring(5, 7) == month && birthday.substring(8, 10) <= day)) {
            	age++;
            }  
        }
        return age;
    },
  //人民币金额转大写程序     
  getnumToCny:function(num){     
	  var strOutput = "";  
	  var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';  
	  num += "00";  
	  var intPos = num.indexOf('.');  
	  if (intPos >= 0)  
	    num = num.substring(0, intPos) + num.substr(intPos + 1, 2);  
	  strUnit = strUnit.substr(strUnit.length - num.length);  
	  for (var i=0; i < num.length; i++)  
	    strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i,1),1) + strUnit.substr(i,1);  
	    return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");   
  }
  , getTimestampUrl: function(url) {
        url = $.trim(url);
        if (url.indexOf('?') > -1) {
            return global_param.context_name + url + '&timestamp=' + new Date().getTime();
        } else {
            return global_param.context_name + url + '?timestamp=' + new Date().getTime();
        }
    },

    /**
     * 验证是否为正整数
     * @param value
     * @returns
     */
    isNum: function(value) {
        value = $.trim(value);
        return /^[1-9]+[0-9]*$/.test(value);
    },
    //验证是否为实数，如果录入012，返回false
    isFloat: function(str) {
        var myreg = /^(-?((0|[1-9]+)|([1-9]\d*)))(\.\d+)?$/;
        return myreg.test(str);
    },
    /**
     * 验证是否为指定格式的实数(包括负数和整数和小数点)
     * @param value， intLen整数部分最长长度，floatLen小数部分最长长度
     * @returns
     */
    isReal: function(value, intLen, floatLen) {
        value = $.trim(value);
        if (!this.isFloat(value)) { //首先验证是否为实数
            return false;
        }
        var pattern;
        if (!intLen) {
            return /^-?\d+(\.\d+)?$/.test(value);
        } else if (!floatLen) {
            pattern = new RegExp('^-?\\d{1,' + intLen + '}$', '');
        } else {
            pattern = new RegExp('^-?\\d{1,' + intLen + '}(\\.\\d{1,' + floatLen + '})?$', '');
        }

        return pattern.test(value);
    },
    //不进行四舍五入保留n位小数
    changeDecimal: function(x, n) {
        if (!this.isFloat(x)) {
            return x;
        }
        if ($.trim(x).length == 0) {
            return '';
        }
        var str = $.trim(x) + '';
        var pattern = new RegExp('\-{0,1}[0-9]+(\.[0-9]{1,' + n + '}){0,1}', '');
        str = str.match(pattern)[0];
        return parseFloat(str);
    },

    //四舍五入保留n位小数
    changeDecimalRound: function(x, n) {
        if (!this.isFloat(x)) {
            return x;
        }
        return parseFloat(x).toFixed(n);
    },

    isEmpty: function(val) {
        if (typeof(val) == 'number') {
            val += '';
        }
        var str = val || '';
        return $.trim(str).length == 0;
    },

    //验证：大小写英文字符、数字、连字符、下划线四类字符
    checkSectCode: function(sectCode) {
        var sectCode = $.trim(sectCode);
        var a = /^([a-z]|[A-Z]|\-|_|\d){1,}$/;
        if (!a.test(sectCode)) {
            return false;
        } else return true;
    },
    
    /**
	   * 过滤对象或数组的属性
	   * @param obj 对象或数组
	   * @param value 过滤值
	   * @param deep 是否深层过滤
	   */
    deleteAttributes: function(obj, value, deep) {
    	if (jQuery.isPlainObject(obj)) {
        	for (var A in obj) {
        		if ((jQuery.isPlainObject(obj[A]) || jQuery.isArray(obj[A])) && deep) {
        			globalUtil.deleteAttributes(obj[A], value, deep);
        		} else if (obj[A] === value) {
    				delete obj[A];
        		}
        	}
    	} else if (jQuery.isArray(obj)) {
    		var temp = [];
    		jQuery.each(obj, function(i, n) {
        		if ((jQuery.isPlainObject(n) || jQuery.isArray(n)) && deep) {
        			globalUtil.deleteAttributes(n, value, deep);
        			if (n.length != 0) {
        				temp.push(n);
        			}
        		} else if (n !== value) {
        			temp.push(n);
        		}
    		});
    		obj.splice(0, obj.length);
    		if (temp.length != 0) {
    			for (var i = 0, l = temp.length; i < l; i++) {
    				obj.push(temp[i]);
    			}
    		}
    	}
    },
    //返回当前日期的字符串
    getCurrentDateStr: function() {
        var now = new Date();
        var year = now.getFullYear(); //年
        var month = now.getMonth() + 1; //月
        var day = now.getDate(); //日
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        if (day < 10) clock += "0";
        clock += day;
        return (clock);
    },
    //返回当前时间的字符串，yyyy-MM-dd HH:mm:ss
    getCurrentDatetimeStr: function() {
        var now = new Date();
        return this._transToDateStr(now);
    },
    
    getBeforeDayOfCurrentDateStr:function(){
    	var today=new Date();
        var yesterday_milliseconds=today.getTime()-1000*60*60*24;
         
        var yesterday=new Date();      
        yesterday.setTime(yesterday_milliseconds);      
            
        var year=yesterday.getFullYear();   
        var day=yesterday.getDate();   
        var month=yesterday.getMonth()+1;
        
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        if (day < 10) clock += "0";
        clock += day;
        return (clock);
    },
    
    getBeforeDaysOfCurrentDateStr:function(){
    	var today=new Date();
        var yesterday_milliseconds=today.getTime()-7000*60*60*24;
         
        var yesterday=new Date();      
        yesterday.setTime(yesterday_milliseconds);      
            
        var year=yesterday.getFullYear();   
        var day=yesterday.getDate();   
        var month=yesterday.getMonth()+1;
        
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        if (day < 10) clock += "0";
        clock += day;
        return (clock);
    },

    /**
	   * 根据format的格式对dateStr进行格式化，内部通过字符串的截取实现
	   * @param datestr
	   * @param format yyyy-MM-dd HH:mm:ss
	   */
    formatDateStr: function(datestr, format) {
        format = $.trim(format);
        var len = format.length;
        if (datestr.length < len) {
            return '';
        }
        return datestr.substring(0, len);
    },
    //获取和指定日期字符串差值diff日的日期
    getDiffDateStr: function(datestr, diff) {
        if (!(datestr instanceof  Date)) {
            datestr = datestr.replace(/-/g, "/");
            datestr = new Date(datestr);
        }
        datestr.setDate(datestr.getDate() + (diff));
        var newDate = this.formatDateStr(this._transToDateStr(datestr), 'yyyy-MM-dd');
        return newDate;
    },
    //获取和指定日期字符串差值diff月的日期
    getDiffMonthStr: function(datestr, diff) {
    	if (!(datestr instanceof  Date)) {
            datestr = datestr.replace(/-/g, "/");
            datestr = new Date(datestr);
        }
        datestr.setMonth(datestr.getMonth() + (diff));
        var newDate = this.formatDateStr(this._transToDateStr(datestr), 'yyyy-MM-dd');
        return newDate;
    },
    /*
	 * 返回指定日期字符串增加指定时间，返回增加后的日期字符串
	 * param中对应的value应该为数值，不能写成字符串
	 * */

    getDiffDateFormatStr: function(datestr, param, format) {
    	if (!(datestr instanceof  Date)) {
            datestr = datestr.replace(/-/g, "/");
            datestr = new Date(datestr);
        }
        if (param.year) {
            datestr.setFullYear(datestr.getFullYear() + (param.year));
        }
        if (param.month) {
            datestr.setMonth(datestr.getMonth() + (param.month));
        }
        if (param.date) {
            datestr.setDate(datestr.getDate() + (param.date));
        }
        if (param.hour) {
            datestr.setHours(datestr.getHours() + (param.hour));
        }
        if (param.min) {
            datestr.setMinutes(datestr.getMinutes() + (param.min));
        }
        if (param.sec) {
            datestr.setSeconds(datestr.getSeconds() + (param.sec));
        }
        var newDate = this.formatDateStr(this._transToDateStr(datestr), format);
        return newDate;
    },
    //返回指定年月份的总天数
    searchDays: function(varYear, varMonth) {
        var lngDay;
        switch (varMonth) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            lngDay = 31;
            break;
        case 4:
        case 6:
        case 9:
        case 11:
            lngDay = 30;
            break;
        case 2:
            if ((varYear % 4 == 0 && varYear % 100 != 0 || (varYear % 400 == 0))) {
                lngDay = 29;
            } else {
                lngDay = 28;
            }
            break;
        }
        return lngDay;
    },
    getFirstDayOfCurrentMonth: function() {
        var now = new Date();
        var year = now.getFullYear(); //年
        var month = now.getMonth() + 1; //月
        var day = "01"; //日
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        clock += day;
        return (clock);
    },
    getLastDayOfCurrentMonth: function() {
        var now = new Date();
        var year = now.getFullYear(); //年
        var month = now.getMonth() + 1; //月			
        var day = this.searchDays(year, month); //日
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        clock += day;
        return (clock);
    },
    _transToDateStr: function(datetime) {
        var year = datetime.getFullYear(); //年
        var month = datetime.getMonth() + 1; //月
        var day = datetime.getDate(); //日
        var hour = datetime.getHours();
        var min = datetime.getMinutes();
        var sec = datetime.getSeconds();
        var clock = year + "-";
        if (month < 10) clock += "0";
        clock += month + "-";
        if (day < 10) clock += "0";
        clock += day;
        clock += " ";
        if (hour < 10) {
            clock += "0";
        }
        clock += hour;
        clock += ":";
        if (min < 10) {
            clock += "0";
        }
        clock += min;
        clock += ":";
        if (sec < 10) {
            clock += "0";
        }
        clock += sec;
        return clock;
    },
    getHtml:function(url){
    	url = $.trim(url);
        if (url.indexOf('?') > -1) {
            return url + '&timestamp=' + new Date().getTime();
        } else {
            return url + '?timestamp=' + new Date().getTime();
        }
    	return url;
    },
    // return Date object from date string, format like 'yyyy-MM-dd' OR 'yyyy-MM-dd HH:mm:ss' OR  'yyyy/MM/dd'
    // Month can be MM or M, means '09' or '9' are all OK, same as Date field
    parseDate: function(dateStr){
	    dateStr = dateStr.replace(/-/g,'/');
	    return new Date(dateStr);
    },
    // compare two date
    // RETURN -2 means some parameter format is WRONG, 
    // RETURN -1 means firstDate is SMALLER then secondDate, 
    // RETURN 0   means equal, 
    // RETURN 1   means firstDate is BIGGER then secondDate, 
    compareDate: function(firstDate, secondDate){
    	if(!firstDate || !(firstDate instanceof Date)){
		    return -2;
		}
		if (!secondDate || !(secondDate instanceof Date)) {
		    return -2;
		}
		
		var firstMil = firstDate.getTime();
		var secondMil = secondDate.getTime();
		
		if (firstMil>secondMil) {
		    return 1;
		}else if (firstMil<secondMil) {
		    return -1;
		}else{
		    return 0;
		}
    },
    
    registerXlsFlashBtn : function (moviePath,fileName,titleJson,dataJson,obj,fnClick){
    	if(moviePath){
    		ZeroClipboard_TableTools.moviePath = moviePath;
    	}
    	var flash = new ZeroClipboard_TableTools.Client();
    	flash.setHandCursor( true );
    	flash.setAction( 'save' );
    	flash.setCharSet('UTF16LE' );
    	flash.setBomInc( true );
    	flash.setFileName(fileName);
    	flash.addEventListener('mouseDown', function(client) {
    		
    		if (typeof(fnClick) == 'function') {
    			// actual function reference
    			var ss = fnClick.call(this);
    			if(typeof ss == "string"){
    				flash.setText(ss);
    			}else{
    				var sNewline = navigator.userAgent.match(/Windows/) ? "\r\n" : "\n";
    				//ss.title={'name':'姓名','age':'年龄'};
    				//ss.data=[{'name':'张三','age':30},{'name':'张三','age':30}];
    				var title = ss.title;
    				var contents = ss.data;
    				var aData=[];
    				var titleKey=[];
    				var aRow=[];
    				for (var key in title) {            
    					aRow.push(title[key]);
    					titleKey.push(key);
    				}
    				aData.push( aRow.join("\t") );
    				for(var i=0;i<contents.length;i++){
    					aRow = [];
    					for (var j=0;j<titleKey.length;j++) {            
    						aRow.push(contents[i][titleKey[j]]);  
    					}
    					aData.push( aRow.join("\t") );
    				}
    				var _sLastData = aData.join(sNewline);
    				flash.setText(_sLastData);
    			}
    		}
    	} );
    	flash.glue(obj, "Save for Excel" );
    	//fnGlue( flash, null, objId, "Save for Excel" );
    },
    /*
     * 指定对象下的所有input、textarea值(除下拉框，下拉框需要单独赋值)
     */
    setFormVal: function(divid,param){
    	var inputlist = $("#"+divid+" input");
    	var textarealist = $("#"+divid+" textarea");
    	$.each(inputlist,function(i,obj){
    		var id = obj.id ? obj.id.toUpperCase() : obj.id;
    		
    		if (isSetVal(id)) {
    			var name = obj.name ? obj.name.toUpperCase() : obj.name;
        		var ligeruiid = obj.attributes.ligeruiid;
        		var val = name ? param[name] : param[id];
        		if(!ligeruiid){
        			$(obj).val(val);
        		}
    		}
    	});
    	$.each(textarealist,function(i,obj){
    		var id = obj.id ? obj.id.toUpperCase() : obj.id;
			var name = obj.name ? obj.name.toUpperCase() : obj.name;
    		if(name){
    			$(obj).val(param[name]);
	  		}else{
	  			$(obj).val(param[id]);
	  		}
    	});
    },
    /*
     * 获取指定DIV下的DOM组件的值
     */
    getDivParam: function(divid, type, prefix) {
    	if (globalUtil.isEmpty(divid)) return;
    	var param = {};
    	var typeList = type.split('#');
    	for (var i in typeList) {
    		var typeString = typeList[i];
    		var objList = $('#' + divid + ' ' + typeString);
        	$.each(objList, function(i, obj){
        		var paramAttibute = prefix ? obj.id.slice(prefix.length) : obj.id;
        		var val = obj.attributes.ligeruiid ? $.trim($('#'+obj.id+'_hidden').val()) : $.trim($(obj).val());
        		if (typeString === 'label') {
        			val = $.trim($(obj).html());
        		}
        		if (1) {//!globalUtil.isEmpty(val)
        			if (obj.attributes.ligeruiid && val < 0) return;
        			param[paramAttibute] = val;
        		}
        	});
    	}
    	return param;
    },
    /**
     * Description :打开当前用户分配的三级菜单,即tab页. 同时,将第一个tab页删除.
     * @param tabId 二级菜单所打开的tab页的id,与对应的sys_menu中的主键(id)相同.
     * @author lvq
     */
    openAuthorizedTabs: function(tabId) {
    	//是否打开新tab页的标志位
    	var flag = false;
    	var firstTabid = "";
    	if (window.parent.currentUserRole) {	//currentUserRole位于main_page中,在生成左侧菜单的同时将用户权限信息放入其中
    		$.each(window.parent.currentUserRole, function(index1, obj1) {		//obj1为一级菜单对象
    			if (obj1.childrenTree) {	//如果一级菜单下有二级菜单
    				$.each(obj1.childrenTree, function(index2, obj2) {		//obj2为二级菜单对象
    					if (obj2.id == tabId) {		//如果是当前点击的二级菜单
    						if (obj2.children) {	//如果有三级菜单
    							$.each(obj2.children, function(index3, obj3) {		//obj3为三级菜单
    								if (obj3) {
    									//记住第一个三级菜单
    									if (!flag) {
    										firstTabid = obj3.id;
    									}
    									//打开新tab页
    									/*if (flag) {
    										setTimeout(globalUtil.openTab(obj3.id, obj3.text, obj3.url, false), 100);
    									} else {
    									}*/
    									globalUtil.openTab(obj3.id, obj3.text, obj3.url, false);
    									flag = true;
    								}
    							});
    						}
    					}
    				});
    			}
    		});
    		//如果打开了新tab页,则将第一个tab页删除
    		if (flag) {
    			globalUtil.selectTabItem(firstTabid);
    			//globalUtil.removeTabItem(tabId);
    			var $query = window.parent.window.$;
    	        //$query("#framecenter").ligerGetTabManager().moveToTabItem(firstTabid);
    		    //移动到第一个三级菜单
    	        $query("#framecenter").ligerGetTabManager().moveToPrevTabItem(firstTabid);
    		    
    			window.parent.window.$(".l-tab-links li:first").hide();
    		}
    	}
    },
    
    /**
     * 补足日期长度, 如 2014-08-11 补足为 2014-08-11 00:00:00
     * @param obj 源对象
     * @param fmt 补足日期格式
     * @returns
     * @author lvq
     */
    formatDate: function(obj, fmt) {
    	if (!fmt) {
    		return obj;
    	}
    	if (!obj) {
    		return obj;
    	}
    	
    	var obj_length = obj.length;
    	var fmt_length = fmt.length;
    	
    	if (fmt_length <= obj_length) {
    		return obj;
    	}
    	
    	var dif = fmt_length - obj_length;
    	var ret;
    	
    	switch (dif) {
    	case 12:
    		ret = obj + "-01 00:00:00";
    		break;
    	case 9:
    		ret = obj + " 00:00:00";
    		break;
    	case 6:
    		ret = obj + ":00:00";
    		break;
    	case 5:
    		ret = obj + "00:00";
    		break;
    	case 3:
    		ret = obj + ":00";
    		break;
    	case 2:
    		ret = obj + "00";
    		break;
    	default : 
    		ret = obj + " 00:00:00";
    	}
    	
    	return ret;
    },
    dealWithDate: function(parms, attr, fmt) {
    	if (globalUtil.isEmpty(parms[attr])) {
    		delete parms[attr];
    	} else {
    		parms[attr] = globalUtil.formatDate(parms[attr], fmt);
    	}
    },
    getShortInstance:function(lon1,lat1,lon2,lat2){
    	var DEF_PI = 3.14159265359; // PI
    	var DEF_2PI = 6.28318530712; // 2*PI
    	var DEF_PI180 = 0.01745329252; // PI/180.0
    	var DEF_R = 6370693.5; // radius of earth
    	var ew1, ns1, ew2, ns2;
		var dx, dy, dew;
		var distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	},
	isCarNumber: function(str){
		return /(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)/.test(str);
	},
	//phoneReg: new RegExp("^1[3|4|5|8][0-9]\d{8}$")
	phoneReg: /^1[3|4|5|8][0-9]\d{8}$/
};
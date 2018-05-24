/*
 * 每个页面加载默认执行的方法
 */
$(function() {
	// layout ����
	$("#left-center").ligerLayout(
			{
				leftWidth : ($("#left-center").attr("leftwidth") ? parseInt($("#left-center").attr("leftwidth")) : 200),
				allowLeftCollapse : false,
				allowRightCollapse : false,
				allowLeftResize : false,
				allowRightResize : false,
				rightWidth: ($("#left-center").attr("rightwidth") ? parseInt($("#left-center").attr("rightwidth")) : 250)
			});
	if (global_param.session_check) {
		var url = window.location.href;
		var chechFlag = true;
		var arr = url.split("/");

		if (arr[arr.length - 1] === 'login.html' || !arr[arr.length - 1]) {
			chechFlag = false;
		};

		if (chechFlag) {
			globalUtil.checkSession();
		}
	}
	$("div").ajaxError(function(e, xhr, opt) {
		if(xhr.readyState == 4){
			globalUtil.errorMsg(globalErrorMsg['100093']);
		}
		parent.window.globalUtil.closeAllInterval();
		return false;
	});

	$("input[class!='l-text-field'], textarea").bind("click", function() {
		globalUtil.clearAllCombo();
	});		
});
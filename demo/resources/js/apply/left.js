//用户登陆
function validateUser() {
	var logtype = document.getElementById("logtype").value;
	var userCode = document.getElementById("userCode").value;
	var user_id_num = document.getElementById("user_id_num").value;
	var userPasswd = document.getElementById("userPasswd").value;
	var url = "/lnram/occupyway/CommonApprovalApplyJttLogin.do";
	$.ajax({
		url:url,
		type:'post',
		data:{
			logtype:logtype,
			userCode:userCode,
			user_id_num:user_id_num,
			userPasswd:userPasswd
		},
		dataType:'json',
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",//jqueryAjax处理中文乱码问题
		success: function(data){
			if (typeof(data.success)!= "undefined" && data.success !=null && data.success !="") {
				alert("登录成功！");
				var apply_type=$("#apply_type").val();	
				window.location.href="/lnram/occupyway/CommonApprovalApplyJttIndex.do?apply_type="+apply_type;
			}else{
				if ("100100" == data.error) {
					alert("用户名密码不匹配！");
					document.getElementById("userPasswd").focus();
				} else if ("100109" == data.error) {
					alert("密码错误！");
					document.getElementById("userPasswd").focus();
				}else{
					alert("输入信息有误，请您检查！");
				}
			}
		},
		error: function(){
			alert('出现错误!');
		}
	});
};
//用户注册页面初始化方法
function regClicked(){
	var apply_type=$("#apply_type").val();	
	document.getElementById("searchForm").action="/lnram/occupyway/CommonApprovalApplyJttReg.do?apply_type="+apply_type;
	$("#searchForm").submit();           
}
//用户登陆类别切换
function logtypecheck() {
	if (document.searchForm.logtype.value == '1') {
		yhm.style.display = "";
		sfz.style.display = "none";
	} else {
		yhm.style.display = "none";
		sfz.style.display = "";
	}
};
//用户登陆信息验证
function checkSubmit() {
	var uname, userPasswd, user_id_num;
	uname = document.searchForm.userCode.value;
	userPasswd = document.searchForm.userPasswd.value;
	user_id_num = document.searchForm.user_id_num.value;
	if (document.searchForm.logtype.value == '1') {
		if (uname == null || uname == '') {
			alert("请输入您的用户名！");
			yhm.style.display = "";
			sfz.style.display = "none";
			document.searchForm.userCode.focus();
			return;
		}
	} else {
		if (user_id_num.length < 15 || (user_id_num.length > 15 && user_id_num.length < 18)) {
			alert("请输入正确的身份证号码！");
			yhm.style.display = "none";
			sfz.style.display = "";
			document.searchForm.user_id_num.focus();
			return;
		}
	}

	if (userPasswd == null || userPasswd == '') {
		alert("请输入您的登录密码！");
		document.searchForm.userPasswd.focus();
		return;
	}

	validateUser();
};
//用户退出方法
function exitapp() {	
	document.searchForm.action = "/lnram/occupyway/CommonApprovalApplyJttLoginOut.do";
	document.searchForm.submit();
};
//左侧业务办理跳转方法，包括非公路业务办理、涉路业务办理、堵道业务办理、附件下载、法律法规、路线规划
function ywblSubmit(url){
	document.getElementById("searchForm").action=url;
	$("#searchForm").submit();           
}
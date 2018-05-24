// 初始化插件
var loginip="10.62.1.39";
var port="80";
var username="admin";
var password="admin123";
var streamtype="1";
// 全局保存当前选中窗口
var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
$(function () {
	// 检查插件是否已经安装过
	if (-1 == WebVideoCtrl.I_CheckPluginInstall()) {
		alert("您还未安装过插件，双击开发包目录里的WebComponents.exe安装！");
		return;
	}
	// 初始化插件参数及插入插件
	WebVideoCtrl.I_InitPlugin(832, 450, {
        iWndowType: 2,
		cbSelWnd: function (xmlDoc) {
			g_iWndIndex = $(xmlDoc).find("SelectWnd").eq(0).text();
		}
	});
	WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

	// 检查插件是否最新
	if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
		alert("检测到新的插件版本，双击开发包目录里的WebComponents.exe升级！");
		return;
	}
	
	// 窗口事件绑定
	$(window).bind({
		resize: function () {
			var $Restart = $("#restartDiv");
			if ($Restart.length > 0) {
				var oSize = getWindowSize();
				$Restart.css({
					width: oSize.width + "px",
					height: oSize.height + "px"
				});
			}
		}
	});
	clickLogin();
});

//服务器用户登录
function clickLogin() {
	var szIP = loginip;
		szPort = port;
		szUsername = username;
		szPassword = password;

	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {

		},
		error: function () {

		}
	});

	if (-1 == iRet) {

	}
}
	// 开始预览
	function clickStartRealPlay(szIP , iChannelID) {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
			iStreamType = parseInt(streamtype, 10);
			bZeroChannel=false;
			szInfo = "";

		if ("" == szIP) {
			return;
		}

		if (oWndInfo != null) {// 已经在播放了，先停止
			WebVideoCtrl.I_Stop();
		}

		var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
			iStreamType: iStreamType,
			iChannelID: iChannelID,
			bZeroChannel: bZeroChannel
		});

		if (0 == iRet) {
			szInfo = "开始预览成功！";
		} else {
			szInfo = "开始预览失败！";
		}
		alert(szInfo);
	}

	// 停止预览
	function clickStopRealPlay() {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		szInfo = "";
		if (oWndInfo != null) {
			var iRet = WebVideoCtrl.I_Stop();
			if (0 == iRet) {
				szInfo = "停止预览成功！";
			} else {
				szInfo = "停止预览失败！";
			}
		}else{
			szInfo = "停止预览失败！";
		}
		alert(szInfo);
	}
	
	// 窗口分割数
	function changeWndNum(iType) {
		iType = parseInt(iType, 10);
		WebVideoCtrl.I_ChangeWndNum(iType);
	}
	
	// 搜索录像
	var iSearchTimes = 0;
	function clickRecordSearch(szIP,iChannelID,szStartTime,szEndTime) {
		//bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
		bZeroChannel=false;
		if ("" == szIP) {
			return;
		}
		if (bZeroChannel) {// 零通道不支持录像搜索
			return;
		}

		WebVideoCtrl.I_RecordSearch(szIP, iChannelID, szStartTime, szEndTime, {
			iSearchPos: iSearchTimes * 40,
			success: function (xmlDoc) {
				if("MORE" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
					
					for(var i = 0, nLen = $(xmlDoc).find("searchMatchItem").length; i < nLen; i++) {
						var szPlaybackURI = $(xmlDoc).find("playbackURI").eq(i).text();
						if(szPlaybackURI.indexOf("name=") < 0) {
							break;
						}
						var szStartTime = $(xmlDoc).find("startTime").eq(i).text();
						var szEndTime = $(xmlDoc).find("endTime").eq(i).text();
						var szFileName = szPlaybackURI.substring(szPlaybackURI.indexOf("name=") + 5, szPlaybackURI.indexOf("&size="));

						var objTr = $("#searchlist").get(0).insertRow(-1);
						var objTd = objTr.insertCell(0);
						objTd.id = "downloadTd" + i;
						objTd.innerHTML = iSearchTimes * 40 + (i + 1);
						objTd = objTr.insertCell(1);
						objTd.width = "30%";
						objTd.innerHTML = szFileName;
						objTd = objTr.insertCell(2);
						objTd.width = "30%";
						objTd.innerHTML = (szStartTime.replace("T", " ")).replace("Z", "");
						objTd = objTr.insertCell(3);
						objTd.width = "30%";
						objTd.innerHTML = (szEndTime.replace("T", " ")).replace("Z", "");
						objTd = objTr.insertCell(4);
						objTd.width = "10%";
						objTd.innerHTML = "<a href='javascript:;' onclick='clickStartDownloadRecord(" + i + ");'>下载</a>";
						$("#downloadTd" + i).data("playbackURI", szPlaybackURI);
					}

					iSearchTimes++;
					clickRecordSearch(1);// 继续搜索
				} else if ("OK" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
					var iLength = $(xmlDoc).find("searchMatchItem").length;
					for(var i = 0; i < iLength; i++) {
						var szPlaybackURI = $(xmlDoc).find("playbackURI").eq(i).text();
						if(szPlaybackURI.indexOf("name=") < 0) {
							break;
						}
						var szStartTime = $(xmlDoc).find("startTime").eq(i).text();
						var szEndTime = $(xmlDoc).find("endTime").eq(i).text();
						var szFileName = szPlaybackURI.substring(szPlaybackURI.indexOf("name=") + 5, szPlaybackURI.indexOf("&size="));

						var objTr = $("#searchlist").get(0).insertRow(-1);
						var objTd = objTr.insertCell(0);
						objTd.id = "downloadTd" + i;
						objTd.innerHTML = iSearchTimes * 40 + (i + 1);
						objTd = objTr.insertCell(1);
						objTd.width = "30%";
						objTd.innerHTML = szFileName;
						objTd = objTr.insertCell(2);
						objTd.width = "30%";
						objTd.innerHTML = (szStartTime.replace("T", " ")).replace("Z", "");
						objTd = objTr.insertCell(3);
						objTd.width = "30%";
						objTd.innerHTML = (szEndTime.replace("T", " ")).replace("Z", "");
						objTd = objTr.insertCell(4);
						objTd.width = "10%";
						objTd.innerHTML = "<a href='javascript:;' onclick='clickStartDownloadRecord(" + i + ");'>下载</a>";
						$("#downloadTd" + i).data("playbackURI", szPlaybackURI);
					}
					alert(szIP + " 搜索录像文件成功！");
				} else if("NO MATCHES" === $(xmlDoc).find("responseStatusStrg").eq(0).text()) {
					setTimeout(function() {
						alert(szIP + " 没有录像文件！");
					}, 50);
				}
			},
			error: function () {
				alert(szIP + " 搜索录像文件失败！");
			}
		});
	}
	
	// 开始回放
	function clickStartPlayback(szIP,iChannelID,szStartTime,szEndTime) {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex);
		//bZeroChannel = $("#channels option").eq($("#channels").get(0).selectedIndex).attr("bZero") == "true" ? true : false,
		bZeroChannel=false;
		szInfo = "",
		bChecked =false;
		iRet = -1;

		if ("" == szIP) {
			return;
		}

		if (bZeroChannel) {// 零通道不支持回放
			return;
		}

		if (oWndInfo != null) {// 已经在播放了，先停止
			WebVideoCtrl.I_Stop();
		}

		if (bChecked) {// 启用转码回放
			var oTransCodeParam = {
				TransFrameRate: "16",// 0：全帧率，5：1，6：2，7：4，8：6，9：8，10：10，11：12，12：16，14：15，15：18，13：20，16：22
				TransResolution: "2",// 255：Auto，3：4CIF，2：QCIF，1：CIF
				TransBitrate: "23"// 2：32K，3：48K，4：64K，5：80K，6：96K，7：128K，8：160K，9：192K，10：224K，11：256K，12：320K，13：384K，14：448K，15：512K，16：640K，17：768K，18：896K，19：1024K，20：1280K，21：1536K，22：1792K，23：2048K，24：3072K，25：4096K，26：8192K
			};
			iRet = WebVideoCtrl.I_StartPlayback(szIP, {
				iChannelID: iChannelID,
				szStartTime: szStartTime,
				szEndTime: szEndTime,
				oTransCodeParam: oTransCodeParam
			});
		} else {
			iRet = WebVideoCtrl.I_StartPlayback(szIP, {
				iChannelID: iChannelID,
				szStartTime: szStartTime,
				szEndTime: szEndTime
			});
		}

		if (0 == iRet) {
			szInfo = "开始回放成功！";
		} else {
			szInfo = "开始回放失败！";
		}
		alert(szInfo);
	}
	
	// 停止回放
	function clickStopPlayback() {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
			szInfo = "";

		if (oWndInfo != null) {
			var iRet = WebVideoCtrl.I_Stop();
			if (0 == iRet) {
				szInfo = "停止回放成功！";
			} else {
				szInfo = "停止回放失败！";
			}
			alert(szInfo);
		}
	}
	
	// 慢放
	function clickPlaySlow() {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
			szInfo = "";

		if (oWndInfo != null) {
			var iRet = WebVideoCtrl.I_PlaySlow();
			if (0 == iRet) {
				szInfo = "慢放成功！";
			} else {
				szInfo = "慢放失败！";
			}
			alert(szInfo);
		}
	}

	// 快放
	function clickPlayFast() {
		var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
			szInfo = "";

		if (oWndInfo != null) {
			var iRet = WebVideoCtrl.I_PlayFast();
			if (0 == iRet) {
				szInfo = "快放成功！";
			} else {
				szInfo = "快放失败！";
			}
			alert(szInfo);
		}
	}

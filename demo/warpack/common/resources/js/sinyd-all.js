var global_param = {
		context_name: '/fam',
		tab_number: 1, // 不控制显示标签数
        tab_limit_tip: true, // 标志标签提示是否已经给出，如果没有给出那么当标签数量超过指定数量的时候则给予提示，如已经提示过了那么直接自动关闭最早打开的标签
        tab_limit_tip_content: '主页默认最多同时打开7个标签页，请您关闭不用的标签页后再打开新页，默认会关闭最早打开的标签页，此提示不再出现。', // 关闭标签提示
        layout_bottomHeight:-4, //标志主页面上最下面面板的高度
        layout_leftWidth: 222, //标志主页面上左侧菜单面板的宽度
        layout_titleHeight: 30, //面板标题高度，与样式“l-tab-links”中的height相关
        tree_menu_backcolor: '#e9e9eb', //树状菜单背景色
        tree_menu_nodeWidth: 177, //树状菜单选中宽度
		version: '?v='+new Date().getTime(),  // 解决静态缓存问题加入的版本信息
		session_check: true, // 是否检查session过期设置
		use_cookie:true, // 设置是否使用cookie
		min_date: '2013-09-01' // 本系统支持的最小日期
};




document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/jquery-1.8.3.min.js"></script>');
document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/jquery.cookie.js"></script>');

document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/ligerui.all-min.js"></script>');
document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/date/WdatePicker.js"></script>');
document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/jquery.json-2.3.js"></script>');
document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/jquery.query-2.1.7.js"></script>');

document.write('<script language="Javascript" src="'+global_param.context_name+'/resources/js/syd-min.js"></script>');



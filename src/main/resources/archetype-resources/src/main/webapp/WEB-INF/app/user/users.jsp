<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="utf-8" />
	<title>Dashboard - Okdi Admin</title>

	<meta name="description" content="overview &amp; stats" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

	<!-- bootstrap & fontawesome -->
	<link rel="stylesheet" href="../assets/css/bootstrap.css" th:href="@{${session.resources}+'/vendor/assets/css/bootstrap.min.css'}"/>
	<link rel="stylesheet" href="../assets/css/font-awesome.css" th:href="@{${session.resources}+'/vendor/assets/css/font-awesome.min.css'}" />

	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="../assets/css/jquery-ui.css" th:href="@{${session.resources}+'/vendor/assets/css/jquery-ui.min.css'}"/>
	<link rel="stylesheet" href="../assets/css/datepicker.css" th:href="@{${session.resources}+'/vendor/assets/css/datepicker.min.css'}"/>
	<link rel="stylesheet" href="../assets/css/ui.jqgrid.css" th:href="@{${session.resources}+'/vendor/assets/css/ui.jqgrid.min.css'}"/>
	
	<!-- text fonts -->
	<link rel="stylesheet" href="../assets/css/ace-fonts.css" th:href="@{${session.resources}+'/vendor/assets/css/ace-fonts.min.css'}" />

	<!-- ace styles -->
	<link rel="stylesheet" href="../assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" th:href="@{${session.resources}+'/vendor/assets/css/ace.min.css'}" />

	<!--[if lte IE 9]>
		<link rel="stylesheet" href="../assets/css/ace-part2.css" class="ace-main-stylesheet" th:href="@{${session.resources}+'/vendor/assets/css/ace-part2.min.css'}"/>
	<![endif]-->

	<!--[if lte IE 9]>
	  <link rel="stylesheet" href="../assets/css/ace-ie.css" th:href="@{${session.resources}+'/vendor/assets/css/ace-ie.min.css'}"/>
	<![endif]-->

	<!-- inline styles related to this page -->

	<!-- ace settings handler -->
	<script src="../assets/js/ace-extra.js" th:src="@{${session.resources}+'/vendor/assets/js/ace-extra.min.js'}"></script>

	<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

	<!--[if lte IE 8]>
	<script src="../assets/js/html5shiv.js" th:src="@{$session.resources}+'/vendor/assets/js/html5shiv.min.js'"></script>
	<script src="../assets/js/respond.js" th:src="@{$session.resources}+'/vendor/assets/js/respond.min.js'></script>
	<![endif]-->
</head>

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default" th:replace="base/navbar :: navbar-container">
	此处是顶部导航栏
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<script type="text/javascript">
		try{ace.settings.check('main-container' , 'fixed')}catch(e){}
	</script>

	<!-- #section:basics/sidebar -->
	<div id="sidebar" class="sidebar responsive" th:replace="base/sidebar-menu :: sidebar-menu">此处是侧边导航栏</div>

	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<!-- #section:basics/content.breadcrumbs -->
			<div class="breadcrumbs" id="breadcrumbs" th:replace="base/sidebar-menu :: breadcrumbs">此处是书签导航条</div>

			<!-- /section:basics/content.breadcrumbs -->
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
						<!-- PAGE CONTENT BEGINS -->
						<div id="query-form"></div>
						<table id="grid-table"></table>
						<div id="grid-pager"></div>
						<!-- PAGE CONTENT ENDS -->
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->

	<div class="footer" th:replace="base/navbar :: footer">此处是网站页脚</div>

	<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
		<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
	</a>
</div><!-- /.main-container -->

<!-- basic scripts -->
<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="../assets/js/excanvas.min.js" th:src="@{${session.resources} + '/vendor/assets/js/excanvas.min.js'}"></script>
<![endif]-->
<script src="../assets/js/jquery1x.min.js" th:src="@{${session.resources} + '/vendor/assets/js/jquery1x.min.js'}"></script>
<script src="../assets/js/bootstrap.js" th:src="@{${session.resources} + '/vendor/assets/js/bootstrap.min.js'}"></script>
<script src="../assets/js/jquery-ui.custom.js" th:src="@{${session.resources} + '/vendor/assets/js/jquery-ui.custom.min.js'}"></script>

<!-- ace scripts 
<script src="../assets/js/ace/elements.scroller.js"></script>
<script src="../assets/js/ace/elements.colorpicker.js"></script>
<script src="../assets/js/ace/elements.fileinput.js"></script>
<script src="../assets/js/ace/elements.typeahead.js"></script>
<script src="../assets/js/ace/elements.wysiwyg.js"></script>
<script src="../assets/js/ace/elements.spinner.js"></script>
<script src="../assets/js/ace/elements.treeview.js"></script>
<script src="../assets/js/ace/elements.wizard.js"></script>
<script src="../assets/js/ace/elements.aside.js"></script>
<script src="../assets/js/ace/ace.ajax-content.js"></script>
<script src="../assets/js/ace/ace.touch-drag.js"></script>
<script src="../assets/js/ace/ace.widget-box.js"></script>
<script src="../assets/js/ace/ace.settings-rtl.js"></script>
<script src="../assets/js/ace/ace.settings-skin.js"></script>
<script src="../assets/js/ace/ace.widget-on-reload.js"></script>
<script src="../assets/js/ace/ace.searchbox-autocomplete.js"></script>-->
<script src="../assets/js/ace/ace.js" th:src="@{${session.resources} + '/vendor/assets/js/ace.min.js'}"></script>
<script src="../assets/js/ace.sidebar.js" th:src="@{${session.resources} + '/vendor/assets/js/ace.sidebar.js'}"></script>
<script src="../assets/js/ace.submenu-hover.js" th:src="@{${session.resources} + '/vendor/assets/js/ace.submenu-hover.js'}"></script>

<script src="../assets/js/jqGrid/jquery.jqGrid.src.js" th:src="@{${session.resources} + '/vendor/assets/js/jqGrid/jquery.jqGrid.min.js'}"></script>
<script src="../assets/js/jqGrid/i18n/grid.locale-en.js" th:src="@{${session.resources} + '/vendor/assets/js/jqGrid/i18n/grid.locale-cn.js'}"></script>
<script src="./app/user/js/users.js" th:src="@{${session.resources} + '/app/user/js/users.js'}"></script>
<script src="./app/base/js/base.js" th:src="@{${session.resources} + '/app/base/js/base.js'}"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
jQuery(function($) {


})
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>用户管理--后台系统</title>

    <!-- Bootstrap core CSS -->
    <link href="/resource/lib/css/bootstrap.min.css" th:href="@{${ session.resources }+'/lib/css/bootstrap.min.css'}" rel="stylesheet">
    <link href="/resource/lib/css/font-awesome.min.css" th:href="@{${ session.resources }+'/lib/css/font-awesome.min.css'}" rel="stylesheet">
	
	<!-- JQgrid sytles -->
    <link href="/resource/css/custom-theme/jquery-ui.min.css" th:href="@{${ session.resources }+'/css/custom-theme/jquery-ui.min.css'}" rel="stylesheet">
	<link href="http://cdn.bootcss.com/jqgrid/4.6.0/css/ui.jqgrid.css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="/resource/css/dashboard.css" th:href="@{${ session.resources }+'/css/dashboard.css'}" rel="stylesheet">
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <form action="" method="post">
    	<label>登录ID</label><input name="loginId" value="test@amssy.com" th:value="${userInfo.loginId}"/>
    	<label>用户名</label><input name="userName" value="test@amssy.com" th:value="${userInfo.userName}"/>
    	<label>手机号</label><input name="phone" value="13588888888" th:value="${userInfo.phone}"/>
    	<label>创建时间</label><input name="createTime" type="date" value="2015-01-01 00:00:00" th:value="${userInfo.createTime}"/>
    	<hr/>
    	${userInfo.createTime }
    </form>
  </body>
</html>
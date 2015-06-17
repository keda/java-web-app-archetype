<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
  <head>
    <title>登录-APP</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	
	<!-- Bootstrap core CSS -->
    <link href="/resouces/lib/css/bootstrap.min.css" th:href="@{${session.resources}+'/vendor/assets/css/bootstrap.min.css'}" rel="stylesheet"/>

    <!-- Custom styles for this template -->
    <link href="/resouces/css/signin.css" th:href="@{${session.resources}+'/css/signin.css'}" rel="stylesheet"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

  </head>
  
  <body>
    
    <div class="container">
     
      <form class="form-signin" action="auth/login" method="post">
        <h2 class="form-signin-heading">请登陆</h2>
        <label for="inputEmail" class="sr-only">邮箱地址</label>
        <input type="username" name="username" id="inputEmail" class="form-control" placeholder="邮箱地址" required autofocus>
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="密码" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> 记住我
          </label>
        </div>
        <!-- <button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button> -->
        <input type="submit" class="btn btn-lg btn-primary btn-block" value="登陆">
      </form>

    </div> <!-- /container -->
    
  </body>
</html>

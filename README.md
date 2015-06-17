# java-web-app-archetype
springmvc mybatis maven archetype

使用说明:

maven骨架项目,包含：
. spring 3.2.3.RELEASE
. spring-mvc 3.2.3.RELEASE
. jackson 2.4.4
. commons-lang 2.4
. junit 4.7
. logback 1.0.13 + slf4j 1.7.5 日志框架
. mybatis 3.2.8
. 支持redis jedis 2.7.2
. httpclient 4.3.6
. 邮件服务
. 模板引擎使用的是 thymeleaf
具体配置在生成后的项目的下面这个目录中：src/main/resources/META-INF/spring/beans

要求：在本地配置MAVEN环境

在群共享下载amssy-web-archtype.zip, 解压到任意目录下面**/amssy-web-archtype

打开命令行,进入到刚解压的那个目录 cd **/amssy-web-archtype

安装骨架：mvn clean install

使用骨架生成web项目,进入到自己的workspace cd **/workspace

使用下面的命令, 命令行中命令是一行的
mvn archetype:generate 
	-DarchetypeGroupId=com.amssy 
	-DarchetypeArtifactId=amssy-web-archetype 
	-DarchetypeVersion=0.0.1-SNAPSHOT 
	-DarchetypeCatalog=local 
	-DgroupId=com.amssy 
	-DartifactId=yunying 
	-Dversion=0.0.1 
	-Dpackage=com.amssy.yunying
	
使用MyEclipse导入MAVEN项目,选择刚刚生成的那个项目,
导入完成后选择 pom.xml 右键
选择Run As ---> 2 maven build... ---> 在弹窗的窗口中Goals中输入 tomcat7:run
点击Apply , Run启动项目

默认地址: http://localhost:8080/${artifactId}
	
命令讲解:
mvn archetype:generate 
	-DarchetypeGroupId=com.amssy -------------------骨架项目groupId			   |
	-DarchetypeArtifactId=amssy-web-archetype ------骨架项目artifactId         |不要改变
	-DarchetypeVersion=0.0.1-SNAPSHOT --------------骨架项目version            |
	-DarchetypeCatalog=local -----------------------在本地仓库中读取骨架项目   |------------
	-DgroupId=com.amssy ----------------------------你创建的项目的groupId      |
	-DartifactId=yunying ---------------------------你创建的项目的artifactId   |任意的
	-Dversion=0.0.1 --------------------------------你创建的项目的version      |符合自己
	-Dpackage=com.amssy.yunying	--------------------你的项目的package          |的需求即可

<%@ page import="servlet_lession.servlet2021_12_25.User" %>
<%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/25
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>


<%
  System.out.println("你好，jsp。我是hello world");
  int a = 1;
  request.setAttribute("a",a);
%>
<h3>算术运算符</h3>
<p>${3+4}</p>
<p>${3/4}</p>
<p>${3 div 4}</p>
<p>${3%4}</p>
<p>${3 mod 4}</p>


<h3>比较运算符</h3>
<p>${3==4}</p>
<h3>逻辑运算符</h3>
<p>${3<=4&&5>=3}</p>
<p>${3<=4 and 5>=3}</p>
<h1>empty运算符<h1>

    <%
String s = new String("avc");
List  b = new ArrayList();
List c = null;
request.setAttribute("s",s);
request.setAttribute("b",b);
request.setAttribute("c",c);
%>

  ${empty b}   true
  ${not empty c} false
  ${empty s}   false


      <h2>获取域对象</h2>
        <% session.setAttribute("name","李四");%>
        <% request.setAttribute("name","张三");%>
        <% session.setAttribute("age","18");%>
      ${requestScope.get("name")};
      ${sessionScope.get("age")}
      ${sessionScope.get("hahaha")}


        <%
            User user = new User();
            user.setName("张三");
          user.setAge(18);
          user.setBirthday(new Date());
          request.setAttribute("use",user);
        %>
      ${requestScope.use}
      ${requestScope.use.age}
      ${requestScope.use.name}



        <%
    User usertwo = new User();
    usertwo.setName("张浩琦");
  usertwo.setAge(18);
  usertwo.setBirthday(new Date());
  request.setAttribute("usertwo",usertwo);
    List aa  = new ArrayList();
    aa.add("aaa");
    aa.add("bbb");
    aa.add("ccc");
    //添加对象到容器
    aa.add(usertwo);
    Map map = new HashMap();
    map.put("name","13");
    //添加对象到集合
    map.put("user",user);
    //添加到共享数据
    request.setAttribute("list",aa);
    request.setAttribute("map",map);
%>
      <h1>容器和集合</h1>
      ${list[0]}
      ${list[1]}
      ${list[2]}
      ${map.age}
      ${map["user"].name}

</body>
</html>

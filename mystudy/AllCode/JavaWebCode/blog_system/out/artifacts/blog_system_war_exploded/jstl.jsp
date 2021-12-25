<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2021/12/25
  Time: 12:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    List array = new ArrayList();
    array.add("aa");
    request.setAttribute("array",array);
    request.setAttribute("number",3);
%>

<%--判断array是否为空，如果不为空，则显示遍历集合--%>
<c:if test="${not empty requestScope.array}">遍历集合</c:if>
<c:if test="${number%2==1}">
<h1>number是奇数<h1>
    </c:if>
    <c:if test="${number%2==0}">
    <h1>number是偶数<h1>
        </c:if>

</body>
</html>

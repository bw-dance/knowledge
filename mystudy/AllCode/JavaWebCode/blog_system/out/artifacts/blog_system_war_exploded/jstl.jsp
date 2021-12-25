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
//    array.add("aa");
    request.setAttribute("array", array);
    request.setAttribute("number", 4);
%>

<%--判断array是否为空，如果不为空，则显示遍历集合--%>
<%--<c:if test="${not empty requestScope.array}">遍历集合</c:if>--%>
<%--<c:if test="${number%2==1}">--%>
<%--<h1>number是奇数<h1>--%>
<%--    </c:if>--%>
<%--    <c:if test="${number%2==0}">--%>
<%--    <h1>number是偶数<h1>--%>
<%--        </c:if>--%>

<%--            <c:choose >--%>
<%--            <c:when test="${number==1}">星期1</c:when>  --%>
<%--            <c:when test="${number==2}">星期2</c:when>--%>
<%--            <c:when test="${number==3}">星期3</c:when>--%>
<%--            <c:when test="${number==4}">星期4</c:when>--%>
<%--            <c:when test="${number==5}">星期5</c:when>--%>
<%--            <c:when test="${number==6}">星期6</c:when>--%>
<%--            <c:when test="${number==7}">星期7</c:when>--%>
<%--            <c:otherwise>输入有误</c:otherwise>--%>
<%--            </c:choose>--%>

<%--<c:forEach begin="0" end="10" var="b" step="1" varStatus="s">--%>
<%--    ${b}  ${s.index} ${s.count};--%>
<%--    <br>--%>
<%--</c:forEach>--%>


<%
    List<String> list =new ArrayList<String>();
    list.add("aaa");
    list.add("bbb");
    list.add("ccc");
    request.setAttribute("list",list);
%>
<c:if test="${not empty list}">
    <c:forEach items="${list}" var="item" varStatus="s">
        ${s.index} ${s.count} ${item}
    </c:forEach>
</c:if>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: ZLC
  Date: 2020/5/23
  Time: 2:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
全部商品列表：
<c:forEach items="${items}" var="item" varStatus="status">
    <c:if test="${status.first}">
        <br/><span style="color: blueviolet">总计:</span>${fn:length(items)} 件商品
    </c:if>
    <br/>${status.index+1}.<span style="color: blue">商品名称：</span>${item.name}，<span style="color: orangered">商品剩余数量：</span>${item.quantity} &nbsp |||| &nbsp <a href="">修改剩余数量</a> &nbsp <a href="">删除</a> &nbsp <a href="/item/cacheItem?itemId=${item.id}">添加到缓存</a> &nbsp <a href="">同步缓存与数据库</a>
</c:forEach>


<br/>
<br/>
操作：
<a href="/item-add.jsp">添加商品</a>
</body>
</html>

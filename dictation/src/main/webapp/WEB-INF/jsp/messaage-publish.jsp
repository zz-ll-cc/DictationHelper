<%--
  Created by IntelliJ IDEA.
  User: ZLC
  Date: 2020/5/21
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/message/publish" method="post" enctype="multipart/form-data">
    titleImage:<input type="file" name="titleImage" />
    <br/><br/>
    title:<input type="text" name="title"/>
    <br/><br/>
    subtitle:<input type="text" name="subtitle" />
    <br/><br/>
    content:<input type="text" name="content" />
    <br/><br/>
    typeName:<input type="text" name="typeName" />
    <br/><br/>
    <button type="submit">发布消息</button>
</form>

</body>
</html>

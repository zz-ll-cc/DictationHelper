<%--
  Created by IntelliJ IDEA.
  User: ZLC
  Date: 2020/4/15
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/upload/uploadWordImage" method="post" enctype="multipart/form-data">
    wid:<input type="text" name="wid"/>
    <br/><br/>
    file:<input type="file" name="file" />
    <br/><br/>
    <button type="submit">上传至七牛</button>
</form>
</body>
</html>

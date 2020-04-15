<%--
  Created by IntelliJ IDEA.
  User: ZLC
  Date: 2020/4/15
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/upload/uploadBookImage" method="post" enctype="multipart/form-data">
    bid:<input type="text" name="bid"/>
    <br/><br/>
    file:<input type="file" name="file" />
    <br/><br/>
    <button type="submit">上传至七牛</button>
</form>

</body>
</html>

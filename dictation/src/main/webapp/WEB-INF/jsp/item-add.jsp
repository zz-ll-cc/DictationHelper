<%--
  Created by IntelliJ IDEA.
  User: ZLC
  Date: 2020/5/23
  Time: 2:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/item/add" method="post" enctype="multipart/form-data">
    name:<input type="text" name="name"/>
    <br/><br/>
    cover:<input type="file" name="cover" />
    <br/><br/>
    description:<input type="text" name="description"/>
    <br/><br/>
    quantity:<input type="number" name="quantity"/>
    <br/><br/>
    itemType:<input type="number" name="itemType"/>
    <br/><br/>
    price:<input type="number" name="price"/>
    <br/><br/>

    <button type="submit">提交</button>


</form>

</body>
</html>

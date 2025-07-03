<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/account/update">
    <%--使用隐藏域，保存id的值，更新时作为条件--%>
    <input type="hidden" name="id" value="${account.id}">
    <div >
        <label>账户名</label>
        <div>
            <input type="text"  id="username" name="name" value="${account.name}"
                   placeholder="请输入账户名">
        </div>
    </div>
    <div>
        <label for="money">余额</label>
        <div>
            <input type="text" id="money" name="money" value="${account.money}"
                   placeholder="请输入余额">
        </div>
    </div>
    <div>
        <div>
            <button type="submit" class="btn btn-default">更新</button>
        </div>
    </div>
</form>
</body>


</html>

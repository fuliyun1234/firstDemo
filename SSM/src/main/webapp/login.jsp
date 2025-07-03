<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div style="text-align:center;margin-top:50px">
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/login">
    
    <div>
        <label>用户名</label>
            <input type="text"  id="name" name="name"
                   placeholder="请输入用户名">
    </div>
    <div>
        <label>密&nbsp&nbsp&nbsp码</label>
            <input type="password" id="money" name="password"
                   placeholder="请输入密码">
    </div>
        <div>
            <button type="submit" class="btn btn-default">登录</button>
        </div>
</form>
</div>
</body>

</html>

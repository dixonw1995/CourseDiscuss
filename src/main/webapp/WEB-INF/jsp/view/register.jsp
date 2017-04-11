<html>
    <body>
        <c:if test="${param.UsernameExists != null}">
            <p>Username is used already.</p>
        </c:if>
        <h2>Registration</h2> 
        <form:form method="post" enctype="multipart/form-data" 
                   modelAttribute="user">
            <form:label path="username">Username:</form:label> 
            <form:input type="text" path="username" /> <br />
            <form:label path="password">Password:</form:label> 
            <form:input type="password" path="password" /> <br />
            <input type="submit" name="register" value="Register" />
        </form:form>
    </body>
</html>
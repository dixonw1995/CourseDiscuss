<html>
    <body>
        <c:if test="${param.error != null}">
            <p>Login failed.</p>
        </c:if>
        <c:if test="${param.logout != null}">
            <p>You have logged out.</p>
        </c:if>
        <c:if test="${param.register != null}">
            <p>You have registered.</p>
        </c:if>
        <h2>Login</h2> 
        <form action="login" method='POST'>
            Username: <input type='text' name='username'><br />
            Password: <input type='password' name='password' /><br />
            Remember Me: <input type="checkbox" name="remember-me" /><br />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input name="submit" type="submit" value="Log In" /><br />
        </form>
        <c:url var="registerUrl" value="/register"/>
        <form:form action="${registerUrl}" method="get">
            <input type="submit" name="register" value="Register" />
        </form:form>    
    </body>
</html>
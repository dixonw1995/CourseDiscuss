<html>
    <body>
        <h2>Edit ${username}'s Account</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="user">
            <form:label path="password">Password</form:label><br/>
            <form:input type="text" path="password" /><br/><br/>
            <form:label path="roles">Roles</form:label><br/>
            <form:checkbox path="roles" value="ROLE_BANNED" />ROLE_BANNED
            <form:checkbox path="roles" value="ROLE_USER" />ROLE_USER
            <form:checkbox path="roles" value="ROLE_ADMIN" />ROLE_ADMIN
            <br /><br />
            <input type="submit" value="Save"/>
        </form:form>
    </body>
</html>
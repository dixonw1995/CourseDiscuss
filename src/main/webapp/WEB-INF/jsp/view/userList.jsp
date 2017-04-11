<html>
    <body>
        <c:if test="${param.create != null}">
            <p>New user is created.</p>
        </c:if>
        <c:if test="${param.UsernameExists != null}">
            <p>Username is used already.</p>
        </c:if>
        <c:if test="${param.edit != null}">
            <p>User information is editted.</p>
        </c:if>
        <c:if test="${param.UserNotFound != null}">
            <p>User doesn't exist.</p>
        </c:if>
        <c:if test="${param.delete != null}">
            <p>User is deleted.</p>
        </c:if>
        [<a href="<c:url value="/user/create" />">Create user</a>]<br /><br />
        <h2>Users</h2>
        <c:choose>
            <c:when test="${fn:length(users) == 0}">
                <i>There are no users in the system.</i>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Roles</th>
                    </tr>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.password}</td>
                            <td>
                                <c:forEach items="${user.roles}" var="role" varStatus="status">
                                    <c:if test="${!status.first}">, </c:if>
                                    ${role.role}
                                </c:forEach>
                            </td>
                            <td>
                                <security:authentication var="principal" property="principal" />
                                <c:if test="${principal.username != user.username}">
                                    [<a href="<c:url value="/user/delete/${user.username}" />">Delete</a>]
                                </c:if></td>
                            <td>[<a href="<c:url value="/user/update/${user.username}" />">Edit</a>]</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </body>
</html>
<html>
    <body>
        <c:if test="${fn:length(users) > 0}">
            <ul>
                <c:forEach var="user" items="${users}">
                    <li>
                        ${user.username}(${user.roles}) : ${user.password}
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </body>
</html>

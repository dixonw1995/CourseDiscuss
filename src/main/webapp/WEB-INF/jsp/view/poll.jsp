<html>
    <body>
        <c:if test="${param.create != null}">
            <p>New poll is created.</p>
        </c:if>
        <c:if test="${param.UsernameExists != null}">
            <p>You have voted this poll.</p>
        </c:if>
        <c:if test="${param.UserNotFound != null}">
            <p>User doesn't exist.</p>
        </c:if>
        <c:if test="${param.delete != null}">
            <p>Poll is deleted.</p>
        </c:if>
        <security:authorize access="hasRole('ADMIN')">
            [<a href="<c:url value="/poll/create" />">Create poll</a>]<br /><br />
        </security:authorize>
        <h2>Polls</h2>
        <c:choose>
            <c:when test="${fn:length(polls) == 0}">
                <i>There are no polls in the system.</i>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>Question</th>
                        <th>Choice</th>
                        <th></th>
                    </tr>
                    <c:url var="vote_url" value='/poll/vote' />
                    <c:forEach items="${polls}" var="poll">
                        <tr>
                            <td>Poll#<c:out value="${poll.id}" />. <c:out value="${poll.question}"  /></td>
                            <form:form method="POST" action="${vote_url}"
                                       enctype="multipart/form-data" modelAttribute="vote">
                                <c:forEach items="${poll.responses}" var="response">
                                    <td>
                                        <security:authorize access="!hasRole('ANONYMOUS')">
                                            <form:radiobutton path="responseId" value="${response.id}" />
                                        </security:authorize>
                                        <form:label path="responseId"><c:out value="${response.content} (${fn:length(response.votes)})" /></form:label><br/>
                                        </td>
                                </c:forEach>
                                <security:authorize access="!hasRole('ANONYMOUS')">
                                    <td><input type="submit" value="Vote"/></td>
                                    </security:authorize>
                                </form:form>
                                <security:authorize access="hasRole('ADMIN')" >
                                <td>[<a href="<c:url value="/poll/delete/${poll.id}" />">Delete</a>]</td>
                            </security:authorize>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </body>
</html>
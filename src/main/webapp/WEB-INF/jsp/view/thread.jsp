<html>
    <body>
        <c:if test="${param.reply != null}">
            <p>Your reply is posted.</p>
        </c:if>
        <security:authorize access="hasRole('ADMIN')">            
            [<a href="<c:url value="/forum/delete/thread/${thread.id}" />">Delete</a>]
        </security:authorize>
        <h2>Thread #${thread.id}: <c:out value="${thread.title}" />(<c:out value="${fn:length(thread.posts)-1}" />)</h2>
        <c:forEach items="${thread.posts}" var="post" varStatus="status">
            <i>#${status.index} By <c:out value="${post.username}" /></i>[<a href='<c:url value="/forum/delete/reply/${post.id}" />'>Delete</a>]<br /><br />
            <c:out value="${post.content}" /><br /><br />
            <c:if test="${fn:length(post.attachments) > 0}">
                Attachments:
                <c:forEach items="${post.attachments}" var="attachment"
                           varStatus="status">
                    <c:if test="${!status.first}">, </c:if>
                    <a href="<c:url value="/forum/post/${post.id}/attachment/${attachment.name}" />">
                        <c:out value="${attachment.name}" /></a>
                </c:forEach><br /><br />
            </c:if>
        </c:forEach>

        <security:authorize access="hasAnyRole('ADMIN', 'USER') and !hasRole('BANNED')">
            <c:url var="reply_url" value='/forum/thread/${thread.id}/reply' />
            <form:form method="POST" action="${reply_url}"
                       enctype="multipart/form-data" modelAttribute="reply">
                <form:label path="content">Reply</form:label><br/>
                <form:textarea path="content" rows="5" cols="30" /><br/><br/>
                <b>Attachments</b><br/>
                <input type="file" name="attachments" multiple="multiple"/><br/><br/>
                <input type="submit" value="Reply"/>
            </form:form>
        </security:authorize>

        <a href="<c:url value="/forum" />">Return to Forum</a>
    </body>
</html>
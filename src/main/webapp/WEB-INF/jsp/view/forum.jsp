<html>
    <body>
        <ul>
            <li><h4><a href="<c:url value='/forum/category/Lecture' />">Lecture</a></h4>(${lectureThreadCount})</li>
            <li><h4><a href="<c:url value='/forum/category/Lab' />">Lab</a></h4>(${labThreadCount})</li>
            <li><h4><a href="<c:url value='/forum/category/Other' />">Other</a></h4>(${otherThreadCount})</li>
        </ul>
        <c:if test="${not empty category}">
            <h2>${category}</h2>
            <c:choose>
                <c:when test="${fn:length(threads) == 0}">
                    <i>There are no threads in this category.</i>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${threads}" var="thread">
                        Thread ${thread.id}:
                        <a href="<c:url value="/forum/thread/${thread.id}" />">
                            <c:out value="${thread.title}" /></a>
                        (Reply: <c:out value="${fn:length(thread.posts)-1}" />)
                        <security:authorize access="hasRole('ADMIN')">            
                            [<a href="<c:url value="/forum/delete/thread/${thread.id}" />">Delete</a>]
                        </security:authorize>
                        <br /><br />
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <security:authorize access="hasAnyRole('ADMIN', 'USER') and !hasRole('BANNED')">
                [<a href="<c:url value="/forum/category/${category}/post" />">Post new thread</a>]<br /><br />
            </security:authorize>
        </c:if>
        <div><h4>Poll</h4></div>
    </body>
</html>
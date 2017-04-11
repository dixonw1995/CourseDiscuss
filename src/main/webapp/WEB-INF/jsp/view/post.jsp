<html>
    <body>
        <h2>New Thread</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="thread">
            <form:label path="title">Title</form:label><br/>
            <form:input type="text" path="title" /><br/><br/>
            <form:label path="content">Content</form:label><br/>
            <form:textarea path="content" rows="5" cols="30" /><br/><br/>
            <b>Attachments</b><br/>
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Post"/>
        </form:form>
    </body>
</html>
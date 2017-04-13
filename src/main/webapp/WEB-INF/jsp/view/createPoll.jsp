<html>
    <body>
        <h2>Create New Poll</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="poll">
            <form:label path="question">Question</form:label><br/>
            <form:textarea path="question" rows="5" cols="30"  /><br/><br/>
            <form:label path="responses">Response</form:label><br/>
            <form:input type="text" path="responses" /><br/><br/>
            <form:input type="text" path="responses" /><br/><br/>
            <form:input type="text" path="responses" /><br/><br/>
            <form:input type="text" path="responses" /><br/><br/>
            <br /><br />
            <input type="submit" value="Publish"/>
        </form:form>
    </body>
</html>
<%@page
    import="java.util.*"
    import="helpers.*"
%><%
	//JSON library .jar file is in the google drive. right click on project -> build path  libraries/add external jar  
	AnalyticsHelper analyzer = new AnalyticsHelper(request);
	String table = request.getParameter("table");
	String diffs = analyzer.parseJSONStringToTable(table);
	System.out.println(table);
%><%=diffs%>
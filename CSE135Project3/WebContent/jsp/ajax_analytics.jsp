<%@page
    import="java.util.*"
    import="helpers.*"%>
    
<%
	AnalyticsHelper analyzer = new AnalyticsHelper(request);
	String table = request.getParameter("table");
	System.out.println(table);
%>

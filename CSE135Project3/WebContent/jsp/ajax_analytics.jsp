<%@page
    import="java.util.List"
    import="helpers.*"%>
    
<%
	AnalyticsHelper analyzer = new AnalyticsHelper(request);
	String requestString = request.toString();
	System.out.println(requestString);
%>

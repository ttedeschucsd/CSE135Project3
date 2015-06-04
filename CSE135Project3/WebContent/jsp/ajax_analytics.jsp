<%@page
    import="java.util.List"
    import="helpers.*"%>
    
<%
	AnalyticsHelper analyzer = new AnalyticsHelper(request);
	TableHelper itemTable = analyzer.submitQuery(request);
%>

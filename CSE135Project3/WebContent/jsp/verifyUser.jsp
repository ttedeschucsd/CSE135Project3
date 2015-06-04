<%@ page import="java.sql.Connection" import="java.sql.ResultSet" import="java.sql.SQLException" import="java.sql.Statement"
		 import="helpers.*"%>

<% response.setContentType("text"); %>
<% Connection conn = null;
String query = "SELECT name FROM users";
try{
	conn = HelperUtils.connect();
} catch (Exception e){
	System.out.println("Could not register PostgreSQL JDBC driver with the DriverManager");
}
String name = request.getParameter("name");
//String age = request.getParameter("age");
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(query);
	System.out.println("name? " + name);
	while(rs.next()){
		if(rs.getString(1).equals(name) && name.length() >= 1){ 
			System.out.println("here");
			%>
				User name already exists
		<% }
	}
%>

<%@ page import="java.sql.Connection" import="java.sql.ResultSet" import="java.sql.SQLException" import="java.sql.Statement"
		 import="helpers.*"%>ß
		 
<% response.setContentType("text"); %>
<% Connection conn = null;
String query = "SELECT name FROM users";
System.out.println(query);
try{
	conn = HelperUtils.connect();
} catch (Exception e){
	System.out.println("Could not register PostgreSQL JDBC driver with the DriverManager");
}
String name = request.getParameter("name");
System.out.println("name? " + name);

ResultSet rc = null;
boolean duplicate = false;
Statement stmt = conn.createStatement();

rc = stmt.executeQuery(query);
while(rc.next()){
	if(rc.getString(1).equals(name) && name.length() >= 1 && name != ""){
		duplicate = true;
		break;
	}
}

if(duplicate == true){%>
	User name is not available
<%} else if(duplicate == false) {%>
	User name available
<%}

%>
<%@ page import="java.sql.Connection" import="java.sql.ResultSet" import="java.sql.SQLException" import="java.sql.Statement"
		 import="helpers.*"%>
		 
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
Integer age = Integer.parseInt(request.getParameter("age"));
String role = request.getParameter("role");
String state = request.getParameter("state");
String returnVal = null;

System.out.println("name? " + name);

ResultSet rc = null;
boolean duplicate = false;
Statement stmt = conn.createStatement();

rc = stmt.executeQuery(query);
while(rc.next()){
	if(rc.getString(1).equals(name) && name.length() >= 1){
		duplicate = true;
	} 
}
System.out.println("duplicate? " + duplicate);
if(duplicate == false){
	returnVal = SignupHelper.signup(name, age, role, state);
	System.out.println("returnVal? " + returnVal);
%>
	You have successfully registered
<%} else if(duplicate == true){ %>
	User name not available
<%}%>

<%@page
    import="java.util.List"
    import="helpers.*"%>
    <script type="text/JavaScript" src="js/list-analytics.js"></script>
    
    <style>
		table{
    		border: 1px solid black;
 		}
 		
 		td{
 			border: 1px solid black;
 			text-align: center;
 			padding: 10px;
 		} 
	</style> 
    

<% List<CategoryWithCount> categories = CategoriesHelper.listCategories();
	AnalyticsHelper analyzer = new AnalyticsHelper(request);
	TableHelper itemTable = analyzer.submitQuery(request);
%>
<div id="dropdowns">
	<form name="query_form" action="analytics" method="post">
		<label for="categories_dropdown"></label>
		<select name="categories_dropdown">
			<option value = "0">All Categories</option>
		<%
        	for (CategoryWithCount cwc : categories) {
        %>
	        <% if( analyzer.categoriesItem != null){ %>
				<option value = "<%=cwc.getId()%>" <%=(analyzer.categoriesItem.equals(Integer.toString(cwc.getId()))) ? "selected" : ""%>><%=cwc.getName()%></option>
			<% } else { %>
				<option value = "<%=cwc.getId()%>"><%=cwc.getName()%></option>
			<% } %>
		<% } %>
		</select>
		<button type="submit">Run Query</button>
	</form>
	
</div>
<div id="table">
<%if (itemTable != null){ %>
	<table>
		<tr>
			<td>     </td>
			<% 
				for(Header col : itemTable.colHeaders){ 
			%>
				<td><b><%= (col.name.length() < 10) ? col.name : col.name.substring(0,9) %></b> (<%= col.total%>)</td>
			<% } %>
		</tr>
		<% 
			int size = itemTable.colHeaders.size()+1;
			for(Header row : itemTable.rowHeaders){ 
		%>
			<tr>
				<td><b><%= row.name %></b> (<%= row.total %>)</td>
				<%
					for(int i=1; i<size; i++){
				%>
					<td><%= itemTable.itemTotals[row.id][i] %></td>
				<% } %>
			</tr>
		<% } %>
	</table>
<% } %>
	<br />
</div>
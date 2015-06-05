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
<div id="precompute">
	<form name="precompute_data" action="analytics" method="post">
		<input type="hidden" name="action" value="precompute">
		<button type="submit">Pre-Compute Tables</button>
	</form>
</div>

<div id="dropdowns">
	<form name="query_form" action="analytics" method="post">
		<label for="categories_dropdown"></label>
		<select name="categories_dropdown" id="category">
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
		<input type="hidden" name="action" value="run">
		<button type="submit">Run</button>
	</form>
	<button id="refresh">Refresh</button>
</div>
<div id="table">
<%if (itemTable != null){ %>
	<table>
		<tr>
			<td class ="blank">     </td>
			<% 
				int count = 1;
				for(Header col : itemTable.colHeaders){ 
			%>
				<td class="col-header" data-pid="<%=col.id%>" data-total="<%=col.total%>" data-colid="<%=count++%>" data-name="<%=col.name%>"><b><%= (col.name.length() < 10) ? col.name : col.name.substring(0,9) %></b> (<%= col.total%>)</td>
			<% } %>
		</tr>
		<% 
			int size = itemTable.colHeaders.size();
			count = 1;
			for(Header row : itemTable.rowHeaders){ 
		%>
			<tr>
				<td class="row-header" data-sid="<%=row.id%>" data-total="<%=row.total%>" data-rowid=<%=count++%> data-name="<%=row.name%>"><b><%= row.name %></b> (<%= row.total %>)</td>
				<%
					for(Header col : itemTable.colHeaders){
						RowCol newRC = new RowCol(row.id, col.id);
						//System.out.println("row: " + row.id + " col: " + col.id);
						RowCol testRC = new RowCol(41, 406);
						//System.out.println("newRC row: " + newRC.state_id);
						//System.out.println("newRC col: " + newRC.prod_id);
						//System.out.println("in hashmap? " + itemTable.items.containsKey(testRC));
						if(itemTable.itemTotals.containsKey(newRC)){
				%>
					<td class="item" data-pid="<%=newRC.prod_id%>" data-sid="<%=newRC.state_id%>"><%= itemTable.itemTotals.get(newRC) %></td>
					<%} else {%>
					<td>0</td>
					<%} %>
				<% } %>
			</tr>
		<% } %>
	</table>
<% } %>
	<br />
</div>
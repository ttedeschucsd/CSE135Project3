	package helpers;

	import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

	public class AnalyticsHelper {
		public String categoriesItem, tempRow, tempCol;
		public String limitColEnd, limitRowEnd, rowoffset, coloffset, action;
		public TableHelper table;
		private Connection conn;
		
		public AnalyticsHelper(HttpServletRequest request){
	        categoriesItem = request.getParameter("categories_dropdown");	//All Categories(0)
	        action = request.getParameter("action");
		}
		
		
		public TableHelper submitQuery(HttpServletRequest request){
			try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
                System.err.println("Internal Server Error. This shouldn't happen.");
                return null;
            }
			if(action != null){
				switch(action){
					case "precompute":
						precomputeData();
					break;
					
					case "run":
						
					break;
				}
			}
//	        try{
//	        	try {
//	                conn = HelperUtils.connect();
//	            } catch (Exception e) {
//	                System.err.println("Internal Server Error. This shouldn't happen.");
//	                return null;
//	            }
//	        	createTempTables();
//	            getRowHeaders();
//	            getColHeaders();
//	            getAllItems();
//	        } catch(Exception e){
//	        	System.err.println("Query failed");
//	        }
	        
	        return table;
		}
		
		private void precomputeData(){
			Statement stmt = null;
			int categoryId = 0;
			
			String row_all_query = "INSERT INTO analytics_row_headers_all(sid, sname, total)"
					+ "("
					+ 	"SELECT s.id, s.name, SUM(sa.price*sa.quantity) "
					+ 	"FROM states as s "
					+ 	"LEFT JOIN users as u ON s.id = u.state "
					+ 	"LEFT JOIN sales as sa ON u.id = sa.uid "
					+ 	"GROUP BY s.id "
					+ 	"ORDER BY sum DESC NULLS LAST"
					+ ")";
			
			String col_query = "INSERT INTO analytics_col_headers(pid, pname, total)"
					+ "("
					+ 	"SELECT p.id, p.name, SUM(sa.price*sa.quantity) "
					+ 	"FROM products as p "
					+ 	"LEFT JOIN sales as sa on sa.pid = p.id "
					+ 	"GROUP BY p.name, p.id "
					+ 	"ORDER BY sum DESC NULLS LAST"
					+ ")";
			String prod_x_state_query = "INSERT INTO analytics_prod_x_state(pid, sid, total)"
					+ "("
					+ 	"SELECT p.id, s.id, SUM(sa.price*sa.quantity) as total "
					+ 	"FROM sales as sa "
					+ 	"LEFT JOIN users as u ON sa.uid = u.id "
					+ 	"LEFT JOIN states as s ON u.state = s.id "
					+ 	"LEFT JOIN products as p ON sa.pid = p.id "
					+ 	"GROUP BY p.id, s.id"
					+ ")";
			
			try {
				stmt = conn.createStatement();
				stmt.execute(row_all_query);
				List<CategoryWithCount> cats = CategoriesHelper.listCategories();
				for (CategoryWithCount cwc : cats) {
					String row_cat_query = "INSERT INTO analytics_row_headers_by_category(sid, sname, cid, total)"
							+ "("
							+ 	"SELECT s.id, s.name, c.id, SUM(sa.price*sa.quantity) "
							+ 	"FROM states as s "
							+ 	"LEFT JOIN users as u ON s.id = u.state "
							+ 	"LEFT JOIN sales as sa ON u.id = sa.uid "
							+ 	"LEFT JOIN products as p on sa.pid = p.id "
							+ 	"LEFT JOIN categories as c ON p.cid = c.id "
							+ 	"WHERE c.id = " + cwc.getId()
							+ 	" GROUP BY s.id, c.id "
							+ 	"ORDER BY sum DESC NULLS LAST"
							+ ")";
					stmt.execute(row_cat_query);
				}
				stmt.execute(col_query);
				stmt.execute(prod_x_state_query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void createTempTables() throws SQLException{
			Statement stmt = null;
			stmt = conn.createStatement();
			String createColHeaders = "CREATE TEMPORARY TABLE col_headers(id SERIAL PRIMARY KEY  NOT NULL, pid integer, pname text, total integer DEFAULT 0)";
			String createRowHeaders = "CREATE TEMPORARY TABLE row_headers(id serial PRIMARY KEY NOT NULL, soruid integer, soruname text, stateid integer, userid integer, total integer);";
			stmt.execute(createColHeaders);
			stmt.execute(createRowHeaders);
		}
		
		private void getRowHeaders() throws SQLException{
			ResultSet rows = null;
			Statement stmt = null;
			stmt = conn.createStatement();
			String insert, select, group, tables, query;
			insert = "INSERT INTO row_headers(soruid, soruname, stateid, userid, total)";
			
			select = "(SELECT s.id, s.name, s.id, u.id, SUM(sa.price*sa.quantity) ";
			tables = "FROM states as s LEFT JOIN users as u ON s.id = u.state LEFT JOIN sales as sa ON u.id = sa.uid ";
			group = " s.id, u.id ";
			query = insert + select + tables + "GROUP BY" + group + "ORDER BY sum DESC NULLS LAST" + ")";
			stmt.execute(query);
			query = "SELECT * FROM row_headers";
			rows = stmt.executeQuery(query);
			while (rows.next()) {
            	Integer id = rows.getInt(1);
                String name = rows.getString(3);
                Integer total = rows.getInt(6);
                table.addRowHeader(new Header(id, name, total));
            }
			return;
		}
			
		private void getColHeaders() throws SQLException{
			ResultSet cols = null;
			Statement stmt = null;
			
			String insert, select, group, query, where;			
			stmt = conn.createStatement();

			insert = "INSERT INTO col_headers(pid, pname, total)";
			select = "(SELECT p.id, p.name, SUM(sa.price*sa.quantity) FROM products as p LEFT JOIN sales as sa on sa.pid = p.id ";
			group = "GROUP BY p.name, p.id ";
			where = "";
			
			if(!categoriesItem.equals("0")){
				select += "LEFT JOIN categories as c on p.cid = c.id ";
				where = "WHERE c.id = " + categoriesItem + " ";
			}
			
			query = insert + select + where + group + "ORDER BY sum DESC NULLS LAST LIMIT 50" + ")";
			stmt.execute(query);
			query = "SELECT * FROM col_headers";
			cols = stmt.executeQuery(query);
			while (cols.next()){
            	Integer id = cols.getInt(1);
            	String name = cols.getString(3);
            	Integer total = cols.getInt(4);
            	table.addColHeader(new Header(id, name, total));
            }
			return;
		}
		
		private void getAllItems() throws SQLException{
			//HAVE TO REMEMBER TO LIMIT number of users/states AND number of products
			ResultSet items = null;
			Statement stmt = null;
			String query;
			
			stmt = conn.createStatement();
			
			query = "SELECT p.id, col.id AS column, row.id AS row, SUM(sa.price*sa.quantity) as total "
					+ "FROM row_headers AS row "
					+ "LEFT JOIN states as s ON row.soruid = s.id "
					+ "LEFT JOIN users as u on row.userid = u.id "
					+ "LEFT JOIN sales as sa ON u.id = sa.uid "
					+ "RIGHT JOIN col_headers AS col ON col.pid = sa.pid "
					+ "LEFT JOIN products as p ON p.id = col.pid "
					+ "GROUP BY p.id, col.id, row.id";
			items = stmt.executeQuery(query);
			while(items.next()){
				table.addItem(items.getInt(3), items.getInt(2), items.getInt(4));
			}
			return;
		}
}

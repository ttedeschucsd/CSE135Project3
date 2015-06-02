	package helpers;

	import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

	public class AnalyticsHelper {
		public String rowsItem, categoriesItem, orderingItem, tempRow, tempCol;
		public String limitColEnd, limitRowEnd, rowoffset, coloffset;
		public TableHelper table;
		private Connection conn;
		
		public AnalyticsHelper(HttpServletRequest request){
			rowsItem = request.getParameter("rows_dropdown");	//Customers(1) or States(2)
	        categoriesItem = request.getParameter("categories_dropdown");	//All Categories(0)
	        orderingItem = request.getParameter("orders_dropdown");	//How the data should be ordered
		}
		
		
		public TableHelper submitQuery(HttpServletRequest request){
	        try{
	        	try {
	                conn = HelperUtils.connect();
	            } catch (Exception e) {
	                System.err.println("Internal Server Error. This shouldn't happen.");
	                return null;
	            }
	        	createTempTables();
	            getRowHeaders();
	            getColHeaders();
	            getAllItems();
	        } catch(Exception e){
	        	System.err.println("Query failed");
	        }
	        
	        return table;
		}
		
		/** QUERIES FOR PRECOMPUTED TABLES**/
		
		
		/** CREATE TABLE analytics_col_headers(id serial NOT NULL, pid integer, pname text, total integer DEFAULT 0, CONSTRAINT analytics_col_headers_pkey PRIMARY KEY (id)) 
		 * 
		 * CREATE TABLE analytics_row_headers( id serial NOT NULL, sid integer, sname text, total integer, CONSTRAINT analytics_row_headers_pkey PRIMARY KEY (id))
		 * 
		 * CREATE TABLE analytics_prod_x_state(id serial NOT NULL, pid integer, sid integer, total integer, CONSTRAINT analytics_prod_x_state_pkey PRIMARY KEY (id))
		 **/
		
		/**END QUERIES**/
		
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

package helpers;

import java.util.*;

public class TableHelper {
	public ArrayList<Header> rowHeaders;
	public ArrayList<Header> colHeaders;
	//public int [][] itemTotals;		//[row][column]
	public HashMap<RowCol, Integer> itemTotals;
	
	public TableHelper(){
		rowHeaders = new ArrayList<>();
		colHeaders = new ArrayList<>();
		//itemTotals = new int[51][51];
		itemTotals = new HashMap<RowCol, Integer>();
	}
	
	public void addRowHeader(Header header){
		rowHeaders.add(header);
	}
	
	public void addColHeader(Header header){
		colHeaders.add(header);
	}
	
	public void addItem(int s, int p, int total){
		//create a new RowCol key to put into hashmap itemTotals
		RowCol newRC = new RowCol(s, p);
		itemTotals.put(newRC, total);
		//System.out.println("row: " + s + "col: " + p + " total: " + items.get(newRC));
		//System.out.println("newRC row: " + s + " col: " + p + " total: " + total);
		//System.out.println("listing totals: " + itemTotals.get(newRC));
	}
	
	/*public void addTableRow(Map<Integer, Integer> input, int rowIndex){
		for(int i=0; i<colHeaders.size(); i++){
			int prodId = colHeaders.get(i).id;
			if(input.containsKey(prodId)){
				int totalVal = input.get(prodId);
				itemTotals[rowIndex][i] = totalVal;
			}
		}
	}
	
	public void addItem(int row, int col, int total){
		itemTotals[row][col] = total;
	}*/
}

package helpers;

import java.util.*;

public class RowCol {
	public int prod_id;
	public int state_id;
	
	public RowCol(int s, int p){
		this.prod_id = p;
		this.state_id = s;
	}

	@Override
	public int hashCode() {
		return (prod_id + state_id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RowCol){
			return prod_id == (((RowCol) obj).prod_id) && state_id == (((RowCol) obj).state_id);
		}
		return false;
	}
	
	
}
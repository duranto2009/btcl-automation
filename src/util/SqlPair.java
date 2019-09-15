package util;

import java.util.List;

public class SqlPair {
	@Override
	public String toString() {
		return "SqlPair [sql=" + sql +", object list = "+objectList+ "]";
	}
	public String sql;
	public List<Object> objectList;
	
	
}

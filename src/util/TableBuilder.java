package util;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;


public class TableBuilder {
	private String collate = "utf8_general_ci";
	private String engine = "InnoDB";
	private static Map<Class<?>, String> mapOfDBTypeToJAVAType = new HashMap<Class<?>, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(Long.class, "BIGINT(20) NULL");
			put(Long.TYPE, "BIGINT(20) NOT NULL");
			put(Integer.class, "INT(11) NULL");
			put(Integer.TYPE, "INT(11) NOT NULL");
			put(Boolean.class, "TINYINT(1) NULL");
			put(Boolean.TYPE, "TINYINT(1) NOT NULL");
			put(String.class, "TEXT NOT NULL");
			put(Double.class, "DOUBLE NULL");
			put(Double.TYPE, "DOUBLE NOT NULL");
		}
	};
	


	public TableBuilder() {
	}
	
	public String getAlterColumnAddSQLCommand(Field field, String tableName) throws Exception{
		String columnName = field.getAnnotation(ColumnName.class).value();
		String result = "ALTER TABLE " + tableName + " ADD " + columnName + " " + mapOfDBTypeToJAVAType.get(field.getType());
		return result;
	}
	public String getAlterColumnModifySQLCommand(Field field, String tableName) throws Exception{
		String columnName = field.getAnnotation(ColumnName.class).value();
		String result = "ALTER TABLE " + tableName + " MODIFY COLUMN " + columnName + " " + mapOfDBTypeToJAVAType.get(field.getType());
		return result;
	}
	public static boolean isValidClass(Class<?> classObject) throws Exception{
		Set <String> columnNameSet = new HashSet<>();
		String tableName = classObject.getAnnotation(TableName.class).value();
		boolean result = true;
		if(tableName == null || tableName.isEmpty() ){
			return false;
		}else {
			Field [] fields = classObject.getDeclaredFields();
			for(Field field: fields){
				

				field.setAccessible(true);
				if(field.getAnnotation(PrimaryKey.class)!=null && field.getAnnotation(ColumnName.class)==null){
					return false;
				}
				
				String columnName = "";
				if(field.getAnnotation(ColumnName.class) != null){
					columnName = field.getAnnotation(ColumnName.class).value().trim();
					if(columnName == null || columnName.isEmpty()){
						return false;
					}
					boolean isInserted = columnNameSet.add(columnName);
					if(!isInserted ){
						return false;
					}
				}

				
			}
		}
		
		return result;
	}
	
	public String getSQLOfCreateTable(Class<?> classObject) throws Exception {
		StringBuilder tableBuilder = new StringBuilder();
		TableName tableName = (TableName)classObject.getAnnotation(TableName.class);
	
		if(tableName == null){
			throw new Exception("No table name annotation found in the class " + classObject);
		}else {
			tableBuilder.append(getFirstLine(tableName));
			tableBuilder.append(getBody(classObject));
			tableBuilder.append(getLastLine());
			
		}
		
		String sql = tableBuilder.toString();
		return sql;
	}
	private StringBuilder getBody(Class<?> classObject) throws Exception {
		Field[] fields = classObject.getDeclaredFields();
		String columnName = "";
		String pkColumnName = "";
		Field primarykeyField = null;
		
		StringBuilder tableBuilder = new StringBuilder();
		for(Field field: fields){
			field.setAccessible(true);
			if(field.getAnnotation(ColumnName.class) != null){
				columnName = field.getAnnotation(ColumnName.class).value();
				tableBuilder.append("`").append(columnName).append("` ").append(mapOfDBTypeToJAVAType.get(field.getType())).append(",");
				
			}
			
			if(field.getAnnotation(PrimaryKey.class) != null){
				if(primarykeyField != null){
					throw new Exception("More than one primary key found");
				}
				primarykeyField = field;
				pkColumnName = field.getAnnotation(ColumnName.class).value();
			}
			
			
		}
		if(primarykeyField == null){
			throw new Exception("No primary key annotation found");
		}
		tableBuilder.append("PRIMARY KEY (`").append(pkColumnName).append("`)");
		return tableBuilder;
	}
		
	private StringBuilder getLastLine() {
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append(")  COLLATE=`").append(collate).append("` ENGINE=").append(engine); 
		return tableBuilder;
	}
	private StringBuilder getFirstLine(TableName tableName) {
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append("CREATE TABLE IF NOT EXISTS `").append(tableName.value()).append("` (");
		return tableBuilder;
	}
	public static void main(String []args) throws Exception{
		
		DatabaseTableStructureChecker databaseTableStructureChecker = (DatabaseTableStructureChecker) ServiceDAOFactory.getService(DatabaseTableStructureChecker.class);
		databaseTableStructureChecker.check();
		System.out.println("done");
		
	}
}


package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import annotation.TableName;
import annotation.Transactional;

class DatabaseColumnProperty{
	boolean isNullAble;
	String datatype;
	
	public DatabaseColumnProperty(boolean isNullAble,String datatype) {
		this.isNullAble = isNullAble;
		this.datatype = datatype;
	}

	@Override
	public String toString() {
		return "DatabaseColumnProperty [isNullAble=" + isNullAble + ", datatype=" + datatype + "]";
	}
	
}

public class DatabaseTableStructureChecker {
	private static Map<Class<?>, String> mapOfDBTypeToJAVAType = new HashMap<Class<?>, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(Long.class, "BIGINT");
			put(Long.TYPE, "BIGINT");
			put(Integer.class, "INT");
			put(Integer.TYPE, "INT");
			put(Boolean.class, "TINYINT");
			put(Boolean.TYPE, "TINYINT");
			put(String.class, "TEXT");
			put(Double.class, "DOUBLE");
			put(Double.TYPE, "DOUBLE");
		}
	};
	public static Map<String,Map<String,DatabaseColumnProperty>> mapOfDatabaseColumnPropertyToColumnNameToTableName 
									= new HashMap<>();
	
	public void loadInformationSchema() throws Exception{
		String sql = "select table_name,column_name,is_nullable,data_type from   "
				+ "information_schema.`COLUMNS` where table_schema=(select Database())";
		
		ResultSet rs = DatabaseConnectionFactory.getCurrentDatabaseConnection()
						.getNewStatement().executeQuery(sql);
		
		while(rs.next()){
			String tableName = rs.getString("table_name");
			String columnName = rs.getString("column_name");
			boolean isNullAble = (rs.getString("is_nullable").equals("NO")?false:true);
			String datatype = rs.getString("data_type");
			
			
			if(!mapOfDatabaseColumnPropertyToColumnNameToTableName.containsKey(tableName)){
				mapOfDatabaseColumnPropertyToColumnNameToTableName.put(tableName, new HashMap());
			}
			
			mapOfDatabaseColumnPropertyToColumnNameToTableName.get(tableName).put(columnName, new DatabaseColumnProperty(isNullAble, datatype));
		}
		
		
	}
	
	public DatabaseColumnProperty getDatabaseColumnProperty(String tableName
			,String columnName) throws Exception{
		if(!mapOfDatabaseColumnPropertyToColumnNameToTableName.containsKey(tableName)){
			return null;
			
			// insert table
		}
		if(!mapOfDatabaseColumnPropertyToColumnNameToTableName.get(tableName).containsKey(columnName)){
			return null;
			// alter add
		}
		return mapOfDatabaseColumnPropertyToColumnNameToTableName.get(tableName).get(columnName);
	}
	
	
	
	
	private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
	    List<Class> classes = new ArrayList<Class>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file,   ( packageName.trim().isEmpty()?"":packageName + '.')  + file.getName()));
	        } else if (file.getName().endsWith(".class") && !file.getName().contains("Service")
	        		&& !file.getName().contains("Action") && !file.getName().contains("DAO") && !file.getName().contains("Repository") ) {
	        	String className = (packageName.trim().isEmpty()?"":packageName + '.') + file.getName().substring(0, file.getName().length() - 6);
	        	classes.add(Class.forName(className));
	        }
	    }
	    return classes;
	}
	
	private Class[] getClasses(String packageName)
	        throws ClassNotFoundException, IOException {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        if(!resource.toString().contains("WEB-INF")){
	        	dirs.add(new File(resource.getFile()));
	        }
	    }
	    ArrayList<Class> classes = new ArrayList<Class>();
	    for (File directory : dirs) {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes.toArray(new Class[classes.size()]);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Class> getTableClasses() throws Exception{
		List<Class> tableClaslses = new ArrayList<>();
		Class[] classes = getClasses("");
		for(Class classObject: classes){
			if(classObject.getAnnotation(TableName.class)!=null){
				tableClaslses.add(classObject);
			}
		}
		return tableClaslses;
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public  void check() throws Exception{
		loadInformationSchema();
		StringBuilder stringBuilder = new StringBuilder("");
		TableBuilder tableBuilder = new TableBuilder();
		for(Class classObject:getTableClasses()){
			
			if(!TableBuilder.isValidClass(classObject)){
				System.out.println("invalid class "+classObject);
				continue;
			}
			
			String tableName = ((TableName)classObject.getAnnotation(TableName.class)).value();
			if(!mapOfDatabaseColumnPropertyToColumnNameToTableName.containsKey(tableName)){
				try{
					String sql = tableBuilder.getSQLOfCreateTable(classObject);
					stringBuilder.append(sql).append(";\n");
				}catch(Exception ex){
					ex.printStackTrace();
				}
				continue;
			}
			/*
			for(Field field: classObject.getDeclaredFields()){
				field.setAccessible(true);
				if(field.getAnnotation(ColumnName.class)!=null){
					String columName = ((ColumnName)field.getAnnotation(ColumnName.class)).value();
					if(!mapOfDatabaseColumnPropertyToColumnNameToTableName.get(tableName).containsKey(columName)){
						// alter add column
						try{
							String sql = tableBuilder.getAlterColumnAddSQLCommand(field, tableName);
							stringBuilder.append(sql).append(";\n");
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}else{
						// check datatype and alter modify if necessarALTER TABLE at_union_distance ALTER COLUMN unionID BIGINT(20) NOT NULLy
						DatabaseColumnProperty databaseColumnProperty = mapOfDatabaseColumnPropertyToColumnNameToTableName.get(tableName).get(columName);
						if((databaseColumnProperty.isNullAble == field.getType().isPrimitive() )&& !field.getType().equals(String.class)
								|| !databaseColumnProperty.datatype.toUpperCase().equals(mapOfDBTypeToJAVAType.get(field.getType()))
								)
						{
							
							String sqlForModify = tableBuilder.getAlterColumnModifySQLCommand(field, tableName);
							System.out.println(sqlForModify);
							stringBuilder.append(sqlForModify).append(";\n");
							try{
								String sql = tableBuilder.getAlterColumnModifySQLCommand(field, tableName);
							//	stringBuilder.append(sql).append(";\n");
							}catch(Exception ex){
								ex.printStackTrace();
							}
							
						}
						
					}
				}

			
			}
			*/

			
		}
		
		
		
		String vbSequencerSql = "CREATE TABLE IF NOT EXISTS `vbSequencer` "
				+ "(	`table_name` VARCHAR(50) NOT NULL,	"
				+ " `next_id` BIGINT(20) NULL DEFAULT NULL,	"
				+ " `table_LastModificationTime` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',"
				+ "	UNIQUE INDEX `table_name` (`table_name`))"
				+ " COLLATE='utf8_general_ci' ENGINE=InnoDB";
		
		stringBuilder.append(vbSequencerSql).append(";");
		
		String sql = "insert into vbSequencer select "
				+ " table_name,0 next_id,0 lastModificationTime "
				+ " from information_schema.`TABLES` where "
				+ " table_schema=(select Database()) "
				+ " and table_name not in "
				+ " (select table_name from vbSequencer)";
		stringBuilder.append(sql);
		
		Statement stm =   DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewStatement();
		
		for(String singleSql: stringBuilder.toString().split(";",-1)){
			System.out.println(singleSql);
			stm.addBatch(singleSql);
		}
		
		stm.executeBatch();
	}
	 
	public static void updateVbSequencer() throws Exception{
		String sql = "insert into vbSequencer select "
				+ " table_name,0 next_id,0 lastModificationTime "
				+ " from information_schema.`TABLES` where "
				+ " table_schema=(select Database()) "
				+ " and table_name not in "
				+ " (select table_name from vbSequencer)";
		
		DatabaseConnectionFactory.getCurrentDatabaseConnection()
				.getNewStatement().execute(sql);
	}
}

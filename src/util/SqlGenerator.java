package util;
import static common.StringUtils.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.math.raw.Mod;
import org.jdom.IllegalTargetException;

import repository.RepositoryManager;
import validator.DTOValidator;
import annotation.*;
import common.StringUtils;
import connection.DatabaseConnection;



public class SqlGenerator {
	static Logger logger = Logger.getLogger(SqlGenerator.class);
	
	
	static public<T>  List<T> getAllObjectForRepository(Class<T> classObject,DatabaseConnection databaseConnection, boolean isFirstReload,String conditionString) throws Exception{
		String conditionStringForRepository = getConditionStringForRepository(classObject, databaseConnection, isFirstReload) ; 
		conditionString = (conditionStringForRepository.isEmpty()?" where ": conditionStringForRepository+" and ")+ conditionString;
		Class<? extends T> subClassObject = ImmutableClassFactory.getImmutableClass(classObject);
		return getAllObjectList(classObject,subClassObject, databaseConnection, conditionString);
	}
	
	static  public<T> List<T> getAllObjectForRepository(Class<T> classObject,DatabaseConnection databaseConnection, boolean isFirstReload) throws Exception{
		
		String conditionString = getConditionStringForRepository(classObject, databaseConnection, isFirstReload);
		Class<? extends T> subClassObject = ImmutableClassFactory.getImmutableClass(classObject);
		return getAllObjectList(classObject,subClassObject, databaseConnection, conditionString);
	}
	

	static public ResultSet getAllResultSetForRepository(Class<?> classObject,DatabaseConnection databaseConnection, boolean isFirstReload) throws Exception{
		String conditionString = getConditionStringForRepository(classObject, databaseConnection, isFirstReload);
		return getAllResultSetOfTable(classObject, databaseConnection, conditionString);
	}            
	
	static private String getConditionStringForRepository(Class<?> classObject,DatabaseConnection databaseConnection,boolean isFirstReload) throws Exception{
		String conditionString = isFirstReload?"":" where "+getColumnName(classObject, "lastModificationTime")+" >= "+RepositoryManager.lastModifyTime;//+"-2000000 and "+getColumnName(classObject, "isDeleted")+"=0";
		return conditionString;
	}
	
	/**
	 * This method soft delete a row in database.
	 * @author Niaz
	 * @param classObject
	 * @param ID
	 * @param lastModificationTime
	 * @param databaseConnection
	 * @return
	 * @throws Exception
	 */
	static public int deleteEntityByID(Class<?> classObject,  long ID,long lastModificationTime, DatabaseConnection databaseConnection) throws Exception{


		int numberOfAffectedRow;
		String sql = "update "+getTableName(classObject)+" set "+getColumnName(classObject, "isDeleted")+"=1,"+getLastModificationTimeColumnName(classObject)+"="+lastModificationTime
				+" where "+getPrimaryKeyColumnName(classObject)+" = "+ID;		
		Statement stmt = databaseConnection.getNewStatement();
		numberOfAffectedRow = stmt.executeUpdate(sql);
		stmt.close();
		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
		return numberOfAffectedRow;
	} 
	
	public static int runUpdateQuery( String sql, DatabaseConnection databaseConnection ) throws SQLException{
		logger.debug("Update sql: " + sql);
		Statement statement = databaseConnection.getNewStatement();
		int noOfAffectedRows = statement.executeUpdate( sql );				
		return noOfAffectedRows;
	}
	
	/**
	 * This method removes a row from database.
	 * @author Niaz 
	 * @param classObject
	 * @param ID
	 * @param databaseConnection
	 * @return
	 * @throws Exception
	 */
	static public int deleteHardEntityByID(Class<?> classObject,  long ID, DatabaseConnection databaseConnection) throws Exception{

		int numberOfAffectedRow;
		String sql = "delete from "+getTableName(classObject)+
				"  where "+getPrimaryKeyColumnName(classObject)+" = "+ID;		
		Statement stmt = databaseConnection.getNewStatement();
		numberOfAffectedRow = stmt.executeUpdate(sql);
		stmt.close();
		return numberOfAffectedRow;
		
	}
	
	static private ColumnNameValuePair getColumnNameValuePair(Field field,Object object) throws Exception{
//		field.setAccessible(true);
//		String columnName = ((ColumnName)field.getAnnotation(ColumnName.class)).value();
//		Object value = field.get(object);
//		return new ColumnNameValuePair(columnName, value);
		return ModifiedSqlGenerator.getColumnNameValuePair(field, object);
	}
	
	public static<T> T getObjectByID(Class<T> classObject,long ID,DatabaseConnection databaseConnection) throws Exception{
		
		List<T> resultList = getAllObjectList(classObject, databaseConnection,
				" where "+getPrimaryKeyColumnName(classObject)+" = "+ID);
		
		return resultList.size()>0? resultList.get(0):null;
		
	}
	
	
	/*
	 * This method lock the corresponding row.
	 * 
	 * */
	public static<T> T getObjectByIDForUpdate(Class<T> classObject,long ID,DatabaseConnection databaseConnection) throws Exception{
		
		List<T> resultList = getAllObjectList(classObject, databaseConnection,
				" where "+getPrimaryKeyColumnName(classObject)+" = "+ID+" FOR UPDATE");
		
		return resultList.size()>0? resultList.get(0):null;
		
	}
	
	
	
	
	public static<T> T getObjectByID(Class<T> classObject,Class<? extends T> subClassObject,long ID,DatabaseConnection databaseConnection) throws Exception{
		
		List<T> resultList = getAllObjectList(classObject,subClassObject, databaseConnection,
				" where "+getPrimaryKeyColumnName(classObject)+" = "+ID);
		
		return resultList.size()>0? resultList.get(0):null;
		
	}
	
	public static<T> List<T> getObjectListByIDList(Class<T> classObject,Class<? extends T> subClassObject,Collection<Long> IDList,DatabaseConnection databaseConnection,Object... conditionString) throws Exception{

		if(conditionString.length>1){
			throw new Exception("Invalid method call");
		}
		List<T> objectList = new ArrayList<>();
		if(IDList.isEmpty()){
			return objectList;
		}
		String primaryKeyColumnName = getPrimaryKeyColumnName(classObject);
		StringBuilder conditionBuilder = new StringBuilder(" where ");
		conditionBuilder.append(" ").append(primaryKeyColumnName).append(" IN (");
		
		int i = 0;
		for(Long id: IDList)
		{
			conditionBuilder.append(id);
			i++;
			if(i < IDList.size())
			{
				conditionBuilder.append(",");
			}
		}
		
		conditionBuilder.append(")");
		if(conditionString.length==1){
			conditionBuilder.append("  and "+conditionString[0]);
		}
		return getAllObjectList(classObject,subClassObject, databaseConnection, conditionBuilder.toString());

	}
	

	public static<T> List<T> getObjectListByIDList(Class<T> classObject,Collection<Long> IDList,DatabaseConnection databaseConnection,Object... conditionString) throws Exception{
		return getObjectListByIDList(classObject, classObject, IDList, databaseConnection, conditionString);
	}
	
	public static String getForeignKeyColumnName(Class<?> classObject) throws Exception{
		if(!isEntity(classObject)){
			throw new Exception("Not an document type "+classObject);
		}
		ForeignKeyName foreignKeyName = (ForeignKeyName)classObject.getAnnotation(ForeignKeyName.class);
		if(foreignKeyName == null){
			throw new Exception("No foreign key Annotation found in class "+classObject);
		}
		return foreignKeyName.value();
	}
	
	public static<T> T getObjectFullyPopulatedByID(Class<T> classObject,DatabaseConnection databaseConnection,long ID ) throws Exception{
		List<T> list = getAllObjectListFullyPopulated(classObject, databaseConnection, " where "+getPrimaryKeyColumnName(classObject)+" = "+ID);
		return list.isEmpty()?null:list.get(0);
	}
	public static<T> List<T> getObjectFullyPopulatedByString(Class<T> classObject,DatabaseConnection databaseConnection,Object[] values, String[] columnNames ,Object... conditionString) throws Exception{
		
		if(values.length!=columnNames.length){
			throw new Exception("Invalid method call. Column names and values length should be same");
		}
		
		StringBuilder conditionBuilder = new StringBuilder(" where ");
		boolean conditionAdded = false;
		for(int i =0;i<values.length;i++){
			Object value = values[i];
			String columnName = columnNames[i];
			
			Field field = classObject.getDeclaredField(columnName);
			field.setAccessible(true);
			if(conditionAdded){
				conditionBuilder.append(" and ");
			}
			conditionAdded = true;
			if(field.getType().equals(String.class)){
				conditionBuilder.append(getColumnName(classObject, columnName)).append(" like '%").append(value).append("%'");
			}else{
				conditionBuilder.append(getColumnName(classObject, columnName)).append(" = ").append(value);
			}
		}
		if(conditionString.length>0){
			conditionBuilder.append(conditionString[0]);
		}
		
		List<T> list = getAllObjectListFullyPopulated(classObject, databaseConnection, conditionBuilder.toString());
		return list;
	}
	public static <T> List<T> getAllObjectListFullyPopulated(Class<T> classObject,DatabaseConnection databaseConnection,Object ...conditionString ) throws Exception{
		if(conditionString.length>1){
			throw new Exception("Invalid number of arguments .");
		}
		
		List<T> returnList = null;
		if(conditionString.length == 1){
			returnList = getAllObjectList(classObject, databaseConnection, conditionString[0]);
		}else{
			returnList = getAllObjectList(classObject, databaseConnection);
		}
		if(superClassExists(classObject) && !returnList.isEmpty()){
			
			Field primaryFieldOfSuperClass = getPrimaryField(classObject.getSuperclass());
			primaryFieldOfSuperClass.setAccessible(true);
			StringBuilder conditionStringBuilder = new StringBuilder(" where "+getColumnName(classObject.getSuperclass(), primaryFieldOfSuperClass.getName())+" IN ( ");
			for(int i=0;i<returnList.size();i++){
				Object element = returnList.get(i);
				long primaryKeyValue = new Long(primaryFieldOfSuperClass.get(element).toString());
				conditionStringBuilder.append(primaryKeyValue);
				if(i!=returnList.size()-1){
					conditionStringBuilder.append(",");
				}
			}
			conditionStringBuilder.append(" )");
			
			List<? super T> superObjectList = getAllObjectListFullyPopulated(classObject.getSuperclass(), databaseConnection, conditionStringBuilder.toString());
			
			Map<Long,Object> superObjectMapToPrimaryKey = new HashMap<Long,Object>();
			for(Object superObject: superObjectList){
				Field superObjectPrimaryField = getPrimaryField(superObject.getClass());
				superObjectPrimaryField.setAccessible(true);
				Object superObjectPrimaryFieldValue = superObjectPrimaryField.get(superObject);
				long primaryKey = new Long(superObjectPrimaryFieldValue.toString());
				superObjectMapToPrimaryKey.put(primaryKey, superObject);
			}
			
			for(Object returnObject:returnList){
				Long foreignKeyValue = new Long(getForeignKeyColumnNameValuePair(returnObject, classObject).value.toString());
				populateObjectFromOtherObject(returnObject, classObject.getSuperclass(), superObjectMapToPrimaryKey.get(foreignKeyValue), classObject.getSuperclass());
			}
			
			
		}
		return returnList;
	}
	
	
	public static<T> List<T> getAllUndeletedObjectList(Class<T> classObject,DatabaseConnection databaseConnection) throws Exception{
		String conditionString = (  hasColumn(classObject, "isDeleted", false) ? " where "+ getColumnName(classObject, "isDeleted")+" = 0":"");
		return getAllObjectList(classObject, databaseConnection, conditionString);
	}
	public static<T> List<T> getAllUndeletedObjectList(Class<T> classObject,DatabaseConnection databaseConnection,String conditionString) throws Exception{
		if(isBlank(conditionString)){
			return getAllUndeletedObjectList(classObject, databaseConnection);
		}else{
			if(!hasColumn(classObject, "isDeleted", false)){
				return getAllObjectList(classObject, databaseConnection, conditionString); 
			}else{
				String conditionStringForDeletionCheck = " WHERE "+getColumnName(classObject, "isDeleted")
														+" = 0 AND ";
				conditionString = conditionString.replace(" where ", " WHERE ");
				if(conditionString.trim().startsWith("WHERE")){
					conditionString = conditionString.replace(" WHERE ", conditionStringForDeletionCheck);
				}else{
					conditionString = conditionStringForDeletionCheck+conditionString;
				}
				
				return getAllObjectList(classObject, databaseConnection, conditionString);
			} 
		}
	}
	
	public static<T> List<T> getAllObjectList(Class<T> classObject,DatabaseConnection databaseConnection,Object ...conditionString) throws Exception{
		return getAllObjectList(classObject, classObject, databaseConnection, conditionString);
	}
	
	public static<T> List<T> getAllObjectList(Class<T> classObject,Class<? extends T> subClassObject, DatabaseConnection databaseConnection,Object ...conditionString) throws Exception{
	
		
		ResultSet rs = getAllResultSetOfTable(classObject, databaseConnection, conditionString);
		
		return getObjectListByResultSet(classObject,subClassObject,rs);
	}
	
	public static<T> List<T> getObjectListByResultSet(Class<T> classObject,ResultSet rs) throws Exception{
		return getObjectListByResultSet(classObject,classObject,rs);	
	}
	
	public static<T> List<T> getObjectListByResultSet(Class<T> classObject,Class<? extends T> subClassObject,ResultSet rs) throws Exception{
		

		List<T> returnObjectList = new ArrayList<T>();
		
		
		while(rs.next()){
			T object = subClassObject.newInstance();
			try{
				populateObjectFromDB(object, rs,classObject);
				returnObjectList.add(object);
			}catch(Exception ex){
				logger.debug("fatal:",ex);
				logger.debug("primary key = "+rs.getObject(getPrimaryKeyColumnName(classObject)));
				
			}
		}
		return returnObjectList;
	}
	
	
	
	public static List<Long> getAllIDList(Class<?> classObject,DatabaseConnection databaseConnection,Object ...conditionString) throws Exception{
		List<Long> returnIDList = new ArrayList<Long>();
		ResultSet rs = getIDResultSetOfTable(classObject, databaseConnection, conditionString);
		
		
		while(rs.next()){
			Long ID = rs.getLong(1);
			returnIDList.add(ID);
		}
		return returnIDList;
	}
	
	public static ResultSet getIDResultSetOfTable(Class<?> classObject,DatabaseConnection databaseConnection,Object ...conditionString) throws Exception{
		
		List<Field> columnFieldList = new ArrayList<Field>();
		columnFieldList.add(getPrimaryField(classObject));
		ResultSet rs = getResultSetFromColumnFieldList(classObject, columnFieldList, databaseConnection, conditionString);	
		return rs;
	}
	
	public static ResultSet getAllResultSetOfTable(Class<?> classObject,DatabaseConnection databaseConnection,Object ...conditionString) throws Exception{
		
		List<String> columnNameList = getColumnNameListForSelectQuery(classObject);
		ResultSet rs = getResultSetFromColumnNameList(classObject, columnNameList, databaseConnection, conditionString);
		return rs;
	}
	
	
	private static ResultSet getResultSetFromColumnNameList(Class<?> classObject,List<String> columnNameList,DatabaseConnection databaseConnection,Object ...conditionString ) throws Exception{
		StringBuilder stringBuilder = new StringBuilder("select ");
		for(int i = 0;i<columnNameList.size();i++){
			if(i!=0){
				stringBuilder.append(",");
			}
			stringBuilder.append(columnNameList.get(i));
		}
		stringBuilder.append(" from ").append(getTableName(classObject));
		
		
		if(conditionString.length > 0){
			stringBuilder.append(conditionString[0]);
		}
		
		
		
		String sql = stringBuilder.toString();
		logger.debug("sql: "+sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);

		if(conditionString.length>1){
			for(int i=1;i<conditionString.length;i++){
				ps.setObject(i, conditionString[i]);
			}
		}
		logger.info(ps);
		ResultSet rs = ps.executeQuery();
		
		return rs;
	}
	
	
	private static ResultSet getResultSetFromColumnFieldList(Class<?> classObject,List<Field> fieldList,DatabaseConnection databaseConnection,Object ...conditionString ) throws Exception{
		StringBuilder stringBuilder = new StringBuilder("select ");
		for(int i = 0;i<fieldList.size();i++){
			if(i!=0){
				stringBuilder.append(",");
			}
			Field field = fieldList.get(i);
			field.setAccessible(true);
			String columnName = "";
			try{
				columnName = getColumnName(classObject, field.getName());
			}catch(Exception ex){
				columnName = getColumnName(classObject.getSuperclass(), field.getName());
			}
			if(!columnName.equals("")){
				stringBuilder.append(columnName);
			}
		}
		stringBuilder.append(" from ").append(getTableName(classObject));
		if(conditionString.length > 0){
			stringBuilder.append(conditionString[0]);
		}
		
		
		
		String sql = stringBuilder.toString();
		logger.debug("sql: "+sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		if(conditionString.length>1){
			for(int i=1;i<conditionString.length;i++){
				ps.setObject(i, conditionString[i]);
			}
		}
		
		ResultSet rs = ps.executeQuery();
		return rs;
	}
	static private List<String> getColumnNameListForSelectQuery(Class<?> classObject) throws Exception{
		List<String> columNameList = new ArrayList<String>();
		Field primaryField = getPrimaryField(classObject);
		for(Field field: classObject.getDeclaredFields()){
			if(!field.equals(primaryField) && field.getAnnotation(ColumnName.class)!=null){
				ColumnName columnName = (ColumnName)field.getAnnotation(ColumnName.class);
				columNameList.add(columnName.value());
			}
		}
		if(classObject.getAnnotation(ForeignKeyName.class)!=null){
			String columName = ((ForeignKeyName)classObject.getAnnotation(ForeignKeyName.class)).value();
			columNameList.add(columName);
		}
		columNameList.add(((ColumnName)primaryField.getAnnotation(ColumnName.class)).value());
		return columNameList;
	}
	static Field getForeignKeyField(Class<?> classObject) throws Exception{
		return getPrimaryField(classObject.getSuperclass());
	}
	
	/*
	 *Primary key will be at the last of the list
	 * */	
	
	
	@SuppressWarnings("unchecked")
	static private List<ColumnNameValuePair> getColumnNameValuePairList(Object object,Class<?> classObject
			,boolean discardNonEditableColumns,Object... objects) throws Exception{
		List<ColumnNameValuePair> columnNameValuePairs = new ArrayList<>();
		
		if(objects.length>1){
			throw new Exception("Invalid method call. Number of optional argument should be less than 2");
		}
		boolean isCustomEditEnabled = (objects.length == 1);
		
		Set<String> columnNameSet = null;
		if(objects.length == 1){
			columnNameSet=(Set<String>)(objects[0]);
		}
		
		
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		for(Field field: classObject.getDeclaredFields()){
			field.setAccessible(true);
			if(!isCustomEditEnabled || isCustomEditEnabled && columnNameSet.contains(field.getName())){
				if(!primaryField.equals(field) && field.getAnnotation(ColumnName.class)!=null){
					if(!discardNonEditableColumns || ((ColumnName)field.getAnnotation(ColumnName.class)).editable()){
						columnNameValuePairs.add(getColumnNameValuePair(field, object));
					}
				}
			}
		}
		
		if(superClassExists(classObject)){
			Field foreignKeyField = getForeignKeyField(classObject);
			if(!isCustomEditEnabled||isCustomEditEnabled && columnNameSet.contains(foreignKeyField.getName())){
				if(!discardNonEditableColumns || ((ForeignKeyName)classObject.getAnnotation(ForeignKeyName.class)).editable()){
					
					ColumnNameValuePair foreignKeyColumnNameValuePair = getForeignKeyColumnNameValuePair(object, classObject);
					if(((ForeignKeyName)classObject.getAnnotation(ForeignKeyName.class)).editable()){
						columnNameValuePairs.add(foreignKeyColumnNameValuePair);
					}
				}
			}
		}
		columnNameValuePairs.add(getColumnNameValuePair(primaryField, object));
		return columnNameValuePairs;
	}
	
	/*
	 *Primary key will be at the last of the list
	 * */	
	static private List<ColumnNameValuePair> getColumnNameValuePairList(Object object, Class<?> classObject) throws Exception{
		List<ColumnNameValuePair> columnNameValuePairs = new ArrayList<>();
		if(superClassExists(classObject)){
			ColumnNameValuePair columnNameValuePair = getForeignKeyColumnNameValuePair(object, classObject);
			columnNameValuePairs.add(columnNameValuePair);
		}
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		for(Field field: classObject.getDeclaredFields()){
			field.setAccessible(true);
			if(!field.equals(primaryField)  && field.getAnnotation(ColumnName.class) != null && field.getAnnotation(ColumnName.class) instanceof ColumnName  ){
				ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(field, object);
				columnNameValuePairs.add(columnNameValuePair);				
			}
		}

		ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(primaryField, object);
		columnNameValuePairs.add(columnNameValuePair);	
		return columnNameValuePairs;
	}
	static public String getTableName(Class<?> classObject) throws Exception{
		TableName tableNameAnnotation = ((TableName)classObject.getAnnotation(TableName.class));
		if(tableNameAnnotation instanceof TableName){
			return tableNameAnnotation.value();
		}else{
			throw new Exception("Table Name Annotation Not defined");
		}
	}
	static public String getColumnName(Class<?> classObject,String propertyName) throws Exception{
		if(hasColumn(classObject, propertyName, true) && !hasColumn(classObject, propertyName, false)){
			ForeignKeyName foreignKeyName = (ForeignKeyName)classObject.getDeclaredAnnotation(ForeignKeyName.class);
			return foreignKeyName.value();
		}
		Field field = classObject.getDeclaredField(propertyName);
		field.setAccessible(true);
		return ((ColumnName)field.getAnnotation(ColumnName.class)).value();
	} 
	static public boolean isDeletedColumnName(Object object) throws Exception{
		Field field = object.getClass().getDeclaredField("isDeleted");
		field.setAccessible(true);
		boolean isDeleted = field.getBoolean(object);
		return isDeleted;
	}
	static public String getLastModificationTimeColumnName(Class<?> classObject) throws Exception{
		return getColumnName(classObject, "lastModificationTime");
	}
	static public int updateEntity(Object object,Class<?> classObject,DatabaseConnection databaseConnection,boolean allowNestedUpdate,boolean applyOptimisticLocking,
			Object ...currentTimeOption) throws Exception{
		DTOValidator.validate(object, classObject);
		if(!isEntity(classObject)){
			throw new Exception("Not an document type.");
		}
		long lastModificationTime = getLastModificationTime(object, classObject);
		
		long currentTime = (currentTimeOption.length==1?(Long)currentTimeOption[0]:System.currentTimeMillis());
		
		if(applyOptimisticLocking){
			// setLastModificationTime
			Field lastModificationTimeField = classObject.getDeclaredField("lastModificationTime");
			lastModificationTimeField.setAccessible(true);
			lastModificationTimeField.set(object, currentTime);
		}
		
		if(superClassExists(classObject) && allowNestedUpdate){

			Field lastModificationTimeFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("lastModificationTime");
			lastModificationTimeFieldOfSuperClass.setAccessible(true);
			lastModificationTimeFieldOfSuperClass.set(object, lastModificationTime);
			updateEntity(object, classObject.getSuperclass(), databaseConnection, true,applyOptimisticLocking,currentTime);
		}
		
		List<ColumnNameValuePair> columnNameValuePairs = getColumnNameValuePairList(object, classObject,true);
		String tableName = getTableName(classObject);
		StringBuilder sqlBuilder = new StringBuilder();
		if(columnNameValuePairs.size()>=2){
			sqlBuilder.append("update ").append(tableName).append(" set ");
		}
		for(int i =0 ;i+1<columnNameValuePairs.size();i++){
			ColumnNameValuePair columnNameValuePair = columnNameValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName).append(" = ? ");
			if(i!=columnNameValuePairs.size()-2){
				sqlBuilder.append(",");
			}
		}
		ColumnNameValuePair lastColumnNameValuePair = columnNameValuePairs.get(columnNameValuePairs.size()-1); 
		sqlBuilder.append(" where ").append(lastColumnNameValuePair.columnName).append(" = ?");
		
		
		List<Object> valueList = new ArrayList<>();
		for(ColumnNameValuePair columnNameValuePair : columnNameValuePairs){
			valueList.add(columnNameValuePair.value);
		}
		if(applyOptimisticLocking){
			sqlBuilder.append(" and ").append(getLastModificationTimeColumnName(classObject))
			  .append(" = ? ");
	
			valueList.add(lastModificationTime);
		}

		String sql = sqlBuilder.toString();
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int parameterIndex = 1;
		
		for(Object value: valueList){
			ps.setObject(parameterIndex++, value);
		}
		
		logger.debug("values : "+valueList);
		logger.debug("update sql: "+ps);
		int cnt = ps.executeUpdate();
		ps.close();
		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
		return cnt;
	}
	
	
	static public void deleteEntity(Object object,DatabaseConnection databaseConnection) throws Exception{
		deleteEntity(object,object.getClass(),databaseConnection,true);
	}
	
	static public void deleteEntity(Object object,Class<?> classObject,DatabaseConnection databaseConnection,boolean allowNestedDelete) throws Exception{
		if(!isEntity(classObject)){
			throw new Exception("This is not a database document");
		}
		
		if(superClassExists(classObject) && allowNestedDelete){
			deleteEntity(object, classObject.getSuperclass(), databaseConnection, allowNestedDelete);
		}
		long lastModificationTime = getLastModificationTime(object, classObject);
		String tableName = ((TableName)classObject.getAnnotation(TableName.class)).value();
		String deleteColumnName = getColumnName(classObject, "isDeleted");
		Field primaryKeyField = getPrimaryField(classObject);
		primaryKeyField.setAccessible(true);
		ColumnNameValuePair columnNameValuePair = getColumnNameValuePair(primaryKeyField, object);
		
		String primaryKeyColumnName = columnNameValuePair.columnName;
		Object primaryKeyValue = columnNameValuePair.value;
		String sql = "update "+tableName + " set "+deleteColumnName+" = true where "+primaryKeyColumnName +" = "+primaryKeyValue;
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.execute();
		ps.close();
		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
	}
	
	static public boolean isEntity(Class<?> classObject) throws Exception{
		Annotation annotation = classObject.getAnnotation(TableName.class);
		return  (annotation!=null) && annotation instanceof TableName;
	}
	
	static private long getLastModificationTime(Object domainObject,Class<?> classObject) throws Exception{
		long lastModificationTime = System.currentTimeMillis();
		Field field = null;
		try {
			field = classObject.getDeclaredField("lastModificationTime");
			field.setAccessible(true);
		} catch (Exception ex) {
			logger.debug("lastModificationTime is not found in dto of class type "+classObject);
			logger.debug("Exception " ,ex);
		}	
			
			
		if(field != null){	
			if (field.getLong(domainObject) == 0) {
				//field.set(domainObject, lastModificationTime);
				throw new Exception("lastModificationTime is not set in dto of class type "+classObject.getName());
			}else{
				lastModificationTime = field.getLong(domainObject);
			}
		}
		return lastModificationTime;
	}
	
	
	static public void insert(Object object,DatabaseConnection databaseConnection) throws Exception{
		
		Class<?> classObject = object.getClass();
		while(!classObject.equals(Object.class) && !isEntity(classObject)) {
			classObject = classObject.getSuperclass();
		}
		if(!isEntity(classObject)) {
			throw new Exception("Not an document type");
		}
		
		insert(object,classObject,databaseConnection,true);
	}
	static void populateObjectWithCurrentTime(Object object,Class<?> classObject) throws Exception{
		List<Field> currentTimeAnnotatedFieldList = getCurrentTimeAnnotatedFieldList(classObject);
		for(Field currentTimeAnnotatedField: currentTimeAnnotatedFieldList){
			currentTimeAnnotatedField.set(object, CurrentTimeFactory.getCurrentTime());
		}
	}
	static private List<Field> getCurrentTimeAnnotatedFieldList(Class<?> classObject) throws Exception{
			// synchronization is not used here intentionally
		Field[] fields = classObject.getDeclaredFields();
		List<Field> fieldList = new ArrayList<>();
		for(Field field: fields){
			if(field.getAnnotation(CurrentTime.class)!=null){
				field.setAccessible(true);
				fieldList.add(field);
			}
		}
		return fieldList;
	}
	static public void insert(Object object, Class<?> classObject, DatabaseConnection databaseConnection,boolean allowNestedInsert) throws Exception{
		
		populateObjectWithCurrentTime(object, classObject);
		
		DTOValidator.validate(object, classObject);
		Annotation annotation = classObject.getAnnotation(TableName.class);
		
		if( (annotation instanceof TableName )== false){
			return ;
		}
		long lastModificationTime = 0;		
		lastModificationTime = getLastModificationTime(object, classObject);
		
		String tableName = ((TableName)annotation).value();
		if(superClassExists(classObject) && allowNestedInsert){
			try{
				Field lastModificationTimeFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("lastModificationTime");
				lastModificationTimeFieldOfSuperClass.setAccessible(true);
				lastModificationTimeFieldOfSuperClass.set(object, lastModificationTime);
			}catch(Exception ex){
				logger.debug("lastModificationTime is not found in dto of class type "+classObject.getSuperclass());
			}
			
			if(hasColumn(classObject, "isDeleted", false) && hasColumn(classObject.getSuperclass(), "isDeleted", false)){
				Field isDeletedFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("isDeleted");
				isDeletedFieldOfSuperClass.setAccessible(true);
				
				Field isDeletedFieldOfCurrentClass = classObject.getDeclaredField("isDeleted");
				isDeletedFieldOfCurrentClass.setAccessible(true);
				isDeletedFieldOfSuperClass.set(object, isDeletedFieldOfCurrentClass.get(object));
			}
			
			insert(object,classObject.getSuperclass(),databaseConnection, true);
		}
		Object primaryKey = databaseConnection.getNextID(tableName);
		setPrimaryKey(object, classObject, primaryKey);
		List<ColumnNameValuePair> columnValuePairs = getColumnNameValuePairList(object, classObject);
		StringBuilder sqlBuilder = new StringBuilder("");
		sqlBuilder.append("insert into ").append(tableName).append(" (");
		StringBuilder valuePart = new StringBuilder("  values(");
		for(int i = 0;i<columnValuePairs.size();i++){
			if(i!=0){
				sqlBuilder.append(",");
				valuePart.append(",");
			}
			
			ColumnNameValuePair columnNameValuePair = columnValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName);
			valuePart.append("?");
		}
		sqlBuilder.append(")");
		valuePart.append(")");
		String sql = sqlBuilder.append(valuePart).toString();		

		List<Object> valueList = new ArrayList<>();
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		for(int i = 1;i<=columnValuePairs.size();i++){
			ps.setObject(i, columnValuePairs.get(i-1).value);
			valueList.add(columnValuePairs.get(i-1).value);
		}
		logger.info("ps : "+ps);
		ps.execute();
		ps.close();
		
		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
	}
	
	static private boolean superClassExists(Class<?> classObject){
		if( classObject.getSuperclass()==null){
			return false;
		}
		if((classObject.getAnnotation(ForeignKeyName.class) instanceof ForeignKeyName)==false){
			return false;
		}
		if((classObject.getSuperclass().getAnnotation(TableName.class) instanceof TableName ) == false){
			return false;
		}
		return true;
	}
	
	static public Object createObject(Class<?> classObject,String value) throws Exception{
		if(Integer.TYPE.equals(classObject)){
			return new Integer(value);
		}
		if(Long.TYPE.equals(classObject)){
			return new Long(value);
		}
		return classObject.getConstructor(String.class).newInstance(value);
	}
	
	static public void setPrimaryKey(Object domainObject, Class<?> classObject,Object primaryKey) throws Exception{
		for(Field field : classObject.getDeclaredFields()){
			field.setAccessible(true);
			if(field.getAnnotation(PrimaryKey.class) instanceof PrimaryKey){
			
				field.set(domainObject, createObject(field.getType(), primaryKey.toString()));
				return;
			}
		}
		throw new Exception("No primary key annotation found");
	}
	
	static private ColumnNameValuePair getForeignKeyColumnNameValuePair(Object object, Class<?> classObject) throws Exception{
		ColumnNameValuePair columnNameValuePair = new ColumnNameValuePair();
		String columnName;
		Object columnValue;
		columnName = ((ForeignKeyName)classObject.getAnnotation(ForeignKeyName.class)).value();
		Field field = getPrimaryField(classObject.getSuperclass());
		field.setAccessible(true);
		columnValue = field.get(object);
		columnNameValuePair.columnName = columnName;
		columnNameValuePair.value = columnValue;
		return columnNameValuePair;
	}
	
	static public Object getPrimaryKeyValue(Object entity) throws Exception {
		Field field = getPrimaryField(entity.getClass());
		field.setAccessible(true);
		return field.get(entity);
	}
	static public String getPrimaryKeyColumnName(Class<?> classObject) throws Exception{
		Field field = getPrimaryField(classObject);
		String primaryKeyColumnName = ((ColumnName)field.getAnnotation(ColumnName.class)).value();
		return primaryKeyColumnName;
	}
	static private Field getPrimaryField(Class<?> classObject) throws Exception{
		for(Field field :classObject.getDeclaredFields()){
			if(field.getAnnotation(PrimaryKey.class) instanceof PrimaryKey){
				return field;
			}
		}
		throw new Exception("No primary key found ");
	}
	
	
	
	static String getInsertSql(Object objectInstance) throws Exception{
		Class<?> classObject = objectInstance.getClass();
		StringBuffer sql = new StringBuffer("");
		List<String> columList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		
		Annotation tableNameAnnotation = classObject.getAnnotation(TableName.class);
		String tableName;
		if(tableNameAnnotation  instanceof TableName){
			tableName =  ((TableName)tableNameAnnotation).value();
		}else{
			throw new Exception();
		}
		
		Field fields[] = classObject.getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
			Annotation annotation = field.getAnnotation(ColumnName.class);
			if (annotation instanceof ColumnName) {
				ColumnName columnNameAnnotation = (ColumnName) annotation;
				columList.add(columnNameAnnotation.value());
				valueList.add(field.get(objectInstance));
			}

		}
		
		sql.append("insert into ").append(tableName).append("(");
		for(int i = 0 ;i<columList.size();i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append(columList.get(i));
		}
		sql.append(") values ( ");
		
		for(int i =0;i<valueList.size();i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append(valueList.get(i));
		}
		sql.append(")");
		return sql.toString();
	}
	static public<T> void populateObjectFromDB(T objectInstance,ResultSet rs) throws Exception{
		
		
		populateObjectFromDB(objectInstance, rs,objectInstance.getClass());
	}
	
	static public void populateObjectFromDB(Object objectInstance,ResultSet rs,Class<?> classObject) throws Exception{
		
		for(Field field : classObject.getDeclaredFields()){
			Annotation annotation = field.getAnnotation(ColumnName.class);
			field.setAccessible(true);
			if(annotation instanceof ColumnName){
				String columnName = ((ColumnName)(field.getAnnotation(ColumnName.class))).value();
				populateField(objectInstance,field,rs,columnName);
			}
		}
		if(superClassExists(classObject)){
			String databaseColumnName = getForeignKeyColumnNameValuePair(objectInstance, classObject).columnName;
			populatePrimaryKeyOfSuperClass(objectInstance, 
					classObject,rs,databaseColumnName);
		}
	}
	
	
	
	private static void populatePrimaryKeyOfSuperClass(Object objectInstance, Class<?> classObject,
			ResultSet rs,String databaseColumnName) throws Exception{
		Field field = getPrimaryField(classObject.getSuperclass());
		field.setAccessible(true);
		populateField(objectInstance, field, rs, databaseColumnName);
	}
	
	static private String filterName(String name){
		String tokens[] = name.split("\\.");
		
		if(tokens.length==0){
			return name;
		}else {
			return tokens[tokens.length-1];
		}
	} 
	
	
	
	static private void populateField(Object domainObject ,Field field , ResultSet rs, String databaseColumnName) throws Exception{
		if(field.getType().isEnum()){
			Enum enumVal = Enum.valueOf((Class<? extends Enum>) field.getType(),  (rs.getString(databaseColumnName)));
			field.set(domainObject, enumVal);
			return;
		}
		String typeName = field.getType().getName();
		typeName = filterName(typeName);
		String methodName = "get"+ typeName.toUpperCase().substring(0,1) +typeName.toLowerCase().substring(1);
		methodName = methodName.replaceAll("Integer", "Int");
		Method method = rs.getClass().getMethod(methodName, new Class[]{String.class});
		
		try{
			Object returnValueObject = method.invoke(rs, databaseColumnName);
			if(!field.getType().isPrimitive() && rs.wasNull()){
				returnValueObject = null;
			}
			try{
				field.set(domainObject, returnValueObject);
			}catch(Exception ex){
				
				if(field.getType().equals(Long.class) || field.getType().equals(Integer.class)){
					field.set(domainObject, 0);
				}else{
					throw ex;
				}
			}
		}catch(IllegalTargetException ex){
			//logger.debug("Fatal:", ex);
			logger.debug("exception thrown while invoking method "+methodName+"("+databaseColumnName+")");
			throw ex;
		}catch (InvocationTargetException e) {
			//logger.debug("fatal",e);
			logger.debug("problem populating "+field.getName());
			throw e;
		}
		
	}
	
	public static void updateSequencerTable(Class<?> classObject, DatabaseConnection databaseConnection, long lastModificationTime) throws Exception{
		String tableName = getTableName(classObject);
		databaseConnection.addSequencerSql(tableName, lastModificationTime);
	}
	
	public static void populateObjectFromOtherObject(Object destinationObject,Class<?> destinationClassObject,Object sourceObject,Class<?> sourceClassObject) throws Exception{
		
		
		if(!destinationClassObject.equals(sourceClassObject)){
			throw new Exception("Source class "+sourceClassObject+"  and Destination class "+destinationClassObject+" type does not match");
		}
		Field[] sourceFields = sourceClassObject.getDeclaredFields();
		for(Field sourceField: sourceFields){
			sourceField.setAccessible(true);
			Object sourceValue = sourceField.get(sourceObject);
			Field destinationField = destinationClassObject.getDeclaredField(sourceField.getName());
			destinationField.setAccessible(true);
			try{
				if(!Modifier.isFinal(destinationField.getModifiers())){
					destinationField.set(destinationObject, sourceValue);
				}
			}catch(Exception ex){
				logger.debug("can not set the value of field "+destinationField.getName()+" of class"+destinationClassObject);
			}
		}
		if(destinationClassObject.getSuperclass()!=Object.class){
			populateObjectFromOtherObject(destinationObject, destinationClassObject.getSuperclass(), sourceObject, sourceClassObject.getSuperclass());
		}
	}

	public static boolean isActivated(Class<?> classObject,long ID,DatabaseConnection databaseConnection) throws Exception{
		if(classObject.equals(Object.class)){
			throw new Exception("No method named isActivated() founed");
		}
		Object returnvalue ;
		try{
			Method isActivatedMethod = classObject.getDeclaredMethod( "isActivated");
			Object domainObject = getObjectByID(classObject, ID, databaseConnection);
			if(domainObject == null){
				throw new Exception("No dto found with class type "+classObject+" where id = "+ID);
			}
			returnvalue = isActivatedMethod.invoke(domainObject);
		}catch(NoSuchMethodException noSuchMethodException){
			returnvalue = isActivated(classObject.getSuperclass(), ID,databaseConnection);
		}
		return (Boolean)returnvalue;
	}
	public static String getSelectQueryForNestedQuery(Class<?> classObject,String columnName) throws Exception{
		SearchFieldFromReferenceTable searchFieldFromReferenceTable = null;
		try{
			Field field = classObject.getDeclaredField(columnName);
			field.setAccessible(true);
			searchFieldFromReferenceTable = (SearchFieldFromReferenceTable)field.getDeclaredAnnotation(SearchFieldFromReferenceTable.class);
		}catch(NoSuchFieldException ex){
			searchFieldFromReferenceTable = (SearchFieldFromReferenceTable)classObject.getDeclaredAnnotation(SearchFieldFromReferenceTable.class);
		}
		
		Class<?> referenceTableClassObject = searchFieldFromReferenceTable.entityClass();
		String referenceTableOperator = searchFieldFromReferenceTable.operator();
		String referenceTableColumnName = searchFieldFromReferenceTable.entityColumnName();
		Field primaryFieldOfReferenceTable = getPrimaryField(referenceTableClassObject);
		primaryFieldOfReferenceTable.setAccessible(true);
		
		StringBuilder selectQueryBuilder =new StringBuilder();
		selectQueryBuilder.append("select ").append(getColumnName(referenceTableClassObject, primaryFieldOfReferenceTable.getName()))
							.append(" from ").append(getTableName(referenceTableClassObject)).append(" where ").append(getColumnName(referenceTableClassObject, referenceTableColumnName))
							.append(" ").append(referenceTableOperator).append(" ?");
		
		
		if(hasColumn(classObject, "isDeleted", false)){
			selectQueryBuilder.append(" and ")
								.append(getColumnName(classObject, "isDeleted"))
								.append("=0");
		}
		if(!StringUtils.isBlank(searchFieldFromReferenceTable.fixedCondition())){
			selectQueryBuilder.append(" ")
								.append(searchFieldFromReferenceTable.fixedCondition());
		}
		
		
		
		String nestedQueryString = selectQueryBuilder.toString();
		logger.debug("nested query: "+nestedQueryString);
		return nestedQueryString;
	}
	
	public static boolean hasColumn(Class<?> classObject,String columnName,boolean isNestedField){
		if(classObject.equals(Object.class)){
			return false;
		}
		try{
			classObject.getDeclaredField(columnName);
			return true;
		}catch(Exception ex){
			if(isNestedField){
				return hasColumn(classObject.getSuperclass(), columnName, true);
			}else{
				return false;
			}
		}
		
	}
	
	public static boolean hasAnnotation(Class<?> classObject,String columnName, Class<? extends Annotation> annotationClass) throws Exception{
		if(!hasColumn(classObject, columnName,false)){
			return false;
		}
		Field field = classObject.getDeclaredField(columnName);
		
		field.setAccessible(true);
		return field.getDeclaredAnnotation(annotationClass)!=null; 
	}
	
	public static String getInnerSqlOperator(Class<?> classObject,String columnName) throws Exception{
		Class<SearchFieldFromReferenceTable> annotationClass = SearchFieldFromReferenceTable.class;
		if(!hasAnnotation(classObject, columnName, annotationClass)){
			throw new Exception("Invalid Method call.");
		}
		Field field = classObject.getDeclaredField(columnName);
		field.setAccessible(true);
		
		return ((SearchFieldFromReferenceTable)field.getDeclaredAnnotation(annotationClass)).operator();
	}
	
	public static List<?> getSelectedColumnListFromSearchCriteria(Class<?> classObject,String[] keys,String[] operators,String[] dtoColumnNames,
			Map<String, String> criteriaMap,String fixedCondition,DatabaseConnection databaseConnection,String columnName,Object... conditionString) throws Exception{

		if(keys.length != operators.length || keys.length!=dtoColumnNames.length){
			throw new Exception("Invalid method call. keys.length,operators.length,dtoColumnNames.length should be same");
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select ").append(getColumnName(classObject, columnName)).append(" from ")
					.append(getTableName(classObject)).append(" ");
		List<Object> values = new ArrayList<Object>();
		boolean conditionAdded = false;
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			String userInput = trim(criteriaMap.get(key));
			String operator = toUpperCase(operators[i]);
			String dtoColumnName = trim(dtoColumnNames[i]);
			if(!isBlank(userInput)){
				if(!conditionAdded){
					sqlBuilder.append(" WHERE ");
					conditionAdded = true;
				}else{
					sqlBuilder.append(" AND ");
				}
				
				
				if(operator.equals("NULL")){
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName))
								.append(" IS ").append(userInput.equals("0")?" NULL ":" NOT NULL ");
					
				}else if(hasAnnotation(classObject, dtoColumnName, SearchFieldFromMethod.class)){
					Field field = classObject.getDeclaredField(dtoColumnName);
					field.setAccessible(true);
					SearchFieldFromMethod searchFieldFromMethod = (SearchFieldFromMethod)field.getDeclaredAnnotation(SearchFieldFromMethod.class);
					Class<?> serviceClass = searchFieldFromMethod.objectClass();
					String methodName = searchFieldFromMethod.methodName();
					Class<?> parameterType = searchFieldFromMethod.parameterType();
					Method method = serviceClass.getDeclaredMethod(methodName, parameterType);
					@SuppressWarnings("unchecked")
					List<Number> list = (List<Number>)method.invoke(null, parameterType.equals(Hashtable.class)?criteriaMap: createObject(parameterType, userInput));
					if(list.isEmpty()){
						return new ArrayList<Long>();
					}
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName)).append(" ").append(operator).append(" ").append(common.StringUtils.getCommaSeparatedString(list));
				}else if(operator.equals("IN")){
					sqlBuilder.append(" ").append(getColumnName(classObject, dtoColumnName)).append(" ").append(operator).append("(").
					append(getSelectQueryForNestedQuery(classObject, dtoColumnName)).append(")");
					
					userInput = userInput.replaceAll("_", "\\\\_");
					values.add(userInput+"%");
					
				}else{
					sqlBuilder.append(" "+getColumnName(classObject, dtoColumnName)+" "+operator+" ? ");
					if(operator.equals("LIKE")){
						userInput = userInput.replaceAll("_", "\\\\_");
						values.add(userInput+"%");
					}else{
						values.add(userInput.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{2,4}")?TimeConverter.getTimeFromString(userInput):userInput);
					}
				}
				/*
				if(hasAnnotation(classObject, dtoColumnName, SearchFieldFromMethod.class)){
					// do nothing
				}else if(StringUtils.containsIgnoreCase(StringUtils.trimToEmpty(operator), "like")||
						hasAnnotation(classObject, dtoColumnName, SearchFieldFromReferenceTable.class) && StringUtils.containsIgnoreCase(StringUtils.trimToEmpty(getInnerSqlOperator(classObject, dtoColumnName)), "like")){
					values.add("%"+userInput+"%");
				}else{
					values.add(userInput.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")?TimeConverter.getTimeFromString(userInput):userInput);
				}*/
			}
		}
		if(!isBlank(fixedCondition)){

			if(!conditionAdded){
				sqlBuilder.append(" WHERE ");
				conditionAdded = true;
			}else{
				sqlBuilder.append(" AND ");
			}
			sqlBuilder.append(" "+fixedCondition);
		
		}
		logger.fatal("sql: "+sqlBuilder);
		logger.fatal("values: "+values);
		
		if(conditionString.length>0){
			sqlBuilder.append(conditionString[0]);
		}
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sqlBuilder.toString());
		for(int i=0;i<values.size();i++){
			ps.setObject(i+1, values.get(i));
		}
		logger.info("PreparedStatement: "+ps);
		ResultSet rs = ps.executeQuery();
		List<Object> selectedColumnList = new ArrayList<Object>();
		while(rs.next()){
			selectedColumnList.add(rs.getLong(1));
		}
		rs.close();
		ps.close();
		return selectedColumnList;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Long> getIDListFromSearchCriteria(Class<?> classObject,String[] keys,String[] operators,String[] dtoColumnNames,
			Map<String, String> criteriaMap,String fixedCondition,DatabaseConnection databaseConnection) throws Exception{
		Field primaryField = getPrimaryField(classObject);
		primaryField.setAccessible(true);
		return (List<Long>)getSelectedColumnListFromSearchCriteria(classObject, keys, operators, dtoColumnNames, criteriaMap, fixedCondition, databaseConnection, primaryField.getName());
	}
	
	
	public static int updateEntityByPropertyList(Object object,Class<?> classObject,DatabaseConnection databaseConnection,boolean allowNestedUpdate,boolean applyOptimisticLocking,
			String[] propertyNames,Object ...currentTimeOption) throws Exception{

		if(!isEntity(classObject)){
			throw new Exception("Not an document type.");
		}
		long lastModificationTime = getLastModificationTime(object, classObject);
		
		long currentTime = (currentTimeOption.length==1?(Long)currentTimeOption[0]:System.currentTimeMillis());
		
		if(applyOptimisticLocking){
			// setLastModificationTime
			Field lastModificationTimeField = classObject.getDeclaredField("lastModificationTime");
			lastModificationTimeField.setAccessible(true);
			lastModificationTimeField.set(object, currentTime);
		}
		
		if(superClassExists(classObject) && allowNestedUpdate){

			Field lastModificationTimeFieldOfSuperClass = classObject.getSuperclass().getDeclaredField("lastModificationTime");
			lastModificationTimeFieldOfSuperClass.setAccessible(true);
			lastModificationTimeFieldOfSuperClass.set(object, lastModificationTime);
			updateEntityByPropertyList(object, classObject.getSuperclass(), databaseConnection, allowNestedUpdate, applyOptimisticLocking, propertyNames, currentTimeOption);
		}
		
		
		Set<String> propertyNameSet = new HashSet<String>();
		
		for(String propertyName: propertyNames){
			propertyNameSet.add(propertyName);
		}
		
		List<ColumnNameValuePair> columnNameValuePairs = getColumnNameValuePairList(object, classObject,true,propertyNameSet);
		String tableName = getTableName(classObject);
		StringBuilder sqlBuilder = new StringBuilder();
		if(columnNameValuePairs.size()>=2){
			sqlBuilder.append("update ").append(tableName).append(" set ");
		}
		for(int i =0 ;i+1<columnNameValuePairs.size();i++){
			ColumnNameValuePair columnNameValuePair = columnNameValuePairs.get(i);
			sqlBuilder.append(columnNameValuePair.columnName).append(" = ? ");
			if(i!=columnNameValuePairs.size()-2){
				sqlBuilder.append(",");
			}
		}
		ColumnNameValuePair lastColumnNameValuePair = columnNameValuePairs.get(columnNameValuePairs.size()-1); 
		sqlBuilder.append(" where ").append(lastColumnNameValuePair.columnName).append(" = ?");
		
		
		List<Object> valueList = new ArrayList<>();
		for(ColumnNameValuePair columnNameValuePair : columnNameValuePairs){
			valueList.add(columnNameValuePair.value);
		}
		if(applyOptimisticLocking){
			sqlBuilder.append(" and ").append(getLastModificationTimeColumnName(classObject))
			  .append(" = ? ");
	
			valueList.add(lastModificationTime);
		}

		String sql = sqlBuilder.toString();
		logger.debug(sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		int parameterIndex = 1;
		
		for(Object value: valueList){
			ps.setObject(parameterIndex++, value);
		}
		
		logger.debug("values : "+valueList);
		logger.debug("sql: "+ ps.toString());
		
		int cnt = ps.executeUpdate();
		ps.close();
		updateSequencerTable(classObject, databaseConnection, lastModificationTime);
		return cnt;
	}
	public static Object getPropertyValueByPropertyName(Object object, String propertyName) throws Exception{
		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		return  field.get(object);
	}

}

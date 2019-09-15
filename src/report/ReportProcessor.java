package report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

import org.apache.log4j.Logger;
import common.StringUtils;
import connection.DatabaseConnection;
import util.SqlPair;


public class ReportProcessor {
	
	
	public static List<List<Object>> getResult(ReportMetadata reportMetadata
			,String tableName,DatabaseConnection databaseConnection) throws Exception{
		return getResult(reportMetadata, tableName, databaseConnection,Integer.MAX_VALUE,0);
	}
	
	public static int getTotalResultCount(ReportMetadata reportMetadata
			,String tableName,DatabaseConnection databaseConnection) throws Exception{
		SqlPair sqlPair = createReportSQL(reportMetadata, tableName);
		
		sqlPair.sql = sqlPair.sql.replaceAll(" order by.*", "");
		
		sqlPair.sql = "select count(*) from ("+sqlPair.sql+") tmp";
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sqlPair.sql);
		for(int i=0;i<sqlPair.objectList.size();i++){
			ps.setObject(i+1, sqlPair.objectList.get(i));
		}
		System.out.println(ps.toString());
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
	
	public static List<List<Object>> getResult(ReportMetadata reportMetadata
			,String tableName,DatabaseConnection databaseConnection,int limit,int offset) throws Exception{
		
		SqlPair sqlPair = createReportSQL(reportMetadata, tableName);
		
		sqlPair.sql+=(" limit "+limit+" offset "+offset);
		
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sqlPair.sql);
		
		for(int i=0;i<sqlPair.objectList.size();i++){
			ps.setObject(i+1, sqlPair.objectList.get(i));
		}
		System.out.println(ps.toString());
		ResultSet rs = ps.executeQuery();
		return getResultByResultSet(rs,reportMetadata.getOutputColumnNames());
	}
	
	
	public static SqlPair createReportSQL(ReportMetadata reportMetadata
			,String tableName) throws Exception{
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT ");
		if(reportMetadata.getOutputColumnNames().isEmpty()){
			sqlBuilder.append(" * ");
		}else{
			for(int i=0;i<reportMetadata.getOutputColumnNames().size();i++){
				if(i!=0){
					sqlBuilder.append(",");
				}
				DisplayColumn outputColum = reportMetadata.getOutputColumnNames().get(i);
				sqlBuilder.append(outputColum.databaseColumnName)
							.append(" '")
							.append(StringUtils.trim(outputColum.caption))
							.append("' ");
			}
		}
		
		List<Object> values = new ArrayList<>();
		sqlBuilder.append(" FROM ")
					.append(tableName)
					.append(" ");
		for(int i=0;i<reportMetadata.getInputColumnNames().size();i++){
			if(i==0){
				sqlBuilder.append(" WHERE ");
			}else{
				sqlBuilder.append(" AND ");
				
			}
			
			
			CriteriaColumn inputColumn = reportMetadata.getInputColumnNames().get(i);
			if(inputColumn.operator.equalsIgnoreCase("null")){
				
				sqlBuilder.append(" ")
							.append(inputColumn.databaseColumnName)
							.append(" IS ");
				
				
				if(inputColumn.criteriaValue.trim().equals("1")){
					sqlBuilder.append(" NOT ");
				}
				
				sqlBuilder.append(" NULL ");
				
			}else{

				sqlBuilder.append(" ")
						.append(inputColumn.databaseColumnName)
						.append(" ")
						.append(inputColumn.operator)
						.append(" ");
				
				if(inputColumn.operator.equalsIgnoreCase("in")){
					sqlBuilder.append(inputColumn.criteriaValue);
				}else{						
					sqlBuilder.append(" ? ");
					values.add(inputColumn.criteriaValue);
					
				}		

			}
			
		}
		
		if(!reportMetadata.getOrderByColumnNames().isEmpty()){
			int i=0;
			sqlBuilder.append(" order by ");
			for(String orderByColumnName: reportMetadata.getOrderByColumnNames()){
				if(i!=0){
					sqlBuilder.append(",");
				}
				sqlBuilder.append(orderByColumnName);
				i++;
			}
		}
		
		SqlPair sqlPair = new SqlPair();
				
		sqlPair.sql = sqlBuilder.toString();
		sqlPair.objectList = values;
		
		Logger logger = Logger.getLogger(ReportProcessor.class);
		logger.debug(sqlPair);
		
		return sqlPair;
	}
	
	
	public static List<List<Object>> getResultByResultSet(ResultSet rs,List<DisplayColumn> outputColums) throws Exception{
		List<List<Object>> result = new ArrayList<>();
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		
		List<Object> captions = new ArrayList<>();
		for(int i=0;i<resultSetMetaData.getColumnCount();i++){
			captions.add(resultSetMetaData.getColumnLabel(i+1));
		}
		
		result.add(captions);
		
		while (rs.next()) {
			List<Object> row = new ArrayList<>();
			for(int i=0;i<resultSetMetaData.getColumnCount();i++){
				Object object = rs.getObject(i+1);
				object = outputColums.get(i).convertor.convert(object);
				row.add(object);
			}
			result.add(row);
			
		}
		
		
		return result;
	}
	
	
}

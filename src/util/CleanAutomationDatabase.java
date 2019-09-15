package util;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import connection.DatabaseConnection;
import permission.LoadPermissions;

public class CleanAutomationDatabase {

	static Logger logger = Logger.getLogger(LoadPermissions.class);
	static Set<String> tableSet = new HashSet<String>();
	static
	{
		tableSet.add("at_action_permission");
		tableSet.add("adpermission");
		tableSet.add("adcolumnpermission");
		
		tableSet.add("adcategory");
		tableSet.add("adcolumn");
		tableSet.add("admenu");
		tableSet.add("adrole");
		tableSet.add("aduser");
		
		tableSet.add("at_action_state");
		tableSet.add("at_cost_chart");
		tableSet.add("at_cost_chart_row");
		tableSet.add("at_cost_chart_col");
		tableSet.add("at_crm_department");
		tableSet.add("at_crm_employee");
		tableSet.add("at_district");
		tableSet.add("at_district_distance");
		tableSet.add("at_global_config");
		tableSet.add("at_inventory_item");
		tableSet.add("at_inv_attr_name");
		tableSet.add("at_inv_attr_value");
		tableSet.add("at_inv_cat_type");
		tableSet.add("at_module");
		tableSet.add("at_state");
		tableSet.add("at_state_actiontype");
		tableSet.add("at_union_distance");
		tableSet.add("at_upazila_distance");
		tableSet.add("at_vpn_category_type");
		tableSet.add("at_vpn_common_charge_config");
		tableSet.add("at_vpn_monthly_charge_cell");
		tableSet.add("vbSequencer");
		tableSet.add("vbmailserverinformation");
	}
	public static void main(String args[]) throws Exception
	{
		logger.debug("Clean Automation DB");
		DatabaseConnection databaseConnection = new DatabaseConnection();
		String sql = "";
		Statement statement = null;
		Statement statement2 = null;
		ResultSet resultSet = null;
		try
		{
			databaseConnection.dbOpen();
			databaseConnection.dbTransationStart();
			statement = databaseConnection.getNewStatement();
			statement2 = databaseConnection.getNewStatement();
			sql = "SET FOREIGN_KEY_CHECKS = 0";
			statement.executeQuery(sql);
			
			sql = "SELECT table_name FROM information_schema.tables where table_schema='btclautomation_global_test'";
			
			
			resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				String tableName = resultSet.getString(1);
				
				if(tableSet.contains(tableName))continue;
				
				sql = "delete from " + tableName;
				logger.debug(sql);
				statement2.execute(sql);
			}

			sql = "SET FOREIGN_KEY_CHECKS = 0";
			statement.executeQuery(sql);
			
			databaseConnection.dbTransationEnd();
		}
		catch(Exception ex)
		{
			logger.debug("ex", ex);
			databaseConnection.dbTransationRollBack();
		}
		finally
		{
			databaseConnection.dbClose();
		}
		logger.debug("Done");
		
	}
	
}

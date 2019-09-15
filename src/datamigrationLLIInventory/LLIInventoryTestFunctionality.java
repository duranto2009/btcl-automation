package datamigrationLLIInventory;

import java.util.ArrayList;
import java.util.List;

import annotation.Transactional;
import datamigrationGeneric.CSVFileHandler;
import datamigrationLLIClient.LLIClientMigrationService;
import inventory.InventoryItem;
import inventory.InventoryItemConditionBuilder;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.SqlGenerator;

public class LLIInventoryTestFunctionality {

	public static void main(String[] args) throws Exception {

		// 1 for routers
		// 2 for switches
		int typeOfInput = 1;

		// routers
		CSVFileHandler csvFileHandler = new CSVFileHandler();
		ArrayList<List<String>> existingEntries = csvFileHandler.getDataFromCSVFile("router-inventory.csv");
		existingEntries.remove(0);
		
		/*

		// Avoid sending null rows (or "") to the methods altogether
		// if the first column is null, don't send the row at all
		for (int i = 0; i < existingEntries.size(); i++) {
			if (existingEntries.get(i).get(0) == null || existingEntries.get(i).get(0) == "")
				continue;
			ServiceDAOFactory.getService(LLIInventoryMigrationService.class).addRouterOrSwitch(existingEntries.get(i),
					typeOfInput);
		}
		
		//switches
		typeOfInput++;
		csvFileHandler = new CSVFileHandler();
		existingEntries = csvFileHandler.getDataFromCSVFile("switch-inventory.csv");
		existingEntries.remove(0);

		for (int i = 0; i < existingEntries.size(); i++) {
			if (existingEntries.get(i).get(0) == null || existingEntries.get(i).get(0) == "")
				continue;
			ServiceDAOFactory.getService(LLIInventoryMigrationService.class).addRouterOrSwitch(existingEntries.get(i),
					typeOfInput);
		}

		
		//vlans
		csvFileHandler = new CSVFileHandler();
		existingEntries = csvFileHandler.getDataFromCSVFile("vlan-inventory.csv");
		existingEntries.remove(0);
		
		for (int i = 0; i < existingEntries.size(); i++) {
			if (existingEntries.get(i).get(0) == null || existingEntries.get(i).get(0) == "")
				continue;
			ServiceDAOFactory.getService(LLIInventoryMigrationService.class).addVlan(existingEntries.get(i));
		}
		*/
	
		System.out.println(existingEntries.get(0).get(0));
	}
}

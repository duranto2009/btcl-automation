package datamigrationLLIClient;

import datamigrationGeneric.CSVFileHandler;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.List;

public class LLIClientTestFunctionality {

    public static void main(String[] args) throws Exception {

        CSVFileHandler csvFileHandler = new CSVFileHandler();
        ArrayList<List<String>> existingEntries = csvFileHandler.getDataFromCSVFile("C:\\Users\\HP\\Desktop\\clients_old.csv");

        existingEntries.remove(0);

        System.out.println(existingEntries.size());

        int start = 12;
        int end = start+10;

        for (int i = 200; i < 743; i++) {

            System.out.println("CURRENT ROW:" + i);

            List<String> list = existingEntries.get(i);
            System.out.println(list);
            ServiceDAOFactory.getService(LLIClientMigrationService.class).insertClient(list);

        }


    }
}

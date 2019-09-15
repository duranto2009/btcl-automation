package datamigrationLLIConnections;

public class LLIConnectionTestFunctionality {

    public static void main(String[] args) throws Exception {

//        CSVFileHandler csvFileHandler = new CSVFileHandler();
//        ArrayList<List<String>> existingEntries = csvFileHandler.getDataFromCSVFile("old_LLI_connections.csv");
//
//        existingEntries.remove(0);
//        existingEntries.remove(0);
//        existingEntries.remove(0);
//        existingEntries.remove(0);
//        existingEntries.remove(0);
//
//
//        int array[] = {738,739,777,778};
//
//
//        int start = 811;
//        int end = 828;
//        for (int i = start; i <= end; i++) {
//            System.out.println("CURRENT ROW: " + i);
//
//            boolean flag = true;
//
//            for (int k = 0; k < array.length; k++)
//                if (i == array[k]) {
//                    flag = false;
//                    break;
//                }
//
//            if (!flag) continue;
//
//            System.out.println(existingEntries.get(i));
//            ServiceDAOFactory.getService(LLIConnectionMigrationService.class).addConnection(existingEntries.get(i));
//        }
//
//
//        try {
//            DatabaseManagerImplementation.getInstance().closeAllConnections();
//        } catch (JDOMException | SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//            e.printStackTrace();
//        }


        System.out.println(LLIConnectionMigrationService.getIpStartAndEndsFromString("123.142.18.149-162"));



    }

}

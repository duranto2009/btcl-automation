package common.bill;

import annotation.DAO;
import annotation.Transactional;
import databasemanager.DatabaseManagerImplementation;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillCheckService {
    @DAO
    BillCheckDAO billCheckDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<BillDTO> checkBillByClientByModule(long clientId, long fromDate, long toDate, int moduleId) throws Exception {
        return billCheckDAO.checkBillByClientByModule(clientId, fromDate, toDate, moduleId);
    }


    public List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>> getMonthWiseDues(long clientId, long fromDate, long toDate, int moduleId) throws Exception {

        List<BillDTO> dueBills = this.checkBillByClientByModule(clientId, fromDate, toDate, moduleId);
        Map<Integer, Map<Integer, List<BillDTO>>> mapOfBillsToMonthToYear = dueBills.stream()
                .collect(
                        Collectors.groupingBy(BillDTO::getYear,
                                Collectors.groupingBy(BillDTO::getMonth)
                        )
                );


        List<KeyValuePair<Integer, KeyValuePair<Integer, Double>>> list = mapOfBillsToMonthToYear.entrySet()
                .stream()
                .map(t -> {
                    int year = t.getKey();
                    Map<Integer, List<BillDTO>> mapOfBillsToMonth = t.getValue();

                    List<KeyValuePair<Integer, Double>> listOfDuesToMonth = mapOfBillsToMonth.entrySet()
                            .stream()
                            .map(x -> {
                                int month = x.getKey();
                                List<BillDTO> bills = x.getValue();
                                double totalDue = bills
                                        .stream()
                                        .mapToDouble(BillDTO::getNetPayable)
                                        .sum();
                                return new KeyValuePair<>(month, totalDue);
                            }).collect(Collectors.toList());


                    return listOfDuesToMonth.stream()
                            .map(x -> new KeyValuePair<>(year, new KeyValuePair<>(x.getKey(), x.getValue())))
                            .collect(Collectors.toList());


                })
                .flatMap(List::stream)
                .collect(Collectors.toList());


        list.forEach(t -> {
            System.out.println(t.getKey() + " :: " + t.getValue().getKey() + " => " + t.getValue().getValue());
        });

        return list;


    }


    public static void main(String args[]) throws Exception {

        ServiceDAOFactory.getService(BillCheckService.class).getMonthWiseDues(460002,
                1546279200000L, 1575136800000L, 9);

        DatabaseManagerImplementation.getInstance().closeAllConnections();
    }
}

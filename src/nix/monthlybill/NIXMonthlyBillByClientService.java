package nix.monthlybill;

import annotation.DAO;
import annotation.Transactional;
import inventory.InventoryConstants;
import login.LoginDTO;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.ServiceDAOFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class NIXMonthlyBillByClientService {

    public static Logger logger = Logger.getLogger(NIXMonthlyBillByClientService.class);
    NIXMonthlyBillByConnectionService nixMonthlyBillByConnectionService = ServiceDAOFactory.getService(NIXMonthlyBillByConnectionService.class);


    @DAO
    NIXMonthlyBillByClientDAO nixMonthlyBillByClientDAO;


    @Transactional
    public void save(NIXMonthlyBillByClient object) {

        try {
            if (object.getId() == null || object.getId() == 0)
                nixMonthlyBillByClientDAO.insertItem(object);
            else
                nixMonthlyBillByClientDAO.updateItem(object);
        } catch (Exception e) {
            logger.error("error while saving ", e);
        }
        return;
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    public NIXMonthlyBillByClient getById(long id) {

        try {
            NIXMonthlyBillByClient monthlyBillByClient = nixMonthlyBillByClientDAO.getItem(id);
            setMonthlyBillAndPortFee(monthlyBillByClient);


            return monthlyBillByClient;
        } catch (Exception e) {
        }
        return null;
    }

    private NIXMonthlyBillByClient setMonthlyBillAndPortFee(NIXMonthlyBillByClient nixMonthlyBillByClient){
        List<NIXMonthlyBillByConnection> nixMonthlyBillByConnection = nixMonthlyBillByConnectionService.getListByMonthlyBillByClientId(nixMonthlyBillByClient.getId());
        List<FeeByPortTypeForClient> feeByPortTypeForClients=new ArrayList<>();
        for (NIXMonthlyBillByConnection monthlyBillByConnection:nixMonthlyBillByConnection
        ) {
            FeeByPortTypeForClient feeByPortTypeForClient=new FeeByPortTypeForClient();
            int portType= (int) monthlyBillByConnection.getPortType();
            feeByPortTypeForClient.setPortType(InventoryConstants.mapOfPortTypeToPortTypeString.get(portType));
            feeByPortTypeForClient.setPortCount(monthlyBillByConnection.getPortCount());
            feeByPortTypeForClient.setPortCost(monthlyBillByConnection.getPortCost());
            feeByPortTypeForClients.add(feeByPortTypeForClient);


        }
        nixMonthlyBillByClient.setFeeByPortTypeForClients(feeByPortTypeForClients);
        nixMonthlyBillByClient.setMonthlyBillByConnections(nixMonthlyBillByConnection);
        return nixMonthlyBillByClient;
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    public NIXMonthlyBillByClient getByClientIdAndDateRange(long clientId, int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);
        try {
            NIXMonthlyBillByClient client = nixMonthlyBillByClientDAO.getByClientIdAndDateRange(clientId, fromDate, toDate);
            client=setMonthlyBillAndPortFee(client);
            return client;
        } catch (Exception e) {
        }
        return null;
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    public List<NIXMonthlyBillByClient> getByDateRange(int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);

        try {
            List<NIXMonthlyBillByClient> nixMonthlyBillByClients=nixMonthlyBillByClientDAO.getByDateRange(fromDate, toDate);
            for (NIXMonthlyBillByClient monthlyBillByClient:nixMonthlyBillByClients
                 ) {
                setMonthlyBillAndPortFee(monthlyBillByClient);

            }
            return nixMonthlyBillByClients;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new ArrayList<NIXMonthlyBillByClient>();
    }

    @Transactional(transactionType = util.TransactionType.READONLY)
    public boolean isMonthlyBillGenerated(int month, int year) {

        long fromDate = DateUtils.getStartTimeOfMonth(month, year);
        long toDate = DateUtils.getEndTimeOfMonth(month, year);
        try {
            return nixMonthlyBillByClientDAO.getCountByDateRange(fromDate, toDate) > 0 ? true : false;
        } catch (Exception e) {
            logger.fatal("error in isMonthlyBillGenerated ");
        }
        return true;
    }

    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
        // TODO Auto-generated method stub
        return nixMonthlyBillByClientDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }


    public Collection getNIXMonthlyBillListByIDList(List<Long> recordIDs) throws Exception {
        // TODO Auto-generated method stub
        return nixMonthlyBillByClientDAO.getNIXMonthlyBillByIDList((List<Long>) recordIDs);
    }


    public static void main(String[] args) {
        System.out.println(ServiceDAOFactory.getService(NIXMonthlyBillByClientService.class).getById(5010));
    }
}

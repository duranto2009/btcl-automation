//package vpn.connection;
//
//import annotation.DAO;
//import annotation.Transactional;
//import common.RequestFailureException;
//import login.LoginDTO;
//import nix.office.NIXOfficeService;
//import util.NavigationService;
//import util.ServiceDAOFactory;
//import util.TransactionType;
//
//import java.util.Collection;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class NIXConnectionService implements NavigationService {
//    @DAO
//    NIXConnectionDAO nixConnectionDAO;
//
//    NIXOfficeService nixOfficeService= ServiceDAOFactory.getService(NIXOfficeService.class);
//
//    @Transactional
//    public void insertConnection(NIXConnection nixConnection)throws Exception {
//        nixConnectionDAO.insertConnection(nixConnection);
//    }
//
//    @Transactional(transactionType = TransactionType.READONLY)
//    public List<NIXConnection> getNIXConnectionInstanceListByDateRange(long fromDate, long toDate) throws Exception {
//
//        List<NIXConnection> nixConnections = nixConnectionDAO
//                .getNIXConnectionInstanceListByDateRange(fromDate, toDate)
//                .stream()
//                .map(t ->
//                {
//                    try {
//                        t.setNixOffices(nixOfficeService.getOfficesByConnectionID(t.getId()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return t;
//                }).collect(Collectors.toList());
////
////        populateNIXConnectionInstancesWithOfficeAndLocalLoop(lliConnectionInstanceList);
//
//        return nixConnections;
//    }
//
//    @Transactional(transactionType = TransactionType.READONLY)
//    public List<NIXConnection> getNIXConnectionInstanceListByHistoryID(long historyID) throws Exception {
//
//        List<NIXConnection> nixConnections = nixConnectionDAO
//                .getNIXConnectionInstanceListByHistoryID(historyID)
//                .stream()
//                .map(t ->
//                {
//                    try {
//                        t.setNixOffices(nixOfficeService.getOfficesByConnectionID(t.getId()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return t;
//                }).collect(Collectors.toList());
//
//
//        return nixConnections;
//    }
//
//    @Transactional
//    public List<NIXConnection> getConnectionByClientID(long clientId) throws Exception{
//        return nixConnectionDAO.getConnectionByClientID(clientId);
//    }
//
//    @Transactional
//    public List<NIXConnection> getActiveConnectionByClientID(long clientId) throws Exception{
//        return nixConnectionDAO.getActiveConnectionByClientID(clientId);
//    }
//
//    @Transactional(transactionType = TransactionType.READONLY)
//    public NIXConnection getConnnectionByConnectionId(long connectionId)throws Exception{
//        List<NIXConnection>nixConnections = nixConnectionDAO.getConnectionByConnectionID(connectionId);
//        if(nixConnections.isEmpty()) {
//            throw new RequestFailureException("No NIX Connection Instance Found for Connection Id : " + connectionId);
//        }
//        return nixConnections.get(0);
//    }
//
//    @Transactional
//    public void updateConnection(NIXConnection nixConnection) throws Exception{
//        nixConnectionDAO.update(nixConnection);
//    }
//
//    public void reconnectionConnectionByClientID(long clientID) {
//    }
//
//    public void tdNIXConnectionByClientID(long clientID, long tdActivationDate) {
//    }
//
//    /**
//     * @author forhad
//     */
//    @SuppressWarnings("rawtypes")
//    @Override
//    @Transactional(transactionType = TransactionType.READONLY)
//    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
//        return getIDsWithSearchCriteria(new Hashtable(), loginDTO, objects);
//    }
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    @Override
//    @Transactional(transactionType = TransactionType.READONLY)
//    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
//        return nixConnectionDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
//    }
//
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    @Override
//    @Transactional(transactionType = TransactionType.READONLY)
//    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
//        return nixConnectionDAO.getNIXConnectionListByConnectionIDList((List) recordIDs);
//    }
//
//
//    @Transactional(transactionType = TransactionType.READONLY)
//    public NIXConnection getLatestNIXConnectionByConnectionHistoryId(long connectionId) throws Exception {
//        return getNIXConnectionInstanceListByHistoryID(connectionId)
//                .stream()
//                .reduce((f, s)->s)
//                .orElseThrow(()->new RequestFailureException("No Data Found with NIX connection history id " + connectionId));
//    }
//    public static void main(String[] args) throws Exception {
//        List<NIXConnection> nixConnections=ServiceDAOFactory.getService(NIXConnectionService.class).getNIXConnectionInstanceListByHistoryID(2);
//    }
//}

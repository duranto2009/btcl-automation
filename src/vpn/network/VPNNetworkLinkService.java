package vpn.network;

import annotation.DAO;
import annotation.Transactional;
import common.ModuleConstants;
import common.RequestFailureException;
import entity.localloop.*;
import entity.office.Office;
import exception.NoDataFoundException;
import global.GlobalService;
import lli.LLIDropdownPair;
import login.LoginDTO;
import requestMapping.Service;
import util.*;
import vpn.VPNConstants;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class VPNNetworkLinkService implements NavigationService {

    @Service private GlobalService globalService;
    @Service private LocalLoopService localLoopService;
    @DAO private VPNNetworkLinkDAO vpnNetworkLinkDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNNetworkLink> getVPNLinksByDateRange(long fromDate, long toDate) throws Exception {

        List<VPNNetworkLink> links = globalService.getAllObjectListByCondition(VPNNetworkLink.class, new VPNNetworkLinkConditionBuilder()
                .Where()
                .activeFromLessThan(toDate)
                .activeToGreaterThan(fromDate)
                .validToEquals(Long.MAX_VALUE)
                .getCondition());

        if (links.isEmpty())
            throw new RequestFailureException("No vpn link instance exists in that date range.");

        return links;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    List<LocalLoop> getLocalLoopsByLinkId(long linkId) throws Exception {

        List<Long> localLoopIds = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class, new LocalLoopConsumerMapConditionBuilder()
                .Where()
                .consumerIdEquals(linkId)
                .getCondition())
                .stream()
                .map(LocalLoopConsumerMap::getLocalLoopId)
                .collect(Collectors.toList());

        List<LocalLoop> loops = new ArrayList<>();
        localLoopIds.forEach(localLoopId -> {
            try {
                loops.add(globalService.findByPK(LocalLoop.class, localLoopId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (loops.isEmpty()) {
            throw new RequestFailureException("No Local Loop Found with link id " + linkId);
        }
        localLoopService.setVolatileProperties(loops);
        return loops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LocalLoop> getBillApplicableLocalLoopsByLinkId(long linkId) throws Exception {
        List<Long> localLoopIds = globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class, new LocalLoopConsumerMapConditionBuilder()
                .Where()
                .consumerIdEquals(linkId)
                .getCondition())
                .stream()
                .filter(LocalLoopConsumerMap::isBillApplicable)
                .map(LocalLoopConsumerMap::getLocalLoopId)
                .collect(Collectors.toList());

        List<LocalLoop> loops = new ArrayList<>();
        localLoopIds.forEach(localLoopId -> {
            try {
                loops.add(globalService.findByPK(LocalLoop.class, localLoopId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        if (loops.isEmpty()) {
//            throw new RequestFailureException("No Local Loop Found with link id " + linkId);
//        }
        return loops;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNNetworkLink> getAllLinksByApplicationId(long applicationId) throws Exception {
        List<VPNNetworkLink> links = globalService.getAllObjectListByCondition(VPNNetworkLink.class,
                new VPNNetworkLinkConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationId)
                        .getCondition());

        if (links.isEmpty()) {
            throw new RequestFailureException("No VPN links Found with application id " + applicationId);
        }

        return links;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    VPNLinkOfficePair getOfficesByLinkId(long linkId) throws Exception {
        VPNNetworkLink link = globalService.findByPK(VPNNetworkLink.class, linkId);
        if (link == null) {
            throw new RequestFailureException("no link found with link id " + linkId);
        }

        Office local = globalService.findByPK(Office.class, link.getLocalOfficeId());
        if (local == null) {
            throw new RequestFailureException("no local office found with office id " + link.getLocalOfficeId());
        }

        Office remote = globalService.findByPK(Office.class, link.getRemoteOfficeId());
        if (remote == null) {
            throw new RequestFailureException("no remote office found with office id " + link.getRemoteOfficeId());
        }
        VPNLinkOfficePair pair = new VPNLinkOfficePair();

        pair.setLocalEndOffice(local);
        pair.setRemoteEndOffice(remote);

        return pair;
    }

    public List<VPNNetworkLink> allInfo(long linkId) throws Exception {
        List<VPNNetworkLink> vpnNetworkLinks = getHistoryOfLinkByLinkId(linkId);
        for (VPNNetworkLink vpnNetworkLink : vpnNetworkLinks) {
            vpnNetworkLink.setOffices();
            List<LocalLoop> localLoops = getLocalLoopsByLinkId(vpnNetworkLink.getId());
            for (LocalLoop localLoop : localLoops) {
                if (localLoop.getOfficeId() == vpnNetworkLink.getLocalOfficeId()) {
                    vpnNetworkLink.getLocalEndOffice().setLocalLoops(Collections.singletonList(localLoop));
                }
                if (localLoop.getOfficeId() == vpnNetworkLink.getRemoteOfficeId()) {
                    vpnNetworkLink.getRemoteEndOffice().setLocalLoops(Collections.singletonList(localLoop));
                }
            }
        }
        return vpnNetworkLinks;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNNetworkLink> getHistoryOfLinkByLinkId(long linkId) throws Exception {
        VPNNetworkLink link = globalService.findByPK(VPNNetworkLink.class, linkId);

        long historyId = link.getHistoryId();

        List<VPNNetworkLink> links = globalService.getAllObjectListByCondition(VPNNetworkLink.class,
                new VPNNetworkLinkConditionBuilder()
                        .Where()
                        .historyIdEquals(historyId)
                        .getCondition());
        if (links.isEmpty()) {
            throw new RequestFailureException("no history found with link id " + linkId);
        }

        return links;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @SuppressWarnings("unchecked")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        searchCriteria.put("moduleId", Integer.toString(ModuleConstants.Module_ID_VPN));
        return vpnNetworkLinkDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @SuppressWarnings("unchecked")
    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        int elementsToSkip = (int) objects[1];
        int elementsToConsider = (int) objects[2];
        return vpnNetworkLinkDAO.getDTOListByIDList((List<Long>) recordIDs)
                .stream()
                .skip(elementsToSkip)
                .limit(elementsToConsider)
                .collect(Collectors.toList())
                ;
    }

    /***
     *
     * @param clientId the specified client
     * @return List of VPNNetworkLinks of the specified client
     * @throws Exception if no network links found 
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNNetworkLink> getActiveNetworkLinksByClient(long clientId) throws Exception {
        List<VPNNetworkLink> networkLinks = globalService.getAllObjectListByCondition(
                VPNNetworkLink.class,
                new VPNNetworkLinkConditionBuilder()
                        .Where()
                        .clientIdEquals(clientId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition()
        );
        if (networkLinks.isEmpty()) {
            throw new NoDataFoundException("No Network Links Found for client id " + clientId);
        }

        return networkLinks.stream()
                .filter(t->!t.getLinkStatus().equalsIgnoreCase(VPNConstants.Status.VPN_TD.getStatus()))
                .collect(Collectors.toList());
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIDropdownPair> getActiveVPNLinkNameIDPairListByClient(long clientID) throws Exception {
        return vpnNetworkLinkDAO.getActiveLinkNameIDPairListByClientID(clientID);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    List<LocalLoop> getLocalLoopsByOfficeId(long officeId) throws Exception {
        List<LocalLoop> localLoops = globalService.getAllObjectListByCondition(LocalLoop.class,
                new LocalLoopConditionBuilder()
                        .Where()
                        .officeIdEquals(officeId)
                        .getCondition());

        if (localLoops.isEmpty()) {
            throw new RequestFailureException("no local loops found with office id " + officeId);
        }

        return localLoops;
    }

    @Transactional
    public void markLatestHistoryObsoleteByNetworkLinkId(VPNNetworkLink vpnNetworkLink) throws Exception {
        if(vpnNetworkLink.getActiveTo() != Long.MAX_VALUE && !vpnNetworkLink.getLinkStatus().equals(VPNConstants.Status.VPN_ACTIVE.getStatus())) {
            throw new RequestFailureException("The snapshot is already obsolete. network link id " + vpnNetworkLink.getId());
        }
        List<LocalLoopConsumerMap> localLoopConsumerMaps=globalService.getAllObjectListByCondition(LocalLoopConsumerMap.class,
                new LocalLoopConsumerMapConditionBuilder()
                .Where()
                .consumerIdEquals(vpnNetworkLink.getId())
                .getCondition()
        );
        for (LocalLoopConsumerMap localLoopConsumerMap:localLoopConsumerMaps
             ) {
            localLoopConsumerMap.setActive(false);
            globalService.update(localLoopConsumerMap);
        }
        vpnNetworkLink.setActiveTo(System.currentTimeMillis());
        globalService.update(vpnNetworkLink);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public int getTotalVPNLinkCount() throws Exception {
        VPNNetworkLinkConditionBuilder networkLinkConditionBuilder = new VPNNetworkLinkConditionBuilder();
        String selectPart = "Select count(*) as count ";
        SqlPair sqlPair = networkLinkConditionBuilder.fromVPNNetworkLink().Where().activeToEquals(Long.MAX_VALUE).getSqlPair();

        sqlPair.sql = selectPart + sqlPair.sql;

        ResultSet rs = ModifiedSqlGenerator.getResultSetBySqlPair(sqlPair);
        rs.next();
        return rs.getInt("count");
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<VPNNetworkLink> getTotalVPNLinkByClient(Long clientId ) throws Exception {
        return globalService.getAllObjectListByCondition(
                VPNNetworkLink.class, new VPNNetworkLinkConditionBuilder()
                .Where()
                .clientIdEquals(clientId)
                .activeToGreaterThan(CurrentTimeFactory.getCurrentTime())
                .getCondition()
        );
    }

}

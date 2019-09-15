package vpn.network;

import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import common.repository.AllClientRepository;
import lli.LLIDropdownPair;
import login.LoginDTO;
import util.ModifiedSqlGenerator;
import vpn.VPNConstants;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.getObjectListByIDList;
import static util.ModifiedSqlGenerator.getResultSetBySqlPair;

public class VPNNetworkLinkDAO {

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria, LoginDTO loginDTO) throws Exception {

        ClientDTO clientDTO =null;

        if (loginDTO.getUserID()< 0) {
            clientDTO = AllClientRepository.getInstance()
                    .getModuleClientByClientIDAndModuleID(loginDTO.getAccountID(), Integer.parseInt(searchCriteria.get("moduleId")));
            if (clientDTO.getClientID() >= 0) {
                searchCriteria.put("clientName", clientDTO.getLoginName());
            }
        }

        ResultSet resultSet = ModifiedSqlGenerator.getResultSetBySqlPair(
                new VPNNetworkLinkConditionBuilder()
                        .selectId()
                        .fromVPNNetworkLink()
                        .Where()
                        .idEqualsString(searchCriteria.getOrDefault("linkId", ""))
                        .activeToGreaterThan(System.currentTimeMillis())
                        .validToGreaterThanEquals(System.currentTimeMillis())
                        .clientIdInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(searchCriteria.get("clientName"))
                                        .getNullableSqlPair()
                        )
                        .getSqlPair()


        );

        return ModifiedSqlGenerator.getSingleColumnListByResultSet(resultSet, Long.class);
    }

    public List<VPNNetworkLink> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(VPNNetworkLink.class, idList)
                .stream()
                .sorted(Comparator.comparing(VPNNetworkLink::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }


    public List<LLIDropdownPair> getActiveLinkNameIDPairListByClientID(long clientID) throws Exception{
        List<LLIDropdownPair> list = new ArrayList<LLIDropdownPair>();
        ResultSet rs = getResultSetBySqlPair(
                new VPNNetworkLinkConditionBuilder()
                        .selectId()
                        .selectLinkName()
                        .selectLinkBandwidth()
                        .fromVPNNetworkLink()
                        .Where()
                        .validToEquals(Long.MAX_VALUE)
//				.activeToEquals(Long.MAX_VALUE)//changed by bony
                        .activeToGreaterThan(System.currentTimeMillis())
                        .clientIdEquals(clientID)
                        .linkStatusEquals(VPNConstants.Status.VPN_ACTIVE.getStatus())
                        .getSqlPair()
        );
        while(rs.next()) {
            Long ID = rs.getLong(1);
            String label = rs.getString(2);
            Long bandwidth = rs.getLong(3);
            list.add(new LLIDropdownPair(ID, label,bandwidth));
        }
        return list;
    }


}

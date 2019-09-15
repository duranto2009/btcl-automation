package lli.asn;

import common.ClientDTO;
import common.ClientDTOConditionBuilder;
import flow.entity.FlowState;
import lli.LLIClientService;
import login.LoginDTO;
import user.UserDTO;
import user.UserRepository;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

public class ASNDAO {
    public Collection getIDsWithSearchCriteria(Hashtable <String,String>criteria, LoginDTO loginDTO)throws Exception {
        int userId = (int) loginDTO.getUserID();
        ClientDTO clientDTO = new ClientDetailsDTO();
        ResultSet rs = null;
        if (userId == -1) {
            clientDTO = new LLIClientService().getLLIClient(loginDTO.getAccountID());
            if (clientDTO.getClientID() >= 0) {
                criteria.put("clientName", clientDTO.getLoginName());
            }
        }
        rs = getResultSetBySqlPair(
                new ASNConditionBuilder()
                        .selectId()
                        .fromASN()
                        .Where()
                      /*  .clientInSqlPair(
                                new ClientDTOConditionBuilder()
                                        .selectClientID()
                                        .fromClientDTO()
                                        .Where()
                                        .loginNameBothLike(criteria.get("clientName"))
                                        .getNullableSqlPair()
                        )
                        .clientEqualsString(criteria.get("clientID"))
                        .clientEquals(loginDTO.getIsAdmin() ? null : loginDTO.getAccountID())*/
                        .statusEquals(ASNConstant.IS_ACTIVE)
                        .orderByidDesc()
                        .getSqlPair()
        );

        List<Long> idList = new ArrayList<>();
        if (rs != null) {
            idList = getSingleColumnListByResultSet(rs, Long.class);
        }
        return idList;
    }

    List<ASN> getLLIApplicationListByIDList(List<Long> recordIDs) throws Exception{
        return getAllObjectList(ASN.class, new ASNConditionBuilder()
                .Where()
                .idIn(recordIDs)
                .getCondition())
                .stream()
                .sorted(Comparator.comparing(ASN::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

package lli.Application.ownership;

import annotation.DAO;
import annotation.Transactional;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

public class LLIOnProcessConnectionService {

    @DAO
    LLIOnProcessConnectionDAO lliOnProcessConnectionDAO;
    public void insert(LLIOnProcessConnection lliOnProcessConnection)throws Exception {
        ModifiedSqlGenerator.insert(lliOnProcessConnection);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<LLIOnProcessConnection> getConnectionByAppId(long id)throws Exception {
        return lliOnProcessConnectionDAO.getConnectionByAppId(id);
    }


    @Transactional
    public List<LLIConnection> getOnProcessConnectionsByAppId(long appId)throws Exception {
        return  lliOnProcessConnectionDAO.getConnectionByAppId(appId)
                .stream()
                .map(t ->
                {
                    try {
                        return ServiceDAOFactory.getService(LLIFlowConnectionService.class).getConnectionByID(t.getConnection());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter( p-> p!=null)
                .collect(Collectors.toList());
    }
}

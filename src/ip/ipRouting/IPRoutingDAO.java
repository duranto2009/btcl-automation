package ip.ipRouting;

import util.ModifiedSqlGenerator;

public class IPRoutingDAO {

    public void save(IPRoutingInfo routingInfo) throws Exception {
        ModifiedSqlGenerator.insert(routingInfo);
    }

    public IPRoutingInfo getById(long id) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(IPRoutingInfo.class, id);
    }
}

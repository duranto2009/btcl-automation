package common.bill;

import util.DatabaseConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static util.ModifiedSqlGenerator.*;

public class BillCheckDAO {
    private final Class <BillDTO> classObject = BillDTO.class;

    public List<BillDTO> checkBillByClientByModule(long clientId, long fromDate, long toDate, int moduleId)throws Exception {
        String sql = " SELECT * from " + getTableName(classObject)
                + " WHERE "
                + getColumnName(classObject, "clientID") + " = ? "
                + " AND "
                + getColumnName(classObject, "generationTime") + " >= ? "
                + " AND "
                + getColumnName(classObject, "generationTime") + " <= ? "
                + " AND "
                + "floor( "+getColumnName(classObject, "entityTypeID") + " / 100.0 ) = ? "
                + " AND ("
                + getColumnName(classObject, "paymentStatus") + " =  ?  "
                + " OR "
                + getColumnName(classObject, "paymentStatus") + " =  ?  )";

        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
        ps.setLong(1, clientId);
        ps.setLong(2, fromDate);
        ps.setLong(3, toDate);
        ps.setInt(4, moduleId);
        ps.setInt(5, BillDTO.UNPAID);
        ps.setInt(6, BillDTO.PAID_UNVERIFIED);

        ResultSet rs = ps.executeQuery();
        return  getObjectListByResultSet(classObject, rs);
    }

}

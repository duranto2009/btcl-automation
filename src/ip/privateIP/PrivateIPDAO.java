package ip.privateIP;

import util.ModifiedSqlGenerator;

import java.util.Hashtable;
import java.util.List;

public class PrivateIPDAO {

    Class<? extends PrivateIPBlock> classObject = PrivateIPBlock.class;

    public void addNewPrivateIP(PrivateIPBlock block) throws Exception {
        ModifiedSqlGenerator.insert(block);
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> searchCriteria) {
        String[] keys = new String[]{"SubRegion", "isDeleted", "activeTo"};
        String[] operators = new String[]{"=", "=", "="};
        String[] columnNames = new String[]{"subRegionId", "isDeleted", "activeTo"};

        return null;
    }

    public List<PrivateIPBlock> getDTOs(List<Long> recordIDs) {
        return null;
    }

    public List<PrivateIPBlock> getPrivateIPsBySubRegionId(long subRegionId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(PrivateIPBlock.class, " Where " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "subRegionId") +
                " = " +
                subRegionId);
    }

    public List<PrivateIPBlock> getPrivateIPsByModuleId(int moduleId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(PrivateIPBlock.class, " Where " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "moduleId") + " = " +
                moduleId);
    }

    //get it checked
    public void deletePrivateIPsBySubRegionId(long subRegionId) throws Exception {
        List<PrivateIPBlock> toBeDeletedIPs = ModifiedSqlGenerator.getAllObjectList(PrivateIPBlock.class, " Where " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "subRegionId") +
                " = " +
                subRegionId);

        for (PrivateIPBlock privateIPBlock : toBeDeletedIPs) {
            privateIPBlock.setDeleted(true);
            ModifiedSqlGenerator.updateEntity(privateIPBlock);
        }
    }

    public void deletePrivateIPsByModuleId(int moduleId) throws Exception {
        List<PrivateIPBlock> toBeDeletedIPs = ModifiedSqlGenerator.getAllObjectList(PrivateIPBlock.class, " Where " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "moduleId") + " = " +
                moduleId);
        for (PrivateIPBlock privateIPBlock : toBeDeletedIPs) {
            privateIPBlock.setDeleted(true);
            ModifiedSqlGenerator.updateEntity(privateIPBlock);
        }
    }

    public void deletePrivateIPsById(long id) throws Exception {
        ModifiedSqlGenerator.deleteEntityByID(PrivateIPBlock.class, id);
    }

    public List<PrivateIPBlock> getPrivateIPsBySubRegionIdAndModuleId(long subRegionId, int moduleId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(PrivateIPBlock.class, " Where " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "subRegionId") +
                " = " +
                subRegionId
                + " and " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "moduleId") +
                " = " +
                moduleId +
                " and " +
                ModifiedSqlGenerator.getColumnName(PrivateIPBlock.class, "isDeleted") +
                " = " +
                0);
    }
}

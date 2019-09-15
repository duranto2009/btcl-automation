package officialLetter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.RequestFailureException;
import connection.DatabaseConnection;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import user.UserDTO;
import user.UserRepository;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

@Log4j
public class OfficialLetterDAO {
     void insertDocumentConcern(OfficialLetterConcern concern) {
        try {
            ModifiedSqlGenerator.insert(concern);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     void insertOfficialLetter(OfficialLetter adviceNote) throws Exception {
        DatabaseConnection databaseConnection = DatabaseConnectionFactory.getCurrentDatabaseConnection();
        String sql = "INSERT INTO official_letter(id, module_id, client_id, class_name, app_id, official_letter_type, creation_time, last_modification_time, is_deleted)" +
                "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
        long primaryKey = databaseConnection.getNextID(getTableName(OfficialLetter.class));
        ps.setObject(1, primaryKey);
        ps.setObject(2, adviceNote.getModuleId());
        ps.setObject(3, adviceNote.getClientId());
        ps.setObject(4, adviceNote.getClassName());
        ps.setObject(5, adviceNote.getApplicationId());
        ps.setObject(6, adviceNote.getOfficialLetterType().name());
        ps.setObject(7, adviceNote.getCreationTime());
        ps.setObject(8, adviceNote.getLastModificationTime());
        ps.setObject(9, adviceNote.isDeleted());
        log.info(ps);
        ps.execute();
        ps.close();
        updateSequencerTable(OfficialLetter.class);
        adviceNote.setId(primaryKey);

    }

    public List<Long> getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO) throws Exception {
        searchCriteria.put("isDeleted", "0");

        String[] keys = 		{"Official letter type","clientId", "isDeleted", "fromDate"    , "toDate"      , "Module"   , "officialLetterId"    };
        String[] operators = 	{"LIKE"			       ,"IN"	    , "="        , ">="          , "<="          , "="       , "LIKE"     };
        String[] dtos = 		{"officialLetterType"  ,"clientId"  , "isDeleted", "creationTime", "creationTime", "moduleId", "officialLetterId"	  };
        String fixedCondition;
        UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID());
        String specificUsers = getPrimaryKeyColumnName(OfficialLetter.class) + " IN ("
                + " SELECT " + getColumnName(OfficialLetterConcern.class, "officialLetterId")
                + " FROM " + getTableName(OfficialLetterConcern.class)
                + " WHERE " + getColumnName(OfficialLetterConcern.class, "recipientId")
                + " = "
                + loginDTO.getUserID()
                +")";

        //CDGM and admin
        if(userDTO.getRoleID() == 1 || userDTO.getRoleID() == 22021) {
           fixedCondition = "";
        }else {
            fixedCondition = specificUsers;
        }
        List<Long> ids = getIDListFromSearchCriteria(OfficialLetter.class, keys, operators, dtos, searchCriteria, fixedCondition);
        return  ids.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<OfficialLetter> getDTOs(List<Long> recordIDs) throws Exception {
        return ModifiedSqlGenerator.getObjectListByIDList(OfficialLetter.class, recordIDs)
                .stream()
                .sorted(Comparator.comparing(OfficialLetter::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());

    }

     List<OfficialLetterConcern> getOfficialConcernsByOfficialLetterId(long id) throws Exception {
        String conditionString = " WHERE " +
                getColumnName(OfficialLetterConcern.class, "officialLetterId") +
                " = " + id;
        return ModifiedSqlGenerator.getAllObjectList(OfficialLetterConcern.class, conditionString);
    }

     List<OfficialLetter> getOfficialLettersByAppIdAndTypeAndModule(long id, OfficialLetterType type, int moduleId) throws Exception {
        String conditionString = " WHERE "
                + getColumnName(OfficialLetter.class, "applicationId") + " = " + id
                + " and "
                + getColumnName(OfficialLetter.class, "officialLetterType") + " = '" + type + "'"
                + " and "
                + getColumnName(OfficialLetter.class, "moduleId") + " = '" +  moduleId + "' ";

        return   ModifiedSqlGenerator.getAllObjectList(OfficialLetter.class, conditionString);
    }

     List<OfficialLetter> getOfficialLettersByAppIdAndType(long appId, OfficialLetterType type) throws Exception {
        List<OfficialLetter>list = ModifiedSqlGenerator.getAllObjectList(OfficialLetter.class,
                " WHERE "
                        + getColumnName(OfficialLetter.class, "applicationId") + " = " + appId
                        + " AND "
                        + getColumnName(OfficialLetter.class, "officialLetterType") + " LIKE '" + type + "'"
        );
        return list;

    }
     long getSenderByAppIdAndVendorIdAndOfficialLetterType(long applicationId, int vendorId, OfficialLetterType type) throws Exception {
        List<OfficialLetter>list = getOfficialLettersByAppIdAndType(applicationId, type);
        List<OfficialLetterConcern> concerns = new ArrayList<>();
        for(OfficialLetter ol: list){
            concerns.addAll(getOfficialConcernsByOfficialLetterId(ol.getId()));
        }
        OfficialLetterConcern oc = concerns.stream().filter(
                t->t.getRecipientId() == vendorId
                && (t.getRecipientType() == RecipientType.VENDOR || t.getRecipientType() == RecipientType.BTCL_OFFICIAL)
                && t.getReferType() == ReferType.TO
        ).findFirst().orElse(null);
        if(oc == null){
            throw  new RequestFailureException( " No Recipient Vendor Found ");

        }
        return oc.getSenderId();
    }

     JsonArray getOfficialLettersByApplicationIdAndModuleId(long appId, int moduleId) throws Exception {
        Class<OfficialLetter> officialLetterClass = OfficialLetter.class;
        Class<OfficialLetterConcern> officialLetterConcernClass = OfficialLetterConcern.class;

        String sql = "Select ol." + getColumnName(officialLetterClass, "officialLetterType") + " as olType,"
                + " ol." + getColumnName(officialLetterClass, "applicationId") + " as olAppId,"
                + " olc." + getColumnName(officialLetterConcernClass, "referType")  + " as olcReferType,"
                + " olc." + getColumnName(officialLetterConcernClass, "recipientId") + " as olcRecipientId,"
                + " olc." + getColumnName(officialLetterConcernClass, "recipientType") + " as olcRecipientType"
                + " from " + getTableName(officialLetterClass) + " ol, " + getTableName(officialLetterConcernClass) + " olc"
                + " where"
                + " ol." + getColumnName(officialLetterClass, "applicationId") + " = ?"
                + " and"
                + " ol." + getColumnName(officialLetterClass, "moduleId") + " = ?"
                + " and"
                + " ol." + getColumnName(officialLetterClass, "id") + " = " + "olc." + getColumnName(officialLetterConcernClass, "officialLetterId")
                + " and"
                + " ol." + getColumnName(officialLetterClass, "isDeleted") + "=0"
                + " and"
                + " olc." + getColumnName(officialLetterClass, "isDeleted") + "=0";
            log.info(sql);
        PreparedStatement ps = DatabaseConnectionFactory.getCurrentDatabaseConnection().getNewPrepareStatement(sql);
        ps.setObject(1, appId);
        ps.setObject(2, moduleId);
        ResultSet rs = ps.executeQuery();

        JsonArray jsonArray = new JsonArray();
        while(rs.next()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", rs.getString("olType"));
            jsonObject.addProperty("appId", rs.getLong("olAppId"));
            jsonObject.addProperty("referType", rs.getString("olcReferType"));
            jsonObject.addProperty("recipientId", rs.getLong("olcRecipientId"));
            jsonObject.addProperty("recipientType", rs.getString("olcRecipientType"));
            jsonArray.add(jsonObject);
        }
        return jsonArray;

    }

    public OfficialLetter getOfficialLetterById(long officialLetterId) throws Exception{
        return ModifiedSqlGenerator.getObjectByID(OfficialLetter.class,officialLetterId);
    }
}

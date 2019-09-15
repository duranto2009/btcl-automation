package inventory;

import static util.SqlGenerator.getColumnName;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import annotation.DAO;
import global.GlobalService;
import org.apache.log4j.Logger;

import com.google.common.base.Objects;

import annotation.Transactional;
import common.CategoryConstants;
import common.CommonActionStatusDTO;
import common.EntityTypeConstant;
import common.RequestFailureException;
import common.StringUtils;
import common.repository.AllClientRepository;
import connection.DatabaseConnection;
import inventory.repository.InventoryRepository;
import requestMapping.Service;
import util.*;

public class InventoryService {
    Logger logger = Logger.getLogger(getClass());
    static Integer mutexLock = new Integer(0);

    InventoryDAO inventoryDAO = new InventoryDAO();

    @DAO
    InventoryDAO inventoryDAO2;


    // if some inventory item is not necessary then set that value to 0;
    // i.e. if district not necessary  essentialData.setDistrict(0)

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Integer, String> getInventoryItemsByInventoryEssentialData (InventoryEssentialData inventoryEssentialData) throws Exception {
        Class<InventoryItem> classObject = InventoryItem.class;
        String idColumnName = ModifiedSqlGenerator.getColumnName(classObject, "ID");

        String districtCondition = idColumnName + " = " + inventoryEssentialData.getDistrictId();
        String popCondition = idColumnName + " = " + inventoryEssentialData.getPopId();
        String rsCondition = idColumnName + " = " + inventoryEssentialData.getRsId();
        String portCondition = idColumnName + " = " + inventoryEssentialData.getPortId();
        String vlanCondition = idColumnName + " = " + inventoryEssentialData.getVlanId();


        String conditionString = " WHERE " + districtCondition + " OR " + popCondition + " OR " + rsCondition + " OR " + portCondition + " OR " + vlanCondition;
        List<InventoryItem> inventoryItems = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(classObject, conditionString);

        return inventoryItems.stream()
                .collect(Collectors.toMap(InventoryItem::getInventoryCatagoryTypeID, InventoryItem::getName));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryItem> getInventoryItemListByItemNameCategoryIDParentID(String itemName, int categoryID, Long parentID) throws Exception {
        return inventoryDAO.getInventoryItemListByItemNameCategoryIDParentID(itemName, categoryID, parentID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryItem> getConnectedRoutersBySwitchID(long switchID) throws Exception {

        InventoryItem inventoryItem = inventoryDAO.getInventoryItemByItemID(switchID
                , DatabaseConnectionFactory.getCurrentDatabaseConnection());
        return getConnectedRoutersBySwichIDAndVisitSet(inventoryItem, new HashSet<>());
    }

    private List<InventoryItem> getConnectedRoutersBySwichIDAndVisitSet(InventoryItem switchItem
            , Set<Long> visitSet) throws Exception {
        if (visitSet.contains(switchItem.getID())) {
            return Collections.emptyList();
        }
        visitSet.add(switchItem.getID());

        int categoryIDOfPort = 99;

        List<InventoryItem> ports = getInventoryItemListForAutocomplete(categoryIDOfPort
                , switchItem.getID(), "", null, null, false, true);

        if (ports.isEmpty()) {
            Collections.emptyList();
        }

        List<Long> connectedPortIDs = new ArrayList<>();

        for (InventoryItem port : ports) {
            if (Objects.equal(port.occupierEntityTypeID, 9999)) {
                connectedPortIDs.add(port.occupierEntityID);
            }
        }

        List<InventoryItem> connectedPorts = inventoryDAO.getInventoryItemListByInventoryItemIDList(connectedPortIDs
                , DatabaseConnectionFactory.getCurrentDatabaseConnection());

        List<Long> connectedRouterIDs = new ArrayList<>();

        for (InventoryItem connectedPort : connectedPorts) {
            if (connectedPort.getParentID() != null) {
                connectedRouterIDs.add(connectedPort.getParentID());
            }
        }
        List<InventoryItem> result = new ArrayList<>();
        List<InventoryItem> connectedItems = inventoryDAO.getInventoryItemListByInventoryItemIDList(connectedRouterIDs
                , DatabaseConnectionFactory.getCurrentDatabaseConnection());


        List<InventoryAttributeValue> switchRouterAttributes = inventoryDAO.getInventoryAttributeValueListByItemIDList(connectedRouterIDs
                , DatabaseConnectionFactory.getCurrentDatabaseConnection());

        Map<Long, String> mapOfAttributeValueToItemID = new HashMap<>();

        for (InventoryAttributeValue inventoryAttributeValue : switchRouterAttributes) {
            if (inventoryAttributeValue.getInventoryAttributeNameID() == 73010) {
                mapOfAttributeValueToItemID.put(inventoryAttributeValue.getInventoryItemID(), inventoryAttributeValue.getValue());
            }
        }

        for (InventoryItem connectedRouterSwitch : connectedItems) {
            if (StringUtils.toUpperCase(mapOfAttributeValueToItemID.get(connectedRouterSwitch.getID()))
                    .equals("ROUTER")) {
                result.add(connectedRouterSwitch);
            } else {
                result.addAll(getConnectedRoutersBySwichIDAndVisitSet(connectedRouterSwitch, visitSet));
            }
        }

        return result;
    }

    private InventoryAttributeName createInventoryAttributeName(Integer catagoryID, String attributeName, int index,
                                                                long lastModificationTime) {
        InventoryAttributeName inventoryAttributeName = new InventoryAttributeName();
        inventoryAttributeName.setName(attributeName);
        inventoryAttributeName.setInventoryCatagoryTypeID(catagoryID);
        inventoryAttributeName.setDeleted(false);
        inventoryAttributeName.setLastModificationTime(lastModificationTime);
        inventoryAttributeName.setIndexNumber(index);
        return inventoryAttributeName;
    }

    private boolean matches(InventoryItemDetails inventoryItemDetails,
                            Map<Long, String> mapOfMatchingStringToAttributeValueID) {

        for (InventoryAttributeValue inventoryAttributeValue : inventoryItemDetails.getInventoryAttributeValues()) {
            String matchingString = mapOfMatchingStringToAttributeValueID.get(inventoryAttributeValue.getID());
            if (matchingString != null && !matchingString.trim().equals("")
                    && !inventoryAttributeValue.value.contains(matchingString)) {
                return false;
            }
        }

        return true;
    }

    public List<InventoryItemDetails> getInvenotryItemDetailsByParentItemIDAndCatagoryID(Long parentItemID,
                                                                                         int catagoryID) {
        return getInvenotryItemDetailsByParentItemIDAndCatagoryIDWithMatchingCriteria(parentItemID, catagoryID,
                new HashMap<Long, String>());
    }

    public List<InventoryItemDetails> getInvenotryItemDetailsByParentItemIDAndCatagoryIDWithMatchingCriteria(
            Long parentItemID, int catagoryID, Map<Long, String> mapOfMatchingStringToAttributeValueID) {
        List<InventoryItemDetails> inventoryItemDetailsList = new ArrayList<InventoryItemDetails>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            List<InventoryItem> inventoryItems = inventoryDAO
                    .getInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID, databaseConnection);

            for (InventoryItem inventoryItem : inventoryItems) {
                List<InventoryAttributeValue> inventoryAttributeValues = inventoryDAO
                        .getInventoryAttributeValueListByItemID(inventoryItem.getID(), databaseConnection);
                InventoryItemDetails inventoryItemDetails = new InventoryItemDetails();
                inventoryItemDetails.setInventoryAttributeValues(inventoryAttributeValues);
                inventoryItemDetails.setInventoryItem(inventoryItem);
                if (matches(inventoryItemDetails, mapOfMatchingStringToAttributeValueID)) {
                    inventoryItemDetailsList.add(inventoryItemDetails);
                }
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }

        return inventoryItemDetailsList;
    }

    private List<Long> getInventoryAttributeNameIDListForDeletion(
            List<InventoryAttributeName> inventoryAttributeNames) {
        Set<Long> inventoryAttributeNameIDSet = new HashSet<Long>();

        for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
            inventoryAttributeNameIDSet.add(inventoryAttributeName.getID());
        }
        int categoryID = inventoryAttributeNames.get(0).getInventoryCatagoryTypeID();
        List<InventoryAttributeName> existingInventoryAttributeNames = InventoryRepository.getInstance()
                .getInventoryAttributeNameListByCatagoryID(categoryID);

        List<Long> IDListForDeletion = new ArrayList<Long>();

        for (InventoryAttributeName inventoryAttributeName : existingInventoryAttributeNames) {
            Long existingAttributeNameID = inventoryAttributeName.getID();
            if (!inventoryAttributeNameIDSet.contains(existingAttributeNameID)) {
                IDListForDeletion.add(existingAttributeNameID);
            }
        }

        return IDListForDeletion;
    }

    private void deleteAttributeNameListByIDList(List<Long> attributeNameIDList, DatabaseConnection databaseConnection)
            throws Exception {
        // delete attribute name

        // delete attribute value
    }

    private void addInventoryAttributeName(InventoryAttributeName inventoryAttributeName,
                                           DatabaseConnection databaseConnection) throws Exception {
        // add attribute name
        inventoryDAO.addInventoryAttributeName(inventoryAttributeName, databaseConnection);
        // add attribute value with default value if any inventory item exists
        // of that type
    }

    public void updateInventoryCategoryDetails(List<InventoryAttributeName> inventoryAttributeNames) throws Exception {

        for (int i = 1; i < inventoryAttributeNames.size(); i++) {
            InventoryAttributeName prevAttributeName = inventoryAttributeNames.get(i - 1);
            InventoryAttributeName nextAttributeName = inventoryAttributeNames.get(i);
            if (!prevAttributeName.getInventoryCatagoryTypeID()
                    .equals(nextAttributeName.getInventoryCatagoryTypeID())) {
                throw new RequestFailureException("Invalid input. Category ID of all attribute names do not match");
            }
        }

        long currentTime = System.currentTimeMillis();

        for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
            inventoryAttributeName.setLastModificationTime(currentTime);
            inventoryAttributeName.setDeleted(false);
        }

        // add attribute name and attribute values with default value if
        // specified
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            // find delete attribute name ids
            List<Long> attributeNameIDListForDeletetion = getInventoryAttributeNameIDListForDeletion(
                    inventoryAttributeNames);
            // delete those attribute names and corresponding attribute values
            deleteAttributeNameListByIDList(attributeNameIDListForDeletetion, databaseConnection);
            // update attribute names

            for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
                if (inventoryAttributeName.getID() == 0) {
                    addInventoryAttributeName(inventoryAttributeName, databaseConnection);
                } else {
                    inventoryDAO.updateInventoryAttributeName(inventoryAttributeName, databaseConnection);
                }
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateInventoryAttributeName(Integer catagoryID, List<String> nameList) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            List<InventoryAttributeName> inventoryAttributeNames = new ArrayList<InventoryAttributeName>();
            long lastModificationTime = System.currentTimeMillis();

            inventoryDAO.deleteInventoryAttributeByCatagoryID(catagoryID, databaseConnection, lastModificationTime);

            for (int i = 0; i < nameList.size(); i++) {
                inventoryAttributeNames
                        .add(createInventoryAttributeName(catagoryID, nameList.get(i), i, lastModificationTime));
            }

            for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
                inventoryDAO.addInventoryAttributeName(inventoryAttributeName, databaseConnection);
            }
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addCatagoryList(List<InventoryCatagoryType> inventoryCatagoryTypeList) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (InventoryCatagoryType inventoryCatagoryType : inventoryCatagoryTypeList) {
                inventoryDAO.addInventoryCatagory(inventoryCatagoryType, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
    }

    private boolean isUsedAsChildCategory(Integer inventoryCategoryID, DatabaseConnection databaseConnection)
            throws Exception {
        if (inventoryCategoryID == null) {
            return false;
        }

        if (inventoryDAO.hasChildOfInventoryCategory(inventoryCategoryID, databaseConnection)) {
            return false;
        }
        if (inventoryDAO.hasAnyInventoryItemOfCategory(inventoryCategoryID, databaseConnection)) {
            return false;
        }

        return true;
    }

    public void addCatagory(InventoryCatagoryType inventoryCatagoryType) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            Integer parentCategoryTypeID = inventoryCatagoryType.getParentCatagoryTypeID();

            if (isUsedAsChildCategory(parentCategoryTypeID, databaseConnection)) {
                throw new RequestFailureException("New category additon failed bacause "
                        + "parent category is already used as child category");
            }

            inventoryDAO.addInventoryCatagory(inventoryCatagoryType, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
    }

    private void isValidAddRequest(int categoryID, Long parentItemID, String[] attributeValues, long[] attributeNameIDs,
                                   String itemName, DatabaseConnection databaseConnection) throws Exception {

        if (attributeValues.length != attributeNameIDs.length) {
            throw new RequestFailureException("Number of attribuetValues (" + attributeValues.length
                    + ") and number of attribute name id(" + attributeNameIDs.length + ") does not match");
        }

        InventoryCatagoryType inventoryCatagoryType = InventoryRepository.getInstance()
                .getInventoryCatgoryTypeByCatagoryID(categoryID);
        if (inventoryCatagoryType == null || inventoryCatagoryType.isDeleted()) {
            throw new RequestFailureException("Not inventory category found with categoryID = " + categoryID);
        }

        InventoryItem parentItem = null;
        if (parentItemID == null && inventoryCatagoryType.getParentCatagoryTypeID() != null) {
            throw new RequestFailureException("Invalid parent item is selected");
        } else if (parentItemID != null) {
            parentItem = inventoryDAO.getInventoryItemByItemID(parentItemID, databaseConnection);
        }

        if (parentItemID != null && (parentItem == null || parentItem.isDeleted())) {
            throw new RequestFailureException("No parent item found with ID " + parentItemID);
        }

        if (parentItem != null) {
            if (!parentItem.getInventoryCatagoryTypeID().equals(inventoryCatagoryType.getParentCatagoryTypeID())) {
                throw new RequestFailureException(
                        "Parent item with id = " + parentItemID + " should be a item of category with category id = "
                                + inventoryCatagoryType.getParentCatagoryTypeID() + " while it is of category id "
                                + parentItem.getInventoryCatagoryTypeID());
            }
        }
        int numberOfAttribueInCategory = InventoryRepository.getInstance()
                .getInventoryAttributeNameListByCatagoryID(categoryID).size();

        if (numberOfAttribueInCategory != attributeNameIDs.length) {
            throw new RequestFailureException("Number of attribue name should be " + numberOfAttribueInCategory
                    + " while the number is " + attributeNameIDs.length);
        }
        for (long inventoryAttributeNameID : attributeNameIDs) {
            InventoryAttributeName inventoryAttributeName = InventoryRepository.getInstance()
                    .getInventoryAttributeNameByNameID(inventoryAttributeNameID);
            if (inventoryAttributeName == null || inventoryAttributeName.isDeleted()) {
                throw new Exception("No valid attribute name found with ID " + inventoryAttributeNameID);
            }

            if (inventoryAttributeName.getInventoryCatagoryTypeID() != categoryID) {
                throw new RequestFailureException(
                        "The requested attribue name shoud be a attribue of a category with id " + categoryID
                                + " while it is " + inventoryAttributeName.getInventoryCatagoryTypeID());
            }
        }

        if (StringUtils.isBlank(itemName)) {
            throw new RequestFailureException("Name can not be empty");
        }

        Set<String> singleNames = new HashSet<>();

        for (String singleItemName : itemName.split(";", -1)) {
            if (!singleNames.add(singleItemName)) {
                throw new RequestFailureException(singleItemName + " is duplicate");
            }
            if (StringUtils.isBlank(singleItemName)) {
                throw new RequestFailureException("Consecutive semicolons (with or without space) are not allowed");
            }
        }
        inventoryDAO.validateItemForInsert(parentItemID, categoryID, itemName.split(";", -1),
                attributeValues, attributeNameIDs, databaseConnection);


    }

    private List<InventoryAttributeValue> createInventoryAttributeValueList(String[] attributeValues, long[] attributeNameIDs, long lastModificationTime, long inventoryItemID) {
        List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<InventoryAttributeValue>();
        int ind = 0;
        for (long attributeNameID : attributeNameIDs) {
            InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
            inventoryAttributeValue.setInventoryAttributeNameID(attributeNameID);
            inventoryAttributeValue.setValue(attributeValues[ind++]);
            inventoryAttributeValue.setLastModificationTime(lastModificationTime);
            inventoryAttributeValue.setInventoryItemID(inventoryItemID);
            // inventoryAttributeValues.add()
        }
        return inventoryAttributeValues;
    }

    public void updateInventoryItem(long inventoryItemID, String itemName, Boolean isUsed, Long occupierEntityID, Integer occupierEntityTypeID, String[] attributeValues,
                                    long[] attributeIDs) throws Exception {
        long lastModificationTime = System.currentTimeMillis();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            if (itemName.contains(";")) {
                throw new RequestFailureException("Inventory item name can not contain semicolon");
            }


            InventoryItem inventoryItem = inventoryDAO.getInventoryItemByItemID(inventoryItemID, databaseConnection);

//			//Shift Inventory
//			if(parentItemID > 0) {
//				InventoryItem newParentItem = getInventoryItemByItemID(parentItemID);
//				InventoryItem oldParentItem = getInventoryItemByItemID(inventoryItem.getParentID());
//				if(oldParentItem.inventoryCatagoryTypeID.equals(newParentItem.inventoryCatagoryTypeID)) {
//					inventoryItem.setParentID(parentItemID);
//				}
//			}


            List<InventoryAttributeName> inventoryAttributeNames = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(inventoryItem.getInventoryCatagoryTypeID());

            long[] attributeNameIDs = new long[inventoryAttributeNames.size()];

            for (int i = 0; i < inventoryAttributeNames.size(); i++) {
                attributeNameIDs[i] = inventoryAttributeNames.get(i).getID();
            }
            if (attributeValues == null) {
                attributeValues = new String[0];
            }

            inventoryDAO.validateItemForUpdate(inventoryItem.getParentID(),
                    inventoryItem.getInventoryCatagoryTypeID(), itemName, inventoryItemID, attributeValues, attributeNameIDs, databaseConnection);
			
			
			/*
			if( inventoryItem.isChildItem() && inventoryItem.getIsUsed() && inventoryItem.getOccupierEntityTypeID()/EntityTypeConstant.MULTIPLIER2 == 99) {
				InventoryItem occupierInventoryItem = getInventoryItemByItemID(inventoryItem.getOccupierEntityID());
				occupierInventoryItem.setIsUsed(false);
				occupierInventoryItem.setOccupierEntityID(null);
				occupierInventoryItem.setOccupierEntityTypeID(null);
				inventoryDAO.updateInventoryItem(occupierInventoryItem, databaseConnection);
			}
			*/


            inventoryItem.setID(inventoryItemID);
            inventoryItem.setName(itemName);
            inventoryItem.setLastModificationTime(lastModificationTime);

            if (inventoryItem.isChildItem()) {
                if ((inventoryItem.getIsUsed() && inventoryItem.getOccupierEntityTypeID() / EntityTypeConstant.MULTIPLIER2 == 99) || !inventoryItem.getIsUsed()) {
                    inventoryItem.setIsUsed(isUsed);
                    inventoryItem.setOccupierEntityID(occupierEntityID);
                    inventoryItem.setOccupierEntityTypeID(occupierEntityTypeID);

                    SqlGenerator.updateEntityByPropertyList(inventoryItem, InventoryItem.class, databaseConnection, false, false,
                            new String[]{"name", "lastModificationTime", "isUsed", "occupierEntityID", "occupierEntityTypeID"});

                    if (occupierEntityID != null) {
                        InventoryItem newOccupierInventoryItem = getInventoryItemByItemID(occupierEntityID);
                        if (newOccupierInventoryItem.getIsUsed()) {
                            throw new RequestFailureException(newOccupierInventoryItem.getName() + " is already used.");
                        }
                        newOccupierInventoryItem.setIsUsed(true);
                        newOccupierInventoryItem.setOccupierEntityID(inventoryItemID);
                        newOccupierInventoryItem.setOccupierEntityTypeID(EntityTypeConstant.INVENTORY_PORT);
                        inventoryDAO.updateInventoryItem(newOccupierInventoryItem, databaseConnection);
                    }
                }
            } else {
                SqlGenerator.updateEntityByPropertyList(inventoryItem, InventoryItem.class, databaseConnection, false, false,
                        new String[]{"name", "lastModificationTime"});
            }

            for (int i = 0; (attributeIDs != null && i < attributeIDs.length); i++) {
                long attributeNameID = attributeIDs[i];
                String attributeVale = attributeValues[i];
                InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
                inventoryAttributeValue.setID(attributeNameID);
                inventoryAttributeValue.setValue(attributeVale);
                inventoryAttributeValue.setLastModificationTime(lastModificationTime);

                logger.debug("inventoryAttributeValue" + inventoryAttributeValue);
                if (inventoryAttributeValue.getID() == -1) { // will be handled
                    // latter
                    // insert a new for the first time
                    /*
                     * InventoryAttributeValue inventoryAttributeValueNew = new
                     * InventoryAttributeValue();
                     * inventoryAttributeValueNew.setValue(attributeVale);
                     * inventoryAttributeValueNew.setLastModificationTime(
                     * lastModificationTime);
                     * SqlGenerator.insert(inventoryAttributeValue,
                     * InventoryAttributeValue.class, databaseConnection, true);
                     */
                } else {
                    // update old value
                    SqlGenerator.updateEntityByPropertyList(inventoryAttributeValue, InventoryAttributeValue.class,
                            databaseConnection, false, false, new String[]{"value", "lastModificationTime"});
                }
                logger.debug("Entity attribute is updated");
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.debug("fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }

    }


    public List<InventoryItem> addInventoryItem(int catagoryID, Long parentItemID, String[] attributeValues,
                                                long[] attributeNameIDs, String itemName) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<InventoryItem> inventoryItems = new ArrayList<>();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            if (attributeNameIDs == null) {
                attributeNameIDs = new long[0];
            }
            if (attributeValues == null) {
                attributeValues = new String[0];
            }
            isValidAddRequest(catagoryID, parentItemID, attributeValues, attributeNameIDs, itemName, databaseConnection);
            long currentTime = System.currentTimeMillis();

            for (String singleItemName : itemName.split(";")) {

                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setInventoryCatagoryTypeID(catagoryID);
                inventoryItem.setParentID(parentItemID);
                inventoryItem.setDeleted(false);
                inventoryItem.setLastModificationTime(currentTime);
                inventoryItem.setName(singleItemName);

                if (!inventoryDAO.hasChildOfInventoryCategory(catagoryID, databaseConnection)) {
                    inventoryItem.setChildItem(true);
                    inventoryItem.setIsUsed(false);
                } else {
                    inventoryItem.setChildItem(false);
                    inventoryItem.setIsUsed(null);
                }

                inventoryDAO.addInventoryItem(inventoryItem, databaseConnection);
                inventoryItems.add(inventoryItem);

                List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<InventoryAttributeValue>();
                long itemID = inventoryItem.getID();
                int ind = 0;
                for (String attributeValue : attributeValues) {


                    InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
                    long inventoryAttributeNameID = attributeNameIDs[ind++];


                    InventoryAttributeName inventoryAttributeName = InventoryRepository.getInstance()
                            .getInventoryAttributeNameByNameID(inventoryAttributeNameID);


                    if (!attributeValue.matches(inventoryAttributeName.getRegexOfAttributeType())) {
                        throw new RequestFailureException("Inventory attribute value doest not match "
                                + "with the provied regular expression");
                    }

                    inventoryAttributeValue.setInventoryAttributeNameID(inventoryAttributeNameID);
                    inventoryAttributeValue.setInventoryItemID(itemID);
                    inventoryAttributeValue.setDeleted(false);
                    inventoryAttributeValue.setLastModificationTime(currentTime);
                    inventoryAttributeValue.setValue(attributeValue);

                    inventoryAttributeValues.add(inventoryAttributeValue);
                }

                for (InventoryAttributeValue inventoryAttributeValue : inventoryAttributeValues) {
                    inventoryDAO.addInventoryValue(inventoryAttributeValue, databaseConnection);
                }
            }


            databaseConnection.dbTransationEnd();
            // catch
        } catch (Exception ex) {
            logger.debug("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
        return inventoryItems;
        //return inventoryItem;
    }

    public void updateCatagory(InventoryCatagoryType inventoryCatagoryType) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.updateInventoryCatagory(inventoryCatagoryType, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    private boolean isDeleteable(InventoryCatagoryTreeNode treeNode, DatabaseConnection databaseConnection)
            throws Exception {

        long catagoryID = treeNode.getInventoryCatagoryDetailsRootNode().getInventoryCatagoryType().getID();
        if (inventoryDAO.checkInventoryItemByCatagoryID(catagoryID, databaseConnection)) {
            return false;
        }
        for (InventoryCatagoryTreeNode childTreeNode : treeNode.getInventoryCatagoryDetailsChildNodes()) {
            if (!isDeleteable(childTreeNode, databaseConnection)) {
                return false;
            }
        }
        return true;

    }

    private void deleteTreeNode(InventoryCatagoryTreeNode treeNode, DatabaseConnection databaseConnection,
                                long lastModificationTime) throws Exception {
        InventoryCatagoryType inventoryCatagoryType = treeNode.getInventoryCatagoryDetailsRootNode()
                .getInventoryCatagoryType();
        inventoryCatagoryType.isDeleted = true;
        inventoryCatagoryType.lastModificationTime = lastModificationTime;
        inventoryDAO.updateInventoryCatagory(inventoryCatagoryType, databaseConnection);
        for (InventoryAttributeName inventoryAttributeName : treeNode.getInventoryCatagoryDetailsRootNode()
                .getInventoryAttributeNameList()) {
            inventoryAttributeName.isDeleted = true;
            inventoryAttributeName.lastModificationTime = lastModificationTime;
            inventoryDAO.updateInventoryAttributeName(inventoryAttributeName, databaseConnection);
        }
        for (InventoryCatagoryTreeNode childNode : treeNode.getInventoryCatagoryDetailsChildNodes()) {
            deleteTreeNode(childNode, databaseConnection, lastModificationTime);
        }
    }

    public void deleteTreeNodeInCasecadeModeByCatagoryID(int catagoryID) throws Exception {
        InventoryCatagoryTreeNode treeNode = InventoryRepository.getInstance()
                .getInventoryCatagoryTreeNodeByCatagoryID(catagoryID);
        if (treeNode == null) {
            throw new Exception("No catagory found by catagory ID " + catagoryID);
        }
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            if (!isDeleteable(treeNode, databaseConnection)) {
                throw new Exception("Delete request is not possible . Because inventory item exists.");
            }
            long lastModificationTime = System.currentTimeMillis();
            deleteTreeNode(treeNode, databaseConnection, lastModificationTime);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw new Exception("Delete request is not possible . Because inventory item exists.");
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addItemList(List<InventoryItem> inventoryItemList) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (InventoryItem inventoryItem : inventoryItemList) {
                inventoryDAO.addInventoryItem(inventoryItem, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addInventoryItemDetails(InventoryItemDetails inventoryItemDetails) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.addInventoryItem(inventoryItemDetails.getInventoryItem(), databaseConnection);
            for (InventoryAttributeValue inventoryAttributeValue : inventoryItemDetails.getInventoryAttributeValues()) {
                inventoryDAO.addInventoryValue(inventoryAttributeValue, databaseConnection);
            }
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateInventoryItemDetails(InventoryItemDetails inventoryItemDetails) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.updateInventoryItem(inventoryItemDetails.getInventoryItem(), databaseConnection);
            for (InventoryAttributeValue inventoryAttributeValue : inventoryItemDetails.getInventoryAttributeValues()) {
                inventoryDAO.updateInventoryValue(inventoryAttributeValue, databaseConnection);
            }
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateItem(InventoryItem inventoryItem) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.updateInventoryItem(inventoryItem, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addAttributeNameList(List<InventoryAttributeName> inventoryAttributeNames) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
                inventoryDAO.addInventoryAttributeName(inventoryAttributeName, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateAttributeName(InventoryAttributeName inventoryAttributeName) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.updateInventoryAttributeName(inventoryAttributeName, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void addAttributeValueList(List<InventoryAttributeValue> inventoryAttributeValues) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            for (InventoryAttributeValue inventoryAttributeValue : inventoryAttributeValues) {
                inventoryDAO.addInventoryValue(inventoryAttributeValue, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void updateAttributeValue(InventoryAttributeValue inventoryAttributeValue) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryDAO.updateInventoryValue(inventoryAttributeValue, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }
    /*
     * public void deleteCatagoryType(InventoryCatagoryType
     * inventoryCatagoryType) { DatabaseConnection databaseConnection = new
     * DatabaseConnection(); try { databaseConnection.dbOpen();
     * databaseConnection.dbTransationStart();
     * deleteInventoryCatagoryType(inventoryCatagoryType, databaseConnection);
     *
     * databaseConnection.dbTransationEnd(); } catch (Exception ex) {
     * logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) { } }
     * finally { databaseConnection.dbClose(); } }
     */
    /*
     * public void deleteInventoryItem(long inventoryItemID) {
     * DatabaseConnection databaseConnection = new DatabaseConnection(); try {
     * databaseConnection.dbOpen(); databaseConnection.dbTransationStart();
     * InventoryItem inventoryItem =
     * inventoryDAO.getInventoryItemByItemID(inventoryItemID,
     * databaseConnection);
     *
     * if(inventoryDAO.hasChildOfInventoryItem(inventoryItemID,
     * databaseConnection)){ throw new
     * Exception("Inventory item deletion failed bacause of child Item(s)."); }
     *
     * inventoryDAO.deleteInventoryAttributeValueByItemID(inventoryItemID,
     * databaseConnection);
     * inventoryDAO.deleteInventoryItemByItemID(inventoryItemID,
     * databaseConnection); databaseConnection.dbTransationEnd(); } catch
     * (Exception ex) { logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) { } }
     * finally { databaseConnection.dbClose(); } }
     *
     *
     */
    /*
     * public void deleteInventoryItem(InventoryItem inventoryItem) {
     * DatabaseConnection databaseConnection = new DatabaseConnection(); try {
     * databaseConnection.dbOpen(); databaseConnection.dbTransationStart();
     * deleteInventoryItem(inventoryItem, databaseConnection);
     *
     * databaseConnection.dbTransationEnd(); } catch (Exception ex) {
     * logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) { } }
     * finally { databaseConnection.dbClose(); } }
     */
    /*
     * public void deleteAttributeName( InventoryAttributeName
     * inventoryAttributeName) { DatabaseConnection databaseConnection = new
     * DatabaseConnection(); try { databaseConnection.dbOpen();
     * databaseConnection.dbTransationStart();
     * deleteInventoryAttrivuteName(inventoryAttributeName, databaseConnection);
     *
     * databaseConnection.dbTransationEnd(); } catch (Exception ex) {
     * logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) { } }
     * finally { databaseConnection.dbClose(); } }
     */

    public void deleteAttrivuteValue(InventoryAttributeValue inventoryAttributeValue) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            deleteInventoryAttributeValue(inventoryAttributeValue, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }
    /*
     * private void deleteInventoryCatagoryType( InventoryCatagoryType
     * inventoryCatagoryType, DatabaseConnection databaseConnection) throws
     * Exception {
     *
     * List<InventoryItem> inventoryItems =
     * inventoryDAO.getInventoryItemLisByCatagoryID(inventoryCatagoryType.getID(
     * ), databaseConnection);
     *
     * for (InventoryItem inventoryItem : inventoryItems) {
     * deleteInventoryItem(inventoryItem); }
     * inventoryDAO.deleteInventoryCatagoryType(inventoryCatagoryType,
     * databaseConnection); }
     */

    /*
     * deletes inventory item , its attribute values and child items
     */

    /*
     * private void deleteInventoryItem(long inventoryItemID, DatabaseConnection
     * databaseConnection) throws Exception {
     *
     *
     * if(inventoryDAO.hasChildOfInventoryItem(inventoryItemID,
     * databaseConnection)){ throw new
     * Exception("Inventory item deletion failed bacause of child Item(s)."); }
     *
     * inventoryDAO.deleteInventoryAttributeValueByItemID(inventoryItemID,
     * databaseConnection);
     * inventoryDAO.deleteInventoryItemByItemID(inventoryItemID,
     * databaseConnection); }
     */
    /*
     * private void deleteInventoryAttrivuteName( InventoryAttributeName
     * inventoryAttributeName, DatabaseConnection databaseConnection) throws
     * Exception { InventoryRepository inventoryRepository = InventoryRepository
     * .getInstance(); List<InventoryAttributeValue> inventoryAttributeValueList
     * = inventoryRepository.getInventoryAttributeValuesByAttributeNameID(
     * inventoryAttributeName.getID()); for (InventoryAttributeValue
     * inventoryAttributeValue : inventoryAttributeValueList) {
     * deleteAttrivuteValue(inventoryAttributeValue); }
     * inventoryDAO.deleteInventoryAttributeName(inventoryAttributeName,
     * databaseConnection); }
     */

    private void deleteInventoryAttributeValue(InventoryAttributeValue inventoryAttributeValue,
                                               DatabaseConnection databaseConnection) throws Exception {
        inventoryDAO.deleteInventoryAttributeValue(inventoryAttributeValue, databaseConnection);
    }

    /*
     * public List<InventoryItem> getDescendantItemList(long ansestorItemID,
     * long inventoryCatagoryTypeID) { DatabaseConnection databaseConnection =
     * new DatabaseConnection(); List<InventoryItem> inventoryItems = new
     * ArrayList<InventoryItem>(); try { InventoryItem ansestorItem =
     * inventoryDAO.getInventoryItemByItemID(ansestorItemID,
     * databaseConnection);
     *
     * InventoryCatagoryType inventoryCatagoryType =
     * InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(
     * inventoryCatagoryTypeID);
     *
     * databaseConnection.dbOpen(); databaseConnection.dbTransationStart();
     * inventoryItems = inventoryDAO.getInventoryItemListOfAnsestor(
     * ansestorItem, inventoryCatagoryType, databaseConnection);
     *
     * databaseConnection.dbTransationEnd(); } catch (Exception ex) {
     * logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) { } }
     * finally { databaseConnection.dbClose(); } return inventoryItems; }
     */
    public InventoryItemDetails getInventoryItemDetailsByItemID(long inventoryItemID) {

        DatabaseConnection databaseConnection = new DatabaseConnection();
        InventoryItemDetails inventoryItemDetails = null;
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            inventoryItemDetails = new InventoryItemDetails();
            InventoryItem inventoryItem = inventoryDAO.getInventoryItemByItemID(inventoryItemID, databaseConnection);
            inventoryItemDetails.setInventoryItem(inventoryItem);
            List<InventoryAttributeValue> inventoryAttributeValueList = inventoryDAO
                    .getInventoryAttributeValueListByItemID(inventoryItemID, databaseConnection);
            inventoryItemDetails.setInventoryAttributeValues(inventoryAttributeValueList);
            List<InventoryAttributeName> inventoryAttributeNames = InventoryRepository.getInstance()
                    .getInventoryAttributeNameListByCatagoryID(inventoryItem.getInventoryCatagoryTypeID());
            inventoryItemDetails.setInventoryAttributeNames(inventoryAttributeNames);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        return inventoryItemDetails;
    }/*
     * public void updateOrInsertInventoryItemDetails(InventoryItemDetails
     * inventoryItemDetails){ DatabaseConnection databaseConnection = new
     * DatabaseConnection(); try {
     *
     * databaseConnection.dbOpen(); databaseConnection.dbTransationStart();
     * if(inventoryDAO.getInventoryItemByItemID(inventoryItemDetails.
     * getInventoryItem().getID(), databaseConnection)!=null){
     * inventoryDAO.updateInventoryItem(inventoryItemDetails.
     * getInventoryItem(), databaseConnection); }else{
     * inventoryDAO.addInventoryItem(inventoryItemDetails.getInventoryItem()
     * , databaseConnection); }
     * inventoryDAO.deleteInventoryAttributeValueByItemID(
     * inventoryItemDetails.getInventoryItem().getID(), databaseConnection);
     * for(InventoryAttributeValue inventoryAttributeValue:
     * inventoryItemDetails.getInventoryAttributeValues()){
     * inventoryAttributeValue.setInventoryItemID(inventoryItemDetails.
     * getInventoryItem().getID());
     * inventoryDAO.addInventoryValue(inventoryAttributeValue,
     * databaseConnection); } databaseConnection.dbTransationEnd(); } catch
     * (Exception ex) { logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) {
     * } } finally { databaseConnection.dbClose(); } }
     *
     * public void deleteInventoryItemDetails(long inventoryItemID){
     * DatabaseConnection databaseConnection = new DatabaseConnection(); try
     * {
     *
     * databaseConnection.dbOpen(); databaseConnection.dbTransationStart();
     *
     * inventoryDAO.deleteInventoryAttributeValueByItemID(inventoryItemID,
     * databaseConnection);
     * inventoryDAO.deleteInventoryItemByItemID(inventoryItemID,
     * databaseConnection);
     *
     * databaseConnection.dbTransationEnd(); } catch (Exception ex) {
     * logger.fatal("Fatal", ex); try {
     * databaseConnection.dbTransationRollBack(); } catch (Exception ex2) {
     * } } finally { databaseConnection.dbClose(); } }
     *
     */

    public List<InventoryItemDetails> getInventoryItemDetailsListByCatagoryIDAndParentItemID(int catagoryID,
                                                                                             long parentItemID) {
        List<InventoryItemDetails> inventoryItemDetailsList = new ArrayList<InventoryItemDetails>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            /*
             * List<InventoryItem> inventoryItemList =
             * inventoryDAO.getInventoryItemListByParentItemAndCatagoryID(
             * parentItemID, catagoryID, databaseConnection);
             *
             * for(InventoryItem inventoryItem: inventoryItemList){
             * InventoryItemDetails inventoryItemDetails = new
             * InventoryItemDetails(); List<InventoryAttributeValue>
             * inventoryAttributeValues =
             * inventoryDAO.getInventoryAttributeValueListByItemID(inventoryItem
             * .getID(), databaseConnection);
             * inventoryItemDetails.setInventoryItem(inventoryItem);
             * inventoryItemDetails.setInventoryAttributeValues(
             * inventoryAttributeValues);
             * inventoryItemDetailsList.add(inventoryItemDetails); }
             */
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        return inventoryItemDetailsList;
    }

    public List<InventoryItem> getInventoryItemForAutoComplete(Integer categoryID, Long parentItemID, String partialName) {
        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryItems = inventoryDAO.getInventoryItemListForAutoComplete(parentItemID, categoryID, partialName,
                    databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }
        return inventoryItems;
    }

    public List<InventoryItem> getInventoryItemListByCatagoryIDAndParentItemID(int catagoryID, long parentItemID) {
        List<InventoryItem> inventoryItemList = new ArrayList<InventoryItem>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryItemList = inventoryDAO.getInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID,
                    databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        return inventoryItemList;
    }

    @Transactional
    public List<InventoryItem> getUnusedInventoryItemListByCatagoryIDAndParentItemID(int catagoryID, Long parentItemID) {
        List<InventoryItem> inventoryItemList = new ArrayList<InventoryItem>();


        try {
            inventoryItemList = inventoryDAO.getUnusedInventoryItemListByParentItemAndCatagoryID(parentItemID, catagoryID);

        } catch (Exception ex) {
            logger.fatal("Fatal", ex);

        }
        return inventoryItemList;
    }


    public HashMap<String, List<Long>> getAvailableChildItemsByAncestoryIDAndCategoryID(int categoryID, long ancestorItemID) {
        logger.debug("categoryID " + categoryID + " " + ancestorItemID);
        HashMap<String, List<Long>> catItemMap = new HashMap<String, List<Long>>();

        InventoryCatagoryTreeNode catTreeNode = InventoryRepository.getInstance()
                .getInventoryCatagoryTreeNodeByCatagoryID(categoryID);
        List<InventoryCatagoryTreeNode> childList = catTreeNode.getInventoryCatagoryDetailsChildNodes();
        for (InventoryCatagoryTreeNode child : childList) {
            InventoryCatagoryDetails categoryDetails = child.getInventoryCatagoryDetailsRootNode();
            int childCategoryID = categoryDetails.getInventoryCatagoryType().getID();
            List<Long> list = getDescendantIDListByAnsestorIDAndDescendantCategoryID(ancestorItemID, childCategoryID);
            if (list != null && list.size() > 0) {
                catItemMap.put(categoryDetails.getInventoryCatagoryType().getName(), list);
            }
        }
        return catItemMap;
    }

    public List<Long> getDescendantIDListByAnsestorIDAndDescendantCategoryID(Long ansestorItemID,
                                                                             int descendantCategoryID) {
        List<Long> returnIDList = new ArrayList<Long>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            returnIDList = inventoryDAO.getDescendantIDListByAnsestorIDAndDescendantCategoryID(ansestorItemID,
                    descendantCategoryID, false, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        return returnIDList;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, InventoryItem> getMapOfInventoryItemToInventoryIDByInventoryIds (List<Long> inventoryIds) throws Exception {
        List<Long> curatedList = inventoryIds.stream()
                .filter(t->t!=0)
                .collect(Collectors.toList());
        return ServiceDAOFactory.getService(GlobalService.class)
                .getAllObjectListByCondition(InventoryItem.class,
                        new InventoryItemConditionBuilder()
                        .Where()
                        .IDIn(curatedList)
                        .getCondition()
                )
        .stream()
        .collect(Collectors.toMap(InventoryItem::getID, Function.identity()));
    }
    public InventoryItem getInventoryItemByItemID(long inventoryItemID) {
        InventoryItem itemDTO = null;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            itemDTO = inventoryDAO.getInventoryItemByItemID(inventoryItemID, databaseConnection);
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        if (itemDTO == null) {
            itemDTO = new InventoryItem();
        }
        return itemDTO;
    }

    /*
     * it will return n-th parent item of the item where n=categoryTypeID
     */
    public InventoryItem getInventoryItemByCategoryTypeIDAndItemID(long categoryTypeID, long inventoryItemID) {
        InventoryItem itemDTO = new InventoryItem();
        itemDTO.setID(inventoryItemID);
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            while (true) {
                itemDTO = inventoryDAO.getInventoryItemByItemID(itemDTO.getID(), databaseConnection);
                if (itemDTO == null || itemDTO.getInventoryCatagoryTypeID() == categoryTypeID) {
                    break;
                } else {
                    itemDTO.setID(itemDTO.getParentID());
                }
            }

            databaseConnection.dbTransationEnd();
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        /*
         * if(itemDTO==null){ itemDTO= new InventoryItem(); }
         */
        return itemDTO;
    }

    /*
     * This will return a map of categoryTypeID and Inventory Item where upto a
     * fixed categoryTypeID
     */
    public Map<Integer, InventoryItem> getInventoryParentItemPathMapByCategoryTypeIDAndItemID(long categoryTypeID,
                                                                                              long inventoryItemID) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Map<Integer, InventoryItem> fromRootToItemPathMap = new LinkedHashMap<Integer, InventoryItem>();

        InventoryItem itemDTO = new InventoryItem();
        itemDTO.setID(inventoryItemID);
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            while (true) {
                itemDTO = inventoryDAO.getInventoryItemByItemID(itemDTO.getID(), databaseConnection);
                if (itemDTO == null) {
                    break;
                } else if (itemDTO.getInventoryCatagoryTypeID() == categoryTypeID) {
                    fromRootToItemPathMap.put(itemDTO.getInventoryCatagoryTypeID(), itemDTO);
                    break;
                } else {
                    fromRootToItemPathMap.put(itemDTO.getInventoryCatagoryTypeID(), itemDTO);
                    if (itemDTO.getParentID() == null) {
                        break;
                    } else {
                        long nextItemID = itemDTO.getParentID();
                        itemDTO = new InventoryItem();
                        itemDTO.setID(nextItemID);
                    }
                }
            }

            databaseConnection.dbTransationEnd();
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }

        return fromRootToItemPathMap;
    }

    /*
     * This will return a map of categoryTypeID and Inventory Item where upto a
     * root (eg: District)
     */
    public Map<Integer, InventoryItem> getInventoryParentItemPathMapUptoRootByItemID(long inventoryItemID) {
        return getInventoryParentItemPathMapByCategoryTypeIDAndItemID(0, inventoryItemID);
    }

    public List<InventoryItem> getInventoryItemDetailsFromRootByItemID(long itemID) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            return inventoryDAO.getInventoryItemDetailsFromRootByItemID(itemID, databaseConnection);
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            databaseConnection.dbClose();
        }
    }

    public List<Long> deleteInventoryItemByItemID(long inventoryItemID, HttpServletRequest request) {
        List<Long> returnIDList = new ArrayList<Long>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            if (inventoryDAO.hasChildOfInventoryItem(inventoryItemID, databaseConnection)) {
                new CommonActionStatusDTO().setErrorMessage("The " + InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(getInventoryItemByItemID(inventoryItemID).getInventoryCatagoryTypeID()).getName() + " "
                        + getInventoryItemByItemID(inventoryItemID).getName() + " has children.", false, request);
                throw new RequestFailureException("Inventory item with id " + inventoryItemID + " can not be deleted because it has child item(s)");
            } else if (inventoryDAO.isItemUsed(inventoryItemID, databaseConnection)) {
                new CommonActionStatusDTO().setErrorMessage("The " + InventoryRepository.getInstance().getInventoryCatgoryTypeByCatagoryID(getInventoryItemByItemID(inventoryItemID).getInventoryCatagoryTypeID()).getName() + " "
                        + getInventoryItemByItemID(inventoryItemID).getName() + " has been used.", false, request);
                throw new RequestFailureException("Inventory item with id " + inventoryItemID + " can not be deleted because it has been used");
            }
            long lastModificationTime = System.currentTimeMillis();
            inventoryDAO.deleteInventoryAttributeValueByItemID(inventoryItemID, databaseConnection,
                    lastModificationTime);
            inventoryDAO.deleteInventoryItemByItemID(inventoryItemID, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
        return returnIDList;
    }

    public InventoryCatagoryType getInventoryCategoryTypeByCategoryTypeID(int categoryTypeID) throws Exception {
        InventoryCatagoryType inventoryCategoryType = new InventoryCatagoryType();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryCategoryType = inventoryDAO.getInventoryCategoryTypeByCategoryTypeID(categoryTypeID, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }

        return inventoryCategoryType;
    }


    public void markInventoryItemAsUsed(long inventoryItemID, DatabaseConnection databaseConnection) throws Exception {
        String inventoryCategoryTypeName = getInventoryCategoryTypeByCategoryTypeID(getInventoryItemByItemID(inventoryItemID).getInventoryCatagoryTypeID()).getName();

        if (inventoryDAO.hasChildOfInventoryItem(inventoryItemID, databaseConnection)) {
            throw new RequestFailureException(inventoryCategoryTypeName + " (" + inventoryItemID + ") can not be used because it has child item(s)");
        }

        int numOfAffectedRows = 0;

        synchronized (mutexLock) {

            InventoryItem inventoryItem = inventoryDAO.getInventoryItemByItemID(inventoryItemID,
                    databaseConnection);
            if (inventoryItem == null || inventoryItem.isDeleted()) {
                throw new RequestFailureException("No inventory item found with ID " + inventoryItemID);
            }
            if (!inventoryItem.isChildItem()) {
                throw new RequestFailureException(inventoryCategoryTypeName + " is not a child item. Hence it can not be executed.");
            }
            if (inventoryItem.getIsUsed()) {
                throw new RequestFailureException(inventoryCategoryTypeName + " (" + inventoryDAO.getInventoryItemByItemID(inventoryItemID, databaseConnection).getName() + ") is already used");
            }
            inventoryItem.setLastModificationTime(System.currentTimeMillis());
            inventoryItem.setIsUsed(true);
            numOfAffectedRows = inventoryDAO.updateInventoryItem(inventoryItem, databaseConnection);
        }

        if (numOfAffectedRows == 0) {
            throw new RequestFailureException(inventoryCategoryTypeName + " (" + inventoryItemID + ") can not be used");
        }


    }

    public void markInventoryItemAsUsed(long inventoryItemID) throws Exception {

        String inventoryCategoryTypeName = getInventoryCategoryTypeByCategoryTypeID(getInventoryItemByItemID(inventoryItemID).getInventoryCatagoryTypeID()).getName();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            markInventoryItemAsUsed(inventoryItemID, databaseConnection);


            databaseConnection.dbTransationEnd();
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw new RequestFailureException(inventoryCategoryTypeName + " (" + inventoryItemID + ") can not be set as used");
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void markInventoryItemAsUsedByOccupierInformation(long inventoryItemID, long occupierEntityID,
                                                             int occupierEntityTypeID, long occupierClientID, DatabaseConnection databaseConnection) throws Exception {


        inventoryDAO.markInventoryItemAsUsedByItemID(inventoryItemID, occupierEntityID, occupierEntityTypeID, occupierClientID, databaseConnection);

    }

    public void markInventoryItemAsUsed(long inventoryItemID, long occupierEntityID, int occupierEntityTypeID, long occupierClientID) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryDAO.markInventoryItemAsUsedByItemID(inventoryItemID, occupierEntityID, occupierEntityTypeID, occupierClientID, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }

    public void markInventoryItemAsUnused(long inventoryItemID) throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryDAO.markInventoryItemAsUnusedByItemID(inventoryItemID, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();
        }
    }


    public void markInventoryItemAsUnusedByItemID(long itemID, DatabaseConnection databaseConnection) throws Exception {
        inventoryDAO.markInventoryItemAsUnusedByItemID(itemID, databaseConnection);
    }


    public List<InventoryItem> getAllInventoryItemByCategoryID(long categoryID) {
        List<InventoryItem> itemList;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {

            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();
            String fixedConditon = " where " + ModifiedSqlGenerator.getColumnName(InventoryItem.class, "inventoryCatagoryTypeID") + " = " + categoryID
                    + " and " + getColumnName(InventoryItem.class, "isDeleted") + "=0 and (invitIsUsed is NULL or invitIsUsed = 0) ";
            itemList = (List<InventoryItem>) SqlGenerator.getAllObjectList(InventoryItem.class, databaseConnection, fixedConditon);
        } catch (RequestFailureException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
            throw new RequestFailureException("Error");
        } finally {
            databaseConnection.dbClose();
        }
        return itemList;
    }

    public void forceFullyAddCategory() {

    }

    public void forceFullyDeleteCategory() {

    }

    public ArrayList<InventoryItem> getAllInventoryItemByCategoryIdWithoutParent(Integer categoryID, String partialName) {
        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            inventoryItems = inventoryDAO.getInventoryItemListForAutoComplete(categoryID, partialName, databaseConnection);
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }
        return (ArrayList<InventoryItem>) inventoryItems;
    }

    public ArrayList<InventoryItem> getAllInventoryItemByCategoryIDAndAttributeNameAndAttributeValue(Integer categoryID, String attributeName, String attributeValue) {
        //Idea is to get inventoryItem by any attribute name and value.
        //Now it is written only to work with attribute "Port Type" under the category "Port"

        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            long inventoryNameID = inventoryDAO.getAttributeNameIDByCategoryIDAndAttributeNameName(categoryID, attributeName, databaseConnection);
            List<Long> inventoryItemIDListByAttributeNameIDAndAttributeValue = inventoryDAO.getInventoryItemIDListByAttributeNameIDAndAttributeValue(inventoryNameID, attributeValue, databaseConnection);
            inventoryItems = inventoryDAO.getInventoryItemListByInventoryItemIDList
                    (inventoryItemIDListByAttributeNameIDAndAttributeValue, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }
        return (ArrayList<InventoryItem>) inventoryItems;
    }


    // TODO: 12/26/2018 this is not working for parent id check later
    public ArrayList<InventoryItem> getAllInventoryItemByCategoryIDAndAttributeNameAndAttributeValue
    (Integer categoryID, String attributeName, String attributeValue, long parentId) {
        //Idea is to get inventoryItem by any attribute name and value.
        //Now it is written only to work with attribute "Port Type" under the category "Port"

        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            long inventoryNameID = inventoryDAO.getAttributeNameIDByCategoryIDAndAttributeNameName
                    (categoryID, attributeName, databaseConnection);
            List<Long> inventoryItemIDListByAttributeNameIDAndAttributeValue = inventoryDAO.
                    getInventoryItemIDListByAttributeNameIDAndAttributeValue(inventoryNameID, attributeValue, databaseConnection);
            inventoryItems = inventoryDAO.getInventoryItemListByInventoryItemIDList
                    (inventoryItemIDListByAttributeNameIDAndAttributeValue, databaseConnection);

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }
        return (ArrayList<InventoryItem>) inventoryItems;
    }

    /**
     * Single Function to get all kinds of inventory items.
     * First part deals with Inventory sub category implemented by attributeName and attributeValue.
     *
     * @author Dhrubo
     * @since 10/09/2017
     */
    public List<InventoryItem> getInventoryItemListForAutocomplete(Integer categoryID, Long parentItemID
            , String partialName, String attributeName, String attributeValue, Boolean onlyUnused
            , Boolean isParentNeeded) {
        List<InventoryItem> inventoryItemList = new ArrayList<InventoryItem>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            if (attributeName != null) {
                long inventoryNameID = inventoryDAO.getAttributeNameIDByCategoryIDAndAttributeNameName(categoryID, attributeName, databaseConnection);
                List<Long> inventoryItemIDListByAttributeNameIDAndAttributeValue = inventoryDAO.getInventoryItemIDListByAttributeNameIDAndAttributeValue(inventoryNameID, attributeValue, databaseConnection);
                List<InventoryItem> tempInventoryItemList = inventoryDAO.getInventoryItemListByInventoryItemIDList(inventoryItemIDListByAttributeNameIDAndAttributeValue, databaseConnection);

                if (partialName != null || parentItemID != null) {
                    for (InventoryItem inventoryItem : tempInventoryItemList) {

                        if (((onlyUnused && !inventoryItem.getIsUsed()) || (!onlyUnused))
                                && (!inventoryItem.isDeleted())
                                && ((isParentNeeded && (parentItemID != null) && (inventoryItem.getParentID().equals(parentItemID))) || !isParentNeeded)
                                && ((partialName == "") || (partialName != null && inventoryItem.getName().contains(partialName)))
                        ) {
                            inventoryItemList.add(inventoryItem);
                        }
                    }
                }

            } else {
                inventoryItemList = inventoryDAO.getInventoryItemListForAutocomplete(categoryID
                        , parentItemID, partialName, onlyUnused, isParentNeeded, databaseConnection);
            }

            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }
        return inventoryItemList;
    }

    /**
     * @author dhrubo
     * @since 01/11/2017
     */
    public InventoryAttributeName getPortTypeInventoryAttributeName() {
        List<InventoryAttributeName> inventoryAttributeNames = InventoryRepository.getInstance().getInventoryAttributeNameListByCatagoryID(CategoryConstants.CATEGORY_ID_PORT);
        InventoryAttributeName portTypeInventoryAttributeName = new InventoryAttributeName();
        for (InventoryAttributeName inventoryAttributeName : inventoryAttributeNames) {
            if (inventoryAttributeName.getName().equals("Port Type")) {
                portTypeInventoryAttributeName = inventoryAttributeName;
                break;
            }
        }
        return portTypeInventoryAttributeName;
    }

    /**
     * @author dhrubo
     */
    public Integer getDivisionOfInventoryItem(long inventoryItemID) throws Exception {
        List<InventoryItem> ancestorInventoryItemList = getInventoryItemDetailsFromRootByItemID(inventoryItemID);
        for (InventoryItem ancestorInventoryItem : ancestorInventoryItemList) {
            if (ancestorInventoryItem.getInventoryCatagoryTypeID().equals(CategoryConstants.CATEGORY_ID_DISTRICT)) {
                return InventoryConstants.districtToDivisionMap.get(ancestorInventoryItem.getID());
            }
        }
        return null;
    }

    public long getDistrictOfInventoryItem(long inventoryItemID) throws Exception {
        List<InventoryItem> ancestorInventoryItemList = getInventoryItemDetailsFromRootByItemID(inventoryItemID);
        return ancestorInventoryItemList.stream()
                .filter(t->t.getInventoryCatagoryTypeID() == CategoryConstants.CATEGORY_ID_DISTRICT)
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No District Id found for Inventory Item Id: " + inventoryItemID))
                .getID();
    }

    /**
     * @author dhrubo
     */
    public List<InventoryItem> getPortsUsedByClient(long clientID, long parentID, String portType) throws Exception {
        List<InventoryItem> portsUsedByClient = new ArrayList<InventoryItem>();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.dbOpen();
            databaseConnection.dbTransationStart();

            portsUsedByClient = SqlGenerator.getAllObjectList(InventoryItem.class, databaseConnection,
                    " where invitOccupierClientID = " + clientID + " and invitParentItemID = " + parentID);
            for (InventoryItem port : portsUsedByClient) {
                port.setName(port.getName() + " (used by " + AllClientRepository.getInstance().getClientByClientID(clientID).getName() + ")");
            }
            databaseConnection.dbTransationEnd();
        } catch (Exception ex) {
            logger.fatal("Fatal", ex);
            try {
                databaseConnection.dbTransationRollBack();
            } catch (Exception ex2) {
            }
        } finally {
            databaseConnection.dbClose();

        }

        List<InventoryItem> portsUsedByClientMatchingAttributeValue = new ArrayList<InventoryItem>();

        for (InventoryItem port : portsUsedByClient) {
            InventoryItemDetails portDetails = getInventoryItemDetailsByItemID(port.getID());
            if (portDetails.getValueByAttributeName("Port Type").equals(portType)) {
                portsUsedByClientMatchingAttributeValue.add(port);
            }
        }

        return portsUsedByClientMatchingAttributeValue;
    }

    private Set<Integer> getSuggestionForVLan(long destination, long sourceID, Map<Integer
            , Map<Long, Long>> parentMap, Set<Integer> availableVLans
            , Map<Long, RouterSwitchDTO> mapOfRouterSwitchDTOToID) throws Exception {


        Queue<Long> q1 = new LinkedList<>();
        Queue<Set<Integer>> q2 = new LinkedList<>();


        Set<Integer> possibleVLansInDestination = new HashSet<>(availableVLans);
        for (int alreadyUsedVlanInDestination : mapOfRouterSwitchDTOToID.get(destination).usedVLanList) {
            possibleVLansInDestination.remove(alreadyUsedVlanInDestination);
        }

        Set<Integer> newlyFoundVLansInDestination = new HashSet<>();

        q1.add(sourceID);
        q2.add(availableVLans);


        while (!q1.isEmpty()) {


            Long currentRouterID = q1.poll();
            Set<Integer> currentlyAvailableVLans = q2.poll();
            for (int usedVLanInSourceRouter : mapOfRouterSwitchDTOToID.get(currentRouterID).usedVLanList) {
                currentlyAvailableVLans.remove(usedVLanInSourceRouter);
            }

            for (int currentlyAvailableVlan : currentlyAvailableVLans) {
                if (!possibleVLansInDestination.contains(currentlyAvailableVlan)) {
                    currentlyAvailableVLans.remove(currentlyAvailableVlan);
                }
            }


            if (currentlyAvailableVLans.isEmpty()) {
                continue;
            }

            if (currentRouterID == destination) {
                for (int currentlyAvailableVLan : currentlyAvailableVLans) {
                    possibleVLansInDestination.remove(currentlyAvailableVLan);
                    newlyFoundVLansInDestination.add(currentlyAvailableVLan);
                }
                if (possibleVLansInDestination.isEmpty()) {
                    break;
                }
            }

            for (long adjacentRouterID : mapOfRouterSwitchDTOToID.get(currentRouterID).connectedDeviceIDList) {
                if (!mapOfRouterSwitchDTOToID.get(adjacentRouterID).type.equalsIgnoreCase("switch")) {
                    continue;
                }
                Set<Integer> availableTmpSet = new HashSet<>();

                for (int currentAvailableVLanForAdjacentSwitch : currentlyAvailableVLans) {
                    if (!parentMap.containsKey(currentAvailableVLanForAdjacentSwitch)
                            || !parentMap.get(currentAvailableVLanForAdjacentSwitch).containsKey(adjacentRouterID)) {
                        if (!parentMap.containsKey(currentAvailableVLanForAdjacentSwitch)) {
                            parentMap.put(currentAvailableVLanForAdjacentSwitch, new HashMap<>());
                        }

                        parentMap.get(currentAvailableVLanForAdjacentSwitch)
                                .put(adjacentRouterID, currentRouterID);
                        availableTmpSet.add(currentAvailableVLanForAdjacentSwitch);

                    }
                }

                q1.add(adjacentRouterID);
                q2.add(availableTmpSet);

            }


        }


        return newlyFoundVLansInDestination;
    }


    /**
     * @author Dhrubo
     */
    @Transactional(transactionType = util.TransactionType.READONLY)
    public String getVlanDetailsByVlanID(long inventoryID) {
        InventoryItem vlan = getInventoryItemByItemID(inventoryID);
        if (vlan.getInventoryCatagoryTypeID() != InventoryConstants.CATEGORY_ID_VIRTUAL_LAN) {
            return "Warning: Invalid VLAN";
        }

        Map<Integer, InventoryItem> mapUptoRoot = getInventoryParentItemPathMapUptoRootByItemID(inventoryID);
        String popName = mapUptoRoot.get(CategoryConstants.CATEGORY_ID_POP).getName();
        String routerName = mapUptoRoot.get(CategoryConstants.CATEGORY_ID_ROUTER).getName();
        String portName = mapUptoRoot.get(CategoryConstants.CATEGORY_ID_PORT).getName();
        return "PoP: " + popName + " | Router: " + routerName + " | Port: " + portName;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public InventoryAttributeValue getInventoryAttributeValueByNameIdAndItemId(long nameId, long itemId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(InventoryAttributeValue.class,
                new InventoryAttributeValueConditionBuilder()
                        .Where()
                        .inventoryItemIDEquals(itemId)
                        .inventoryAttributeNameIDEquals(nameId)
                        .getCondition()
        ).stream()
                .findFirst()
                .orElseThrow(() -> new RequestFailureException(
                        "No Inventory Attribute Value Found for Item Id : " + itemId + " attribute Name id: " + nameId));
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Integer, List<InventoryItem> > getInventoryItemsByParentInventoryItems(List<Long> parentIds ) throws Exception{

        if(parentIds.isEmpty())return Collections.emptyMap();

        List<InventoryItem> list = ServiceDAOFactory.getService(GlobalService.class)
                .getAllObjectListByCondition(
                        InventoryItem.class,
                        new InventoryItemConditionBuilder()
                                .Where()
                                .parentIDIn(parentIds)
                                .isDeleted(false)
                                .getCondition()
                );

        List<InventoryItem> modifiedList = getInventoryItemsModifiedByAllocationHistories(list);
        return modifiedList.stream()
                .collect(Collectors.groupingBy(InventoryItem::getInventoryCatagoryTypeID));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryItem> getInventoryItems(int childCategory, Long parent) throws Exception {
        List<InventoryItem> inventoryItems = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
                new InventoryItemConditionBuilder()
                        .Where()
                        .parentIDEquals(parent)
                        .inventoryCatagoryTypeIDEquals(childCategory)
                        .isDeleted(false)
                        .getCondition()


        );
        return getInventoryItemsModifiedByAllocationHistories(inventoryItems);

    }

    public List<InventoryItem> getInventoryItemsModifiedByAllocationHistories(List<InventoryItem> inventoryItems) throws Exception {
        List<Long> ids = inventoryItems.stream()
                .map(InventoryItem::getID)
                .collect(Collectors.toList());

        Map<Long, List<InventoryAllocationHistory>> inventoryAllocationHistoriesMap = getMapOfAllocationHistoriesByItemIds(ids);
        for (InventoryItem item : inventoryItems) {
            List<InventoryAllocationHistory> list = inventoryAllocationHistoriesMap.getOrDefault(item.getID(), Collections.emptyList());

            if(list.isEmpty()) {
                item.setIsUsed(false);
            }else {
                item.setIsUsed(true);
            }

        }
        return inventoryItems;

    }

    public Map<Long, List<InventoryAllocationHistory>> getMapOfAllocationHistoriesByItemIds(List<Long> ids) throws Exception {
       return ServiceDAOFactory.getService(InventoryAllocationHistoryService.class)
                .getCurrentHistoryIdByInventoryIds(ids)
                .stream()
                .collect(Collectors.groupingBy(InventoryAllocationHistory::getItemId));

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryItem> getInventoryItems(int childCategory, Long parent, int portType) throws Exception {
        List<InventoryItem> inventoryItems = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
                new InventoryItemConditionBuilder()
                        .Where()
                        .parentIDEquals(parent)
                        .inventoryCatagoryTypeIDEquals(childCategory)
                        .isDeleted(false)
                        .getCondition()
        );
        String portAttributeValue = InventoryConstants.mapOfPortTypeToPortTypeString.get(portType);
        List<Long> ids = inventoryItems.stream()
                .mapToLong(InventoryItem::getID)
                .boxed()
                .collect(Collectors.toList());
        if (childCategory == InventoryConstants.CATEGORY_ID_PORT) {
            if(ids.isEmpty())return Collections.emptyList();

            Map<Long, String> map =getMapOfAttributeValueToItemIdByItemIds(ids);

            inventoryItems = inventoryItems.stream()
                    .filter(t->{
                        String portTypeStr = map.getOrDefault(t.getID(), null);
                        return portAttributeValue.equals(portTypeStr);
                    }).collect(Collectors.toList());
        }

        ids = inventoryItems.stream()
                .mapToLong(InventoryItem::getID)
                .boxed()
                .collect(Collectors.toList());

        List<InventoryAllocationHistory> inventoryAllocationHistories = ServiceDAOFactory.
                getService(InventoryAllocationHistoryService.class).getCurrentHistoryIdByInventoryIds(ids);

        for (InventoryItem item : inventoryItems) {
            InventoryAllocationHistory inventoryAllocationHistory = inventoryAllocationHistories
                    .stream()
                    .filter(t -> t.itemId == item.getID())
                    .findFirst()
                    .orElse(null);
            if (inventoryAllocationHistory == null) {
                item.setIsUsed(false);
            } else {
                item.setIsUsed(true);
            }
        }
        return inventoryItems;

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, String> getMapOfAttributeValueToItemIdByItemIds(List<Long> itemIds )throws Exception {
        if(itemIds.isEmpty())return Collections.emptyMap();
        return ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                InventoryAttributeValue.class, new InventoryAttributeValueConditionBuilder()
                        .Where()
                        .inventoryItemIDIn(
                                itemIds
                        ).getCondition()
        ).stream()
                .collect(Collectors.toMap(InventoryAttributeValue::getInventoryItemID, InventoryAttributeValue::getValue));
    }

    /*
    Added by Touhid
    Used for Connection migration
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public long getItemIdByItemName(String itemName) throws Exception {
        List<InventoryItem> inventoryItems = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
                " Where " +
                        getColumnName(InventoryItem.class, "name") +
                        " = '" +
                        itemName + "'");

        if(inventoryItems.size()==0){
            return 0;
        }
        else{
            return inventoryItems.get(0).getID();
        }
    }






	public List<InventoryAttributeValue> getInventoryAttributeValueListByItemID(long id) throws Exception{
		return inventoryDAO.getInventoryAttributeValueListByItemID(id,DatabaseConnectionFactory.getCurrentDatabaseConnection());
	}

	@Transactional(transactionType = TransactionType.READONLY)
    public List<InventoryItem> getGlobalVlans() throws Exception {
       List<InventoryItem> list = ServiceDAOFactory.getService(GlobalService.class).getAllObjectListByCondition(
                InventoryItem.class,
                new InventoryItemConditionBuilder()
                        .Where()
                        .inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_GLOBAL_VIRTUAL_LAN)
                        .isDeleted(false)
                        .getCondition()
        );

       return getInventoryItemsModifiedByAllocationHistories(list);
    }
}
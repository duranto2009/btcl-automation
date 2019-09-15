package datamigrationLLIInventory;

import java.util.ArrayList;
import java.util.List;

import inventory.InventoryAttributeValue;
import inventory.InventoryConstants;
import inventory.InventoryItem;
import inventory.InventoryItemConditionBuilder;
import inventory.repository.AttributeNameIdMap;
import annotation.Transactional;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;

public class LLIInventoryMigrationService {

	@Transactional
	public void addVlan(List<String> inventoryItemAttributes) throws Exception {
		CurrentTimeFactory.initializeCurrentTimeFactory();

		String switchOfVlan = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.SWITCH_NAME_FOR_VLAN);
		String vlan = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.VIRTUAL_LAN);
		String portNumber = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.ACCESS_PORT_NUMBER);

		if (switchOfVlan == "")
			return;

		long switchOfVlanId = switchExists(switchOfVlan);

		if (switchOfVlanId == -1) {
			// switch doesn't exist
			// add switch??
			return;
		}

		else {
			// add vlan

			/*if (virtualLanExists(vlan, parentSwitchId) != -1) {
				System.out.println("VLAN already exists");
				return;
			}*/

			System.out.println(vlan);

			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN);
			inventoryItem.setName(vlan);
			//inventoryItem.setParentID(parentSwitchId);
			inventoryItem.setChildItem(true);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
		}
		CurrentTimeFactory.destroyCurrentTimeFactory();
	}

	@Transactional
	public void addRouterOrSwitch(List<String> inventoryItemAttributes, int mode) throws Exception {

		CurrentTimeFactory.initializeCurrentTimeFactory();

		String district = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.DISTRICT);
		String upazila = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.UPAZILA);
		String union = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.UNION);
		String pop = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.POP);
		String router = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.ROUTER_NAME);
		String switch_name = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.SWITCH_NAME);
		String vendor = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.VENDOR);
		String port = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.PORT);
		String description = inventoryItemAttributes.get(LLIInventoryExistingDataColumnNameMap.DESC);

		// add district if non-existent

		long districtId = districtExists(district);
		if (districtId == -1) {
			// add
			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_DISTRICT);
			inventoryItem.setName(district);
			inventoryItem.setChildItem(false);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
			districtId = districtExists(district);
		}

		// add upazila if non-existent

		long upazilaId = upazilaExists(upazila);
		if (upazilaId == -1) {
			// add
			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_UPAZILA);
			inventoryItem.setName(upazila);
			inventoryItem.setParentID(districtId);
			inventoryItem.setChildItem(true);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
			upazilaId = upazilaExists(upazila);
		}

		// add union if non-existent

		long unionId = unionExists(union);
		if (unionId == -1) {
			// add
			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_UNION);
			inventoryItem.setName(union);
			inventoryItem.setParentID(upazilaId);
			inventoryItem.setChildItem(true);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
			unionId = unionExists(union);
		}

		// add POP if non-existent

		long popId = popExists(pop);
		if (popId == -1) {
			// add
			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_POP);
			inventoryItem.setName(pop);
			inventoryItem.setParentID(unionId);
			inventoryItem.setChildItem(true);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
			popId = popExists(pop);
		}

		// add router/switch if non-existent

		// router
		long routerId = 0;
		long switchId = 0;

		if (mode == 1) {

			routerId = routerExists(router);
			if (routerId == -1) {
				// add
				InventoryItem inventoryItem = new InventoryItem();
				inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_ROUTER);
				inventoryItem.setName(router);
				inventoryItem.setParentID(popId);
				inventoryItem.setChildItem(true);
				inventoryItem.setLastModificationTime(System.currentTimeMillis());
				inventoryItem.setDeleted(false);

				ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
				routerId = routerExists(router);

				// add router to the other table here
				// insert the router

				InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
				inventoryAttributeValue.setValue("Router");
				inventoryAttributeValue.setInventoryItemID(routerId);
				inventoryAttributeValue.setInventoryAttributeNameID(AttributeNameIdMap.TYPE);
				inventoryAttributeValue.setLastModificationTime(System.currentTimeMillis());
				inventoryAttributeValue.setDeleted(false);

				ModifiedSqlGenerator.insert(inventoryAttributeValue, InventoryAttributeValue.class, false);

				// insert the vendor
				inventoryAttributeValue = null;
				inventoryAttributeValue = new InventoryAttributeValue();
				inventoryAttributeValue.setValue(vendor);
				inventoryAttributeValue.setInventoryItemID(routerId);
				inventoryAttributeValue.setInventoryAttributeNameID(AttributeNameIdMap.VENDOR);
				inventoryAttributeValue.setLastModificationTime(System.currentTimeMillis());
				inventoryAttributeValue.setDeleted(false);
				ModifiedSqlGenerator.insert(inventoryAttributeValue, InventoryAttributeValue.class, false);

			}
		}

		// switch

		if (mode == 2) {

			switchId = switchExists(switch_name);
			if (switchId == -1) {
				// add
				InventoryItem inventoryItem = new InventoryItem();
				inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_ROUTER);
				inventoryItem.setName(switch_name);
				inventoryItem.setParentID(popId);
				inventoryItem.setChildItem(true);
				inventoryItem.setLastModificationTime(System.currentTimeMillis());
				inventoryItem.setDeleted(false);

				ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
				switchId = switchExists(switch_name);

				// add switch to the other table here
				// insert the switch

				InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
				inventoryAttributeValue.setValue("Switch");
				inventoryAttributeValue.setInventoryItemID(switchId);
				inventoryAttributeValue.setInventoryAttributeNameID(AttributeNameIdMap.TYPE);
				inventoryAttributeValue.setLastModificationTime(System.currentTimeMillis());
				inventoryAttributeValue.setDeleted(false);

				ModifiedSqlGenerator.insert(inventoryAttributeValue, InventoryAttributeValue.class, false);

				// insert the vendor
				inventoryAttributeValue = null;
				inventoryAttributeValue = new InventoryAttributeValue();
				inventoryAttributeValue.setValue(vendor);
				inventoryAttributeValue.setInventoryItemID(switchId);
				inventoryAttributeValue.setInventoryAttributeNameID(AttributeNameIdMap.VENDOR);
				inventoryAttributeValue.setLastModificationTime(System.currentTimeMillis());
				inventoryAttributeValue.setDeleted(false);
				ModifiedSqlGenerator.insert(inventoryAttributeValue, InventoryAttributeValue.class, false);

			}
		}

		// add port if non-existent

		long portId = portExists(port);
		if (portId == -1) {
			// add

			List<String> partsOfPortName = getPartsOfPortName(port);
			String portType = partsOfPortName.get(0);
			String portName = partsOfPortName.get(1);

			InventoryItem inventoryItem = new InventoryItem();
			inventoryItem.setInventoryCatagoryTypeID(InventoryConstants.CATEGORY_ID_PORT);
			inventoryItem.setName(portName);

			// problem (//solved)
			if (mode == 1)
				inventoryItem.setParentID(routerId);
			else if (mode == 2)
				inventoryItem.setParentID(switchId);

			inventoryItem.setChildItem(true);
			inventoryItem.setLastModificationTime(System.currentTimeMillis());
			inventoryItem.setDeleted(false);

			ModifiedSqlGenerator.insert(inventoryItem, InventoryItem.class, false);
			portId = portExists(port);

			// add port to the other table

			InventoryAttributeValue inventoryAttributeValue = new InventoryAttributeValue();
			inventoryAttributeValue.setValue(portType);
			inventoryAttributeValue.setInventoryItemID(portId);
			inventoryAttributeValue.setInventoryAttributeNameID(AttributeNameIdMap.PORT_TYPE);
			inventoryAttributeValue.setLastModificationTime(System.currentTimeMillis());
			inventoryAttributeValue.setDeleted(false);
			ModifiedSqlGenerator.insert(inventoryAttributeValue, InventoryAttributeValue.class, false);
		}

		CurrentTimeFactory.destroyCurrentTimeFactory();
	}

	private long districtExists(String district) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_DISTRICT).nameEquals(district)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long upazilaExists(String upazila) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_UPAZILA).nameEquals(upazila)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long unionExists(String union) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_UNION).nameEquals(union)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long popExists(String pop) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_POP).nameEquals(pop)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long routerExists(String router) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_ROUTER).nameEquals(router)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long switchExists(String switchName) throws Exception {
		// router and switch both have id 5
		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_ROUTER).nameEquals(switchName)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	/*
	 * private long virtualLanExists(String virtualLan) throws Exception {
	 * 
	 * List<InventoryItem> list =
	 * ModifiedSqlGenerator.getAllObjectList(InventoryItem.class, new
	 * InventoryItemConditionBuilder().Where()
	 * .inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN)
	 * .nameEquals(virtualLan).getCondition());
	 * 
	 * if (list.isEmpty()) return -1; else return list.get(0).getID(); }
	 */

	private long portExists(String port) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_PORT).nameEquals(port)
						.getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	private long virtualLanExists(String virtualLan, long parentId) throws Exception {

		List<InventoryItem> list = ModifiedSqlGenerator.getAllObjectList(InventoryItem.class,
				new InventoryItemConditionBuilder().Where()
						.inventoryCatagoryTypeIDEquals(InventoryConstants.CATEGORY_ID_VIRTUAL_LAN)
						.nameEquals(virtualLan).parentIDEquals(parentId).getCondition());

		if (list.isEmpty())
			return -1;
		else
			return list.get(0).getID();
	}

	public List<String> getPartsOfPortName(String port) {
		List<String> partsOfPortName = new ArrayList<String>();

		String typeName = "";
		String modelNumber = "";

		for (int i = 0; i < port.length(); i++) {
			if ((port.charAt(i) >= 'a' && port.charAt(i) <= 'z') || (port.charAt(i) >= 'A' && port.charAt(i) <= 'Z'))
				typeName += port.charAt(i);
			else
				modelNumber += port.charAt(i);
		}

		partsOfPortName.add(typeName);
		partsOfPortName.add(modelNumber);

		return partsOfPortName;

	}

}

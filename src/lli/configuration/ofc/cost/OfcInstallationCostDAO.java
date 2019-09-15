package lli.configuration.ofc.cost;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import databasemanager.DatabaseManager;
import util.ModifiedSqlGenerator;

public class OfcInstallationCostDAO {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public OfcInstallationCostDTO insertItem(OfcInstallationCostDTO ofcInstallationCostDTO) throws Exception {
		ModifiedSqlGenerator.insert(ofcInstallationCostDTO);
		return ofcInstallationCostDTO;
	}
	
	public OfcInstallationCostDTO getByID(Long id) throws Exception {
		return ModifiedSqlGenerator.getObjectByID(OfcInstallationCostDTO.class, id);
	}
	
	public OfcInstallationCostDTO getLatestByDate(Long date) throws Exception{
		List<OfcInstallationCostDTO> list = ModifiedSqlGenerator.getAllObjectList(OfcInstallationCostDTO.class, new OfcInstallationCostDTOConditionBuilder()
				.Where()
				.applicableFromLessThanEquals(date)
				.getCondition());
		return list.size() > 0 ? list.get(list.size()-1) : null;
	}
	
//	public OfcInstallationCostDTO addNewOfcInstallationCostDTO(OfcInstallationCostDTO ofcInstallationCostDTO) 
//			throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, JDOMException {
//		String tableName = "lli_local_loop_base_charge";
//		String rows = "(id, fiberLength, fiberCost, createdDate, applicableFrom)";
//		String sql = "INSERT INTO "+tableName+" "+rows+" VALUES (?, ?, ?, ?, ?)";
//		
//		Connection connection = DatabaseManager.getInstance().getConnection();
//		PreparedStatement ps = connection.prepareStatement(sql);
//		
//		int index = 1;
//		ps.setLong(index++, ofcInstallationCostDTO.getId());
//		ps.setInt(index++, ofcInstallationCostDTO.getFiberLength());
//		ps.setInt(index++, ofcInstallationCostDTO.getFiberCost());
//		ps.setLong(index++, ofcInstallationCostDTO.getCreatedDate());
//		ps.setLong(index++, ofcInstallationCostDTO.getApplicableFrom());
//		
//		int numRows = ps.executeUpdate();
//		if (numRows == 1) {
//			return ofcInstallationCostDTO;
//		}
//
//		return null;
//	}
//	
	public OfcInstallationCostDTO createOfcInstallationDTO(Integer fiberLength, Integer oneTimeCost, Integer fiberCost, Long applicableFrom) throws Exception {
		OfcInstallationCostDTO ofcInstallationCostDTO = new OfcInstallationCostDTO();
		
		String tableName = "lli_local_loop_base_charge";
		Long id = DatabaseManager.getInstance().getNextSequenceId(tableName);
		
		ofcInstallationCostDTO.setId(id);
		ofcInstallationCostDTO.setFiberLength(fiberLength);
		ofcInstallationCostDTO.setOneTimeCost(oneTimeCost);
		ofcInstallationCostDTO.setFiberCost(fiberCost);
		ofcInstallationCostDTO.setCreatedDate(System.currentTimeMillis());
		ofcInstallationCostDTO.setApplicableFrom(applicableFrom);
		
		return ofcInstallationCostDTO;
	}
}

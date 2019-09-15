package vpn.ofcinstallation;

import java.util.List;

import org.apache.log4j.Logger;

import util.ModifiedSqlGenerator;

public class DistrictOfcInstallationDAO {
	
	static org.apache.log4j.Logger logger = Logger.getLogger(DistrictOfcInstallationDAO.class);
	
	
	List<DistrictOfcInstallationDTO> getDistrictOfcInstallationDTOByDistrictID(long districtID) throws Exception{
		String conditionString =  " where "+ModifiedSqlGenerator.getColumnName(DistrictOfcInstallationDTO.class, "districtID")+ " = "+districtID;
		return ModifiedSqlGenerator.getAllObjectList(DistrictOfcInstallationDTO.class, conditionString);
	}

	public void insertDistrictOfcInstallationDTO(DistrictOfcInstallationDTO districtOfcInstallationDTO) throws Exception {
		ModifiedSqlGenerator.insert(districtOfcInstallationDTO, DistrictOfcInstallationDTO.class, false);
	}
}

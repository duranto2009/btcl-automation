package lli.configuration;

import java.util.List;

import org.apache.log4j.Logger;

import common.RequestFailureException;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;

public class LLICostConfigurationDAO {
	Logger logger = Logger.getLogger(getClass());
	Class<LLIFixedCostConfigurationDTO> classObject = LLIFixedCostConfigurationDTO.class;
	
	public List<LLIFixedCostConfigurationDTO> getFixedCostConfigurationDTOListByDateRange(long fromDate,long toDate) throws Exception{
		if(fromDate>toDate){
			throw new RequestFailureException("From date must be less than to date");
		}
		List<LLIFixedCostConfigurationDTO> lliFixedCostConfigurationDTOs = ModifiedSqlGenerator.getAllObjectList(classObject, 
				new LLIFixedCostConfigurationDTOConditionBuilder()
				.Where()
				.activationDateLessThanEquals(toDate)
				//.activationDateGreaterThanEquals(fromDate)
				//.isDeleted(false)
				.orderBylastModificationTimeDesc()
				//.orderByactivationDateDesc()
				.limit(1)
				.getCondition());
		return lliFixedCostConfigurationDTOs;
		
	}
	public LLIFixedCostConfigurationDTO getCurrentActiveLLI_FixedCostConfigurationDTO() throws Exception {
		List<LLIFixedCostConfigurationDTO> lliFixedCostConfigurationDTOs = ModifiedSqlGenerator.getAllObjectList(classObject, 
				new LLIFixedCostConfigurationDTOConditionBuilder()
				.Where()
				.activationDateLessThanEquals(CurrentTimeFactory.getCurrentTime())
				.isDeleted(false)
				.orderByactivationDateDesc()
				.limit(1)
				.getCondition());
		return lliFixedCostConfigurationDTOs.isEmpty() ? null : lliFixedCostConfigurationDTOs.get(0);
	}

	public List<LLIFixedCostConfigurationDTO> getAllFutureLLI_FixedCost_ConfigurationDTOs() throws Exception {
		List<LLIFixedCostConfigurationDTO> futureFixedCostConfigurationDTOs = ModifiedSqlGenerator.getAllObjectList( classObject, 
											new LLIFixedCostConfigurationDTOConditionBuilder()
											.Where()
											.activationDateGreaterThan(CurrentTimeFactory.getCurrentTime())
											.isDeleted(false)
											.getCondition()
											);

		return futureFixedCostConfigurationDTOs;
	}

	public void insert(LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO) throws Exception {
		ModifiedSqlGenerator.insert(lliFixedCostConfigurationDTO);
	}
	public void update(LLIFixedCostConfigurationDTO currentFixedCostConfigurationDTO) throws Exception {
		ModifiedSqlGenerator.updateEntityByPropertyList(currentFixedCostConfigurationDTO, classObject, false, false, 
				new String[]{"isDeleted"}, CurrentTimeFactory.getCurrentTime());
	}
}

package lli.configuration;

import java.util.List;

import org.apache.log4j.Logger;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;

public class LLICostConfigurationService {
	public static Logger logger = Logger.getLogger(LLICostConfigurationService.class);
	
	@DAO
	LLICostConfigurationDAO lliCostConfigurationDAO;
	
	@Transactional
	public void insert (LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO ) throws Exception {
		LLIFixedCostConfigurationDTO currentFixedCostConfiguration = getCurrentActiveLLI_FixedCostConfigurationDTO();
		currentFixedCostConfiguration.setDeleted(true);
		lliCostConfigurationDAO.update(currentFixedCostConfiguration);
		lliCostConfigurationDAO.insert(lliFixedCostConfigurationDTO);
	}
	
	@Transactional(transactionType=TransactionType.READONLY)
	public LLIFixedCostConfigurationDTO getCurrentActiveLLI_FixedCostConfigurationDTO() throws Exception {
		LLIFixedCostConfigurationDTO lliFixedCostConfigurationDTO = lliCostConfigurationDAO.getCurrentActiveLLI_FixedCostConfigurationDTO();
		if(lliFixedCostConfigurationDTO == null) {
			throw new RequestFailureException("No LLI OTC Charge Configuration Found");
		}
		return lliFixedCostConfigurationDTO;
	}
	
	@Transactional(transactionType=TransactionType.READONLY)
	public List<LLIFixedCostConfigurationDTO> getLLI_FixedCostConfigurationDTOByDateRange(long fromDate,long toDate) throws Exception {
		return lliCostConfigurationDAO.getFixedCostConfigurationDTOListByDateRange(fromDate, toDate);
	}
	
	@Transactional(transactionType=TransactionType.READONLY)
	public List<LLIFixedCostConfigurationDTO> getAllFutureLLI_FixedCost_ConfigurationDTOs() throws Exception {
		List<LLIFixedCostConfigurationDTO> lliCommonChargeDTOs = lliCostConfigurationDAO.getAllFutureLLI_FixedCost_ConfigurationDTOs();
		return lliCommonChargeDTOs;
	}

}
package lli.configuration.ofc.cost;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.DAO;
import annotation.Transactional;


public class OfcInstallationCostService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@DAO
	OfcInstallationCostDAO ofcInstallationCostDAO;
	
//	public OfcInstallationCostDTO addNewCost(Integer fiberLength, Integer fiberCost, Long applicableFrom) throws Exception {
//		OfcInstallationCostDTO ofcInstallationCostDTO = ofcInstallationCostDAO.createOfcInstallationDTO(fiberLength, fiberCost, applicableFrom);
//		ofcInstallationCostDAO.insertItem(ofcInstallationCostDTO);
//		
//		return ofcInstallationCostDTO;
//	}
	
	public OfcInstallationCostDTO createObject(Integer length, Integer oneTimeCost, Integer cost, Long from) {
		try {
			return ofcInstallationCostDAO.createOfcInstallationDTO(length, oneTimeCost, cost, from);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	public OfcInstallationCostDTO getLatestByDate(Long date) {
		try {
			return ofcInstallationCostDAO.getLatestByDate(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	public OfcInstallationCostDTO insertItem(OfcInstallationCostDTO ofc) {
		try {
			return ofcInstallationCostDAO.insertItem(ofc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

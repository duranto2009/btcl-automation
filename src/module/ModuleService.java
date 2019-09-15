package module;

import java.util.ArrayList;
import java.util.List;

import common.ModuleConstants;
import common.ObjectPair;
import config.GlobalConfigurationRepository;

public class ModuleService {
	
	public List<ObjectPair<Integer, String>> getActiceModuleList() {
		List<ObjectPair<Integer, String>> activeModuleList = new ArrayList<>();
		
		for(Integer moduleID : ModuleConstants.ActiveModuleMap.keySet()) {
			if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(moduleID).getValue() > 0) {
				activeModuleList.add(new ObjectPair<Integer, String>(moduleID, ModuleConstants.ActiveModuleMap.get(moduleID)));
			}
		}
		return activeModuleList;
	}
	
}

package report;

import common.EntityTypeConstant;
import common.ModuleConstants;
import common.StringUtils;
import request.StateRepository;

public class SubqueryBuilderForStatus implements SubqueryBuilder{

	@Override
	public String createSubquery(String status, Integer... moduleID) throws RuntimeException{
		int module = 0;
		if(moduleID.length==1){
			module = moduleID[0];
			if(module == ModuleConstants.Module_ID_CLIENT){
				return StringUtils.getCommaSeparatedString(StateRepository.getInstance().getStatusListByActivationStatus(Integer.parseInt(status)));
			}
		}
		
		
		return StringUtils.getCommaSeparatedString(StateRepository.getInstance().getStatusListByModuleIDAndActivationStatus(module,Integer.parseInt(status)));
	}
}

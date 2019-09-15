package common.actionProcessor;

import request.CommonRequestDTO;

public class LLINewConnectionApplicationVerificationProcessor extends ActionProcessor{

	public LLINewConnectionApplicationVerificationProcessor() {
		setFromState(70101);
		setToState(70102);
	}
	
	@Override
	protected void processAction(CommonRequestDTO commonRequestDTO) throws Exception {
		
//		LliLinkDTO linkDTO = null;
		//update
		//new requDTO submit
	}

	@Override
	protected void rollbackAction(CommonRequestDTO commonRequestDTO) throws Exception {
		// TODO Auto-generated method stub
//		LliLinkDTO linkDTO = null;
		//update
		//new requDTO 
		
		
	}

}

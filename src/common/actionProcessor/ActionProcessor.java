package common.actionProcessor;

import common.RequestFailureException;
import request.CommonRequestDTO;

public abstract class ActionProcessor {
	private Integer fromState;
	private int toState;
	
	protected void setFromState(Integer fromState){
		this.fromState = fromState;
	}
	
	protected void setToState(int toState){
		this.toState = toState;
	}
	
	public int getFromState(){
		return fromState;
	}
	public int getToState(){
		return toState;
	}
	
	private void validateProcessAction(CommonRequestDTO currentRequesDTO) throws Exception{
		if(currentRequesDTO == null && fromState == null){
			return;
		}
		
		if(currentRequesDTO.getState()!=fromState){
			throw new RequestFailureException("");
		}
	}
	private void validateRollbackAction(CommonRequestDTO currentRequestDTO) throws Exception{
		if( currentRequestDTO == null || currentRequestDTO.getState()!=toState){
			throw new RequestFailureException("");
		}
	}
	
	public void process(CommonRequestDTO commonRequestDTO) throws Exception{
		validateProcessAction(commonRequestDTO);
		processAction(commonRequestDTO);
	}
	
	public void rollback(CommonRequestDTO commonRequestDTO) throws Exception{
		validateRollbackAction(commonRequestDTO);
		rollbackAction(commonRequestDTO);
	}
	
	protected abstract void processAction(CommonRequestDTO commonRequestDTO) throws Exception;
	
	protected abstract void rollbackAction(CommonRequestDTO commonRequestDTO) throws Exception;
}

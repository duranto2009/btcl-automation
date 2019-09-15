package request;

import common.EntityTypeConstant;

public class EntityActionGenerator {
	
	public static String getAction(CommonRequestDTO dto){
		String url=EntityTypeConstant.entityStrutsActionMap.get(dto.getEntityTypeID());
		url+=".do?moduleID="+dto.getModuleIDFromThisDTO()+"&entityID="+dto.getEntityID()+"&entityTypeID="+dto.getEntityTypeID(); //for view
		
		//add additional parameter for edit
		return url;
	}
	
	public static String getAction(int moduleID, long entityID, long entityTypeID ){
		Integer entityTypeIDObject = Integer.parseInt(String.valueOf(entityTypeID)); 
		String url=EntityTypeConstant.entityStrutsActionMap.get( entityTypeIDObject );
		url+=".do?moduleID=" + moduleID + "&entityID=" + entityID + "&entityTypeID=" + entityTypeID; //for view
		return url;
	}
	
	public static String getAction(long entityID, long entityTypeID ){
		Integer entityTypeIDObject = Integer.parseInt(String.valueOf(entityTypeID)); 
		String url=EntityTypeConstant.entityStrutsActionMap.get( entityTypeIDObject );
		url+=".do?moduleID=" + (entityTypeID / EntityTypeConstant.MULTIPLIER2) + "&entityID=" + entityID + "&entityTypeID=" + entityTypeID; //for view
		return url;
	}
	
	public static String getManualDemandNoteAction(CommonRequestDTO dto){
		String url="domain/demandNote/manualDemandNote.jsp";
		url+="?moduleID="+dto.getModuleIDFromThisDTO()+"&entityID="+dto.getEntityID()+"&entityTypeID="+dto.getEntityTypeID(); //for view
		return url;
	}
	public static String getDomainViewAction(CommonRequestDTO dto){
		String url="ViewDomain.do";
		url+="?moduleID="+dto.getModuleIDFromThisDTO()+"&entityID="+dto.getEntityID()+"&entityTypeID="+dto.getEntityTypeID(); //for view
		return url;
	}

	
}

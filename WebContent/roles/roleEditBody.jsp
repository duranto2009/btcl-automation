
<%@page import="common.ModuleConstants"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="login.LoginDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="permission.*"%>
<%@page import="request.*"%>
<%@page import="role.RoleDAO"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="util.ServiceDAOFactory"%>
<%@page import="java.util.*"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Logger logger = Logger.getLogger(getClass());
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	RoleDAO dao = new RoleDAO();
	int rolePermission = 3;
	String roleIDstr=request.getAttribute("id").toString(); 
// 	logger.debug(roleIDstr);
	long roleID=Long.parseLong(roleIDstr);
	String actionName = (roleID == -1 ?"/AddRole" : "/UpdateRole");
	
	ArrayList rolePermissionList = dao.getRolePermission(roleIDstr);	
	boolean checked = false;
	HashMap<Integer, String> copiedMap = new HashMap<Integer, String>(ModuleConstants.ModuleMap);
    copiedMap.put(ModuleConstants.Module_ID_GENERAL, "General");
    copiedMap.put(ModuleConstants.Module_ID_CRM, "CRM");
    copiedMap.remove(ModuleConstants.Module_ID_IIG);
    copiedMap.remove(ModuleConstants.Module_ID_IPADDRESS);
    SortedSet<Integer> sortedModuleSet = new TreeSet<Integer>(copiedMap.keySet());
    
    Map<Integer,MenuDTO> mapOfMenuToMenuID = new HashMap<Integer,MenuDTO> ();
    Map<Integer,List<MenuDTO>> mapOfMenuDTOListToModuleID = new HashMap<Integer,List<MenuDTO>>();
    
    List<MenuDTO> allMenuList = ServiceDAOFactory.getService(RoleDAO.class).getAllMenuList();
	Map<Integer, PermissionDTO> permissionDTOFromPermissionTable = PermissionRepository.getInstance().getMenuPermissionMapByRoleID(roleID); // already done
    for(MenuDTO menuDTO: allMenuList){
    	if(!mapOfMenuDTOListToModuleID.containsKey(menuDTO.getModuleTypeID())){
    		mapOfMenuDTOListToModuleID.put(menuDTO.getModuleTypeID(), new ArrayList<MenuDTO>());
    	}
    	if(menuDTO.getParentMenuID() == -1){
    		mapOfMenuDTOListToModuleID.get(menuDTO.getModuleTypeID()).add(menuDTO);
    	}
    	mapOfMenuToMenuID.put(menuDTO.getMenuID(), menuDTO);
    }
    
    for(MenuDTO menuDTO: allMenuList){
    	int parentMenuID = menuDTO.getParentMenuID();
    	if(mapOfMenuToMenuID.containsKey(parentMenuID)){
    		mapOfMenuToMenuID.get(parentMenuID).addChildPermission(menuDTO);
    	}
    }
    
    
    Map<Integer, ColumnDTO> mapOfColumnPermissionDTOToModuleID = new HashMap<Integer, ColumnDTO>();
    Map<Integer, List<ColumnDTO>> mapOfColumnPermissionDTOListToModuleID = new HashMap<Integer, List<ColumnDTO>>();
    
    List<ColumnDTO> columnDTOs = ServiceDAOFactory.getService(RoleDAO.class).getAllColumnList();
    Map<Integer, ColumnPermissionDTO> columnPermissionDTOFromPermissionTable = PermissionRepository.getInstance().getColumnPermissionMapByRoleID(roleID);
    
    for(ColumnDTO columnDTO: columnDTOs){
    	if(!mapOfColumnPermissionDTOListToModuleID.containsKey(columnDTO.getModuleTypeID())){
    		mapOfColumnPermissionDTOListToModuleID.put(columnDTO.getModuleTypeID(), new ArrayList<ColumnDTO>());
    	}
 		mapOfColumnPermissionDTOListToModuleID.get(columnDTO.getModuleTypeID()).add(columnDTO);
    }
    
    
    Set<Integer> saIDSet = PermissionRepository.getInstance().getStateActionTypeSetByRoleID(roleID);
	Map<Integer, StateActionDTO> stateActionTypeMap = PermissionRepository.getInstance().getStateActionTypeMapByRoleID(roleID);
	HashMap<Integer, Set<Integer>> enabledStateActionMap = new HashMap<Integer, Set<Integer>>();
	if(stateActionTypeMap != null)
	{			
		for(Integer saID: stateActionTypeMap.keySet())
		{
			Set<Integer> actionSet = enabledStateActionMap.get(stateActionTypeMap.get(saID).getStateID());
			if(actionSet == null){
				actionSet = new HashSet<Integer>();												
			}
			actionSet.addAll(stateActionTypeMap.get(saID).getActionTypeIDs());
			enabledStateActionMap.put(stateActionTypeMap.get(saID).getStateID(), actionSet);
		}
	}
	logger.debug("enabledStateActionMap " + enabledStateActionMap);
%>
<html:base />
<%try{ %>
 <html:form  action="<%=actionName%>" method="POST"  styleClass="form-horizontal" styleId='roleEditId'>
     <html:hidden property="roleID" />
			<div class="portlet box portlet-btcl">
				<div class="portlet-title">
					<div class="caption">
						<div class="caption"><i class="icon-plus"></i><%=request.getParameter("title")%></div>
					</div>
				</div>
				<div class="portlet-body form">
					<div class="form-body ">
						<div class="form-group">
							<label class="col-md-4 control-label">Role Name <span class="required"> * </span></label>
							<div class="col-md-4">
								<html:text property="rolename" style="width:100%;" styleClass="form-control"/>
								<html:hidden property="parentRoleID"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label">Description <span class="required"> * </span></label>
							<div class="col-md-4">
								<html:text property="roleDesc" style="width:100%;" styleClass="form-control" />
							</div>
						</div>
								
					<br><hr>
                	<div class="tabbable-custom ">
	                    <ul class="nav nav-tabs ">
	                    <%
	                    for(int r : sortedModuleSet){
	                    	if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(r).getValue() > 0){
	                    %>
	                        <li class=<%=(r==ModuleConstants.Module_ID_DOMAIN)?"active": ""%>>
	                            <a href="#tab_1_1_<%=r%>" data-toggle="tab"> <%=copiedMap.get(r) %> </a>
	                        </li>
	                    	<%}
	                    }%>
	                    </ul>
	                    <div class="tab-content">
	                    <%
	                  
							for(int p : sortedModuleSet){
								if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(p).getValue() == 0){
									continue; 
								}
						%>
							<div class="tab-pane <%if(p==ModuleConstants.Module_ID_DOMAIN){%>active<%}%>" id="tab_1_1_<%=p%>">
	                     		<div class="portlet light">
									<div class="portlet-title">
										<div class="caption"><i class="fa fa-list"></i> Menu Permission </div>
									</div>
									<div class="portlet-body">	
										<table class="table table-striped table-bordered table-hover" >
											<tr>									
						                    	<td width="55%" class="td_viewheader" align="center" colspan="5">Menu</td>							
						                    	<td width="15%" class="td_viewheader" align="center">Full</td>
						                    	<td width="15%" class="td_viewheader" align="center">Modify</td>
						                    	<td width="15%" class="td_viewheader" align="center">Read Only</td>
							                </tr>
							                <%
							                 	List<MenuDTO> data =   mapOfMenuDTOListToModuleID.get(p); ///dao.getAllMenuPermissionList(p);
								                for(int i=0;i<data.size();i++){								                	
								                	MenuDTO dto = (MenuDTO)data.get(i);
									                if(loginDTO.getMenuPermission(dto.getMenuID())==-1){	
									                     continue;
									                }
									                int rpx;
									               /* for( rpx = 0; rpx < rolePermissionList.size(); rpx++) {
									                	PermissionDTO userPermission = (PermissionDTO )rolePermissionList.get(rpx);
									                    if( dto.getMenuID() == userPermission.getMenuID()){
										                   dto.setPermissionType(userPermission.getPermissionType());
									                       break;
									                    }	
									                }
								                  	if( rpx == rolePermissionList.size()){
														dto.setPermissionType(-1);
								                  	}			*/						
								                  	ArrayList<MenuDTO> child2 = dto.getChildMenuList();
													StringBuilder childMenuIDList = new StringBuilder(5000);
								                  	for(int cIndex=0;cIndex<child2.size();cIndex++){
								                  		MenuDTO childDTO=(MenuDTO)child2.get(cIndex);
														childMenuIDList.append(childDTO.getMenuID()).append(";");
								                	  	ArrayList<MenuDTO> leaf=(ArrayList<MenuDTO>)childDTO.getChildMenuList();
								                	  	for(int leafIndex=0;leafIndex<leaf.size();leafIndex++){
								                		  	int leafDTOMenuID = leaf.get(leafIndex).getMenuID();
								                		  	childMenuIDList.append(leafDTOMenuID).append(";").
								                		  					append(leafDTOMenuID).append("Full;").
								                		  					append(leafDTOMenuID).append("Modify;").
								                		  					append(leafDTOMenuID).append("Read;");
								                	  	}
								                  	}
								            %>
						                  	<tr>
						                  	<%
						                  	checked = permissionDTOFromPermissionTable == null ? false : permissionDTOFromPermissionTable.get(dto.getMenuID()) != null;
						                  	%>
							                	<td width="5%" class="td_viewdata1" align="left">
							                		<input type="checkbox" name="Menu" parent=<%=dto.getParentMenuID()%> 
							                		value=<%=dto.getMenuID()%> <%=checked?"checked=\"checked\"":""%> id="<%=dto.getMenuID()%>" 
							                		onClick="validateSelection(this,'','<%=childMenuIDList%>')" />
							                	</td>
							                  	<td width="45%"  colspan="4" style="font-weight: bold" class="td_viewdata2" align="left"><%=dto.getMenuName()%></td>
							                  	<td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
							                  	<td width="15%" class="td_viewdata2" align="center">&nbsp;</td>
							                  	<td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
							             	</tr>
									        <%
											for( int j=0; j< child2.size();j++){													         
												MenuDTO dto2 = (MenuDTO)child2.get(j);
					                         	if(loginDTO.getMenuPermission(dto2.getMenuID())==-1){	
									        		continue;
					                         	}/*
					                         	int rpy;
					                         	for( rpy = 0; rpy < rolePermissionList.size(); rpy++){
				                                  	PermissionDTO userPermission = (PermissionDTO )rolePermissionList.get(rpy);
				                                   	if( dto2.getMenuID() == userPermission.getMenuID()){
				                                     		dto2.setPermissionType(userPermission.getPermissionType());
				                                      	break;
				                                   	}
					                         	}
					                         	if( rpy == rolePermissionList.size()){
					                           		dto2.setPermissionType(-1);
					                         	}*/
							                    ArrayList child3 = dto2.getChildMenuList();
							                    String multiplePullDown2= "showPullDown('pullDown_" +p+"_"+dto2.getMenuID()+"');";     
												StringBuilder leafMenuIDList = new StringBuilder(5000);
							                    for(int lcIndex=0;lcIndex<child3.size();lcIndex++){
							                    	MenuDTO tempDTO4 = (MenuDTO)child3.get(lcIndex);
					                          		int child3MenuID = tempDTO4.getMenuID();
					                          		leafMenuIDList.append(child3MenuID).append(";");
					                          		leafMenuIDList.append(child3MenuID).append("Full;");
					                          		leafMenuIDList.append(child3MenuID).append("Modify;");
					                          		leafMenuIDList.append(child3MenuID).append("Read;");
													if(tempDTO4!=null){
								                    	ArrayList<MenuDTO> tempChild4 = tempDTO4.getChildMenuList();
								                        if(tempChild4!=null || (tempChild4!=null && tempChild4.size()>0)){
								                        for(int innerIndex=0;innerIndex<tempChild4.size();innerIndex++){
						                           			int tempChild4MenuID = ((MenuDTO)tempChild4.get(innerIndex)).getMenuID();
					                           				int tempPermission=loginDTO.getMenuPermission(tempChild4MenuID);
						               	         			if(tempPermission==-1){	
						                             	  		continue;
						                               		}
							                          		leafMenuIDList.append(tempChild4MenuID).append(";");
							                          		leafMenuIDList.append(tempChild4MenuID).append("Full;");
							                          		leafMenuIDList.append(tempChild4MenuID).append("Modify;");
							                          		leafMenuIDList.append(tempChild4MenuID).append("Read;");
						                           		}   
									               	}
								                 }
							                 }
							                 %>
				                          	<tr>
				                          	<%
						                  	checked = permissionDTOFromPermissionTable == null ? false: permissionDTOFromPermissionTable.get(dto2.getMenuID()) != null;
						                  	%>
							                  	<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent="<%=dto2.getParentMenuID()%>" 
							                  	value="<%=dto2.getMenuID()%>" <%=checked?"checked=\"checked\"":""%>  id="<%=dto2.getMenuID()%>" 
							                  	onClick="validateSelection(this,'<%=dto2.getParentMenuID()%>','<%=leafMenuIDList%>')" /></td>
							                  	<td width="5%"  class="td_viewdata1 menuItem" align="center">
					                          		<a class="menuItem" onMouseOver="overMenuItem(this);" onMouseOut="outMenuItem(this);" onClick="<%=multiplePullDown2%>">
														<i class="icon-control-play icons triangle"></i>							    	
											 		</a>
					    						</td>
						                		<td width="40%" class="td_viewdata2" align="left" colspan="3"><%="&nbsp;&nbsp;&nbsp;"+dto2.getMenuName()%></td>
								                <td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
								                <td width="15%" class="td_viewdata2" align="center">&nbsp;</td>
								                <td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
					                		</tr>
					                		<tbody id=<%=("pullDown_" +p+"_"+dto2.getMenuID())%> style="display:none;">
											<%
								            	int permission=-1;
												for( int k=0; k< child3.size();k++){														             
													MenuDTO dto3 = (MenuDTO)child3.get(k);
					                	         	permission=loginDTO.getMenuPermission(dto3.getMenuID());
										            if(permission==-1){            	
					                 	              continue;
					                                }/*
													int rp;
													for( rp = 0;rp<rolePermissionList.size();rp++){
													   PermissionDTO userPermission = (PermissionDTO )rolePermissionList.get(rp);
													   if( dto3.getMenuID() == userPermission.getMenuID()){
													     	dto3.setPermissionType(userPermission.getPermissionType());
													     break;
													   }
													}
			                                       	if( rp == rolePermissionList.size()){
			                                       		dto3.setPermissionType(-1);
			                                       	}*/
													//int permissionCheck=dto3.getPermissionType();
			                                       	int permissionCheck = -1;
			                                       	if(permissionDTOFromPermissionTable !=null && permissionDTOFromPermissionTable.get(dto3.getMenuID()) != null)
			                                       	{
				                                       	permissionCheck = permissionDTOFromPermissionTable.get(dto3.getMenuID()).getPermissionType();
														if(permission<permissionCheck){
															permissionCheck=permission;
														}
			                                       	}             	
						                            ArrayList<MenuDTO> child4 = dto3.getChildMenuList();
									               	if(child4==null || (child4!=null && child4.size()==0)){ 
									        %>
									        <tr>
									        <%
						                  	checked = permissionDTOFromPermissionTable == null?false : permissionDTOFromPermissionTable.get(dto3.getMenuID()) != null;
						                  	%>
						                  		<td width="5%" class="td_viewdata1">&nbsp;</td>
						                  		<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" 
						                  		parent="<%=dto3.getParentMenuID()%>" value="<%=dto3.getMenuID()%>" <%=checked?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()%>" 
						                  		onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()%>','');checkPermissionType(this)"/></td>
			        	        		  		<td width="40%" class="td_viewdata2" align="left" colspan="3"><%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto3.getMenuName()%></td>
						                  		<td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Full" value=<%=dto3.getMenuID()%> align="middle"  <%=permissionCheck ==3?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Full"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Full','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')" /></td>
					        	          		<td width="15%" class="td_viewdata2" align="center"><input type="checkbox" name="Modify" value=<%=dto3.getMenuID()%> align="middle" <%=permissionCheck ==2?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Modify"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Modify','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')"/></td>
					                	  		<td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read" value=<%=dto3.getMenuID()%> align="middle" <%=permissionCheck ==1?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Read"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Read','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')"/></td>
										    </tr>
									        		<%}else{%>
									                			
								            <%
												String multiplePullDown3="";
				                		 		for( int y=0; y< child4.size();y++){
				                		 			MenuDTO dto4 = (MenuDTO)child4.get(y);
				                		 			permission=loginDTO.getMenuPermission(dto4.getMenuID());
						               	         	if(permission==-1){	
						                             	  continue;
						                            }
			                		 				multiplePullDown3+="showPullDown('pullDown_" +p+"_"+dto2.getMenuID()+"_"+dto3.getMenuID()+"_"+dto4.getMenuID()+"');";
				                		 		}
												StringBuilder leaf4MenuIDList = new StringBuilder(5000);
					                           	for(int lcIndex=0;lcIndex<child4.size();lcIndex++){
					                           		permission=loginDTO.getMenuPermission(((MenuDTO)child4.get(lcIndex)).getMenuID());
						               	         	if(permission==-1){	
						                             	  continue;
						                            }
						               	         	int child4MenuID = ((MenuDTO)child4.get(lcIndex)).getMenuID();
						               	         	leaf4MenuIDList.append(child4MenuID).append(";");
						               	         	leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Full;");
						               	         	leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Modify;");
						               	      		leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Read;");
									            }
					                           	
					                           	//
// 					                           	checked = permissionDTOFromPermissionTable == null ? false: 
// 					                           		permissionDTOFromPermissionTable.get(dto3.getMenuID()) != null;
					                           	//
					                           	int permissionCheck3 = -1;
		                                       	if(permissionDTOFromPermissionTable !=null && permissionDTOFromPermissionTable.get(dto3.getMenuID()) != null)
		                                       	{
			                                       	permissionCheck3 = permissionDTOFromPermissionTable.get(dto3.getMenuID()).getPermissionType();
													if(permission<permissionCheck3){
														permissionCheck3=permission;
													}
		                                       	}
		                                       	
		                                       	
// 					                           	PermissionDTO permissionDTOFor3rdLvl = PermissionRepository.getInstance().getMenuPermissionMapByRoleID(roleID).get(dto3.getMenuID());
// 		                                       	int permissionCheck3 = -1;
// 		                                       	if(permissionDTOFor3rdLvl != null) {
// 		                                       		permissionCheck3 = permissionDTOFor3rdLvl.getPermissionType();//dto4.getPermissionType();
// 		                                       	}
		                                        
// 		                                        if(permission<permissionCheck3){
// 		                                    	    permissionCheck3=permission;
// 		                                       	}
							               %>
									       <tr>
				                		 		<td width="5%"  class="td_viewdata1">&nbsp;</td>
							                  	<td width="5%" class="td_viewdata1" align="center">
							                  		<input type="checkbox" name="Menu" 
							                  		parent=<%=dto3.getParentMenuID()%> 
							                  		value=<%=dto3.getMenuID()%> <%=permissionCheck3!=-1?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID()+";"+dto3.getParentMenuID()%>','<%=leaf4MenuIDList%>')"/>
							                  	</td>
							                  	<td width="5%"  class="td_viewdata1 menuItem" align="center">
					                             	<a class="menuItem" onMouseOver="overMenuItem(this);" onMouseOut="outMenuItem(this);" onClick="<%=(multiplePullDown3)%>">
					    								<i class="icon-control-play icons triangle"></i>
					    							</a>
					    						</td>
					                		  	<td width="40%" class="td_viewdata2" align="left" colspan="2"><%="&nbsp;&nbsp;&nbsp;"+dto3.getMenuName()%></td>
							                  	<td width="15%" class="td_viewdata1" align="center">&nbsp;</td>
							                  	<td width="15%" class="td_viewdata2" align="center">&nbsp;</td>
							                  	<td width="15%" class="td_viewdata1" align="center">&nbsp;</td>
					                		</tr>
				                			<% 
				                		 		for( int m=0; m< child4.size();m++){
				                		 			MenuDTO dto4 = (MenuDTO)child4.get(m); 
				                		 			permission=loginDTO.getMenuPermission(dto4.getMenuID());
						               	         	if(permission==-1){	
						                             	  continue;
					                               	}/*
						               	        	int rp4;
			                                       	for( rp4 = 0;rp4<rolePermissionList.size();rp4++){
			                                        	PermissionDTO userPermission = (PermissionDTO )rolePermissionList.get(rp4);
			                                           	if( dto4.getMenuID() == userPermission.getMenuID()){
			                                        		dto4.setPermissionType(userPermission.getPermissionType());
			                                            	break;
			                                          	}
			                                       	}
			                                       	if( rp4 == rolePermissionList.size()){
			                                        	dto4.setPermissionType(-1);
			                                       	}*/
			                                       	
// 			                                       	checked = permissionDTOFromPermissionTable == null ? false: 
// 						                           		permissionDTOFromPermissionTable.get(dto4.getMenuID()) != null;
			                                  
			                                       	int permissionCheck4 = -1;
			                                       	if(permissionDTOFromPermissionTable !=null && permissionDTOFromPermissionTable.get(dto4.getMenuID()) != null)
			                                       	{
				                                       	permissionCheck4 = permissionDTOFromPermissionTable.get(dto4.getMenuID()).getPermissionType();
														if(permission<permissionCheck4){
															permissionCheck4=permission;
														}
			                                       	}
			                                       	
			                                       	
// 			                                       	PermissionDTO permissionDTOFor4thLvl = PermissionRepository.getInstance().getMenuPermissionMapByRoleID(roleID).get(dto4.getMenuID());
// 			                                       	int permissionCheck4 = -1;
// 			                                       	if(permissionDTOFor4thLvl != null) {
// 			                                       		permissionCheck4 = permissionDTOFor4thLvl.getPermissionType();//dto4.getPermissionType();
// 			                                       	}
			                                        
// 			                                        if(permission<permissionCheck4){
// 			                                    	    permissionCheck4=permission;
// 			                                       	}
					                 		%>
			                 				<tr id=<%=("pullDown_" +p+"_"+dto2.getMenuID()+"_"+dto3.getMenuID()+"_"+dto4.getMenuID())%> style="display:none;">
								                  <td width="5%" class="td_viewdata1">&nbsp;</td>
								                  <td width="5%" class="td_viewdata1">&nbsp;</td>
								                  <td width="5%" class="td_viewdata1" align="center">
								                  <input type="checkbox" name="Menu" parent=<%=dto4.getParentMenuID()%> 
								                  value=<%=dto4.getMenuID()%> <%=permissionCheck4!=-1?"checked=\"checked\"":""%>  
								                  id="<%=dto4.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()+";"+dto4.getParentMenuID()%>','');checkPermissionType(this)"/></td>
					        	        		  <td width="40%" class="td_viewdata2" align="left" colspan="2">
					        	        		  <%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto4.getMenuName()%></td>
					        	        		  <td width="15%" class="td_viewdata1"align="center"><%if(permission>2){%><input type="checkbox" name="Full" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Full"%>" align="middle"  <%=permissionCheck4 ==3?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Full','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>					        	        		  
								                  <td width="15%" class="td_viewdata2" align="center"><%if(permission>1){%><input type="checkbox" name="Modify" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Modify"%>" align="middle" <%=permissionCheck4 ==2?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Modify','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>
							                	  <td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read"   value=<%=dto4.getMenuID()%> align="middle" id="<%=dto4.getMenuID()+"Read"%>" <%=permissionCheck4 ==1?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Read','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /></td>
					                		</tr>
								                <%}%>
									         <%}%>
								           <%}%>
									    </tbody>
									<% }
								}%>					
									</table>
								</div>
							</div>
						
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-globe"></i>Column Permission
									</div>
								</div>
							<% 
								List<ColumnDTO> columnPermissionList = mapOfColumnPermissionDTOListToModuleID.getOrDefault(p, new ArrayList<ColumnDTO>());//dao.getRoleColumnPermission(roleIDstr,p);
// 								ArrayList allColumnPermissionList = dao.getColumnPermissionList(p); 
								if(columnPermissionList.size() > 0){ 
									int numOfColumns = 4;
							%>
								<div class="portlet-body table-responsive">															
				              		<table  class="table table-striped table-bordered table-hover">
				                    <%
										for(int i=0;i<=columnPermissionList.size()/numOfColumns;i++){
									%>
										<tr>
									<%
											for(int j=0;j<numOfColumns;j++){
												if(i * numOfColumns +  j == columnPermissionList.size()){
													break;
												}
												ColumnDTO columnDTO = columnPermissionList.get(i*numOfColumns + j);	
												String check = "";
												if(columnPermissionDTOFromPermissionTable.get(columnDTO.getColumnID()) != null){
						                      		check = "checked=\"checked\"";
						                      	}
									%>
											<td class="td_viewdata2">
												<input type="checkbox" name="Column"  value=<%=columnDTO.getColumnID()+" "+ check %>/>
													<%=columnDTO.getColumnName()%> 
											</td>
									<%		}%>
										</tr>
									<%}%>
								    </table>
								</div>
								<%}%>
							</div>	
							
							
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-globe"></i>State Permission
									</div>
								</div>
								<div class="portlet-body">	
							<%
								CommonRequestDTO comDTO = new CommonRequestDTO();
								comDTO.setModuleID(p);
								comDTO.setVisibleOnly(false);
								Set<Integer> rootReqIDs = RequestActionStateRepository.getInstance().getAllActions(comDTO, loginDTO);
								logger.debug("rootReqIDs " + rootReqIDs);
								logger.debug("rootReqIDs size " + rootReqIDs.size());
								RequestUtilDAO rudao = new RequestUtilDAO();
								HashMap<Integer, ArrayList<StateDTO>> stateByRequest = rudao.getStatesRelatedToRootRequestTypeIDs(rootReqIDs);
								logger.debug("stateByRequest " + stateByRequest);
								logger.debug("stateByRequest size " + stateByRequest.size());
								int blankState = -(p * ModuleConstants.MULTIPLIER);
								int rangeLower = p * ModuleConstants.MULTIPLIER;
								int rangeUpper = (p + 1) * ModuleConstants.MULTIPLIER;
									
								/* HashMap<Integer, Set<Integer>> enabledStateActionMap = new HashMap<Integer, Set<Integer>>();
								if(saIDSet != null){
									for(Integer saID: saIDSet){				
										StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
										if(sadto == null)continue;
										int action = sadto.getActionTypeIDs().iterator().next();
										int actionAbs = Math.abs(action);
										if(actionAbs >= rangeLower || actionAbs <= rangeUpper){
											Set<Integer> actionSet = enabledStateActionMap.get(sadto.getStateID());
											if(actionSet == null){
												actionSet = new HashSet<Integer>();												
											}
											actionSet.add(action);
											enabledStateActionMap.put(sadto.getStateID(), actionSet);
										}
									}
								} */

								
								
								
								//logger.debug("enabledStateActionMap " + enabledStateActionMap);
							%>
							<div class="panel-group accordion" id="accordion2">
							<%
								int q = 0;
								if(stateByRequest != null){
								for(Integer reqType:stateByRequest.keySet()){			
									q++;
							%>									    
	                        	<div class="panel panel-default">
	                                <div class="panel-heading">
	                                    <h4 class="panel-title">
	                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href='#<%=p%><%=reqType%>'> <%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(reqType).getDescription()%> </a>
	                                    </h4>
	                                </div>
			                        <%
										boolean showChecked = false;                                    		
										if(enabledStateActionMap.get(blankState) != null){
											if(enabledStateActionMap.get(blankState).contains(reqType)){
												showChecked = true;		
											}
										}                                            
									%>
	                                <div id="<%=p%><%=reqType%>" class="panel-collapse collapse">
	                                	<div class="panel-body">
											<table class="table table-striped table-bordered table-hover">
												<tbody>
													<tr>
														<th width="20%">State</th>
														<th width="80%">Action</th>
													</tr>
													<tr>
														<td></td>
														<td>
															<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 checkbox-list">													
																	<input type="checkbox" name="saID" value='<%=(blankState +"_"+ reqType)%>' <%if(showChecked){%>checked='checked'<%}%> ><%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(reqType).getDescription()%>
															</div>
														</td>
													</tr>
													
													<%
	                                                    ArrayList<StateDTO> slist = stateByRequest.get(reqType);
	                                         			q = 0;
	                                                    for(StateDTO sdto: slist){
	                                              	%>
	                                                    	<tr>
																<td>
																	<%=sdto.getName()%>
																</td>
																<td>
																	<%
																		StateActionDTO sactionDTO =  RequestStateActionRepository.getInstance().getStateActionByStateID(sdto.getId());
																		if(sactionDTO != null){
																			Set<Integer> permittedActions = loginDTO.getActionPermission(sdto.getId());
																			if(permittedActions == null)continue;
																			for(Integer action : sactionDTO.getActionTypeIDs()){
																				ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(action);
																				if(asdto == null || !asdto.isSystemAction()) continue;
																				if(!permittedActions.contains(asdto.getActionTypeID()))continue;
																	%>
																					<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 checkbox-list">
																					<%
																						showChecked = false;
																						if(enabledStateActionMap.get(sdto.getId()) != null){
																							if(enabledStateActionMap.get(sdto.getId()).contains(asdto.getActionTypeID())){
																								showChecked = true;		
																							}
																						}
																					%>
																						<input type="checkbox" name="saID" value='<%=(sactionDTO.getStateID() +"_"+ asdto.getActionTypeID())%>' <%if(showChecked){%>checked='checked'<%}%> ><%=asdto.getDescription()%>
																					</div>
																	<%
																			}
																		}
																	%>
																</td>
															</tr>	
														<%}%>
													</tbody>
												</table>
	                                         </div>
	                                      </div>
	                                   </div>
						<%
								}
							}
						%>
						</div>
					</div>									
				</div>											
							
				</div>
			<%}%>
		</div>
	</div>
</div>
							<div class="form-actions fluid">
								<div class="row">
									<div class="col-md-offset-5 col-md-4">
										<a class="btn btn-cancel-btcl" href="<%=request.getHeader("referer")%>">Cancel</a>	
										<%if (rolePermission == 3) {%>
											<input class="btn btn-submit-btcl" type="submit" value="Submit" name="B2">
										<%}%>
									</div>
								</div>
							</div>
						</div>
					</div>
			 </html:form>
<%} catch(Exception ex){
	logger.fatal("",ex);
}%>               							
             							
               															
<script  src="../scripts/util.js"></script>
<script  src="../scripts/menu.js"></script>
<script  src="../roles/role.js"></script>


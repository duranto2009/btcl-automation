
<%@page import="common.RequestFailureException"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="com.sun.xml.internal.ws.api.server.Module"%>
<%@page import="permission.PermissionDTO"%>
<%@page import="request.StateRepository"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="permission.ActionPermissionRepository"%>
<%@page import="java.util.Set"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.RequestUtilDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="request.RequestStateActionRepository"%>
<%@page import="request.StateDTO"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="common.ModuleConstants"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="role.RoleDAO"%>
<%@page import="login.LoginDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="role.ColumnPermissionDTO"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	Logger logger = Logger.getLogger(getClass());
	
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
	RoleDAO dao = new RoleDAO();
	int rolePermission = 3;
	
	String roleIDstr=request.getAttribute("id").toString(); logger.debug(roleIDstr);
	long roleID=Long.parseLong(roleIDstr);
	ArrayList rolePermissionList = dao.getRolePermission(roleIDstr);	
	
%>
<html:base />

<%try{ %>
 <html:form  action="/UpdateRole" method="POST"  styleClass="form-horizontal" styleId='roleEditId'>
     <html:hidden property="roleID" />
			<div class="portlet box portlet-btcl">
				<div class="portlet-title">
					<div class="caption">
						<div class="caption"><i class="icon-plus"></i>Edit Role</div>
					</div>
				</div>
				<div class="portlet-body form">
					<div class="form-body ">
						<div class="form-group">
							<label class="col-md-4 control-label">Role Name <span class="required"> * </span></label>
							<div class="col-md-4">
								<html:text property="rolename" style="width:100%;" styleClass="form-control"/>
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
	                    HashMap<Integer, String> copiedMap = new HashMap<Integer, String>(ModuleConstants.ModuleMap);
	                    copiedMap.put(ModuleConstants.Module_ID_GENERAL, "General");
	                    copiedMap.put(ModuleConstants.Module_ID_CRM, "CRM");
			            copiedMap.remove(ModuleConstants.Module_ID_IIG);
			            copiedMap.remove(ModuleConstants.Module_ID_IPADDRESS);
	                    SortedSet<Integer> sortedModuleSet = new TreeSet<Integer>(copiedMap.keySet());
	                    for(int r : sortedModuleSet){
	                    	if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(r).getValue() > 0){
	                    %>
	                        <li class="<%if(r==ModuleConstants.Module_ID_DOMAIN){%>active<%}%>">
	                            <a href="#tab_1_1_<%=r%>" data-toggle="tab"> <%=copiedMap.get(r) %> </a>
	                        </li>
	                    	<%}
	                    }%>
	                    </ul>
	
	                                            <!-- <div class="tab-pane active" id="tab_1_1_1"> -->
	                    <div class="tab-content">
	                   
						<%for(int p : sortedModuleSet){
		                   
							if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(p).getValue() == 0){
									continue; 
								 }
							boolean firstRow = true;
						%>
							<div class="tab-pane <%if(p==ModuleConstants.Module_ID_DOMAIN){%>active<%}%>" id="tab_1_1_<%=p%>">								
	
	                     			<div class="portlet light">
									<div class="portlet-title">
										<div class="caption"><i class="fa fa-list"></i> Menu Permission </div>
									</div>
									<div class="portlet-body">	
												<table class="table table-striped table-bordered table-hover" >
												<tr >									
							                    	<td width="55%" class="td_viewheader" align="center" colspan="5">Menu</td>							
							                    	<td width="15%" class="td_viewheader" align="center"><!--Full-->Full</td>
							                    	<td width="15%" class="td_viewheader" align="center"><!--Modify-->Modify</td>
							                    	<td width="15%" class="td_viewheader" align="center"><!--Read Only-->Read Only</td>
							                  	</tr>
							                  	<% ArrayList data = dao.getAllMenuPermissionList(p);
							                  	for(int i=0;i<data.size();i++){								                	
								                	PermissionDTO dto = (PermissionDTO)data.get(i);
									                 if(loginDTO.getMenuPermission(dto.getMenuID())==-1){	
									                      continue;
									                 }
								                  	int rpx;
								                 	for( rpx = 0; rpx < rolePermissionList.size(); rpx++) {
								                    	PermissionDTO userPermission = (PermissionDTO )rolePermissionList.get(rpx);
								                      	if( dto.getMenuID() == userPermission.getMenuID()){
									                    	dto.setPermissionType(userPermission.getPermissionType());
								                         	break;
								                      	}	
								                  	}
								                  	if( rpx == rolePermissionList.size()){
								                         dto.setPermissionType(-1);
								                  	}									
								                  
								                  	ArrayList child2 = dto.getChildPermissionList();
								                  							                
// 								                  	String childMenuIDList="";

													StringBuilder childMenuIDList = new StringBuilder(5000);
								                  	for(int cIndex=0;cIndex<child2.size();cIndex++){
								                	  PermissionDTO childDTO=(PermissionDTO)child2.get(cIndex);
// 								                	  childMenuIDList+=childDTO.getMenuID() + ";";
														childMenuIDList.append(childDTO.getMenuID()).append(";");
								                	  ArrayList<PermissionDTO> leaf=(ArrayList<PermissionDTO>)childDTO.getChildPermissionList();
								                	  for(int leafIndex=0;leafIndex<leaf.size();leafIndex++){
								                		  int leafDTOMenuID = leaf.get(leafIndex).getMenuID();
								                		  
								                		  childMenuIDList.append(leafDTOMenuID).append(";").
								                		  					append(leafDTOMenuID).append("Full;").
								                		  					append(leafDTOMenuID).append("Modify;").
								                		  					append(leafDTOMenuID).append("Read;");
// 								                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + ";";
// 								                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Full" + ";";
// 								                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Modify" + ";";
// 								                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Read" + ";";
								                	  }
								                  	}%>
								                  	<tr>
									                	<td width="5%" class="td_viewdata1" align="left"><input type="checkbox" name="Menu" parent=<%=dto.getParentMenuID()%> 
									                	value=<%=dto.getMenuID()%> <%=dto.getPermissionType() !=-1?"checked=\"checked\"":""%> id="<%=dto.getMenuID()%>" 
									                	onClick="validateSelection(this,'','<%=childMenuIDList%>')" /></td>
									                  	<td width="45%"  colspan="4" style="font-weight: bold" class="td_viewdata2" align="left"><%=dto.getMenuName()%></td>
									                  	<td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
									                  	<td width="15%" class="td_viewdata2" align="center">&nbsp;</td>
									                  	<td width="15%" class="td_viewdata1"align="center">&nbsp;</td>
									             	</tr>
									             	<%//ArrayList child2 = dto.getChildPermissionList();
													for( int j=0; j< child2.size();j++){													         
							                        	PermissionDTO dto2 = (PermissionDTO)child2.get(j);
							                         	if(loginDTO.getMenuPermission(dto2.getMenuID())==-1){	
											        		continue;
							                         	}
						                		 		
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
							                         	}
							                                 
							                         	ArrayList child3 = dto2.getChildPermissionList();
							                         	String multiplePullDown2= "showPullDown('pullDown_" +p+"_"+dto2.getMenuID()+"');";     
							                         	
// 							                          	String leafMenuIDList="";
														StringBuilder leafMenuIDList = new StringBuilder(5000);
							                          	for(int lcIndex=0;lcIndex<child3.size();lcIndex++){
							                          		int child3MenuID = ((PermissionDTO)child3.get(lcIndex)).getMenuID();
							                          		leafMenuIDList.append(child3MenuID).append(";");
							                          		leafMenuIDList.append(child3MenuID).append("Full;");
							                          		leafMenuIDList.append(child3MenuID).append("Modify;");
							                          		leafMenuIDList.append(child3MenuID).append("Read;");
							                          		
// 							                              leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + ";";
// 							                              leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Full" + ";";
// 							                              leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Modify" + ";";
// 							                              leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Read" + ";";
							                              
							                              /*****modified by sharif--> regirster 4rd level***/
								                           	PermissionDTO tempDTO4 = (PermissionDTO)child3.get(lcIndex);
								                           	if(tempDTO4!=null){
								                           		ArrayList tempChild4 = tempDTO4.getChildPermissionList();
								                           		if(tempChild4!=null || (tempChild4!=null && tempChild4.size()>0)){
								                           		for(int innerIndex=0;innerIndex<tempChild4.size();innerIndex++){
								                           			int tempChild4MenuID = ((PermissionDTO)tempChild4.get(innerIndex)).getMenuID();
							                           				int tempPermission=loginDTO.getMenuPermission(tempChild4MenuID);
								               	         			if(tempPermission==-1){	
								                             	  		continue;
								                               		}
									               	         		
									                          		leafMenuIDList.append(tempChild4MenuID).append(";");
									                          		leafMenuIDList.append(tempChild4MenuID).append("Full;");
									                          		leafMenuIDList.append(tempChild4MenuID).append("Modify;");
									                          		leafMenuIDList.append(tempChild4MenuID).append("Read;");
									                          
								               	         			
// 										                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + ";";
// 										                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Full" + ";";
// 										                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Modify" + ";";
// 										                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Read" + ";";
								                           		 	}
									               	     		}
								                           	}
								                           	
							                          	}%>
							                          	<tr>
										                  	<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent="<%=dto2.getParentMenuID()%>" 
										                  	value="<%=dto2.getMenuID()%>" <%=dto2.getPermissionType() !=-1?"checked=\"checked\"":""%>  id="<%=dto2.getMenuID()%>" 
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
															<%//ArrayList child3 = dto2.getChildPermissionList();
											                int permission=-1;
															for( int k=0; k< child3.size();k++){														             
								                	         	PermissionDTO dto3 = (PermissionDTO)child3.get(k);
								                	         	permission=loginDTO.getMenuPermission(dto3.getMenuID());
													            if(permission==-1){            	
								                 	              continue;
								                                }
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
							                                       }
							                                        
							                                       int permissionCheck=dto3.getPermissionType();
							                                       if(permission<permissionCheck){
							                                      		permissionCheck=permission;
							                                       }
							                                      	
						                                       	ArrayList child4 = dto3.getChildPermissionList();
									               	     		if(child4==null || (child4!=null && child4.size()==0)){ %>
									                				<tr>
												                  		<td width="5%" class="td_viewdata1">&nbsp;</td>
												                  		<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" 
												                  		parent="<%=dto3.getParentMenuID()%>" value="<%=dto3.getMenuID()%>" <%=dto3.getPermissionType() !=-1?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()%>" 
												                  		onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()%>','');checkPermissionType(this)"/></td>
									        	        		  		<td width="40%" class="td_viewdata2" align="left" colspan="3"><%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto3.getMenuName()%></td>
												                  		<td width="15%" class="td_viewdata1"align="center"><%if(permission>2){%><input type="checkbox" name="Full" value=<%=dto3.getMenuID()%> align="middle"  <%=permissionCheck ==3?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Full"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Full','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>
											        	          		<td width="15%" class="td_viewdata2" align="center"><%if(permission>1){%><input type="checkbox" name="Modify" value=<%=dto3.getMenuID()%> align="middle" <%=permissionCheck ==2?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Modify"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Modify','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')"/><%} %></td>
											                	  		<td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read" value=<%=dto3.getMenuID()%> align="middle" <%=permissionCheck ==1?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()+"Read"%>" onClick="uncheckOther('<%=dto3.getMenuID()%>','Read','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')"/></td>
										                			</tr>
									                			<%}else{ %>
									                			
								                				<%
								                		 		/*code added by shariful*/
									                		 		String multiplePullDown3="";
									                		 		for( int y=0; y< child4.size();y++){
									                		 			PermissionDTO dto4 = (PermissionDTO)child4.get(y);
									                		 			permission=loginDTO.getMenuPermission(dto4.getMenuID());
											               	         	if(permission==-1){	
											                             	  continue;
											                            }
								                		 				multiplePullDown3+="showPullDown('pullDown_" +p+"_"+dto2.getMenuID()+"_"+dto3.getMenuID()+"_"+dto4.getMenuID()+"');";
									                		 		}
								                                       
// 									                		 	 	String leaf4MenuIDList="";
																	StringBuilder leaf4MenuIDList = new StringBuilder(5000);
										                           	for(int lcIndex=0;lcIndex<child4.size();lcIndex++){
										                           		permission=loginDTO.getMenuPermission(((PermissionDTO)child4.get(lcIndex)).getMenuID());
											               	         	if(permission==-1){	
											                             	  continue;
											                            }
											               	         	int child4MenuID = ((PermissionDTO)child4.get(lcIndex)).getMenuID();
											               	         	leaf4MenuIDList.append(child4MenuID).append(";");
											               	         	leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Full;");
											               	         	leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Modify;");
											               	      		leaf4MenuIDList.append(child4MenuID).append(child4MenuID).append("Read;");
// 										                        	   leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + ";";
// 										                        	   leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Full" + ";";
// 										                        	   leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Modify" + ";";
// 										                        	   leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Read" + ";";
										                     	    }						                            
								                		 			%>
									                		 		<tr>
										                		 		<td width="5%"  class="td_viewdata1">&nbsp;</td>
													                  	<td width="5%" class="td_viewdata1" align="center">
													                  		<input type="checkbox" name="Menu" parent=<%=dto3.getParentMenuID()%> value=<%=dto3.getMenuID()%> <%=dto3.getPermissionType() !=-1?"checked=\"checked\"":""%> id="<%=dto3.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID()+";"+dto3.getParentMenuID()%>','<%=leaf4MenuIDList%>')"/>
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
										                		 			PermissionDTO dto4 = (PermissionDTO)child4.get(m); 
										                		 			permission=loginDTO.getMenuPermission(dto4.getMenuID());
												               	         	if(permission==-1){	
												                             	  continue;
											                               	}
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
									                                       }
									                                        
									                                       int permissionCheck4=dto4.getPermissionType();
									                                       if(permission<permissionCheck4){
									                                    	   permissionCheck4=permission;
									                                       }
											                 			%>
											                 			
										                 				<tr id=<%=("pullDown_" +p+"_"+dto2.getMenuID()+"_"+dto3.getMenuID()+"_"+dto4.getMenuID())%> style="display:none;">
															                  <td width="5%" class="td_viewdata1">&nbsp;</td>
															                  <td width="5%" class="td_viewdata1">&nbsp;</td>
															                  <td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto4.getParentMenuID()%> value=<%=dto4.getMenuID()%> <%=dto4.getPermissionType() !=-1?"checked=\"checked\"":""%>  id="<%=dto4.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()+";"+dto4.getParentMenuID()%>','');checkPermissionType(this)"/></td>
												        	        		  <td width="40%" class="td_viewdata2" align="left" colspan="2"><%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto4.getMenuName()%></td>
												        	        		  <td width="15%" class="td_viewdata1"align="center"><%if(permission>2){%><input type="checkbox" name="Full" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Full"%>" align="middle"  <%=permissionCheck4 ==3?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Full','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>					        	        		  
															                  <td width="15%" class="td_viewdata2" align="center"><%if(permission>1){%><input type="checkbox" name="Modify" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Modify"%>" align="middle" <%=permissionCheck4 ==2?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Modify','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>
														                	  <td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read"   value=<%=dto4.getMenuID()%> align="middle" id="<%=dto4.getMenuID()+"Read"%>" <%=permissionCheck4 ==1?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Read','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /></td>
												                		</tr>
								                		 			<%}%>
									                			<%} %>
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
										ArrayList columnPermissionList = dao.getRoleColumnPermission(roleIDstr,p);
										ArrayList allColumnPermissionList = dao.getColumnPermissionList(p); 
										if(allColumnPermissionList.size() > 0){ %>
										<div class="portlet-body">															
							              		<table  class="table table-striped table-bordered table-hover">
									                <!-- <tr>
								                    	<td colspan="42" align="center" style="font-weight:bold" class="td_viewheader">
								                      		Column Data Permission List
								                      		Add Column Data
								                      	</td>
								                    </tr> -->
								                    <tr>
									                    <% int col=0;
									                    for(int columnNo = 0;columnNo < allColumnPermissionList.size();columnNo++){
									                      String check="";
									                      ColumnPermissionDTO cdto = (ColumnPermissionDTO)allColumnPermissionList.get(columnNo);
									                      if(!loginDTO.getColumnPermission(cdto.getColumnID())){
									                    	  logger.debug("continue with cdto " + cdto);
									                    	  continue;
									                      }
									                      col++;
									                      for( int cc = 0 ; cc < columnPermissionList.size();cc++){
										                      ColumnPermissionDTO ccdto = (ColumnPermissionDTO)columnPermissionList.get(cc);
									                        if(cdto.getColumnID() == ccdto.getColumnID()){
									                          check = "checked=\"checked\"";
									                          break;
									                        }
									                      }%>
									                      <%-- <td class="td_viewdata1"><input type="checkbox" name="Column"  value=<%=cdto.getColumnID()+" "+ check %> /> </td> --%>
									                      <td class="td_viewdata2"><input type="checkbox" name="Column"  value=<%=cdto.getColumnID()+" "+ check %>/><%=cdto.getColumnName()%> </td>							
									                        <% if(col==3){
										                        col=0; %>
											                 </tr>
									                 		<tr>
									                         <% }							
									                    	}%>
								                    	</tr>
								            		</table>
												</div>
										
											<%}%>
										</div>	
										
										<!-- -- -->
										
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
	
									logger.debug("moduleID " + p + " rootReqIDs " + rootReqIDs);
									RequestUtilDAO rudao = new RequestUtilDAO();
									HashMap<Integer, ArrayList<StateDTO>> stateByRequest = rudao.getStatesRelatedToRootRequestTypeIDs(rootReqIDs);
									logger.debug("stateByRequest " + stateByRequest);
									/* if(stateByRequest != null && stateByRequest.size() > 0)
									{
										logger.debug("stateByRequest " + stateByRequest);
									} */
									//ArrayList actionPermissionList = dao.getActionPermissionList((String)request.getAttribute("id"),p);
									Set<Integer> saIDSet = ActionPermissionRepository.getInstance().getStateActionTypeIDSet(Long.parseLong(roleIDstr));
																
									int blankState = -(p * ModuleConstants.MULTIPLIER);
									int rangeLower = p * ModuleConstants.MULTIPLIER;
									int rangeUpper = (p + 1) * ModuleConstants.MULTIPLIER;
									
									HashMap<Integer, Set<Integer>> enabledStateActionMap = new HashMap<Integer, Set<Integer>>();
									if(saIDSet != null)
									{
										for(Integer saID: saIDSet)
										{				
											StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionBySaID(saID);
											if(sadto == null)continue;
											int action = sadto.getActionTypeIDs().iterator().next();
											int actionAbs = Math.abs(action);
											if(actionAbs >= rangeLower || actionAbs <= rangeUpper)
											{
												Set<Integer> actionSet = enabledStateActionMap.get(sadto.getStateID());
												if(actionSet == null)
												{
													actionSet = new HashSet<Integer>();												
												}
												actionSet.add(action);
												enabledStateActionMap.put(sadto.getStateID(), actionSet);
											}
										}
										/* StateActionDTO sadto = RequestStateActionRepository.getInstance().getStateActionByStateID(blankState);
										if(sadto != null)
										{
											enabledStateActionMap.put(blankState, sadto.getActionTypeIDs());
										} */
										/* enabledStateActionMap.put(blankState, rootReqIDs); */
									}
									/*
									enabledStateActionMap is filtered with role and module
									*/
									
									logger.debug("enabledStateActionMap " + enabledStateActionMap);
									%>
									<div class="panel-group accordion" id="accordion2">
									<%
									int q = 0;
									if(stateByRequest != null){
									for(Integer reqType:stateByRequest.keySet())
									{			
										logger.debug("reqType " + reqType + " states " + stateByRequest.get(reqType));
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
	                                    			
													if(enabledStateActionMap.get(blankState) != null)
													{
														if(enabledStateActionMap.get(blankState).contains(reqType))
														{
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
	                                                    for(StateDTO sdto: slist)
	                                                    {
	                                                    	//if(sdto.getId() < 0) continue;//kayesh
	                                                    %>
	                                                    <tr>
														<td><%=sdto.getName()%></td>
														<td>
														<%
														StateActionDTO sactionDTO =  RequestStateActionRepository.getInstance().getStateActionByStateID(sdto.getId());
														if(sactionDTO != null){
															StateActionDTO permittedSaDTO = loginDTO.getActionPermission(sdto.getId());
															logger.debug("permittedSaDTO " + permittedSaDTO);
															if(permittedSaDTO == null)continue;
															Set<Integer> permittedActions = permittedSaDTO.getActionTypeIDs(); 
															if(permittedActions == null)continue;
															
															for(Integer action : sactionDTO.getActionTypeIDs())
															{
																ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(action);
																if(asdto == null || !asdto.isSystemAction()) continue;
																if(!permittedActions.contains(asdto.getActionTypeID()))continue;
															%>
														
														
																<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 checkbox-list">
																<%
																	showChecked = false;
																	
																	
																	if(enabledStateActionMap.get(sdto.getId()) != null)
																	{
																		if(enabledStateActionMap.get(sdto.getId()).contains(asdto.getActionTypeID()))
																		{
																			showChecked = true;		
																		}
																	}
																	
																	  
																%>
																<input type="checkbox" name="saID" value='<%=(sactionDTO.getStateID() +"_"+ asdto.getActionTypeID())%>' <%if(showChecked){%>checked='checked'<%}%> ><%=asdto.getDescription()%>
																</div>
																										
															<%}
													}%>
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
									}%>
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
<!--        	<script type="text/javascript">
       	
	       	$(document).ready(function(){
	   			$("#roleEditId").submit(function(){
	   				var roleName = $('input[name=rolename]').val();
					if (!$.trim(roleName)) {
						toastr.error("Please give a role name");
						$('input[name=rolename]').focus();
						return false;
					}
					var roleNameDesc = $('input[name=roleDesc]').val();
					if (!$.trim(roleNameDesc)) {
						toastr.error("Please give a role description");
						$('input[name=roleDesc]').focus();
						return false;
					}
	   			})
	   		})			
			function validateSelection(ob,parentList,childList){
				if(ob.checked==false){
				   checkByIds(childList,false);
				}
				else
				{
				   checkByIds(parentList,true);
			    }
			}
		    function checkByIds(ids,checkValue){
		    	var splitted=ids.split(";");
		    	if(ids=="")return;
			    for(var i=0;i<splitted.length;i++){
				   if(splitted[i]!="")
				   {
					   if(document.getElementById(splitted[i]))
				       {
						   document.getElementById(splitted[i]).checked=checkValue;
						   if(checkValue)
						   {
							   $("#"+splitted[i]).parent().addClass("checked");  
						   }
						   else $("#"+splitted[i]).parent().removeClass("checked");
						   
						   
					   }		    
				   }
			    }
		    }
		    function checkPermissionType(ob){       
		    	if(ob.checked==false){
		    		var id=ob.id;
		    		
		    		if(document.getElementById(id + "Full")){
		    			document.getElementById(id + "Full").checked=false;
		    			$("#"+id+"Full").parent().removeClass("checked");
		    		}
		    		if(document.getElementById(id + "Modify")){
		    			document.getElementById(id + "Modify").checked=false;
		    			$("#"+id+"Modify").parent().removeClass("checked");
		    		}
		    		if(document.getElementById(id + "Read")){
		    			document.getElementById(id + "Read").checked=false;
		    			$("#"+id+"Read").parent().removeClass("checked");
		    		}
				}
		    	else{
		    		id=ob.id;
		    		if(document.getElementById(id + "Read")){
		    			document.getElementById(id + "Read").checked=true;
		    			$("#"+id+"Read").parent().addClass("checked");
		    		}
		    		else{
		        		alert("can not find permission type Read");
		    		}
		    		
		    		if(document.getElementById(id + "Modify")){
		    			document.getElementById(id + "Modify").checked=false;
		    		}
		    		if(document.getElementById(id + "Full")){
		    			document.getElementById(id + "Full").checked=false;
		    		}
		    	}
		    }
		
		    function uncheckOther(objID,typeStr,parentList)
		    {
		        if(document.getElementById(objID + typeStr).checked==true)
		        {
		        	if(typeStr != "Full"){
		            	if(document.getElementById(objID + "Full")){
		            		document.getElementById(objID + "Full").checked=false;
		            		$("#"+objID+"Full").parent().removeClass("checked");
		            	}
		        	}
		        	if(typeStr != "Modify"){
		            	if(document.getElementById(objID + "Modify")){
		            		document.getElementById(objID + "Modify").checked=false;
		            		$("#"+objID+"Modify").parent().removeClass("checked");
		            	}
		        	}
		        	if(typeStr != "Read"){
		            	if(document.getElementById(objID + "Read")){
		            		document.getElementById(objID + "Read").checked=false;
		            		$("#"+objID+"Read").parent().removeClass("checked");
		            	}
		        	}
		        	checkByIds(parentList,true);
		        }
		        else{
		        	document.getElementById(objID).checked=false;
		        }
		    }
		</script> -->

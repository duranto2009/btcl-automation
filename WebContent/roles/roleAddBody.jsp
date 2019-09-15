<%@page import="util.SOP"%>
<%@page import="config.GlobalConfigurationRepository"%>
<%@page import="permission.PermissionDTO"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.SortedSet"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="request.StateDTO"%>
<%@page import="java.util.Set"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="request.StateRepository"%>
<%@page import="permission.ActionStateDTO"%>
<%@page import="request.RequestActionStateRepository"%>
<%@page import="permission.StateActionDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="request.RequestStateActionRepository"%>
<%@page import="request.RequestUtilDAO"%>
<%@page import="login.LoginDTO"%>
<%@page import="common.ModuleConstants"%>
<%@ page language="java"%>
<%@ page errorPage="failure.jsp"%>
<%@ page import="java.util.ArrayList,java.sql.*,databasemanager.*, sessionmanager.SessionConstants,role.*"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>

<html:base />

<%
	LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN); 
	RoleDAO dao = new RoleDAO();
	int rolePermission = 3;
	Logger logger = Logger.getLogger(getClass());
%>

<%try{ %>

	<html:form action="/AddRole" method="POST" styleClass="form-horizontal " styleId="roleAddId">
		<div class="portlet box portlet-btcl">
			<div class="portlet-title">
				<div class="caption">
					<div class="caption"><i class="icon-plus"></i>Add Role</div>
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
						<label class="col-md-4 control-label">Descripion <span class="required"> * </span></label>
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
			
		               for(int r : sortedModuleSet){ %>
		               <% 
		              		if(GlobalConfigurationRepository.getInstance().getGlobalConfigDTOByID(r).getValue() > 0){
			%>
		                   <li class="<%if(r==ModuleConstants.Module_ID_DOMAIN){%>active<%}%>">
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
			boolean firstRow = true;
		%>
			<div class="tab-pane <%if(p==ModuleConstants.Module_ID_DOMAIN){%>active<%}%>" id="tab_1_1_<%=p%>">								
          		<div class="portlet light">
					<div class="portlet-title">
						<div class="caption"><i class="fa fa-list"></i> Menu Permission </div>
					</div>
					<div class="portlet-body">								
					
						<table class="table table-striped table-bordered table-hover" id="sample_1">
							<%ArrayList<PermissionDTO> data = dao.getAllMenuPermissionList(p);
							logger.debug("received data size " + data.size());
			                for(int i=0;i<data.size();i++){
			                  PermissionDTO dto = (PermissionDTO)data.get(i);
			                  if(loginDTO.getMenuPermission(dto.getMenuID())==-1){
			                	  logger.debug("Absent dto.getMenuID() " + dto.getMenuID());
			                	  continue;
			                  }
					                  
			                  ArrayList child2 = dto.getChildPermissionList();
			                  
			                  /** code added by safyat*/
			                  String childMenuIDList="";
			                  for(int cIndex=0;cIndex<child2.size();cIndex++){
			                	  PermissionDTO childDTO=(PermissionDTO)child2.get(cIndex);
			                	  childMenuIDList+=childDTO.getMenuID() + ";";
			                	  ArrayList<PermissionDTO> leaf=(ArrayList<PermissionDTO>)childDTO.getChildPermissionList();
			                	  for(int leafIndex=0;leafIndex<leaf.size();leafIndex++){
			                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + ";";
			                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Full" + ";";
			                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Modify" + ";";
			                		  childMenuIDList+=leaf.get(leafIndex).getMenuID() + "Read" + ";";
			                	  }
			                  }
			                  /*** code finished by safayat*/ %>
			                  <%if(firstRow){
			                  firstRow = false;
			                  %>
			                  <tr align="center">
								<td class="td_viewheader" valign="top" width="55%" colspan="5">
									Menu												
								</td>
								<td class="td_viewheader" valign="top" width="15%">
									Full												
								</td>
								<td class="td_viewheader" valign="top" width="15%">
									Modify												
								</td>
								<td class="td_viewheader" valign="top" width="15%">
									Read												
								</td>											
							</tr>
			                  <%}%>
           					<tr>
			                  <td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto.getParentMenuID()%> value=<%=dto.getMenuID()%> checked="checked" id="<%=dto.getMenuID()%>" onClick="validateSelection(this,'','<%=childMenuIDList%>')"/></td>
			                  <td width="45%"  colspan="4"  style="font-weight: bold" class="td_viewdata2" align="left"><%=dto.getMenuName()%></td>
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
		                           	ArrayList child3 = dto2.getChildPermissionList();
	                		 		String multiplePullDown2= "showPullDown('pullDown_" +p+"_"+dto2.getMenuID()+"');";
	                		 		
		                           /** code added by safyat*/
		                           String leafMenuIDList="";
		                           for(int lcIndex=0;lcIndex<child3.size();lcIndex++){
			                           	leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + ";";
			                           	leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Full" + ";";
			                           	leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Modify" + ";";
			                           	leafMenuIDList+=((PermissionDTO)child3.get(lcIndex)).getMenuID() + "Read" + ";";
			                           	
			               	        	/*****modified by sharif--> regirster 4rd level***/
			                           	PermissionDTO tempDTO4 = (PermissionDTO)child3.get(lcIndex);
			                           	if(tempDTO4!=null){
			                           		ArrayList tempChild4 = tempDTO4.getChildPermissionList();
			                           		if(tempChild4!=null || (tempChild4!=null && tempChild4.size()>0)){
			                           		 for(int innerIndex=0;innerIndex<tempChild4.size();innerIndex++){
			                           				int tempPermission=loginDTO.getMenuPermission(((PermissionDTO)tempChild4.get(innerIndex)).getMenuID());
					               	         		if(tempPermission==-1){	
					                             	  continue;
					                               	}
						                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + ";";
						                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Full" + ";";
						                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Modify" + ";";
						                           	leafMenuIDList+=((PermissionDTO)tempChild4.get(innerIndex)).getMenuID() + "Read" + ";";
			                           		 	}
				               	     		}
			                           	}
			               	     		
		                     	    }					
		                           
		                           
				                 /*** code finished by safayat*/%>
				               
		                		<tr>
				                  	<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto2.getParentMenuID()%> value=<%=dto2.getMenuID()%> checked="checked" id="<%=dto2.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID()%>','<%=leafMenuIDList%>')"/></td>
				                  	<td width="5%"  class="td_viewdata1 menuItem" align="center">
		                             	<a class="menuItem" onMouseOver="overMenuItem(this);" onMouseOut="outMenuItem(this);" onClick="<%=multiplePullDown2 %>">
		    								<i class="icon-control-play icons triangle"></i>
		    							</a>
		    						</td>
		                		  	<td width="40%" class="td_viewdata2" align="left" colspan="3"><%="&nbsp;&nbsp;&nbsp;"+dto2.getMenuName()%></td>
				                  	<td width="15%" class="td_viewdata1" align="center">&nbsp;</td>
				                  	<td width="15%" class="td_viewdata2" align="center">&nbsp;</td>
				                  	<td width="15%" class="td_viewdata1" align="center">&nbsp;</td>
		                		</tr>
					            <tbody id=<%=("pullDown_" +p+"_"+dto2.getMenuID())%> style="display:none;">
                                <%
			                        int permission=-1;
									for( int k=0; k< child3.size();k++){
			               	         	PermissionDTO dto3 = (PermissionDTO)child3.get(k);
			               	         	permission=loginDTO.getMenuPermission(dto3.getMenuID());
			               	         	if(permission==-1){	
			                             	  continue;
			                               }
			               	     		ArrayList child4 = dto3.getChildPermissionList();
			               	     		if(child4==null || (child4!=null && child4.size()==0)){ %>
				               				<tr>
							                  <td width="5%" class="td_viewdata1">&nbsp;</td>
							                  <td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto3.getParentMenuID()%> value=<%=dto3.getMenuID()%> checked="checked"  id="<%=dto3.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()%>','');checkPermissionType(this)"/></td>
				        	        		  <td width="40%" class="td_viewdata2" align="left" colspan="3"><%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto3.getMenuName()%></td>
				        	        		  <td width="15%" class="td_viewdata1"align="center"><%if(permission>2){%><input type="checkbox" name="Full" value=<%=dto3.getMenuID()%> id="<%=dto3.getMenuID()+"Full"%>" align="middle"  <%=permission ==3?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto3.getMenuID()%>','Full','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>					        	        		  
							                  <td width="15%" class="td_viewdata2" align="center"><%if(permission>1){%><input type="checkbox" name="Modify" value=<%=dto3.getMenuID()%> id="<%=dto3.getMenuID()+"Modify"%>" align="middle" <%=permission ==2?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto3.getMenuID()%>','Modify','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>
						                	  <td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read"   value=<%=dto3.getMenuID()%> align="middle" id="<%=dto3.getMenuID()+"Read"%>" <%=permission ==1?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto3.getMenuID()%>','Read','<%=dto3.getMenuID() + ";" + dto2.getMenuID() + ";" + dto.getMenuID()%>')" /></td>
					                		</tr>
			                		 	<%
			                		 	}else{ %>
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
			                		 	 	String leaf4MenuIDList="";
				                           	for(int lcIndex=0;lcIndex<child4.size();lcIndex++){
		                           				permission=loginDTO.getMenuPermission(((PermissionDTO)child4.get(lcIndex)).getMenuID());
					               	         	if(permission==-1){	
					                             	  continue;
					                            }
				                        	   	leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + ";";
				                        	   	leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Full" + ";";
				                        	   	leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Modify" + ";";
				                        	   	leaf4MenuIDList+=((PermissionDTO)child4.get(lcIndex)).getMenuID() + "Read" + ";";
				                     	    }						                            
			                		 		%>
			                		 		<tr>
				                		 		<td width="5%"  class="td_viewdata1">&nbsp;</td>
							                  	<td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto3.getParentMenuID()%> value=<%=dto3.getMenuID()%> checked="checked" id="<%=dto3.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID()+";"+dto3.getParentMenuID()%>','<%=leaf4MenuIDList%>')"/></td>
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
						                 			%>
					                 				<tr id=<%=("pullDown_" +p+"_"+dto2.getMenuID()+"_"+dto3.getMenuID()+"_"+dto4.getMenuID())%> style="display:none;">
										                  <td width="5%" class="td_viewdata1">&nbsp;</td>
										                  <td width="5%" class="td_viewdata1">&nbsp;</td>
										                  <td width="5%" class="td_viewdata1" align="center"><input type="checkbox" name="Menu" parent=<%=dto4.getParentMenuID()%> value=<%=dto4.getMenuID()%> checked="checked"  id="<%=dto4.getMenuID()%>" onClick="validateSelection(this,'<%=dto2.getParentMenuID() + ";" + dto3.getParentMenuID()+";"+dto4.getParentMenuID()%>','');checkPermissionType(this)"/></td>
							        	        		  <td width="40%" class="td_viewdata2" align="left" colspan="2"><%="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dto4.getMenuName()%></td>
							        	        		  <td width="15%" class="td_viewdata1"align="center"><%if(permission>2){%><input type="checkbox" name="Full" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Full"%>" align="middle"  <%=permission ==3?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Full','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>					        	        		  
										                  <td width="15%" class="td_viewdata2" align="center"><%if(permission>1){%><input type="checkbox" name="Modify" value=<%=dto4.getMenuID()%> id="<%=dto4.getMenuID()+"Modify"%>" align="middle" <%=permission ==2?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Modify','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+";"+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /><%} %></td>
									                	  <td width="15%" class="td_viewdata1"align="center"><input type="checkbox" name="Read"   value=<%=dto4.getMenuID()%> align="middle" id="<%=dto4.getMenuID()+"Read"%>" <%=permission ==1?"checked=\"checked\"":""%> onClick="uncheckOther('<%=dto4.getMenuID()%>','Read','<%=dto4.getMenuID() + ";" + dto3.getMenuID()+ dto2.getMenuID() + ";" + dto.getMenuID()%>')" /></td>
							                		</tr>
			                		 			<%}%>
			                		 	<%}%>
	                                <%}%>
			                        </tbody>
	        	                <%}
			                }%>
						</table>
					</div>
				</div>
		
		
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-globe"></i>Column Data
						</div>
					</div>
						
					<% ArrayList allColumnPermissionList = dao.getColumnPermissionList(p); 
					if(allColumnPermissionList.size() > 0){ %>
			
						<div class="portlet-body">															
							<div class="form-group">
								<% int col=0;
			                    for(int columnNo = 0;columnNo < allColumnPermissionList.size();columnNo++){
			                      String check="";
			                      ColumnPermissionDTO cdto = (ColumnPermissionDTO)allColumnPermissionList.get(columnNo);
			                      if(!loginDTO.getColumnPermission(cdto.getColumnID())){								                    	 
			                    	  continue;
			                      }
			                      col++;
			                      check = "checked=\"checked\"";
			                      
			                      /* for( int cc = 0 ; cc < allColumnPermissionList.size();cc++){
				                      ColumnPermissionDTO ccdto = (ColumnPermissionDTO)allColumnPermissionList.get(cc);
			                        if(cdto.getColumnID() == ccdto.getColumnID()){
			                          check = "checked=\"checked\"";
			                          break;
			                        }
			                      } */
			                      %>
								<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12 checkbox-list">
										<input type="checkbox" name="Column"	value=<%=cdto.getColumnID()+" "+ check %> ><%=cdto.getColumnName()%>
								</div>
							<%} %>
						</div>							            		
					</div>
				<%}%>									
				</div>	
						<!-- ----------- -->
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
		
					//logger.debug("rootReqIDs " + rootReqIDs);
					RequestUtilDAO rudao = new RequestUtilDAO();
					HashMap<Integer, ArrayList<StateDTO>> stateByRequest = rudao.getStatesRelatedToRootRequestTypeIDs(rootReqIDs);
					%>
					<div class="panel-group accordion " id="accordion2">
					<%
					int q = 0;
					if(stateByRequest != null){
					for(Integer reqType:stateByRequest.keySet())
					{					
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
                         int blankState = -(p * ModuleConstants.MULTIPLIER);												                                         
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
												<input type="checkbox" name="saID" value='<%=(blankState +"_"+ reqType)%>' ><%=RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(reqType).getDescription()%>
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
									for(Integer action : sactionDTO.getActionTypeIDs())
									{
										StateActionDTO sadtoLocal = loginDTO.getActionPermission(sdto.getId());
										if(sadtoLocal == null) continue;
										if(sadtoLocal.getActionTypeIDs() == null) continue;
										if(!sadtoLocal.getActionTypeIDs().contains(action))continue;
									%>
									<%ActionStateDTO asdto = RequestActionStateRepository.getInstance().getActionStateDTOActionTypeID(action);
									if(asdto != null && asdto.isSystemAction()){
									%>
									
									
									<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 checkbox-list">
									<input type="checkbox" name="saID" value="<%=(sactionDTO.getStateID() +"_"+ asdto.getActionTypeID())%>"><%=asdto.getDescription()%>
									</div>
																				
									
									<%}%>
										
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
					<%}
					}%>
					</div>
				</div>									
			</div>									
			<!-- ------------- -->
			</div>
						<%
						firstRow = true;				
		}%>
			</div>
						
				</div>
						<!-- END EXAMPLE TABLE PORTLET-->
				</div>
				<div class="form-actions fluid">
					<div class="row">
						<div class="col-md-offset-5 col-md-4">
							<input class="btn btn-reset-btcl" type="reset" name="B1" value="Reset"/>	
							<%if (rolePermission == 3) {%>
								<input class="btn btn-submit-btcl" type="submit" value="Submit" name="B2">
							<%}%>
						</div>
					</div>
				</div>		
			</div>
		</div>
	<!-- END EXAMPLE TABLE PORTLET-->
    </html:form>
			            
  <%}catch(Exception ex){
   		ex.printStackTrace();
  } %>




<script  src="../scripts/util.js"></script>
<script  src="../scripts/menu.js"></script>
<script  src="../roles/role.js"></script>

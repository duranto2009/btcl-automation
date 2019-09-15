
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic"%>


  

	
	<!-- <script type="text/javascript">
		$(document).ready(function(){
			var clicked = false;
			$('#ip_submenu').hide();
			$('.ip-address').click(function(){
				if(clicked == false){
					$(this).css('padding-right','0');
					$('#ip_submenu').show();
					clicked = true;
				}else{
					$(this).css('padding-right','15px');
					$('#ip_submenu').hide();
					clicked = false;
				}
				
			});
		});
	
function setCost()
{
	var f = document.forms[1];

	var e = f.iigBANDWIDTH;
	
	if(e.options[e.selectedIndex].value == '0')
	{			
		f.iigCost.value = '960';
	}
	else if(e.options[e.selectedIndex].value == '1')
	{			
		f.iigCost.value = '720';
	}	
	else if(e.options[e.selectedIndex].value == '2')
	{			
		f.iigCost.value = '660';
	}	
	else if(e.options[e.selectedIndex].value == '3')
	{			
		f.iigCost.value = '600';
	}	
	else if(e.options[e.selectedIndex].value == '4')
	{			
		f.iigCost.value = '540';
	}	
	else if(e.options[e.selectedIndex].value == '5')
	{			
		f.iigCost.value = '460';
	}	
	else if(e.options[e.selectedIndex].value == '6')
	{			
		f.iigCost.value = '400';
	}	
	
}

</script> -->
    



			<div class="row">
			<div class="col-lg-offset-1 col-lg-10">
				<div class="panel panel-success">
					
						<!-- <div class="panel-heading" style="text-align: center; font-size: 14pt; background-color: #aeeaae;"><div class="div_title">Cost Config</div></div> -->
					
					<div class="panel-body">
						
<!-- 							<form name="costForm" method="post" action="../CostConfig.do" class="form-horizontal" onsubmit="return validate();"> -->
						<form>
								<%-- <logic:iterate id="rowDTO" name="CostConfigForm"
									property="rowList"
									type="util.RowDTO"
									indexId="idx">
									<tr>
										<td class="label" style="text-align: center;"><html:text
												name="rowDTO" property="upperRange" indexed="true" />
										</td>
										<td class="label" style="text-align: center;"><html:text
												name="rowDTO" property="lowerRange" indexed="true" />
										</td>
									</tr>
								</logic:iterate> --%>



								<table class="table table-bordered table-striped" style="margin-top: 30px;text-align: center">
                                            <thead>
                                                <tr>
                                                    <td class="td_viewheader">Sl</td>
                                                    <td class="align-center td_viewheader">Bandwidth(Mbps)</td>
                                                    <td class="align-center td_viewheader">Step 1 <br>(0-50 KM) BDT/Mbps </td>
                                                    <td class="align-center td_viewheader">Step 2 <br>(51-100 KM) BDT/Mbps</td>
                                                    <td class="align-center td_viewheader">Step 3 <br>(101-200 KM) BDT/Mbps </td>
                                                    <td class="align-center td_viewheader">Step 4 <br>(201-300 KM) BDT/Mbps</td>
                                                    <td class="align-center td_viewheader">Step 5 <br>(300+ KM) BDT/Mbps </td>
                                                    
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>1.</td>
                                                    <td><div  class="input-group"><input value="1" class="form-control" style="width:40%"/><span>-</span><input value="5" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="1000" class="form-control"/></td>
                                                    <td><input value="9.00" class="form-control"/></td>
                                                    <td><input value="8.00" class="form-control"/></td>
                                                    <td><input value="7.00" class="form-control"/></td>
                                                    <td><input value="6.00" class="form-control"/></td>
                                                     
                                                </tr>
                                                 <tr>
                                                    <td>2.</td>
                                                    
                                                    <td> <div class="input-group"><input value="6" class="form-control" style="width:40%"/><span>-</span><input value="20" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="700" class="form-control"/></td>
                                                    <td><input value="6.50" class="form-control"/></td>
                                                    <td><input value="6.00" class="form-control"/></td>
                                                    <td><input value="5.50" class="form-control"/></td>
                                                    <td><input value="5.00" class="form-control"/></td>                                                    
                                                    
                                                    
                                                </tr>
                                                 <tr>
                                                    <td>3.</td>
<!--                                                     <td>21-50</td>
                                                    <td>500</td>
                                                    <td>4.75</td>
                                                    <td>4.50</td>
                                                    <td>4.25</td>
                                                    <td>4.00</td> -->
                                                    
                                                    <td> <div class="input-group"><input value="21" class="form-control" style="width:40%"/><span>-</span><input value="50" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="500" class="form-control"/></td>
                                                    <td><input value="4.75" class="form-control"/></td>
                                                    <td><input value="4.50" class="form-control"/></td>
                                                    <td><input value="4.25" class="form-control"/></td>
                                                    <td><input value="4.00" class="form-control"/></td>                                                    
                                                    
                                                </tr>
                                                  <tr>
                                                    <td>4.</td>
<!--                                                     <td>51-150</td>
                                                    <td>300</td>
                                                    <td>2.75</td>
                                                    <td>2.50</td>
                                                    <td>2.25</td>
                                                    <td>2.00</td> -->
                                                    
                                                    
                                                    <td> <div class="input-group"><input value="51" class="form-control" style="width:40%"/><span>-</span><input value="150" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="300" class="form-control"/></td>
                                                    <td><input value="2.75" class="form-control"/></td>
                                                    <td><input value="2.50" class="form-control"/></td>
                                                    <td><input value="2.25" class="form-control"/></td>
                                                    <td><input value="2.00" class="form-control"/></td>                                                    
                                                    
                                                    
                                                </tr>
                                                  <tr>
                                                    <td>5.</td>
<!--                                                     <td>151-300</td>
                                                    <td>200</td>
                                                    <td>1.75</td>
                                                    <td>1.75</td>
                                                    <td>1.50</td>
                                                    <td>1.50</td> -->
                                                    
                                                    <td> <div class="input-group"><input value="151" class="form-control" style="width:40%"/><span>-</span><input value="300" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="200" class="form-control"/></td>
                                                    <td><input value="1.75" class="form-control"/></td>
                                                    <td><input value="1.75" class="form-control"/></td>
                                                    <td><input value="1.50" class="form-control"/></td>
                                                    <td><input value="1.50" class="form-control"/></td>                                                    
                                                    
                                                    
                                                </tr>  
                                                <tr>
                                                    <td>6.</td>
<!--                                                     <td>301-500</td>
                                                    <td>150</td>
                                                    <td>1.50</td>
                                                    <td>1.25</td>
                                                    <td>1.25</td>
                                                    <td>1.00</td> -->
                                                    
                                                    
                                                    <td> <div class="input-group"><input value="301" class="form-control" style="width:40%"/><span>-</span><input value="500" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="150" class="form-control"/></td>
                                                    <td><input value="1.50" class="form-control"/></td>
                                                    <td><input value="1.25" class="form-control"/></td>
                                                    <td><input value="1.25" class="form-control"/></td>
                                                    <td><input value="1.00" class="form-control"/></td>                                                    
                                                    
                                                    
                                                </tr>  
                                                <tr>
                                                    <td>7.</td>
<!--                                                     <td>501+</td>
                                                    <td>100</td>
                                                    <td>1.00</td>
                                                    <td>1.00</td>
                                                    <td>1.00</td>
                                                    <td>1.00</td> -->
                                                    
                                                    <td> <div class="input-group"><input value="500" class="form-control" style="width:40%"/><span>-</span><input value="~" class="form-control" style="width:40%;float:right"/></div></td>
                                                    <td><input value="100" class="form-control"/></td>
                                                    <td><input value="1.00" class="form-control"/></td>
                                                    <td><input value="1.00" class="form-control"/></td>
                                                    <td><input value="1.00" class="form-control"/></td>
                                                    <td><input value="1.00" class="form-control"/></td>                                                    
                                                    
                                                    
                                                </tr>  
                                            </tbody>
                                        </table>
								
								

							</form>
							
													<!-- <div class="form-group"> -->
									<div class="text-center">
																				
										    <input type="reset" value="Reset" class="btn btn-default btn-reset-btcl"/>
										    
										    <input type="submit" value="Submit" class="btn btn-success btn-submit-btcl"/>
										
									</div>
								<!-- </div>		 -->					
							
					</div>

				</div>
			</div>





			</div>




<style type="text/css">
		
		.btn {
    
    border: 0 none;
    font-weight: 700;
    letter-spacing: 1px;
    text-transform: uppercase;
    height: 40px;
}
 
.btn:focus, .btn:active:focus, .btn.active:focus {
    outline: 0 none;
}
 
.btn-primary {
    background: #47d147;
    color: #ffffff;
}
 
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open > .dropdown-toggle.btn-primary {
    background: #29a329;
}
 
.btn-primary:active, .btn-primary.active {
    background: #007299;
    box-shadow: none;
}

.panel {
	border: 0 none;
}
.panel-body{
border: 0 none;
}

.panel-primary > .panel-heading {
    background-color: #337ab7;
    border-color: #aeeaae;
    color: #fff;
}
		</style>


									
								

<c:if test="${action.actionTypeID == 60100}">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">
		<form role="form">
			<div class="box-body">
				<div class="form-group">
					<label for="shiftTargetDistrict">Target District</label> <input type="text" class="form-control" id=""
						placeholder="Type to select...">
				</div>
				<div class="form-group">
					<label for="shiftTargetDistrict">Target Pop</label> <input type="text" class="form-control" id=""
						placeholder="Type to select...">
				</div>
				<div class="form-group">
					<label for="description">Target Address</label>
					<textarea class="form-control" rows="3" id="vpnLnDescription" placeholder="Write target address"></textarea>
				</div>
			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/.box edit
<c:if test="${action.actionTypeID == 60102|| action.actionTypeID == 65002  || action.actionTypeID == 65003}">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">
		<a class="btn btn-primary btn-block" href="<%=editActionName%>">Edit</a>
	</div>
</c:if>
/.box ownership change
<c:if test="${action.actionTypeID == 60100}">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">

		<form role="form">
			<div class="box-body">
				<div class="form-group">
					<label for="shiftTargetDistrict">Target Client</label> <input type="text" class="form-control" id=""
						placeholder="Type to select a client...">
				</div>

			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/.box ownership change
<c:if test="${action.actionTypeID == 60100}">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">

		<form role="form">
			<div class="box-body">
				<div class="form-group">
					<label for="description">Description of owner</label>
					<textarea class="form-control" rows="3" id="vpnLnDescription" placeholder="Write target address"></textarea>
				</div>
			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/.box upgrade
<c:if test="${action.actionTypeID == 60100}">
	<div class="box box-success" id="link_action_${action.actionTypeID }"
		style="background: rgb(236, 240, 245); display: none;">

		<form role="form" method="post" action="../../VpnLinkUgBW.do?mode=update&action=upgrade">
			<div class="box-body">
				<div class="form-group">
					<label for="shiftTargetDistrict">Target Bandwidth</label> <input type="hidden" name="vpnLinkID"
						value="${vpnLink.ID }" /> <input type="hidden" name="clientID" value="${vpnConnection.serviceClientID }" /> <input
						type="hidden" name="requestByAccountID" value="${vpnConnection.serviceClientID }" /> <input type="text"
						class="form-control" name="newBandwidth" placeholder="Upgrade bandwidth" />
				</div>
			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/upgrade downgrade
<c:if test="${action.actionTypeID == 60100}">
	<div class="box box-success" id="link_action_${action.actionTypeID }"
		style="background: rgb(236, 240, 245); display: none;">

		<form role="form" method="post" action="../../VpnLinkUgBW.do?mode=update&action=downgrade">
			<div class="box-body">
				<div class="form-group">
					<label for="shiftTargetDistrict">Target Bandwidth</label> <input type="hidden" name="vpnLinkID"
						value="${vpnLink.ID }" /> <input type="hidden" name="clientID" value="${vpnConnection.serviceClientID }" /> <input
						type="hidden" name="requestByAccountID" value="${vpnConnection.serviceClientID }" /> <input type="text"
						class="form-control" name="newBandwidth" placeholder="Downgrade bandwidth" />
				</div>
			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/downgrade close
<c:if test="${action.actionTypeID == -60103 || action.actionTypeID == -60102 }">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">

		<form role="form">
			<div class="box-body">
				<div class="form-group">
					<label for="description">Note</label>
					<textarea class="form-control" rows="3" id="vpnLnDescription" placeholder="Reason of closing"></textarea>
				</div>


			</div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/.box redundant
<c:if test="${action.actionTypeID == 60100}">
	<div class="portlet" id="link_action_${action.actionTypeID }" style="display: none;">

		<form role="form">
			<div class="box-body"></div>
			/.box-body

			<div class="box-footer">
				<button type="submit" class="btn btn-block btn-success btn-sm">Submit</button>
			</div>
		</form>
	</div>
</c:if>
/.box

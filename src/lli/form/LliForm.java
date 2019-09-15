package lli.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LliForm extends ActionForm{

    long llID;
    int llBandWidth;
    int llMonth;
    String llAddress;
    int llLoopProvider;
    double llCost;
    int llEquipmentName;
    String llEquipmentModel;
    int llEquipmentInterface;
    int llIPNeed;
    int llIPRoute;
    String llComment;
    int actionType;
    int type;
    long orderID;
    int llStatus;
    String llVat;
    String llCostVatLess;
    
    private String[] deleteIDs;
    
    public String[] getDeleteIDs() {
            return deleteIDs;
    }
    public void setDeleteIDs(String[] deleteIDs) {
            this.deleteIDs = deleteIDs;
    }

   
    public long getLlID() {
		return llID;
	}
	public void setLlID(long llID) {
		this.llID = llID;
	}
	public int getLlBandWidth() {
		return llBandWidth;
	}
	public void setLlBandWidth(int llBandWidth) {
		this.llBandWidth = llBandWidth;
	}
	public int getLlMonth() {
		return llMonth;
	}
	public void setLlMonth(int llMonth) {
		this.llMonth = llMonth;
	}
	public String getLlAddress() {
		return llAddress;
	}
	public void setLlAddress(String llAddress) {
		this.llAddress = llAddress;
	}
	public int getLlLoopProvider() {
		return llLoopProvider;
	}
	public void setLlLoopProvider(int llLoopProvider) {
		this.llLoopProvider = llLoopProvider;
	}
	public int getLlEquipmentName() {
		return llEquipmentName;
	}
	public void setLlEquipmentName(int llEquipmentName) {
		this.llEquipmentName = llEquipmentName;
	}
	public String getLlEquipmentModel() {
		return llEquipmentModel;
	}
	public void setLlEquipmentModel(String llEquipmentModel) {
		this.llEquipmentModel = llEquipmentModel;
	}
	public int getLlEquipmentInterface() {
		return llEquipmentInterface;
	}
	public void setLlEquipmentInterface(int llEquipmentInterface) {
		this.llEquipmentInterface = llEquipmentInterface;
	}
	public int getLlIPNeed() {
		return llIPNeed;
	}
	public void setLlIPNeed(int llIPNeed) {
		this.llIPNeed = llIPNeed;
	}
	public int getLlIPRoute() {
		return llIPRoute;
	}
	public void setLlIPRoute(int llIPRoute) {
		this.llIPRoute = llIPRoute;
	}
	

	public double getLlCost() {
		return llCost;
	}
	public void setLlCost(double llCost) {
		this.llCost = llCost;
	}
	
	
	public String getLlComment() {
		return llComment;
	}
	public void setLlComment(String llComment) {
		this.llComment = llComment;
	}
	
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public long getOrderID() {
		return orderID;
	}
	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
	
	
	public String getLlVat() {
		return llVat;
	}
	public void setLlVat(String llVat) {
		this.llVat = llVat;
	}
	public String getLlCostVatLess() {
		return llCostVatLess;
	}
	public void setLlCostVatLess(String llCostVatLess) {
		this.llCostVatLess = llCostVatLess;
	}
	public int getLlStatus() {
		return llStatus;
	}
	public void setLlStatus(int llStatus) {
		this.llStatus = llStatus;
	}
	public ActionErrors validate(ActionMapping p_mapping, HttpServletRequest p_request)
    {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
}
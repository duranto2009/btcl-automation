package crm.inventory;
import java.util.*;
public class CRMInventoryCatagoryTreeNode {
	private CRMInventoryCatagoryDetails CRMInventoryCatagoryDetailsRootNode;
	List<CRMInventoryCatagoryTreeNode> CRMInventoryCatagoryDetailsChildNodes;
	public CRMInventoryCatagoryDetails getCRMInventoryCatagoryDetailsRootNode() {
		return CRMInventoryCatagoryDetailsRootNode;
	}
	public void setCRMInventoryCatagoryDetailsRootNode(
			CRMInventoryCatagoryDetails CRMInventoryCatagoryDetailsRootNode) {
		this.CRMInventoryCatagoryDetailsRootNode = CRMInventoryCatagoryDetailsRootNode;
	}
	public List<CRMInventoryCatagoryTreeNode> getCRMInventoryCatagoryDetailsChildNodes() {
		return CRMInventoryCatagoryDetailsChildNodes;
	}
	public void setCRMInventoryCatagoryDetailsChildNodes(
			List<CRMInventoryCatagoryTreeNode> CRMInventoryCatagoryDetailsChildNodes) {
		this.CRMInventoryCatagoryDetailsChildNodes = CRMInventoryCatagoryDetailsChildNodes;
	}
	
	private String print(String prefixString){
		String returnString = "";
		String CRMInventoryCatagoryDetailsString = CRMInventoryCatagoryDetailsRootNode.toString();
		CRMInventoryCatagoryDetailsString = CRMInventoryCatagoryDetailsString;
		CRMInventoryCatagoryDetailsString = prefixString+CRMInventoryCatagoryDetailsString.replaceAll("\n", "\n"+ prefixString+ "*");
		returnString += CRMInventoryCatagoryDetailsString;
		
		for(CRMInventoryCatagoryTreeNode childTreeNode: CRMInventoryCatagoryDetailsChildNodes){
			returnString+= childTreeNode.toString().replaceAll("\n","\n"+ prefixString+"\t");
		}
		return returnString;
	}
	
	private String printNode(CRMInventoryCatagoryTreeNode treeNode){
		String returnString = "";
		returnString = treeNode.getCRMInventoryCatagoryDetailsRootNode().toString();
		if(!treeNode.getCRMInventoryCatagoryDetailsChildNodes().isEmpty()){
			returnString+="\n";
		}
		for(CRMInventoryCatagoryTreeNode childNode: treeNode.getCRMInventoryCatagoryDetailsChildNodes()){
			returnString+="\t"+(printNode(childNode)+"\n").replaceAll("\n", "\n\t");
		}
		return returnString;
	}
	
	@Override
	public String toString() {
		return printNode(this);
	}
	
}

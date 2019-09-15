package inventory;
import java.util.*;
public class InventoryCatagoryTreeNode {
	private InventoryCatagoryDetails inventoryCatagoryDetailsRootNode;
	List<InventoryCatagoryTreeNode> inventoryCatagoryDetailsChildNodes;
	public InventoryCatagoryDetails getInventoryCatagoryDetailsRootNode() {
		return inventoryCatagoryDetailsRootNode;
	}
	public void setInventoryCatagoryDetailsRootNode(
			InventoryCatagoryDetails inventoryCatagoryDetailsRootNode) {
		this.inventoryCatagoryDetailsRootNode = inventoryCatagoryDetailsRootNode;
	}
	public List<InventoryCatagoryTreeNode> getInventoryCatagoryDetailsChildNodes() {
		return inventoryCatagoryDetailsChildNodes;
	}
	public void setInventoryCatagoryDetailsChildNodes(
			List<InventoryCatagoryTreeNode> inventoryCatagoryDetailsChildNodes) {
		this.inventoryCatagoryDetailsChildNodes = inventoryCatagoryDetailsChildNodes;
	}
	
	private String print(String prefixString){
		String returnString = "";
		String inventoryCatagoryDetailsString = inventoryCatagoryDetailsRootNode.toString();
		inventoryCatagoryDetailsString = inventoryCatagoryDetailsString;
		inventoryCatagoryDetailsString = prefixString+inventoryCatagoryDetailsString.replaceAll("\n", "\n"+ prefixString+ "*");
		returnString += inventoryCatagoryDetailsString;
		
		for(InventoryCatagoryTreeNode childTreeNode: inventoryCatagoryDetailsChildNodes){
			returnString+= childTreeNode.toString().replaceAll("\n","\n"+ prefixString+"\t");
		}
		return returnString;
	}
	
	private String printNode(InventoryCatagoryTreeNode treeNode){
		String returnString = "";
		returnString = treeNode.getInventoryCatagoryDetailsRootNode().toString();
		if(!treeNode.getInventoryCatagoryDetailsChildNodes().isEmpty()){
			returnString+="\n";
		}
		for(InventoryCatagoryTreeNode childNode: treeNode.getInventoryCatagoryDetailsChildNodes()){
			returnString+="\t"+(printNode(childNode)+"\n").replaceAll("\n", "\n\t");
		}
		return returnString;
	}
	
	@Override
	public String toString() {
		return printNode(this);
	}
	
}

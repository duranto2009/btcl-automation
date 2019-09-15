package crm;

import java.util.ArrayList;
import java.util.List;
import static util.ModifiedSqlGenerator.*;

public class CrmComplainTreeNode extends CrmComplainDTO{
	List<CrmComplainTreeNode> childCrmComplainNodeList = new ArrayList<>();

	public List<CrmComplainTreeNode> getChildCrmComplainNodeList() {
		return childCrmComplainNodeList;
	}

	public void setChildCrmComplainNodeList(List<CrmComplainTreeNode> childCrmComplainNodeList) {
		this.childCrmComplainNodeList = childCrmComplainNodeList;
	}

	public void addChildCrmComplainNode(CrmComplainTreeNode childCrmComplainTreeNode){
		if(childCrmComplainNodeList == null){
			childCrmComplainNodeList = new ArrayList<>();
		}
		childCrmComplainNodeList.add(childCrmComplainTreeNode);
	}
	
	public void addChildCrmComplainNode(CrmComplainDTO childCrmComplainDTO){
		CrmComplainTreeNode crmComplainTreeNode = new CrmComplainTreeNode();
		populateObjectFromOtherObject(childCrmComplainDTO, crmComplainTreeNode, CrmComplainDTO.class);
		addChildCrmComplainNode(crmComplainTreeNode);
	}

	@Override
	public String toString() {
		String resultantString = super.toString()+"\n";
		for(CrmComplainTreeNode childComplainNode: childCrmComplainNodeList){
			String childToString = childComplainNode.toString();
			childToString = "\t"+childToString.replace("\n", "\n\t")+"\n";
			resultantString+=childToString;
		}
		
		return resultantString.substring(0, resultantString.length()-2);
	}
}

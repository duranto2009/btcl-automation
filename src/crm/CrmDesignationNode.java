package crm;

import static util.ModifiedSqlGenerator.*;

import java.util.ArrayList;
import java.util.List;

public class CrmDesignationNode extends CrmDesignationDTO{
	List<CrmDesignationNode> childCrmDesignationNodeList = new ArrayList<>();

	public List<CrmDesignationNode> getChildCrmDesignationNodeList() {
		return childCrmDesignationNodeList;
	}

	public void setChildCrmDesignationNodeList(List<CrmDesignationNode> childCrmDesignationNodeList) {
		this.childCrmDesignationNodeList = childCrmDesignationNodeList;
	}

	public void addChildCrmDesignationNode(CrmDesignationNode childCrmDesignationNode) {
		if (childCrmDesignationNodeList == null) {
			childCrmDesignationNodeList = new ArrayList<>();
		}
		childCrmDesignationNodeList.add(childCrmDesignationNode);
	}

	public void addChildCrmDesignationNode(CrmDesignationDTO childCrmDesignationDTO) {
		CrmDesignationNode crmDesignationNode = new CrmDesignationNode();
		populateObjectFromOtherObject(childCrmDesignationDTO, crmDesignationNode, CrmDesignationDTO.class);
		addChildCrmDesignationNode(crmDesignationNode);
	}

	@Override
	public String toString() {
		String resultantString = super.toString()+"\n";
		for(CrmDesignationNode childDesignationNode: childCrmDesignationNodeList){
			String childToString = childDesignationNode.toString();
			childToString = "\t"+childToString.replace("\n", "\n\t")+"\n";
			resultantString+=childToString;
		}
		
		return resultantString.substring(0, resultantString.length()-2);
	}
	
}

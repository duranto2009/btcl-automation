package crm;

import static util.ModifiedSqlGenerator.*;

import java.util.ArrayList;
import java.util.List;

public class CrmEmployeeNode extends CrmEmployeeDTO{
	List<CrmEmployeeNode> childCrmEmployeeNodeList = new ArrayList<>();
	
	public List<CrmEmployeeNode> getChildCrmEmployeeNodeList() {
		return childCrmEmployeeNodeList;
	}

	public void setChildCrmEmployeeNodeList(List<CrmEmployeeNode> childCrmEmployeeNodeList) {
		this.childCrmEmployeeNodeList = childCrmEmployeeNodeList;
	}

	public void addChildCrmEmployeeNode(CrmEmployeeNode childCrmEmployeeNode){
		if(childCrmEmployeeNodeList == null){
			childCrmEmployeeNodeList = new ArrayList<>();
		}
		childCrmEmployeeNodeList.add(childCrmEmployeeNode);
	}
	
	public void addChildCrmEmployeeNode(CrmEmployeeDTO childCrmEmployeeDTO){
		CrmEmployeeNode crmEmployeeNode = new CrmEmployeeNode();
		populateObjectFromOtherObject(childCrmEmployeeDTO, crmEmployeeNode, CrmEmployeeDTO.class);
		addChildCrmEmployeeNode(crmEmployeeNode);
	}

	@Override
	public String toString() {
		String resultantString = super.toString()+"\n";
		for(CrmEmployeeNode childEmployeeNode: childCrmEmployeeNodeList){
			String childToString = childEmployeeNode.toString();
			childToString = "\t"+childToString.replace("\n", "\n\t")+"\n";
			resultantString+=childToString;
		}
		
		return resultantString.substring(0, resultantString.length()-2);
	}
}

package crm;

import static util.ModifiedSqlGenerator.populateObjectFromOtherObject;

import java.util.ArrayList;
import java.util.List;

public class CrmComplainHistoryNode extends CrmComplainHistoryDTO{
	List<CrmComplainHistoryNode> childCrmComplainHisotryNodeList;

	public List<CrmComplainHistoryNode> getChildCrmComplainHisotryNodeList() {
		return childCrmComplainHisotryNodeList;
	}

	public void setChildCrmComplainHisotryNodeList(List<CrmComplainHistoryNode> childCrmComplainHisotryNodeList) {
		this.childCrmComplainHisotryNodeList = childCrmComplainHisotryNodeList;
	}

	public void addChildCrmComplainHisotryNode(CrmComplainHistoryNode childCrmComplainHisotryNode) {
		if (childCrmComplainHisotryNodeList == null) {
			childCrmComplainHisotryNodeList = new ArrayList<>();
		}
		childCrmComplainHisotryNodeList.add(childCrmComplainHisotryNode);
	}

}

package complain;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.SearchFieldFromReferenceTable;
import annotation.TableName;
import common.ClientDTO;

@TableName("at_complaint")
public class ComplainDTO extends ActionForm
{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@ColumnName("cmID")
	long ID;
	@ColumnName("cmSubjectID")
    int subjectID;
	@ColumnName("cmClientID")
	@SearchFieldFromReferenceTable(entityClass = ClientDTO.class, entityColumnName = "loginName", operator = " like ")
	long clientID;
	@ColumnName("cmStatus")
	int status;
	@ColumnName("cmOtherSubject")
    String otherSubject;
	@ColumnName("cmPriority")
    int priority;
	@ColumnName("cmIsTaken")
    int isTaken;
	@ColumnName("cmSummary")
    String summary;
	@ColumnName("cmCreationTime")
    long creationTime;
	@ColumnName("cmModuleID")
    int moduleID;
	@ColumnName("cmRelatedComplainID")
	long relatedComplainID;
	
    ComplainHistoryDTO complainHistory = new ComplainHistoryDTO();

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public int getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}

	public long getClientID() {
		return clientID;
	}

	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOtherSubject() {
		return otherSubject;
	}

	public void setOtherSubject(String otherSubject) {
		this.otherSubject = otherSubject;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getIsTaken() {
		return isTaken;
	}

	public void setIsTaken(int isTaken) {
		this.isTaken = isTaken;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public int getModuleID() {
		return moduleID;
	}

	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}

	public long getRelatedComplainID() {
		return relatedComplainID;
	}

	public void setRelatedComplainID(long relatedComplainID) {
		this.relatedComplainID = relatedComplainID;
	}

	public ComplainHistoryDTO getComplainHistory() {
		return complainHistory;
	}

	public void setComplainHistory(ComplainHistoryDTO complainHistoryDTO) {
		this.complainHistory = complainHistoryDTO;
	}

	
	public void insert( ClientDTO dto, String message ) throws Exception {
		
		new ComplainService().insertComplainUsingSms(this, dto , message);
	}
	
	public static ComplainDTO makeComplainFromSMS( long clientId, String message, int moduleId ){
		
		ComplainDTO complainDTO = new ComplainDTO();
		
		complainDTO.setClientID( clientId );
		
		complainDTO.setStatus( ComplainConstants.STATUS_MAP_GET_VAL.get( "Submitted" ) );
		
		complainDTO.setSubjectID( ComplainConstants.SUBJECT_ID_FOR_SMS_COMPLAIN );
		
		complainDTO.setOtherSubject( ComplainConstants.SUBJECT_FOR_COMPLAIN_SMS );
		
		complainDTO.setPriority( ComplainConstants.PRIORITY_MAP_GET_VAL.get( "Medium" ));
		
		complainDTO.setIsTaken( ComplainConstants.COMPLAIN_NOT_TAKEN );
		
		complainDTO.setSummary( ComplainConstants.SUMMARY_FOR_COMPLAIN_SMS );
		
		complainDTO.setCreationTime( System.currentTimeMillis() );
		
		complainDTO.setModuleID( moduleId );
		
		return complainDTO;
		
	}

	@Override
	public String toString() {
		return "ComplainDTO [ID=" + ID + ", subjectID=" + subjectID + ", clientID=" + clientID + ", status=" + status
				+ ", otherSubject=" + otherSubject + ", priority=" + priority + ", isTaken=" + isTaken + ", summary="
				+ summary + ", creationTime=" + creationTime + ", moduleID=" + moduleID + ", relatedComplainID="
				+ relatedComplainID + ", complainHistory=" + complainHistory + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplainDTO other = (ComplainDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
    
}
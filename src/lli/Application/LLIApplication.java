package lli.Application;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import annotation.Transactional;
import common.RequestFailureException;
import lli.Application.Office.Office;
import lli.Application.newOffice.NewOffice;
import lli.LLIConnectionInstance;
import lli.LLIConnectionService;
import lli.connection.LLIConnectionConstants;
import lombok.ToString;
import util.ServiceDAOFactory;

import java.util.List;
@ToString
@TableName("at_lli_application")
public class LLIApplication {
    @PrimaryKey
    @ColumnName("applicationID")
    long applicationID;
    @ColumnName("applicationType")
    int applicationType;
    @ColumnName("submissionDate")
    long submissionDate;
    @ColumnName("clientID")
    long clientID;
    @ColumnName("userID")
    Long userID;
    @ColumnName("status")
    int status;
    @ColumnName("content")
    String content;
    @ColumnName("demandNoteID")
    Long demandNoteID;

    @ColumnName("billID")
    Long billID;

    @ColumnName("isDemandNoteNeeded")
    boolean isDemandNoteNeeded=true;
    @ColumnName("isServiceStarted")
    boolean isServiceStarted;
    @ColumnName("comment")
    String comment;
    @ColumnName("description")
    String description;
    @ColumnName("requestForCorrectionComment")
    String requestForCorrectionComment;
    @ColumnName("rejectionComment")
    String rejectionComment;
    @ColumnName("zone_id")
    int zoneId;
    @ColumnName("state")
    int state;
    @ColumnName("connection_id")
    int connectionId;
    @ColumnName("source_connection_id")
    int sourceConnectionID;
    @ColumnName("second_zone_id")
    int secondZoneID;
    @ColumnName("is_forwarded")
    int isForwarded;
    @ColumnName("loopProvider")
    int loopProvider;
    @ColumnName("duration")
    long duration;

    @ColumnName("bandwidth")
    double bandwidth;
    @ColumnName("connectionType")
    int connectionType;

    @ColumnName("suggestedDate")
    long suggestedDate;
    @ColumnName("skipPayment")
    long skipPayment = 0;

    @ColumnName("is_new_loop")
    boolean isNewLoop = false;

    boolean hasPermission = false;
    int policyType = 0;
    String[] documents;

    String stateDescription;
    String color;
    List<Office> officeList;
    List<NewOffice>newOfficeList;

    public boolean isNewLoop() {
        return isNewLoop;
    }

    public void setNewLoop(boolean newLoop) {
        isNewLoop = newLoop;
    }

    int ip;
    int port;



    public int getPolicyType() {
        return policyType;
    }

    public void setPolicyType(int policyType) {
        this.policyType = policyType;
    }

    public boolean getHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public int getSourceConnectionID() {
        return sourceConnectionID;
    }

    public void setSourceConnectionID(int sourceConnectionID) {
        this.sourceConnectionID = sourceConnectionID;
    }
    public void setPort(int portCount){
        this.port = portCount;
    }

    public int getPort(){
        return this.port;
    }

    public void setIp(int ipCount){this.ip = ipCount;}
    public int getIp(){
        return  this.ip;
    }


    public List<NewOffice> getNewOfficeList() {
        return newOfficeList;
    }

    public void setNewOfficeList(List<NewOffice> officeList) {
        this.newOfficeList = officeList;
    }

    public Long getBillID() {
        return billID;
    }

    public void setBillID(Long billID) {
        this.billID = billID;
    }

    public long getSkipPayment() {
        return skipPayment;
    }

    public void setSkipPayment(long skipPayment) {
        this.skipPayment = skipPayment;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLoopProvider() {
        return loopProvider;
    }

    public void setLoopProvider(int loopProvider) {
        this.loopProvider = loopProvider;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    public long getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(long suggestedDate) {
        this.suggestedDate = suggestedDate;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public int getSecondZoneID() {
        return secondZoneID;
    }

    public void setSecondZoneID(int secondZoneID) {
        this.secondZoneID = secondZoneID;
    }

    public int getIsForwarded() {
        return isForwarded;
    }

    public void setIsForwarded(int isForwarded) {
        this.isForwarded = isForwarded;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public long getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(long applicationID) {
        this.applicationID = applicationID;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
    }

    public long getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(long submissionDate) {
        this.submissionDate = submissionDate;
    }

    public long getClientID() {
        return clientID;
    }

    public void setClientID(long clientID) {
        this.clientID = clientID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDemandNoteID() {
        return demandNoteID;
    }

    public void setDemandNoteID(Long demandNoteID) {
        this.demandNoteID = demandNoteID;
    }

    public boolean isServiceStarted() {
        return isServiceStarted;
    }

    public void setServiceStarted(boolean isServiceStarted) {
        this.isServiceStarted = isServiceStarted;
    }

    public boolean isDemandNoteNeeded() {
        return isDemandNoteNeeded;
    }

    public void setDemandNoteNeeded(boolean isDemandNoteNeeded) {
        this.isDemandNoteNeeded = isDemandNoteNeeded;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRejectionComment() {
        return rejectionComment;
    }

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestForCorrectionComment() {
        return requestForCorrectionComment;
    }

    public void setRequestForCorrectionComment(String requestForCorrectionComment) {
        this.requestForCorrectionComment = requestForCorrectionComment;
    }


    @Transactional
    public void insertApplication() throws Exception {

        if (this instanceof SingleConnectionApplication) {
            SingleConnectionApplication singleConnectionApplication = (SingleConnectionApplication) this;
            long connectionID = singleConnectionApplication.getConnectionID();
            LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
            LLIConnectionInstance lliConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(connectionID);
            if (lliConnectionInstance == null) {
                throw new RequestFailureException("No connection found with connection ID " + connectionID);
            }
            if (lliConnectionInstance.getStatus() == LLIConnectionInstance.STATUS_CLOSED || lliConnectionInstance.getStatus() == LLIConnectionInstance.OWNERSHIP_CHANGED) {
                throw new RequestFailureException("The connection with connection ID " + lliConnectionInstance.getID() + " is not a valid connection anymore.");
            }
            if (lliConnectionInstance.getClientID() != this.clientID) {
                throw new RequestFailureException("This client is not the owner of the connection with Connection ID " + connectionID);
            }
        }


        if (this instanceof DoubleConnectionApplication) {
            DoubleConnectionApplication doubleConnectionApplication = (DoubleConnectionApplication) this;
            long secondConnectionID = doubleConnectionApplication.getSecondConnectionID();
            LLIConnectionService lliConnectionService = ServiceDAOFactory.getService(LLIConnectionService.class);
            LLIConnectionInstance secondConnectionInstance = lliConnectionService.getLLIConnectionByConnectionID(secondConnectionID);
            if (secondConnectionInstance == null) {
                throw new RequestFailureException("No destination connection found with connection ID " + secondConnectionID);
            }

            if (secondConnectionInstance.getClientID() != this.clientID) {
                throw new RequestFailureException("You can not set other client's connection as destination connection.");
            }
            if (secondConnectionInstance.getStatus() == LLIConnectionInstance.STATUS_CLOSED || secondConnectionInstance.getStatus() == LLIConnectionInstance.OWNERSHIP_CHANGED) {
                throw new RequestFailureException("The destination connection with connection ID " + secondConnectionInstance.getID() + " is not a valid connection anymore.");
            }
        }


        this.setSubmissionDate(System.currentTimeMillis());
        this.setStatus(LLIConnectionConstants.STATUS_APPLIED);
    }

    public void setImmutablePropertyWhileProcessing(LLIApplication lastApplicationSnapshot) throws Exception {
    }

    @Transactional
    public void completeApplication() throws Exception {
    }

    public String[] getDocuments() {
        return documents;
    }

    public void setDocuments(String[] documents) {
        this.documents = documents;
    }


}

package file.upload;

import java.io.Serializable;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("at_uploaded_file")
public class FileForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("moduleId")
    long moduleId;
    @ColumnName("componentId")
    long componentId;
    @ColumnName("applicationId")
    long applicationId;

    @ColumnName("realName")
    String realName;
    @ColumnName("localName")
    String localName;
    @ColumnName("ownerId")
    long ownerId;
    @ColumnName("size")
    long size;
    @ColumnName("uploadTime")
    long uploadTime;
    @ColumnName("directoryPath")
    String directoryPath;
    @ColumnName("isDeleted")
    boolean isDeleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    @Override
    public String toString() {
        return "FileForm{" +
                "id=" + id +
                ", moduleId=" + moduleId +
                ", componentId=" + componentId +
                ", applicationId=" + applicationId +
                ", realName='" + realName + '\'' +
                ", localName='" + localName + '\'' +
                ", ownerId=" + ownerId +
                ", size=" + size +
                ", uploadTime=" + uploadTime +
                ", directoryPath='" + directoryPath + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

}

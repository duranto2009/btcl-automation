package file.upload;

public class FileJsonParams {
    long moduleId;
    long componentId;
    long applicationId;
    long stateId;

    public FileJsonParams(long moduleId, long componentId, long applicationId, long stateId) {
        this.moduleId = moduleId;
        this.componentId = componentId;
        this.applicationId = applicationId;
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return "FileJsonParams{" +
                "moduleId=" + moduleId +
                ", componentId=" + componentId +
                ", applicationId=" + applicationId +
                ", stateId=" + stateId +
                '}';
    }
}

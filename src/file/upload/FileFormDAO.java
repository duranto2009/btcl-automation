package file.upload;

import file.FileDTO;
import util.ModifiedSqlGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileFormDAO {

    public void addNewFile(FileForm fileForm) throws Exception {
        ModifiedSqlGenerator.insert(fileForm);
    }

    public FileForm getFileById(long id) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(FileForm.class, id);
    }

    public void deleteFileById(long id) throws Exception {
        ModifiedSqlGenerator.deleteHardEntityByID(FileForm.class, id);
    }

    public List<FileForm> getAllFiles(long moduleId, long componentId, long applicationId, long stateId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FileForm.class,
                " where moduleId = " + moduleId +
                        " and componentId = " + componentId +
                        " and applicationId = " + applicationId +
                        " and stateId = " + stateId);
    }

    public List<FileForm> getAllFilesByModuleIdAndApplicationId(long moduleId, long applicationId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FileForm.class,
                " where moduleId = " + moduleId +
                        " and applicationId = " + applicationId);
    }

    public List<FileForm> getAllFilesByApplicationIdAndStateId(long applicationId, long stateId) throws Exception {
        List<FileVsState> fileVsStates = getAllFilesByStateId(stateId);

        List<FileForm> files = new ArrayList<>();
        for (FileVsState fileVsState : fileVsStates) {

            FileForm fileForm = ModifiedSqlGenerator.getObjectByID(FileForm.class, fileVsState.getFileId());
            if (fileForm.getApplicationId() == applicationId) {
                files.add(fileForm);
            }
        }
        return files;
    }


    public void addNewStateForAFile(FileVsState fileVsState) throws Exception {
        ModifiedSqlGenerator.insert(fileVsState);
    }

    public List<FileVsState> getAllFilesByStateId(long stateId) throws Exception {
        List<FileVsState> fileVsStates = ModifiedSqlGenerator.getAllObjectList(FileVsState.class,
                " Where " + ModifiedSqlGenerator.getColumnName(FileVsState.class, "stateId")
                        + " = " + stateId);

        return fileVsStates;
    }
}

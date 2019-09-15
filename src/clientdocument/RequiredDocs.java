package clientdocument;

import lombok.Data;
import util.KeyValuePair;

import java.util.List;

@Data
public class RequiredDocs {
    List<FileAttributes> commonFiles;
    List<KeyValuePair<ClientDocumentTypeDTO, Boolean>> docTypes;
}

@Data
class FileAttributes {
    long docID;
    String docTypeName;
    String docActualFileName;
    String docSizeStr;

    FileAttributes(long docID, String docTypeName, String docActualFileName, String docSizeStr) {
        this.docID = docID;
        this.docTypeName = docTypeName;
        this.docActualFileName = docActualFileName;
        this.docSizeStr = docSizeStr;
    }
}


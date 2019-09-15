package test;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FileUploadForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private FormFile doc;

	public FormFile getDoc() {
		return doc;
	}

	public void setDoc(FormFile doc) {
		this.doc = doc;
	}
}

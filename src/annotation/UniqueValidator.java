/**
 * 
 */
package annotation;

import java.lang.annotation.Annotation;
import java.util.List;

import clientdocument.ClientDocumentTypeDTO;
import common.RequestFailureException;
import util.ModifiedSqlGenerator;
import validator.PojoValidator;

/**
 * @author Touhid
 *
 */
public class UniqueValidator implements PojoValidator {

	@Override
	public void validate(String fieldName, Object fieldValue, Annotation annotation) {
		System.out.println("checked");
		Unique unique = (Unique) annotation;

		long value = (long) fieldValue;
		List<ClientDocumentTypeDTO> clientDocumentTypeDTOs = null;
		try {
			clientDocumentTypeDTOs = ModifiedSqlGenerator.getAllObjectList(ClientDocumentTypeDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (clientDocumentTypeDTOs != null) {
			for (ClientDocumentTypeDTO clientDocumentTypeDTO : clientDocumentTypeDTOs) {
				if(value == clientDocumentTypeDTO.getDocTypeId()) {
					throw new RequestFailureException("Already exists");
				}

			}
		}

	
	}

}

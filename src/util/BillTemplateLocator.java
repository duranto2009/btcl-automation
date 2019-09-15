/**
 * 
 */
package util;

import common.bill.BillDTO;

/**
 * @author Alam
 */
@FunctionalInterface
public interface BillTemplateLocator {
	String getTemplateFilename(BillDTO billDTO);
}

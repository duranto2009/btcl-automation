package vpn.client;

import common.ModuleConstants;

public class ClientUpdateChecker {
	/*public static final int client_approved_remainder = DomainStateConstants.REQUEST_NEW_CLIENT.CLIENT_APPROVED
			% ModuleConstants.MULTIPLIER;
	public static final int client_verified_remainder = DomainStateConstants.REQUEST_NEW_CLIENT.CLIENT_VERIFIED
			% ModuleConstants.MULTIPLIER;
	public static final int client_requested_for_correction_remainder = DomainStateConstants.REQUEST_NEW_CLIENT.CLIENT_REQUESTED_FOR_CORRECTION
			% ModuleConstants.MULTIPLIER;*/

	ClientUpdateChecker() {

	}

	public static boolean isClientAllowedForUpdate(int currentStatus) {
		/*int client_current_status_remainder = currentStatus % ModuleConstants.MULTIPLIER;
		if ((client_current_status_remainder != client_verified_remainder) && (client_current_status_remainder != client_approved_remainder)
				*//*&& (client_current_status_remainder != client_requested_for_correction_remainder)*//* ) {
			return true;
		}*/
		return true;
	}

}

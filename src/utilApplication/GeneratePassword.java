package utilApplication;

import util.PasswordService;

public class GeneratePassword {

	public static void main(String[] args) throws Exception {
		String password = "turkish_hackers_from_netherland_14012018";
		//String password = "dhrubo_admin";
		System.out.println(PasswordService.getInstance().encrypt(password));
		System.exit(0);
	}

}

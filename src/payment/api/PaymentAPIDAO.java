package payment.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.mysql.fabric.Response;

import connection.DatabaseConnection;

public class PaymentAPIDAO {
	Logger logger = Logger.getLogger(getClass());
	
	
	public void validateBranchCodeWithBankCode(String bankCode,String branchCode,
			DatabaseConnection databaseConnection) throws Exception{
		String sql = "select exists(select * from at_payment_api_client_branch_code where bankCode=? and branchCode=?)";
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, bankCode);
		ps.setObject(2, branchCode);
		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)!=1){
			throw new PaymentApiException(ResponseCode.INVALID_CREDENTIAL, "invalid branch code");
		}
	}
	
	public boolean isValidUser(String userName,String password,DatabaseConnection databaseConnection) throws Exception{
		String sql = " select 1 from at_payment_api_client where paclUserName = ? and paclPassword = ?";
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, userName);
		ps.setObject(2, password);
		logger.debug(ps);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	public String getBankCode(String userName,String password,DatabaseConnection databaseConnection) throws Exception{
		
		String sql = "select paclBankCode from at_payment_api_client where paclUserName = ? and paclPassword = ?";
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, userName);
		ps.setObject(2, password);
		ResultSet rs = ps.executeQuery();
		String bankCode = null;
		if(rs.next()){
			bankCode = rs.getString(1);
		}
		return bankCode;
	}
	public void changePassword(String userName,String oldPassword,String newPassword,DatabaseConnection databaseConnection) throws Exception{
		String sql = "update at_payment_api_client set paclPassword = ? where paclUserName = ? and paclPassword = ?";
		logger.debug("sql: "+sql);
		PreparedStatement ps = databaseConnection.getNewPrepareStatement(sql);
		ps.setObject(1, newPassword);
		ps.setObject(2, userName);
		ps.setObject(3, oldPassword);
		logger.debug(ps);
		int numberOfAffectedRows = ps.executeUpdate();
		if(numberOfAffectedRows == 0){
			throw new Exception("Password update failed");
		}
	}
}

package user;

import java.util.HashMap;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("aduser")
public class UserDTO
{
	public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_BLOCKED = 1;
    public static final HashMap<Integer, String> userStatusMap= new HashMap<>();
    static{
    	userStatusMap.put(STATUS_ACTIVE, "<label class='label label-success'>Active</label>");
    	userStatusMap.put(STATUS_BLOCKED, "<label class='label label-danger'>Blocked</label>");
    }
    
    @PrimaryKey
    @ColumnName("usUserID")
    private long userID;
    @ColumnName("usUserName")
    private String username;
    @ColumnName("usPassword")
    private String password;
    @ColumnName("usRoleID")
    private long roleID;
    @ColumnName("usMailAddr")
    private String  mailAddress;
    @ColumnName("usStatus")
    private int status;
    @ColumnName("usFullName")
    private String fullName;
    @ColumnName("usDesignation")
    private String designation;
    
    @ColumnName("usPhoneNo")
    private String phoneNo;
    @ColumnName("usAdditionalInfo")
    private String additionalInfo;
    @ColumnName("usSecurityToken")
    private String secToken;
    @ColumnName("usAddedBy")
    private long addedBy;
    @CurrentTime
    @ColumnName(value ="usAddTime",editable = false)
    private long addTime;
    @ColumnName("usLastModifiedBy")
    private long lastModifiedBy;
    @CurrentTime
    @ColumnName("usLastModificationTime")
    private long lastModificationTime;
    @ColumnName("usProfilePicturePath")
    private String profilePicturePath;
    @ColumnName("usIsDeleted")
    private boolean isDeleted;
    @ColumnName("usBalance")
    private double balance;
    @ColumnName("usLoginIPs")
    private String loginIPs;
    
    
    @ColumnName("usDeptName")
    private String departmentName;
    
    @ColumnName("usDistrictID")
    private Long districtID;
    @ColumnName("usUpazilaID")
    private Long upazilaID;
    @ColumnName("usUnionID")
    private Long unionID;
    
    @ColumnName("usIsBTCLPersonnel")
    private boolean isBTCLPersonnel;
    @ColumnName("usAddress")
    private String address;


    @ColumnName("usZoneID")
    private int zoneID;

    public int getZoneID() {
        return zoneID;
    }

    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public long getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(long  addedBy) {
		this.addedBy =  addedBy;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long  addTime) {
		this.addTime =  addTime;
	}

	public long getLastModifiedBy() {
		return  lastModifiedBy;
	}

	public void setLastModifiedBy(long  lastModifiedBy) {
		this. lastModifiedBy =  lastModifiedBy;
	}

	public long getLastModifyTime() {
		return lastModificationTime;
	}

	public void setLastModifyTime(long  lastModifyTime) {
		this.lastModificationTime =  lastModifyTime;
	}
	
	public int getStatus() {
        return  status;
    }
    
    public void setStatus(int p_status) {
         status = p_status;
    }

    public String getAdditionalInfo() {
        return  additionalInfo;
    }

    public void setAdditionalInfo(String p_additional) {
         additionalInfo = p_additional;
    }

    public String getDesignation(){
        return  designation;
    }

    public void setDesignation(String p_designation){
         designation = p_designation;
    }

    public String getPhoneNo() {
        return  phoneNo;
    }

    public void setPhoneNo(String p_phoneNo) {
         phoneNo = p_phoneNo;
    }

    public String getFullName(){
        return  fullName;
    }

    public void setFullName(String p_fullName) {
         fullName = p_fullName;
    }

    public String getLoginIPs(){
        return  loginIPs;
    }

    public void setLoginIPs(String p_loginIPs) {
         loginIPs = p_loginIPs;
    }

    public String getMailAddress(){
        return  mailAddress;
    }

    public void setMailAddress(String p_mailAddress){
         mailAddress = p_mailAddress;
    }

    public String getSecToken(){
        return  secToken;
    }

    public void setSecToken(String p_secToken){
         secToken = p_secToken;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long p_userID){
        userID = p_userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String p_username){
        username = p_username;
    }

    public String getPassword(){
        return  password;
    }

    public void setPassword(String p_password){
         password = p_password;
    }

    public long getRoleID(){
        return  roleID;
    }

    public void setRoleID(long p_roleID){
         roleID = p_roleID;
    }

    public String getProfilePicturePath() {
		return  profilePicturePath;
	}

	public void setProfilePicturePath(String profilePicturePath) {
		 this.profilePicturePath = profilePicturePath;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public Long getDistrictID() {
		return districtID;
	}

	public void setDistrictID(Long districtID) {
		this.districtID = districtID;
	}

	public Long getUpazilaID() {
		return upazilaID;
	}

	public void setUpazilaID(Long upazilaID) {
		this.upazilaID = upazilaID;
	}

	public Long getUnionID() {
		return unionID;
	}

	public void setUnionID(Long unionID) {
		this.unionID = unionID;
	}
	

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

    public boolean isBTCLPersonnel() {
		return isBTCLPersonnel;
	}

	public void setBTCLPersonnel(boolean isBTCLPersonnel) {
		this.isBTCLPersonnel = isBTCLPersonnel;
	}

	public UserDTO(){
         status = 0;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userID ^ (userID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		if (userID != other.userID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDTO [userID=" + userID + ", username=" + username + ", password=" + password + ", roleID=" + roleID
				+ ", mailAddress=" + mailAddress + ", status=" + status + ", fullName=" + fullName + ", designation="
				+ designation + ", phoneNo=" + phoneNo + ", additionalInfo=" + additionalInfo + ", secToken=" + secToken
				+ ", addedBy=" + addedBy + ", addTime=" + addTime + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModificationTime=" + lastModificationTime + ", profilePicturePath=" + profilePicturePath
				+ ", isDeleted=" + isDeleted + ", balance=" + balance + ", loginIPs=" + loginIPs + ", departmentName="
				+ departmentName + ", districtID=" + districtID + ", upazilaID=" + upazilaID + ", unionID=" + unionID
				+ ", isBTCLPersonnel=" + isBTCLPersonnel + "]";
	}


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
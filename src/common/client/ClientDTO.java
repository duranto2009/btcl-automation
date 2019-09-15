package common.client;

public class ClientDTO {
    long applicationId;
    long clientId;
    long userId;
    long demandNoteId;
    String loginName;
    double balance;
    String name;
    String lastName;
    String fatherName;
    String motherName;
    String gender;
    String dateOfBirth;
    String webAddress;
    String address;
    String city;
    String postCode;
    String country;
    String email;
    String fax;
    String phone;
    String signature;
    int jobType;
    String occupation;
    String contactInfoName;
    String landLineNumber;

    public ClientDTO() {
    }

    public ClientDTO(long applicationId, long clientId, long userId, long demandNoteId, String loginName, double balance, String name, String lastName, String fatherName, String motherName, String gender, String dateOfBirth, String webAddress, String city, String postCode, String country, String email, String fax, String phone, String signature, int jobType, String occupation, String contactInfoName, String landLineNumber) {
        this.applicationId = applicationId;
        this.clientId = clientId;
        this.userId = userId;
        this.demandNoteId = demandNoteId;
        this.loginName = loginName;
        this.balance = balance;
        this.name = name;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.webAddress = webAddress;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.email = email;
        this.fax = fax;
        this.phone = phone;
        this.signature = signature;
        this.jobType = jobType;
        this.occupation = occupation;
        this.contactInfoName = contactInfoName;
        this.landLineNumber = landLineNumber;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDemandNoteId() {
        return demandNoteId;
    }

    public void setDemandNoteId(long demandNoteId) {
        this.demandNoteId = demandNoteId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContactInfoName() {
        return contactInfoName;
    }

    public void setContactInfoName(String contactInfoName) {
        this.contactInfoName = contactInfoName;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "applicationId=" + applicationId +
                ", clientId=" + clientId +
                ", userId=" + userId +
                ", demandNoteId=" + demandNoteId +
                ", loginName='" + loginName + '\'' +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", webAddress='" + webAddress + '\'' +
                ", city='" + city + '\'' +
                ", postCode='" + postCode + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                ", phone='" + phone + '\'' +
                ", signature='" + signature + '\'' +
                ", jobType=" + jobType +
                '}';
    }
}

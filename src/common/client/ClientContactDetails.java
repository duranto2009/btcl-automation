package common.client;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author maruf
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
@TableName("at_client_contact_details")
public class ClientContactDetails {

    @PrimaryKey
    @ColumnName("vclcID")
    long id;
    @ColumnName("vclcVpnClientID")
    long vpnClientId;
    @ColumnName("vclcName")
    String name;
    @ColumnName("vclcLastName")
    String lastName;
    @ColumnName("vclcFatherName")
    String fatherName;
    @ColumnName("vclcMotherName")
    String motherName;
    @ColumnName("vclcGender")
    String gender;
    @ColumnName("vclcDateOfBirth")
    String dateOfBirth;
    @ColumnName("vclcWebAddress")
    String webAddress;
    @ColumnName("vclcDetailsType")
    int detailsType;
    @ColumnName("vclcAddress")
    String address;
    @ColumnName("vclcCity")
    String city;
    @ColumnName("vclcPostCode")
    String postalCode;
    @ColumnName("vclcCountry")
    String country;
    @ColumnName("vclcEmail")
    String email;
    @ColumnName("vclcIsEmailVerified")
    boolean isEmailVerified;
    @ColumnName("vclcOrganization")
    String organization;
    @ColumnName("vclcDate")
    long dateTimestamp;
    @ColumnName("vclcFaxNumber")
    String fax;
    @ColumnName("vclcPhoneNumber")
    String phone;
    @ColumnName("vclcIsPhoneNumberVerified")
    int isPhoneVerified;
    @ColumnName("vclcSignature")
    String signature;
    @ColumnName("vclcContactInfoName")
    String contactInfoName;
    @ColumnName("vclcIsDeleted")
    boolean isDeleted;
    @ColumnName("vclcOccupation")
    String occupation;
    @ColumnName("vclcJobType")
    int jobType;
    @ColumnName("vclcLandlineNumber")
    String landLineNumber;
}

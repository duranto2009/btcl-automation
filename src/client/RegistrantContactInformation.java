package client;

import lombok.Data;

@Data
public class RegistrantContactInformation {
    String registrantsName;
    String registrantsLastName;
    String phoneNumber;
    int isPhoneNumberVerified;
    String email;
    int isEmailVerified;
    String webAddress;
    String city;
    String postCode;
    String address;
    String country;
    int detailsType;
    String landlineNumber;
    String faxNumber;
}

package client;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrantInformation {

    String loginName;
    String loginPassword;
    String confirmPassword;

    int moduleID;
    int registrantType;
    int regCategory;
    int regSubCategory;
    int clientCategoryType=2;

    List<RegistrantContactInformation> contactInfoList =  new ArrayList<>();

    String captchaAnswer;
}

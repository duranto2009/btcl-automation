package lli.Application;

import annotation.Transactional;
import com.google.gson.*;
import common.ClientDTO;
import common.RequestFailureException;
import common.ValidationService;
import common.repository.AllClientRepository;
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.FlowConnectionManager.LLIFlowConnectionService;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.LocalLoop.LocalLoopConditionBuilder;
import lli.Application.LocalLoop.LocalLoopService;
import lli.Application.Office.Office;
import lli.Application.Office.OfficeConditionBuilder;
import lli.Application.newOffice.NewOffice;
import lli.LLIConnectionService;
import lli.LLIOfficeService;
import lli.connection.LLIConnectionConstants;
import location.Zone;
import util.ServiceDAOFactory;
import util.TimeConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.ServiceDAOFactory.getService;

public class LLIApplicationDeserializer implements JsonDeserializer<LLIApplication> {

    LLIApplicationService lliApplicationService = getService(LLIApplicationService.class);
    LLIFlowConnectionService lliFlowConnectionService = getService(LLIFlowConnectionService.class);
    LLIOfficeService lliOfficeService = ServiceDAOFactory.getService(LLIOfficeService.class);
    LocalLoopService localLoopService = ServiceDAOFactory.getService(LocalLoopService.class);
    GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);


    @Override
    public LLIApplication deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context) throws JsonParseException {
        LLIApplication lliApplication = new LLIApplication();


        //Receive JSON
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        validate(jsonObject);


        //common form element for all
        lliApplication.setBandwidth(jsonObject.get("bandwidth") != null ? jsonObject.get("bandwidth").getAsDouble() : 0);

        //added by jami 15/06/19
        if (jsonObject.get("suggestedDate") == null) {
            throw new RequestFailureException("Select a Date first");
        }
        else if (jsonObject.get("suggestedDate").getAsLong()< TimeConverter.getStartTimeOfTheDay(System.currentTimeMillis())) {
            throw new RequestFailureException("You can not set suggested date before current date!!");
        }
        else {
            lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        }
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
//        lliApplication.setSkipPayment(jsonObject.get("skipPayment").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setSkipPayment(jsonObject.get("skipPayment") != null ? jsonObject.get("skipPayment").getAsJsonObject().get("ID").getAsInt() : 0);
        lliApplication.setPolicyType(jsonObject.get("policyType") != null ? jsonObject.get("policyType").getAsJsonObject().get("ID").getAsInt() : 0);


        if (jsonObject.get("connection") != null) {

            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            LLIConnection lliConnection = new LLIConnection();


            if (connectionID > 0) {
                List<LLIApplication> lliApplication1 = null;


                try {
                    List<LLIConnection> lliConnections = lliFlowConnectionService.getConnectionByClient(lliApplication.getClientID());
                    lliConnections = lliConnections
                            .stream()
                            .filter(t -> t.getID() == connectionID)
                            .collect(Collectors.toList());

                    if (lliConnections.size() == 0) {
                        throw new RequestFailureException("Connection and Client mismatched ");
                    }


                    lliConnection = lliConnections.get(0);

                    if (lliApplication.getSuggestedDate() < System.currentTimeMillis()) {
                        throw new RequestFailureException("Suggested date must be greater than system date");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                lliApplication.setZoneId(lliConnection.getZoneID());
                lliApplication.setConnectionId((int) connectionID);
                try {
//                    LLIOffice lliOffices=lliOfficeService.getLLIOfficeListByConnectionHistoryID(lliConnection.getHistoryID()).get(0);

                    List<Office> officeList=globalService.getAllObjectListByCondition(Office.class,
                            new OfficeConditionBuilder()
                            .Where()
                            .connectionIDEquals(lliConnection.getHistoryID())
                            .getCondition()
                            );

                    if(officeList!=null && officeList.size()>0) {
                        List<LocalLoop> localLoopList = globalService.getAllObjectListByCondition(LocalLoop.class,
                                new LocalLoopConditionBuilder()
                                        .Where()
                                        .officeIDEquals(officeList.get(0).getId())
                                        .getCondition()
                        );
                        if (localLoopList != null && localLoopList.size() > 0) {
                            LocalLoop localLoop = localLoopList.get(0);
                            if (localLoop.getBTCLDistances() == 0 && localLoop.getOCDistances() == 0) {
                                lliApplication.setLoopProvider(LLIConnectionConstants.LOOP_PROVIDER_CLIENT);
                            }
                        } else {
                            throw new RequestFailureException("No localLoop Found For this connection");
                        }
                    }else{
                        throw new RequestFailureException("No office Found For this connection");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                lliApplication.setConnectionType(lliConnection.getConnectionType());


            }
        } else {

            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID());
            if (clientDTO.isCorporate()) {
                lliApplication.setZoneId(0);

            } else {

                long zoneId = jsonObject.get("zone").getAsJsonObject().get("id").getAsLong();
                try {
                    Zone zone = globalService.findByPK(Zone.class, zoneId);
                    if (zone.isCentral()) {
                        lliApplication.setZoneId(0);

                    } else {

                        lliApplication.setZoneId((int) zoneId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            lliApplication.setConnectionType(jsonObject.get("connectionType") != null ? jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt() : 0);
            lliApplication.setLoopProvider((jsonObject.get("loopProvider") != null ? jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt() : 0));
            lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
            JsonArray jsonArray = (JsonArray) jsonObject.get("officeList");
            List<Office> officeList = new ArrayList<>();
            if (jsonArray != null) {
                for (JsonElement jsonElement1 : jsonArray
                ) {
                    JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
                    Office office = new Office();
                    office.setOfficeName(officeJsonObject.get("officeName").getAsString());
                    office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
//			office.set
                    officeList.add(office);
                }
                lliApplication.setOfficeList(officeList);
            }
        }

        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
//        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : 0);
        lliApplication.setSubmissionDate(System.currentTimeMillis());
        lliApplication.setStatus(jsonObject.get("state") != null ? jsonObject.get("state").getAsInt() : 0);
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);
        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");

        lliApplication.setRejectionComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : null);
        lliApplication.setSecondZoneID(jsonObject.get("oldZone") != null ? jsonObject.get("oldZone").getAsInt() : 0);
        lliApplication.setIsForwarded(jsonObject.get("isForwarded") != null ? jsonObject.get("isForwarded").getAsInt() : 0);

        return lliApplication;
    }

    public LLIApplication deserialize_custom(JsonElement jsonElement) throws Exception {
        LLIApplication lliApplication = new LLIApplication();
        LLIApplicationValidationService lliApplicationValidationService = getService(LLIApplicationValidationService.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        validate(jsonObject);
        lliApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
        if (jsonObject.get("connectionType").isJsonObject()) {

            lliApplication.setConnectionType(jsonObject.get("connectionType") != null ? jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt() : 0);
        } else {
            lliApplication.setConnectionType(jsonObject.get("connectionType").getAsInt());

        }
        lliApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
        lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
        if (jsonObject.get("suggestedDate") == null) throw new RequestFailureException("Select a Date first");
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : 0);
        lliApplication.setStatus(jsonObject.get("state").getAsInt());
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);
        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");
        lliApplication.setRejectionComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : null);
        if (jsonObject.has("zone")) {
            if (jsonObject.get("zone").getAsJsonObject().has("id")) {
                int zoneId = (int) jsonObject.get("zone").getAsJsonObject().get("id").getAsLong();
                try {
                    Zone zone = globalService.findByPK(Zone.class, zoneId);
                    ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(lliApplication.getClientID());
                    if (zone.isCentral() || clientDTO.isCorporate()) {
                        lliApplication.setZoneId(0);

                    } else {

                        lliApplication.setZoneId(zoneId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else lliApplication.setZoneId(0);
        } else {
            lliApplication.setZoneId(0);
        }
        lliApplication.setState((int) jsonObject.get("state").getAsLong());
        if (jsonObject.get("skipPayment") != null) {
            if (jsonObject.get("skipPayment").getAsJsonObject().has("ID")) {

                lliApplication.setSkipPayment(jsonObject.get("skipPayment") != null ? jsonObject.get("skipPayment").getAsJsonObject().get("ID").getAsInt() : 0);
            } else {
                lliApplication.setSkipPayment(0);
            }

        } else {
            lliApplication.setSkipPayment(jsonObject.get("skipPay") != null ? jsonObject.get("skipPay").getAsLong() : 0);
        }


        JsonArray jsonArray = (JsonArray) jsonObject.get("officeList");
        List<Office> officeList = new ArrayList<>();
        for (JsonElement jsonElement1 : jsonArray
        ) {
            JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
            Office office = new Office();
            if (officeJsonObject.get("id") != null) {
                if (officeJsonObject.get("id").getAsLong() > 0) {
                    office.setId(officeJsonObject.get("id").getAsLong());
                    office.setApplicationId(officeJsonObject.get("applicationId").getAsLong());
                    office.setHistoryId(officeJsonObject.get("historyId").getAsLong());
                    office.setConnectionID(officeJsonObject.get("connectionID").getAsLong());
                }
            }
            office.setOfficeName(officeJsonObject.get("officeName").getAsString());
            office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
//			office.set
            officeList.add(office);
        }
        lliApplication.setOfficeList(officeList);


        //Deserialize Common LLI Application


        try {

            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            List<LLIApplication> lliApplication1 = lliApplicationService.getLLIApplicationByConnectionID(connectionID);
            if (lliApplication1.size() > 0) {
                LLIApplication application = lliApplication1.get(0);
                lliApplication.setZoneId(application.getZoneId());
                lliApplication.setConnectionId(application.getConnectionId());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.get("sourceConnection") != null) {
            lliApplicationValidationService.validateExistence(jsonObject, "sourceConnection", "Source Connection");
            lliApplication.setSourceConnectionID(jsonObject.get("sourceConnection").getAsJsonObject().get("ID").getAsInt());
            try {
                int destZone = lliApplication.getZoneId();
                lliApplication.setZoneId(lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID()).getZoneID());
                lliApplication.setSecondZoneID(destZone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return lliApplication;
    }

    //region jaminur
    public LLIApplication deserialize_custom_port(JsonElement jsonElement) throws JsonParseException {
        LLIApplication lliApplication = new LLIApplication();

        //Receive JSON
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//		lliApplication=deserialize(jsonElement,)
//        validate(jsonObject);

//        lliApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
//        lliApplication.setConnectionType(jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt());
        lliApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
        lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

//        JsonArray jsonArray = (JsonArray) jsonObject.get("officeList");
//        List<Office> officeList = new ArrayList<>();
//        for (JsonElement jsonElement1 : jsonArray
//                ) {
//            JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
//            Office office = new Office();
//            office.setOfficeName(officeJsonObject.get("officeName").getAsString());
//            office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
////			office.set
//            officeList.add(office);
//        }
//        lliApplication.setOfficeList(officeList);


        //Deserialize Common LLI Application
        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : 0);
//		lliApplication.setStatus(jsonObject.get("status") != null ? jsonObject.get("status").getAsJsonObject().get("ID").getAsInt() : 0);
//        lliApplication.setStatus(jsonObject.get("state").getAsInt());
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);

        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");
        lliApplication.setRejectionComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : null);
//        lliApplication.setZoneId((int) jsonObject.get("zone").getAsJsonObject().get("id").getAsLong());
//        lliApplication.setState((int) jsonObject.get("state").getAsLong());

//		lliApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());
//
//		lliApplication.setConnectionType(jsonObject.get("connectionType").getAsJsonObject().get("ID").getAsInt());
//
//		lliApplication.setLoopProvider((jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt()));
//		lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
//
//		lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        try {

            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            List<LLIApplication> lliApplication1 = lliApplicationService.getLLIApplicationByConnectionID(connectionID);
            if (lliApplication1.size() > 0) {
                LLIApplication application = lliApplication1.get(0);
                lliApplication.setZoneId(application.getZoneId());
                lliApplication.setConnectionId(application.getConnectionId());

            } else {

                lliApplication.setZoneId((int) jsonObject.get("zone").getAsJsonObject().get("id").getAsLong());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return lliApplication;
    }

    public LLIApplication deserialize_local_loop(JsonElement jsonElement) throws JsonParseException {
        LLIApplication lliApplication = new LLIApplication();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        lliApplication.setPort(jsonObject.get("portCount").getAsInt());
        lliApplication.setLoopProvider(jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt());
        JsonArray jsonArray = (JsonArray) jsonObject.get("addedOfficeList");
        List<NewOffice> officeList = new ArrayList<>();
        for (JsonElement jsonElement1 : jsonArray) {
            JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
            NewOffice office = new NewOffice();
            office.setOfficeName(officeJsonObject.get("officeName").getAsString());
            office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
            officeList.add(office);
        }

        JsonArray jsonArray2 = (JsonArray) jsonObject.get("selectedOfficeList");
        for (JsonElement jsonElement1 : jsonArray2) {
            JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
            long id = officeJsonObject.get("ID").getAsLong();
            String name = officeJsonObject.get("label").getAsString();
            String address = officeJsonObject.get("object").getAsJsonObject().get("officeAddress").getAsString();
            NewOffice office = new NewOffice();
            office.setOfficeName(name);
            office.setOfficeAddress(address);
            office.setOld_office_id(id);
            officeList.add(office);
        }

        lliApplication.setNewOfficeList(officeList);

        lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        //Deserialize Common LLI Application
        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : System.currentTimeMillis());
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);

        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");
        lliApplication.setRejectionComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : null);
        try {
            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            LLIConnection flowConnection = lliFlowConnectionService.getConnectionByID(connectionID);
            lliApplication.setZoneId(flowConnection.getZoneID());
            lliApplication.setConnectionType(flowConnection.getConnectionType());
            lliApplication.setConnectionId((int) connectionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lliApplication;
    }

    //deserializer for additional ip application
    public LLIApplication deserialize_ip(JsonElement jsonElement) throws JsonParseException {
        LLIApplication lliApplication = new LLIApplication();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        lliApplication.setIp(jsonObject.get("ipCount").getAsInt());

        lliApplication.setDuration(jsonObject.get("duration") != null ? jsonObject.get("duration").getAsInt() : 0);
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        //Deserialize Common LLI Application
        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : 0);
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);

        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");
        lliApplication.setRejectionComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : null);
        try {
            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            LLIConnection flowConnection = lliFlowConnectionService.getConnectionByID(connectionID);
            lliApplication.setZoneId(flowConnection.getZoneID());
            lliApplication.setConnectionType(flowConnection.getConnectionType());
            lliApplication.setConnectionId((int) connectionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lliApplication;
    }
    //end deserializer for additional ip

    public LLIApplication deserializeCustomShiftBW(JsonElement jsonElement) throws JsonParseException {
        LLIApplication lliApplication = new LLIApplication();


        //Receive JSON
        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        validate(jsonObject);
        LLIApplicationValidationService lliApplicationValidationService = getService(LLIApplicationValidationService.class);


        //parse client
        lliApplicationValidationService.validateExistingClient(jsonObject);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);


        //parse source id
        lliApplicationValidationService.validateExistence(jsonObject, "sourceConnection", "Source Connection");
        lliApplication.setSourceConnectionID(jsonObject.get("sourceConnection").getAsJsonObject().get("ID").getAsInt());
        try {
            lliApplication.setZoneId(lliFlowConnectionService.getConnectionByID(lliApplication.getSourceConnectionID()).getZoneID());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //parse destination id
        lliApplicationValidationService.validateExistence(jsonObject, "destinationConnection", "Destination Connection");
        lliApplication.setConnectionId(jsonObject.get("destinationConnection").getAsJsonObject().get("ID").getAsInt());
        try {
            lliApplication.setSecondZoneID(lliFlowConnectionService.getConnectionByID(lliApplication.getConnectionId()).getZoneID());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //parse bandwidth
        lliApplicationValidationService.validatePositiveNumber(jsonObject, "bandwidth", "Bandwidth");
        lliApplication.setBandwidth(jsonObject.get("bandwidth").getAsDouble());

        //parse description
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");

        //parse comment
        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");

        //parse suggested Date
        lliApplicationValidationService.validatePositiveNumber(jsonObject, "suggestedDate", "Suggested Date");
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());

        return lliApplication;
    }


    private void validate(JsonObject application) {
        LLIApplicationValidationService lliApplicationValidationService = getService(LLIApplicationValidationService.class);
        lliApplicationValidationService.validateExistingClient(application);
        if (application.get("connection") == null) {
//            if()
            lliApplicationValidationService.validateConnectionType(application);
            if (application.get("connectionType").isJsonObject()) {
                if (application.get("connectionType").getAsJsonObject().get("ID").getAsInt() == LLIConnectionConstants.CONNECTION_TYPE_TEMPORARY) {
                    lliApplicationValidationService.validatePositiveNumber(application, "duration", "Duration");
                }
            }

            for (JsonElement jsonOfficeElement : application.get("officeList").getAsJsonArray()) {
                ValidationService.validateNonEmptyString(jsonOfficeElement.getAsJsonObject(), "officeName", "Office Name");
                ValidationService.validateNonEmptyString(jsonOfficeElement.getAsJsonObject(), "officeAddress", "Office Address");

            }

            lliApplicationValidationService.validateMandatoryDropdown(application, "loopProvider", "Loop Provider");
            //lliApplicationValidationService.validateMandatoryDropdownZone(application, "zone", "Zone");
            lliApplicationValidationService.validateConnectionType(application);

            lliApplicationValidationService.validatePositiveNumber(application, "suggestedDate", "Suggested Date");


            //todo:check effect for upgrade downgrade: done for close connection purpose
            lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");


            //transmission for cache  check
            if (application.get("connectionType").getAsJsonObject().get("ID").getAsInt() == LLIConnectionConstants.CONNECTION_TYPE_CACHE) {
                double totalExistingBandwidth = 0;
                try {
                    totalExistingBandwidth = ServiceDAOFactory.getService(LLIConnectionService.class).getTotalExistingRegularBWByClientID(application.get("client").getAsJsonObject().get("ID").getAsLong());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                lliApplicationValidationService.validatePositiveNumber(application, "bandwidth", "Bandwidth");

            }

        }
        lliApplicationValidationService.validateExistingClient(application);
        //if client is not govt. employee and try to skip dn
        if (application.get("client").getAsJsonObject().get("registrantType") != null) {
            int skipDN = 0;
            if (application.get("client").getAsJsonObject().get("registrantType").getAsInt() == 1) {

                skipDN = application.get("skipPayment") != null ? application.get("skipPayment").getAsJsonObject().get("ID").getAsInt() : 0;
            }

            if (application.get("client").getAsJsonObject().get("registrantType").getAsInt() != 1 && skipDN == 1) {

                throw new RequestFailureException("You do not have permission to skip Demand Note.");
            }
        }

    }


    @Transactional
    public LLIApplication additional_local_loop(JsonObject jsonObject) throws Exception {

        LLIApplication lliApplication = new LLIApplication();
        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        lliApplication.setPort(jsonObject.get("portCount").getAsInt());
        lliApplication.setLoopProvider(jsonObject.get("loopProvider").getAsJsonObject().get("ID").getAsInt());
        JsonObject officeObject = jsonObject.get("selectedOfficeIndex").getAsJsonObject();
        long oldOfficeId = officeObject.get("ID") != null ? officeObject.get("ID").getAsLong() : 0;
        List<NewOffice> officeList = new ArrayList<>();
        if (oldOfficeId > 0) {
            NewOffice office = new NewOffice();
            office.setOfficeName(jsonObject.get("selectedOfficeIndex").getAsJsonObject().get("object").getAsJsonObject().get("officeName").getAsString());
            office.setOfficeAddress(jsonObject.get("selectedOfficeIndex").getAsJsonObject().get("object").getAsJsonObject().get("officeAddress").getAsString());
            office.setOld_office_id(oldOfficeId);
            officeList.add(office);
        } else {
            JsonArray jsonArray = (JsonArray) jsonObject.get("addedOfficeList");
            for (JsonElement jsonElement1 : jsonArray) {
                JsonObject officeJsonObject = jsonElement1.getAsJsonObject();
                NewOffice office = new NewOffice();
                office.setOld_office_id(oldOfficeId);
                office.setOfficeName(officeJsonObject.get("officeName").getAsString());
                office.setOfficeAddress(officeJsonObject.get("officeAddress").getAsString());
                officeList.add(office);
            }
        }
        lliApplication.setNewOfficeList(officeList);


        lliApplication.setSkipPayment(jsonObject.get("skipPayment") != null ? jsonObject.get("skipPayment").getAsJsonObject().get("ID").getAsInt() : 0);

        lliApplication.setSuggestedDate(jsonObject.get("suggestedDate").getAsLong());
        lliApplication.setApplicationID(jsonObject.get("applicationID") != null ? jsonObject.get("applicationID").getAsLong() : 0);
        lliApplication.setClientID(jsonObject.get("client").getAsJsonObject().get("ID").getAsLong());
        lliApplication.setUserID(jsonObject.get("userID") != null ? jsonObject.get("userID").getAsLong() : null);
        lliApplication.setSubmissionDate(jsonObject.get("submissionDate") != null ? jsonObject.get("submissionDate").getAsLong() : System.currentTimeMillis());
        lliApplication.setContent(jsonObject.get("content") != null ? jsonObject.get("content").getAsString() : "");
        lliApplication.setDemandNoteID(jsonObject.get("demandNoteID") != null ? jsonObject.get("demandNoteID").getAsLong() : null);
        lliApplication.setApplicationType(jsonObject.get("applicationType") != null ? jsonObject.get("applicationType").getAsJsonObject().get("ID").getAsInt() : 0);

        lliApplication.setComment(jsonObject.get("comment") != null ? jsonObject.get("comment").getAsString() : "");
        lliApplication.setDescription(jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : "");
        lliApplication.setRequestForCorrectionComment(jsonObject.get("requestForCorrectionComment") != null ? jsonObject.get("requestForCorrectionComment").getAsString() : "");

        try {
            long connectionID = jsonObject.get("connection") != null ? jsonObject.get("connection").getAsJsonObject().get("ID").getAsLong() : -1;
            LLIConnection flowConnection = lliFlowConnectionService.getConnectionByID(connectionID);
            lliApplication.setZoneId(flowConnection.getZoneID());
            lliApplication.setConnectionType(flowConnection.getConnectionType());
            lliApplication.setConnectionId((int) connectionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lliApplication;
    }
}

package vpn.client.api;

import annotation.Transactional;
import client.temporaryClient.TemporaryClient;
import client.temporaryClient.TemporaryClientConditionBuilder;
import com.google.gson.Gson;
import common.ClientDTO;
import common.CommonActionStatusDTO;
import common.repository.AllClientRepository;
import forgetPassword.ForgetPassword;
import global.GlobalService;
import login.LoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import requestMapping.Service;
import sessionmanager.SessionConstants;
import util.ServiceDAOFactory;
import vpn.client.ClientService;
import vpn.client.ClientUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


/**
 * @author Alam
 */

public class ClientAPI extends DispatchAction {


    @Service
    private GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);

    public Logger logger = Logger.getLogger(getClass());

    public ActionForward sendVerificationMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        LoginDTO loginDTO = (LoginDTO) request.getSession().getAttribute(SessionConstants.USER_LOGIN);

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        if (loginDTO == null) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("User not logged in");

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        String id = request.getParameter("id");
        String email = request.getParameter("email");

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(email)) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("User Id or email is not given");
            out.write(gson.toJson(commonActionStatusDTO));
            return null;
        }

        ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(Long.parseLong(id));

        if (clientDTO == null) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("No user found with id " + id);

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        try {

            ClientService.sendVerificationMail(clientDTO.getLoginName(), email, request);
        } catch (Exception e) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage(e.getMessage());

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.SUCCESS_STATUS_CODE);
        commonActionStatusDTO.setMessage("Verification mail has been sent successfully");
        out.write(gson.toJson(commonActionStatusDTO));

        out.close();

        return null;
    }

    /**
     * This action verify an email address.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @author Alam
     */


    public ActionForward sendVerificationSMS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();

        LoginDTO loginDTO = (LoginDTO) request.getSession().getAttribute(SessionConstants.USER_LOGIN);

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        if (loginDTO == null) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("User not logged in");

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        String id = request.getParameter("id");
        String phoneNo = request.getParameter("phoneNo");

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(phoneNo)) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("User Id or phone no is not given");
            out.write(gson.toJson(commonActionStatusDTO));
            return null;
        }

        phoneNo = phoneNo.replace("+", "").replace("-", "");

        List<ClientDTO> clientDTO = ClientUtility.getByPhoneNumber(phoneNo);
        //ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID( Long.parseLong( id ) );

        if (clientDTO == null) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("No user found with phone no " + phoneNo);

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        try {

            ClientService.sendVerificationSMS(clientDTO.get(0).getLoginName(), phoneNo, request);
        } catch (Exception e) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage(e.getMessage());

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.SUCCESS_STATUS_CODE);
        commonActionStatusDTO.setMessage("Verification token has been sent to your phone");
        out.write(gson.toJson(commonActionStatusDTO));

        out.close();

        return null;
    }


    public ActionForward verifyEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        Gson gson = new Gson();

        String username = request.getParameter("username");
        String token = request.getParameter("token");
        String mailVerifiedForwardPath = "home";

        try {

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {

                commonActionStatusDTO.setErrorMessage("Invalid username or token", false, request);
                //throw new Exception("Invalid username or token");
            }

            ForgetPassword forgetPassword = ForgetPassword.getByUsername(username);

            if (forgetPassword == null) {

                commonActionStatusDTO.setErrorMessage("Invalid email", false, request);
                //throw new Exception("Invalid username or token");
            }

            if (!forgetPassword.getToken().equals(token)) {

                commonActionStatusDTO.setErrorMessage("Invalid token", false, request);
                //throw new Exception("Invalid username or token");
            } else {

                try {

                    ClientUtility.verifyMail(username);

                    logger.debug("Mail verified " + username);
                    String message = username + " is verified successfully!!!";
                    commonActionStatusDTO.setSuccessMessage(message, false, request);

                    updateTemporaryClientStateIfNeeded(username);

                    forgetPassword.remove();

                } catch (Exception e) {

                    logger.fatal(e);
                    commonActionStatusDTO.setErrorMessage(e.toString(), false, request);
                }
            }

        } catch (Exception e) {

            logger.debug("", e);
        } finally {

        }
        if (request.getMethod().equalsIgnoreCase("get")) {

            return mapping.findForward(mailVerifiedForwardPath);
        } else {

            PrintWriter out = response.getWriter();
            out.write(gson.toJson(commonActionStatusDTO));
            out.close();
            return null;
        }
    }

    public ActionForward verifyEmailTemporaryClient(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        Gson gson = new Gson();

        String username = request.getParameter("username");
        String token = request.getParameter("token");
        String clientId = request.getParameter("clientId");


        TemporaryClient client = ServiceDAOFactory.getService(GlobalService.class).findByPK(
                TemporaryClient.class, Long.parseLong(clientId)
        );

        if (client == null) {
            mapping.findForward("404");
        }


        try {

            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {

                commonActionStatusDTO.setErrorMessage("Invalid username or token", false, request);
                //throw new Exception("Invalid username or token");
            }

            ForgetPassword forgetPassword = ForgetPassword.getByUsername(username);

            if (forgetPassword == null) {

                commonActionStatusDTO.setErrorMessage("Invalid email", false, request);
                //throw new Exception("Invalid username or token");
            }

            if (!forgetPassword.getToken().equals(token)) {

                commonActionStatusDTO.setErrorMessage("Invalid token", false, request);
                //throw new Exception("Invalid username or token");
            } else {

                try {

                    ClientUtility.verifyMail(username);
                    updateTemporaryClientStateIfNeeded(username);

                    logger.debug("Mail verified " + username);
                    String message = username + " is verified successfully!!!";
                    commonActionStatusDTO.setSuccessMessage(message, false, request);

                    forgetPassword.remove();

                } catch (Exception e) {

                    logger.fatal(e);
                    commonActionStatusDTO.setErrorMessage(e.toString(), false, request);
                }
            }

        } catch (Exception e) {

            logger.debug("", e);
        } finally {

        }
        if (request.getMethod().equalsIgnoreCase("get")) {

            response.sendRedirect(request.getContextPath() + "/" + "Client/Registration.do?clientId="
                    + clientId + "&redirect=1");
            return null;
        } else {

            PrintWriter out = response.getWriter();
            out.write(gson.toJson(commonActionStatusDTO));
            out.close();
            return null;
        }
    }

    /**
     * This action verify an Phone number.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @author Alam
     */

    public ActionForward isEmailVerified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();
        Gson gson = new Gson();

        Long clientId = Long.valueOf(request.getParameter("id"));

        TemporaryClient client = globalService.findByPK(TemporaryClient.class, clientId);
        commonActionStatusDTO.setSuccessMessage(String.valueOf(client.isEmailVerified()), true, request);

        PrintWriter out = response.getWriter();

        out.write(gson.toJson(commonActionStatusDTO));

        out.close();
        return null;
    }


    //Author:Touhid


    public ActionForward sendVerificationSMSTemporaryClient(ActionMapping mapping, ActionForm form,
                                                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        CommonActionStatusDTO commonActionStatusDTO = new CommonActionStatusDTO();

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String id = request.getParameter("id");
        String phoneNo = request.getParameter("phoneNo");

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(phoneNo)) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage("User Id or phone no is not given");
            out.write(gson.toJson(commonActionStatusDTO));
            return null;
        }

        phoneNo = phoneNo.replace("+", "").replace("-", "");


        try {

            ClientService.sendVerificationSMS("", phoneNo, request);
        } catch (Exception e) {

            commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.ERROR_STATUS_CODE);
            commonActionStatusDTO.setMessage(e.getMessage());

            out.write(gson.toJson(commonActionStatusDTO));
            return null;

        }

        commonActionStatusDTO.setStatusCode(CommonActionStatusDTO.SUCCESS_STATUS_CODE);
        commonActionStatusDTO.setMessage("Verification token has been sent to your phone");
        out.write(gson.toJson(commonActionStatusDTO));

        out.close();

        return null;
    }


    @Transactional
    private void updateTemporaryClientStateIfNeeded(String email) throws Exception {
        List<TemporaryClient> clients = globalService.getAllObjectListByCondition(TemporaryClient.class,
                new TemporaryClientConditionBuilder()
                        .Where()
                        .emailIdEquals(email)
                        .getCondition());

        if (clients.isEmpty())
            return;

        TemporaryClient client = clients.get(0);


        client.setEmailVerified(true);
        globalService.update(client);

    }





}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import login.LoginDTO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sessionmanager.SessionConstants;
import sessionmanager.SessionManager;
import user.UserDTO;
import user.UserRepository;
import user.UserService;

/**
 *
 * @author Md. Kayesh
 */
public class GetUserList extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String target = SUCCESS;
        //System.out.println("whooooa!!");
        List<UserDTO> userList = null;
        LoginDTO loginDTO = (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        SessionManager sessionManager = new SessionManager();
        if (!sessionManager.isLoggedIn(request)) {
            return (mapping.findForward("login"));
        }

        String role = null;
        try {
//                SingleReportForm sForm = (SingleReportForm)form;
//                role = sForm.getRoleSelect();
            role = request.getParameter("role");
            //System.out.println("role selected " + role);
//                UserService uService = new UserService();
//                Hashtable searchValues = new Hashtable();
//                searchValues.put("USERID", role);
            userList =  UserRepository.getInstance().getUserList();

//	    	userList = (ArrayList<String>)uService.getIDsWithSearchCriteria(searchValues, loginDTO);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.flush();
        //System.out.println("size = " + userList.size());

        String str = "";
        str += "<select name = \'adminNameSelect\'> ";
        if (Integer.parseInt(role) == -1) {
            str += "<option value = \'-1\'>" + "All" + "</option>";
            for (int i = 0; i < userList.size(); i++) {
                str += "<option value = \'" + Long.toString(userList.get(i).getUserID()) + "\'>" + userList.get(i).getUsername() + "</option>";
            }
        } else {
            for (int i = 0; i < userList.size(); i++) {
                try {
                    //System.out.println(userList.get(i).getRoleID() + " " + Integer.parseInt(role));
                    ////System.out.println(userList.get(i).getRole() + " " + role);
                    if (userList.get(i).getRoleID() == Integer.parseInt(role)) {
                        str += "<option value = \'" + Long.toString(userList.get(i).getUserID()) + "\'>" + userList.get(i).getUsername() + "</option>";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        str += "</select>";
        //System.out.println("response :"+str);
        out.println(str);


        return mapping.findForward(null);

//        


//        return mapping.findForward(target);
    }
}

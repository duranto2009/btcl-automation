package util;

import common.RequestFailureException;
import login.LoginDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import sessionmanager.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

@SuppressWarnings({"SingleStatementInBlock", "unchecked"})
@AllArgsConstructor
@Log4j
public class RecordNavigationManager {
	String m_navigatorName;
	HttpServletRequest m_request;
	NavigationService m_service;
    String m_dtoCollectionName;
	String m_searchFieldInfo[][];

    public void doJob(LoginDTO loginDTO, Object... objects ) throws Exception {
        KeyValuePair<HttpSession, RecordNavigator> pair = getPairOfSessionAndRecordNavigator();

        doProcessing(pair.key, pair.value, loginDTO, objects);
        doPagination(pair.key, pair.value, loginDTO, false, objects);
    }

    public void doJobCustom(LoginDTO loginDTO, Object... objects ) throws Exception {
        KeyValuePair<HttpSession, RecordNavigator> pair = getPairOfSessionAndRecordNavigator();

        doProcessing(pair.key, pair.value, loginDTO, objects);
        doPagination(pair.key, pair.value, loginDTO, true, objects);
    }

    private KeyValuePair<HttpSession, RecordNavigator> getPairOfSessionAndRecordNavigator() {
        HttpSession session = m_request.getSession();
        RecordNavigator rn = (RecordNavigator)session.getAttribute(m_navigatorName);
        if(rn == null) {
            rn = new RecordNavigator();
        }
        return new KeyValuePair<>(session, rn);

    }
    private void doProcessing(HttpSession session, RecordNavigator rn, LoginDTO loginDTO, Object... objects) throws Exception {
        checkParameter(objects);

        prepareData(session, rn, loginDTO, objects);
    }

    private void prepareData(HttpSession session, RecordNavigator rn, LoginDTO loginDTO, Object... objects) throws Exception {

        String goCheck = m_request.getParameter(SessionConstants.GO_CHECK_FIELD);
        String searchCheck = m_request.getParameter(SessionConstants.SEARCH_CHECK_FIELD);
        String htmlSearchCheck = m_request.getParameter(SessionConstants.HTML_SEARCH_CHECK_FIELD);
        String id = m_request.getParameter(SessionConstants.NAVIGATION_LINK);

        if(id != null){
            fromNavigationLink(id, rn, session);
        }
        if(goCheck != null) {
            fromGoCheckButton(rn, session);
        }
        else if(searchCheck != null) {
            fromSearchCheck(htmlSearchCheck, session, objects, rn, loginDTO);
        }
        else if(id==null){
            fromHyperLink(session, objects,  rn, loginDTO);
        }
    }

    private void doPagination( HttpSession session, RecordNavigator rn, LoginDTO loginDTO, boolean customPagination, Object... objects) throws Exception {
        ArrayList recordIDs;
        int nextCollectionSize;
        if(rn.getTotalRecords() == 0) {
            nextCollectionSize = 0;
        }
        else if(rn.getTotalRecords() > 0 && rn.getCurrentPageNo() == rn.getTotalPages() && rn.getTotalRecords() % rn.getPageSize() != 0) {
            nextCollectionSize = rn.getTotalRecords() % rn.getPageSize();
        }
        else {
            nextCollectionSize = rn.getPageSize();
        }
        int initial = nextCollectionSize != 0 ? (rn.getCurrentPageNo() - 1) * rn.getPageSize() + 1 : 0;
        recordIDs = (ArrayList)rn.getIDs();
        if(customPagination) {
            doPaginationCustom(rn, session, recordIDs, initial, nextCollectionSize, loginDTO, objects);
        }else {
            doPaginationDefault(rn, session, recordIDs, initial, nextCollectionSize, loginDTO , objects);
        }

    }

    private void doPaginationCustom(RecordNavigator rn,
                                    HttpSession session,
                                    ArrayList recordIDs,
                                    int initial,
                                    int nextCollectionSize,
                                    LoginDTO loginDTO,
                                    Object... objects) throws Exception{
        Collection records = new ArrayList();

        if(nextCollectionSize > 0) {
            if(objects.length == 1) {
                records = m_service.getDTOs(recordIDs, objects[0]);
            }
            else {
                records = m_service.getDTOs(recordIDs, loginDTO, initial-1, nextCollectionSize);
            }
        }

        session.setAttribute(m_navigatorName, rn);
        session.setAttribute(m_dtoCollectionName, records);

    }

    private void doPaginationDefault(RecordNavigator rn,
                                     HttpSession session,
                                     ArrayList recordIDs,
                                     int initial,
                                     int nextCollectionSize,
                                     LoginDTO loginDTO,
                                      Object... objects) throws Exception {
        Collection records = new ArrayList();
        Collection recordIDToSend;

        recordIDToSend = new ArrayList();

        log.info("Skipping/ Offset: "+(initial-1) + ", Limiting: " + nextCollectionSize);
        for(int i = initial; i < initial + nextCollectionSize; i++) {
            recordIDToSend.add(recordIDs.get(i - 1));
        }

        if(nextCollectionSize > 0) {
            if(objects.length == 1) {
                records = m_service.getDTOs(recordIDToSend, objects[0]);
            }
            else {
                records = m_service.getDTOs(recordIDToSend, loginDTO);
            }
        }

        session.setAttribute(m_navigatorName, rn);
        session.setAttribute(m_dtoCollectionName, records);
    }



    private void fromHyperLink(HttpSession session, Object[] objects, RecordNavigator rn, LoginDTO loginDTO) throws Exception {
        log.debug("From hyperlink");
        session.setAttribute(m_navigatorName, null);


        rn.setSearchFieldInfo(m_searchFieldInfo);


        ArrayList recordIDs;
        if(objects.length == 1){
            recordIDs = (ArrayList)m_service.getIDs(loginDTO, objects[0]);
        }
        else {
            recordIDs = (ArrayList) m_service.getIDs(loginDTO);
        }

        process(rn, recordIDs);
    }

    private void fromSearchCheck(String htmlSearchCheck,
                                 HttpSession session,
                                 Object[] objects,
                                 RecordNavigator rn,
                                 LoginDTO loginDTO)
            throws Exception {
        if(htmlSearchCheck != null) {
            session.setAttribute(m_navigatorName, null);
            rn = new RecordNavigator();
            rn.setSearchFieldInfo(m_searchFieldInfo);
        }
        log.debug("Search");
        Enumeration paramList = m_request.getParameterNames();
        Hashtable searchValues = new Hashtable();
        while (paramList.hasMoreElements()) {
            String paramName = (String) paramList.nextElement();
            if (
                    !paramName.equalsIgnoreCase(SessionConstants.RECORDS_PER_PAGE)
                            && !paramName.equalsIgnoreCase(SessionConstants.SEARCH_CHECK_FIELD)
                            && !paramName.equalsIgnoreCase(SessionConstants.HTML_SEARCH_CHECK_FIELD)
            ) {
                String paramValue = m_request.getParameter(paramName);
                session.setAttribute(paramName, paramValue);
                searchValues.put(paramName, paramValue);
            }
        }

        log.debug("Search Values (specific search)");
        log.debug(searchValues.toString());
        ArrayList recordIDs;
        if(objects.length == 1) {
            recordIDs = (ArrayList) m_service.getIDsWithSearchCriteria(searchValues, loginDTO, objects[0]);
        }
        else{
            recordIDs = (ArrayList)m_service.getIDsWithSearchCriteria(searchValues, loginDTO);
        }
        int pageSizeInt = -1;
        String pageSize = m_request.getParameter(SessionConstants.RECORDS_PER_PAGE);
        log.debug("Page Size Given : " + pageSize);

        if(pageSize != null) {
            try {
                pageSizeInt = Integer.parseInt(pageSize);
                if(pageSizeInt > 0){
                    rn.setPageSize(pageSizeInt);
                }
            }
            catch(NumberFormatException ex) {
                log.fatal("Next page Size is not number ");
            }
        }

        process(rn, recordIDs);
    }

    private void process(RecordNavigator rn, ArrayList recordIDs) {
        int totalPage = recordIDs.size() / rn.getPageSize();
        if(recordIDs.size() % rn.getPageSize() != 0) {
            totalPage++;
        }
        if(recordIDs.size() > 0) {
            rn.setCurrentPageNo(1);
        }
        else {
            rn.setCurrentPageNo(0);
        }

        rn.setIDs(recordIDs);
        rn.setTotalRecords(recordIDs.size());
        rn.setTotalPages(totalPage);
    }

    private void fromGoCheckButton(RecordNavigator rn, HttpSession session) {
        log.info("Go");
        int tempNumber = Integer.parseInt(m_request.getParameter("pageno"));
        if(tempNumber <= rn.getTotalPages() && tempNumber > 0) {
            rn.setCurrentPageNo(tempNumber);
        }

        Enumeration paramList = m_request.getParameterNames();
        Hashtable searchValues = new Hashtable();
        while (paramList.hasMoreElements()) {
            String paramName = (String) paramList.nextElement();
            if (
                    !paramName.equalsIgnoreCase(SessionConstants.RECORDS_PER_PAGE)
                            && !paramName.equalsIgnoreCase(SessionConstants.SEARCH_CHECK_FIELD)
                            && !paramName.equalsIgnoreCase(SessionConstants.HTML_SEARCH_CHECK_FIELD)
            ) {
                String paramValue = m_request.getParameter(paramName);
                session.setAttribute(paramName, paramValue);
                searchValues.put(paramName, paramValue);
            }
        }
    }

    private void fromNavigationLink(String id, RecordNavigator rn, HttpSession session) {
        log.info("Navigation links");
        int pageno = rn.getCurrentPageNo();
        if(!id.equals(SessionConstants.ADVANCED_SEARCH)) {
            switch (id) {
                case SessionConstants.NAVIGATION_BAR_FIRST:
                    if (rn.getTotalPages() >= 1) {
                        pageno = 1;
                    }
                    break;
                case SessionConstants.NAVIGATION_BAR_NEXT:
                    if (pageno < rn.getTotalPages()) {
                        pageno++;
                    }
                    break;
                case SessionConstants.NAVIGATION_BAR_PREVIOUS:
                    if (pageno > 1) {
                        pageno--;
                    }
                    break;
                case SessionConstants.NAVIGATION_BAR_LAST:
                    pageno = rn.getTotalPages();
                    break;
                case SessionConstants.NAVIGATION_BAR_CURRENT:
                    pageno = rn.getCurrentPageNo();
                    break;
            }

//            Enumeration paramList = session.getAttributeNames();
//            Hashtable searchValues = new Hashtable();
//            while (paramList.hasMoreElements()) {
//                String paramName = (String) paramList.nextElement();
//                if (
//                        !paramName.equalsIgnoreCase(SessionConstants.RECORDS_PER_PAGE)
//                                && !paramName.equalsIgnoreCase(SessionConstants.SEARCH_CHECK_FIELD)
//                                && !paramName.equalsIgnoreCase(SessionConstants.HTML_SEARCH_CHECK_FIELD)
//                ) {
//                    String paramValue =(String) session.getAttribute(paramName);
//                    session.setAttribute(paramName, paramValue);
//                    searchValues.put(paramName, paramValue);
//                }
//            }
        }
        rn.setCurrentPageNo(pageno);
    }

    private void checkParameter(Object[] objects) {
        log.info("Record Navigation" + objects.length);
        if(objects.length > 1) {
            throw new RequestFailureException("At most 1 object as parameter is allowed for search page along with loginDTO");
        }
    }


}
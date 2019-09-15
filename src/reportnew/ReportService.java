package reportnew;

import annotation.Transactional;
import common.ModuleConstants;
import common.RequestFailureException;
import connection.DatabaseConnection;
import login.LoginDTO;
import report.ReportHelper;
import report.ReportMetadata;
import report.ReportProcessor;
import requestMapping.Service;
import util.DatabaseConnectionFactory;
import util.TransactionType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class ReportService {

    @Service private LLIReportService lliReportService;
    @Service private NIXReportService nixReportService;
    @Service private CoLocationReportService coLocationReportService;
    @Service private UpStreamReportService upStreamReportService;
    @Service private VPNReportService vpnReportService;

    @Transactional(transactionType = TransactionType.READONLY)
    Report getReportBatchOperationApplication(PostEntity postEntity, int module, LoginDTO loginDTO)throws Exception {
        if(module ==ModuleConstants.Module_ID_LLI){
            return lliReportService.getReportBatchOperationForlliApplication(postEntity,loginDTO);
        }
        else if( module == ModuleConstants.Module_ID_VPN){
            return vpnReportService.getReportBatchOperationVPNApplication(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_NIX){
            return nixReportService.getReportBatchOperationForNIXApplication(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_COLOCATION){
            return coLocationReportService.getReportBatchOperationForCoLocationApplication(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_UPSTREAM){
            return upStreamReportService.getReportBatchOperationForUpStreamApplication(postEntity,loginDTO);
        }
        else {
            throw new RequestFailureException("No Data found for this module");
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    Report getReportBatchOperationConnection(PostEntity postEntity, int module, LoginDTO loginDTO)throws Exception {
        if(module ==ModuleConstants.Module_ID_LLI){
            return lliReportService.getReportBatchOperationForlliConnection(postEntity,loginDTO);
        }
        else if( module == ModuleConstants.Module_ID_VPN){
            return vpnReportService.getReportBatchOperationForNetworkLink(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_NIX){
            return nixReportService.getReportBatchOperationFornixConnection(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_COLOCATION){
            return coLocationReportService.getReportBatchOperationForCoLocationConnection(postEntity,loginDTO);
        }
        else if(module == ModuleConstants.Module_ID_UPSTREAM){
            return upStreamReportService.getReportBatchOperationForUpStreamConnection(postEntity,loginDTO);
        }
        else {
            throw new RequestFailureException("No Data found for this module");
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForBill(Integer moduleID, HttpServletRequest request, int limit,
                                                    int offset) throws Exception {
        return getResultSet(ReportHelper.getBillReportHelperByModuleID(moduleID).classMap,
                ReportHelper.getBillReportHelperByModuleID(moduleID).tableJoinSql, request, limit, offset);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForClient(Integer moduleID,HttpServletRequest request, int limit, int offset)
            throws Exception {
        return getResultSet(ReportHelper.getClientReportHelperByModuleID(moduleID).classMap,
                ReportHelper.getClientReportHelperByModuleID(moduleID).tableJoinSql, request, limit, offset);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForDomain(HttpServletRequest request, int limit, int offset)
            throws Exception {
        return getResultSet(ReportHelper.getDomainReportHelper().classMap,
                ReportHelper.getDomainReportHelper().tableJoinSql, request, limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForVPN(HttpServletRequest request, int limit, int offset) throws Exception {
        return getResultSet(ReportHelper.getVPNReportHelper().classMap, ReportHelper.getVPNReportHelper().tableJoinSql,
                request, limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForLLI(HttpServletRequest request, int limit, int offset) throws Exception {
        return getResultSet(ReportHelper.getLLIReportHelper().classMap, ReportHelper.getLLIReportHelper().tableJoinSql,
                request, limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForColocation(HttpServletRequest request, Integer limit,
                                                   int offset) throws Exception {
        return getResultSet(ReportHelper.getColocationReportHelper().classMap, ReportHelper.getColocationReportHelper().tableJoinSql,
                request, limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForCRM(HttpServletRequest request, int limit, int offset) throws Exception {
        return getResultSet(ReportHelper.getCRMReportHelper().classMap, ReportHelper.getCRMReportHelper().tableJoinSql,
                request, limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForLog(HttpServletRequest request, int limit, int offset) throws Exception {
        return getResultSet(ReportHelper.getLogHelper().classMap, ReportHelper.getLogHelper().tableJoinSql, request,
                limit, offset);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> generateReportForPayment(HttpServletRequest request, Integer limit, int offset) throws Exception {
        return getResultSet(ReportHelper.getPaymentHelper().classMap, ReportHelper.getPaymentHelper().tableJoinSql, request,
                limit, offset);
    }



    @Transactional(transactionType = TransactionType.READONLY)
    List<List<Object>> getResultSet(Map<String, Class> classMap, String tableJoinSql, HttpServletRequest request,
                                           int limit, int offset) throws Exception {
        ReportMetadata reportMetadata = new ReportMetadata(classMap, request);
        return ReportProcessor.getResult(reportMetadata,
                tableJoinSql, DatabaseConnectionFactory.getCurrentDatabaseConnection(), limit, offset);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    Integer getBillTotalReportCount(Integer moduleID, HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getBillReportHelperByModuleID(moduleID).classMap, ReportHelper.getBillReportHelperByModuleID(moduleID).tableJoinSql, request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    Integer getTotalCount(Map<String, Class> classMap, String tableJoinSql, HttpServletRequest request) throws Exception {
        ReportMetadata reportMetadata = new ReportMetadata(classMap, request);
        return ReportProcessor.getTotalResultCount(reportMetadata, tableJoinSql, DatabaseConnectionFactory.getCurrentDatabaseConnection());

    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForDomain(HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getDomainReportHelper().classMap,
                ReportHelper.getDomainReportHelper().tableJoinSql, request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForVPN(HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getVPNReportHelper().classMap, ReportHelper.getVPNReportHelper().tableJoinSql,
                request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForForLLI(HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getLLIReportHelper().classMap, ReportHelper.getLLIReportHelper().tableJoinSql,
                request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForForColocation(HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getColocationReportHelper().classMap, ReportHelper.getColocationReportHelper().tableJoinSql,
                request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForCRM(HttpServletRequest request) throws Exception {
        return getTotalCount(ReportHelper.getCRMReportHelper().classMap, ReportHelper.getCRMReportHelper().tableJoinSql,
                request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForLog(HttpServletRequest request) throws Exception {

        return getTotalCount(ReportHelper.getLogHelper().classMap, ReportHelper.getLogHelper().tableJoinSql, request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getTotalReportCountForPayment(HttpServletRequest request) throws Exception {

        return getTotalCount(ReportHelper.getPaymentHelper().classMap, ReportHelper.getPaymentHelper().tableJoinSql, request);
    }
    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getClientTotalReportCount(Integer moduleID,HttpServletRequest request) throws Exception {

        return getTotalCount(ReportHelper.getClientReportHelperByModuleID(moduleID).classMap, ReportHelper.getClientReportHelperByModuleID(moduleID).tableJoinSql, request);
    }
}

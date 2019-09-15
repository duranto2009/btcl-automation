/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.sql.*;
import databasemanager.*;
import java.util.Iterator;
import util.TimeFormat;

/**
 *
 * @author reve
 */
public class CDRTableHandler {

    static Logger logger = Logger.getLogger(CDRTableHandler.class);
    public static final long REFRESH_INTERVAL = 10 * 1000 + (int) (Math.random() * 1000);
    private long loadingTime;
    private static final String sql_all_cdr_tbl_names = "select distinct table_name from cols where table_name like 'ADCDR_%'";
    //private static final String sql_get_bill_cycle="select DISTINCT BILLCYCLESTART, BILLCYCLEEND from ADESSENTIALS";
    //String sql_create_new_cdr_table = "CREATE TABLE ADCDR_"+table_name_postfix+"(  CDRID NUMBER(18, 0) DEFAULT 0 NOT NULL , CDRCLIENTID NUMBER(18, 0) DEFAULT NULL , CDRMACADDRESS VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMNAME VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMPORT NUMBER(9, 0) DEFAULT NULL , CDRIPADDRESS NUMBER(11, 0) DEFAULT NULL , CDRCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRDISCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRCALLERID VARCHAR2(13 BYTE) DEFAULT NULL , CDRINBYTES NUMBER(12, 0) DEFAULT NULL , CDROUTBYTES NUMBER(12, 0) DEFAULT NULL , CDRTARIFFID NUMBER(18, 0) DEFAULT NULL , CDRBILLAMOUNT NUMBER(10, 4) DEFAULT NULL , CDRLOGINSTATUS VARCHAR2(40 BYTE) DEFAULT NULL , CONSTRAINT ADCDR_"+table_name_postfix+"CONSTRAINT PRIMARY KEY   (    CDRID   )  ENABLE )";
    private static final String sql_create_new_cdr_table_1 = "CREATE TABLE ADCDR_";
    private static final String sql_create_new_cdr_table_2 = "(  CDRID NUMBER(18, 0) DEFAULT 0 NOT NULL , CDRCLIENTID NUMBER(18, 0) DEFAULT NULL , CDRMACADDRESS VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMNAME VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMPORT NUMBER(9, 0) DEFAULT NULL , CDRIPADDRESS NUMBER(11, 0) DEFAULT NULL , CDRCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRDISCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRCALLERID VARCHAR2(13 BYTE) DEFAULT NULL , CDRINBYTES NUMBER(12, 0) DEFAULT NULL , CDROUTBYTES NUMBER(12, 0) DEFAULT NULL , CDRTARIFFID NUMBER(18, 0) DEFAULT NULL , CDRBILLAMOUNT NUMBER(10, 4) DEFAULT NULL , CDRLOGINSTATUS VARCHAR2(40 BYTE) DEFAULT NULL , CONSTRAINT ADCDR_";
    private static final String sql_create_new_cdr_table_3 = "CONSTRAINT PRIMARY KEY   (    CDRID   )  ENABLE )";
    private static final String[] mm_dd_list_start = new String[4];
    private static final String[] mm_dd_list_end = new String[4];
    private static final int mm_dd_01 = 0;
    private static final int mm_dd_04 = 1;
    private static final int mm_dd_07 = 2;
    private static final int mm_dd_10 = 3;
    static CDRTableHandler cdr_table_handler = null;
    private String currentCDRTableName;
    private String currentCDRTableName_date_postfix;
    ArrayList<String> table_list;

    private CDRTableHandler() {
        mm_dd_list_start[mm_dd_01] = "0101";
        mm_dd_list_start[mm_dd_04] = "0401";
        mm_dd_list_start[mm_dd_07] = "0701";
        mm_dd_list_start[mm_dd_10] = "1001";

        mm_dd_list_end[mm_dd_01] = "0331";
        mm_dd_list_end[mm_dd_04] = "0630";
        mm_dd_list_end[mm_dd_07] = "0930";
        mm_dd_list_end[mm_dd_10] = "1231";


        //currentCDRTableName=getLastCreatedCDRTableName();
        forceReload();
        /*
        if(currentCDRTableName==null || currentCDRTableName.isEmpty()){
        manageCDRTableCreation();
        }
         * 
         */
        //currentCDRTableName = getLastCreatedCDRTableName();
        /*
        if(currentCDRTableName.equals("")){
            currentCDRTableName_date_postfix="";            
            //forceReload();
        }
         * 
         */
        createFirstCDRTableIfNeeded();
    }

    public static CDRTableHandler getInstance() {
        if (cdr_table_handler == null) {
            createCDRTableHandler();
        }
        return cdr_table_handler;
    }

    private synchronized static void createCDRTableHandler() {
        if (cdr_table_handler == null) {
            cdr_table_handler = new CDRTableHandler();
        }
    }

    private void checkForReload() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - loadingTime > REFRESH_INTERVAL) {
            loadingTime = currentTime;
            reload();
        }
    }

    public synchronized void forceReload() {
        loadingTime = System.currentTimeMillis();
        reload();
    }

    private void manageCDRTableCreation() {
        if (willCreateNewCDRTable()) {
            createNewCDRTable(getNextCDRTableNameDatePostfix(currentCDRTableName_date_postfix));
            forceReload();
        }

    }

    public synchronized String getCurrentCDRTableName() {
        manageCDRTableCreation();
        return this.currentCDRTableName;
    }

    private void setCurrentCDRTableName(String table_name) {
        this.currentCDRTableName = table_name;        
        if (!currentCDRTableName.equals("")) {
            currentCDRTableName_date_postfix = currentCDRTableName.substring(6);
        }else{
            currentCDRTableName_date_postfix="";
        }
    }

    private boolean isIncludable(String date_yyymmdd, long start_date, long end_date) {
        //if((TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date)>=TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(start_date))&&(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date)<=TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(end_date)))
        // return true;
        long date = TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date_yyymmdd);
        
        String start_slot= getCDRTableNamePostfixFromAnyDate(TimeFormat.getBillFileDate(start_date));
        //String end_slot=getEndDateFromStartDate(getCDRTableNamePostfixFromAnyDate(TimeFormat.getBillFileDate(end_date)));
        
        ////System.out.println(date+" "+start_slot+", "+end_slot);
        //TimeFormat.showTimeFromMili(date);
        //TimeFormat.showTimeFromMili(TimeFormat.getStartTimeOfDayInMilis(start_date));
        //TimeFormat.showTimeFromMili(TimeFormat.getEndTimeOfDayInMilis(end_date));
        //TimeFormat.showTimeFromMili(TimeFormat.getStartTimeOfDayInMilis(start_slot));
        //TimeFormat.showTimeFromMili(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(start_slot));
        
        if ((date >= TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(start_slot)) && (date <= end_date)) {
            return true;
        }
        return false;
    }
    
    
    private boolean isIncludable(String date_yyymmdd, String start_date_yyyymmdd, String end_date_yyyymmdd) {
        //if((TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date)>=TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(start_date))&&(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date)<=TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(end_date)))
        // return true;
        long date = TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(date_yyymmdd);
        if ((date >= TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(start_date_yyyymmdd))
                && (date <= TimeFormat.getEndTimeOfDayInMilis(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(end_date_yyyymmdd)))) {
            return true;
        }

        return false;
    }
    //private boolean 

    private boolean willCreateNewCDRTable() {
        long curr_time = System.currentTimeMillis();
        if ((curr_time >= TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(currentCDRTableName_date_postfix))
                && (curr_time <= TimeFormat.getEndTimeOfDayInMilis(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(getEndDateFromStartDate(currentCDRTableName_date_postfix))))) {
            return false;
        }
        return true;
    }

    private String getEndDateFromStartDate(String startDate) {

        String mm_dd = startDate.substring(4);
        if (mm_dd.equals(mm_dd_list_start[mm_dd_01])) {
            return startDate.substring(0, 4) + mm_dd_list_end[mm_dd_01];
        } else if (mm_dd.equals(mm_dd_list_start[mm_dd_04])) {
            return startDate.substring(0, 4) + mm_dd_list_end[mm_dd_04];
        } else if (mm_dd.equals(mm_dd_list_start[mm_dd_07])) {
            return startDate.substring(0, 4) + mm_dd_list_end[mm_dd_07];
        } else {
            return startDate.substring(0, 4) + mm_dd_list_end[mm_dd_10];
        }
    }

    private String getNextCDRTableNameDatePostfix(String startDate) {
        
        String year=TimeFormat.getBillFileDate().substring(0,4);
        
        String mm_dd = startDate.substring(4);
        if (mm_dd.equals(mm_dd_list_start[mm_dd_01])) {
            return year + mm_dd_list_start[mm_dd_04];
        } else if (mm_dd.equals(mm_dd_list_start[mm_dd_04])) {
            return year + mm_dd_list_start[mm_dd_07];
        } else if (mm_dd.equals(mm_dd_list_start[mm_dd_07])) {
            return year + mm_dd_list_start[mm_dd_10];
        } else {
            return year + mm_dd_list_start[mm_dd_01];
        }
    }

    private void createFirstCDRTableIfNeeded() {
        String curr_date = TimeFormat.getBillFileDate();
        ////System.out.println("Current date:"+curr_date);
        String calculated_curr_cdr_table_date_postfix = getCDRTableNamePostfixFromAnyDate(curr_date);
        
        ////System.out.println("Estimated table name postfix:"+calculated_curr_cdr_table_date_postfix);
        
        if (!calculated_curr_cdr_table_date_postfix.equals(currentCDRTableName_date_postfix)) {
            createNewCDRTable(calculated_curr_cdr_table_date_postfix);
            forceReload();
        }
    }

    private String getCDRTableNamePostfixFromAnyDate(String date_yyyymmdd) {

        String year = date_yyyymmdd.substring(0, 4);
        ////System.out.println("Year="+year);
        if (isIncludable(date_yyyymmdd, year + mm_dd_list_start[mm_dd_01], year + mm_dd_list_end[mm_dd_01])) {
            return year + mm_dd_list_start[mm_dd_01];
        } else if (isIncludable(date_yyyymmdd, year + mm_dd_list_start[mm_dd_04], year + mm_dd_list_end[mm_dd_04])) {
            return year + mm_dd_list_start[mm_dd_04];
        } else if (isIncludable(date_yyyymmdd, year + mm_dd_list_start[mm_dd_07], year + mm_dd_list_end[mm_dd_07])) {
            return year + mm_dd_list_start[mm_dd_07];
        } else {
            return year + mm_dd_list_start[mm_dd_10];
        }

    }

    private void reload() {
        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        table_list = new ArrayList<String>();

        try {
            connection = DatabaseManager.getInstance().getConnection();
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql_all_cdr_tbl_names);
            
            while (resultSet.next()) {
                ////System.out.println("Table name:"+resultSet.getString("table_name"));
                table_list.add(resultSet.getString("table_name"));
            }
            resultSet.close();
        } catch (Exception ex) {
            logger.fatal("Error while retrieving cdr table name" + ex);
            //System.out.println("Error while retrieving cdr table name" + ex);

        } finally {
            try {
                stmt.close();
            } catch (Throwable th) {
            }
            try {
                DatabaseManager.getInstance().freeConnection(connection);
            } catch (Throwable th) {
            }
            ////System.out.println("Total no of tables:"+table_list.size());
            setCurrentCDRTableName(getLastCreatedCDRTableName());
            /*
            if(currentCDRTableName!=""){
                currentCDRTableName_date_postfix=currentCDRTableName.substring(6);
            }
             * 
             */
        }
    }

    public synchronized ArrayList<String> getAllCDRTables(long start_date, long end_date) {

        checkForReload();
        Iterator table_list_iterator = table_list.iterator();
        ArrayList<String> filtered_table_list = new ArrayList<String>();

        while (table_list_iterator.hasNext()) {
            //String curr_table= (String) table_list_iterator.next(); 
            String table_name = (String) table_list_iterator.next();
            ////System.out.println("OK:"+table_name);
            String date_postfix = table_name.substring(6);
            ////System.out.println("OK1:"+date_postfix);
            if (isIncludable(date_postfix, start_date, end_date)) {
                ////System.out.println("OK2:"+table_name);
                filtered_table_list.add(table_name);                
            }
        }
        return filtered_table_list;
        /*
        if ((table_list == null) || (table_list.isEmpty())) {
        return null;
        } else {
        
        }
         */
    }

    private String getLastCreatedCDRTableName() {

        checkForReload();
        Iterator table_list_iterator = table_list.iterator();
        
        String last_table = "";//(String) table_list_iterator.next();
        if(table_list_iterator.hasNext()){
            last_table = (String) table_list_iterator.next();
        }
        while (table_list_iterator.hasNext()) {
            String curr_table = (String) table_list_iterator.next();
            if (TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(curr_table.substring(6)) > TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(last_table.substring(6))) {
                ////System.out.println(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(curr_table.substring(6))+"="+curr_table.substring(6));
                ////System.out.println(TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(last_table.substring(6))+"="+last_table.substring(6));
                last_table = curr_table;
            }
        }
        ////System.out.println("Last table name from getLastCreatedCDRTableName function:"+last_table);
        return last_table;

        /*
        if ((table_list == null) || (table_list.isEmpty())) {
        return null;
        } else {
        Iterator table_list_iterator = table_list.iterator();
        String last_table = (String) table_list_iterator.next();
        while (table_list_iterator.hasNext()) {
        String curr_table = (String) table_list_iterator.next();
        if (TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(curr_table.substring(6)) > TimeFormat.getStartTimeOfDayInMilisFromBillFileDate(last_table.substring(6))) {
        last_table = curr_table;
        }
        }
        return last_table;
        }
         * 
         */
    }

    //private boolean 
    private void createNewCDRTable(String table_name_postfix) {
        String sql_create_new_cdr_table = sql_create_new_cdr_table_1+table_name_postfix+sql_create_new_cdr_table_2+table_name_postfix+sql_create_new_cdr_table_3;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseManager.getInstance().getConnection();
            connection.setAutoCommit(true);
            ps = connection.prepareStatement(sql_create_new_cdr_table);

            //ps.setString(1, table_name_postfix);
            //ps.setString(2, table_name_postfix);

            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error("Exception thrown while creating new cdr table with table postfix:" + table_name_postfix + ". " + ex);
            //System.out.println("Exception thrown while creating new cdr table with table postfix:" + table_name_postfix + ". " + ex);

        } finally {
            try {
                ps.close();
            } catch (Throwable th) {
            }
            try {
                DatabaseManager.getInstance().freeConnection(connection);
            } catch (Throwable th) {
            }
        }
    }
}

/*
CREATE TABLE ADCDR_TEST(  CDRID NUMBER(18, 0) DEFAULT 0 NOT NULL , CDRCLIENTID NUMBER(18, 0) DEFAULT NULL , CDRMACADDRESS VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMNAME VARCHAR2(20 BYTE) DEFAULT NULL , CDRDSLMPORT NUMBER(9, 0) DEFAULT NULL , CDRIPADDRESS NUMBER(11, 0) DEFAULT NULL , CDRCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRDISCONNECTTIME NUMBER(18, 0) DEFAULT NULL , CDRCALLERID VARCHAR2(13 BYTE) DEFAULT NULL , CDRINBYTES NUMBER(12, 0) DEFAULT NULL , CDROUTBYTES NUMBER(12, 0) DEFAULT NULL , CDRTARIFFID NUMBER(18, 0) DEFAULT NULL , CDRBILLAMOUNT NUMBER(10, 4) DEFAULT NULL , CDRLOGINSTATUS VARCHAR2(40 BYTE) DEFAULT NULL , CONSTRAINT ADCDR_TEST_CONSTRAINT PRIMARY KEY   (    CDRID   )  ENABLE );
 * 
 */
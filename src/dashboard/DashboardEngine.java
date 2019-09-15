package dashboard;

import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.KeyValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j
public class DashboardEngine {

    @Service private DashboardService dashboardService;
    @Service private DashboardServiceClient dashboardServiceClient;
    @Service private DashboardServiceCDGM dashboardServiceCDGM;
    @Service private DashboardServiceServerRoom dashboardServiceServerRoom;

    public KeyValuePair<List<DashboardCounterCard>, List<DashboardMainFrame>> getData(LoginDTO loginDTO) throws Exception {

        // TODO configure this in DB

        long roleID = loginDTO.getRoleID();
        if(roleID == 22021) { // CDGM
            return getCDGMRoleData();
        }else if(roleID == -1) { // client
            return getClientRoleData(loginDTO.getAccountID());
        }else if(roleID == 16021) { // SR
            return getServerRoomRoleData();
        }
        else {
            // default
            return getCDGMRoleData();
        }

    }

    private KeyValuePair<List<DashboardCounterCard>, List<DashboardMainFrame>> getCDGMRoleData() throws Exception {
        List<DashboardCounterCard> cards = Arrays.asList(
                DashboardCounterCard.getDashboardCounterCard(
                        dashboardServiceCDGM.getActiveClientData(),
                        DashboardConstants.TextClass.GREEN_SHARP.getTextClass(), "", "icon-user", ChartType.StackedBarVertical
                ),
                DashboardCounterCard.getDashboardCounterCard(
                        dashboardServiceCDGM.getDemandNoteData(),
                        DashboardConstants.TextClass.RED_HAZE.getTextClass(), " &#2547;", "fa fa-money", ChartType.BrokenPie
                ),
                DashboardCounterCard.getDashboardCounterCard(
                        dashboardServiceCDGM.getOutsourceBillData(),
                        DashboardConstants.TextClass.BLUE_SHARP.getTextClass(), " &#2547;", "icon-wallet", ChartType.VariableRadiusPie
                ),
                DashboardCounterCard.getDashboardCounterCard(
                        dashboardServiceCDGM.getActiveUsersData(),
                        DashboardConstants.TextClass.PURPLE_SOFT.getTextClass(), "", "icon-users", ChartType.SimpleColumn
                )

        );

        List<DashboardMainFrame> mainFrames = Arrays.asList(
                DashboardMainFrame.getMainFrame(dashboardServiceCDGM.getApplicationData(), "Application", "chart-div-left", ChartType.LayeredColumn),
                DashboardMainFrame.getMainFrame(dashboardServiceCDGM.getEntityDataForCDGM(), "Connection/Link", "chart-div-right", ChartType.SimplePie)
        );

        return new KeyValuePair<>(cards, mainFrames);

    }


    private KeyValuePair<List<DashboardCounterCard>, List<DashboardMainFrame>> getClientRoleData(long clientId) throws Exception {
        List<DashboardCounterCard> cards = new ArrayList<>(dashboardServiceClient.getEntityDataForClient(clientId));
        cards.add(
            DashboardCounterCard.getDashboardCounterCard(
                dashboardServiceClient.getCRMTicketDataForClient(clientId),
                    DashboardConstants.TextClass.BLUE_SHARP.getTextClass(), "", "icon-wallet", ChartType.VariableRadiusPie
            )
        );
        cards.add(
            DashboardCounterCard.getDashboardCounterCard(
                dashboardServiceClient.getLastPaymentDateDataForClient(clientId),
                    DashboardConstants.TextClass.PURPLE_SOFT.getTextClass(), "", "icon-users", ChartType.TimeLine
            )
        );

        List<DashboardMainFrame> mainFrames = Arrays.asList(
                DashboardMainFrame.getMainFrame(
                        dashboardServiceClient.getApplicationDataForClient(clientId), "Application", "chart-div-left", ChartType.SimplePie
                ),
                DashboardMainFrame.getMainFrame(
                        dashboardServiceClient.getTotalBillDataForClientForceDirected(clientId), "Bills", "chart-div-right", ChartType.CollapsibleForceDirectedTree
                )
        );

        return new KeyValuePair<>(cards, mainFrames);
    }

    private KeyValuePair<List<DashboardCounterCard>, List<DashboardMainFrame>> getServerRoomRoleData() throws Exception {

        KeyValuePair<DashboardMainFrame, List<DashboardCounterCard>> pair = dashboardServiceServerRoom.getInventoryDashboardCounterCards();
        List<DashboardCounterCard> cards = new ArrayList<>();
        cards.add(dashboardServiceServerRoom.getBWUsageData());
        cards.addAll(pair.getValue());


        List<DashboardMainFrame> mainFrames = Arrays.asList(

                DashboardMainFrame.getMainFrame(
                        dashboardServiceServerRoom.getApplicationPhaseData(), "Application Phase", "chart-div-left", ChartType.BrokenPie
                ),
                pair.getKey()

        );
        return new KeyValuePair<>(cards, mainFrames);
    }
}

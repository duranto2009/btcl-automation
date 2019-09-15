package notification;

import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.List;


@ActionRequestMapping("notification/")
public class NotificationAction extends AnnotatedRequestMappingAction {

    @Service
    NotificationService notificationService;

    @RequestMapping(mapping = "get-user-notification", requestMethod = RequestMethod.All)
    public List<NotificationDTO> getNotificationByRoleId(@RequestParameter("id") long roleId, @RequestParameter("accountId") long accountId, LoginDTO loginDTO) throws Exception {

        List<NotificationDTO> notificationDTO;
        if(roleId>0){
             notificationDTO = notificationService.getNotificationListByRoleId(roleId,loginDTO);
        }else{
            notificationDTO = notificationService.getNotificationListByRoleId(accountId,loginDTO);
        }



        return notificationDTO;
    }

    @RequestMapping(mapping = "update-notification-as-seen", requestMethod = RequestMethod.All)
    public void updateAsSeen(@RequestParameter("notification") NotificationDTO notification) throws Exception {

        notificationService.updateNotificationAsSeen(notification);

    }


}

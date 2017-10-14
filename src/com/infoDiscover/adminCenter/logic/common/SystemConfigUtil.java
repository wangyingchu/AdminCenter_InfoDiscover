package com.infoDiscover.adminCenter.logic.common;

import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;

public class SystemConfigUtil {

    public static boolean verifyUserLoginInfo(String userName,String userPWD){
        String adminAccountName=AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTNAME);
        String adminAccountPWD=AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.BUILDIN_ADMINISTRATOR_ACCOUNTPWD);
        if(adminAccountName==null||adminAccountPWD==null){
            return true;
        }else{
            if(adminAccountName.equals(userName)&&adminAccountPWD.equals(userPWD)){
                return true;
            }else{
                return false;
            }
        }
    }
}

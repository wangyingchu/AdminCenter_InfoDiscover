package com.infoDiscover.adminCenter.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by wangychu on 2/21/17.
 */
public class AdminCenterPropertyHandler {
    private static Properties _properties;
    public static String INFO_ANALYSE_SERVICE_ROOT_LOCATION="INFO_ANALYSE_SERVICE_ROOT_LOCATION";
    private static String web_inf_Path=AdminCenterPropertyHandler.class.getResource("/").getPath();

    public static String getPropertyValue(String resourceFileName){
        _properties=new Properties();
        try {
            _properties.load(new FileInputStream(web_inf_Path+"AdminCenterCfg.properties"));
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }
        return _properties.getProperty(resourceFileName);
    }
}

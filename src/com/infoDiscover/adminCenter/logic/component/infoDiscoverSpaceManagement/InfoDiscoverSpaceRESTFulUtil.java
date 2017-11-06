package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement;


import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import org.apache.cxf.jaxrs.client.WebClient;

public class InfoDiscoverSpaceRESTFulUtil {

    private static String dataAnalyzeApplicationBaseAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"ws/";

    public static void refreshDataAnalyzeApplicationDiscoverSpaceMetaInfo(String discoverSpaceName){
        WebClient client = WebClient.create(dataAnalyzeApplicationBaseAddress);
        client.path("systemManagementService/refreshDiscoverSpaceDataMetaInfo/"+discoverSpaceName+"/");
        client.type("application/xml").accept("application/xml");
        client.get();
    }
}

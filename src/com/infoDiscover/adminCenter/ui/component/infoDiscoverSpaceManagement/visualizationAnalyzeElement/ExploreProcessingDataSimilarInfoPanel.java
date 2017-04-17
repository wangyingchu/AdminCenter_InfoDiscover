package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 4/17/17.
 */
public class ExploreProcessingDataSimilarInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private ProcessingDataVO processingData;
    private String typeInstanceRelationsDetailGraphQueryAddress;

    public ExploreProcessingDataSimilarInfoPanel(UserClientInfo userClientInfo,ProcessingDataVO processingData){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        this.processingData=processingData;
        this.setWidth(100,Unit.PERCENTAGE);
        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
    }

    public ProcessingDataVO getProcessingData(){
        return this.processingData;
    }
}

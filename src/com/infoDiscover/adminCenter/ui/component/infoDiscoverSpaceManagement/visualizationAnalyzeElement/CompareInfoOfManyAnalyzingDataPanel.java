package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 4/13/17.
 */
public class CompareInfoOfManyAnalyzingDataPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public CompareInfoOfManyAnalyzingDataPanel(UserClientInfo userClientInfo){
        this.setMargin(true);
        this.setWidth(100,Unit.PERCENTAGE);
        this.currentUserClientInfo = userClientInfo;
        Label xx=new Label("CompareInfoOfManyAnalyzingDataPanel");
        this.addComponent(xx);
    }
}

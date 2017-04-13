package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement
        .InfoDiscoverSpacesDetailPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by sun.
 */
public class RuleEngineManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;

    public RuleEngineManagementPanel(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;

        int screenHeight = this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        String windowsHeight = "" + (screenHeight - 230) + "px";
        this.setHeight(windowsHeight);

        HorizontalSplitPanel ruleEngineManagementSplitPanel = new HorizontalSplitPanel();
        ruleEngineManagementSplitPanel.setSizeFull();
        ruleEngineManagementSplitPanel.setSplitPosition(200, Unit.PIXELS);

        this.addComponent(ruleEngineManagementSplitPanel);
        this.setExpandRatio(ruleEngineManagementSplitPanel, 1.0F);

        RuleEngineBrowser ruleEngineBrowser = new RuleEngineBrowser(this.currentUserClientInfo);
        ruleEngineManagementSplitPanel.setFirstComponent(ruleEngineBrowser);

        RulesDetailPanel rulesDetailPanel = new
                RulesDetailPanel(this.currentUserClientInfo);
        ruleEngineManagementSplitPanel.setSecondComponent(rulesDetailPanel);
    }
}

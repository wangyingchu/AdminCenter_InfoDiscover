package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.ui.component.common.ElementStatusBar;
import com.infoDiscover.adminCenter.ui.component.common.GeneralInfoView;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event
        .RuleEngineComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by sun.
 */
public class RulesDetailPanel extends VerticalLayout implements RuleEngineComponentSelectedEvent.RuleEngineComponentSelectedListener,
        RuleEngineDeletedEvent.RuleEngineDeletedListener{

    private UserClientInfo currentUserClientInfo;
    private ElementStatusBar elementStatusBar;
    private Navigator contentNavigator;
    private static final String NAV_GENERAL="general_idm";
    private static final String NAV_DISCOVERSPACE_DETAIL="ds_detail";
    private  RuleDetail ruleDetail;

    public RulesDetailPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.elementStatusBar=new ElementStatusBar();
        this.addComponent(elementStatusBar);

        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.setMargin(false);
        contentContainer.setSpacing(false);
        contentContainer.setSizeFull();
        this.addComponent(contentContainer);

        ComponentContainer componentContainer=(ComponentContainer)contentContainer;
        contentNavigator = new Navigator(UI.getCurrent(),componentContainer);
        /* Config Components View */
        GeneralInfoView generalInfoView=new GeneralInfoView(currentUserClientInfo);
        contentNavigator.addView(NAV_GENERAL, generalInfoView);
        this.ruleDetail =new RuleDetail(currentUserClientInfo);
        contentNavigator.addView(NAV_DISCOVERSPACE_DETAIL, ruleDetail);

        contentNavigator.navigateTo(NAV_GENERAL);
    }

    @Override
    public void receivedRuleEngineDeletedEvent(RuleEngineDeletedEvent event) {
        this.elementStatusBar.clearRuleName();
        contentNavigator.navigateTo(NAV_GENERAL);
    }

    @Override
    public void receivedRuleEngineComponentSelectedEvent(RuleEngineComponentSelectedEvent
                                                                             event) {
        String ruleId = event.getRuleId();
        this.elementStatusBar.setRuleName(ruleId);
        if (ruleId != null) {
            this.ruleDetail.setRuleId(ruleId);
            contentNavigator.navigateTo(NAV_DISCOVERSPACE_DETAIL);
            this.ruleDetail.renderRuleDetail();
        }
    }

}

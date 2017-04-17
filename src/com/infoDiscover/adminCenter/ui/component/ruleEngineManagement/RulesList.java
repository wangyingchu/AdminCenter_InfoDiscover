package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingInitializer;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.RuleEngineOperationUtil;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event
        .RuleEngineComponentSelectedEvent;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun.
 */
public class RulesList extends VerticalLayout implements RuleEngineComponentSelectedEvent
        .RuleEngineComponentSelectedListener,
        RuleEngineDeletedEvent.RuleEngineDeletedListener, RuleEngineCreatedEvent.RuleEngineCreatedListener {

    private UserClientInfo currentUserClientInfo;
    private List<Button> ruleButtonsList;

    public RulesList(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.ruleButtonsList = new ArrayList<Button>();

        // check if RuleEngine is initialized
        if (!RuleEngineOperationUtil.checkRuleEngineDataSpaceExistence()) {
            PropertyMappingInitializer.initRuleEngine();
        }

        renderRulesList();
    }

    private void renderRulesList() {
        this.removeAllComponents();
        this.ruleButtonsList.clear();
        List<RuleVO> rulesList = RuleEngineOperationUtil.getExistingRulesList();
        if (rulesList != null) {
            for (final RuleVO rule : rulesList) {
                Button ruleButton = new Button(rule.getName());
                ruleButton.setIcon(FontAwesome.CUBE);
                ruleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
                ruleButton.addStyleName(ValoTheme.BUTTON_SMALL);
                ruleButton.addStyleName("ui_appHighLightElement");
                ruleButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        clearButtonSelectedStyle();
                        event.getButton().addStyleName("ui_appFriendlyElement");
                        sendRuleSelectedEvent(rule.getRuleId());
                    }
                });
                this.ruleButtonsList.add(ruleButton);
                addComponent(ruleButton);
            }
        }
    }

    private void clearButtonSelectedStyle() {
        for (Button currentButton : this.ruleButtonsList) {
            currentButton.removeStyleName("ui_appFriendlyElement");
        }
    }

    private void sendRuleSelectedEvent(String ruleName) {
        RuleEngineComponentSelectedEvent ruleEngineComponentSelectedEvent = new
                RuleEngineComponentSelectedEvent
                (ruleName);
        this.currentUserClientInfo.getEventBlackBoard().fire(ruleEngineComponentSelectedEvent);
    }

    @Override
    public void receivedRuleEngineDeletedEvent(RuleEngineDeletedEvent event) {
        this.renderRulesList();
    }

    @Override
    public void receivedRuleEngineComponentSelectedEvent(RuleEngineComponentSelectedEvent event) {
        this.renderRulesList();
    }

    @Override
    public void receivedRuleEngineCreatedEvent(RuleEngineCreatedEvent event) {
        this.renderRulesList();
    }
}

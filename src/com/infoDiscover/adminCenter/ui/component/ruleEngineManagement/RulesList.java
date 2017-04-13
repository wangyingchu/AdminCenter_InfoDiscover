package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.RuleEngineOperationUtil;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event
        .RuleEngineComponentSelectedEvent;
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
        RuleEngineDeletedEvent.RuleEngineDeletedListener {

    private UserClientInfo currentUserClientInfo;
    private List<Button> discoverSpaceButtonsList;

    public RulesList(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        this.discoverSpaceButtonsList = new ArrayList<Button>();
        renderDiscoverSpacesList();
    }

    private void renderDiscoverSpacesList() {
        this.removeAllComponents();
        this.discoverSpaceButtonsList.clear();
        List<String> rulesList = RuleEngineOperationUtil.getExistingRulesList();
        if (rulesList != null) {
            for (final String rule : rulesList) {
                Button spaceButton = new Button(rule);
                spaceButton.setIcon(FontAwesome.CUBE);
                spaceButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
                spaceButton.addStyleName(ValoTheme.BUTTON_SMALL);
                spaceButton.addStyleName("ui_appHighLightElement");
                spaceButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        clearButtonSelectedStyle();
                        event.getButton().addStyleName("ui_appFriendlyElement");
                        sendRuleSelectedEvent(rule);
                    }
                });
                this.discoverSpaceButtonsList.add(spaceButton);
                addComponent(spaceButton);
            }
        }
    }

    private void clearButtonSelectedStyle() {
        for (Button currentButton : this.discoverSpaceButtonsList) {
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
        this.renderDiscoverSpacesList();
    }

    @Override
    public void receivedRuleEngineComponentSelectedEvent(RuleEngineComponentSelectedEvent event) {
        this.renderDiscoverSpacesList();
    }
}

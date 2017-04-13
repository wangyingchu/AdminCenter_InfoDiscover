package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by sun.
 */
public class RuleEngineCreatedEvent implements Event {

    private String ruleName;

    public RuleEngineCreatedEvent(String ruleName){
        this.setRuleName(ruleName);
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public interface RuleEngineCreatedListener extends Listener {
        void receivedRuleEngineCreatedEvent(final RuleEngineCreatedEvent event);
    }
}

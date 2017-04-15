package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by sun.
 */
public class RuleEngineDeletedEvent implements Event {

    private String ruleId;

    public RuleEngineDeletedEvent(String ruleId){
        this.setRuleId(ruleId);
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public interface RuleEngineDeletedListener extends Listener {
        void receivedRuleEngineDeletedEvent(final RuleEngineDeletedEvent event);
    }
}

package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by sun.
 */
public class RuleEngineExecutedEvent implements Event {

    private String ruleId;

    public RuleEngineExecutedEvent(String ruleId){
        this.setRuleId(ruleId);
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public interface RuleEngineExecutedListener extends Listener {
        void receivedRuleEngineExecutedEvent(final RuleEngineExecutedEvent event);
    }
}

package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 10/1/16.
 */
public class RuleEngineComponentSelectedEvent implements Event {

    private String ruleId;

    public RuleEngineComponentSelectedEvent(String ruleId){
        this.setRuleId(ruleId);
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public interface RuleEngineComponentSelectedListener extends Listener {
        void receivedRuleEngineComponentSelectedEvent(final
                                                                RuleEngineComponentSelectedEvent event);
    }
}

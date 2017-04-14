package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 10/1/16.
 */
public class RuleEngineDeletedEvent implements Event {

    private String ruleName;

    public RuleEngineDeletedEvent(String ruleName){
        this.setRuleName(ruleName);
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public interface RuleEngineDeletedListener extends Listener {
        void receivedRuleEngineDeletedEvent(final RuleEngineDeletedEvent event);
    }
}

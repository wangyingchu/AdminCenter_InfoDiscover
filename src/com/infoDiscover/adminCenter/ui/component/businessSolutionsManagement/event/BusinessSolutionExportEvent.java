package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 5/5/17.
 */
public class BusinessSolutionExportEvent implements Event {

    private String businessSolutionName;

    public BusinessSolutionExportEvent(String businessSolutionName){
        this.setBusinessSolutionName(businessSolutionName);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }

    public interface BusinessSolutionExportListener extends Listener {
        public void receivedBusinessSolutionExportEvent(final BusinessSolutionExportEvent event);
    }
}

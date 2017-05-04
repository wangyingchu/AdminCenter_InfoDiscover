package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionsDetailPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;

    public BusinessSolutionsDetailPanel(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;

    }
}

package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

/**
 * Created by wangychu on 11/8/16.
 */
public class TypeDataInstanceTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;

    public TypeDataInstanceTableRowActions(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;



        Button showBelongedRolesButton = new Button();
        showBelongedRolesButton.setIcon(FontAwesome.EYE);
        showBelongedRolesButton.setDescription("Belongs To Roles");
        showBelongedRolesButton.addStyleName("small");
        showBelongedRolesButton.addStyleName("borderless");
        addComponent(showBelongedRolesButton);


        Button showWorkingTasksButton = new Button();
        showWorkingTasksButton.setIcon(FontAwesome.DOWNLOAD);
        showWorkingTasksButton.setDescription("Participant Tasks");
        showWorkingTasksButton.addStyleName("small");
        showWorkingTasksButton.addStyleName("borderless");
        addComponent(showWorkingTasksButton);


        Button deleteButton = new Button();
        deleteButton.setIcon(FontAwesome.TRASH_O);
        deleteButton.setDescription("Participant Tasks");
        deleteButton.addStyleName("small");
        deleteButton.addStyleName("borderless");
        addComponent(deleteButton);


    }

    public MeasurableValueVO getMeasurableValue() {
        return measurableValue;
    }

    public void setMeasurableValue(MeasurableValueVO measurableValue) {
        this.measurableValue = measurableValue;
    }
}

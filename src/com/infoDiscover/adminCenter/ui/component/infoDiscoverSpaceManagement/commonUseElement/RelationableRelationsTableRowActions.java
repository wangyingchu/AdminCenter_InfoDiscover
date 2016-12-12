package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 12/12/16.
 */
public class RelationableRelationsTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationableValueVO relationableValueVO;

    public RelationableRelationsTableRowActions(UserClientInfo userClientInfo,RelationableValueVO relationableValueVO) {
        this.currentUserClientInfo = userClientInfo;
        this.relationableValueVO=relationableValueVO;

        Button deleteRelationButton = new Button();
        deleteRelationButton.setIcon(FontAwesome.CHAIN_BROKEN);
        deleteRelationButton.setDescription("删除关联关系");
        deleteRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        deleteRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                deleteRelationButton.setEnabled(false);
                //showDataDetailInfoPanel();
            }
        });
        addComponent(deleteRelationButton);
    }
}

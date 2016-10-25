package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceDetail;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 10/24/16.
 */
public class InfoDiscoverSpaceRelationsInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private SecondarySectionTitle mainSectionTitle;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private RelationsRuntimeGeneralInfoPanel relationsRuntimeGeneralInfoPanel;
    private RelationTypesManagementPanel relationTypesManagementPanel;
    private RelationInstancesManagementPanel relationInstancesManagementPanel;

    public InfoDiscoverSpaceRelationsInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        mainSectionTitle=new SecondarySectionTitle("-------");
        addComponent(mainSectionTitle);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        this.relationsRuntimeGeneralInfoPanel =new RelationsRuntimeGeneralInfoPanel(this.currentUserClientInfo);
        TabSheet.Tab relationsRuntimeGeneralInfoLayoutTab =tabs.addTab(this.relationsRuntimeGeneralInfoPanel, "关系数据运行时信息");
        relationsRuntimeGeneralInfoLayoutTab.setIcon(FontAwesome.INFO_CIRCLE);

        this.relationTypesManagementPanel =new RelationTypesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab relationTypesManagementPanelLayoutTab =tabs.addTab(this.relationTypesManagementPanel, "关系类型管理");
        relationTypesManagementPanelLayoutTab.setIcon(FontAwesome.EXCHANGE);

        this.relationInstancesManagementPanel =new RelationInstancesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab relationInstancesManagementLayoutTab =tabs.addTab(this.relationInstancesManagementPanel, "关系数据管理");
        relationInstancesManagementLayoutTab.setIcon(FontAwesome.SHARE_ALT_SQUARE);

        HorizontalLayout actionButtonsPlacementLayout=new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceDimensionsInfoButton=new Button("刷新关系数据信息");
        refreshDiscoverSpaceDimensionsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        refreshDiscoverSpaceDimensionsInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceDimensionsInfoButton.setIcon(FontAwesome.REFRESH);
        refreshDiscoverSpaceDimensionsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(parentInfoDiscoverSpaceDetail!=null){
                    parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceDimensionsInfoButton);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
    }

    public void renderRelationsInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.mainSectionTitle.setValue(this.discoverSpaceName);
        this.relationsRuntimeGeneralInfoPanel.renderRelationsRuntimeGeneralInfo(discoverSpaceStatisticMetrics);
        this.relationTypesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.relationTypesManagementPanel.renderRelationTypesManagementInfo(discoverSpaceStatisticMetrics);
        this.relationInstancesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.relationInstancesManagementPanel.renderDimensionInstancesManagementInfo(discoverSpaceStatisticMetrics);
    }
}

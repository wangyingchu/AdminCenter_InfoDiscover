package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

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
 * Created by wangychu on 10/21/16.
 */
public class InfoDiscoverSpaceFactsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private SecondarySectionTitle mainSectionTitle;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private FactsRuntimeGeneralInfoPanel factsRuntimeGeneralInfoPanel;
    private FactTypesManagementPanel factTypesManagementPanel;

    public InfoDiscoverSpaceFactsInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        mainSectionTitle=new SecondarySectionTitle("-------");
        addComponent(mainSectionTitle);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        this.factsRuntimeGeneralInfoPanel =new FactsRuntimeGeneralInfoPanel(this.currentUserClientInfo);
        TabSheet.Tab factsRuntimeGeneralInfoLayoutTab =tabs.addTab(this.factsRuntimeGeneralInfoPanel, "事实数据运行时信息");
        factsRuntimeGeneralInfoLayoutTab.setIcon(FontAwesome.INFO_CIRCLE);

        this.factTypesManagementPanel =new FactTypesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab factTypesManagementPanelLayoutTab =tabs.addTab(this.factTypesManagementPanel, "事实类型管理");
        factTypesManagementPanelLayoutTab.setIcon(FontAwesome.TASKS);

        VerticalLayout VerticalLayout3=new VerticalLayout();
        TabSheet.Tab factInstancesManagementLayoutTab =tabs.addTab(VerticalLayout3, "事实数据管理");
        factInstancesManagementLayoutTab.setIcon(FontAwesome.SQUARE_O);

        HorizontalLayout actionButtonsPlacementLayout=new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceFactsInfoButton=new Button("刷新事实数据信息");
        refreshDiscoverSpaceFactsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        refreshDiscoverSpaceFactsInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceFactsInfoButton.setIcon(FontAwesome.REFRESH);

        refreshDiscoverSpaceFactsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(parentInfoDiscoverSpaceDetail!=null){
                    parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceFactsInfoButton);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderFactsInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.mainSectionTitle.setValue(this.discoverSpaceName);
        this.factsRuntimeGeneralInfoPanel.renderFactsRuntimeGeneralInfo(discoverSpaceStatisticMetrics);
        this.factTypesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.factTypesManagementPanel.renderFactTypesManagementInfo(discoverSpaceStatisticMetrics);
        //this.dimensionInstancesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        //this.dimensionInstancesManagementPanel.renderDimensionInstancesManagementInfo(discoverSpaceStatisticMetrics);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
    }
}

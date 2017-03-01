package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionActionBarTitle;
import com.infoDiscover.adminCenter.ui.component.event.DiscoverSpaceOpenProcessingDataListEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceDetail;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 10/3/16.
 */
public class InfoDiscoverSpaceDimensionsInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private DimensionsRuntimeGeneralInfoPanel dimensionsRuntimeGeneralInfoPanel;
    private DimensionTypesManagementPanel dimensionTypesManagementPanel;
    private DimensionInstancesManagementPanel dimensionInstancesManagementPanel;
    private SecondarySectionActionBarTitle secondarySectionActionBarTitle;

    public InfoDiscoverSpaceDimensionsInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        Button openProcessingDataListButton = new Button("待处理数据");
        openProcessingDataListButton.setIcon(VaadinIcons.MAILBOX);
        openProcessingDataListButton.setDescription("显示待处理数据列表");
        openProcessingDataListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        openProcessingDataListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        openProcessingDataListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DiscoverSpaceOpenProcessingDataListEvent discoverSpaceOpenProcessingDataListEvent =new DiscoverSpaceOpenProcessingDataListEvent(discoverSpaceName);
                currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceOpenProcessingDataListEvent);
            }
        });

        secondarySectionActionBarTitle=new SecondarySectionActionBarTitle("-------",new Button[]{openProcessingDataListButton});
        addComponent(secondarySectionActionBarTitle);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        this.dimensionsRuntimeGeneralInfoPanel =new DimensionsRuntimeGeneralInfoPanel(this.currentUserClientInfo);
        TabSheet.Tab dimensionsRuntimeGeneralInfoLayoutTab =tabs.addTab(this.dimensionsRuntimeGeneralInfoPanel, "维度数据运行时信息");
        dimensionsRuntimeGeneralInfoLayoutTab.setIcon(FontAwesome.INFO_CIRCLE);

        this.dimensionTypesManagementPanel =new DimensionTypesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab dimensionTypesManagementPanelLayoutTab =tabs.addTab(this.dimensionTypesManagementPanel, "维度类型管理");
        dimensionTypesManagementPanelLayoutTab.setIcon(FontAwesome.CODE_FORK);

        this.dimensionInstancesManagementPanel=new DimensionInstancesManagementPanel(this.currentUserClientInfo);
        TabSheet.Tab dimensionInstancesManagementLayoutTab =tabs.addTab(this.dimensionInstancesManagementPanel, "维度数据管理");
        dimensionInstancesManagementLayoutTab.setIcon(FontAwesome.TAG);

        HorizontalLayout actionButtonsPlacementLayout=new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout=new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceDimensionsInfoButton=new Button("刷新维度数据信息");
        refreshDiscoverSpaceDimensionsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        refreshDiscoverSpaceDimensionsInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceDimensionsInfoButton.setIcon(FontAwesome.REFRESH);

        final InfoDiscoverSpaceDimensionsInfo self=this;
        refreshDiscoverSpaceDimensionsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(self.parentInfoDiscoverSpaceDetail!=null){
                    self.parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceDimensionsInfoButton);

        VerticalLayout spacingLayout=new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderDimensionsInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.secondarySectionActionBarTitle.updateSectionTitle(this.discoverSpaceName);
        this.dimensionsRuntimeGeneralInfoPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.dimensionsRuntimeGeneralInfoPanel.renderDimensionsRuntimeGeneralInfo(discoverSpaceStatisticMetrics);
        this.dimensionTypesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.dimensionTypesManagementPanel.renderDimensionTypesManagementInfo(discoverSpaceStatisticMetrics);
        this.dimensionInstancesManagementPanel.setDiscoverSpaceName(this.discoverSpaceName);
        this.dimensionInstancesManagementPanel.renderDimensionInstancesManagementInfo(discoverSpaceStatisticMetrics);
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void setParentInfoDiscoverSpaceDetail(InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail) {
        this.parentInfoDiscoverSpaceDetail = parentInfoDiscoverSpaceDetail;
        this.dimensionInstancesManagementPanel.setAncestorInfoDiscoverSpaceDetail(this.parentInfoDiscoverSpaceDetail);
    }
}
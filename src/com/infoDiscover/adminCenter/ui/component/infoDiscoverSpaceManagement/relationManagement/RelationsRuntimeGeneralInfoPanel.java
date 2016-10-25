package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement;

import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * Created by wangychu on 10/25/16.
 */
public class RelationsRuntimeGeneralInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Grid relationTypesDataGrid;
    private InfoDiscoverSpaceRelationsInfoChart infoDiscoverSpaceRelationsInfoChart;

    public RelationsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("关系数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        relationTypesDataGrid = new Grid();
        relationTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        relationTypesDataGrid.setWidth("100%");
        // Define columns
        relationTypesDataGrid.addColumn("关系类型名称", String.class);
        relationTypesDataGrid.addColumn("关系类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(relationTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        this.infoDiscoverSpaceRelationsInfoChart=new InfoDiscoverSpaceRelationsInfoChart(this.currentUserClientInfo);
        elementPlacementLayout.addComponent(this.infoDiscoverSpaceRelationsInfoChart);
        elementPlacementLayout.setComponentAlignment(this.infoDiscoverSpaceRelationsInfoChart, Alignment.MIDDLE_LEFT);
    }

    public void renderRelationsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceRelationsInfoChart.renderRelationsInfoChart(discoverSpaceStatisticMetrics);
        this.relationTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> relationsStatisticMetricsList=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:relationsStatisticMetricsList){
            String relationTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            this.relationTypesDataGrid.addRow(relationTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        relationTypesDataGrid.recalculateColumnWidths();
    }
}

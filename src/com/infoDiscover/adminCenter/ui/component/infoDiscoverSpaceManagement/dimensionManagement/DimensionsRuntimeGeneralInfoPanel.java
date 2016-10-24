package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

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
 * Created by wangychu on 10/6/16.
 */
public class DimensionsRuntimeGeneralInfoPanel extends VerticalLayout{

    private UserClientInfo currentUserClientInfo;
    private Grid dimensionTypesDataGrid;
    private InfoDiscoverSpaceDimensionsInfoChart infoDiscoverSpaceDimensionsInfoChart;

    public DimensionsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("维度数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        dimensionTypesDataGrid = new Grid();
        dimensionTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        dimensionTypesDataGrid.setWidth("100%");
        // Define columns
        dimensionTypesDataGrid.addColumn("维度类型名称", String.class);
        dimensionTypesDataGrid.addColumn("维度类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(dimensionTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        this.infoDiscoverSpaceDimensionsInfoChart=new InfoDiscoverSpaceDimensionsInfoChart(this.currentUserClientInfo);
        elementPlacementLayout.addComponent(infoDiscoverSpaceDimensionsInfoChart);
        elementPlacementLayout.setComponentAlignment(infoDiscoverSpaceDimensionsInfoChart, Alignment.MIDDLE_LEFT);
    }

    public void renderDimensionsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceDimensionsInfoChart.renderDimensionsInfoChart(discoverSpaceStatisticMetrics);
        this.dimensionTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> dimensionsStatisticMetricsList=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:dimensionsStatisticMetricsList){
            String dimensionTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            this.dimensionTypesDataGrid.addRow(dimensionTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        dimensionTypesDataGrid.recalculateColumnWidths();
    }
}

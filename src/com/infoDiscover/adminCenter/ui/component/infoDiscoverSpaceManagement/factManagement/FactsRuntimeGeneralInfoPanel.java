package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement;

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
 * Created by wangychu on 10/21/16.
 */
public class FactsRuntimeGeneralInfoPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Grid factTypesDataGrid;
    private InfoDiscoverSpaceFactsInfoChart infoDiscoverSpaceFactsInfoChart;
    public FactsRuntimeGeneralInfoPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;

        SecondarySectionTitle secondarySectionTitle=new SecondarySectionTitle("事实数据基本信息");
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);
        factTypesDataGrid = new Grid();
        factTypesDataGrid.setSelectionMode(Grid.SelectionMode.NONE);
        factTypesDataGrid.setWidth("100%");
        // Define columns
        factTypesDataGrid.addColumn("事实类型名称", String.class);
        factTypesDataGrid.addColumn("事实类型数据总量", Long.class);

        VerticalLayout generalInfoContainer=new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(factTypesDataGrid);
        elementPlacementLayout.addComponent(generalInfoContainer);

        infoDiscoverSpaceFactsInfoChart=new InfoDiscoverSpaceFactsInfoChart(this.currentUserClientInfo);
        elementPlacementLayout.addComponent(infoDiscoverSpaceFactsInfoChart);
        elementPlacementLayout.setComponentAlignment(infoDiscoverSpaceFactsInfoChart, Alignment.MIDDLE_LEFT);
    }

    public void renderFactsRuntimeGeneralInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.infoDiscoverSpaceFactsInfoChart.renderFactsInfoChart(discoverSpaceStatisticMetrics);
        this.factTypesDataGrid.getContainerDataSource().removeAllItems();
        List<DataTypeStatisticMetrics> factsStatisticMetricsList=discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
        for(DataTypeStatisticMetrics currentMetrics:factsStatisticMetricsList){
            String factTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_FACT,"");
            this.factTypesDataGrid.addRow(factTypeName, new Long(currentMetrics.getTypeDataCount()));
        }
        factTypesDataGrid.recalculateColumnWidths();
    }
}

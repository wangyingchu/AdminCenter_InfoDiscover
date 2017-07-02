package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by wangychu on 6/30/17.
 */
public class DataMappingDefinitionsManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private CommonDataRelationMappingDefinitionEditPanel commonDataRelationMappingDefinitionEditPanel;
    private FactAndDateDimensionMappingDefinitionEditPanel factAndDateDimensionMappingDefinitionEditPanel;

    public DataMappingDefinitionsManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");

        TabSheet tabs=new TabSheet();
        this.addComponent(tabs);

        VerticalLayout commonDataRelationMappingDefinitionInfoLayout=new VerticalLayout();
        TabSheet.Tab commonDataRelationMappingDefinitionInfoLayoutTab =tabs.addTab(commonDataRelationMappingDefinitionInfoLayout, "常规数据属性关联映射管理");
        commonDataRelationMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.COGS);
        commonDataRelationMappingDefinitionEditPanel=new CommonDataRelationMappingDefinitionEditPanel(this.currentUserClientInfo);
        commonDataRelationMappingDefinitionInfoLayout.addComponent(commonDataRelationMappingDefinitionEditPanel);

        VerticalLayout factAndDateDimensionMappingDefinitionInfoLayout=new VerticalLayout();
        TabSheet.Tab factAndDateDimensionMappingDefinitionInfoLayoutTab =tabs.addTab(factAndDateDimensionMappingDefinitionInfoLayout, "事实与时间维度关联定义管理");
        factAndDateDimensionMappingDefinitionInfoLayoutTab.setIcon(FontAwesome.CLOCK_O);
        factAndDateDimensionMappingDefinitionEditPanel=new FactAndDateDimensionMappingDefinitionEditPanel(this.currentUserClientInfo);
        factAndDateDimensionMappingDefinitionInfoLayout.addComponent(factAndDateDimensionMappingDefinitionEditPanel);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void renderDataMappingDefinitionsInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        commonDataRelationMappingDefinitionEditPanel.setBusinessSolutionName(getBusinessSolutionName());
        commonDataRelationMappingDefinitionEditPanel.renderCommonDataRelationMappingDefinitionInfo(getBusinessSolutionName());
        factAndDateDimensionMappingDefinitionEditPanel.setBusinessSolutionName(getBusinessSolutionName());
        factAndDateDimensionMappingDefinitionEditPanel.renderFactAndDateDimensionMappingDefinitionInfo();
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}

package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.DataMappingDefinitionVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 6/30/17.
 */
public class CommonDataRelationMappingDefinitionEditPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private TreeTable dataRelationMappingDefinitionsTable;

    public CommonDataRelationMappingDefinitionEditPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        this.setMargin(new MarginInfo(true,false,false,false));

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-470;

        HorizontalLayout actionButtonPlacementLayout=new HorizontalLayout();
        this.addComponent(actionButtonPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout1=new HorizontalLayout();
        actionButtonsSpacingLayout1.setWidth("10px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout1);

        Label actionTitle= new Label(FontAwesome.LIST.getHtml() +" 数据属性关联映射规则", ContentMode.HTML);
        actionButtonPlacementLayout.addComponent(actionTitle);

        HorizontalLayout actionButtonsSpacingLayout2=new HorizontalLayout();
        actionButtonsSpacingLayout2.setWidth("20px");
        actionButtonPlacementLayout.addComponent(actionButtonsSpacingLayout2);

        Button createRelationMappingRuleButton=new Button("创建数据属性关联映射规则");
        createRelationMappingRuleButton.setIcon(FontAwesome.PLUS_CIRCLE);
        createRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        createRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        createRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(createRelationMappingRuleButton);
        createRelationMappingRuleButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateDataMappingRuleOperation();
            }
        });

        Label spaceDivLabel1=new Label("|");
        actionButtonPlacementLayout. addComponent(spaceDivLabel1);

        Button removeRelationMappingRuleButton=new Button("删除关联映射规则");
        removeRelationMappingRuleButton.setEnabled(false);
        removeRelationMappingRuleButton.setIcon(FontAwesome.TRASH_O);
        removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        removeRelationMappingRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        removeRelationMappingRuleButton.addStyleName("ui_appElementBottomSpacing");
        actionButtonPlacementLayout.addComponent(removeRelationMappingRuleButton);

        this.dataRelationMappingDefinitionsTable = new TreeTable();
        this.dataRelationMappingDefinitionsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.dataRelationMappingDefinitionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.dataRelationMappingDefinitionsTable.setSizeFull();
        this.dataRelationMappingDefinitionsTable.setSelectable(true);

        this.dataRelationMappingDefinitionsTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.dataRelationMappingDefinitionsTable.setNullSelectionAllowed(false);

        this.dataRelationMappingDefinitionsTable.addContainerProperty("源数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源类型名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性最小值", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("源属性最大值", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("关联关系类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("数据关联方向", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("不存在映射处理策略", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标数据类型", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标类型名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标属性名称", String.class, "");
        this.dataRelationMappingDefinitionsTable.addContainerProperty("目标属性数据类型", String.class, "");

        this.addComponent(this.dataRelationMappingDefinitionsTable);
    }

    public void renderCommonDataRelationMappingDefinitionInfo(String businessSolutionName){
        this.dataRelationMappingDefinitionsTable.removeAllItems();
        List<DataMappingDefinitionVO> dataMappingDefinitionsList= BusinessSolutionOperationUtil.getCommonDataRelationMappingDefinitionList(businessSolutionName);
        for(DataMappingDefinitionVO currentDataMappingDefinitionVO:dataMappingDefinitionsList){
            String sourceMinValue=currentDataMappingDefinitionVO.getMinValue()!=null?currentDataMappingDefinitionVO.getMinValue():"";
            String sourceMaxValue=currentDataMappingDefinitionVO.getMaxValue()!=null?currentDataMappingDefinitionVO.getMaxValue():"";
            Object[] newDefinitionInfo=new Object[]{
                    currentDataMappingDefinitionVO.getSourceDataTypeKind(),
                    currentDataMappingDefinitionVO.getSourceDataTypeName(),
                    currentDataMappingDefinitionVO.getTargetDataPropertyName(),
                    currentDataMappingDefinitionVO.getSourceDataPropertyType(),
                    sourceMinValue,
                    sourceMaxValue,
                    currentDataMappingDefinitionVO.getRelationTypeName(),
                    currentDataMappingDefinitionVO.getRelationDirection(),
                    currentDataMappingDefinitionVO.getMappingNotExistHandleMethod(),
                    currentDataMappingDefinitionVO.getTargetDataTypeKind(),
                    currentDataMappingDefinitionVO.getTargetDataTypeName(),
                    currentDataMappingDefinitionVO.getTargetDataPropertyName(),
                    currentDataMappingDefinitionVO.getTargetDataPropertyType()
            };
            final Object newDataItemKey =this.dataRelationMappingDefinitionsTable.addItem(newDefinitionInfo,null);
            this.dataRelationMappingDefinitionsTable.setChildrenAllowed(newDataItemKey, false);
        }
    }

    private void executeCreateDataMappingRuleOperation(){
        CommonDataRelationMappingDefinitionEditor commonDataRelationMappingDefinitionEditor=new CommonDataRelationMappingDefinitionEditor(this.currentUserClientInfo);
        commonDataRelationMappingDefinitionEditor.setBusinessSolutionName(getBusinessSolutionName());
        commonDataRelationMappingDefinitionEditor.setRelatedCommonDataRelationMappingDefinitionEditPanel(this);
        final Window window = new Window();
        window.setWidth(750.0f, Unit.PIXELS);
        window.setHeight(600.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(commonDataRelationMappingDefinitionEditor);
        commonDataRelationMappingDefinitionEditor.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}

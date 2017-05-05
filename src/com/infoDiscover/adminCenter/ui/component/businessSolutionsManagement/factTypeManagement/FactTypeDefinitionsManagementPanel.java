package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.factTypeManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.BusinessSolutionOperationUtil;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.FactTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypePropertyPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 5/5/17.
 */
public class FactTypeDefinitionsManagementPanel extends VerticalLayout implements CreateTypePropertyPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private String businessSolutionName;
    private TreeTable factTypesTreeTable;
    private TreeTable factTypePropertiesTable;

    private String NAME_PROPERTY="事实类型名称";
    private String ALIASNAME_PROPERTY="类型别名";
    private String PROPERTYNAME_PROPERTY="类型属性名";
    private String PROPERTYALIASNAME_PROPERTY="属性别名";
    private String PROPERTYTYPE_PROPERTY="属性数据类型";
    private String MUSTINPUT_PROPERTY="必填属性";
    private String READONLY_PROPERTY="只读属性";
    private String ALLOWNULL_PROPERTY="允许空值";

    private VerticalLayout rightSideUIElementsContainer;
    private VerticalLayout rightSideUIElementsBox;
    private VerticalLayout rightSideUIPromptBox;

    private Button createFactTypeButton;
    private Button removeFactTypeButton;
    private Button deleteFactTypePropertyButton;

    private Map<String,PropertyTypeVO> currentFactTypePropertiesMap;
    private String currentSelectedFactTypeName;
    private String currentSelectedFactTypePropertyName;

    public FactTypeDefinitionsManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dataDisplayElementHeight=screenHeight-450;

        //left side elements
        VerticalLayout leftSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(leftSideUIElementsContainer);

        VerticalLayout leftSideActionButtonsSpaceDiv=new VerticalLayout();
        leftSideUIElementsContainer.addComponent(leftSideActionButtonsSpaceDiv);

        HorizontalLayout leftSideActionButtonPlacementLayout=new HorizontalLayout();
        leftSideUIElementsContainer.addComponent(leftSideActionButtonPlacementLayout);

        HorizontalLayout leftSideActionButtonsSpacingLayout1=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout1.setWidth("10px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout1);

        Label leftSideTitle= new Label(FontAwesome.TASKS.getHtml() +" 事实类型", ContentMode.HTML);
        leftSideActionButtonPlacementLayout.addComponent(leftSideTitle);

        HorizontalLayout leftSideActionButtonsSpacingLayout2=new HorizontalLayout();
        leftSideActionButtonsSpacingLayout2.setWidth("20px");
        leftSideActionButtonPlacementLayout.addComponent(leftSideActionButtonsSpacingLayout2);

        this.createFactTypeButton=new Button("创建事实类型");
        this.createFactTypeButton.setEnabled(true);
        this.createFactTypeButton.setIcon(FontAwesome.PLUS_CIRCLE);
        this.createFactTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.createFactTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.createFactTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.createFactTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                executeCreateFactTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(createFactTypeButton);

        Label spaceDivLabel1=new Label("|");
        leftSideActionButtonPlacementLayout. addComponent(spaceDivLabel1);

        this.removeFactTypeButton=new Button("删除事实类型");
        this.removeFactTypeButton.setEnabled(false);
        this.removeFactTypeButton.setIcon(FontAwesome.TRASH_O);
        this.removeFactTypeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.removeFactTypeButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.removeFactTypeButton.addStyleName("ui_appElementBottomSpacing");
        this.removeFactTypeButton.addClickListener(new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //executeDeleteFactTypeOperation();
            }
        });

        leftSideActionButtonPlacementLayout.addComponent(removeFactTypeButton);

        this.factTypesTreeTable = new TreeTable();
        this.factTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.factTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.factTypesTreeTable.setSizeFull();
        this.factTypesTreeTable.setSelectable(true);
        this.factTypesTreeTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.factTypesTreeTable.setNullSelectionAllowed(false);

        this.factTypesTreeTable.addContainerProperty(NAME_PROPERTY, String.class, "");
        this.factTypesTreeTable.addContainerProperty(ALIASNAME_PROPERTY, String.class, "");

        this.factTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedFactTypeName = itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderFactTypeSelectedUIElements(selectedFactTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(factTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.LIST_UL.getHtml()+" 选定事实类型包含的属性信息。", ContentMode.HTML);
        functionMessage.setStyleName("ui_appLightDarkMessage");
        functionMessage.addStyleName(ValoTheme.LABEL_LARGE);
        this.rightSideUIPromptBox.addComponent(functionMessage);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);

        this.rightSideUIElementsBox=new VerticalLayout();

        VerticalLayout rightSideActionButtonsSpaceDiv=new VerticalLayout();
        this.rightSideUIElementsBox.addComponent(rightSideActionButtonsSpaceDiv);

        HorizontalLayout rightSideActionButtonPlacementLayout=new HorizontalLayout();
        this.rightSideUIElementsBox.addComponent(rightSideActionButtonPlacementLayout);

        HorizontalLayout rightSideActionButtonsSpacingLayout1=new HorizontalLayout();
        rightSideActionButtonsSpacingLayout1.setWidth("10px");
        rightSideActionButtonPlacementLayout.addComponent(rightSideActionButtonsSpacingLayout1);

        Label rightSideTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 类型属性", ContentMode.HTML);
        rightSideActionButtonPlacementLayout.addComponent(rightSideTitle);

        HorizontalLayout rightSideActionButtonsSpacingLayout2=new HorizontalLayout();
        rightSideActionButtonsSpacingLayout2.setWidth("20px");
        rightSideActionButtonPlacementLayout.addComponent(rightSideActionButtonsSpacingLayout2);

        Button addFactTypePropertyButton=new Button("添加类型属性");
        addFactTypePropertyButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        addFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        addFactTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        addFactTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //executeAddFactTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(addFactTypePropertyButton);

        Label spaceDivLabel2=new Label("|");
        rightSideActionButtonPlacementLayout. addComponent(spaceDivLabel2);

        this.deleteFactTypePropertyButton =new Button("删除类型属性");
        this.deleteFactTypePropertyButton.setIcon(FontAwesome.TRASH_O);
        this.deleteFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        this.deleteFactTypePropertyButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.deleteFactTypePropertyButton.addStyleName("ui_appElementBottomSpacing");
        this.deleteFactTypePropertyButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //executeDeleteFactTypePropertyOperation();
            }
        });

        rightSideActionButtonPlacementLayout.addComponent(this.deleteFactTypePropertyButton);

        this.factTypePropertiesTable = new TreeTable();
        this.factTypePropertiesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.factTypePropertiesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.factTypePropertiesTable.setSizeFull();
        this.factTypePropertiesTable.setSelectable(true);
        this.factTypePropertiesTable.setHeight(dataDisplayElementHeight, Unit.PIXELS);
        this.factTypePropertiesTable.setNullSelectionAllowed(false);

        this.factTypePropertiesTable.addContainerProperty(PROPERTYNAME_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(PROPERTYALIASNAME_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(PROPERTYTYPE_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(MUSTINPUT_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(READONLY_PROPERTY, String.class, "");
        this.factTypePropertiesTable.addContainerProperty(ALLOWNULL_PROPERTY, String.class, "");

        this.factTypePropertiesTable.setColumnWidth(PROPERTYTYPE_PROPERTY,100);
        this.factTypePropertiesTable.setColumnWidth(MUSTINPUT_PROPERTY,70);
        this.factTypePropertiesTable.setColumnWidth(READONLY_PROPERTY,70);
        this.factTypePropertiesTable.setColumnWidth(ALLOWNULL_PROPERTY,70);

        this.factTypePropertiesTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedFactTypeName=itemClickEvent.getItem().getItemProperty(PROPERTYNAME_PROPERTY).getValue().toString();
                //renderFactTypePropertySelectedUIElements(selectedFactTypeName);
            }
        });
        this.rightSideUIElementsBox.addComponent(factTypePropertiesTable);
    }

    private void executeCreateFactTypeOperation(){
        CreateFactTypeDefinitionPanel createFactTypeDefinitionPanel=new CreateFactTypeDefinitionPanel(this.currentUserClientInfo);
        createFactTypeDefinitionPanel.setBusinessSolutionName(this.businessSolutionName);
        createFactTypeDefinitionPanel.setFactTypeDefinitionsManagementPanel(this);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setHeight(240.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createFactTypeDefinitionPanel);
        createFactTypeDefinitionPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }









    @Override
    public void createTypePropertyActionFinish(boolean actionResult) {

    }

    public void renderFactTypeDefinitionsManagementInfo(String businessSolutionName){
        setBusinessSolutionName(businessSolutionName);
        this.factTypesTreeTable.getContainerDataSource().removeAllItems();
        clearFactTypeSelectStatus();

        List<FactTypeDefinitionVO> factTypeDefinitionsList= BusinessSolutionOperationUtil.getBusinessSolutionFactTypeList(getBusinessSolutionName());
        for(FactTypeDefinitionVO currentFactTypeDefinition:factTypeDefinitionsList){

            Object[] factTypeInfo=new Object[]{
                    currentFactTypeDefinition.getTypeName(),currentFactTypeDefinition.getTypeAliasName()
            };
            final Object currentFactTypeInfoKey = this.factTypesTreeTable.addItem(factTypeInfo, null);
            this.factTypesTreeTable.setChildrenAllowed(currentFactTypeInfoKey, false);
            this.factTypesTreeTable.setColumnCollapsible(currentFactTypeInfoKey, false);
        }
        this.factTypePropertiesTable.setWidth("100%");
    }

    private void clearFactTypeSelectStatus(){
        this.currentFactTypePropertiesMap =null;
        this.factTypePropertiesTable.getContainerDataSource().removeAllItems();
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        this.removeFactTypeButton.setEnabled(false);
        this.currentSelectedFactTypeName =null;
    }

    private void renderFactTypeSelectedUIElements(String factTypeName){
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
        this.currentFactTypePropertiesMap =null;
        this.factTypePropertiesTable.getContainerDataSource().removeAllItems();
        /*
        List<PropertyTypeVO> factTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(this.discoverSpaceName, factTypeName);
        this.currentFactTypePropertiesMap =new HashMap<String,PropertyTypeVO>();
        for(PropertyTypeVO currentPropertyTypeVO:factTypePropertiesList){
            this.currentFactTypePropertiesMap.put(currentPropertyTypeVO.getPropertyName(), currentPropertyTypeVO);
            Object[] currentFactTypePropertiesInfo=new Object[]{
                    " "+currentPropertyTypeVO.getPropertyName(),
                    currentPropertyTypeVO.getPropertyAliasName(),
                    currentPropertyTypeVO.getPropertyType(),
                    ""+ currentPropertyTypeVO.isMandatory(),
                    ""+ currentPropertyTypeVO.isReadOnly(),
                    ""+ currentPropertyTypeVO.isNullable()
            };
            final Object currentFactTypeInfoKey = this.factTypePropertiesTable.addItem(currentFactTypePropertiesInfo, null);
            if(factTypeName.equals(currentPropertyTypeVO.getPropertySourceOwner())){
                this.factTypePropertiesTable.setItemIcon(currentFactTypeInfoKey, FontAwesome.CIRCLE_O);
            }else{
                this.factTypePropertiesTable.setItemIcon(currentFactTypeInfoKey, FontAwesome.REPLY_ALL);
            }
            this.factTypePropertiesTable.setChildrenAllowed(currentFactTypeInfoKey, false);
            this.factTypePropertiesTable.setColumnCollapsible(currentFactTypeInfoKey, false);
        }
        */
        this.currentSelectedFactTypeName =factTypeName;
        this.removeFactTypeButton.setEnabled(true);
        this.deleteFactTypePropertyButton.setEnabled(false);
    }

    public String getBusinessSolutionName() {
        return businessSolutionName;
    }

    public void setBusinessSolutionName(String businessSolutionName) {
        this.businessSolutionName = businessSolutionName;
    }
}

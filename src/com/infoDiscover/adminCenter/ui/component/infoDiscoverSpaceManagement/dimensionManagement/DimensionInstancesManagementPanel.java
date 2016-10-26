package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.DimensionTypeVO;
import com.infoDiscover.adminCenter.ui.component.event.DiscoverSpaceTypeDataInstanceQueryRequiredEvent;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.CreateTypeDataInstancePanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 10/13/16.
 */
public class DimensionInstancesManagementPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private VerticalLayout rightSideUIElementsContainer;
    private HierarchicalContainer dimensionTypesInfoContainer;
    private TreeTable dimensionTypesTreeTable;
    private Label dimensionTypeNameLabel;
    private Label dimensionCountLabel;
    private String NAME_PROPERTY="维度类型名称";
    private DiscoverSpaceStatisticMetrics currentDiscoverSpaceStatisticMetrics;
    private String currentSelectedDimensionTypeName;
    private DimensionTypeDataInstancesInfoChart dimensionTypeDataInstancesInfoChart;
    private MenuBar.Command createDimensionInstanceMenuItemCommand;
    private MenuBar.Command searchDimensionInstanceMenuItemCommand;
    private MenuBar.MenuItem createDimensionInstanceMenuItem;
    private MenuBar.MenuItem searchDimensionInstanceMenuItem;
    private FormLayout dimensionTypeDataInstanceCountInfoForm;

    private VerticalLayout rightSideUIPromptBox;
    private VerticalLayout rightSideUIElementsBox;

    public DimensionInstancesManagementPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setWidth("100%");
        HorizontalLayout elementPlacementLayout=new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        int screenHeight=this.currentUserClientInfo.getUserWebBrowserInfo().getScreenHeight();
        int dimensionTypesTreeHeight=screenHeight-520;
        int dataCountFormPanelHeight=screenHeight-660;

        //left side elements
        VerticalLayout leftSideUIElementsContainer=new VerticalLayout();
        leftSideUIElementsContainer.setWidth(400,Unit.PIXELS);
        leftSideUIElementsContainer.setHeight("100%");
        elementPlacementLayout.addComponent(leftSideUIElementsContainer);

        Label leftSideTitle= new Label(FontAwesome.CODE_FORK.getHtml() +" 维度类型选择:", ContentMode.HTML);
        leftSideTitle.addStyleName(ValoTheme.LABEL_SMALL);
        leftSideTitle.addStyleName("ui_appStandaloneElementPadding");
        leftSideTitle.addStyleName("ui_appSectionLightDiv");
        leftSideUIElementsContainer.addComponent(leftSideTitle);

        this.dimensionTypesInfoContainer=new HierarchicalContainer();
        this.dimensionTypesInfoContainer.addContainerProperty(NAME_PROPERTY, String.class, "");

        this.dimensionTypesTreeTable = new TreeTable();
        this.dimensionTypesTreeTable.setSizeFull();
        this.dimensionTypesTreeTable.setSelectable(true);
        this.dimensionTypesTreeTable.setMultiSelect(false);
        this.dimensionTypesTreeTable.setImmediate(true);
        this.dimensionTypesTreeTable.setNullSelectionAllowed(false);
        this.dimensionTypesTreeTable.setContainerDataSource(this.dimensionTypesInfoContainer);
        this.dimensionTypesTreeTable.setItemCaptionPropertyId(NAME_PROPERTY);
        this.dimensionTypesTreeTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.dimensionTypesTreeTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        this.dimensionTypesTreeTable.setHeight(dimensionTypesTreeHeight, Unit.PIXELS);
        this.dimensionTypesTreeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String selectedDimensionTypeName=itemClickEvent.getItem().getItemProperty(NAME_PROPERTY).getValue().toString();
                renderDimensionTypeSelectedUIElements(selectedDimensionTypeName);
            }
        });
        leftSideUIElementsContainer.addComponent(this.dimensionTypesTreeTable);
        leftSideUIElementsContainer.addStyleName("ui_appElementRightSideSpacing");

        //right side elements
        this.rightSideUIElementsContainer=new VerticalLayout();
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);
        rightSideUIElementsContainer.setHeight("100%");
        elementPlacementLayout.addComponent(rightSideUIElementsContainer);
        elementPlacementLayout.setExpandRatio(rightSideUIElementsContainer, 1.0F);

        this.rightSideUIPromptBox=new VerticalLayout();
        VerticalLayout messageHeightSpaceDiv=new VerticalLayout();
        messageHeightSpaceDiv.setHeight(30,Unit.PIXELS);
        this.rightSideUIPromptBox.addComponent(messageHeightSpaceDiv);
        Label functionMessage = new Label( FontAwesome.TAGS.getHtml()+" 维度数据操作。", ContentMode.HTML);
        functionMessage.setStyleName("ui_appLightDarkMessage");
        functionMessage.addStyleName(ValoTheme.LABEL_LARGE);
        this.rightSideUIPromptBox.addComponent(functionMessage);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);

        this.rightSideUIElementsBox=new VerticalLayout();
        //this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);

        Label rightSideTitle= new Label(FontAwesome.TAGS.getHtml() +" 维度数据操作:", ContentMode.HTML);
        rightSideTitle.addStyleName(ValoTheme.LABEL_SMALL);
        rightSideTitle.addStyleName("ui_appStandaloneElementPadding");
        rightSideTitle.addStyleName("ui_appSectionLightDiv");
        this.rightSideUIElementsBox.addComponent(rightSideTitle);

        HorizontalLayout dimensionTypeInfoLayout=new HorizontalLayout();
        dimensionTypeInfoLayout.setWidth("100%");
        dimensionTypeInfoLayout.addStyleName("ui_appStandaloneElementPadding");
        dimensionTypeInfoLayout.addStyleName("ui_appSectionLightDiv");
        this.rightSideUIElementsBox.addComponent(dimensionTypeInfoLayout);

        HorizontalLayout dimensionTypeDetailContainerLayout=new HorizontalLayout();
        dimensionTypeInfoLayout.addComponent(dimensionTypeDetailContainerLayout);

        Label dimensionTypeLabel = new Label( FontAwesome.CODE_FORK.getHtml()+" 维度类型名称:", ContentMode.HTML);
        dimensionTypeLabel.addStyleName(ValoTheme.LABEL_TINY);
        dimensionTypeDetailContainerLayout.addComponent(dimensionTypeLabel);

        HorizontalLayout spacingDivLayout1=new HorizontalLayout();
        spacingDivLayout1.setWidth(10,Unit.PIXELS);
        dimensionTypeDetailContainerLayout.addComponent(spacingDivLayout1);

        dimensionTypeDetailContainerLayout.setComponentAlignment(dimensionTypeLabel,Alignment.MIDDLE_LEFT);

        this.dimensionTypeNameLabel=new Label("-");
        //this.dimensionTypeNameLabel.addStyleName(ValoTheme.LABEL_COLORED);
        this.dimensionTypeNameLabel.addStyleName("ui_appFriendlyElement");
        this.dimensionTypeNameLabel.addStyleName(ValoTheme.LABEL_H2);
        this.dimensionTypeNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        this.dimensionTypeNameLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        dimensionTypeDetailContainerLayout.addComponent(this.dimensionTypeNameLabel);

        HorizontalLayout spacingDivLayout2=new HorizontalLayout();
        spacingDivLayout2.setWidth(10,Unit.PIXELS);
        dimensionTypeDetailContainerLayout.addComponent(spacingDivLayout2);

        Label dimensionNumberLabel = new Label( " 类型数据总量:");
        dimensionNumberLabel.addStyleName(ValoTheme.LABEL_TINY);
        dimensionTypeDetailContainerLayout.addComponent(dimensionNumberLabel);
        dimensionTypeDetailContainerLayout.setComponentAlignment(dimensionNumberLabel,Alignment.MIDDLE_LEFT);

        HorizontalLayout spacingDivLayout3=new HorizontalLayout();
        spacingDivLayout3.setWidth(10,Unit.PIXELS);
        dimensionTypeDetailContainerLayout.addComponent(spacingDivLayout3);

        this.dimensionCountLabel=new Label("");
        //this.dimensionCountLabel.addStyleName(ValoTheme.LABEL_COLORED);
        this.dimensionCountLabel.addStyleName("ui_appFriendlyElement");
        this.dimensionCountLabel.addStyleName(ValoTheme.LABEL_H2);
        this.dimensionCountLabel.addStyleName(ValoTheme.LABEL_BOLD);
        this.dimensionCountLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        dimensionTypeDetailContainerLayout.addComponent(this.dimensionCountLabel);

        HorizontalLayout dimensionDataOperationContainerLayout=new HorizontalLayout();
        this.rightSideUIElementsBox.addComponent(dimensionDataOperationContainerLayout);

        HorizontalLayout dimensionDataOperationContainerSpaceDivLayout=new HorizontalLayout();
        dimensionDataOperationContainerSpaceDivLayout.setWidth("10px");
        dimensionDataOperationContainerLayout.addComponent(dimensionDataOperationContainerSpaceDivLayout);

        MenuBar dimensionInstanceOperationMenuBar = new MenuBar();
        dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        //dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        dimensionDataOperationContainerLayout.addComponent(dimensionInstanceOperationMenuBar);

        // Define a common menu command for all the create dimension menu items
        this.createDimensionInstanceMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedDimensionTypeName=selectedItem.getText();
                executeCreateDimensionOperation(selectedDimensionTypeName);
            }
        };
        // Define a common menu command for all the search dimension menu items
        this.searchDimensionInstanceMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedDimensionTypeName=selectedItem.getText();
                executeSearchDimensionTypeOperation(selectedDimensionTypeName);
            }
        };

        // Put operation items in the menu
        this.createDimensionInstanceMenuItem = dimensionInstanceOperationMenuBar.addItem("创建维度数据", FontAwesome.PLUS_CIRCLE, null);
        this.searchDimensionInstanceMenuItem = dimensionInstanceOperationMenuBar.addItem("查询维度数据", FontAwesome.SEARCH, null);

        HorizontalLayout dimensionTypeDataInstanceInfoContainerLayout=new HorizontalLayout();
        dimensionTypeDataInstanceInfoContainerLayout.setWidth(700,Unit.PIXELS);
        this.rightSideUIElementsBox.addComponent(dimensionTypeDataInstanceInfoContainerLayout);

        VerticalLayout dimensionTypeSummaryInfo=new VerticalLayout();
        dimensionTypeSummaryInfo.setWidth("100%");
        dimensionTypeDataInstanceInfoContainerLayout.addComponent(dimensionTypeSummaryInfo);

        Label dimensionDataCountLabel=new Label("维度类型数据量");
        dimensionDataCountLabel.setWidth("100%");
        dimensionDataCountLabel.addStyleName("h4");
        dimensionDataCountLabel.addStyleName("ui_appSectionDiv");
        dimensionDataCountLabel.addStyleName("ui_appFadeMargin");
        dimensionTypeSummaryInfo.addComponent(dimensionDataCountLabel);

        this.dimensionTypeDataInstanceCountInfoForm = new FormLayout();
        this.dimensionTypeDataInstanceCountInfoForm.setMargin(false);
        this.dimensionTypeDataInstanceCountInfoForm.setWidth("100%");
        this.dimensionTypeDataInstanceCountInfoForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        Panel dataCountFormContainerPanel = new Panel();
        dataCountFormContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        dataCountFormContainerPanel.setWidth("100%");
        dataCountFormContainerPanel.setHeight(dataCountFormPanelHeight,Unit.PIXELS);
        dataCountFormContainerPanel.setContent(this.dimensionTypeDataInstanceCountInfoForm);
        dimensionTypeSummaryInfo.addComponent(dataCountFormContainerPanel);

        this.dimensionTypeDataInstancesInfoChart=new DimensionTypeDataInstancesInfoChart(this.currentUserClientInfo);
        dimensionTypeDataInstanceInfoContainerLayout.addComponent(this.dimensionTypeDataInstancesInfoChart);
        dimensionTypeDataInstanceInfoContainerLayout.setComponentAlignment(this.dimensionTypeDataInstancesInfoChart,Alignment.TOP_LEFT);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderDimensionInstancesManagementInfo(DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        this.dimensionTypeNameLabel.setValue("-");
        this.dimensionCountLabel.setValue("-");
        this.createDimensionInstanceMenuItem.removeChildren();
        this.searchDimensionInstanceMenuItem.removeChildren();
        this.dimensionTypeDataInstanceCountInfoForm.removeAllComponents();
        this.dimensionTypesInfoContainer.removeAllItems();
        this.dimensionTypeDataInstancesInfoChart.cleanChartDisplay();
        this.currentDiscoverSpaceStatisticMetrics=discoverSpaceStatisticMetrics;
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIElementsBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIPromptBox);
        DimensionTypeVO rootDimensionTypeVO= InfoDiscoverSpaceOperationUtil.retrieveRootDimensionTypeRuntimeInfo(this.discoverSpaceName, discoverSpaceStatisticMetrics);
        String rootDimensionTypeItemId="DIMENSION_TYPE_ID";
        Item rootDimensionTypeItem =  this.dimensionTypesInfoContainer.addItem(rootDimensionTypeItemId);
        rootDimensionTypeItem.getItemProperty(NAME_PROPERTY).setValue(rootDimensionTypeVO.getTypeName());
        List<DimensionTypeVO> childDimensionTypesList= rootDimensionTypeVO.getChildDimensionTypesVOList();
        for(int i=0;i<childDimensionTypesList.size();i++){
            DimensionTypeVO currentChildDimensionType=childDimensionTypesList.get(i);
            setDimensionTypesTreeTableData(rootDimensionTypeItemId,i,currentChildDimensionType);
        }
        this.dimensionTypesTreeTable.setCollapsed(rootDimensionTypeItemId, false);
        this.dimensionTypesTreeTable.select(null);
    }

    private void setDimensionTypesTreeTableData(String parentDataKey,int currentDataIndex,DimensionTypeVO currentDimensionTypeVO){
        String currentDataId=parentDataKey+currentDataIndex;
        Item currentDimensionTypeItem =  this.dimensionTypesInfoContainer.addItem(currentDataId);
        currentDimensionTypeItem.getItemProperty(NAME_PROPERTY).setValue(currentDimensionTypeVO.getTypeName());
        this.dimensionTypesInfoContainer.setParent(currentDataId,parentDataKey);
        List<DimensionTypeVO> childDimensionTypesList= currentDimensionTypeVO.getChildDimensionTypesVOList();
        if(childDimensionTypesList.size()==0){
            this.dimensionTypesTreeTable.setChildrenAllowed(currentDataId,false);
            this.dimensionTypesTreeTable.setColumnCollapsible(currentDataId,false);
        }
        for(int i=0;i<childDimensionTypesList.size();i++){
            DimensionTypeVO currentChildDimensionType=childDimensionTypesList.get(i);
            setDimensionTypesTreeTableData(currentDataId,i,currentChildDimensionType);
        }
    }

    private void renderDimensionTypeSelectedUIElements(String dimensionTypeName){
        this.currentSelectedDimensionTypeName = dimensionTypeName;
        this.dimensionTypeNameLabel.setValue(dimensionTypeName);
        this.rightSideUIElementsContainer.removeComponent(this.rightSideUIPromptBox);
        this.rightSideUIElementsContainer.addComponent(this.rightSideUIElementsBox);
        if(dimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            this.dimensionCountLabel.setValue(""+this.currentDiscoverSpaceStatisticMetrics.getSpaceDimensionDataCount());
        }else{
            DataTypeStatisticMetrics dataTypeStatisticMetrics=getDimensionTypeStatisticMetrics(dimensionTypeName);
            this.dimensionCountLabel.setValue(""+dataTypeStatisticMetrics.getTypeDataCount());
        }
        this.setDimensionInstanceOperationsUIElements();
        this.dimensionTypeDataInstancesInfoChart.renderDimensionTypeDataInstancesInfoChart(this.discoverSpaceName,dimensionTypeName,this.currentDiscoverSpaceStatisticMetrics);
    }

    private DataTypeStatisticMetrics getDimensionTypeStatisticMetrics(String dimensionTypeName){
        List<DataTypeStatisticMetrics> dimensionTypesStatisticMetrics=this.currentDiscoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dimensionTypesStatisticMetrics){
            String currentDimensionTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(currentDimensionTypeName.equals(dimensionTypeName)) {
                return currentDataTypeStatisticMetrics;
            }
        }
        return null;
    }

    private void setDimensionInstanceOperationsUIElements(){
        this.createDimensionInstanceMenuItem.removeChildren();
        this.searchDimensionInstanceMenuItem.removeChildren();
        if(this.currentSelectedDimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            DimensionTypeVO currentDimensionTypeVO=InfoDiscoverSpaceOperationUtil.retrieveRootDimensionTypeRuntimeInfo(this.discoverSpaceName,this.currentDiscoverSpaceStatisticMetrics);
            setDimensionInstanceCountInfo(currentDimensionTypeVO);
            List<DimensionTypeVO> childDimensionTypeVOsList=currentDimensionTypeVO.getChildDimensionTypesVOList();
            MenuBar.MenuItem rootSearchDimensionTypeItem = searchDimensionInstanceMenuItem.addItem(this.currentSelectedDimensionTypeName, null, searchDimensionInstanceMenuItemCommand);
            if(childDimensionTypeVOsList!=null){
                for(DimensionTypeVO currentDimensionType:childDimensionTypeVOsList){
                    MenuBar.MenuItem currentCreateDimensionTypeItem = createDimensionInstanceMenuItem.addItem(currentDimensionType.getTypeName(), null, createDimensionInstanceMenuItemCommand);
                    setChildMenuItem(currentCreateDimensionTypeItem,currentDimensionType,createDimensionInstanceMenuItemCommand);
                    MenuBar.MenuItem currentSearchDimensionTypeItem = rootSearchDimensionTypeItem.addItem(currentDimensionType.getTypeName(), null, searchDimensionInstanceMenuItemCommand);
                    setChildMenuItem(currentSearchDimensionTypeItem,currentDimensionType,searchDimensionInstanceMenuItemCommand);
                }
            }
        }else{
            DimensionTypeVO currentDimensionTypeVO=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypeRuntimeInfo(this.discoverSpaceName, this.currentSelectedDimensionTypeName,this.currentDiscoverSpaceStatisticMetrics);
            setDimensionInstanceCountInfo(currentDimensionTypeVO);
            MenuBar.MenuItem currentCreateDimensionTypeItem = createDimensionInstanceMenuItem.addItem(currentDimensionTypeVO.getTypeName(), null, createDimensionInstanceMenuItemCommand);
            setChildMenuItem(currentCreateDimensionTypeItem,currentDimensionTypeVO,createDimensionInstanceMenuItemCommand);
            MenuBar.MenuItem currentSearchDimensionTypeItem = searchDimensionInstanceMenuItem.addItem(currentDimensionTypeVO.getTypeName(), null, searchDimensionInstanceMenuItemCommand);
            setChildMenuItem(currentSearchDimensionTypeItem,currentDimensionTypeVO,searchDimensionInstanceMenuItemCommand);
        }
    }

    private void setChildMenuItem(MenuBar.MenuItem parentMenuItem,DimensionTypeVO parentDimensionTypeVO,MenuBar.Command menuCommand){
        List<DimensionTypeVO> childDimensionTypeVOsList=parentDimensionTypeVO.getChildDimensionTypesVOList();
        if(childDimensionTypeVOsList!=null){
            for(DimensionTypeVO currentDimensionType:childDimensionTypeVOsList){
                MenuBar.MenuItem currentDimensionTypeItem = parentMenuItem.addItem(currentDimensionType.getTypeName(), null, menuCommand);
                setChildMenuItem(currentDimensionTypeItem,currentDimensionType,menuCommand);
            }
        }
    }

    private void setDimensionInstanceCountInfo(DimensionTypeVO targetDimensionTypeVO){
        this.dimensionTypeDataInstanceCountInfoForm.removeAllComponents();
        if(targetDimensionTypeVO.getTypeName().equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
            List<DimensionTypeVO> childTypeVOList=targetDimensionTypeVO.getChildDimensionTypesVOList();
            if(childTypeVOList!=null){
                for(DimensionTypeVO currentDimensionTypeVO:childTypeVOList){
                    TextField currentDimensionTypeDataSize = new TextField(currentDimensionTypeVO.getTypeName());
                    currentDimensionTypeDataSize.setValue(""+currentDimensionTypeVO.getTypeDataRecordCount());
                    currentDimensionTypeDataSize.setRequired(false);
                    currentDimensionTypeDataSize.setReadOnly(true);
                    this.dimensionTypeDataInstanceCountInfoForm.addComponent(currentDimensionTypeDataSize);
                }
            }
        }else{
            long parentDimensionTypeTotalCount=targetDimensionTypeVO.getTypeDataRecordCount();
            List<DimensionTypeVO> childTypeVOList=targetDimensionTypeVO.getChildDimensionTypesVOList();
            if(childTypeVOList!=null){
                for(DimensionTypeVO currentDimensionTypeVO:childTypeVOList){
                    TextField currentDimensionTypeDataSize = new TextField(currentDimensionTypeVO.getTypeName());
                    currentDimensionTypeDataSize.setValue(""+currentDimensionTypeVO.getTypeDataRecordCount());
                    currentDimensionTypeDataSize.setRequired(false);
                    currentDimensionTypeDataSize.setReadOnly(true);
                    this.dimensionTypeDataInstanceCountInfoForm.addComponent(currentDimensionTypeDataSize);
                    parentDimensionTypeTotalCount=parentDimensionTypeTotalCount-currentDimensionTypeVO.getTypeDataRecordCount();
                }
            }
            TextField currentDimensionTypeDataSize = new TextField(targetDimensionTypeVO.getTypeName());
            currentDimensionTypeDataSize.setValue(""+parentDimensionTypeTotalCount);
            currentDimensionTypeDataSize.setRequired(false);
            currentDimensionTypeDataSize.setReadOnly(true);
            this.dimensionTypeDataInstanceCountInfoForm.addComponent(currentDimensionTypeDataSize);
        }
    }

    private void executeCreateDimensionOperation(String dimensionType){
        CreateTypeDataInstancePanel createTypeDataInstancePanel=new CreateTypeDataInstancePanel(this.currentUserClientInfo);
        createTypeDataInstancePanel.setDiscoverSpaceName(this.getDiscoverSpaceName());
        createTypeDataInstancePanel.setDataInstanceTypeName(dimensionType);
        createTypeDataInstancePanel.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        final Window window = new Window();
        window.setWidth(550.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(createTypeDataInstancePanel);
        createTypeDataInstancePanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    private void executeSearchDimensionTypeOperation(String dimensionType){
        DiscoverSpaceTypeDataInstanceQueryRequiredEvent currentQueryEvent=new DiscoverSpaceTypeDataInstanceQueryRequiredEvent();
        currentQueryEvent.setDiscoverSpaceName(this.getDiscoverSpaceName());
        currentQueryEvent.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        currentQueryEvent.setDataInstanceTypeName(dimensionType);
        this.currentUserClientInfo.getEventBlackBoard().fire(currentQueryEvent);
    }
}

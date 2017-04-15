package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by wangychu on 4/13/17.
 */
public class CompareInfoOfManyAnalyzingDataPanel extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private VerticalLayout dataCheckboxFormLayout;
    private VerticalLayout propertyCheckboxFormLayout;
    private Map<CheckBox,String> analyzingDataCheckBoxMap;
    private Map<CheckBox,String> analyzingPropertyCheckBoxMap;
    private Map<String,List<PropertyValueVO>> dataPropertiesValueListMap;
    private List<String> globalMeasurablePropertiesNameList;
    private MenuBar.MenuItem visualizationAnalyzeRootItem;
    private Table dataPropertiesValueCompareTable;
    private BrowserFrame dataCompareChartBrowserFrame;

    public CompareInfoOfManyAnalyzingDataPanel(UserClientInfo userClientInfo,String discoverSpaceName){
        this.setMargin(true);
        this.setSpacing(true);
        this.setWidth(100,Unit.PERCENTAGE);
        this.setHeight(800,Unit.PIXELS);
        this.currentUserClientInfo = userClientInfo;
        this.discoverSpaceName=discoverSpaceName;
        this.analyzingDataCheckBoxMap=new HashMap<>();
        this.analyzingPropertyCheckBoxMap=new HashMap<>();
        this.dataPropertiesValueListMap=new HashMap<>();
        this.globalMeasurablePropertiesNameList=new ArrayList<>();

        VerticalLayout dataInfoLayout=new VerticalLayout();
        dataInfoLayout.setHeight(100,Unit.PERCENTAGE);
        dataInfoLayout.setWidth(180,Unit.PIXELS);

        this.addComponent(dataInfoLayout);

        Label analyzeDataSelectorsTitle= new Label(FontAwesome.SQUARE_O.getHtml() +" 选择待比较数据", ContentMode.HTML);
        analyzeDataSelectorsTitle.addStyleName(ValoTheme.LABEL_COLORED);
        analyzeDataSelectorsTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeDataSelectorsTitle.setWidth(100,Unit.PERCENTAGE);
        dataInfoLayout.addComponent(analyzeDataSelectorsTitle);

        Panel analyzeDataSelectorsPanel=new Panel();
        analyzeDataSelectorsPanel.setWidth(100,Unit.PERCENTAGE);
        analyzeDataSelectorsPanel.setHeight(200,Unit.PIXELS);
        dataInfoLayout.addComponent(analyzeDataSelectorsPanel);

        dataCheckboxFormLayout=new VerticalLayout();
        dataCheckboxFormLayout.setMargin(true);
        analyzeDataSelectorsPanel.setContent(dataCheckboxFormLayout);

        VerticalLayout spacingDivLayout0=new VerticalLayout();
        spacingDivLayout0.setHeight(5,Unit.PIXELS);
        dataInfoLayout.addComponent(spacingDivLayout0);

        Label analyzePropertySelectorsTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 选择待比较属性", ContentMode.HTML);
        analyzePropertySelectorsTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzePropertySelectorsTitle.addStyleName(ValoTheme.LABEL_COLORED);
        analyzePropertySelectorsTitle.setWidth(100,Unit.PERCENTAGE);
        dataInfoLayout.addComponent(analyzePropertySelectorsTitle);

        Panel analyzePropertySelectorsPanel=new Panel();
        analyzePropertySelectorsPanel.setWidth(100,Unit.PERCENTAGE);
        analyzePropertySelectorsPanel.setHeight(450,Unit.PIXELS);
        dataInfoLayout.addComponent(analyzePropertySelectorsPanel);

        propertyCheckboxFormLayout=new VerticalLayout();
        propertyCheckboxFormLayout.setMargin(true);
        analyzePropertySelectorsPanel.setContent(propertyCheckboxFormLayout);

        VerticalLayout spacingDivLayout01=new VerticalLayout();
        spacingDivLayout01.setHeight(5,Unit.PIXELS);
        dataInfoLayout.addComponent(spacingDivLayout01);

        HorizontalLayout actionButtonsContainerLayout=new HorizontalLayout();
        MenuBar graphDisplaySelectorMenuBar = new MenuBar();
        graphDisplaySelectorMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

        MenuBar.Command visualizationAnalyzeMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedCommandName=selectedItem.getText();
                compareSelectedDataProperties(selectedCommandName);
            }
        };

        visualizationAnalyzeRootItem=graphDisplaySelectorMenuBar.addItem("比较已选数据", VaadinIcons.CHART_GRID, null);
        visualizationAnalyzeRootItem.addItem("折线图比较", FontAwesome.LINE_CHART, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem("雷达图比较", FontAwesome.CROSSHAIRS, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem("面积图比较", VaadinIcons.SPLINE_AREA_CHART, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.addItem("柱状图比较", VaadinIcons.BAR_CHART, visualizationAnalyzeMenuItemCommand);
        visualizationAnalyzeRootItem.setEnabled(false);

        actionButtonsContainerLayout.addComponent(graphDisplaySelectorMenuBar);

        Button clearSelectedData=new Button();
        clearSelectedData.setIcon(FontAwesome.ERASER);
        clearSelectedData.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        clearSelectedData.setDescription("清除已选待比较数据");
        clearSelectedData.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                clearDataListForCompare();
            }
        });

        actionButtonsContainerLayout.addComponent(clearSelectedData);
        dataInfoLayout.addComponent(actionButtonsContainerLayout);

        dataInfoLayout.setExpandRatio(actionButtonsContainerLayout,1);

        VerticalLayout dataPropertyTableLayout=new VerticalLayout();
        dataPropertyTableLayout.setHeight(750,Unit.PIXELS);
        dataPropertyTableLayout.setWidth(600,Unit.PIXELS);
        this.addComponent(dataPropertyTableLayout);

        Label dataPropertyTableTitle= new Label(VaadinIcons.TABLE.getHtml() +" 待比较数据属性值", ContentMode.HTML);
        //dataPropertyTableTitle.addStyleName(ValoTheme.LABEL_COLORED);
        dataPropertyTableTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataPropertyTableTitle.setWidth(100,Unit.PERCENTAGE);
        dataPropertyTableLayout.addComponent(dataPropertyTableTitle);

        dataPropertiesValueCompareTable=new Table();
        //dataPropertiesValueCompareTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        dataPropertiesValueCompareTable.addStyleName(ValoTheme.TABLE_SMALL);
        dataPropertiesValueCompareTable.setWidth(600,Unit.PIXELS);
        dataPropertiesValueCompareTable.setHeight(750,Unit.PIXELS);
        dataPropertyTableLayout.addComponent(dataPropertiesValueCompareTable);
        dataPropertyTableLayout.setExpandRatio(dataPropertiesValueCompareTable,1);

        VerticalLayout dataDetailLayout=new VerticalLayout();
        dataDetailLayout.setHeight(100,Unit.PERCENTAGE);
        this.addComponent(dataDetailLayout);

        this.setExpandRatio(dataDetailLayout,1);

        //Label xx1=new Label("CompareInfoOfManyAnalyzingDataPanel2");
        //dataDetailLayout.addComponent(xx1);



        //typeInstanceRelationsDetailGraphQueryAddress= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+
        //        "infoAnalysePages/typeInstanceRelationAnalyse/typeInstanceRelationsExploreGraph.html?dataInstanceId="+dataInstanceQueryId+"&discoverSpace="+discoverSpaceName+"&timestamp="+timeStampPostValue;
        dataCompareChartBrowserFrame = new BrowserFrame();
        dataCompareChartBrowserFrame.setSizeFull();
        dataCompareChartBrowserFrame.setHeight(800,Unit.PIXELS);
        //int relationsCycleGraphHeight=dataRelationGraphBrowserFrameHeight-20;
        //dataCompareChartBrowserFrame.setSource(new ExternalResource());

        dataDetailLayout.addComponent(dataCompareChartBrowserFrame);



    }

    public void addDataForCompare(ProcessingDataVO targetProcessingData){
        if(this.dataPropertiesValueListMap.get(targetProcessingData.getId())!=null){
            return;
        }
        visualizationAnalyzeRootItem.setEnabled(true);
        CheckBox dataSelectCheckBox = new CheckBox(targetProcessingData.getId());
        dataSelectCheckBox.setDescription(targetProcessingData.getDataTypeName());
        dataSelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
        dataCheckboxFormLayout.addComponent(dataSelectCheckBox);
        analyzingDataCheckBoxMap.put(dataSelectCheckBox,targetProcessingData.getId());

        List<PropertyValueVO> propertyValueList= InfoDiscoverSpaceOperationUtil.getMeasurablePropertiesById(this.discoverSpaceName,targetProcessingData.getId());
        this.dataPropertiesValueListMap.put(targetProcessingData.getId(),propertyValueList);
        List<String> currentMeasurableProperties=getMeasurablePropertieNameList(propertyValueList);

        for(String currentPropertyName:currentMeasurableProperties){
            if(!this.globalMeasurablePropertiesNameList.contains(currentPropertyName)){
                this.globalMeasurablePropertiesNameList.add(currentPropertyName);
                CheckBox propertySelectCheckBox = new CheckBox(currentPropertyName);
                propertySelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
                propertyCheckboxFormLayout.addComponent(propertySelectCheckBox);
                analyzingPropertyCheckBoxMap.put(propertySelectCheckBox,currentPropertyName);
            }
        }
    }

    private void clearDataListForCompare(){
        visualizationAnalyzeRootItem.setEnabled(false);
        this.analyzingDataCheckBoxMap.clear();
        this.dataCheckboxFormLayout.removeAllComponents();
        this.analyzingPropertyCheckBoxMap.clear();
        this.propertyCheckboxFormLayout.removeAllComponents();
        this.globalMeasurablePropertiesNameList.clear();
        this.dataPropertiesValueListMap.clear();

        Container dataContainer=this.dataPropertiesValueCompareTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.dataPropertiesValueCompareTable.setContainerDataSource(queryResultDataContainer);
    }

    private List<String> getMeasurablePropertieNameList( List<PropertyValueVO> measurablePropertiesList){
        List<String> measurablePropertiesNameList=new ArrayList<>();
        if(measurablePropertiesList!=null){
            for(PropertyValueVO currentPropertyValueVO:measurablePropertiesList){
                if((""+ PropertyType.INT).equals(currentPropertyValueVO.getPropertyType())||
                        (""+PropertyType.SHORT).equals(currentPropertyValueVO.getPropertyType())||
                        (""+PropertyType.LONG).equals(currentPropertyValueVO.getPropertyType())||
                        (""+PropertyType.FLOAT).equals(currentPropertyValueVO.getPropertyType())||
                        (""+PropertyType.DOUBLE).equals(currentPropertyValueVO.getPropertyType())
                        ){
                    measurablePropertiesNameList.add(currentPropertyValueVO.getPropertyName());
                }
            }
        }
        return measurablePropertiesNameList;
    }

    private void compareSelectedDataProperties(String commandName){
        List<String> selectedDataItem=new ArrayList<>();
        Set<CheckBox> dataCheckboxSet=analyzingDataCheckBoxMap.keySet();
        Iterator<CheckBox> dataCheckboxIterator=dataCheckboxSet.iterator();
        while(dataCheckboxIterator.hasNext()){
            CheckBox currentCheckBox=dataCheckboxIterator.next();
            if(currentCheckBox.getValue()){
                selectedDataItem.add(analyzingDataCheckBoxMap.get(currentCheckBox));
            }
        }
        if(selectedDataItem.size()==0){
            Notification errorNotification = new Notification("数据校验错误","请选择至少一项待比较数据", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        List<String> selectedPropertyItem=new ArrayList<>();
        Set<CheckBox> propertyCheckboxSet=analyzingPropertyCheckBoxMap.keySet();
        Iterator<CheckBox> propertyCheckboxIterator=propertyCheckboxSet.iterator();
        while(propertyCheckboxIterator.hasNext()){
            CheckBox currentCheckBox=propertyCheckboxIterator.next();
            if(currentCheckBox.getValue()){
                selectedPropertyItem.add(analyzingPropertyCheckBoxMap.get(currentCheckBox));
            }
        }
        if(selectedPropertyItem.size()==0){
            Notification errorNotification = new Notification("数据校验错误","请选择至少一项待比较属性", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        renderCompareDataPropertiesTable(selectedDataItem,selectedPropertyItem);
        renderCompareDataPropertiesChart(selectedDataItem,selectedPropertyItem,commandName);
    }

    private void renderCompareDataPropertiesTable(List<String> dataIdList,List<String> dataPropertiesNameList){
        Container dataContainer=this.dataPropertiesValueCompareTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.dataPropertiesValueCompareTable.setContainerDataSource(queryResultDataContainer);

        this.dataPropertiesValueCompareTable.addContainerProperty("数据ID",String.class,"");
        this.dataPropertiesValueCompareTable.setColumnIcon("数据ID",FontAwesome.KEY);
        if(dataPropertiesNameList!=null){
            for(String currentPropertyName:dataPropertiesNameList){
                this.dataPropertiesValueCompareTable.addContainerProperty(currentPropertyName, String.class, "");
            }
        }

        if(dataIdList!=null){
            for(int i=0;i<dataIdList.size();i++){
                String dataItemId=dataIdList.get(i);
                Item newRecord=this.dataPropertiesValueCompareTable.addItem("typeInstanceForCompare_index_"+i);
                newRecord.getItemProperty("数据ID").setValue(dataItemId);
                List<PropertyValueVO> currentPropertyValueList=this.dataPropertiesValueListMap.get(dataItemId);
                if(dataPropertiesNameList!=null){
                    for(String currentPropertyName:dataPropertiesNameList){
                        String currentPropertyValue=getPropertyValueString(currentPropertyName,currentPropertyValueList);
                        if(currentPropertyValue!=null){
                            newRecord.getItemProperty(currentPropertyName).setValue(currentPropertyValue);
                        }
                    }
                }
            }
        }
    }

    private String getPropertyValueString(String propertyName,List<PropertyValueVO> propertyValueList){
        for(PropertyValueVO currentPropertyValueVO:propertyValueList){
            if(currentPropertyValueVO.getPropertyName().equals(propertyName)){
                return currentPropertyValueVO.getPropertyValue().toString();
            }
        }
        return null;
    }

    private void renderCompareDataPropertiesChart(List<String> dataIdList,List<String> dataPropertiesNameList,String chartType){
        System.out.println(dataIdList);
        System.out.println(dataPropertiesNameList);
        System.out.println(chartType);
        this.dataCompareChartBrowserFrame.setSource(new ExternalResource("http://localhost:8080/infoAnalysePages/typeInstancesDataAnalyse/measurableInstanceDataExhibition_highcharts_LineChart.html?discoverSpace=chartDS&graphHeight=700&measurableIds=%2341%3a0,%2341%3a1,%2341%3a2&lineProperties=int01,long01,float03,double01,int02,double02,double03,long02,long03&chartType=polar"));
    }
}

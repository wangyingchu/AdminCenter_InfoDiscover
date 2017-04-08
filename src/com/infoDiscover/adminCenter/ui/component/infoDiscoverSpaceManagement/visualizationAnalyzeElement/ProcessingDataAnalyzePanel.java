package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.TypeDataInstanceDetailPanel;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 4/5/17.
 */
public class ProcessingDataAnalyzePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Map<String,List<ProcessingDataVO>> processingDataMapForAnalyzing;
    private List<ProcessingDataVO> processingDimensionsForAnalyzing;
    private List<ProcessingDataVO> processingFactsForAnalyzing;
    private List<ProcessingDataVO> processingRelationsForAnalyzing;

    private final static String DataTypeName_PROPERTY="DataTypeName_PROPERTY";
    private final static String Id_PROPERTY="Id_PROPERTY";
    private final static String DataTypeKind_PROPERTY="DataTypeKind_PROPERTY";
    private final static String DiscoverSpaceName_PROPERTY="DiscoverSpaceName_PROPERTY";

    private final static String exploreRelatedInfoActionName = "探索本数据项关联的数据信息";
    private final static String findRelationInfoOfTwoItemActionName = "发现本数据项与另一数据项的关联信息";
    private final static String compareInfoOfManyItemsActionName = "比较本数据项与其他数据项的属性信息";
    private final static String showDataDetailInfoActionName="显示数据详情";
    private final static String exploreRelatedInfoTabNamePerfix="关联数据探索-";

    private TabSheet dataAnalyzePageTabs;
    private Map<String,TabSheet.Tab> exploreRelatedInfoActionLayoutTabMap;

    public ProcessingDataAnalyzePanel(UserClientInfo userClientInfo,String discoverSpaceNam,Map<String,List<ProcessingDataVO>> processingDataMap){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        this.discoverSpaceName=discoverSpaceNam;
        this.processingDataMapForAnalyzing=processingDataMap;

        this.processingDimensionsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        this.processingFactsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        this.processingRelationsForAnalyzing=this.processingDataMapForAnalyzing.get(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);

        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(new Label(FontAwesome.CUBE.getHtml()+" "+this.discoverSpaceName, ContentMode.HTML));
        addComponent(dataTypeNoticeActionsBar);

        HorizontalSplitPanel visualizationAnalyzeSplitPanel = new HorizontalSplitPanel();
        visualizationAnalyzeSplitPanel.setSizeFull();
        visualizationAnalyzeSplitPanel.setSplitPosition(170, Unit.PIXELS);
        addComponent(visualizationAnalyzeSplitPanel);

        //Left side
        VerticalLayout processingDataTreesContainer=new VerticalLayout();

        int browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        processingDataTreesContainer.setHeight(browserWindowHeight-110,Unit.PIXELS);
        processingDataTreesContainer.setWidth(100,Unit.PERCENTAGE);
        processingDataTreesContainer.setMargin(false);
        processingDataTreesContainer.setSpacing(false);

        Label analyzeDataSelectorsTitle= new Label(FontAwesome.LIST_UL.getHtml() +" 待分析数据", ContentMode.HTML);
        analyzeDataSelectorsTitle.addStyleName(ValoTheme.LABEL_TINY);
        analyzeDataSelectorsTitle.addStyleName("ui_appSectionLightDiv");
        processingDataTreesContainer.addComponent(analyzeDataSelectorsTitle);

        VerticalLayout processingDataSelectorContainerLayout=new VerticalLayout();
        processingDataSelectorContainerLayout.setWidth(100,Unit.PERCENTAGE);

        Panel processingDataSelectorContainerPanel=new Panel();
        processingDataSelectorContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        processingDataSelectorContainerPanel.setContent(processingDataSelectorContainerLayout);
        processingDataSelectorContainerPanel.setHeight(browserWindowHeight-140,Unit.PIXELS);
        processingDataTreesContainer.addComponent(processingDataSelectorContainerPanel);
        processingDataTreesContainer.setExpandRatio(processingDataSelectorContainerPanel,1f);

        Tree processingDataTree = new Tree();
        processingDataTree.addContainerProperty(DataTypeName_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(Id_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(DataTypeKind_PROPERTY, String.class, "");
        processingDataTree.addContainerProperty(DiscoverSpaceName_PROPERTY, String.class, "");

        processingDataTree.setSelectable(false);
        processingDataSelectorContainerLayout.addComponent(processingDataTree);

        AbstractSelect.ItemDescriptionGenerator itemDescriptionGenerator=new AbstractSelect.ItemDescriptionGenerator() {
            @Override
            public String generateDescription(Component component, Object itemId, Object o1) {
                return getProcessingDataDescription(itemId.toString());
            }
        };
        processingDataTree.setItemDescriptionGenerator(itemDescriptionGenerator);

        String factDataRootItemId="processingDataInstance_Fact";
        String dimensionDataRootItemId="processingDataInstance_Dimension";
        String relationDataRootItemId="processingDataInstance_Relation";

        processingDataTree.addItem(factDataRootItemId);
        processingDataTree.setItemCaption(factDataRootItemId,"事实数据");
        processingDataTree.setItemIcon(factDataRootItemId,FontAwesome.CLONE);

        processingDataTree.addItem(dimensionDataRootItemId);
        processingDataTree.setItemCaption(dimensionDataRootItemId,"维度数据");
        processingDataTree.setItemIcon(dimensionDataRootItemId,FontAwesome.TAGS);

        processingDataTree.addItem(relationDataRootItemId);
        processingDataTree.setItemCaption(relationDataRootItemId,"关系数据");
        processingDataTree.setItemIcon(relationDataRootItemId,FontAwesome.SHARE_ALT);

        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            Item newRecord=processingDataTree.addItem(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
            newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
            newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
            processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
            processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.SQUARE_O);
            processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
            processingDataTree.setParent(currentFactVO.getId(),factDataRootItemId);
        }

        for(ProcessingDataVO currentFactVO:this.processingDimensionsForAnalyzing){
            Item newRecord=processingDataTree.addItem(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
            newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
            newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
            newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
            processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
            processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.TAG);
            processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
            processingDataTree.setParent(currentFactVO.getId(),dimensionDataRootItemId);
        }

        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentFactVO:this.processingRelationsForAnalyzing){
                Item newRecord=processingDataTree.addItem(currentFactVO.getId());
                newRecord.getItemProperty(DataTypeName_PROPERTY).setValue(currentFactVO.getDataTypeName());
                newRecord.getItemProperty(Id_PROPERTY).setValue(currentFactVO.getId());
                newRecord.getItemProperty(DataTypeKind_PROPERTY).setValue(currentFactVO.getDataTypeKind());
                newRecord.getItemProperty(DiscoverSpaceName_PROPERTY).setValue(currentFactVO.getDiscoverSpaceName());
                processingDataTree.setItemCaption(currentFactVO.getId(),currentFactVO.getId());
                processingDataTree.setItemIcon(currentFactVO.getId(),FontAwesome.SHARE_ALT_SQUARE);
                processingDataTree.setChildrenAllowed(currentFactVO.getId(),false);
                processingDataTree.setParent(currentFactVO.getId(),relationDataRootItemId);
            }
        }

        processingDataTree.expandItem(factDataRootItemId);
        processingDataTree.expandItem(dimensionDataRootItemId);
        processingDataTree.expandItem(relationDataRootItemId);

        processingDataTree.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object itemId, Object o1) {
                if(itemId==null||itemId.equals(factDataRootItemId)||itemId.equals(dimensionDataRootItemId)||itemId.equals(relationDataRootItemId)){
                    return new Action[0];
                }
                String dataKind=getProcessingDataKind(itemId.toString());
                if(dataKind==null){
                    return new Action[0];
                }
                Action showDataDetailInfoAction = new Action(showDataDetailInfoActionName, FontAwesome.EYE);
                Action exploreRelatedInfoAction = new Action(exploreRelatedInfoActionName, VaadinIcons.CLUSTER);
                Action findRelationInfoOfTwoItemAction = new Action(findRelationInfoOfTwoItemActionName, VaadinIcons.SPECIALIST);
                Action compareInfoOfManyItemsAction = new Action(compareInfoOfManyItemsActionName, VaadinIcons.SCALE_UNBALANCE);

                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataKind)){
                    return new Action[]{exploreRelatedInfoAction,findRelationInfoOfTwoItemAction,compareInfoOfManyItemsAction,showDataDetailInfoAction};
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataKind)){
                    return new Action[]{exploreRelatedInfoAction,showDataDetailInfoAction};
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataKind)){
                    return new Action[]{showDataDetailInfoAction};
                }
                return new Action[0];
            }

            @Override
            public void handleAction(Action action, Object o, Object itemId) {
                executeDataAnalyzeLogic(action.getCaption(),itemId.toString());
            }
        });
        VerticalLayout spacingDivLayout1=new VerticalLayout();
        spacingDivLayout1.setHeight(10,Unit.PIXELS);
        processingDataSelectorContainerLayout.addComponent(spacingDivLayout1);

        visualizationAnalyzeSplitPanel.setFirstComponent(processingDataTreesContainer);
        //Right side
        this.dataAnalyzePageTabs=new TabSheet();
        visualizationAnalyzeSplitPanel.setSecondComponent(dataAnalyzePageTabs);

        TabSheet.Tab findRelationInfoOfTwoItemActionLayoutTab =dataAnalyzePageTabs.addTab(new VerticalLayout(), "两项数据间关联关系发现");
        findRelationInfoOfTwoItemActionLayoutTab.setIcon(VaadinIcons.SPECIALIST);

        TabSheet.Tab compareInfoOfManyItemsActionLayoutTab =dataAnalyzePageTabs.addTab(new VerticalLayout(), "多项数据间属性值比较");
        compareInfoOfManyItemsActionLayoutTab.setIcon(VaadinIcons.SCALE_UNBALANCE);

        /*
        this.dataAnalyzePageTabs.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent selectedTabChangeEvent) {
                Component currentSelectedComponent=selectedTabChangeEvent.getTabSheet().getSelectedTab();
                if(currentSelectedComponent instanceof ExploreProcessingDataRelatedInfoPanel){}
            }
        });
        */
        this.dataAnalyzePageTabs.setCloseHandler(new TabSheet.CloseHandler() {
            @Override
            public void onTabClose(TabSheet tabSheet, Component component) {
                if(component instanceof ExploreProcessingDataRelatedInfoPanel){
                    ExploreProcessingDataRelatedInfoPanel currentPanel=(ExploreProcessingDataRelatedInfoPanel)component;
                    String currentTabNameKey=exploreRelatedInfoTabNamePerfix+currentPanel.getProcessingData().getId();
                    exploreRelatedInfoActionLayoutTabMap.remove(currentTabNameKey);
                    tabSheet.removeComponent(component);
                }
            }
        });

        this.exploreRelatedInfoActionLayoutTabMap=new HashMap<>();
    }

    private String getProcessingDataKind(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT;
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION;
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION;
                }
            }
        }
        return null;
    }

    private String getProcessingDataDescription(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return currentFactVO.getDataTypeName()+"<br/>"+currentFactVO.getId();
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return currentDimensionVO.getDataTypeName()+"<br/>"+currentDimensionVO.getId();
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return currentRelationVO.getDataTypeName()+"<br/>"+currentRelationVO.getId();
                }
            }
        }
        return null;
    }

    private ProcessingDataVO getProcessingData(String dataId){
        for(ProcessingDataVO currentFactVO:this.processingFactsForAnalyzing){
            if(currentFactVO.getId().equals(dataId)){
                return currentFactVO;
            }
        }
        for(ProcessingDataVO currentDimensionVO:this.processingDimensionsForAnalyzing){
            if(currentDimensionVO.getId().equals(dataId)){
                return currentDimensionVO;
            }
        }
        if(this.processingRelationsForAnalyzing!=null){
            for(ProcessingDataVO currentRelationVO:this.processingRelationsForAnalyzing){
                if(currentRelationVO.getId().equals(dataId)){
                    return currentRelationVO;
                }
            }
        }
        return null;
    }

    private void executeDataAnalyzeLogic(String analyzeCommandName,String dataItemId){
        ProcessingDataVO targetProcessingDataVO=getProcessingData(dataItemId);
        if(targetProcessingDataVO==null){
            return;
        }
        if(exploreRelatedInfoActionName.equals(analyzeCommandName)){
            String tabCaption=exploreRelatedInfoTabNamePerfix+dataItemId;
            TabSheet.Tab alreadyExistTab=this.exploreRelatedInfoActionLayoutTabMap.get(tabCaption);
            if(alreadyExistTab!=null){
                dataAnalyzePageTabs.setSelectedTab(alreadyExistTab);
            }else{
                ExploreProcessingDataRelatedInfoPanel exploreProcessingDataRelatedInfoPanel=new ExploreProcessingDataRelatedInfoPanel(this.currentUserClientInfo,targetProcessingDataVO);
                TabSheet.Tab exploreRelatedInfoActionLayoutTab = dataAnalyzePageTabs.addTab(exploreProcessingDataRelatedInfoPanel, exploreRelatedInfoTabNamePerfix+dataItemId);
                exploreRelatedInfoActionLayoutTab.setClosable(true);
                exploreRelatedInfoActionLayoutTab.setIcon(VaadinIcons.CLUSTER);
                dataAnalyzePageTabs.setSelectedTab(exploreRelatedInfoActionLayoutTab);
                this.exploreRelatedInfoActionLayoutTabMap.put(tabCaption,exploreRelatedInfoActionLayoutTab);
            }
        }else if(showDataDetailInfoActionName.equals(analyzeCommandName)){
            showDataDetailInfoPanel(targetProcessingDataVO);
        }
    }

    private void showDataDetailInfoPanel(ProcessingDataVO targetProcessingDataVO){
        String discoverSpaceName=targetProcessingDataVO.getDiscoverSpaceName();
        String dataTypeName=targetProcessingDataVO.getDataTypeName();
        String dataId=targetProcessingDataVO.getId();
        String targetWindowUID=discoverSpaceName+"_GlobalDataInstanceDetailWindow_"+dataTypeName+"_"+dataId;
        Window targetWindow=this.currentUserClientInfo.getRuntimeWindowsRepository().getExistingWindow(discoverSpaceName,targetWindowUID);
        if(targetWindow!=null){
            targetWindow.bringToFront();
            targetWindow.center();
        }else{
            String dataTypeKind= targetProcessingDataVO.getDataTypeKind();
            String dataDetailInfoTitle;
            if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION)){
                dataDetailInfoTitle="维度数据详细信息";
            }
            else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT)){
                dataDetailInfoTitle="事实数据详细信息";
            }
            else if(dataTypeKind.equals(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION)){
                dataDetailInfoTitle="关系数据详细信息";
            }else{
                dataDetailInfoTitle="数据详细信息";
            }

            MeasurableValueVO targetMeasurableValueVO=new MeasurableValueVO();
            targetMeasurableValueVO.setId(targetProcessingDataVO.getId());
            targetMeasurableValueVO.setDiscoverSpaceName(targetProcessingDataVO.getDiscoverSpaceName());
            targetMeasurableValueVO.setMeasurableTypeKind(targetProcessingDataVO.getDataTypeKind());
            targetMeasurableValueVO.setMeasurableTypeName(targetProcessingDataVO.getDataTypeName());

            TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,targetMeasurableValueVO);
            final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(570, Unit.PIXELS);
            window.setHeight(800,Unit.PIXELS);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.setContent(typeDataInstanceDetailPanel);
            typeDataInstanceDetailPanel.setContainerDialog(window);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow(discoverSpaceName,targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow(discoverSpaceName,targetWindowUID,window);
            UI.getCurrent().addWindow(window);
        }
    }
}

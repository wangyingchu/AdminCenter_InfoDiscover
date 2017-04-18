package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 4/17/17.
 */
public class ExploreProcessingDataSimilarInfoPanel extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private ProcessingDataVO processingData;
    private int browserWindowHeight;
    private VerticalLayout dimensionCheckboxFormLayout;
    private Table similarDataInfoTable;
    private BrowserFrame similarDataInfoBrowserFrame;
    private Map<CheckBox,String> analyzingDimensionCheckBoxMap;

    public ExploreProcessingDataSimilarInfoPanel(UserClientInfo userClientInfo,ProcessingDataVO processingData){
        this.setMargin(true);
        this.setSpacing(true);
        this.currentUserClientInfo = userClientInfo;
        this.processingData=processingData;
        this.setWidth(100,Unit.PERCENTAGE);
        browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        this.setHeight(browserWindowHeight-150,Unit.PIXELS);

        VerticalLayout dataRelatedDimensionInfoLayout=new VerticalLayout();
        dataRelatedDimensionInfoLayout.setHeight(100,Unit.PERCENTAGE);
        dataRelatedDimensionInfoLayout.setWidth(170,Unit.PIXELS);
        this.addComponent(dataRelatedDimensionInfoLayout);

        Label analyzeDataDimensionSelectorsTitle= new Label(FontAwesome.TAG.getHtml() +" 选择相似数据关联的维度", ContentMode.HTML);
        analyzeDataDimensionSelectorsTitle.addStyleName(ValoTheme.LABEL_COLORED);
        analyzeDataDimensionSelectorsTitle.addStyleName(ValoTheme.LABEL_SMALL);
        analyzeDataDimensionSelectorsTitle.setWidth(100,Unit.PERCENTAGE);
        dataRelatedDimensionInfoLayout.addComponent(analyzeDataDimensionSelectorsTitle);

        Panel analyzeDataDimensionSelectorsPanel=new Panel();
        analyzeDataDimensionSelectorsPanel.setWidth(170,Unit.PERCENTAGE);
        analyzeDataDimensionSelectorsPanel.setHeight(browserWindowHeight-250,Unit.PIXELS);
        dataRelatedDimensionInfoLayout.addComponent(analyzeDataDimensionSelectorsPanel);

        dimensionCheckboxFormLayout=new VerticalLayout();
        dimensionCheckboxFormLayout.setMargin(true);
        analyzeDataDimensionSelectorsPanel.setContent(dimensionCheckboxFormLayout);

        dataRelatedDimensionInfoLayout.setExpandRatio(analyzeDataDimensionSelectorsPanel,1);

        HorizontalLayout actionButtonsContainerLayout=new HorizontalLayout();
        dataRelatedDimensionInfoLayout.addComponent(actionButtonsContainerLayout);

        Button exploreSimilarDataButton=new Button("探索相似数据");
        exploreSimilarDataButton.setIcon(VaadinIcons.SEARCH);
        actionButtonsContainerLayout.addComponent(exploreSimilarDataButton);

        VerticalLayout spacingDivLayout0=new VerticalLayout();
        spacingDivLayout0.setHeight(100,Unit.PERCENTAGE);
        spacingDivLayout0.setWidth(100,Unit.PIXELS);
        this.addComponent(spacingDivLayout0);

        VerticalLayout similarDataTableLayout=new VerticalLayout();
        similarDataTableLayout.setHeight(100,Unit.PERCENTAGE);
        similarDataTableLayout.setWidth(400,Unit.PIXELS);
        this.addComponent(similarDataTableLayout);

        Label similarDataTableTitle= new Label(VaadinIcons.TABLE.getHtml() +" 相似的数据", ContentMode.HTML);
        similarDataTableTitle.addStyleName(ValoTheme.LABEL_SMALL);
        similarDataTableTitle.setWidth(100,Unit.PERCENTAGE);
        similarDataTableLayout.addComponent(similarDataTableTitle);

        similarDataInfoTable=new Table();
        similarDataInfoTable.addStyleName(ValoTheme.TABLE_SMALL);
        similarDataInfoTable.setWidth(400,Unit.PIXELS);
        similarDataInfoTable.setHeight(browserWindowHeight-200,Unit.PIXELS);
        similarDataTableLayout.addComponent(similarDataInfoTable);
        similarDataTableLayout.setExpandRatio(similarDataInfoTable,1);

        VerticalLayout dataDetailGraphLayout=new VerticalLayout();
        dataDetailGraphLayout.setHeight(100,Unit.PERCENTAGE);
        dataDetailGraphLayout.setWidth(100,Unit.PERCENTAGE);
        this.addComponent(dataDetailGraphLayout);
        this.setExpandRatio(dataDetailGraphLayout,1f);

        similarDataInfoBrowserFrame = new BrowserFrame();
        similarDataInfoBrowserFrame.setSizeFull();
        similarDataInfoBrowserFrame.setHeight(browserWindowHeight-180,Unit.PIXELS);
        dataDetailGraphLayout.addComponent(similarDataInfoBrowserFrame);

        analyzingDimensionCheckBoxMap=new HashMap<>();
        loadRelatedDimensionsInfo();
    }

    private void loadRelatedDimensionsInfo(){
        List<RelationValueVO> relationValuesList = InfoDiscoverSpaceOperationUtil.getRelationableRelationsById(processingData.getDiscoverSpaceName(), processingData.getDataTypeKind(), processingData.getId());
        if(relationValuesList!=null){
            for(RelationValueVO currentRelationValue:relationValuesList){
                currentRelationValue.getId();
                currentRelationValue.getRelationTypeName();
                RelationableValueVO fromData=currentRelationValue.getFromRelationable();
                if(!fromData.getId().equals(processingData.getId())){
                    if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(fromData.getRelationableTypeKind())){
                        CheckBox dataSelectCheckBox = new CheckBox(VaadinIcons.ANGLE_DOUBLE_LEFT.getHtml()+fromData.getRelationableTypeName()+"["+fromData.getId()+"]");
                        dataSelectCheckBox.setCaptionAsHtml(true);
                        dataSelectCheckBox.setDescription( "关联的关系: "+currentRelationValue.getRelationTypeName()+"["+currentRelationValue.getId()+"]");
                        dataSelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
                        dimensionCheckboxFormLayout.addComponent(dataSelectCheckBox);
                        analyzingDimensionCheckBoxMap.put(dataSelectCheckBox,fromData.getId());
                    }
                }
                RelationableValueVO toData=currentRelationValue.getToRelationable();
                if(!toData.getId().equals(processingData.getId())){
                    if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(toData.getRelationableTypeKind())){
                        CheckBox dataSelectCheckBox = new CheckBox(VaadinIcons.ANGLE_DOUBLE_RIGHT.getHtml()+toData.getRelationableTypeName()+"["+toData.getId()+"]");
                        dataSelectCheckBox.setCaptionAsHtml(true);
                        dataSelectCheckBox.setDescription( "关联的关系: "+currentRelationValue.getRelationTypeName()+"["+currentRelationValue.getId()+"]");
                        dataSelectCheckBox.addStyleName(ValoTheme.CHECKBOX_SMALL);
                        dimensionCheckboxFormLayout.addComponent(dataSelectCheckBox);
                        analyzingDimensionCheckBoxMap.put(dataSelectCheckBox,toData.getId());
                    }
                }
            }
        }
    }

    public ProcessingDataVO getProcessingData(){
        return this.processingData;
    }
}

package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement.CreateRelationPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement.CreateRelationPanelInvoker;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/10/16.
 */
public class TypeDataInstanceDetailPanel extends VerticalLayout implements CreateRelationPanelInvoker {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private Window containerDialog;
    private TypeDataInstancePropertiesEditorPanel typeDataInstancePropertiesEditorPanel;
    private VerticalLayout dataInteractionInfoLayout;
    private RelationableRelationsList relationableRelationsList;

    public TypeDataInstanceDetailPanel(UserClientInfo userClientInfo,MeasurableValueVO measurableValue) {
        this.currentUserClientInfo = userClientInfo;
        this.measurableValue=measurableValue;
        this.setWidth(100,Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);
        String dataTypeKind= this.measurableValue.getMeasurableTypeKind();
        String dataTypeName=this.measurableValue.getMeasurableTypeName();
        String discoverSpaceName=this.measurableValue.getDiscoverSpaceName();
        String dataId=this.measurableValue.getId();

        String dataInstanceBasicInfoNoticeText;
        String dataInstanceTypeText;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.TAGS.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            dataInstanceTypeText="维度数据";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            dataInstanceTypeText="事实数据";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
            dataInstanceTypeText="关系数据";
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        }else{
            dataInstanceTypeText="数据";
            dataInstanceBasicInfoNoticeText="";
        }
        Label sectionActionBarLabel=new Label(dataInstanceBasicInfoNoticeText, ContentMode.HTML);
        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(sectionActionBarLabel);
        addComponent(dataTypeNoticeActionsBar);

        HorizontalLayout dataInstanceDetailContainerLayout=new HorizontalLayout();
        dataInstanceDetailContainerLayout.setHeight(100,Unit.PERCENTAGE);
        addComponent(dataInstanceDetailContainerLayout);

        //left side properties editor
        VerticalLayout dataPropertyInfoLayout=new VerticalLayout();
        dataPropertyInfoLayout.setHeight(100,Unit.PERCENTAGE);
        dataPropertyInfoLayout.setWidth(485,Unit.PIXELS);

        HorizontalLayout dataPropertyInfoTitleContainerLayout=new HorizontalLayout();
        dataPropertyInfoTitleContainerLayout.setWidth(100,Unit.PERCENTAGE);
        dataPropertyInfoLayout.addComponent(dataPropertyInfoTitleContainerLayout);

        Label dataPropertyTitle= new Label(FontAwesome.LIST_UL.getHtml() +" "+dataInstanceTypeText+"属性", ContentMode.HTML);
        dataPropertyTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataPropertyTitle.addStyleName("ui_appSectionLightDiv");
        dataPropertyInfoTitleContainerLayout.addComponent(dataPropertyTitle);

        Button showRelationsSwitchButton=new Button();
        showRelationsSwitchButton.setIcon(VaadinIcons.EXPAND_SQUARE);
        showRelationsSwitchButton.setDescription("显示数据关联交互信息");
        showRelationsSwitchButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showRelationsSwitchButton.addStyleName(ValoTheme.BUTTON_SMALL);
        dataPropertyInfoTitleContainerLayout.addComponent(showRelationsSwitchButton);
        showRelationsSwitchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(getContainerDialog()!=null){
                    if (getContainerDialog().getWindowMode().equals(WindowMode.MAXIMIZED)){
                        getContainerDialog().setWindowMode(WindowMode.NORMAL);
                        showRelationsSwitchButton.setIcon(VaadinIcons.EXPAND_SQUARE);
                        showRelationsSwitchButton.setDescription("显示数据关联交互信息");
                    }else{
                        getContainerDialog().setWindowMode(WindowMode.MAXIMIZED);
                        showRelationsSwitchButton.setIcon(VaadinIcons.INSERT);
                        showRelationsSwitchButton.setDescription("隐藏数据关联交互信息");
                    }
                }
            }
        });
        dataPropertyInfoTitleContainerLayout.setExpandRatio(dataPropertyTitle,1);

        typeDataInstancePropertiesEditorPanel=new TypeDataInstancePropertiesEditorPanel(this.currentUserClientInfo,this.measurableValue);
        dataPropertyInfoLayout.addComponent(typeDataInstancePropertiesEditorPanel);

        dataInstanceDetailContainerLayout.addComponent(dataPropertyInfoLayout);

        //right side data relation info editor
        this.dataInteractionInfoLayout=new VerticalLayout();
        this.dataInteractionInfoLayout.setHeight(100,Unit.PERCENTAGE);
        this.dataInteractionInfoLayout.setWidth(500,Unit.PIXELS);
        this.dataInteractionInfoLayout.addStyleName("ui_appSubViewContainer");

        HorizontalLayout dataRelationInfoTitleContainerLayout=new HorizontalLayout();
        dataRelationInfoTitleContainerLayout.setWidth(100,Unit.PERCENTAGE);
        this.dataInteractionInfoLayout.addComponent(dataRelationInfoTitleContainerLayout);

        Label dataRelationInfoTitle= new Label(VaadinIcons.CLUSTER.getHtml() +" "+dataInstanceTypeText+"关联交互信息", ContentMode.HTML);
        dataRelationInfoTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataRelationInfoTitle.addStyleName("ui_appSectionLightDiv");
        dataRelationInfoTitleContainerLayout.addComponent(dataRelationInfoTitle);

        Button refreshRelationsInfoButton=new Button();
        refreshRelationsInfoButton.setIcon(FontAwesome.REFRESH);
        refreshRelationsInfoButton.setDescription("刷新数据关联交互信息");
        refreshRelationsInfoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        refreshRelationsInfoButton.addStyleName(ValoTheme.BUTTON_SMALL);
        dataRelationInfoTitleContainerLayout.addComponent(refreshRelationsInfoButton);
        refreshRelationsInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                relationableRelationsList.reloadRelationsInfo();
            }
        });

        Button createNewRelationButton=new Button();
        createNewRelationButton.setIcon(FontAwesome.CHAIN);
        createNewRelationButton.setDescription("建立新的数据关联");
        createNewRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        createNewRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        dataRelationInfoTitleContainerLayout.addComponent(createNewRelationButton);
        createNewRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                createNewRelation();
            }
        });

        dataRelationInfoTitleContainerLayout.setExpandRatio(dataRelationInfoTitle,1);

        RelationableValueVO currentRelationableValueVO=new RelationableValueVO();
        currentRelationableValueVO.setDiscoverSpaceName(discoverSpaceName);
        currentRelationableValueVO.setRelationableTypeName(dataTypeName);
        currentRelationableValueVO.setRelationableTypeKind(dataTypeKind);
        currentRelationableValueVO.setId(dataId);

        this.relationableRelationsList=new RelationableRelationsList(this.currentUserClientInfo,currentRelationableValueVO);
        this.dataInteractionInfoLayout.addComponent(this.relationableRelationsList);

        dataInstanceDetailContainerLayout.addComponent(this.dataInteractionInfoLayout);
    }

    @Override
    public void attach() {
        super.attach();
        Window containerWindow=this.getContainerDialog();
        containerWindow.addWindowModeChangeListener(new Window.WindowModeChangeListener() {
            @Override
            public void windowModeChanged(Window.WindowModeChangeEvent windowModeChangeEvent) {
                setUIElementsSizeForWindowSizeChange();
            }
        });
        setUIElementsSizeForWindowSizeChange();
    }

    private void setUIElementsSizeForWindowSizeChange(){
        Window containerDialog=this.getContainerDialog();
        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        int browserWindowWidth=UI.getCurrent().getPage().getBrowserWindowWidth();
        int containerWindowDialogFixHeight=(int)containerDialog.getHeight();
        int typeInstanceDetailPropertiesEditorContainerPanelHeight=0;
        int dataRelationInfoLayoutWidth=500;
        int relationsListHeight;
        if (containerDialog.getWindowMode().equals(WindowMode.MAXIMIZED)){
            typeInstanceDetailPropertiesEditorContainerPanelHeight=browserWindowHeight-230;
            dataRelationInfoLayoutWidth=browserWindowWidth-510;
            relationsListHeight=browserWindowHeight-500;
        }else{
            typeInstanceDetailPropertiesEditorContainerPanelHeight=containerWindowDialogFixHeight-230;
            relationsListHeight=50;
        }
        this.typeDataInstancePropertiesEditorPanel.setPropertiesEditorContainerPanelHeight(typeInstanceDetailPropertiesEditorContainerPanelHeight);
        this.dataInteractionInfoLayout.setWidth(dataRelationInfoLayoutWidth,Unit.PIXELS);
        this.relationableRelationsList.setRelationableRelationsTableHeight(relationsListHeight);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void createNewRelation(){
        String dataTypeKind= this.measurableValue.getMeasurableTypeKind();
        String dataTypeName=this.measurableValue.getMeasurableTypeName();
        String discoverSpaceName=this.measurableValue.getDiscoverSpaceName();
        String dataId=this.measurableValue.getId();

        RelationableValueVO currentRelationableValueVO=new RelationableValueVO();
        currentRelationableValueVO.setDiscoverSpaceName(discoverSpaceName);
        currentRelationableValueVO.setRelationableTypeName(dataTypeName);
        currentRelationableValueVO.setRelationableTypeKind(dataTypeKind);
        currentRelationableValueVO.setId(dataId);

        CreateRelationPanel createRelationPanel=new CreateRelationPanel(this.currentUserClientInfo,currentRelationableValueVO);
        createRelationPanel.setCreateRelationPanelInvoker(this);

        final Window window = new Window();
        window.setWidth(1150, Unit.PIXELS);
        window.setHeight(720,Unit.PIXELS);
        window.setCaptionAsHtml(true);
        window.setResizable(false);
        window.setDraggable(true);
        window.setModal(true);
        window.center();
        window.setContent(createRelationPanel);
        createRelationPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void createRelationsActionFinish(boolean actionResult) {
        relationableRelationsList.reloadRelationsInfo();
    }
}

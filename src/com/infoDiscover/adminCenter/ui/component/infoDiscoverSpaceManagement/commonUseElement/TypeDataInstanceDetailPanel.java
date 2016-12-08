package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/10/16.
 */
public class TypeDataInstanceDetailPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private Window containerDialog;
    private TypeDataInstancePropertiesEditorPanel typeDataInstancePropertiesEditorPanel;

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
        String propertiesNoticeText;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.TAGS.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            propertiesNoticeText="维度数据属性";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            propertiesNoticeText="事实数据属性";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
            propertiesNoticeText="关系数据属性";
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
        }else{
            propertiesNoticeText="数据属性";
            dataInstanceBasicInfoNoticeText="";
        }
        Label sectionActionBarLabel=new Label(dataInstanceBasicInfoNoticeText, ContentMode.HTML);
        SectionActionsBar dataTypeNoticeActionsBar = new SectionActionsBar(sectionActionBarLabel);
        addComponent(dataTypeNoticeActionsBar);

        Label dataPropertyTitle= new Label(FontAwesome.LIST_UL.getHtml() +" "+propertiesNoticeText, ContentMode.HTML);
        dataPropertyTitle.addStyleName(ValoTheme.LABEL_SMALL);
        dataPropertyTitle.addStyleName("ui_appSectionLightDiv");
        dataPropertyTitle.addStyleName("ui_appSectionLightDiv");
        addComponent(dataPropertyTitle);

        typeDataInstancePropertiesEditorPanel=new TypeDataInstancePropertiesEditorPanel(this.currentUserClientInfo,this.measurableValue);
        addComponent(typeDataInstancePropertiesEditorPanel);
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
        int containerWindowDialogFixHeight=(int)containerDialog.getHeight();
        int typeInstanceDetailPropertiesEditorContainerPanelHeight=0;
        if (containerDialog.getWindowMode().equals(WindowMode.MAXIMIZED)){
            typeInstanceDetailPropertiesEditorContainerPanelHeight=browserWindowHeight-250;
        }else{
            typeInstanceDetailPropertiesEditorContainerPanelHeight=containerWindowDialogFixHeight-250;
        }
        typeDataInstancePropertiesEditorPanel.setPropertiesEditorContainerPanelHeight(typeInstanceDetailPropertiesEditorContainerPanelHeight);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public Window getContainerDialog() {
        return containerDialog;
    }
}

package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
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
            propertiesNoticeText="维度属性";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(dataTypeKind)){
            dataInstanceBasicInfoNoticeText=FontAwesome.CUBE.getHtml()+" "+discoverSpaceName+" /"+FontAwesome.CLONE.getHtml()+" "+dataTypeName+" /"+FontAwesome.KEY.getHtml()+" "+dataId;
            propertiesNoticeText="事实属性";
        }else if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(dataTypeKind)){
            propertiesNoticeText="关系属性";
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


    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

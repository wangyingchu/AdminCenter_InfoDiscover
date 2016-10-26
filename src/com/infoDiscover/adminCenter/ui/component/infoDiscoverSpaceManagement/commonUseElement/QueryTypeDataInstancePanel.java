package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 10/26/16.
 */
public class QueryTypeDataInstancePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private Label operationTitle;
    private MainSectionTitle queryTypeDataInstanceSectionTitle;
    private SectionActionsBar dataTypeNoticeActionsBar;

    public QueryTypeDataInstancePanel(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        this.queryTypeDataInstanceSectionTitle = new MainSectionTitle("---");
        addComponent(this.queryTypeDataInstanceSectionTitle);
        dataTypeNoticeActionsBar = new SectionActionsBar(new Label("---", ContentMode.HTML));
        addComponent(dataTypeNoticeActionsBar);
        this.operationTitle = new Label(FontAwesome.LIST_UL.getHtml() + " ---", ContentMode.HTML);
        this.operationTitle.addStyleName(ValoTheme.LABEL_SMALL);
        this.operationTitle.addStyleName("ui_appStandaloneElementPadding");
        this.operationTitle.addStyleName("ui_appSectionLightDiv");
        addComponent(operationTitle);
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    @Override
    public void attach() {
        super.attach();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询维度数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 维度属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.TAGS.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询事实数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 事实属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.CLONE.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询关系数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 关系属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}



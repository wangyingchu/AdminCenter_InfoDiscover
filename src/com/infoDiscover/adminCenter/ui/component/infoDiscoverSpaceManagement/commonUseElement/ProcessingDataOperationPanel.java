package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 12/15/16.
 */
public class ProcessingDataOperationPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private SectionActionsBar discoverSpaceNameNoticeActionsBar;
    private ProcessingDataListVO processingDataList;

    private ProcessingDataList dimensionProcessingDataList;
    private ProcessingDataList factProcessingDataList;
    private ProcessingDataList relationProcessingDataList;

    public ProcessingDataOperationPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        this.discoverSpaceNameNoticeActionsBar =new SectionActionsBar(new Label("---", ContentMode.HTML));
        this.addComponent(this.discoverSpaceNameNoticeActionsBar);

        TabSheet processingDataListTabs=new TabSheet();
        VerticalLayout discoverSpaceDimensionsListLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceDimensionsInfoLayoutTab =processingDataListTabs.addTab(discoverSpaceDimensionsListLayout, "空间维度数据");
        discoverSpaceDimensionsInfoLayoutTab.setIcon(FontAwesome.TAGS);
        this.dimensionProcessingDataList=new ProcessingDataList(this.currentUserClientInfo);
        this.dimensionProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        discoverSpaceDimensionsListLayout.addComponent(this.dimensionProcessingDataList);

        VerticalLayout discoverSpaceFactsListLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceFactsInfoLayoutTab =processingDataListTabs.addTab(discoverSpaceFactsListLayout, "空间事实数据");
        discoverSpaceFactsInfoLayoutTab.setIcon(FontAwesome.CLONE);
        this.factProcessingDataList=new ProcessingDataList(this.currentUserClientInfo);
        this.factProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
        discoverSpaceFactsListLayout.addComponent(this.factProcessingDataList);

        VerticalLayout discoverSpaceRelationsListLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceRelationsInfoLayoutTab =processingDataListTabs.addTab(discoverSpaceRelationsListLayout, "空间关系数据");
        discoverSpaceRelationsInfoLayoutTab.setIcon(FontAwesome.SHARE_ALT);
        this.relationProcessingDataList=new ProcessingDataList(this.currentUserClientInfo);
        this.relationProcessingDataList.setProcessingDataType(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
        discoverSpaceRelationsListLayout.addComponent(this.relationProcessingDataList);

        this.addComponent(processingDataListTabs);
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
        this.dimensionProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
        this.factProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
        this.relationProcessingDataList.setDiscoverSpaceName(this.discoverSpaceName);
    }

    @Override
    public void attach() {
        super.attach();
        Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName(), ContentMode.HTML);
        discoverSpaceNameNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        Button refreshListButton = new Button();
        refreshListButton.setIcon(FontAwesome.REFRESH);
        refreshListButton.setDescription("刷新待处理数据列表信息");
        refreshListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        refreshListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        refreshListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                dimensionProcessingDataList.refreshDataList();
                factProcessingDataList.refreshDataList();
                relationProcessingDataList.refreshDataList();
            }
        });
        discoverSpaceNameNoticeActionsBar.addActionComponent(refreshListButton);
    }

    public ProcessingDataListVO getProcessingDataList() {
        return processingDataList;
    }

    public void setProcessingDataList(ProcessingDataListVO processingDataList) {
        this.processingDataList = processingDataList;
        this.dimensionProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingDimensions());
        this.factProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingFacts());
        this.relationProcessingDataList.setProcessingDataVOList(this.processingDataList.getProcessingRelations());
    }
}

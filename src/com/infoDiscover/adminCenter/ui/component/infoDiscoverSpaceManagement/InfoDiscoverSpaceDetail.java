package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.RiskActionConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.event.*;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.ProcessingDataOperationPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement.QueryTypeDataInstancePanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.dimensionManagement.InfoDiscoverSpaceDimensionsInfo;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.factManagement.InfoDiscoverSpaceFactsInfo;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.relationManagement.InfoDiscoverSpaceRelationsInfo;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.runtimeInfo.InfoDiscoverSpaceRuntimeGeneralInfo;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 10/1/16.
 */
public class InfoDiscoverSpaceDetail extends VerticalLayout implements View,
        DiscoverSpaceTypeDataInstanceQueryRequiredEvent.DiscoverSpaceTypeDataInstanceQueryRequiredListener,
        DiscoverSpaceOpenProcessingDataListEvent.DiscoverSpaceOpenProcessingDataListListener,
        DiscoverSpaceAddProcessingDataEvent.DiscoverSpaceAddProcessingDataListener,
        DiscoverSpaceRemoveProcessingDataEvent.DiscoverSpaceRemoveProcessingDataListener{
    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private InfoDiscoverSpaceRuntimeGeneralInfo infoDiscoverSpaceRuntimeGeneralInfo;
    private InfoDiscoverSpaceDimensionsInfo infoDiscoverSpaceDimensionsInfo;
    private InfoDiscoverSpaceFactsInfo infoDiscoverSpaceFactsInfo;
    private InfoDiscoverSpaceRelationsInfo infoDiscoverSpaceRelationsInfo;

    public InfoDiscoverSpaceDetail(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);

        TabSheet tabs=new TabSheet();
        viewContentContainer.addComponent(tabs);

        VerticalLayout discoverSpaceRuntimeInfoLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceRuntimeInfoLayoutTab =tabs.addTab(discoverSpaceRuntimeInfoLayout, "空间运行状态信息");
        discoverSpaceRuntimeInfoLayoutTab.setIcon(FontAwesome.INFO);
        infoDiscoverSpaceRuntimeGeneralInfo=new InfoDiscoverSpaceRuntimeGeneralInfo(this.currentUserClientInfo);
        infoDiscoverSpaceRuntimeGeneralInfo.setParentInfoDiscoverSpaceDetail(this);
        discoverSpaceRuntimeInfoLayout.addComponent(infoDiscoverSpaceRuntimeGeneralInfo);

        VerticalLayout discoverSpaceDimensionsInfoLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceDimensionsInfoLayoutTab =tabs.addTab(discoverSpaceDimensionsInfoLayout, "空间维度管理");
        discoverSpaceDimensionsInfoLayoutTab.setIcon(FontAwesome.TAGS);
        infoDiscoverSpaceDimensionsInfo=new InfoDiscoverSpaceDimensionsInfo(this.currentUserClientInfo);
        infoDiscoverSpaceDimensionsInfo.setParentInfoDiscoverSpaceDetail(this);
        discoverSpaceDimensionsInfoLayout.addComponent(infoDiscoverSpaceDimensionsInfo);

        VerticalLayout discoverSpaceFactsInfoLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceFactsInfoLayoutTab =tabs.addTab(discoverSpaceFactsInfoLayout, "空间事实管理");
        discoverSpaceFactsInfoLayoutTab.setIcon(FontAwesome.CLONE);
        infoDiscoverSpaceFactsInfo=new InfoDiscoverSpaceFactsInfo(this.currentUserClientInfo);
        infoDiscoverSpaceFactsInfo.setParentInfoDiscoverSpaceDetail(this);
        discoverSpaceFactsInfoLayout.addComponent(infoDiscoverSpaceFactsInfo);

        VerticalLayout discoverSpaceRelationsInfoLayout=new VerticalLayout();
        TabSheet.Tab discoverSpaceRelationsInfoLayoutTab =tabs.addTab(discoverSpaceRelationsInfoLayout, "空间关系管理");
        discoverSpaceRelationsInfoLayoutTab.setIcon(FontAwesome.SHARE_ALT);
        infoDiscoverSpaceRelationsInfo=new InfoDiscoverSpaceRelationsInfo(this.currentUserClientInfo);
        infoDiscoverSpaceRelationsInfo.setParentInfoDiscoverSpaceDetail(this);
        discoverSpaceRelationsInfoLayout.addComponent(infoDiscoverSpaceRelationsInfo);



        QueryTypeDataInstancePanel queryTypeDataInstancePanel=new QueryTypeDataInstancePanel(this.currentUserClientInfo);
        queryTypeDataInstancePanel.setDiscoverSpaceName("PerformanceTestDB");
        queryTypeDataInstancePanel.setDataInstanceTypeName(("ID_DIMENSION"));
        queryTypeDataInstancePanel.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        final Window window = new Window();
        window.setResizable(true);
        window.setDraggable(true);
        window.setWidth(80, Unit.PERCENTAGE);
        window.setHeight(80, Unit.PERCENTAGE);
        window.center();
        //window.setModal(true);
        window.setContent(queryTypeDataInstancePanel);
        queryTypeDataInstancePanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {}

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public void renderDiscoverSpaceDetail(){
        DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics=InfoDiscoverSpaceOperationUtil.getDiscoverSpaceStatisticMetrics(this.discoverSpaceName);
        this.infoDiscoverSpaceRuntimeGeneralInfo.setDiscoverSpaceName(this.discoverSpaceName);
        this.infoDiscoverSpaceRuntimeGeneralInfo.renderRuntimeGeneralInfo(discoverSpaceStatisticMetrics);

        this.infoDiscoverSpaceDimensionsInfo.setDiscoverSpaceName(this.discoverSpaceName);
        this.infoDiscoverSpaceDimensionsInfo.renderDimensionsInfo(discoverSpaceStatisticMetrics);

        this.infoDiscoverSpaceFactsInfo.setDiscoverSpaceName(this.discoverSpaceName);
        this.infoDiscoverSpaceFactsInfo.renderFactsInfo(discoverSpaceStatisticMetrics);

        this.infoDiscoverSpaceRelationsInfo.setDiscoverSpaceName(this.discoverSpaceName);
        this.infoDiscoverSpaceRelationsInfo.renderRelationsInfo(discoverSpaceStatisticMetrics);
    }

    public void deleteCurrentDiscoverSpace(){
        //do delete space logic
        Label confirmMessage=new Label("<span style='color:#CE0000;'><span style='font-weight:bold;'>"+FontAwesome.EXCLAMATION_TRIANGLE.getHtml()+
                " 请确认是否删除信息发现空间  <b style='color:#333333;'>"+this.discoverSpaceName +"</b>,执行删除操作将会永久性的删除该发现空间中的所有数据。</span><br/>注意：此项操作不可撤销执行结果。</span>", ContentMode.HTML);
        final RiskActionConfirmDialog deleteDiscoverSpaceConfirmDialog = new RiskActionConfirmDialog();
        deleteDiscoverSpaceConfirmDialog.setConfirmMessage(confirmMessage);
        final InfoDiscoverSpaceDetail self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteDiscoverSpaceConfirmDialog.close();
                boolean deleteActivitySpaceResult=InfoDiscoverSpaceOperationUtil.deleteDiscoverSpace(self.discoverSpaceName);
                if(deleteActivitySpaceResult){
                    DiscoverSpaceDeletedEvent discoverSpaceDeletedEvent=new DiscoverSpaceDeletedEvent(self.discoverSpaceName);
                    self.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceDeletedEvent);
                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除信息发现空间成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("删除信息发现空间错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        deleteDiscoverSpaceConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteDiscoverSpaceConfirmDialog);
    }

    @Override
    public void receivedDiscoverSpaceTypeDataInstanceQueryRequiredEvent(DiscoverSpaceTypeDataInstanceQueryRequiredEvent event) {
        QueryTypeDataInstancePanel queryTypeDataInstancePanel=new QueryTypeDataInstancePanel(this.currentUserClientInfo);
        queryTypeDataInstancePanel.setDiscoverSpaceName(event.getDiscoverSpaceName());
        queryTypeDataInstancePanel.setDataInstanceTypeName((event.getDataInstanceTypeName()));
        queryTypeDataInstancePanel.setDataInstanceTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
        final Window window = new Window();
        window.setResizable(true);
        window.setDraggable(true);
        window.setWidth(80, Unit.PERCENTAGE);
        window.setHeight(80, Unit.PERCENTAGE);
        window.center();
        //window.setModal(true);
        window.setContent(queryTypeDataInstancePanel);
        queryTypeDataInstancePanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void receivedDiscoverSpaceOpenProcessingDataListEvent(DiscoverSpaceOpenProcessingDataListEvent event) {
        String discoverSpaceName=event.getDiscoverSpaceName();
        ProcessingDataOperationPanel processingDataOperationPanel =new ProcessingDataOperationPanel(this.currentUserClientInfo);
        processingDataOperationPanel.setDiscoverSpaceName(discoverSpaceName);
        ProcessingDataListVO targetProcessingDataList=this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().get(discoverSpaceName);
        if(targetProcessingDataList==null){
            targetProcessingDataList=new ProcessingDataListVO(discoverSpaceName);
            currentUserClientInfo.getDiscoverSpacesProcessingDataMap().put(discoverSpaceName,targetProcessingDataList);
        }
        processingDataOperationPanel.setProcessingDataList(targetProcessingDataList);

        String dataDetailInfoTitle="信息发现空间待处理数据列表";
        final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
        window.setWidth(600, Unit.PIXELS);
        window.setHeight(820,Unit.PIXELS);
        window.setCaptionAsHtml(true);
        window.setResizable(true);
        window.setDraggable(true);
        window.setModal(false);

        window.setContent(processingDataOperationPanel);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void receivedDiscoverSpaceAddProcessingDataEvent(DiscoverSpaceAddProcessingDataEvent event) {
        String targetDiscoverSpaceName=event.getDiscoverSpaceName();
        ProcessingDataListVO targetProcessingDataList=this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().get(targetDiscoverSpaceName);
        if(targetProcessingDataList==null){
            targetProcessingDataList=new ProcessingDataListVO(targetDiscoverSpaceName);
            this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().put(targetDiscoverSpaceName,targetProcessingDataList);
        }
        ProcessingDataVO targetProcessingData=event.getProcessingData();
        boolean addToProcessingListResult=targetProcessingDataList.addProcessingData(targetProcessingData);
        if(addToProcessingListResult){
            Notification resultNotification = new Notification("添加待处理数据操作成功",
                    "已成功将数据"+targetProcessingData.getDataTypeName()+" /"+targetProcessingData.getId()+"添加入待处理数据列表", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification resultNotification = new Notification("添加待处理数据操作未成功",
                    "待处理数据列表中已经加入过数据"+targetProcessingData.getDataTypeName()+" /"+targetProcessingData.getId()+"，或该数据在当前信息发现空间中不存在", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    @Override
    public void receivedDiscoverSpaceRemoveProcessingDataEvent(DiscoverSpaceRemoveProcessingDataEvent event) {
        String targetDiscoverSpaceName=event.getDiscoverSpaceName();
        ProcessingDataListVO targetProcessingDataList=this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().get(targetDiscoverSpaceName);
        if(targetProcessingDataList!=null){
            ProcessingDataVO targetProcessingData=event.getProcessingData();
            boolean removeFromProcessingListResult=targetProcessingDataList.removeProcessingData(targetProcessingData);
            if(removeFromProcessingListResult){
                Notification resultNotification = new Notification("移除待处理数据操作成功",
                        "已成功将数据"+targetProcessingData.getDataTypeName()+" /"+targetProcessingData.getId()+"从待处理数据列表中移除", Notification.Type.HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.BOTTOM_RIGHT);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            }else{
                Notification resultNotification = new Notification("移除待处理数据操作未成功",
                        "待处理数据列表中不存在数据"+targetProcessingData.getDataTypeName()+" /"+targetProcessingData.getId()+"", Notification.Type.HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.BOTTOM_RIGHT);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            }
        }
    }
}

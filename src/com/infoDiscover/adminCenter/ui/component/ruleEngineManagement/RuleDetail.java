package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo
        .ProcessingDataListVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.RuleEngineOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.RiskActionConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.event.*;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement
        .DataAnalyzeApplicationBrowserPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement
        .ProcessingDataOperationPanel;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement
        .QueryTypeDataInstancePanel;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineDeletedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by sun.
 */
public class RuleDetail extends VerticalLayout implements View{
       /* ,
        DiscoverSpaceTypeDataInstanceQueryRequiredEvent
                .DiscoverSpaceTypeDataInstanceQueryRequiredListener,
        DiscoverSpaceOpenProcessingDataListEvent.DiscoverSpaceOpenProcessingDataListListener,
        DiscoverSpaceAddProcessingDataEvent.DiscoverSpaceAddProcessingDataListener,
        DiscoverSpaceRemoveProcessingDataEvent.DiscoverSpaceRemoveProcessingDataListener,
        DiscoverSpaceLaunchDataAnalyzeApplicationEvent
                .DiscoverSpaceLaunchDataAnalyzeApplicationListener
                */

    private UserClientInfo currentUserClientInfo;
    private String ruleName;
    private String ruleId;
    private RuleGeneralInfo ruleGeneralInfo;

    public RuleDetail(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
       // this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);

        ruleGeneralInfo = new RuleGeneralInfo(this.currentUserClientInfo);
        ruleGeneralInfo.setRuleDetail(this);
        viewContentContainer.addComponent(ruleGeneralInfo);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public void renderRuleDetail() {
        RuleVO ruleVO = RuleEngineOperationUtil.getRuleById(ruleId);
        this.ruleGeneralInfo.setRuleName(this.ruleName);
        this.ruleGeneralInfo.setRuleId(this.ruleId);
        this.ruleGeneralInfo.renderRuleGeneralInfo(ruleVO);
    }

    public void executeRule() {
        // do execute rule logic
        Label confirmMessage = new Label("<span style='color:#CE0000;'><span " +
                "style='font-weight:bold;'>" + FontAwesome.EXCLAMATION_TRIANGLE.getHtml() +
                " 请确认是否执行规则  <b style='color:#333333;'>" + this.ruleId + "。</b>" +
                "</span><br/>注意：此项操作将根据数据集的大小，执行一定的时间，请耐心等候。</span>", ContentMode.HTML);
        final ConfirmDialog executeRuleConfirmDialog = new ConfirmDialog();
        executeRuleConfirmDialog.setConfirmMessage(confirmMessage);
        final RuleDetail self = this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                executeRuleConfirmDialog.close();
                boolean executeRuleResult = RuleEngineOperationUtil.executeRule(self.ruleId);
                if (executeRuleResult) {
                    RuleEngineDeletedEvent ruleDeletedEvent = new
                            RuleEngineDeletedEvent(self.ruleId);
                    self.currentUserClientInfo.getEventBlackBoard().fire(ruleDeletedEvent);
                    Notification resultNotification = new Notification("执行完成",
                            "规则执行成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                } else {
                    Notification errorNotification = new Notification("规则执行错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        executeRuleConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(executeRuleConfirmDialog);
    }

    public void deleteRule() {
        //do delete space logic
        Label confirmMessage = new Label("<span style='color:#CE0000;'><span " +
                "style='font-weight:bold;'>" + FontAwesome.EXCLAMATION_TRIANGLE.getHtml() +
                " 请确认是否删除规则  <b style='color:#333333;'>" + this.ruleId + "</b>," +
                "执行删除操作将会永久性的删除此条规则。</span><br/>注意：此项操作不可撤销执行结果。</span>", ContentMode.HTML);
        final RiskActionConfirmDialog deleteRuleConfirmDialog = new RiskActionConfirmDialog();
        deleteRuleConfirmDialog.setConfirmMessage(confirmMessage);
        final RuleDetail self = this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                deleteRuleConfirmDialog.close();
                boolean deleteRuleResult = RuleEngineOperationUtil.softDeleteRule(self.ruleId);
                if (deleteRuleResult) {
                    RuleEngineDeletedEvent ruleDeletedEvent = new
                            RuleEngineDeletedEvent(self.ruleId);
                    self.currentUserClientInfo.getEventBlackBoard().fire(ruleDeletedEvent);
                    Notification resultNotification = new Notification("删除数据操作成功",
                            "删除规则成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                } else {
                    Notification errorNotification = new Notification("删除规则错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        deleteRuleConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteRuleConfirmDialog);
    }
/*
    @Override
    public void receivedDiscoverSpaceTypeDataInstanceQueryRequiredEvent
            (DiscoverSpaceTypeDataInstanceQueryRequiredEvent event) {
        String discoverSpaceName = event.getDiscoverSpaceName();
        String targetWindowUID = discoverSpaceName + "_GlobalQueryDimensionDataInstanceWindow_" +
                event.getDataInstanceTypeName();
        Window targetWindow = this.currentUserClientInfo.getRuntimeWindowsRepository()
                .getExistingWindow(discoverSpaceName, targetWindowUID);
        if (targetWindow != null) {
            targetWindow.bringToFront();
            targetWindow.center();
        } else {
            QueryTypeDataInstancePanel queryTypeDataInstancePanel = new
                    QueryTypeDataInstancePanel(this.currentUserClientInfo);
            queryTypeDataInstancePanel.setDiscoverSpaceName(event.getDiscoverSpaceName());
            queryTypeDataInstancePanel.setDataInstanceTypeName((event.getDataInstanceTypeName()));
            queryTypeDataInstancePanel.setDataInstanceTypeKind(event.getDataInstanceTypeKind());
            final Window window = new Window();
            window.setResizable(true);
            window.setDraggable(true);
            window.setWidth(80, Unit.PERCENTAGE);
            window.setHeight(80, Unit.PERCENTAGE);
            window.center();
            //window.setModal(true);
            window.setContent(queryTypeDataInstancePanel);
            queryTypeDataInstancePanel.setContainerDialog(window);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow
                            (discoverSpaceName, targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow
                    (discoverSpaceName, targetWindowUID, window);
            UI.getCurrent().addWindow(window);
        }
    }

    @Override
    public void receivedDiscoverSpaceOpenProcessingDataListEvent
            (DiscoverSpaceOpenProcessingDataListEvent event) {
        String discoverSpaceName = event.getDiscoverSpaceName();
        String targetWindowUID = discoverSpaceName + "_GlobalProcessingDataListWindow";
        Window targetWindow = this.currentUserClientInfo.getRuntimeWindowsRepository()
                .getExistingWindow(discoverSpaceName, targetWindowUID);
        if (targetWindow != null) {
            targetWindow.bringToFront();
            targetWindow.center();
        } else {
            ProcessingDataOperationPanel processingDataOperationPanel = new
                    ProcessingDataOperationPanel(this.currentUserClientInfo, true, false, true);
            processingDataOperationPanel.setDiscoverSpaceName(discoverSpaceName);
            ProcessingDataListVO targetProcessingDataList = this.currentUserClientInfo
                    .getDiscoverSpacesProcessingDataMap().get(discoverSpaceName);
            if (targetProcessingDataList == null) {
                targetProcessingDataList = new ProcessingDataListVO(discoverSpaceName);
                currentUserClientInfo.getDiscoverSpacesProcessingDataMap().put(discoverSpaceName,
                        targetProcessingDataList);
            }
            processingDataOperationPanel.setProcessingDataList(targetProcessingDataList);

            String dataDetailInfoTitle = "信息发现空间待处理数据列表";
            final Window window = new Window(UICommonElementsUtil
                    .generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(600, Unit.PIXELS);
            window.setHeight(820, Unit.PIXELS);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.setContent(processingDataOperationPanel);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow
                            (discoverSpaceName, targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow
                    (discoverSpaceName, targetWindowUID, window);
            UI.getCurrent().addWindow(window);
        }
    }

    @Override
    public void receivedDiscoverSpaceAddProcessingDataEvent(DiscoverSpaceAddProcessingDataEvent
                                                                        event) {
        String targetDiscoverSpaceName = event.getDiscoverSpaceName();
        ProcessingDataListVO targetProcessingDataList = this.currentUserClientInfo
                .getDiscoverSpacesProcessingDataMap().get(targetDiscoverSpaceName);
        if (targetProcessingDataList == null) {
            targetProcessingDataList = new ProcessingDataListVO(targetDiscoverSpaceName);
            this.currentUserClientInfo.getDiscoverSpacesProcessingDataMap().put
                    (targetDiscoverSpaceName, targetProcessingDataList);
        }
        ProcessingDataVO targetProcessingData = event.getProcessingData();
        boolean addToProcessingListResult = targetProcessingDataList.addProcessingData
                (targetProcessingData);
        if (addToProcessingListResult) {
            Notification resultNotification = new Notification("添加待处理数据操作成功",
                    "已成功将数据" + targetProcessingData.getDataTypeName() + " /" +
                            targetProcessingData.getId() + "添加入待处理数据列表", Notification.Type
                    .HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        } else {
            Notification resultNotification = new Notification("添加待处理数据操作未成功",
                    "待处理数据列表中已经加入过数据" + targetProcessingData.getDataTypeName() + " /" +
                            targetProcessingData.getId() + "，或该数据在当前信息发现空间中不存在", Notification
                    .Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    @Override
    public void receivedDiscoverSpaceRemoveProcessingDataEvent
            (DiscoverSpaceRemoveProcessingDataEvent event) {
        String targetDiscoverSpaceName = event.getDiscoverSpaceName();
        ProcessingDataListVO targetProcessingDataList = this.currentUserClientInfo
                .getDiscoverSpacesProcessingDataMap().get(targetDiscoverSpaceName);
        if (targetProcessingDataList != null) {
            ProcessingDataVO targetProcessingData = event.getProcessingData();
            boolean removeFromProcessingListResult = targetProcessingDataList
                    .removeProcessingData(targetProcessingData);
            if (removeFromProcessingListResult) {
                Notification resultNotification = new Notification("移除待处理数据操作成功",
                        "已成功将数据" + targetProcessingData.getDataTypeName() + " /" +
                                targetProcessingData.getId() + "从待处理数据列表中移除", Notification.Type
                        .HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.BOTTOM_RIGHT);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            } else {
                Notification resultNotification = new Notification("移除待处理数据操作未成功",
                        "待处理数据列表中不存在数据" + targetProcessingData.getDataTypeName() + " /" +
                                targetProcessingData.getId() + "", Notification.Type
                        .HUMANIZED_MESSAGE);
                resultNotification.setPosition(Position.BOTTOM_RIGHT);
                resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                resultNotification.show(Page.getCurrent());
            }
        }
    }

    @Override
    public void receivedDiscoverSpaceLaunchDataAnalyzeApplicationEvent
            (DiscoverSpaceLaunchDataAnalyzeApplicationEvent event) {
        String discoverSpaceName = event.getDiscoverSpaceName();
        String targetWindowUID = discoverSpaceName + "_GlobalDataAnalyzeApplicationWindow";
        Window targetWindow = this.currentUserClientInfo.getRuntimeWindowsRepository()
                .getExistingWindow(discoverSpaceName, targetWindowUID);
        if (targetWindow != null) {
            targetWindow.bringToFront();
            targetWindow.center();
        } else {
            DataAnalyzeApplicationBrowserPanel dataAnalyzeApplicationBrowserPanel = new
                    DataAnalyzeApplicationBrowserPanel(this.currentUserClientInfo);
            dataAnalyzeApplicationBrowserPanel.setDiscoverSpaceName(discoverSpaceName);
            //dataAnalyzeApplicationBrowserPanel.setDataAnalyzeApplicationBaseAddress(this
//                    .dataAnalyzeApplicationBaseAddress);
            String dataDetailInfoTitle = "信息发现空间 " + discoverSpaceName + " 数据分析发现应用系统";
            final Window window = new Window(UICommonElementsUtil
                    .generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
            window.setWidth(80, Unit.PERCENTAGE);
            window.setHeight(80, Unit.PERCENTAGE);
            window.setCaptionAsHtml(true);
            window.setResizable(true);
            window.setDraggable(true);
            window.setModal(false);
            window.setContent(dataAnalyzeApplicationBrowserPanel);
            window.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent closeEvent) {
                    currentUserClientInfo.getRuntimeWindowsRepository().removeExistingWindow
                            (discoverSpaceName, targetWindowUID);
                }
            });
            this.currentUserClientInfo.getRuntimeWindowsRepository().addNewWindow(discoverSpaceName, targetWindowUID, window);
            UI.getCurrent().addWindow(window);
        }
    }
    */
}

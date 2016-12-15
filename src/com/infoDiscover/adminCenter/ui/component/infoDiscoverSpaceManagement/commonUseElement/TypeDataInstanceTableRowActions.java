package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;
import com.infoDiscover.adminCenter.ui.component.common.UICommonElementsUtil;
import com.infoDiscover.adminCenter.ui.component.event.DiscoverSpaceAddProcessingDataEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/8/16.
 */
public class TypeDataInstanceTableRowActions extends HorizontalLayout {

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private TypeDataInstanceList containerTypeDataInstanceList;
    private Button showTypeDataDetailButton;

    public TypeDataInstanceTableRowActions(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        showTypeDataDetailButton = new Button();
        showTypeDataDetailButton.setIcon(FontAwesome.EYE);
        showTypeDataDetailButton.setDescription("显示数据详情");
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showTypeDataDetailButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        showTypeDataDetailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showTypeDataDetailButton.setEnabled(false);
                showDataDetailInfoPanel();
            }
        });
        addComponent(showTypeDataDetailButton);

        Button addToProcessingListButton = new Button();
        addToProcessingListButton.setIcon(VaadinIcons.INBOX);
        addToProcessingListButton.setDescription("加入待处理数据列表");
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addToProcessingListButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        addToProcessingListButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processAddToProcessingList();
            }
        });
        addComponent(addToProcessingListButton);

        Button deleteButton = new Button();
        deleteButton.setIcon(FontAwesome.TRASH_O);
        deleteButton.setDescription("删除数据");
        deleteButton.addStyleName(ValoTheme.BUTTON_SMALL);
        deleteButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                processDeleteData();
            }
        });
        addComponent(deleteButton);
    }

    public MeasurableValueVO getMeasurableValue() {
        return measurableValue;
    }

    public void setMeasurableValue(MeasurableValueVO measurableValue) {
        this.measurableValue = measurableValue;
    }

    private void showDataDetailInfoPanel(){
        String dataTypeKind= getMeasurableValue().getMeasurableTypeKind();
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
        TypeDataInstanceDetailPanel typeDataInstanceDetailPanel=new TypeDataInstanceDetailPanel(this.currentUserClientInfo,getMeasurableValue());
        final Window window = new Window(UICommonElementsUtil.generateMovableWindowTitleWithFormat(dataDetailInfoTitle));
        window.setWidth(500, Unit.PIXELS);
        window.setHeight(800,Unit.PIXELS);
        window.setCaptionAsHtml(true);
        window.setResizable(true);
        window.setDraggable(true);
        window.setModal(false);
        window.setContent(typeDataInstanceDetailPanel);
        typeDataInstanceDetailPanel.setContainerDialog(window);
        UI.getCurrent().addWindow(window);
        int currentSubWindowXPositionOffset=getContainerTypeDataInstanceList().getSubWindowsXPositionOffset();
        int currentSubWindowYPositionOffset=getContainerTypeDataInstanceList().getSubWindowsYPositionOffset();

        window.setPosition(currentSubWindowXPositionOffset,currentSubWindowYPositionOffset);
        window.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                showTypeDataDetailButton.setEnabled(true);
            }
        });
        getContainerTypeDataInstanceList().setSubWindowsXPositionOffset(currentSubWindowXPositionOffset + 50);
        if(currentSubWindowYPositionOffset<=450){
            getContainerTypeDataInstanceList().setSubWindowsYPositionOffset(currentSubWindowYPositionOffset + 50);
        }else{
            getContainerTypeDataInstanceList().setSubWindowsYPositionOffset(20);
        }
    }

    private void processAddToProcessingList(){
        ProcessingDataVO processingDataVO=new ProcessingDataVO();
        processingDataVO.setId(getMeasurableValue().getId());
        processingDataVO.setDiscoverSpaceName(getMeasurableValue().getDiscoverSpaceName());
        processingDataVO.setDataTypeKind(getMeasurableValue().getMeasurableTypeKind());
        processingDataVO.setDataTypeName(getMeasurableValue().getMeasurableTypeName());

        DiscoverSpaceAddProcessingDataEvent discoverSpaceAddProcessingDataEvent=new DiscoverSpaceAddProcessingDataEvent(getMeasurableValue().getDiscoverSpaceName());
        discoverSpaceAddProcessingDataEvent.setProcessingData(processingDataVO);
        this.currentUserClientInfo.getEventBlackBoard().fire(discoverSpaceAddProcessingDataEvent);
    }

    private void processDeleteData(){}

    public TypeDataInstanceList getContainerTypeDataInstanceList() {
        return containerTypeDataInstanceList;
    }

    public void setContainerTypeDataInstanceList(TypeDataInstanceList containerTypeDataInstanceList) {
        this.containerTypeDataInstanceList = containerTypeDataInstanceList;
    }
}

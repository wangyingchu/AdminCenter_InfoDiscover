package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 4/13/17.
 */
public class FindRelationInfoOfTwoAnalyzingDataPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Label analyzingData1IdLabel;
    private Label analyzingData2IdLabel;

    public FindRelationInfoOfTwoAnalyzingDataPanel(UserClientInfo userClientInfo){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;

        HorizontalLayout twoAnalyzingDataInfoContainerLayout=new HorizontalLayout();
        twoAnalyzingDataInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        twoAnalyzingDataInfoContainerLayout.addStyleName("ui_appSectionLightDiv");
        this.addComponent(twoAnalyzingDataInfoContainerLayout);

        HorizontalLayout analyzingDataInfoContainerLayout=new HorizontalLayout();
        Label analyzingData_1Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (1) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_1Label);

        HorizontalLayout spacingDiv01Layout=new HorizontalLayout();
        spacingDiv01Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv01Layout);

        analyzingData1IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData1IdLabel);

        HorizontalLayout spacingDiv02Layout=new HorizontalLayout();
        spacingDiv02Layout.setWidth(20,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv02Layout);

        Label analyzingData_2Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (2) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_2Label);

        HorizontalLayout spacingDiv03Layout=new HorizontalLayout();
        spacingDiv03Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv03Layout);

        analyzingData2IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData2IdLabel);

        HorizontalLayout spacingDiv04Layout=new HorizontalLayout();
        spacingDiv04Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv04Layout);

        Button showShortPathRelationButton=new Button("发现最短路径关系");
        showShortPathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showShortPathRelationButton);
        showShortPathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showShortPathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showShortPathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showShortestPathRelation();
            }
        });

        Button showAllPathRelationButton=new Button("发现所有路径关系");
        showAllPathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showAllPathRelationButton);
        showAllPathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAllPathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(showAllPathRelationButton, Alignment.TOP_LEFT);
        showAllPathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showAllPathRelations();
            }
        });

        twoAnalyzingDataInfoContainerLayout.addComponent(analyzingDataInfoContainerLayout);
    }

    public void addFirstAnalyzingData(String processingDataId){
        analyzingData1IdLabel.setValue(processingDataId);
    }

    public void addSecondAnalyzingData(String processingDataId){
        analyzingData2IdLabel.setValue(processingDataId);
    }

    private void showShortestPathRelation(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        InfoDiscoverSpaceOperationUtil.xxx(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
    }

    private void showAllPathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        InfoDiscoverSpaceOperationUtil.yyy(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}

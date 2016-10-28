package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.VaadinIcons;

/**
 * Created by wangychu on 10/27/16.
 */
public class QueryConditionItem extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Label propertyNameLabel;

    public QueryConditionItem(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        this.addStyleName("ui_appSection_Top_LightDiv");

        Panel propertyInfoElementContainerPanel=new Panel();
        propertyInfoElementContainerPanel.setWidth(380,Unit.PIXELS);
        propertyInfoElementContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        this.addComponent(propertyInfoElementContainerPanel);

        HorizontalLayout queryPropertyAndGroupIngInfoContainerLayout=new HorizontalLayout();
        propertyInfoElementContainerPanel.setContent(queryPropertyAndGroupIngInfoContainerLayout);

        this.propertyNameLabel=new Label(FontAwesome.CIRCLE_O.getHtml()+" "+"-", ContentMode.HTML);
        propertyNameLabel.addStyleName(ValoTheme.LABEL_BOLD);
        queryPropertyAndGroupIngInfoContainerLayout.addComponent(propertyNameLabel);

        HorizontalLayout propertyConditionControllerContainerLayout=new HorizontalLayout();
        queryPropertyAndGroupIngInfoContainerLayout.addComponent(propertyConditionControllerContainerLayout);
        queryPropertyAndGroupIngInfoContainerLayout.setComponentAlignment(propertyConditionControllerContainerLayout, Alignment.MIDDLE_RIGHT);
        queryPropertyAndGroupIngInfoContainerLayout.setExpandRatio(propertyNameLabel,1.0f);

        Button filteringLogicOrButton=new Button("OR");
        filteringLogicOrButton.setIcon(VaadinIcons.PLUS);
        filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_TINY);
        filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        propertyConditionControllerContainerLayout.addComponent(filteringLogicOrButton);

        Button filteringLogicAndButton=new Button("AND");
        filteringLogicAndButton.setIcon(VaadinIcons.CLOSE);
        filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_TINY);
        filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        propertyConditionControllerContainerLayout.addComponent(filteringLogicAndButton);

        HorizontalLayout spacingDivLayout0=new HorizontalLayout();
        spacingDivLayout0.setWidth(10,Unit.PIXELS);
        propertyConditionControllerContainerLayout.addComponent(spacingDivLayout0);

        Button filteringLogicNotButton=new Button("NOT");
        filteringLogicNotButton.setIcon(FontAwesome.BAN);
        filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_TINY);
        filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        propertyConditionControllerContainerLayout.addComponent(filteringLogicNotButton);

        HorizontalLayout conditionValueInfoContainerLayout = new HorizontalLayout();

        Label inputIconLabel=new Label(VaadinIcons.INPUT.getHtml(), ContentMode.HTML);
        conditionValueInfoContainerLayout.addComponent(inputIconLabel);

        ComboBox dataFieldType = new ComboBox();
        dataFieldType.setTextInputAllowed(false);
        dataFieldType.setPageLength(11);
        dataFieldType.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        dataFieldType.addStyleName(ValoTheme.COMBOBOX_SMALL);
        dataFieldType.setRequired(true);
        dataFieldType.setWidth(155,Unit.PIXELS);
        dataFieldType.setTextInputAllowed(false);
        dataFieldType.setNullSelectionAllowed(false);
        dataFieldType.setInputPrompt("约束条件");
        dataFieldType.addItem("Equal");
        dataFieldType.addItem("Between");
        dataFieldType.addItem("Great Than Equal");
        dataFieldType.addItem("Great Than");
        dataFieldType.addItem("In Value");
        dataFieldType.addItem("Less Than");
        dataFieldType.addItem("Less Than Equal");
        dataFieldType.addItem("Not Equal");
        dataFieldType.addItem("Null Value");
        dataFieldType.addItem("Regular Match");
        dataFieldType.addItem("Similar to");
        conditionValueInfoContainerLayout.addComponent(dataFieldType);

        this.addComponent(conditionValueInfoContainerLayout);
    }
    @Override
    public void attach() {
        super.attach();
        //String propertyName="propertyNameprpropertyNameprpropertyNameprpropertyNamepr1234567890";
        //String propertyName="propertyNameprp2345tdfghj56789";
        String propertyName="propertyName-0101102234";
        this.propertyNameLabel.setValue(FontAwesome.CIRCLE_O.getHtml()+" "+propertyName);
        if(propertyName.length()<=30){
            propertyNameLabel.setWidth(280,Unit.PIXELS);
        }
    }
}

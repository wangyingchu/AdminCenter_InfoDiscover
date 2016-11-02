package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
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
    private PropertyTypeVO propertyTypeVO;
    private String dataInstanceTypeName;
    private boolean reverseCondition=false;
    private boolean isFirstQueryCondition=false;
    private String filteringLogic_AND="AND";
    private String filteringLogic_OR="OR";
    private String filteringLogic;
    private Button filteringLogicNotButton;
    private Button filteringLogicOrButton;
    private Button filteringLogicAndButton;

    public QueryConditionItem(UserClientInfo userClientInfo,PropertyTypeVO propertyTypeVO) {
        this.currentUserClientInfo = userClientInfo;
        this.propertyTypeVO=propertyTypeVO;
        this.reverseCondition=false;
        this.filteringLogic=filteringLogic_OR;
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

        this.filteringLogicOrButton=new Button("OR");
        this.filteringLogicOrButton.setIcon(VaadinIcons.PLUS);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicOrButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setFilteringLogic(filteringLogic_OR);
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicOrButton);

        this.filteringLogicAndButton=new Button("AND");
        this.filteringLogicAndButton.setIcon(VaadinIcons.CLOSE);
        this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicAndButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setFilteringLogic(filteringLogic_AND);
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicAndButton);

        HorizontalLayout spacingDivLayout0=new HorizontalLayout();
        spacingDivLayout0.setWidth(10,Unit.PIXELS);
        propertyConditionControllerContainerLayout.addComponent(spacingDivLayout0);

        this.filteringLogicNotButton=new Button("NOT");
        this.filteringLogicNotButton.setIcon(FontAwesome.BAN);
        this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.filteringLogicNotButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                setReverseConditionLogic();
            }
        });
        propertyConditionControllerContainerLayout.addComponent(this.filteringLogicNotButton);

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



        setIsFirstQueryConditionControll(this.isFirstQueryCondition);
    }
    @Override
    public void attach() {
        super.attach();
        //String propertyName="propertyNameprpropertyNameprpropertyNameprpropertyNamepr1234567890";
        //String propertyName="propertyNameprp2345tdfghj56789";
        String propertyName="-";
        if(this.propertyTypeVO!=null){
            String propertyDataType=this.propertyTypeVO.getPropertyType();

            switch(propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    propertyName="["+ApplicationConstant.DataFieldType_STRING+"] "+this.propertyTypeVO.getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    propertyName="["+ApplicationConstant.DataFieldType_BOOLEAN+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    propertyName= "["+ApplicationConstant.DataFieldType_DATE+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_INT:
                    propertyName="["+ApplicationConstant.DataFieldType_INT+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    propertyName="["+ ApplicationConstant.DataFieldType_LONG+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    propertyName="["+ApplicationConstant.DataFieldType_DOUBLE+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    propertyName="["+ApplicationConstant.DataFieldType_FLOAT+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    propertyName="["+ApplicationConstant.DataFieldType_SHORT+"] "+this.propertyTypeVO.getPropertyName();

                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    propertyName="["+ApplicationConstant.DataFieldType_BYTE+"] "+this.propertyTypeVO.getPropertyName();
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    propertyName="["+ApplicationConstant.DataFieldType_BINARY+"] "+this.propertyTypeVO.getPropertyName();
                    break;
            }

            if(this.getDataInstanceTypeName()!=null){
                if(this.getDataInstanceTypeName().equals(this.propertyTypeVO.getPropertySourceOwner())){
                    this.propertyNameLabel.setValue(FontAwesome.CIRCLE_O.getHtml()+" "+propertyName);
                }else{
                    this.propertyNameLabel.setValue(FontAwesome.REPLY_ALL.getHtml()+" "+propertyName);
                }
            }else{
                this.propertyNameLabel.setValue(" "+propertyName);
            }


        }else{
            this.propertyNameLabel.setValue(" "+propertyName);
        }
        if(propertyName.length()<=30){
            propertyNameLabel.setWidth(280, Unit.PIXELS);
        }
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    private void setReverseConditionLogic(){
        this.reverseCondition=!this.reverseCondition;
        if(this.reverseCondition){
            this.filteringLogicNotButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }else{
            this.filteringLogicNotButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
    }

    private void setFilteringLogic(String filteringLogicValue){
        this.filteringLogic=filteringLogicValue;
        this.filteringLogicOrButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicAndButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        if(this.filteringLogic.equals(filteringLogic_OR)){
            this.filteringLogicOrButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
        if(this.filteringLogic.equals(filteringLogic_AND)){
            this.filteringLogicAndButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        }
    }

    private void setIsFirstQueryConditionControll(boolean isFirstQueryCondition){
        this.filteringLogicOrButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        this.filteringLogicAndButton.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        if(isFirstQueryCondition){
            this.filteringLogicOrButton.setEnabled(false);
            this.filteringLogicAndButton.setEnabled(false);
        }else{
            this.filteringLogicOrButton.setEnabled(true);
            this.filteringLogicAndButton.setEnabled(true);
            setFilteringLogic(this.filteringLogic);
        }
    }

    public void setIsFirstQueryCondition(boolean isFirstQueryCondition) {
        this.isFirstQueryCondition = isFirstQueryCondition;
        setIsFirstQueryConditionControll(this.isFirstQueryCondition);
    }
}

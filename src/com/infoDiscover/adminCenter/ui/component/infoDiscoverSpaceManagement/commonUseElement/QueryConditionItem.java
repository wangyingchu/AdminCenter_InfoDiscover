package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;

import com.vaadin.data.Property;
import com.vaadin.data.validator.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
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
    private ComboBox filteringItemTypeSelection;
    private  HorizontalLayout conditionValueInputElementsLayout;

    private final String FilteringItemType_Equal="Equal";
    private final String FilteringItemType_NotEqual="Not Equal";
    private final String FilteringItemType_SimilarTo="Similar To";
    private final String FilteringItemType_RegularMatch="Regular Match";
    private final String FilteringItemType_Between= "Between";
    private final String FilteringItemType_GreatThan="Great Than";
    private final String FilteringItemType_GreatThanEqual="Great Than Equal";
    private final String FilteringItemType_LessThan="Less Than";
    private final String FilteringItemType_LessThanEqual="Less Than Equal";
    private final String FilteringItemType_InValue="In Value";
    private final String FilteringItemType_NullValue="Null Value";

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

        this.filteringItemTypeSelection = new ComboBox();
        this.filteringItemTypeSelection.setTextInputAllowed(false);
        this.filteringItemTypeSelection.setPageLength(11);
        this.filteringItemTypeSelection.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        this.filteringItemTypeSelection.addStyleName(ValoTheme.COMBOBOX_SMALL);
        this.filteringItemTypeSelection.setRequired(true);
        this.filteringItemTypeSelection.setWidth(155,Unit.PIXELS);
        this.filteringItemTypeSelection.setTextInputAllowed(false);
        this.filteringItemTypeSelection.setNullSelectionAllowed(false);
        this.filteringItemTypeSelection.setInputPrompt("约束条件");
        this.filteringItemTypeSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String filteringItemType=valueChangeEvent.getProperty().getValue().toString();
                renderFilteringItemInputElements(filteringItemType);
            }
        });

        conditionValueInfoContainerLayout.addComponent(this.filteringItemTypeSelection);
        this.addComponent(conditionValueInfoContainerLayout);

        this.conditionValueInputElementsLayout = new HorizontalLayout();
        conditionValueInfoContainerLayout.addComponent(this.conditionValueInputElementsLayout);

        setIsFirstQueryConditionControl(this.isFirstQueryCondition);
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
        setQueryConditionSelectionByDataType();
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    private void setQueryConditionSelectionByDataType(){
        if(this.propertyTypeVO!=null) {
            String propertyDataType = this.propertyTypeVO.getPropertyType();
            switch (propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_SimilarTo);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_RegularMatch);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Equal);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NotEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_Between);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_GreatThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThan);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_LessThanEqual);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_InValue);
                    this.filteringItemTypeSelection.addItem(FilteringItemType_NullValue);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    break;
            }
        }
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

    private void setIsFirstQueryConditionControl(boolean isFirstQueryCondition){
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
        setIsFirstQueryConditionControl(this.isFirstQueryCondition);
    }

    private void renderFilteringItemInputElements(String filteringItemType){
        this.conditionValueInputElementsLayout.removeAllComponents();
        Field currentValueEditor=null;
        switch(filteringItemType){
            case FilteringItemType_Equal:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_NotEqual:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_RegularMatch:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_GreatThan:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_GreatThanEqual:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_LessThan:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_LessThanEqual:
                currentValueEditor= generateSingleQueryValueTextField();
                this.conditionValueInputElementsLayout.addComponent(currentValueEditor);
                break;
            case FilteringItemType_SimilarTo:

                this.filteringItemTypeSelection.setWidth(85,Unit.PIXELS);

                HorizontalLayout fieldLayout=generateSimilarToQueryValueInputElements();
                this.conditionValueInputElementsLayout.addComponent(fieldLayout);
                break;

        }


    }

    private HorizontalLayout generateSimilarToQueryValueInputElements(){
        HorizontalLayout containerHorizontalLayout=new HorizontalLayout();
        ComboBox matchingTypeSelector = new ComboBox();
        matchingTypeSelector.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        matchingTypeSelector.setWidth(100,Unit.PIXELS);
        matchingTypeSelector.setTextInputAllowed(false);
        matchingTypeSelector.setNullSelectionAllowed(false);
        matchingTypeSelector.addItem("Begin With");
        matchingTypeSelector.addItem("End With");
        matchingTypeSelector.addItem("Contain");
        matchingTypeSelector.setValue("Begin With");
        containerHorizontalLayout.addComponent(matchingTypeSelector);
        TextField conditionValueEditor = new TextField();
        conditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        conditionValueEditor.setWidth(140,Unit.PIXELS);
        containerHorizontalLayout.addComponent(conditionValueEditor);
        return containerHorizontalLayout;
    }

    private Field generateSingleQueryValueTextField() {
        if (this.propertyTypeVO != null) {
            String propertyDataType = this.propertyTypeVO.getPropertyType();
            Field currentConditionValueEditor = null;
            switch (propertyDataType) {
                case ApplicationConstant.DataFieldType_STRING:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    currentConditionValueEditor = new ComboBox();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((ComboBox) currentConditionValueEditor).setTextInputAllowed(false);
                    ((ComboBox) currentConditionValueEditor).setNullSelectionAllowed(false);
                    ((ComboBox) currentConditionValueEditor).addItem("true");
                    ((ComboBox) currentConditionValueEditor).addItem("false");
                    ((ComboBox) currentConditionValueEditor).setValue("true");
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    currentConditionValueEditor = new PopupDateField();
                    currentConditionValueEditor.addStyleName(ValoTheme.DATEFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((DateField) currentConditionValueEditor).setDateFormat("yyyy-MM-dd hh:mm:ss");
                    ((DateField) currentConditionValueEditor).setResolution(Resolution.SECOND);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Integer.class);
                    currentConditionValueEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Long.class);
                    currentConditionValueEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Double.class);
                    currentConditionValueEditor.addValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Float.class);
                    currentConditionValueEditor.addValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0.0");
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    ((TextField) currentConditionValueEditor).setConverter(Short.class);
                    currentConditionValueEditor.addValidator(new ShortRangeValidator("该项属性值必须为SHORT类型", null, null));
                    ((TextField) currentConditionValueEditor).setValue("0");
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    currentConditionValueEditor = new TextField();
                    currentConditionValueEditor.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    currentConditionValueEditor.setWidth(190,Unit.PIXELS);
                    break;
            }
            return currentConditionValueEditor;
        }
        return null;
    }
}

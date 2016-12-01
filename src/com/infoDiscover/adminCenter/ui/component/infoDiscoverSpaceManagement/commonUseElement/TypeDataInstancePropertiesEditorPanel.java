package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.FieldPosition;
import java.util.*;

/**
 * Created by wangychu on 11/16/16.
 */
public class TypeDataInstancePropertiesEditorPanel extends VerticalLayout implements InputPropertyNamePanelInvoker{

    private UserClientInfo currentUserClientInfo;
    private MeasurableValueVO measurableValue;
    private Map<String,PropertyTypeVO> typePropertiesInfoMap;
    private Map<String,Field> dataPropertiesEditorMap;
    private MenuBar.MenuItem createTypeDefinedPropertyMenuItem;
    private MenuBar.MenuItem createDataCustomMenuItem;
    private MenuBar.MenuItem removeDataPropertyMenuItem;
    private MenuBar.Command createTypePropertyMenuItemCommand;
    private MenuBar.Command createDataCustomPropertyMenuItemCommand;
    private MenuBar.Command removeDataPropertyMenuItemCommand;
    private FormLayout propertiesEditForm;
    private Button editPropertiesButton;

    private Button cancelEditButton;
    private HorizontalLayout editorFormFooter;

    private String editPropertiesButtonCaption="---";


    private String currentTempCustomPropertyDataType;

    public TypeDataInstancePropertiesEditorPanel(UserClientInfo userClientInfo,MeasurableValueVO measurableValue){
        this.currentUserClientInfo=userClientInfo;
        this.measurableValue=measurableValue;
        this.typePropertiesInfoMap=new HashMap<String,PropertyTypeVO>();
        this.dataPropertiesEditorMap =new HashMap<String,Field>();
        MenuBar addRecordOperationMenuBar = new MenuBar();
        addRecordOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        addRecordOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);
        this.createTypeDefinedPropertyMenuItem = addRecordOperationMenuBar.addItem("添加类型预定义属性", FontAwesome.CODE_FORK, null);



        this.createDataCustomPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyDataType=selectedItem.getText();
                addDataCustomPropertyEditUI(selectedPropertyDataType);
            }
        };



        this.createDataCustomMenuItem = addRecordOperationMenuBar.addItem("添加自定义属性", FontAwesome.TAG, null);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_STRING, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_BOOLEAN, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_DATE, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_INT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_LONG, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_DOUBLE, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_FLOAT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);
        this.createDataCustomMenuItem.addItem(ApplicationConstant.DataFieldType_SHORT, FontAwesome.CIRCLE_O, this.createDataCustomPropertyMenuItemCommand);

        this.removeDataPropertyMenuItem = addRecordOperationMenuBar.addItem("删除属性", FontAwesome.TRASH_O, null);
        addComponent(addRecordOperationMenuBar);

        this.propertiesEditForm = new FormLayout();
        this.propertiesEditForm.setMargin(false);
        this.propertiesEditForm.setWidth("100%");
        this.propertiesEditForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addComponent(this.propertiesEditForm);

        this.editorFormFooter = new HorizontalLayout();
        this.editorFormFooter.setMargin(new MarginInfo(true, false, true, false));
        this.editorFormFooter.setSpacing(true);
        this.editorFormFooter.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(this.editorFormFooter);

        HorizontalLayout footerSpacingDiv=new HorizontalLayout();
        footerSpacingDiv.setWidth(170,Unit.PIXELS);
        this.editorFormFooter.addComponent(footerSpacingDiv);

        this.editPropertiesButton =new Button(editPropertiesButtonCaption, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editPropertiesButtonClickedAction();
            }
        });
        this.editPropertiesButton.setIcon(FontAwesome.EDIT);
        this.editorFormFooter.addComponent(this.editPropertiesButton);

        this.cancelEditButton=new Button("取消");
        this.cancelEditButton.setIcon(FontAwesome.TIMES);
        this.cancelEditButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                cancelPropertiesEdit();
            }
        });

        this.createTypePropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedTypePropertyName=selectedItem.getText();
                //addTypePropertyEditUI(selectedTypePropertyName);
            }
        };

        this.createDataCustomPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyDataType=selectedItem.getText();
                //addDataCustomPropertyEditUI(selectedPropertyDataType);
            }
        };

        this.removeDataPropertyMenuItemCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String selectedPropertyName=selectedItem.getText();
                //removeDataProperty(selectedPropertyName,selectedItem);
            }
        };
    }

    @Override
    public void attach() {
        super.attach();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(this.measurableValue.getMeasurableTypeKind())){
            this.editPropertiesButtonCaption="修改维度属性";
            List<PropertyTypeVO> dimensionTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(this.measurableValue.getDiscoverSpaceName(), this.measurableValue.getMeasurableTypeName());
            if(dimensionTypePropertiesList!=null){
                for(PropertyTypeVO currentPropertyTypeVO:dimensionTypePropertiesList){
                    this.typePropertiesInfoMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
                    if(this.measurableValue.getMeasurableTypeName().equals(currentPropertyTypeVO.getPropertySourceOwner())){
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.CIRCLE_O, this.createTypePropertyMenuItemCommand);
                    }else{
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.REPLY_ALL, this.createTypePropertyMenuItemCommand);
                    }
                }
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(this.measurableValue.getMeasurableTypeKind())){
            this.editPropertiesButtonCaption="修改事实属性";
            List<PropertyTypeVO> factTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(this.measurableValue.getDiscoverSpaceName(), this.measurableValue.getMeasurableTypeName());
            if(factTypePropertiesList!=null){
                for(PropertyTypeVO currentPropertyTypeVO:factTypePropertiesList){
                    this.typePropertiesInfoMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
                    if(this.measurableValue.getMeasurableTypeName().equals(currentPropertyTypeVO.getPropertySourceOwner())){
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.CIRCLE_O, this.createTypePropertyMenuItemCommand);
                    }else{
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.REPLY_ALL, this.createTypePropertyMenuItemCommand);
                    }
                }
            }
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(this.measurableValue.getMeasurableTypeKind())){
            this.editPropertiesButtonCaption="修改关系属性";
            List<PropertyTypeVO> factTypePropertiesList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(this.measurableValue.getDiscoverSpaceName(), this.measurableValue.getMeasurableTypeName());
            if(factTypePropertiesList!=null){
                for(PropertyTypeVO currentPropertyTypeVO:factTypePropertiesList){
                    this.typePropertiesInfoMap.put(currentPropertyTypeVO.getPropertyName(),currentPropertyTypeVO);
                    if(this.measurableValue.getMeasurableTypeName().equals(currentPropertyTypeVO.getPropertySourceOwner())){
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.CIRCLE_O, this.createTypePropertyMenuItemCommand);
                    }else{
                        this.createTypeDefinedPropertyMenuItem.addItem(currentPropertyTypeVO.getPropertyName(), FontAwesome.REPLY_ALL, this.createTypePropertyMenuItemCommand);
                    }
                }
            }
        }
        this.editPropertiesButton.setCaption(this.editPropertiesButtonCaption);
        List<String> dataInstanceProperties=this.measurableValue.getPropertyNames();
        if(dataInstanceProperties!=null){
            for(String currentPropertyName:dataInstanceProperties){
                PropertyValueVO currentPropertyValueVO=this.measurableValue.getPropertyValue(currentPropertyName);
                if(currentPropertyValueVO!=null){
                    if(this.typePropertiesInfoMap.get(currentPropertyName)==null){
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentPropertyValueVO.getPropertyName());
                        currentPropertyTypeVO.setPropertyType(currentPropertyValueVO.getPropertyType());
                        currentPropertyTypeVO.setReadOnly(false);
                        currentPropertyTypeVO.setNullable(false);
                        currentPropertyTypeVO.setMandatory(false);
                        currentPropertyTypeVO.setPropertySourceOwner(this.measurableValue.getMeasurableTypeName());
                        this.typePropertiesInfoMap.put(currentPropertyValueVO.getPropertyName(), currentPropertyTypeVO);
                    }
                    addTypePropertyEditUI(currentPropertyName,this.measurableValue.getPropertyValue(currentPropertyName).getPropertyValue());
                }
            }
        }
        setDisableFormEditableStatue(true);
    }

    private void setFormReadOnlyStatue(boolean status){
        Set<String> formKeySet=this.dataPropertiesEditorMap.keySet();
        Iterator<String> keyIterator=formKeySet.iterator();
        while(keyIterator.hasNext()){
            Field currentField=this.dataPropertiesEditorMap.get(keyIterator.next());
            if(currentField!=null){
                currentField.setReadOnly(status);
            }
        }
    }

    private void setDisableFormEditableStatue(boolean disableEdit){
        if(disableEdit){
            this.createTypeDefinedPropertyMenuItem.setEnabled(false);
            this.createDataCustomMenuItem.setEnabled(false);
            this.removeDataPropertyMenuItem.setEnabled(false);

            this.editorFormFooter.removeComponent(this.cancelEditButton);
            this.editPropertiesButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
            this.editPropertiesButton.setIcon(FontAwesome.EDIT);
            this.editPropertiesButton.setCaption(this.editPropertiesButtonCaption);
        }else{
            this.createTypeDefinedPropertyMenuItem.setEnabled(true);
            this.createDataCustomMenuItem.setEnabled(true);
            this.removeDataPropertyMenuItem.setEnabled(true);
            this.editorFormFooter.addComponent(this.cancelEditButton);
            this.editPropertiesButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
            this.editPropertiesButton.setIcon(FontAwesome.SAVE);
            this.editPropertiesButton.setCaption("保存修改");
        }
        setFormReadOnlyStatue(disableEdit);
    }

    private void cancelPropertiesEdit(){
        setDisableFormEditableStatue(false);
        this.dataPropertiesEditorMap.clear();
        this.propertiesEditForm.removeAllComponents();
        List<String> dataInstanceProperties=this.measurableValue.getPropertyNames();
        if(dataInstanceProperties!=null){
            for(String currentPropertyName:dataInstanceProperties){
                PropertyValueVO currentPropertyValueVO=this.measurableValue.getPropertyValue(currentPropertyName);
                if(currentPropertyValueVO!=null){
                    if(this.typePropertiesInfoMap.get(currentPropertyName)==null){
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentPropertyValueVO.getPropertyName());
                        currentPropertyTypeVO.setPropertyType(currentPropertyValueVO.getPropertyType());
                        currentPropertyTypeVO.setReadOnly(false);
                        currentPropertyTypeVO.setNullable(false);
                        currentPropertyTypeVO.setMandatory(false);
                        currentPropertyTypeVO.setPropertySourceOwner(this.measurableValue.getMeasurableTypeName());
                        this.typePropertiesInfoMap.put(currentPropertyValueVO.getPropertyName(), currentPropertyTypeVO);
                    }
                    addTypePropertyEditUI(currentPropertyName,this.measurableValue.getPropertyValue(currentPropertyName).getPropertyValue());
                }
            }
        }
        setDisableFormEditableStatue(true);
    }

    private void editPropertiesButtonClickedAction(){
        if(this.editPropertiesButton.getCaption().equals(this.editPropertiesButtonCaption)){
            setDisableFormEditableStatue(false);
        }else{
System.out.println("dddd");
        }
    }

    private void addTypePropertyEditUI(String properTyName,Object propertyValue){
        Field propertyEditor=this.dataPropertiesEditorMap.get(properTyName);
        if(propertyEditor!=null){
            Notification errorNotification = new Notification("数据校验错误",
                    "属性 "+properTyName+" 已存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        PropertyTypeVO propertyType=this.typePropertiesInfoMap.get(properTyName);
        if(propertyType!=null){
            String propertyName=propertyType.getPropertyName();
            String propertyDataType=propertyType.getPropertyType();
            Field currentPropertyEditor=null;
            switch(propertyDataType){
                case ApplicationConstant.DataFieldType_STRING:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_STRING+"] "+propertyName);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    currentPropertyEditor=new ComboBox("["+ApplicationConstant.DataFieldType_BOOLEAN+"] "+propertyName);
                    ((ComboBox)currentPropertyEditor).addItem("true");
                    ((ComboBox)currentPropertyEditor).addItem("false");
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    currentPropertyEditor= new PopupDateField("["+ApplicationConstant.DataFieldType_DATE+"] "+propertyName);
                    ((DateField)currentPropertyEditor).setDateFormat("yyyy-MM-dd hh:mm:ss");
                    ((DateField)currentPropertyEditor).setResolution(Resolution.SECOND);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_INT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Integer.class);
                    currentPropertyEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    currentPropertyEditor=new TextField("["+ ApplicationConstant.DataFieldType_LONG+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Long.class);
                    currentPropertyEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null,null));
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_DOUBLE+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Double.class);
                    currentPropertyEditor.addValidator(new DoubleRangeValidator("该项属性值必须为DOUBLE类型", null,null));
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_FLOAT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Float.class);
                    currentPropertyEditor.addValidator(new FloatRangeValidator("该项属性值必须为FLOAT类型", null,null));
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_SHORT+"] "+propertyName);
                    ((TextField)currentPropertyEditor).setConverter(Short.class);
                    currentPropertyEditor.addValidator(new ShortRangeValidator("该项属性值必须为SHORT类型", null,null));
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_BYTE+"] "+propertyName);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    currentPropertyEditor=new TextField("["+ApplicationConstant.DataFieldType_BINARY+"] "+propertyName);
                    break;
            }
            setTypePropertyEditorValue(currentPropertyEditor,propertyDataType,propertyValue);
            if(currentPropertyEditor!=null){
                if(propertyType.isMandatory()){
                    currentPropertyEditor.setRequired(true);
                }
                if(propertyType.isReadOnly()){
                    currentPropertyEditor.setReadOnly(true);
                }
                if(!propertyType.isNullable()){
                    currentPropertyEditor.addValidator(new NullValidator("该项属性值不允许为空", false));
                }
            }
            this.dataPropertiesEditorMap.put(properTyName, currentPropertyEditor);
            this.propertiesEditForm.addComponent(currentPropertyEditor);
            if(!propertyType.isMandatory()) {
                if (this.measurableValue.getMeasurableTypeName().equals(propertyType.getPropertySourceOwner())) {
                    this.removeDataPropertyMenuItem.addItem(propertyType.getPropertyName(), FontAwesome.CIRCLE_O, this.removeDataPropertyMenuItemCommand);
                } else {
                    this.removeDataPropertyMenuItem.addItem(propertyType.getPropertyName(), FontAwesome.REPLY_ALL, this.removeDataPropertyMenuItemCommand);
                }
            }
        }
    }

    private void setTypePropertyEditorValue(Field propertyEditor,String propertyDataType,Object propertyValue){
        if(propertyEditor==null||propertyValue==null){
            return;
        }
        switch(propertyDataType){
            case ApplicationConstant.DataFieldType_STRING:
                propertyEditor.setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_BOOLEAN:
                propertyEditor.setValue(((Boolean) propertyValue).toString());
                break;
            case ApplicationConstant.DataFieldType_DATE:
                propertyEditor.setValue(((Date) propertyValue));
                break;
            case ApplicationConstant.DataFieldType_INT:
                ((TextField)propertyEditor).setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_LONG:
                ((TextField)propertyEditor).setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_DOUBLE:
                ((TextField)propertyEditor).setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_FLOAT:
                ((TextField)propertyEditor).setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_SHORT:
                ((TextField)propertyEditor).setValue(propertyValue.toString());
                break;
            case ApplicationConstant.DataFieldType_BYTE:
                break;
            case ApplicationConstant.DataFieldType_BINARY:
                break;
        }
    }

    private void addDataCustomPropertyEditUI(String propertyDataType){
        this.currentTempCustomPropertyDataType=propertyDataType;
        InputPropertyNamePanel inputPropertyNamePanel=new InputPropertyNamePanel(this.currentUserClientInfo);
        final Window window = new Window();
        window.setWidth(450.0f, Unit.PIXELS);
        window.setResizable(false);
        window.center();
        window.setModal(true);
        window.setContent(inputPropertyNamePanel);
        inputPropertyNamePanel.setContainerDialog(window);
        inputPropertyNamePanel.setInputPropertyNamePanelInvoker(this);
        UI.getCurrent().addWindow(window);
    }


    @Override
    public void inputPropertyNameActionFinish(String propertyNameValue) {
        System.out.println(propertyNameValue);
        System.out.println(propertyNameValue);
        System.out.println(propertyNameValue);
        System.out.println(propertyNameValue);


        String dataTypeStr="---";
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(this.measurableValue.getMeasurableTypeKind())){
            dataTypeStr="维度";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(this.measurableValue.getMeasurableTypeKind())){
            dataTypeStr="事实";
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(this.measurableValue.getMeasurableTypeKind())){
            dataTypeStr="关系";
        }




        if(this.currentTempCustomPropertyDataType!=null){
            Field propertyEditor=this.dataPropertiesEditorMap.get(propertyNameValue);
            if(propertyEditor!=null){
                Notification errorNotification = new Notification("数据校验错误",
                        "属性 "+propertyNameValue+" 已存在", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
            PropertyTypeVO propertyType=this.typePropertiesInfoMap.get(propertyNameValue);
            if(propertyType!=null){
                Notification errorNotification = new Notification("数据校验错误",
                        "属性 "+propertyNameValue+" 是所属"+dataTypeStr+"类型预定义属性", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }

            PropertyTypeVO dataCustomPropertyTypeVO=new PropertyTypeVO();
            dataCustomPropertyTypeVO.setPropertySourceOwner(this.measurableValue.getMeasurableTypeName());
            dataCustomPropertyTypeVO.setReadOnly(false);
            dataCustomPropertyTypeVO.setNullable(false);
            dataCustomPropertyTypeVO.setMandatory(false);
            dataCustomPropertyTypeVO.setPropertyName(propertyNameValue);
            dataCustomPropertyTypeVO.setPropertyType(this.currentTempCustomPropertyDataType);

            this.typePropertiesInfoMap.put(propertyNameValue,dataCustomPropertyTypeVO);
            this.addTypePropertyEditUI(propertyNameValue,null);
        }







    }
}
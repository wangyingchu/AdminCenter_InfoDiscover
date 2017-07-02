package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.dataMappingManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.SolutionTypePropertyTypeDefinitionVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 7/1/17.
 */
public class PropertyMappingConfigurationItem extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private HorizontalLayout configItemsContainerLayout;
    private SolutionTypePropertyTypeDefinitionVO sourceSolutionTypePropertyTypeDefinitionVO;
    private TextField minValueTextField;
    private TextField maxValueTextField;

    public PropertyMappingConfigurationItem(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.configItemsContainerLayout=new HorizontalLayout();
        addComponent(this.configItemsContainerLayout);
    }

    public void setMappingConfigUI(SolutionTypePropertyTypeDefinitionVO sourceSolutionTypePropertyTypeDefinitionVO){
        this.configItemsContainerLayout.removeAllComponents();
        if(this.minValueTextField!=null){
            this.minValueTextField.setValue(null);
            this.minValueTextField.setConvertedValue(null);
        }
        if(this.maxValueTextField!=null){
            this.maxValueTextField.setValue(null);
            this.maxValueTextField.setConvertedValue(null);
        }
        this.sourceSolutionTypePropertyTypeDefinitionVO=sourceSolutionTypePropertyTypeDefinitionVO;
        String propertyType=this.sourceSolutionTypePropertyTypeDefinitionVO.getPropertyType();
        if(ApplicationConstant.DataFieldType_INT.equals(propertyType)||
                ApplicationConstant.DataFieldType_INT.equals(propertyType)||
                ApplicationConstant.DataFieldType_SHORT.equals(propertyType)||
                ApplicationConstant.DataFieldType_LONG.equals(propertyType)||
                ApplicationConstant.DataFieldType_FLOAT.equals(propertyType)||
                ApplicationConstant.DataFieldType_DOUBLE.equals(propertyType)||
                ApplicationConstant.DataFieldType_BYTE.equals(propertyType)
                ){
            this.minValueTextField=new TextField();
            this.minValueTextField.addStyleName(ValoTheme.TEXTFIELD_TINY);
            this.minValueTextField.setInputPrompt("最小值");
            this.configItemsContainerLayout.addComponent(this.minValueTextField);

            HorizontalLayout spacingDiv1=new HorizontalLayout();
            spacingDiv1.setWidth(10,Unit.PIXELS);
            this.configItemsContainerLayout.addComponent(spacingDiv1);

            Label sectionDivLabel=new Label(" "+VaadinIcons.ARROWS_LONG_H.getHtml()+" ", ContentMode.HTML);
            this.configItemsContainerLayout.addComponent(sectionDivLabel);

            HorizontalLayout spacingDiv2=new HorizontalLayout();
            spacingDiv2.setWidth(10,Unit.PIXELS);
            this.configItemsContainerLayout.addComponent(spacingDiv2);

            this.maxValueTextField=new TextField();
            this.maxValueTextField.addStyleName(ValoTheme.TEXTFIELD_TINY);
            this.maxValueTextField.setInputPrompt("最大值");
            this.configItemsContainerLayout.addComponent(this.maxValueTextField);

            switch (propertyType) {
                case ApplicationConstant.DataFieldType_STRING: break;
                case ApplicationConstant.DataFieldType_BOOLEAN: break;
                case ApplicationConstant.DataFieldType_DATE: break;
                case ApplicationConstant.DataFieldType_INT:
                    this.minValueTextField.setConverter(Integer.class);
                    this.minValueTextField.addValidator(new IntegerRangeValidator("该项值必须为INT类型", null, null));
                    this.maxValueTextField.setConverter(Integer.class);
                    this.maxValueTextField.addValidator(new IntegerRangeValidator("该项值必须为INT类型", null, null));
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    this.minValueTextField.setConverter(Long.class);
                    this.minValueTextField.addValidator(new IntegerRangeValidator("该项值必须为LONG类型", null, null));
                    this.maxValueTextField.setConverter(Long.class);
                    this.maxValueTextField.addValidator(new IntegerRangeValidator("该项值必须为LONG类型", null, null));
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    this.minValueTextField.setConverter(Double.class);
                    this.minValueTextField.addValidator(new IntegerRangeValidator("该项值必须为DOUBLE类型", null, null));
                    this.maxValueTextField.setConverter(Double.class);
                    this.maxValueTextField.addValidator(new IntegerRangeValidator("该项值必须为DOUBLE类型", null, null));
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    this.minValueTextField.setConverter(Float.class);
                    this.minValueTextField.addValidator(new IntegerRangeValidator("该项值必须为FLOAT类型", null, null));
                    this.maxValueTextField.setConverter(Float.class);
                    this.maxValueTextField.addValidator(new IntegerRangeValidator("该项值必须为FLOAT类型", null, null));
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    this.minValueTextField.setConverter(Short.class);
                    this.minValueTextField.addValidator(new IntegerRangeValidator("该项值必须为SHORT类型", null, null));
                    this.maxValueTextField.setConverter(Short.class);
                    this.maxValueTextField.addValidator(new IntegerRangeValidator("该项值必须为SHORT类型", null, null));
                    break;
                case ApplicationConstant.DataFieldType_BYTE: break;
                case ApplicationConstant.DataFieldType_BINARY: break;
            }
        }
    }

    public void clearMappingConfigUI(){
        this.sourceSolutionTypePropertyTypeDefinitionVO=null;
        this.configItemsContainerLayout.removeAllComponents();
    }

    public String getMinValue(){
        if(this.minValueTextField!=null){
            if(this.minValueTextField.getValue()!=null&&this.minValueTextField.getConvertedValue()!=null){
                return this.minValueTextField.getConvertedValue().toString();
            }
        }
        return null;
    }

    public String getMaxValue(){
        if(this.maxValueTextField!=null){
            if(this.maxValueTextField.getValue()!=null&&this.maxValueTextField.getConvertedValue()!=null){
                return this.maxValueTextField.getConvertedValue().toString();
            }
        }
        return null;
    }
}

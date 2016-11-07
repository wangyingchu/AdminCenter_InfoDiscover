package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo;

import java.util.List;

/**
 * Created by wangychu on 11/6/16.
 */
public class MeasurableValueVO {
    private String id;
    private String measurableTypeName;
    private String measurableTypeKind;
    private List<String> propertyNames;
    private List<PropertyValueVO> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public List<PropertyValueVO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyValueVO> properties) {
        this.properties = properties;
    }

    public String getMeasurableTypeName() {
        return measurableTypeName;
    }

    public void setMeasurableTypeName(String measurableTypeName) {
        this.measurableTypeName = measurableTypeName;
    }

    public String getMeasurableTypeKind() {
        return measurableTypeKind;
    }

    public void setMeasurableTypeKind(String measurableTypeKind) {
        this.measurableTypeKind = measurableTypeKind;
    }

    public PropertyValueVO getPropertyValue(String propertyName){
        if(this.properties!=null){
            for(PropertyValueVO currentPropertyValueVO:this.properties){
                if(currentPropertyValueVO.getPropertyName().equals(propertyName)){
                   return currentPropertyValueVO;
                }
            }
            return null;
        }else{
            return null;
        }
    }
}

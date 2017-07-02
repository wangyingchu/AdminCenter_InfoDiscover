package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo;

/**
 * Created by wangychu on 7/2/17.
 */
public class DataMappingDefinitionVO {

    private String sourceDataTypeName;
    private String sourceDataTypeKind;
    private String sourceDataPropertyName;
    private String sourceDataPropertyType;
    private String relationTypeName;
    private String relationDirection;
    private String mappingNotExistHandleMethod;
    private String targetDataTypeName;
    private String targetDataTypeKind;
    private String targetDataPropertyName;
    private String targetDataPropertyType;
    private String minValue;
    private String maxValue;
    private String solutionName;

    public String getSourceDataTypeName() {
        return sourceDataTypeName;
    }

    public void setSourceDataTypeName(String sourceDataTypeName) {
        this.sourceDataTypeName = sourceDataTypeName;
    }

    public String getSourceDataTypeKind() {
        return sourceDataTypeKind;
    }

    public void setSourceDataTypeKind(String sourceDataTypeKind) {
        this.sourceDataTypeKind = sourceDataTypeKind;
    }

    public String getSourceDataPropertyName() {
        return sourceDataPropertyName;
    }

    public void setSourceDataPropertyName(String sourceDataPropertyName) {
        this.sourceDataPropertyName = sourceDataPropertyName;
    }

    public String getSourceDataPropertyType() {
        return sourceDataPropertyType;
    }

    public void setSourceDataPropertyType(String sourceDataPropertyType) {
        this.sourceDataPropertyType = sourceDataPropertyType;
    }

    public String getRelationTypeName() {
        return relationTypeName;
    }

    public void setRelationTypeName(String relationTypeName) {
        this.relationTypeName = relationTypeName;
    }

    public String getRelationDirection() {
        return relationDirection;
    }

    public void setRelationDirection(String relationDirection) {
        this.relationDirection = relationDirection;
    }

    public String getMappingNotExistHandleMethod() {
        return mappingNotExistHandleMethod;
    }

    public void setMappingNotExistHandleMethod(String mappingNotExistHandleMethod) {
        this.mappingNotExistHandleMethod = mappingNotExistHandleMethod;
    }

    public String getTargetDataTypeName() {
        return targetDataTypeName;
    }

    public void setTargetDataTypeName(String targetDataTypeName) {
        this.targetDataTypeName = targetDataTypeName;
    }

    public String getTargetDataTypeKind() {
        return targetDataTypeKind;
    }

    public void setTargetDataTypeKind(String targetDataTypeKind) {
        this.targetDataTypeKind = targetDataTypeKind;
    }

    public String getTargetDataPropertyName() {
        return targetDataPropertyName;
    }

    public void setTargetDataPropertyName(String targetDataPropertyName) {
        this.targetDataPropertyName = targetDataPropertyName;
    }

    public String getTargetDataPropertyType() {
        return targetDataPropertyType;
    }

    public void setTargetDataPropertyType(String targetDataPropertyType) {
        this.targetDataPropertyType = targetDataPropertyType;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
}

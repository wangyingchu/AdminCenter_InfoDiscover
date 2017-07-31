package com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo;

import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingConstants;
//import com.infoDiscover.common.util.JsonUtil;

/**
 * Created by sun.
 */
public class RuleContent {
    private String spaceName;
    private String sourceType;
    private String sourceProperties;
    private String targetType;
    private String targetProperty;

    public RuleContent(String spaceName, String sourceType, String sourceProperties, String
            targetType, String targetProperty) {
        this.spaceName = spaceName;
        this.sourceType = sourceType;
        this.sourceProperties = sourceProperties;
        this.targetType = targetType;
        this.targetProperty = targetProperty;
    }

    public RuleContent(String content) {

        /*
        this.spaceName = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SPACE,
                content);
        this.sourceType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_SOURCE_TYPE,
                content);
        this.sourceProperties = JsonUtil.getPropertyValues(PropertyMappingConstants
                        .JSON_SOURCE_PROPERTIES,
                content);
        this.targetType = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_TYPE,
                content);
        this.targetProperty = JsonUtil.getPropertyValues(PropertyMappingConstants.JSON_TARGET_PROPERTY,
                content);

        */

    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceProperties() {
        return sourceProperties;
    }

    public void setSourceProperties(String sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }
}

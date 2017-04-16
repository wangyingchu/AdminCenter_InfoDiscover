package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement
        .InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.RuleEngineOperationUtil;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleContent;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineCreatedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.infoDiscover.common.util.JsonUtil;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun.
 */
public class CreateRulePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private TextField ruleName;
    private ComboBox ruleTypeComboBox;
    private ComboBox spaceNameComboBox;
    private ComboBox factNameComboBox;
    private ListSelect factPropertiesMultiSelect;
    private ComboBox dimensionNameComboBox;
    private ComboBox dimensionPropertyComboBox;
    private TextArea description;

    private List<DataTypeStatisticMetrics> factDataTypeList = new ArrayList<>();
    private List<DataTypeStatisticMetrics> dimensionDataTypeList = new ArrayList<>();

    private List<PropertyTypeVO> factPropertiesList = new ArrayList<>();
    private List<PropertyTypeVO> dimensionPropertiesList = new ArrayList<>();

    public CreateRulePanel(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Rule Section
        MainSectionTitle addNewDiscoverSpaceSectionTitle = new MainSectionTitle("创建新的规则");
        addComponent(addNewDiscoverSpaceSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        ruleName = new TextField("规则名称");
        ruleName.setRequired(true);
        form.addComponent(ruleName);
        form.setReadOnly(true);

        ruleTypeComboBox = new ComboBox("规则类型");
        ruleTypeComboBox.addItem("PropertyMapping");
        ruleTypeComboBox.setItemCaption("PropertyMapping", "属性匹配");
        ruleTypeComboBox.setNullSelectionAllowed(false);
        ruleTypeComboBox.setValue("PropertyMapping");
        ruleTypeComboBox.setTextInputAllowed(false);
        form.addComponent(ruleTypeComboBox);
        form.setReadOnly(true);

        final String[] selectedSpace = new String[1];
        spaceNameComboBox = new ComboBox("信息发现空间");
        List<String> spaceList = InfoDiscoverSpaceOperationUtil.getExistDiscoverSpace(new
                String[]{RuleEngineDatabaseConstants.RuleEngineSpace});
        spaceList.remove("RuleEngine");
        spaceNameComboBox.addItems(spaceList);
        spaceNameComboBox.setRequired(true);
        spaceNameComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                // remove
                factNameComboBox.removeAllItems();
                dimensionNameComboBox.removeAllItems();
                factPropertiesMultiSelect.removeAllItems();
                dimensionPropertyComboBox.removeAllItems();

                selectedSpace[0] = event.getProperty().getValue().toString();
                DiscoverSpaceStatisticMetrics
                        discoverSpaceStatisticMetrics = InfoDiscoverSpaceOperationUtil
                        .getDiscoverSpaceStatisticMetrics(selectedSpace[0]);
                factDataTypeList = discoverSpaceStatisticMetrics
                        .getFactsStatisticMetrics();
                dimensionDataTypeList = discoverSpaceStatisticMetrics
                        .getDimensionsStatisticMetrics();

                if (factDataTypeList != null && factDataTypeList.size() > 0) {
                    for (DataTypeStatisticMetrics dataType : factDataTypeList) {
                        String factType = dataType.getDataTypeName();
                        factNameComboBox.addItem(factType.substring(factType.lastIndexOf("_") + 1));
                    }
                }

                if (dimensionDataTypeList != null && dimensionDataTypeList.size() > 0) {
                    for (DataTypeStatisticMetrics dataType : dimensionDataTypeList) {
                        String dimensionTypeName = dataType.getDataTypeName();
                        dimensionNameComboBox.addItem(dimensionTypeName.substring(dimensionTypeName
                                .lastIndexOf("_") + 1));
                    }
                }
            }
        });
        form.addComponent(spaceNameComboBox);
        form.setReadOnly(true);

        factNameComboBox = new ComboBox("事实表名称");
        final String[] selectedFactTypeName = new String[1];
        factNameComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    selectedFactTypeName[0] = event.getProperty().getValue().toString();
                    factPropertiesList = InfoDiscoverSpaceOperationUtil.
                            retrieveFactTypePropertiesInfo(selectedSpace[0],
                                    selectedFactTypeName[0]);

                    if (factPropertiesList != null && factPropertiesList.size() > 0) {
                        List<String> properties = new ArrayList<>();
                        for (PropertyTypeVO vo : factPropertiesList) {
                            properties.add(vo.getPropertyName());
                        }
                        factPropertiesMultiSelect.addItems(properties);
                    }
                }
            }
        });
        factNameComboBox.setRequired(true);
        form.addComponent(factNameComboBox);
        form.setReadOnly(true);

        factPropertiesMultiSelect = new ListSelect("事实表映射属性");
        factPropertiesMultiSelect.setMultiSelect(true);
        factPropertiesMultiSelect.setNullSelectionAllowed(false);
        factPropertiesMultiSelect.setRows(4);
        factPropertiesMultiSelect.setSizeFull();
        factPropertiesMultiSelect.setRequired(true);
        form.addComponent(factPropertiesMultiSelect);
        form.setReadOnly(true);

        dimensionNameComboBox = new ComboBox("维度名称");
        final String[] selectDimensionTypeName = new String[1];
        dimensionNameComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    selectDimensionTypeName[0] = event.getProperty().getValue().toString();
                    dimensionPropertiesList = InfoDiscoverSpaceOperationUtil
                            .retrieveDimensionTypePropertiesInfo(selectedSpace[0],
                                    selectDimensionTypeName[0]);
                    if (dimensionPropertiesList != null && dimensionPropertiesList.size() > 0) {
                        List<String> properties = new ArrayList<>();
                        for (PropertyTypeVO vo : dimensionPropertiesList) {
                            properties.add(vo.getPropertyName());
                        }
                        dimensionPropertyComboBox.addItems(properties);
                    }
                }
            }
        });
        dimensionNameComboBox.setRequired(true);
        form.addComponent(dimensionNameComboBox);
        form.setReadOnly(true);

        dimensionPropertyComboBox = new ComboBox("维度映射属性");
        dimensionPropertyComboBox.setRequired(true);
        form.addComponent(dimensionPropertyComboBox);
        form.setReadOnly(true);

        description = new TextArea("说明");
        description.setRequired(false);
        form.addComponent(description);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton = new Button("创建规则", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new discover space logic */
                addNewRule();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    private boolean isEmptyValue(String value) {
        return value == null || value.trim().equals("");
    }

    private boolean isEmptyFactPropertiesValue(String value) {
        return isEmptyValue(value) || value.trim().equals("[]");
    }

    private void addNewRule() {
        final String ruleNameValue = ruleName.getValue();
        final String ruleTypeValue = ruleTypeComboBox.getValue() == null ? "" : ruleTypeComboBox
                .getValue().toString();
        final String spaceNameValue = spaceNameComboBox.getValue() == null ? "" :
                spaceNameComboBox.getValue().toString();
        final String factTypeNameValue = factNameComboBox.getValue() == null ? "" :
                factNameComboBox.getValue().toString();
        final String factPropertiesValue = factPropertiesMultiSelect.getValue() == null ? "" :
                factPropertiesMultiSelect.getValue().toString();
        final String dimensionTypeNameValue = dimensionNameComboBox.getValue() == null ? "" :
                dimensionNameComboBox.getValue().toString();
        final String dimensionPropertyValue = dimensionPropertyComboBox.getValue() == null ? "" :
                dimensionNameComboBox.getValue().toString();
        final String descriptionValue = description.getValue();

        if (isEmptyValue(ruleNameValue) || isEmptyValue(spaceNameValue) || isEmptyValue
                (factTypeNameValue) || isEmptyFactPropertiesValue(factPropertiesValue) || isEmptyValue
                (dimensionTypeNameValue) || isEmptyValue(dimensionPropertyValue)) {
            Notification errorNotification = new Notification("数据校验错误",
                    "请检查 规则名称 信息发现空间 事实表名称 事实表映射属性 维度名称 维度映射属性 是否为空", Notification.Type
                    .ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }

        String ruleId = spaceNameValue + "_" + factTypeNameValue;
        boolean isExistedRule = RuleEngineOperationUtil.checkRuleExistence(ruleId);
        if (isExistedRule) {
            Notification errorNotification = new Notification("数据校验错误",
                    "事实表 " + factTypeNameValue + " 的规则已经存在", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }


        //do add new logic
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " 请确认为事实表<b>" + factTypeNameValue + "</b>创建规则.", ContentMode.HTML);
        final ConfirmDialog addRuleConfirmDialog = new ConfirmDialog();
        addRuleConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateRulePanel self = this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addRuleConfirmDialog.close();

                RuleContent ruleContent = new RuleContent(spaceNameValue, factTypeNameValue,
                        factPropertiesValue, dimensionTypeNameValue, dimensionPropertyValue);
                String contentStr = JsonUtil.beanToJson(ruleContent);

                String ruleId = spaceNameValue + "_" + factTypeNameValue;
                RuleVO rule = new RuleVO(ruleId, ruleNameValue, ruleTypeValue, descriptionValue,
                        contentStr, false);

                boolean createRuleResult = false;
                if (RuleEngineOperationUtil.checkRuleExistence(rule.getRuleId())) {
                    createRuleResult = RuleEngineOperationUtil.updateRule(rule);
                } else {
                    createRuleResult = RuleEngineOperationUtil.createRule(rule);
                }

                if (createRuleResult) {
                    self.containerDialog.close();

                    RuleEngineCreatedEvent ruleEngineCreatedEvent = new
                            RuleEngineCreatedEvent(factTypeNameValue);
                    self.currentUserClientInfo.getEventBlackBoard().fire(ruleEngineCreatedEvent);

                    Notification resultNotification = new Notification("添加数据操作成功",
                            "创建规则成功", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                } else {
                    Notification errorNotification = new Notification("创建规则错误",
                            "发生服务器端错误", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addRuleConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addRuleConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

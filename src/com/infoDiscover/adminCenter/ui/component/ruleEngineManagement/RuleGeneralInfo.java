package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleVO;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by sun.
 */
public class RuleGeneralInfo extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private TextField ruleType;
    private TextField spaceName;
    private TextField factName;
    private TextField factMappingProperties;
    private TextField dimensionName;
    private TextField dimensionProperty;
    private RuleDetail ruleDetail;
    private String ruleName;
    private MainSectionTitle mainSectionTitle;

    public RuleGeneralInfo(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;

        mainSectionTitle = new MainSectionTitle("规则基本信息");
        addComponent(mainSectionTitle);

        SecondarySectionTitle secondarySectionTitle = new SecondarySectionTitle("规则基本信息");
        this.setWidth("100%");

        HorizontalLayout elementPlacementLayout = new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        FormLayout generalInfoForm = new FormLayout();
        generalInfoForm.setMargin(false);
        generalInfoForm.setWidth("100%");
        generalInfoForm.addStyleName("light");

        this.ruleType = new TextField("规则类型");
        this.ruleType.setRequired(false);
        this.ruleType.setReadOnly(true);
        generalInfoForm.addComponent(ruleType);

        this.spaceName = new TextField("信息发现空间名称");
        this.spaceName.setRequired(false);
        this.spaceName.setReadOnly(true);
        generalInfoForm.addComponent(spaceName);

        this.factName = new TextField("事实表名称");
        this.factName.setRequired(false);
        this.factName.setReadOnly(true);
        generalInfoForm.addComponent(factName);

        this.factMappingProperties = new TextField("事实表映射字段");
        this.factMappingProperties.setRequired(false);
        this.factMappingProperties.setReadOnly(true);
        generalInfoForm.addComponent(factMappingProperties);

        this.dimensionName = new TextField("维度名称");
        this.dimensionName.setRequired(false);
        this.dimensionName.setReadOnly(true);
        generalInfoForm.addComponent(dimensionName);
        this.dimensionProperty = new TextField("维度映射字段");
        this.dimensionProperty.setRequired(false);
        this.dimensionProperty.setReadOnly(true);
        generalInfoForm.addComponent(dimensionProperty);

        VerticalLayout generalInfoContainer = new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(generalInfoForm);
        elementPlacementLayout.addComponent(generalInfoContainer);

        HorizontalLayout actionButtonsPlacementLayout = new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout = new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceInfoButton=new Button("执行规则");
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceInfoButton.setIcon(FontAwesome.REFRESH);

        final RuleGeneralInfo self=this;
        refreshDiscoverSpaceInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceInfoButton);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidth("15px");
        actionButtonsPlacementLayout.addComponent(spaceDivLayout);

        Button deleteRuleButton = new Button("删除规则");
        deleteRuleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        deleteRuleButton.addStyleName(ValoTheme.BUTTON_TINY);
        deleteRuleButton.setIcon(FontAwesome.TRASH_O);
        deleteRuleButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (self.ruleDetail != null) {
                    self.ruleDetail.deleteCurrentDiscoverSpace();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(deleteRuleButton);

        VerticalLayout spacingLayout = new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderRuleGeneralInfo(RuleVO
                                              ruleVO) {
        this.mainSectionTitle.setValue(this.ruleName);

        this.ruleType.setReadOnly(false);
        String ruleType = ruleVO.getType().equalsIgnoreCase("PropertyMapping") ? "属性匹配" : ruleVO
                .getType();
        this.ruleType.setValue("" + ruleType);
        this.ruleType.setReadOnly(true);

        this.spaceName.setReadOnly(false);
        this.spaceName.setValue("" + ruleVO.getSpaceName());
        this.spaceName.setReadOnly(true);

        this.factName.setReadOnly(false);
        this.factName.setValue("" + ruleVO.getFactName());
        this.factName.setReadOnly(true);

        this.factMappingProperties.setReadOnly(false);
        this.factMappingProperties.setValue("" + ruleVO
                .getSourceProperties());
        this.factMappingProperties.setReadOnly(true);

        this.dimensionName.setReadOnly(false);
        this.dimensionName.setValue("" + ruleVO.getDimensionName());
        this.dimensionName.setReadOnly(true);

        this.dimensionProperty.setReadOnly(false);
        this.dimensionProperty.setValue("" + ruleVO.getTargetProperty());
        this.dimensionProperty.setReadOnly(true);
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}

package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleVO;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SecondarySectionTitle;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement
        .InfoDiscoverSpaceDetail;
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
    private TextField factName;
    private TextField factMappingProperties;
    private TextField dimensionName;
    private TextField dimensionProperty;
    private InfoDiscoverSpaceDetail parentInfoDiscoverSpaceDetail;
    private String ruleName;
    private MainSectionTitle mainSectionTitle;

    public RuleGeneralInfo(UserClientInfo currentUserClientInfo) {
        this.currentUserClientInfo = currentUserClientInfo;

        mainSectionTitle = new MainSectionTitle("规则基本信息");
        addComponent(mainSectionTitle);

        SecondarySectionTitle secondarySectionTitle3 = new SecondarySectionTitle("规则类型");
        this.setWidth("100%");

        HorizontalLayout elementPlacementLayout3 = new HorizontalLayout();
        elementPlacementLayout3.setWidth("100%");
        addComponent(elementPlacementLayout3);

        FormLayout generalInfoForm3 = new FormLayout();
        generalInfoForm3.setMargin(false);
        generalInfoForm3.setWidth("100%");
        generalInfoForm3.addStyleName("light");

        this.ruleType = new TextField("类型");
        this.ruleType.setRequired(false);
        this.ruleType.setReadOnly(true);
        generalInfoForm3.addComponent(ruleType);

        SecondarySectionTitle secondarySectionTitle = new SecondarySectionTitle("事实表信息");
        this.setWidth("100%");

        HorizontalLayout elementPlacementLayout = new HorizontalLayout();
        elementPlacementLayout.setWidth("100%");
        addComponent(elementPlacementLayout);

        FormLayout generalInfoForm = new FormLayout();
        generalInfoForm.setMargin(false);
        generalInfoForm.setWidth("100%");
        generalInfoForm.addStyleName("light");

        this.factName = new TextField("表名");
        this.factName.setRequired(false);
        this.factName.setReadOnly(true);
        generalInfoForm.addComponent(factName);
        this.factMappingProperties = new TextField("映射字段");
        this.factMappingProperties.setRequired(false);
        this.factMappingProperties.setReadOnly(true);
        generalInfoForm.addComponent(factMappingProperties);

        SecondarySectionTitle secondarySectionTitle2 = new SecondarySectionTitle("维度信息");
        this.setWidth("100%");

        HorizontalLayout elementPlacementLayout2 = new HorizontalLayout();
        elementPlacementLayout2.setWidth("100%");
        addComponent(elementPlacementLayout2);

        FormLayout generalInfoForm2 = new FormLayout();
        generalInfoForm2.setMargin(false);
        generalInfoForm2.setWidth("100%");
        generalInfoForm2.addStyleName("light");

        this.dimensionName = new TextField("维度名称");
        this.dimensionName.setRequired(false);
        this.dimensionName.setReadOnly(true);
        generalInfoForm2.addComponent(dimensionName);
        this.dimensionProperty = new TextField("映射字段");
        this.dimensionProperty.setRequired(false);
        this.dimensionProperty.setReadOnly(true);
        generalInfoForm2.addComponent(dimensionProperty);

        VerticalLayout generalInfoContainer = new VerticalLayout();
        generalInfoContainer.addComponent(secondarySectionTitle3);
        generalInfoContainer.addComponent(generalInfoForm3);
        generalInfoContainer.addComponent(secondarySectionTitle);
        generalInfoContainer.addComponent(generalInfoForm);
        generalInfoContainer.addComponent(secondarySectionTitle2);
        generalInfoContainer.addComponent(generalInfoForm2);
        elementPlacementLayout.addComponent(generalInfoContainer);

        HorizontalLayout actionButtonsPlacementLayout = new HorizontalLayout();
        addComponent(actionButtonsPlacementLayout);

        HorizontalLayout actionButtonsSpacingLayout = new HorizontalLayout();
        actionButtonsSpacingLayout.setWidth("10px");
        actionButtonsPlacementLayout.addComponent(actionButtonsSpacingLayout);

        Button refreshDiscoverSpaceInfoButton = new Button("刷新规则信息");
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        refreshDiscoverSpaceInfoButton.addStyleName(ValoTheme.BUTTON_TINY);
        refreshDiscoverSpaceInfoButton.setIcon(FontAwesome.REFRESH);

        final RuleGeneralInfo self = this;
        refreshDiscoverSpaceInfoButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (self.parentInfoDiscoverSpaceDetail != null) {
                    self.parentInfoDiscoverSpaceDetail.renderDiscoverSpaceDetail();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(refreshDiscoverSpaceInfoButton);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidth("15px");
        actionButtonsPlacementLayout.addComponent(spaceDivLayout);

        Button deleteDiscoverSpaceButton = new Button("删除规则");
        deleteDiscoverSpaceButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        deleteDiscoverSpaceButton.addStyleName(ValoTheme.BUTTON_TINY);
        deleteDiscoverSpaceButton.setIcon(FontAwesome.TRASH_O);
        deleteDiscoverSpaceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (self.parentInfoDiscoverSpaceDetail != null) {
                    self.parentInfoDiscoverSpaceDetail.deleteCurrentDiscoverSpace();
                }
            }
        });
        actionButtonsPlacementLayout.addComponent(deleteDiscoverSpaceButton);

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

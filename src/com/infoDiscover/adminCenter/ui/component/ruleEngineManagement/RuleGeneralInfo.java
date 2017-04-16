package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.vo.RuleVO;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleContent;
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
    private TextField factTypeName;
    private TextField factMappingProperties;
    private TextField dimensionTypeName;
    private TextField dimensionProperty;
    private TextArea description;
    private RuleDetail ruleDetail;
    private String ruleName;
    private String ruleId;
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

        this.factTypeName = new TextField("事实表名称");
        this.factTypeName.setRequired(false);
        this.factTypeName.setReadOnly(true);
        generalInfoForm.addComponent(factTypeName);

        this.factMappingProperties = new TextField("事实表映射字段");
        this.factMappingProperties.setRequired(false);
        this.factMappingProperties.setReadOnly(true);
        generalInfoForm.addComponent(factMappingProperties);

        this.dimensionTypeName = new TextField("维度名称");
        this.dimensionTypeName.setRequired(false);
        this.dimensionTypeName.setReadOnly(true);
        generalInfoForm.addComponent(dimensionTypeName);
        this.dimensionProperty = new TextField("维度映射字段");
        this.dimensionProperty.setRequired(false);
        this.dimensionProperty.setReadOnly(true);
        generalInfoForm.addComponent(dimensionProperty);

        this.description = new TextArea("说明");
        this.description.setRequired(false);
        this.description.setReadOnly(true);
        generalInfoForm.addComponent(description);

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
                if (self.ruleDetail != null) {
                    self.ruleDetail.executeRule();
                }
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
                    self.ruleDetail.deleteRule();
                }
            }
        });

        actionButtonsPlacementLayout.addComponent(deleteRuleButton);

        VerticalLayout spacingLayout = new VerticalLayout();
        addComponent(spacingLayout);
    }

    public void renderRuleGeneralInfo(RuleVO
                                              ruleVO) {

        this.mainSectionTitle.setValue(ruleVO.getName());

        this.ruleType.setReadOnly(false);
        String ruleType = ruleVO.getType().equalsIgnoreCase("PropertyMapping") ? "属性匹配" : ruleVO
                .getType();
        this.ruleType.setValue("" + ruleType);
        this.ruleType.setReadOnly(true);

        RuleContent ruleContent = new RuleContent(ruleVO.getContent());
        this.spaceName.setReadOnly(false);
        this.spaceName.setValue("" + ruleContent.getSpaceName());
        this.spaceName.setReadOnly(true);

        this.factTypeName.setReadOnly(false);
        this.factTypeName.setValue("" + ruleContent.getSourceType());
        this.factTypeName.setReadOnly(true);

        this.factMappingProperties.setReadOnly(false);
        this.factMappingProperties.setValue("" + ruleContent
                .getSourceProperties());
        this.factMappingProperties.setReadOnly(true);

        this.dimensionTypeName.setReadOnly(false);
        this.dimensionTypeName.setValue("" + ruleContent.getTargetType());
        this.dimensionTypeName.setReadOnly(true);

        this.dimensionProperty.setReadOnly(false);
        this.dimensionProperty.setValue("" + ruleContent.getTargetProperty());
        this.dimensionProperty.setReadOnly(true);

        this.description.setReadOnly(false);
        this.description.setValue("" + ruleVO.getDescription());
        this.description.setReadOnly(true);
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public void setRuleDetail(RuleDetail ruleDetail) {this.ruleDetail = ruleDetail; }
}

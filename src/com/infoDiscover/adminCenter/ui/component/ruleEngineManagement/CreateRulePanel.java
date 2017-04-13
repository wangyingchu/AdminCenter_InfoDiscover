package com.infoDiscover.adminCenter.ui.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.RuleEngineOperationUtil;
import com.infoDiscover.adminCenter.ui.component.common.ConfirmDialog;
import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.event.DiscoverSpaceCreatedEvent;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.RuleEngineCreatedEvent;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by sun.
 */
public class CreateRulePanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private ComboBox ruleType;
    private TextField spaceName;
    private TextField factName;
    private TextField factProperties;
    private TextField dimensionName;
    private TextField dimensionProperty;

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


        ruleType = new ComboBox("规则类型");

        ruleType.addItem("属性匹配");
        ruleType.setRequired(true);
        form.addComponent(ruleType);
        form.setReadOnly(true);

        spaceName = new TextField("信息发现空间");
        spaceName.setRequired(true);
        form.addComponent(spaceName);
        form.setReadOnly(true);

        factName = new TextField("事实表名称");
        factName.setRequired(true);
        form.addComponent(factName);
        form.setReadOnly(true);

        factProperties = new TextField("事实表属性");
        factProperties.setRequired(true);
        form.addComponent(factProperties);
        form.setReadOnly(true);


        dimensionName = new TextField("维度名称");
        dimensionName.setRequired(true);
        form.addComponent(dimensionName);
        form.setReadOnly(true);

        dimensionProperty = new TextField("维度属性");
        dimensionProperty.setRequired(true);
        form.addComponent(dimensionProperty);
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

    private void addNewRule() {
        final String ruleTypeValue = ruleType.getValue().toString();
        final String spaceNameValue = spaceName.getValue();
        final String factNameValue = factName.getValue();
        final String factPropertiesValue = factProperties.getValue();
        final String dimensionNameValue = dimensionName.getValue();
        final String dimensionPropertyValue = dimensionProperty.getValue();

        // TODO: ruleType should be selected not input
        // TODO: spaceName should be

        // check factNameValue is already existed, use factName as rule name, so need to check this
        // if existed, ask user to update it or not


//        if(ruleTypeValue==null||ruleTypeValue.trim().equals("")){
//            Notification errorNotification = new Notification("数据校验错误",
//                    "请输入信息发现空间名称", Notification.Type.ERROR_MESSAGE);
//            errorNotification.setPosition(Position.MIDDLE_CENTER);
//            errorNotification.show(Page.getCurrent());
//            errorNotification.setIcon(FontAwesome.WARNING);
//            return;
//        }
//        boolean isExistDiscoverSpaceName= InfoDiscoverSpaceOperationUtil
//                .checkDiscoverSpaceExistence(ruleTypeValue);
//        if(isExistDiscoverSpaceName){
//            Notification errorNotification = new Notification("数据校验错误",
//                    "信息发现空间 "+ruleTypeValue+" 已经存在", Notification.Type.ERROR_MESSAGE);
//            errorNotification.setPosition(Position.MIDDLE_CENTER);
//            errorNotification.show(Page.getCurrent());
//            errorNotification.setIcon(FontAwesome.WARNING);
//            return;
//        }


        //do add new logic
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " 请确认为事实表<b>" + factNameValue + "</b>创建规则.", ContentMode.HTML);
        final ConfirmDialog addRuleConfirmDialog = new ConfirmDialog();
        addRuleConfirmDialog.setConfirmMessage(confirmMessage);

        final CreateRulePanel self = this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addRuleConfirmDialog.close();
                boolean createRuleResult = RuleEngineOperationUtil.createRule(
                        ruleTypeValue,spaceNameValue, factNameValue,
                        factPropertiesValue, dimensionNameValue, dimensionPropertyValue);

                if (createRuleResult) {
                    self.containerDialog.close();

                    RuleEngineCreatedEvent ruleEngineCreatedEvent = new
                            RuleEngineCreatedEvent(factNameValue);
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

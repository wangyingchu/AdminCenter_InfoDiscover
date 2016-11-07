package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by wangychu on 11/7/16.
 */
public class QueryExploreParametersConfigInput extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;

    private TextField pageSizeEditor;
    private TextField startPageEditor;
    private TextField endPageEditor;

    public QueryExploreParametersConfigInput(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);

        MainSectionTitle setConfigParametersTitle =new MainSectionTitle("设定查询结果集参数");
        addComponent(setConfigParametersTitle);

        FormLayout propertiesEditForm = new FormLayout();
        propertiesEditForm.setMargin(false);
        propertiesEditForm.setWidth("100%");
        propertiesEditForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addComponent(propertiesEditForm);

        this.pageSizeEditor=new TextField("Page Size");
        this.pageSizeEditor.setConverter(Integer.class);
        this.pageSizeEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.pageSizeEditor);

        this.startPageEditor=new TextField("Start Page");
        this.startPageEditor.setConverter(Integer.class);
        this.startPageEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.startPageEditor);

        this.endPageEditor=new TextField("End Page");
        this.endPageEditor.setConverter(Integer.class);
        this.endPageEditor.addValidator(new IntegerRangeValidator("该项属性值必须为INT类型", null,null));
        propertiesEditForm.addComponent(this.endPageEditor);

        TextField resultNumberEditor=new TextField("Result Number");
        resultNumberEditor.setConverter(Integer.class);
        resultNumberEditor.addValidator(new LongRangeValidator("该项属性值必须为LONG类型", null,null));
        propertiesEditForm.addComponent(resultNumberEditor);

        CheckBox distinctModeCheck=new CheckBox("Distinct Mode");
        propertiesEditForm.addComponent(distinctModeCheck);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        footer.setWidth("100%");
        Button confirmButton=new Button("确定", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //addNewTypeData();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);
        addComponent(footer);
    }

    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setPageSize(int pageSize){
        this.pageSizeEditor.setValue(""+pageSize);
    }

    public void setStartPage(int startPage){
        this.startPageEditor.setValue(""+startPage);
    }

    public void setEndPage(int endPage){
        this.endPageEditor.setValue(""+endPage);
    }

}

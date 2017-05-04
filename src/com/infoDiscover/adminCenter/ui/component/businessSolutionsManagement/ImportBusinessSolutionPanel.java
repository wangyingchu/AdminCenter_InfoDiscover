package com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 5/5/17.
 */
public class ImportBusinessSolutionPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;

    public ImportBusinessSolutionPanel(UserClientInfo userClientInfo){
        this.currentUserClientInfo=userClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Business Solution Section
        MainSectionTitle addNewBusinessSolutionSectionTitle=new MainSectionTitle("导入新的业务解决方案模板");
        addComponent(addNewBusinessSolutionSectionTitle);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        //businessSolutionName = new TextField("业务解决方案名称");
        //businessSolutionName.setRequired(true);
        //form.addComponent(businessSolutionName);
        form.setReadOnly(true);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button addButton=new Button("导入业务解决方案模板", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new business solution logic */
                //addNewBusinessSolution();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
        footer.addComponent(addButton);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

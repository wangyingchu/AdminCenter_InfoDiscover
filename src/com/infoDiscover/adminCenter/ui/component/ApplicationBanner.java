package com.infoDiscover.adminCenter.ui.component;

import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by wangychu on 9/28/16.
 */
public class ApplicationBanner extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    public ApplicationBanner(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setHeight("55px");
        setWidth("100%");
        setSpacing(true);
        this.setStyleName("ui_appBanner");

        HorizontalLayout leftElementContainer=new HorizontalLayout();
        HorizontalLayout rightElementContainer=new HorizontalLayout();
        this.addComponent(leftElementContainer);
        this.addComponent(rightElementContainer);
        this.setComponentAlignment(leftElementContainer, Alignment.MIDDLE_LEFT);
        this.setComponentAlignment(rightElementContainer, Alignment.MIDDLE_RIGHT);

        Image applicationLogo = new Image();
        applicationLogo.setSource(new ThemeResource("imgs/productLogo_ID.png"));
        leftElementContainer. addComponent(applicationLogo);

        Label applicationTitle = new Label("高价值密度信息发现平台 - 系统管理终端");
        applicationTitle.addStyleName("ui_appTitle");
        leftElementContainer. addComponent(applicationTitle);

        Label loginUserName = new Label( FontAwesome.MALE.getHtml() + " 登陆用户id", ContentMode.HTML);
        rightElementContainer. addComponent(loginUserName);
        rightElementContainer.setComponentAlignment(loginUserName, Alignment.MIDDLE_CENTER);

        Button signOutButton = new Button("注销用户");
        signOutButton.setIcon(FontAwesome.SIGN_OUT);
        signOutButton.addStyleName("borderless-colored");
        rightElementContainer.addComponent(signOutButton);

        signOutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                UI.getCurrent().getSession().close();
            }
        });

        Label spaceDivLabel1=new Label("|");
        rightElementContainer. addComponent(spaceDivLabel1);
        rightElementContainer.setComponentAlignment(spaceDivLabel1, Alignment.MIDDLE_CENTER);

        Button viewfunctionDescButton = new Button("关于");
        viewfunctionDescButton.setIcon(FontAwesome.INFO_CIRCLE);
        viewfunctionDescButton.addStyleName("borderless");
        rightElementContainer. addComponent(viewfunctionDescButton);
        viewfunctionDescButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window("www.viewfunction.com");
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(800.0f, Unit.PIXELS);
                window.center();
                BrowserFrame viewfunctionWebPage = new BrowserFrame("www.viewfunction.com", new ExternalResource(
                        "http://www.viewfunction.com"));
                viewfunctionWebPage.setSizeFull();
                window.setContent(viewfunctionWebPage);
                UI.getCurrent().addWindow(window);
            }
        });
        Label supportLabel2=new Label(" ");
        rightElementContainer. addComponent(supportLabel2);
    }
}


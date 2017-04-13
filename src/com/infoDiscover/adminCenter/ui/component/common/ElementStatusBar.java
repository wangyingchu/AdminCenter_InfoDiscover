package com.infoDiscover.adminCenter.ui.component.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ElementStatusBar extends HorizontalLayout {

    private Label infoDiscoverSpaceNameProp;
    private HorizontalLayout statusElementsBarLayout;

    public ElementStatusBar(){
        setHeight("32px");
        setWidth("100%");
        setSpacing(false);
        this.setStyleName("ui_compElementStatusBar");

        HorizontalLayout statusElementContainer=new HorizontalLayout();
        this.addComponent(statusElementContainer);

        infoDiscoverSpaceNameProp = new Label( FontAwesome.TERMINAL.getHtml(), ContentMode.HTML);
        infoDiscoverSpaceNameProp.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(infoDiscoverSpaceNameProp);

        statusElementsBarLayout=new HorizontalLayout();
        statusElementsBarLayout.setWidth("100%");
        statusElementsBarLayout.setStyleName("ui_appLightDarkMessage");
        statusElementContainer.addComponent(statusElementsBarLayout);
    }

    public void setInfoDiscoverSpaceName(String discoverSpaceName){
        this.infoDiscoverSpaceNameProp.setValue(FontAwesome.TERMINAL.getHtml()  + FontAwesome.CUBE.getHtml()+ " " +discoverSpaceName);
    }

    public void clearInfoDiscoverSpaceName(){
        this.infoDiscoverSpaceNameProp.setValue(FontAwesome.TERMINAL.getHtml());
    }

    // rule engine
    public void setRuleName(String ruleName) {
        this.infoDiscoverSpaceNameProp.setValue(FontAwesome.TERMINAL.getHtml()  + FontAwesome
                .CUBE.getHtml()+ " " +ruleName);
    }

    public void clearRuleName(){
        this.infoDiscoverSpaceNameProp.setValue(FontAwesome.TERMINAL.getHtml());
    }

    public void addStatusElement(Component barElementComponent){
        statusElementsBarLayout.addComponent(barElementComponent);
    }

    public void clearStatusElements(){
        statusElementsBarLayout.removeAllComponents();
    }
}

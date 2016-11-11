package com.infoDiscover.adminCenter.ui.component.common;

import org.vaadin.teemu.VaadinIcons;

/**
 * Created by wangychu on 11/11/16.
 */
public class UICommonElementsUtil {

    public static String generateMovableWindowTitleWithFormat(String titleText){
        String resultTitle= VaadinIcons.MODAL.getHtml()+"<span style='font-weight:200;color:#197de1;font-size:20px'>"+" "+titleText.trim()+"</span>";
        return resultTitle;
    }
}

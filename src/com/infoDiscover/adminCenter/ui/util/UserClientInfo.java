package com.infoDiscover.adminCenter.ui.util;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.server.WebBrowser;

import java.io.Serializable;

/**
 * Created by wangychu on 9/28/16.
 */
public class UserClientInfo implements Serializable {

    private static final long serialVersionUID = 3930971222892866428L;
    private WebBrowser userWebBrowserInfo;
    private Blackboard eventBlackBoard;

    public WebBrowser getUserWebBrowserInfo() {
        return userWebBrowserInfo;
    }

    public void setUserWebBrowserInfo(WebBrowser userWebBrowserInfo) {
        this.userWebBrowserInfo = userWebBrowserInfo;
    }

    public Blackboard getEventBlackBoard() {
        return eventBlackBoard;
    }

    public void setEventBlackBoard(Blackboard eventBlackBoard) {
        this.eventBlackBoard = eventBlackBoard;
    }

}

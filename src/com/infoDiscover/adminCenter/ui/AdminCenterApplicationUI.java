package com.infoDiscover.adminCenter.ui;

import com.github.wolfie.blackboard.Blackboard;
import com.infoDiscover.adminCenter.logic.common.CustomizedConverterFactory;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataListVO;
import com.infoDiscover.adminCenter.ui.component.ApplicationBanner;
import com.infoDiscover.adminCenter.ui.component.ApplicationContent;
import com.infoDiscover.adminCenter.ui.component.businessSolutionsManagement.event.*;
import com.infoDiscover.adminCenter.ui.component.ruleEngineManagement.event.*;
import com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.event.*;
import com.infoDiscover.adminCenter.ui.util.RuntimeWindowsRepository;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangychu on 9/28/16.
 */

@Theme("infodiscover")
//@Title("High Value Density Information Discovery Platform Administration")
@Title("[ InfoDiscover ] - 高价值密度信息发现平台 系统管理")
@PreserveOnRefresh
public class AdminCenterApplicationUI extends UI {

    @VaadinServletConfiguration(productionMode = false, ui = AdminCenterApplicationUI.class)
    public static class Servlet extends VaadinServlet {

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            //getService().addSessionInitListener(......);
        }
    }

    @Override
    public void init(VaadinRequest request) {
        //set CustomizedConverterFactory for text fields string -> number convert logic
        VaadinSession.getCurrent().setConverterFactory(new CustomizedConverterFactory());

        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        UserClientInfo currentUserClientInfo=new UserClientInfo();
        currentUserClientInfo.setUserWebBrowserInfo(webBrowser);

        Blackboard BLACKBOARD = new Blackboard();
        BLACKBOARD.enableLogging();
        //DiscoverSpace Management
        BLACKBOARD.register(DiscoverSpaceComponentSelectedEvent.DiscoverSpaceComponentSelectedListener.class,
                DiscoverSpaceComponentSelectedEvent.class);
        BLACKBOARD.register(DiscoverSpaceCreatedEvent.DiscoverSpaceCreatedListener.class,
                DiscoverSpaceCreatedEvent.class);
        BLACKBOARD.register(DiscoverSpaceDeletedEvent.DiscoverSpaceDeletedListener.class,
                DiscoverSpaceDeletedEvent.class);
        BLACKBOARD.register(DiscoverSpaceTypeDataInstanceQueryRequiredEvent.DiscoverSpaceTypeDataInstanceQueryRequiredListener.class,
                DiscoverSpaceTypeDataInstanceQueryRequiredEvent.class);
        BLACKBOARD.register(DiscoverSpaceOpenProcessingDataListEvent.DiscoverSpaceOpenProcessingDataListListener.class,
                DiscoverSpaceOpenProcessingDataListEvent.class);
        BLACKBOARD.register(DiscoverSpaceAddProcessingDataEvent.DiscoverSpaceAddProcessingDataListener.class,
                DiscoverSpaceAddProcessingDataEvent.class);
        BLACKBOARD.register(DiscoverSpaceRemoveProcessingDataEvent.DiscoverSpaceRemoveProcessingDataListener.class,
                DiscoverSpaceRemoveProcessingDataEvent.class);
        BLACKBOARD.register(DiscoverSpaceLaunchDataAnalyzeApplicationEvent.DiscoverSpaceLaunchDataAnalyzeApplicationListener.class,
                DiscoverSpaceLaunchDataAnalyzeApplicationEvent.class);
        //BusinessSolution Management
        BLACKBOARD.register(BusinessSolutionCreatedEvent.BusinessSolutionCreatedListener.class,
                BusinessSolutionCreatedEvent.class);
        BLACKBOARD.register(BusinessSolutionDeletedEvent.BusinessSolutionDeletedListener.class,
                BusinessSolutionDeletedEvent.class);
        BLACKBOARD.register(BusinessSolutionComponentSelectedEvent.BusinessSolutionComponentSelectedListener.class,
                BusinessSolutionComponentSelectedEvent.class);
        BLACKBOARD.register(BusinessSolutionExportEvent.BusinessSolutionExportListener.class,
                BusinessSolutionExportEvent.class);

        currentUserClientInfo.setEventBlackBoard(BLACKBOARD);

         // rule engine
        BLACKBOARD.register(RuleEngineComponentSelectedEvent.RuleEngineComponentSelectedListener
                .class,RuleEngineComponentSelectedEvent.class);
        BLACKBOARD.register(RuleEngineDeletedEvent.RuleEngineDeletedListener.class,
                RuleEngineDeletedEvent.class);
        BLACKBOARD.register(RuleEngineCreatedEvent.RuleEngineCreatedListener.class,
                RuleEngineCreatedEvent.class);
        BLACKBOARD.register(RuleEngineExecutedEvent.RuleEngineExecutedListener.class,
                RuleEngineExecutedEvent.class);

        Map<String,ProcessingDataListVO> discoverSpacesProcessingDataMap=new HashMap<String,ProcessingDataListVO>();
        currentUserClientInfo.setDiscoverSpacesProcessingDataMap(discoverSpacesProcessingDataMap);

        RuntimeWindowsRepository runtimeWindowsRepository=new RuntimeWindowsRepository();
        currentUserClientInfo.setRuntimeWindowsRepository(runtimeWindowsRepository);

        if (browserCantRenderFontsConsistently()) {
            getPage().getStyles().add(".v-app.v-app.v-app {font-family: Sans-Serif;}");
        }
        Responsive.makeResponsive(this);

        VerticalLayout rootLayout = new VerticalLayout();
        // sure it's 100% sized, and remove unwanted margins
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);

        ApplicationBanner applicationBanner=new ApplicationBanner(currentUserClientInfo);
        rootLayout.addComponent(applicationBanner);

        ApplicationContent applicationContent=new ApplicationContent(currentUserClientInfo);
        rootLayout.addComponent(applicationContent);
        rootLayout.setExpandRatio(applicationContent, 1.0F);

        setContent(rootLayout);
    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592
        // IE8 also has randomness in its font rendering...
        return getPage().getWebBrowser().getBrowserApplication().contains("PhantomJS")|| (getPage().getWebBrowser()
                .isIE() && getPage().getWebBrowser().getBrowserMajorVersion() <= 9);
    }
}

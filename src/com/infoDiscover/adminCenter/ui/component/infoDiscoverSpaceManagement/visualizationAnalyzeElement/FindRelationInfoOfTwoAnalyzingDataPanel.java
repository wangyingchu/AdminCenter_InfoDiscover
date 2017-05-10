package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.visualizationAnalyzeElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationablesPathVO;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Created by wangychu on 4/13/17.
 */
public class FindRelationInfoOfTwoAnalyzingDataPanel extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private String discoverSpaceName;
    private Label analyzingData1IdLabel;
    private Label analyzingData2IdLabel;
    private BrowserFrame pathsDetailGraphBrowserFrame;
    private RelationablesPathInfoList relationablesPathInfoList;
    private int browserWindowHeight;
    private final static String shortestPathInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesShortestPathExploreGraph.html";
    private final static String specifiedPathInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesSpecifiedPathExploreGraph.html";
    private final static String pathsInfoGraphBaseAddress= AdminCenterPropertyHandler.
            getPropertyValue(AdminCenterPropertyHandler.INFO_ANALYSE_SERVICE_ROOT_LOCATION)+"infoAnalysePages/typeInstanceRelationAnalyse/typeInstancesPathsExploreGraph.html";
    public FindRelationInfoOfTwoAnalyzingDataPanel(UserClientInfo userClientInfo){
        this.setMargin(true);
        this.currentUserClientInfo = userClientInfo;
        browserWindowHeight= UI.getCurrent().getPage().getBrowserWindowHeight();
        HorizontalLayout twoAnalyzingDataInfoContainerLayout=new HorizontalLayout();
        twoAnalyzingDataInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        twoAnalyzingDataInfoContainerLayout.addStyleName("ui_appSectionLightDiv");
        this.addComponent(twoAnalyzingDataInfoContainerLayout);

        HorizontalLayout analyzingDataInfoContainerLayout=new HorizontalLayout();
        Label analyzingData_1Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (1) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_1Label);

        HorizontalLayout spacingDiv01Layout=new HorizontalLayout();
        spacingDiv01Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv01Layout);

        analyzingData1IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData1IdLabel);

        HorizontalLayout spacingDiv02Layout=new HorizontalLayout();
        spacingDiv02Layout.setWidth(20,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv02Layout);

        Label analyzingData_2Label=new Label(FontAwesome.SQUARE_O.getHtml()+" 数据项 (2) : ", ContentMode.HTML);
        analyzingDataInfoContainerLayout.addComponent(analyzingData_2Label);

        HorizontalLayout spacingDiv03Layout=new HorizontalLayout();
        spacingDiv03Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv03Layout);

        analyzingData2IdLabel=new Label("-");
        analyzingDataInfoContainerLayout.addComponent(analyzingData2IdLabel);

        HorizontalLayout spacingDiv04Layout=new HorizontalLayout();
        spacingDiv04Layout.setWidth(10,Unit.PIXELS);
        analyzingDataInfoContainerLayout.addComponent(spacingDiv04Layout);

        Button showShortPathRelationButton=new Button("发现最短路径关系");
        showShortPathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showShortPathRelationButton);
        showShortPathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showShortPathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        showShortPathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showShortestPathRelation();
            }
        });

        Button showAllPathRelationButton=new Button("发现所有路径关系");
        showAllPathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showAllPathRelationButton);
        showAllPathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAllPathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(showAllPathRelationButton, Alignment.TOP_LEFT);
        showAllPathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showAllPathRelations();
            }
        });

        Button showShortest5PathRelationButton=new Button("发现最短５条路径关系");
        showShortest5PathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showShortest5PathRelationButton);
        showShortest5PathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showShortest5PathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(showShortest5PathRelationButton, Alignment.TOP_LEFT);
        showShortest5PathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showShortest5PathRelations();
            }
        });

        Button showLongest5PathRelationButton=new Button("发现最长５条路径关系");
        showLongest5PathRelationButton.setIcon(FontAwesome.SEARCH);
        analyzingDataInfoContainerLayout.addComponent(showLongest5PathRelationButton);
        showLongest5PathRelationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showLongest5PathRelationButton.addStyleName(ValoTheme.BUTTON_SMALL);
        analyzingDataInfoContainerLayout.setComponentAlignment(showLongest5PathRelationButton, Alignment.TOP_LEFT);
        showLongest5PathRelationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showLongest5PathRelations();
            }
        });

        twoAnalyzingDataInfoContainerLayout.addComponent(analyzingDataInfoContainerLayout);

        HorizontalLayout pathsDetailInfoContainerLayout=new HorizontalLayout();
        pathsDetailInfoContainerLayout.setWidth(100,Unit.PERCENTAGE);
        pathsDetailInfoContainerLayout.setHeight(browserWindowHeight-200,Unit.PIXELS);
        this.addComponent(pathsDetailInfoContainerLayout);
        this.addComponent(pathsDetailInfoContainerLayout);

        relationablesPathInfoList=new RelationablesPathInfoList(this.currentUserClientInfo);
        relationablesPathInfoList.setContainerFindRelationInfoOfTwoAnalyzingDataPanel(this);
        pathsDetailInfoContainerLayout.addComponent(relationablesPathInfoList);
        pathsDetailGraphBrowserFrame = new BrowserFrame();
        pathsDetailGraphBrowserFrame.setSizeFull();
        pathsDetailGraphBrowserFrame.setHeight(browserWindowHeight-200,Unit.PIXELS);
        pathsDetailInfoContainerLayout.addComponent(pathsDetailGraphBrowserFrame);
        pathsDetailInfoContainerLayout.setExpandRatio(pathsDetailGraphBrowserFrame,1f);
    }

    public void addFirstAnalyzingData(String processingDataId){
        analyzingData1IdLabel.setValue(processingDataId);
        cleanCurrentResult();
    }

    public void addSecondAnalyzingData(String processingDataId){
        analyzingData2IdLabel.setValue(processingDataId);
        cleanCurrentResult();
    }

    private void cleanCurrentResult(){
        relationablesPathInfoList.renderRelationablesPathsList(null);
        this.pathsDetailGraphBrowserFrame.setSource(null);
    }

    private void showShortestPathRelation(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        RelationablesPathVO shortestPath=InfoDiscoverSpaceOperationUtil.getShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(shortestPath!=null){
            List<RelationablesPathVO> pathInfoList=new ArrayList<>();
            pathInfoList.add(shortestPath);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.shortestPathInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showAllPathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getAllPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
            this.pathsDetailGraphBrowserFrame.setSource(null);
            /*
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.allPathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            */
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showShortest5PathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathNumber=5&pathType=SHORTEST"+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getShortestPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue(),5);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    private void showLongest5PathRelations(){
        if(analyzingData1IdLabel.getValue().equals("-")||analyzingData2IdLabel.getValue().equals("-")){
            Notification errorNotification = new Notification("数据校验错误","请选择待发现关联关系的两项数据项", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return;
        }
        boolean hasShortestPath=InfoDiscoverSpaceOperationUtil.hasShortestPathBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue());
        if(hasShortestPath){
            long timeStampPostValue=new Date().getTime();
            String relationableAId=analyzingData1IdLabel.getValue();
            String relationableBId=analyzingData2IdLabel.getValue();
            String relationableAIdCode=relationableAId.replaceAll("#","%23");
            relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
            String relationableBIdCode=relationableBId.replaceAll("#","%23");
            relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
            String graphLocationFullAddress=
                    this.pathsInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                            "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+"&pathNumber=5&pathType=LONGEST"+
                            "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
            this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
            List<RelationablesPathVO> pathInfoList= InfoDiscoverSpaceOperationUtil.getLongestPathsBetweenTwoRelationables(this.getDiscoverSpaceName(),analyzingData1IdLabel.getValue(),analyzingData2IdLabel.getValue(),5);
            relationablesPathInfoList.renderRelationablesPathsList(pathInfoList);
        }else{
            this.pathsDetailGraphBrowserFrame.setSource(null);
            Notification resultNotification = new Notification("未发现关联路径",
                    "在数据项　"+analyzingData1IdLabel.getValue()+" 与　"+analyzingData2IdLabel.getValue()+"之间未发现关联路径", Notification.Type.WARNING_MESSAGE);
            resultNotification.setPosition(Position.BOTTOM_RIGHT);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }
    }

    public void showRelationablesPathGraph(RelationablesPathVO pathInfo){
        Stack<RelationValueVO> relationsStack=pathInfo.getPathRelationRoute();
        StringBuffer relationsListStr=new StringBuffer();
        for(int i=0;i<relationsStack.size();i++){
            String currentDataId=relationsStack.elementAt(i).getId();
            String enCodedID=currentDataId.replaceFirst("#","%23").replaceFirst(":","%3a");
            if(i!=relationsStack.size()-1){
                relationsListStr.append(enCodedID+",");
            }else{
                relationsListStr.append(enCodedID);
            }
        }
        this.pathsDetailGraphBrowserFrame.setSource(null);
        long timeStampPostValue=new Date().getTime();
        String relationableAId=analyzingData1IdLabel.getValue();
        String relationableBId=analyzingData2IdLabel.getValue();
        String relationableAIdCode=relationableAId.replaceAll("#","%23");
        relationableAIdCode=relationableAIdCode.replaceAll(":","%3a");
        String relationableBIdCode=relationableBId.replaceAll("#","%23");
        relationableBIdCode=relationableBIdCode.replaceAll(":","%3a");
        String graphLocationFullAddress=
                this.specifiedPathInfoGraphBaseAddress+"?discoverSpace="+discoverSpaceName+
                        "&relationableAId="+relationableAIdCode+"&relationableBId="+relationableBIdCode+
                        "&pathRelationIds="+relationsListStr+

                        "&timestamp="+timeStampPostValue+"&graphHeight="+(browserWindowHeight-220);
        this.pathsDetailGraphBrowserFrame.setSource(new ExternalResource(graphLocationFullAddress));
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }
}

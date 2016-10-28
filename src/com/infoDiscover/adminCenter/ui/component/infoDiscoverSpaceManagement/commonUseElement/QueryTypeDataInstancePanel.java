package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;

import com.infoDiscover.adminCenter.ui.component.common.MainSectionTitle;
import com.infoDiscover.adminCenter.ui.component.common.SectionActionsBar;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.pvv.criteriabuilder.CriteriaBuilder;
import com.pvv.criteriabuilder.CriteriaField;
import com.vaadin.addon.modeltable.ModelTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by wangychu on 10/26/16.
 */
public class QueryTypeDataInstancePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private String discoverSpaceName;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private Label operationTitle;
    private MainSectionTitle queryTypeDataInstanceSectionTitle;
    private SectionActionsBar dataTypeNoticeActionsBar;

    private MenuBar.MenuItem queryTypeDefinedPropertyMenuItem;
    private MenuBar.MenuItem queryCustomPropertyMenuItem;
    private Button queryButton;
    private VerticalLayout queryConditionItemsContainerLayout;



    public static Map<Integer, Item> itemFieldMap = new HashMap<>();


    public QueryTypeDataInstancePanel(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        setSpacing(true);
        setMargin(true);
        this.queryTypeDataInstanceSectionTitle = new MainSectionTitle("---");
        addComponent(this.queryTypeDataInstanceSectionTitle);
        dataTypeNoticeActionsBar = new SectionActionsBar(new Label("---", ContentMode.HTML));
        addComponent(dataTypeNoticeActionsBar);



        HorizontalSplitPanel typeDataInstanceQuerySplitPanel = new HorizontalSplitPanel();
        typeDataInstanceQuerySplitPanel.setSizeFull();
        typeDataInstanceQuerySplitPanel.setSplitPosition(420, Unit.PIXELS);
        addComponent(typeDataInstanceQuerySplitPanel);






       // HorizontalLayout typeDataInstanceQueryContainerLayout=new HorizontalLayout();
        //typeDataInstanceQueryContainerLayout.setWidth(100,Unit.PERCENTAGE);
        //addComponent(typeDataInstanceQueryContainerLayout);










        VerticalLayout queryConditionInputContainerLayout=new VerticalLayout();
       // queryConditionInputContainerLayout.setWidth(350,Unit.PIXELS);
        //queryConditionInputContainerLayout.setWidth(480,Unit.PIXELS);




        queryConditionInputContainerLayout.addStyleName("ui_appElementRightSideSpacing");
       // typeDataInstanceQueryContainerLayout.addComponent(queryConditionInputContainerLayout);
        typeDataInstanceQuerySplitPanel.setFirstComponent(queryConditionInputContainerLayout);



        this.operationTitle = new Label(FontAwesome.LIST_UL.getHtml() + " ---", ContentMode.HTML);
        this.operationTitle.addStyleName(ValoTheme.LABEL_SMALL);
        this.operationTitle.addStyleName("ui_appStandaloneElementPadding");
        this.operationTitle.addStyleName("ui_appSectionLightDiv");
        queryConditionInputContainerLayout.addComponent(operationTitle);

        MenuBar dimensionInstanceOperationMenuBar = new MenuBar();
        dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        dimensionInstanceOperationMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

        this.queryTypeDefinedPropertyMenuItem = dimensionInstanceOperationMenuBar.addItem("类型预定义属性", FontAwesome.FILTER, null);
        this.queryTypeDefinedPropertyMenuItem.addItem("类型预定义属性", FontAwesome.CODE_FORK, null);




        this.queryCustomPropertyMenuItem = dimensionInstanceOperationMenuBar.addItem("自定义属性", FontAwesome.FILTER, null);
        this.queryCustomPropertyMenuItem.addItem("类型预定义属性", FontAwesome.CODE_FORK, null);

        queryConditionInputContainerLayout.addComponent(dimensionInstanceOperationMenuBar);


        this.queryConditionItemsContainerLayout=new VerticalLayout();
        this.queryConditionItemsContainerLayout.setWidth(100,Unit.PERCENTAGE);



        for(int i=0;i<3;i++) {
            QueryConditionItem queryConditionItem = new QueryConditionItem(this.currentUserClientInfo);
            this.queryConditionItemsContainerLayout.addComponent(queryConditionItem);
        }

        Panel queryConditionInputContainerPanel=new Panel();

        queryConditionInputContainerLayout.addComponent(queryConditionInputContainerPanel);

        queryConditionInputContainerPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        queryConditionInputContainerPanel.setHeight(450,Unit.PIXELS);
        queryConditionInputContainerPanel.setContent(this.queryConditionItemsContainerLayout);




        VerticalLayout spacingLayout0=new VerticalLayout();
        spacingLayout0.setWidth(100,Unit.PERCENTAGE);
        queryConditionInputContainerLayout.addComponent(spacingLayout0);
        spacingLayout0.addStyleName("ui_appSectionLightDiv");

        this.queryButton=new Button("---", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new data logic */
                //addNewTypeData();
            }
        });
        this.queryButton.setIcon(FontAwesome.SEARCH);
        this.queryButton.addStyleName("primary");
        this.queryButton.addStyleName(ValoTheme.BUTTON_SMALL);
        queryConditionInputContainerLayout.addComponent(this.queryButton);
        queryConditionInputContainerLayout.setComponentAlignment(this.queryButton,Alignment.MIDDLE_CENTER);


        VerticalLayout spacingLayout1=new VerticalLayout();
        queryConditionInputContainerLayout.addComponent(spacingLayout1);


















        VerticalLayout queryResultContainerLayout=new VerticalLayout();
        queryResultContainerLayout.setWidth(100,Unit.PERCENTAGE);
        //typeDataInstanceQueryContainerLayout.addComponent(queryResultContainerLayout);
        //typeDataInstanceQueryContainerLayout.setExpandRatio(queryResultContainerLayout,1.0f);
        typeDataInstanceQuerySplitPanel.setSecondComponent(queryResultContainerLayout);





        Label operationResultTitle = new Label(FontAwesome.DATABASE.getHtml() + " 查询结果", ContentMode.HTML);
        operationResultTitle.addStyleName(ValoTheme.LABEL_SMALL);
        operationResultTitle.addStyleName("ui_appStandaloneElementPadding");
        operationResultTitle.addStyleName("ui_appSectionLightDiv");
        queryResultContainerLayout.addComponent(operationResultTitle);



        ModelTable<Item> table1 = buildModelTable(0);
        table1.setTitleCaption("Direction.RIGHT [ ColumnSize: " + 3 + "]");
        table1.setItemDirection(ModelTable.Direction.RIGHT, 3);


        Item item0=new Item();
        item0.setField01("fdwefwf f");
        item0.setField02("fdwefwf fv");
        item0.setField03("fdwefwfw");
        item0.setField04("fdwefwf3");
        item0.setField05("fdwefwf f");

        table1.setItem(item0);


        queryResultContainerLayout.addComponent(table1);

        final Collection<CriteriaField> fields = new HashSet<CriteriaField>();
        fields.add(new CriteriaField("name", "Name"));
        fields.add(new CriteriaField("code", "Code", CriteriaField.ClassField.INTEGER));
        fields.add(new CriteriaField("summa", "Summa", CriteriaField.ClassField.FLOAT));
        fields.add(new CriteriaField("date", "Date", CriteriaField.ClassField.DATE));
        fields.add(new CriteriaField("city", "City"));
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(fields);
        queryResultContainerLayout.addComponent(criteriaBuilder);








    }



    public class Item implements Serializable {
        private String field01;
        private String field02;
        private String field03;
        private String field04;
        private String field05;

        public String getField01() {
            return field01;
        }

        public void setField01(String field01) {
            this.field01 = field01;
        }

        public String getField02() {
            return field02;
        }

        public void setField02(String field02) {
            this.field02 = field02;
        }

        public String getField03() {
            return field03;
        }

        public void setField03(String field03) {
            this.field03 = field03;
        }

        public String getField04() {
            return field04;
        }

        public void setField04(String field04) {
            this.field04 = field04;
        }

        public String getField05() {
            return field05;
        }

        public void setField05(String field05) {
            this.field05 = field05;
        }
    }


    private ModelTable<Item> buildModelTable(final int dataIndex) {
        final Item item = itemFieldMap.get(dataIndex);
        final ModelTable<Item> modelTable = new ModelTable<>(Item.class);


        modelTable.setItem(item);
        modelTable.addMenuItem("Edit", FontAwesome.EDIT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                item.setField01("Edit :" + dataIndex);
                modelTable.setItem(item);
                Notification.show("Edit : " + dataIndex);
            }
        });
        modelTable.addMenuItem("Reload", FontAwesome.CIRCLE_O_NOTCH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                Notification.show("Refresh[removeItem] : " + dataIndex);
                modelTable.removeItem();
            }
        });


        return modelTable;
    }
















    public Window getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    @Override
    public void attach() {
        super.attach();
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询维度数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 维度属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.TAGS.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询维度数据");
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询事实数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 事实属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.CLONE.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询事实数据");
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            this.queryTypeDataInstanceSectionTitle.setValue("查询关系数据");
            this.operationTitle.setValue(FontAwesome.LIST_UL.getHtml() +" 查询条件 ( 关系属性 ) :");
            Label sectionActionBarLabel=new Label(FontAwesome.CUBE.getHtml()+" "+getDiscoverSpaceName()+" /"+FontAwesome.SHARE_ALT.getHtml()+" "+this.getDataInstanceTypeName(), ContentMode.HTML);
            dataTypeNoticeActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
            this.queryButton.setCaption("查询关系数据");
        }
    }
}



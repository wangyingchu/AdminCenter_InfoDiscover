package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.addon.pagination.Pagination;
import com.vaadin.addon.pagination.PaginationChangeListener;
import com.vaadin.addon.pagination.PaginationResource;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wangychu on 11/7/16.
 */
public class TypeDataInstanceList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private Label queryResultCountLabel;
    private Button querySQLButton;
    private Table typeDataInstanceTable;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private String discoverSpaceName;
    private String querySQL="";
    private SimpleDateFormat dateTypePropertyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HorizontalLayout paginationContainerLayout;
    private int tablePageSize=20;

    public TypeDataInstanceList(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        this.setWidth(100,Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);

        HorizontalLayout queryResultSummaryInfoContainerLayout=new HorizontalLayout();
        addComponent(queryResultSummaryInfoContainerLayout);
        Label queryResultCountInfo=new Label(FontAwesome.LIST_OL.getHtml()+" 类型数据总量: ", ContentMode.HTML);
        queryResultCountInfo.addStyleName(ValoTheme.LABEL_TINY);
        queryResultSummaryInfoContainerLayout.addComponent(queryResultCountInfo);
        queryResultSummaryInfoContainerLayout.setComponentAlignment(queryResultCountInfo,Alignment.MIDDLE_LEFT);
        this.queryResultCountLabel=new Label("--");
        this.queryResultCountLabel.addStyleName("ui_appFriendlyElement");
        queryResultSummaryInfoContainerLayout.addComponent(this.queryResultCountLabel);
        queryResultSummaryInfoContainerLayout.setComponentAlignment(this.queryResultCountLabel,Alignment.MIDDLE_LEFT);
        this.querySQLButton=new Button("查询条件 SQL");
        this.querySQLButton.setIcon(FontAwesome.FILE_TEXT_O);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_TINY);
        this.querySQLButton.addStyleName(ValoTheme.BUTTON_LINK);
        this.querySQLButton.setEnabled(false);
        this.querySQLButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showQuerySQL();
            }
        });
        queryResultSummaryInfoContainerLayout.addComponent(this.querySQLButton);

        this.typeDataInstanceTable=new Table();
        this.typeDataInstanceTable.setWidth(100, Unit.PERCENTAGE);
        this.typeDataInstanceTable.setSelectable(true);
        this.typeDataInstanceTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        addComponent(this.typeDataInstanceTable);

        this.paginationContainerLayout=new HorizontalLayout();
        this.paginationContainerLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(this.paginationContainerLayout);
    }

    public void setQuerySQL(String querySQL){
        this.querySQL=querySQL;
    }

    public void setTypeDataInstanceListHeight(int tableHeight){
        this.typeDataInstanceTable.setHeight(tableHeight,Unit.PIXELS);
    }

    public void renderTypeDataInstanceList(List<PropertyTypeVO> queryParameters,List<MeasurableValueVO> queryResults){
        this.querySQLButton.setEnabled(true);
        this.queryResultCountLabel.setValue(""+queryResults.size());

        List<PropertyTypeVO> typePropertiesInfoList=null;
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveDimensionTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveFactTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(getDataInstanceTypeKind())){
            typePropertiesInfoList=InfoDiscoverSpaceOperationUtil.retrieveRelationTypePropertiesInfo(getDiscoverSpaceName(),getDataInstanceTypeName());
        }
        if(queryParameters!=null) {
            setAdditionalQueryParams(typePropertiesInfoList, queryParameters);
        }

        Container dataContainer=this.typeDataInstanceTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.typeDataInstanceTable.setContainerDataSource(queryResultDataContainer);
        this.typeDataInstanceTable.addContainerProperty(" ID",String.class,"");
        this.typeDataInstanceTable.setColumnIcon(" ID",FontAwesome.KEY);
        if(typePropertiesInfoList!=null){
            for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){
                this.typeDataInstanceTable.addContainerProperty(currentPropertyTypeVO.getPropertyName(), String.class, "");
            }
        }
        this.typeDataInstanceTable.addContainerProperty(" 操作",TypeDataInstanceTableRowActions.class,null);
        this.typeDataInstanceTable.setColumnIcon(" 操作",FontAwesome.WRENCH);
        this.typeDataInstanceTable.setColumnWidth(" 操作", 130);
        typeDataInstanceTable.setPageLength(this.getTablePageSize());

        this.paginationContainerLayout.removeAllComponents();
        int startPage=queryResults.size()>0?1:0;
        Pagination typeDataInstancePagination=createPagination(queryResults.size(), startPage);
        typeDataInstancePagination.setItemsPerPageVisible(false);
        typeDataInstancePagination.addPageChangeListener(new PaginationChangeListener() {
            @Override
            public void changed(PaginationResource event) {
                //typeDataInstanceTable.setPageLength(event.limit());
                typeDataInstanceTable.setCurrentPageFirstItemIndex(event.offset());
            }
        });
        this.paginationContainerLayout.addComponent(typeDataInstancePagination);

        if (queryResults!=null){
            for(int i=0;i<queryResults.size();i++){
                MeasurableValueVO currentMeasurableValueVO=queryResults.get(i);
                Item newRecord=this.typeDataInstanceTable.addItem("typeInstance_index_"+i);
                newRecord.getItemProperty(" ID").setValue(currentMeasurableValueVO.getId());
                for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){
                    PropertyValueVO currentPropertyValueVO=currentMeasurableValueVO.getPropertyValue(currentPropertyTypeVO.getPropertyName());
                    if(currentPropertyValueVO!=null&&currentPropertyValueVO.getPropertyValue()!=null) {
                        Object propertyValue=currentPropertyValueVO.getPropertyValue();
                        if(propertyValue instanceof Date){
                            String dateValue=dateTypePropertyFormat.format(propertyValue);
                            newRecord.getItemProperty(currentPropertyTypeVO.getPropertyName()).setValue(dateValue);
                        }else{
                            newRecord.getItemProperty(currentPropertyTypeVO.getPropertyName()).setValue(""+propertyValue);
                        }
                    }
                }
                TypeDataInstanceTableRowActions typeDataInstanceTableRowActions=new TypeDataInstanceTableRowActions(this.currentUserClientInfo);
                typeDataInstanceTableRowActions.setMeasurableValue(currentMeasurableValueVO);
                newRecord.getItemProperty(" 操作").setValue(typeDataInstanceTableRowActions);
            }
        }
    }

    private void setAdditionalQueryParams(List<PropertyTypeVO> typePropertiesInfoList,List<PropertyTypeVO> additionalQueryParameters){
        for(PropertyTypeVO currentAdditionalQueryParam:additionalQueryParameters){
            boolean isExistParam=false;
            for(PropertyTypeVO currentTypeProperty:typePropertiesInfoList){
                if(currentTypeProperty.getPropertyName().equals(currentAdditionalQueryParam.getPropertyName())){
                    isExistParam=true;
                }
            }
            if(!isExistParam){
                typePropertiesInfoList.add(currentAdditionalQueryParam);
            }
        }
    }

    public String getDataInstanceTypeKind() {
        return dataInstanceTypeKind;
    }

    public void setDataInstanceTypeKind(String dataInstanceTypeKind) {
        this.dataInstanceTypeKind = dataInstanceTypeKind;
    }

    public String getDataInstanceTypeName() {
        return dataInstanceTypeName;
    }

    public void setDataInstanceTypeName(String dataInstanceTypeName) {
        this.dataInstanceTypeName = dataInstanceTypeName;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    private void showQuerySQL(){
        final Window window = new Window();
        window.setCaption(" 查询条件 SQL");
        window.setIcon(FontAwesome.FILE_TEXT_O);
        VerticalLayout containerLayout=new VerticalLayout();
        containerLayout.setSpacing(true);
        containerLayout.setMargin(true);
        Panel sqlTextPanel=new Panel();
        sqlTextPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        sqlTextPanel.setWidth(300,Unit.PIXELS);
        sqlTextPanel.setHeight(200,Unit.PIXELS);
        Label querySqlLabel=new Label();
        querySqlLabel.addStyleName(ValoTheme.LABEL_COLORED);
        querySqlLabel.setValue(this.querySQL);
        sqlTextPanel.setContent(querySqlLabel);
        containerLayout.addComponent(sqlTextPanel);
        window.setWidth(320.0f, Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);
        window.setContent(containerLayout);
        UI.getCurrent().addWindow(window);
    }

    private Pagination createPagination(long totalData, int initPageNumber) {
        final PaginationResource paginationResource = PaginationResource.newBuilder().setTotal(totalData).setPage(initPageNumber).setLimit(this.getTablePageSize()).build();
        final Pagination pagination = new Pagination(paginationResource);
        return pagination;
    }

    public int getTablePageSize() {
        return tablePageSize;
    }

    public void setTablePageSize(int tablePageSize) {
        this.tablePageSize = tablePageSize;
    }
}

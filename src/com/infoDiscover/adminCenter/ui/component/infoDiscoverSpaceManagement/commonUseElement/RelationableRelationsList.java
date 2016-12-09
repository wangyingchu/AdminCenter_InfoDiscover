package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationableValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by wangychu on 12/8/16.
 */
public class RelationableRelationsList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private RelationableValueVO relationableValueVO;

    private Table relationableRelationsTable;
    private HorizontalLayout paginationContainerLayout;

    private Label relationResultCountLabel;

    public RelationableRelationsList(UserClientInfo userClientInfo,RelationableValueVO relationableValueVO) {
        this.currentUserClientInfo = userClientInfo;
        this.relationableValueVO=relationableValueVO;
        this.setWidth(100,Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);

        HorizontalLayout relationsSummaryInfoContainerLayout=new HorizontalLayout();
        addComponent(relationsSummaryInfoContainerLayout);
        Label relationResultCountInfo=new Label(FontAwesome.SHARE_ALT_SQUARE.getHtml()+" 关联关系总量: ", ContentMode.HTML);
        relationResultCountInfo.addStyleName(ValoTheme.LABEL_TINY);
        relationsSummaryInfoContainerLayout.addComponent(relationResultCountInfo);
        relationsSummaryInfoContainerLayout.setComponentAlignment(relationResultCountInfo,Alignment.MIDDLE_LEFT);
        this.relationResultCountLabel=new Label("--");
        this.relationResultCountLabel.addStyleName("ui_appFriendlyElement");
        relationsSummaryInfoContainerLayout.addComponent(this.relationResultCountLabel);
        relationsSummaryInfoContainerLayout.setComponentAlignment(this.relationResultCountLabel,Alignment.MIDDLE_LEFT);

        this.relationableRelationsTable=new Table();
        this.relationableRelationsTable.setWidth(100, Unit.PERCENTAGE);

        this.relationableRelationsTable.setHeight(500,Unit.PIXELS);


        this.relationableRelationsTable.setSelectable(true);
        this.relationableRelationsTable.addStyleName(ValoTheme.TABLE_SMALL);
        //this.relationableRelationsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        //this.relationableRelationsTable.addStyleName(ValoTheme.TABLE_COMPACT);

        this.relationableRelationsTable.setRowHeaderMode(Table.RowHeaderMode.INDEX);



        addComponent(this.relationableRelationsTable);

        this.paginationContainerLayout=new HorizontalLayout();
        this.paginationContainerLayout.setWidth(100,Unit.PERCENTAGE);
        addComponent(this.paginationContainerLayout);
    }

    @Override
    public void attach() {
        super.attach();
        List<RelationValueVO> relationValuesList= InfoDiscoverSpaceOperationUtil.getRelationableRelationsById(relationableValueVO.getDiscoverSpaceName(),relationableValueVO.getRelationableTypeKind(),relationableValueVO.getId());
        this.relationResultCountLabel.setValue(""+relationValuesList.size());

        Container dataContainer=this.relationableRelationsTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Container queryResultDataContainer = new IndexedContainer();
        this.relationableRelationsTable.setContainerDataSource(queryResultDataContainer);

        this.relationableRelationsTable.addContainerProperty(" 关系方向",String.class,"");
        this.relationableRelationsTable.setColumnWidth(" 关系方向", 90);

        this.relationableRelationsTable.addContainerProperty(" 关系ID",String.class,"");
        this.relationableRelationsTable.setColumnIcon(" 关系ID",FontAwesome.KEY);
        this.relationableRelationsTable.setColumnWidth(" 关系ID", 180);

        this.relationableRelationsTable.addContainerProperty(" 关系类型",TypeDataInstanceTableRowActions.class,null);
        this.relationableRelationsTable.setColumnIcon(" 关系类型", FontAwesome.EXCHANGE);

        this.relationableRelationsTable.addContainerProperty(" 相关数据",TypeDataInstanceTableRowActions.class,null);
        this.relationableRelationsTable.setColumnIcon(" 相关数据", VaadinIcons.FILE_TREE_SMALL);

        this.relationableRelationsTable.setColumnExpandRatio(" 相关数据",0.8f);

        if(relationValuesList!=null){
            for(RelationValueVO currentRelationValueVO:relationValuesList){

                currentRelationValueVO.getId();
                currentRelationValueVO.getRelationTypeName();
                currentRelationValueVO.getProperties();
                currentRelationValueVO.getFromRelationable();
                currentRelationValueVO.getToRelationable();

                //this.typeDataInstanceTable.addContainerProperty(currentPropertyTypeVO.getPropertyName(), String.class, "");
            }
        }


        for(int i=0;i<1500;i++){
            Item newRecord=this.relationableRelationsTable.addItem("dataRelation_index_"+i);

            newRecord.getItemProperty(" 关系ID").setValue(" "+i);






        }



        this.relationableRelationsTable.setPageLength(20);










    }
}

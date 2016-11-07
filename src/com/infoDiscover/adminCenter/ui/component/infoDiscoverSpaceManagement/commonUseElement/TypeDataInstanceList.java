package com.infoDiscover.adminCenter.ui.component.infoDiscoverSpaceManagement.commonUseElement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.MeasurableValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.ui.util.UserClientInfo;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by wangychu on 11/7/16.
 */
public class TypeDataInstanceList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;

    private Label querySqlLabel;

    private Table typeDataInstanceTable;
    private String dataInstanceTypeKind;
    private String dataInstanceTypeName;
    private String discoverSpaceName;

    public TypeDataInstanceList(UserClientInfo userClientInfo) {
        this.currentUserClientInfo = userClientInfo;
        this.setWidth(100,Unit.PERCENTAGE);
        setSpacing(true);
        setMargin(true);
        this.querySqlLabel=new Label();
        this.querySqlLabel.addStyleName(ValoTheme.LABEL_TINY);
        //this.querySqlLabel.addStyleName(ValoTheme.LABEL_LIGHT);
        //this.querySqlLabel.addStyleName(ValoTheme.LABEL_COLORED);

        addComponent(this.querySqlLabel);

        this.typeDataInstanceTable=new Table();

        this.typeDataInstanceTable.setHeight(400,Unit.PIXELS);
        this.typeDataInstanceTable.setWidth(100, Unit.PERCENTAGE);

        this.typeDataInstanceTable.addStyleName(ValoTheme.TABLE_COMPACT);
        this.typeDataInstanceTable.addStyleName(ValoTheme.TABLE_SMALL);
        addComponent(this.typeDataInstanceTable);

    }

    public void setQuerySQL(String querySQL){

        this.querySqlLabel.setValue(querySQL);

    }

    public void renderTypeDataInstanceList(List<PropertyTypeVO> queryParameters,List<MeasurableValueVO> queryResults){
        System.out.println(this.getDataInstanceTypeName());
        System.out.println(this.getDataInstanceTypeKind());
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

        Container dataContainer=this.typeDataInstanceTable.getContainerDataSource();
        dataContainer.removeAllItems();
        Collection containerPropertiesId=dataContainer.getContainerPropertyIds();
        Iterator propertyKeyIterator=containerPropertiesId.iterator();
        this.typeDataInstanceTable.clear();
        while(propertyKeyIterator.hasNext()){
            //this.typeDataInstanceTable.removeContainerProperty(propertyKeyIterator.next());
        }


        if(typePropertiesInfoList!=null){
            for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){
                this.typeDataInstanceTable.addContainerProperty(currentPropertyTypeVO.getPropertyName(), String.class, "");
            }
        }

        if(queryResults!=null){

            for(int i=0;i<queryResults.size();i++){

                MeasurableValueVO currentMeasurableValueVO=queryResults.get(i);
                Item newRecord=this.typeDataInstanceTable.addItem("index_"+i);


                for(PropertyTypeVO currentPropertyTypeVO:typePropertiesInfoList){

                   /// Property property=new Property();


                    PropertyValueVO vooo=currentMeasurableValueVO.getPropertyValue(currentPropertyTypeVO.getPropertyName());
                    if(vooo!=null) {

                        newRecord.getItemProperty(currentPropertyTypeVO.getPropertyName()).setValue("" +vooo.getPropertyValue());
                    }
                   // this.typeDataInstanceTable.getContainerDataSource().



                  //  newRecord.addItemProperty(""+currentMeasurableValueVO.getPropertyValue(currentPropertyTypeVO.getPropertyName()),this.typeDataInstanceTable.getContainerProperty("index_"+i,currentPropertyTypeVO.getPropertyName()));





                }




            }



        }




        //this.typeDataInstanceTable.getContainerDataSource().removeAllItems();

        //this.typeDataInstanceTable.get


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
}

package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.*;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.infoDiscoverEngine.dataMart.*;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverAdminSpace;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.config.RuntimeEnvironmentHandler;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticHelper;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;

import java.io.*;
import java.util.*;

/**
 * Created by wangychu on 9/30/16.
 */
public class InfoDiscoverSpaceOperationUtil {
    public static final String TYPEKIND_DIMENSION="TYPEKIND_DIMENSION";
    public static final String TYPEKIND_FACT="TYPEKIND_FACT";
    public static final String TYPEKIND_RELATION="TYPEKIND_RELATION";

    public static final String RELATION_DIRECTION_FROM="FROM";
    public static final String RELATION_DIRECTION_TO="TO";
    public static final String RELATION_DIRECTION_BOTH="BOTH";
    public static final String TYPEKIND_AliasNameFactType="TypeKind_AliasName";
    public static final String TYPEPROPERTY_AliasNameFactType="TypeProperty_AliasName";

    public static final String MetaConfig_PropertyName_DiscoverSpace="discoverSpace";
    public static final String MetaConfig_PropertyName_TypeKind="typeKind";
    public static final String MetaConfig_PropertyName_TypeName="typeName";
    public static final String MetaConfig_PropertyName_TypeAliasName="typeAliasName";
    public static final String MetaConfig_PropertyName_TypePropertyName="typePropertyName";
    public static final String MetaConfig_PropertyName_TypePropertyAliasName="typePropertyAliasName";

    private static HashMap<String,String> TYPEKIND_AliasNameMap=new HashMap<>();
    private static HashMap<String,String> TypeProperty_AliasNameMap=new HashMap<>();

    public static boolean checkDiscoverSpaceExistence(String spaceName){
        return DiscoverEngineComponentFactory.checkDiscoverSpaceExistence(spaceName);
    }

    public static boolean createDiscoverSpace(String spaceName){
        return DiscoverEngineComponentFactory.createInfoDiscoverSpace(spaceName);
    }

    public static boolean deleteDiscoverSpace(String spaceName){
        return DiscoverEngineComponentFactory.deleteInfoDiscoverSpace(spaceName);
    }

    public static List<String> getExistDiscoverSpace(){
        return DiscoverEngineComponentFactory.getDiscoverSpacesListInEngine();
    }

    public static List<String> getExistDiscoverSpace(String[] excludedDiscoverSpaces){
        List<String>  existingSpaceList= DiscoverEngineComponentFactory
                .getDiscoverSpacesListInEngine();
        for(String excludedSpace: excludedDiscoverSpaces) {
            existingSpaceList.remove(excludedSpace);
        }
        return existingSpaceList;
    }

    public static DiscoverSpaceStatisticMetrics getDiscoverSpaceStatisticMetrics(String spaceName){
        DiscoverSpaceStatisticHelper discoverSpaceStatisticHelper= DiscoverEngineComponentFactory.getDiscoverSpaceStatisticHelper();
        return discoverSpaceStatisticHelper.getDiscoverSpaceStatisticMetrics(spaceName);
    }

    public static DimensionTypeVO retrieveRootDimensionTypeRuntimeInfo(String spaceName, DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        List<DataTypeStatisticMetrics> dimensionsStatisticMetrics=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
        DimensionTypeVO rootDimensionTypeVO=new DimensionTypeVO();
        rootDimensionTypeVO.setTypeName(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME);
        rootDimensionTypeVO.setTypeDataRecordCount(discoverSpaceStatisticMetrics.getSpaceDimensionDataCount());
        if(dimensionsStatisticMetrics!=null){
            rootDimensionTypeVO.setDescendantDimensionTypesNumber(dimensionsStatisticMetrics.size());
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            List<DimensionTypeVO> childDimensionTypesVOList=new ArrayList<DimensionTypeVO>();
            rootDimensionTypeVO.setChildDimensionTypesVOList(childDimensionTypesVOList);

            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<String> rootDimensionTypesList=targetSpace.getRootDimensionTypesList();
            if(rootDimensionTypesList!=null){
                for(String currentDimensionTypeName:rootDimensionTypesList){
                    DimensionType currentDimensionType=targetSpace.getDimensionType(currentDimensionTypeName);
                    DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(spaceName,currentDimensionType,dimensionsStatisticMetrics);
                    childDimensionTypesVOList.add(currentDimensionTypeVO);
                }
            }
            return rootDimensionTypeVO;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static RelationTypeVO retrieveRootRelationTypeRuntimeInfo(String spaceName, DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        List<DataTypeStatisticMetrics> relationsStatisticMetrics=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
        RelationTypeVO rootRelationTypeVO=new RelationTypeVO();
        rootRelationTypeVO.setTypeName(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME);
        rootRelationTypeVO.setTypeDataRecordCount(discoverSpaceStatisticMetrics.getSpaceRelationDataCount());
        if(relationsStatisticMetrics!=null){
            rootRelationTypeVO.setDescendantRelationTypesNumber(relationsStatisticMetrics.size());
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            List<RelationTypeVO> childRelationTypesVOList=new ArrayList<RelationTypeVO>();
            rootRelationTypeVO.setChildRelationTypesVOList(childRelationTypesVOList);

            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<String> rootRelationTypesList=targetSpace.getRootRelationTypesList();
            if(rootRelationTypesList!=null){
                for(String currentRelationTypeName:rootRelationTypesList){
                    RelationType currentRelationType=targetSpace.getRelationType(currentRelationTypeName);
                    RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(spaceName,currentRelationType, relationsStatisticMetrics);
                    childRelationTypesVOList.add(currentRelationTypeVO);
                }
            }
            return rootRelationTypeVO;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static DimensionTypeVO retrieveDimensionTypeRuntimeInfo(String spaceName, String dimensionTypeName,DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return null;
            }else{
                List<DataTypeStatisticMetrics> dimensionsStatisticMetrics=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
                DimensionType currentDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(spaceName,currentDimensionType,dimensionsStatisticMetrics);
                return currentDimensionTypeVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static RelationTypeVO retrieveRelationTypeRuntimeInfo(String spaceName, String relationTypeName,DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return null;
            }else{
                List<DataTypeStatisticMetrics> relationsStatisticMetrics=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();
                RelationType currentRelationType=targetSpace.getRelationType(relationTypeName);
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(spaceName,currentRelationType, relationsStatisticMetrics);
                return currentRelationTypeVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static DimensionTypeVO retrieveDimensionTypeRuntimeInfo(String discoverSpaceName,DimensionType targetDimensionType,List<DataTypeStatisticMetrics> dimensionsStatisticMetrics){
        DimensionTypeVO targetDimensionTypeVO=new DimensionTypeVO();
        targetDimensionTypeVO.setTypeName(targetDimensionType.getTypeName());
        targetDimensionTypeVO.setTypeAliasName(getTypeKindAliasName(discoverSpaceName,TYPEKIND_DIMENSION,targetDimensionType.getTypeName()));
        if(targetDimensionType.getDescendantDimensionTypes()!=null){
            targetDimensionTypeVO.setDescendantDimensionTypesNumber(targetDimensionType.getDescendantDimensionTypes().size());
        }
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:dimensionsStatisticMetrics){
            String currentDimensionTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(currentDimensionTypeName.equals(targetDimensionType.getTypeName())){
                targetDimensionTypeVO.setTypeDataRecordCount(currentDataTypeStatisticMetrics.getTypeDataCount());
                break;
            }
        }
        List<DimensionTypeVO> childDimensionTypesVOList=new ArrayList<DimensionTypeVO>();
        targetDimensionTypeVO.setChildDimensionTypesVOList(childDimensionTypesVOList);
        List<DimensionType> childDimensionTypesList=targetDimensionType.getChildDimensionTypes();
        if(childDimensionTypesList!=null){
            for(DimensionType currentDimensionType:childDimensionTypesList){
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(discoverSpaceName,currentDimensionType,dimensionsStatisticMetrics);
                childDimensionTypesVOList.add(currentDimensionTypeVO);
            }
        }
        return targetDimensionTypeVO;
    }

    private static RelationTypeVO retrieveRelationTypeRuntimeInfo(String discoverSpaceName,RelationType targetRelationType,List<DataTypeStatisticMetrics> relationsStatisticMetrics){
        RelationTypeVO targetRelationTypeVO=new RelationTypeVO();
        targetRelationTypeVO.setTypeName(targetRelationType.getTypeName());
        targetRelationTypeVO.setTypeAliasName(getTypeKindAliasName(discoverSpaceName,TYPEKIND_RELATION,targetRelationType.getTypeName()));
        if(targetRelationType.getDescendantRelationTypes()!=null){
            targetRelationTypeVO.setDescendantRelationTypesNumber(targetRelationType.getDescendantRelationTypes().size());
        }
        for(DataTypeStatisticMetrics currentDataTypeStatisticMetrics:relationsStatisticMetrics){
            String currentRelationTypeName=currentDataTypeStatisticMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(currentRelationTypeName.equals(targetRelationType.getTypeName())){
                targetRelationTypeVO.setTypeDataRecordCount(currentDataTypeStatisticMetrics.getTypeDataCount());
                break;
            }
        }
        List<RelationTypeVO> childRelationTypesVOList=new ArrayList<RelationTypeVO>();
        targetRelationTypeVO.setChildRelationTypesVOList(childRelationTypesVOList);
        List<RelationType> childRelationTypesList=targetRelationType.getChildRelationTypes();
        if(childRelationTypesList!=null){
            for(RelationType currentRelationType:childRelationTypesList){
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(discoverSpaceName,currentRelationType, relationsStatisticMetrics);
                childRelationTypesVOList.add(currentRelationTypeVO);
            }
        }
        return targetRelationTypeVO;
    }

    public static List<PropertyTypeVO> retrieveDimensionTypePropertiesInfo(String spaceName, String dimensionTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
            if(targetDimensionType!=null){
                List<TypeProperty> propertiesList=targetDimensionType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_DIMENSION,dimensionTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<PropertyTypeVO> retrieveRelationTypePropertiesInfo(String spaceName, String relationTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
            if(targetRelationType!=null){
                List<TypeProperty> propertiesList=targetRelationType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_RELATION,relationTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<PropertyTypeVO> retrieveFactTypePropertiesInfo(String spaceName, String factTypeName){
        List<PropertyTypeVO> propertyTypeVOs=new ArrayList<PropertyTypeVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            FactType targetFactType=targetSpace.getFactType(factTypeName);
            if(targetFactType!=null){
                List<TypeProperty> propertiesList=targetFactType.getTypeProperties();
                if(propertiesList!=null){
                    for(TypeProperty currentTypeProperty:propertiesList){
                        currentTypeProperty.getPropertyName();
                        currentTypeProperty.getPropertyType();
                        currentTypeProperty.isMandatory();
                        currentTypeProperty.isReadOnly();
                        currentTypeProperty.isNullable();
                        PropertyTypeVO currentPropertyTypeVO=new PropertyTypeVO();
                        currentPropertyTypeVO.setPropertyName(currentTypeProperty.getPropertyName());
                        currentPropertyTypeVO.setPropertyAliasName(getTypePropertyAliasName(spaceName,
                                TYPEKIND_FACT,factTypeName,currentTypeProperty.getPropertyName()));
                        currentPropertyTypeVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                        currentPropertyTypeVO.setMandatory(currentTypeProperty.isMandatory());
                        currentPropertyTypeVO.setNullable(currentTypeProperty.isNullable());
                        currentPropertyTypeVO.setReadOnly(currentTypeProperty.isReadOnly());
                        currentPropertyTypeVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                        propertyTypeVOs.add(currentPropertyTypeVO);
                    }
                }
            }
            return propertyTypeVOs;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkDimensionTypeExistence(String spaceName, String dimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasDimensionType(dimensionTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkRelationTypeExistence(String spaceName, String relationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasRelationType(relationTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean checkFactTypeExistence(String spaceName, String factTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return targetSpace.hasFactType(factTypeName);
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createDimensionType(String spaceName, String dimensionTypeName,String dimensionTypeAliasName,String parentDimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                DimensionType targetDimensionType=null;
                if(parentDimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
                    targetDimensionType=targetSpace.addDimensionType(dimensionTypeName);
                }else{
                    targetDimensionType=targetSpace.addChildDimensionType(dimensionTypeName,parentDimensionTypeName);
                }
                if(targetDimensionType!=null&&targetDimensionType.getTypeName().equals(dimensionTypeName)){
                    recordTypeKindAliasName(spaceName,TYPEKIND_DIMENSION,dimensionTypeName,dimensionTypeAliasName);
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createRelationType(String spaceName, String relationTypeName,String relationTypeAliasName,String parentRelationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                RelationType targetRelationType=null;
                if(parentRelationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
                    targetRelationType=targetSpace.addRelationType(relationTypeName);
                }else{
                    targetRelationType=targetSpace.addChildRelationType(relationTypeName, parentRelationTypeName);
                }
                if(targetRelationType!=null&&targetRelationType.getTypeName().equals(relationTypeName)){
                    recordTypeKindAliasName(spaceName,TYPEKIND_RELATION,relationTypeName,relationTypeAliasName);
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createFactType(String spaceName, String factTypeName,String factTypeAliasName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasFactType(factTypeName)) {
                return false;
            }else{
                FactType targetFactType=targetSpace.addFactType(factTypeName);
                if(targetFactType!=null&&targetFactType.getTypeName().equals(factTypeName)){
                    recordTypeKindAliasName(spaceName,TYPEKIND_FACT,factTypeName,factTypeAliasName);
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteDimensionType(String spaceName, String dimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                boolean removeResult=targetSpace.removeDimensionType(dimensionTypeName);
                if(removeResult) {
                    deleteTypeKindAliasName(spaceName, TYPEKIND_DIMENSION, dimensionTypeName);
                }
                return removeResult;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteRelationType(String spaceName, String relationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                boolean removeResult=targetSpace.removeRelationType(relationTypeName);
                if(removeResult) {
                    deleteTypeKindAliasName(spaceName, TYPEKIND_RELATION, relationTypeName);
                }
                return removeResult;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteFactType(String spaceName, String factTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                boolean removeResult=targetSpace.removeFactType(factTypeName);
                if(removeResult) {
                    deleteTypeKindAliasName(spaceName, TYPEKIND_FACT, factTypeName);
                }
                return removeResult;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean checkTypePropertyExistence(String spaceName, String typeKind,String typeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(TYPEKIND_DIMENSION.equals(typeKind)){
                if(!targetSpace.hasDimensionType(typeName)){
                    return false;
                }else{
                    DimensionType targetDimensionType=targetSpace.getDimensionType(typeName);
                    return targetDimensionType.hasTypeProperty(propertyName);
                }
            }
            if(TYPEKIND_FACT.equals(typeKind)){
                if(!targetSpace.hasFactType(typeName)){
                    return false;
                }else{
                    FactType targetFactType=targetSpace.getFactType(typeName);
                    return targetFactType.hasTypeProperty(propertyName);
                }
            }
            if(TYPEKIND_RELATION.equals(typeKind)){
                if(!targetSpace.hasRelationType(typeName)){
                    return false;
                }else {
                    RelationType targetRelationType = targetSpace.getRelationType(typeName);
                    return targetRelationType.hasTypeProperty(propertyName);
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createTypeProperty(String spaceName, String typeKind,String typeName,
                                             String propertyName,String propertyAliasName,String propertyType,boolean isMandatory,boolean isReadOnly,boolean isNullable){
        PropertyType targetPropertyType=null;
        if(ApplicationConstant.DataFieldType_BOOLEAN.equals(propertyType)){
            targetPropertyType=PropertyType.BOOLEAN;
        }
        if(ApplicationConstant.DataFieldType_INT.equals(propertyType)){
            targetPropertyType=PropertyType.INT;
        }
        if(ApplicationConstant.DataFieldType_SHORT.equals(propertyType)){
            targetPropertyType=PropertyType.SHORT;
        }
        if(ApplicationConstant.DataFieldType_LONG.equals(propertyType)){
            targetPropertyType=PropertyType.LONG;
        }
        if(ApplicationConstant.DataFieldType_FLOAT.equals(propertyType)){
            targetPropertyType=PropertyType.FLOAT;
        }
        if(ApplicationConstant.DataFieldType_DOUBLE.equals(propertyType)){
            targetPropertyType=PropertyType.DOUBLE;
        }
        if(ApplicationConstant.DataFieldType_DATE.equals(propertyType)){
            targetPropertyType=PropertyType.DATE;
        }
        if(ApplicationConstant.DataFieldType_STRING.equals(propertyType)){
            targetPropertyType=PropertyType.STRING;
        }
        if(ApplicationConstant.DataFieldType_BINARY.equals(propertyType)){
            targetPropertyType=PropertyType.BINARY;
        }
        if(ApplicationConstant.DataFieldType_BYTE.equals(propertyType)){
            targetPropertyType=PropertyType.BYTE;
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(TYPEKIND_DIMENSION.equals(typeKind)){
                if(!targetSpace.hasDimensionType(typeName)){
                    return false;
                }else{
                    DimensionType targetDimensionType=targetSpace.getDimensionType(typeName);
                    TypeProperty targetTypeProperty=targetDimensionType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    recordTypePropertyAliasName(spaceName,TYPEKIND_DIMENSION,typeName,propertyName,propertyAliasName);
                    return targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
            if(TYPEKIND_FACT.equals(typeKind)){
                if(!targetSpace.hasFactType(typeName)){
                    return false;
                }else{
                    FactType targetFactType=targetSpace.getFactType(typeName);
                    TypeProperty targetTypeProperty=targetFactType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    recordTypePropertyAliasName(spaceName,TYPEKIND_FACT,typeName,propertyName,propertyAliasName);
                    return targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
            if(TYPEKIND_RELATION.equals(typeKind)){
                if(!targetSpace.hasRelationType(typeName)){
                    return false;
                }else {
                    RelationType targetRelationType = targetSpace.getRelationType(typeName);
                    TypeProperty targetTypeProperty=targetRelationType.addTypeProperty(propertyName,targetPropertyType);
                    targetTypeProperty.setMandatory(isMandatory);
                    targetTypeProperty.setReadOnly(isReadOnly);
                    targetTypeProperty.setNullable(isNullable);
                    recordTypePropertyAliasName(spaceName,TYPEKIND_RELATION,typeName,propertyName,propertyAliasName);
                    return targetTypeProperty.getPropertyName().equals(propertyName);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteDimensionTypeProperty(String spaceName, String dimensionTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                if(!targetDimensionType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }

        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            boolean removeResult=adminSpace.removeDimensionTypeProperty(dimensionTypeName,propertyName);
            if(removeResult){
                deleteTypePropertyAliasName(spaceName,TYPEKIND_DIMENSION,dimensionTypeName,propertyName);
            }
            return removeResult;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteRelationTypeProperty(String spaceName, String relationTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasRelationType(relationTypeName)){
                return false;
            }else{
                RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
                if(!targetRelationType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }

        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            boolean removeResult=adminSpace.removeRelationTypeProperty(relationTypeName, propertyName);
            if(removeResult){
                deleteTypePropertyAliasName(spaceName,TYPEKIND_RELATION,relationTypeName,propertyName);
            }
            return removeResult;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteFactTypeProperty(String spaceName, String factTypeName,String propertyName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                FactType targetFactType=targetSpace.getFactType(factTypeName);
                if(!targetFactType.hasTypeProperty(propertyName)){
                    return false;
                }
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }

        InfoDiscoverAdminSpace adminSpace=null;
        try {
            adminSpace=DiscoverEngineComponentFactory.connectInfoDiscoverAdminSpace(spaceName);
            boolean removeResult=adminSpace.removeFactTypeProperty(factTypeName,propertyName);
            if(removeResult){
                deleteTypePropertyAliasName(spaceName,TYPEKIND_FACT,factTypeName,propertyName);
            }
            return removeResult;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(adminSpace!=null){
                adminSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<String> retrieveChildDimensionTypesRuntimeInfo(String spaceName, String parentDimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(parentDimensionTypeName.equals(InfoDiscoverEngineConstant.DIMENSION_ROOTCLASSNAME)){
                List<String> rootDimensionTypesList=targetSpace.getRootDimensionTypesList();
                if(rootDimensionTypesList!=null){
                    return rootDimensionTypesList;
                }else{
                    return new ArrayList<String>();
                }
            }
            List<String> childDimensionTypeNamesList=new ArrayList<String>();
            if(targetSpace.hasDimensionType(parentDimensionTypeName)){
                List<DimensionType> childDimensionTypesList=targetSpace.getDimensionType(parentDimensionTypeName).getChildDimensionTypes();
                if(childDimensionTypesList!=null){
                    for(DimensionType currentDimensionType:childDimensionTypesList){
                        childDimensionTypeNamesList.add(currentDimensionType.getTypeName());
                    }
                }
            }
            return childDimensionTypeNamesList;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<String> retrieveChildRelationTypesRuntimeInfo(String spaceName, String parentRelationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(parentRelationTypeName.equals(InfoDiscoverEngineConstant.RELATION_ROOTCLASSNAME)){
                List<String> rootRelationTypesList=targetSpace.getRootRelationTypesList();
                if(rootRelationTypesList!=null){
                    return rootRelationTypesList;
                }else{
                    return new ArrayList<String>();
                }
            }
            List<String> childRelationTypeNamesList=new ArrayList<String>();
            if(targetSpace.hasRelationType(parentRelationTypeName)){
                List<RelationType> childRelationTypesList=targetSpace.getRelationType(parentRelationTypeName).getChildRelationTypes();
                if(childRelationTypesList!=null){
                    for(RelationType currentRelationType:childRelationTypesList){
                        childRelationTypeNamesList.add(currentRelationType.getTypeName());
                    }
                }
            }
            return childRelationTypeNamesList;
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createDimension(String spaceName, String dimensionTypeName,List<PropertyValueVO> dimensionProperties){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasDimensionType(dimensionTypeName)){
                return false;
            }else{
                Dimension targetDimension=DiscoverEngineComponentFactory.createDimension(dimensionTypeName);
                setMeasurableInitProperties(targetDimension,dimensionProperties);
                Dimension resultDimension=targetSpace.addDimension(targetDimension);
                if(resultDimension!=null){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static boolean createFact(String spaceName, String factTypeName,List<PropertyValueVO> factProperties){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(!targetSpace.hasFactType(factTypeName)){
                return false;
            }else{
                Fact targetFact=DiscoverEngineComponentFactory.createFact(factTypeName);
                setMeasurableInitProperties(targetFact,factProperties);
                Fact resultFact=targetSpace.addFact(targetFact);
                if(resultFact!=null){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static void setMeasurableInitProperties(Measurable measurable,List<PropertyValueVO> properties){
        for(PropertyValueVO currentPropertyValueVO:properties){
            String propertyName=currentPropertyValueVO.getPropertyName();
            String propertyType=currentPropertyValueVO.getPropertyType();
            Object propertyValue=currentPropertyValueVO.getPropertyValue();
            if(propertyValue!=null){
                switch(propertyType){
                    case ApplicationConstant.DataFieldType_STRING:
                        String stringValue=propertyValue.toString();
                        measurable.setInitProperty(propertyName,stringValue);
                        break;
                    case ApplicationConstant.DataFieldType_BOOLEAN:
                        String valueText=propertyValue.toString();
                        boolean booleanValue=Boolean.parseBoolean(valueText);
                        measurable.setInitProperty(propertyName,booleanValue);
                        break;
                    case ApplicationConstant.DataFieldType_DATE:
                        Date dateValue=(Date)propertyValue;
                        measurable.setInitProperty(propertyName,dateValue);
                        break;
                    case ApplicationConstant.DataFieldType_INT:
                        int intValue=((Integer)propertyValue).intValue();
                        measurable.setInitProperty(propertyName,intValue);
                        break;
                    case ApplicationConstant.DataFieldType_LONG:
                        long longValue=((Long)propertyValue).longValue();
                        measurable.setInitProperty(propertyName,longValue);
                        break;
                    case ApplicationConstant.DataFieldType_DOUBLE:
                        double doubleValue=((Double)propertyValue).doubleValue();
                        measurable.setInitProperty(propertyName,doubleValue);
                        break;
                    case ApplicationConstant.DataFieldType_FLOAT:
                        float floatValue=((Float)propertyValue).floatValue();
                        measurable.setInitProperty(propertyName,floatValue);
                        break;
                    case ApplicationConstant.DataFieldType_SHORT:
                        short shortValue=((Short)propertyValue).shortValue();
                        measurable.setInitProperty(propertyName,shortValue);
                        break;
                case ApplicationConstant.DataFieldType_BYTE:
                        byte byteValue=((Byte)propertyValue).byteValue();
                        measurable.setInitProperty(propertyName,byteValue);
                        break;
                case ApplicationConstant.DataFieldType_BINARY:
                    byte[] binaryValue=(byte[])propertyValue;
                    measurable.setInitProperty(propertyName,binaryValue);
                    break;
                }
            }
        }
    }

    public static List<MeasurableValueVO> queryDimensions(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Dimension> resultDimensionsList=ie.discoverDimensions(exploreParameters);
            if(resultDimensionsList!=null) {
                for (Dimension currentDimension : resultDimensionsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentDimension.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION);
                    currentMeasurableValueVO.setMeasurableTypeName(currentDimension.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentDimension.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static List<MeasurableValueVO> queryFacts(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Fact> resultFactsList=ie.discoverFacts(exploreParameters);
            if(resultFactsList!=null) {
                for (Fact currentFact : resultFactsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentFact.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT);
                    currentMeasurableValueVO.setMeasurableTypeName(currentFact.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentFact.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static List<MeasurableValueVO> queryRelations(String spaceName,ExploreParameters exploreParameters){
        List<MeasurableValueVO> measurableValueList=new ArrayList<MeasurableValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Relation> resultRelationsList=ie.discoverRelations(exploreParameters);
            if(resultRelationsList!=null) {
                for (Relation currentRelation : resultRelationsList) {
                    MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                    measurableValueList.add(currentMeasurableValueVO);
                    currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                    currentMeasurableValueVO.setId(currentRelation.getId());
                    currentMeasurableValueVO.setMeasurableTypeKind(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION);
                    currentMeasurableValueVO.setMeasurableTypeName(currentRelation.getType());
                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentMeasurableValueVO.setProperties(propertyValueVOList);
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return measurableValueList;
    }

    public static boolean updateMeasurableProperties(String spaceName,String measurableId,List<PropertyValueVO> propertiesValue){
        if(propertiesValue==null){
            return false;
        }
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable = targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable==null){
                return false;
            }
            List<String> currentPropertiesNameList= targetMeasurable.getPropertyNames();
            //remove properties not contained in new PropertyValue List
            if(currentPropertiesNameList!=null){
                for(String currentPropertyName:currentPropertiesNameList){
                    if(!propertyContainedInPropertyValueList(currentPropertyName,propertiesValue)){
                        boolean resultPropertyResult=targetMeasurable.removeProperty(currentPropertyName);
                        if(!resultPropertyResult){
                            return false;
                        }
                    }
                }
            }

            Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
            Map<String, Object> propertiesNeedAddUpdate=new HashMap<String, Object>();

            for(PropertyValueVO currentNewPropertyValue:propertiesValue){
                String propertyName=currentNewPropertyValue.getPropertyName();
                if(!targetMeasurable.hasProperty(propertyName)){
                    //add new property value
                    propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                }else{
                    //update existing property value
                    String propertyType=currentNewPropertyValue.getPropertyType();
                    Property measurableCurrentProperty=targetMeasurable.getProperty(propertyName);
                    PropertyType currentPropertyType=measurableCurrentProperty.getPropertyType();
                    if(!currentPropertyType.toString().equals(propertyType)){
                        targetMeasurable.removeProperty(propertyName);
                        propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                    }else{
                        propertiesNeedAddUpdate.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
                    }
                }
            }
            targetMeasurable.addProperties(propertiesNeedAddMap);
            targetMeasurable.updateProperties(propertiesNeedAddUpdate);

        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return true;
    }

    private static boolean propertyContainedInPropertyValueList(String propertyName,List<PropertyValueVO> propertiesValue){
        if(propertiesValue==null){
            return false;
        }
        for(PropertyValueVO currentPropertyValue:propertiesValue){
            if(currentPropertyValue.getPropertyName().equals(propertyName)){
                return true;
            }
        }
        return false;
    }

    private static Object getMeasurablePropertyValue(PropertyValueVO currentPropertyValueVO){
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();
        Object measurablePropertyValue=null;
        if(propertyValue!=null){
            switch(propertyType){
                case ApplicationConstant.DataFieldType_STRING:
                    measurablePropertyValue=propertyValue.toString();
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    String valueText=propertyValue.toString();
                    measurablePropertyValue=Boolean.parseBoolean(valueText);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    measurablePropertyValue=(Date)propertyValue;
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    measurablePropertyValue=((Integer)propertyValue).intValue();
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    measurablePropertyValue=((Long)propertyValue).longValue();
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    measurablePropertyValue=((Double)propertyValue).doubleValue();
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    measurablePropertyValue=((Float)propertyValue).floatValue();
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    measurablePropertyValue=((Short)propertyValue).shortValue();
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    measurablePropertyValue=((Byte)propertyValue).byteValue();
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    measurablePropertyValue=(byte[])propertyValue;
                    break;
            }
        }
        return measurablePropertyValue;
    }

    public static void addMeasurableSingleProperty(Measurable measurable,PropertyValueVO currentPropertyValueVO) throws InfoDiscoveryEngineRuntimeException {
        String propertyName=currentPropertyValueVO.getPropertyName();
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();
        if(propertyValue!=null){
            switch(propertyType){
                case ApplicationConstant.DataFieldType_STRING:
                    String stringValue=propertyValue.toString();
                    measurable.addProperty(propertyName,stringValue);
                    break;
                case ApplicationConstant.DataFieldType_BOOLEAN:
                    String valueText=propertyValue.toString();
                    boolean booleanValue=Boolean.parseBoolean(valueText);
                    measurable.addProperty(propertyName,booleanValue);
                    break;
                case ApplicationConstant.DataFieldType_DATE:
                    Date dateValue=(Date)propertyValue;
                    measurable.addProperty(propertyName,dateValue);
                    break;
                case ApplicationConstant.DataFieldType_INT:
                    int intValue=((Integer)propertyValue).intValue();
                    measurable.addProperty(propertyName,intValue);
                    break;
                case ApplicationConstant.DataFieldType_LONG:
                    long longValue=((Long)propertyValue).longValue();
                    measurable.addProperty(propertyName,longValue);
                    break;
                case ApplicationConstant.DataFieldType_DOUBLE:
                    double doubleValue=((Double)propertyValue).doubleValue();
                    measurable.addProperty(propertyName,doubleValue);
                    break;
                case ApplicationConstant.DataFieldType_FLOAT:
                    float floatValue=((Float)propertyValue).floatValue();
                    measurable.addProperty(propertyName,floatValue);
                    break;
                case ApplicationConstant.DataFieldType_SHORT:
                    short shortValue=((Short)propertyValue).shortValue();
                    measurable.addProperty(propertyName,shortValue);
                    break;
                case ApplicationConstant.DataFieldType_BYTE:
                    byte byteValue=((Byte)propertyValue).byteValue();
                    measurable.addProperty(propertyName,byteValue);
                    break;
                case ApplicationConstant.DataFieldType_BINARY:
                    byte[] binaryValue=(byte[])propertyValue;
                    measurable.addProperty(propertyName,binaryValue);
                    break;
            }
        }
    }

    public static void addMeasurableMultiProperties(Measurable measurable,List<PropertyValueVO> propertiesValueList){
        Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
        for(PropertyValueVO currentNewPropertyValue:propertiesValueList){
            String propertyName=currentNewPropertyValue.getPropertyName();
            propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
        }
        measurable.addProperties(propertiesNeedAddMap);
    }

    public static void updateMeasurableSingleProperty(Measurable measurable,PropertyValueVO currentPropertyValueVO) throws InfoDiscoveryEngineRuntimeException {
        String propertyName=currentPropertyValueVO.getPropertyName();
        String propertyType=currentPropertyValueVO.getPropertyType();
        Object propertyValue=currentPropertyValueVO.getPropertyValue();

        Property measurableCurrentProperty=measurable.getProperty(propertyName);
        PropertyType currentPropertyType=measurableCurrentProperty.getPropertyType();

        if(!currentPropertyType.toString().equals(propertyType)){
            measurable.removeProperty(propertyName);
            addMeasurableSingleProperty(measurable,currentPropertyValueVO);
        }else{
            if(propertyValue!=null){
                switch(propertyType){
                    case ApplicationConstant.DataFieldType_STRING:
                        String stringValue=propertyValue.toString();
                        measurable.updateProperty(propertyName,stringValue);
                        break;
                    case ApplicationConstant.DataFieldType_BOOLEAN:
                        String valueText=propertyValue.toString();
                        boolean booleanValue=Boolean.parseBoolean(valueText);
                        measurable.updateProperty(propertyName,booleanValue);
                        break;
                    case ApplicationConstant.DataFieldType_DATE:
                        Date dateValue=(Date)propertyValue;
                        measurable.updateProperty(propertyName,dateValue);
                        break;
                    case ApplicationConstant.DataFieldType_INT:
                        int intValue=((Integer)propertyValue).intValue();
                        measurable.updateProperty(propertyName,intValue);
                        break;
                    case ApplicationConstant.DataFieldType_LONG:
                        long longValue=((Long)propertyValue).longValue();
                        measurable.updateProperty(propertyName,longValue);
                        break;
                    case ApplicationConstant.DataFieldType_DOUBLE:
                        double doubleValue=((Double)propertyValue).doubleValue();
                        measurable.updateProperty(propertyName,doubleValue);
                        break;
                    case ApplicationConstant.DataFieldType_FLOAT:
                        float floatValue=((Float)propertyValue).floatValue();
                        measurable.updateProperty(propertyName,floatValue);
                        break;
                    case ApplicationConstant.DataFieldType_SHORT:
                        short shortValue=((Short)propertyValue).shortValue();
                        measurable.updateProperty(propertyName,shortValue);
                        break;
                    case ApplicationConstant.DataFieldType_BYTE:
                        byte byteValue=((Byte)propertyValue).byteValue();
                        measurable.updateProperty(propertyName,byteValue);
                        break;
                    case ApplicationConstant.DataFieldType_BINARY:
                        byte[] binaryValue=(byte[])propertyValue;
                        measurable.updateProperty(propertyName,binaryValue);
                        break;
                }
            }
        }
    }

    public static List<RelationValueVO> getRelationableRelationsById(String spaceName,String relationableTypeKind,String relationableId){
        List<RelationValueVO> resultRelationValueList=new ArrayList<RelationValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            List<Relation> relationsList=null;
            if(TYPEKIND_DIMENSION.equals(relationableTypeKind)){
                Dimension targetDimension=targetSpace.getDimensionById(relationableId);
                if(targetDimension!=null){
                    relationsList= targetDimension.getAllRelations();
                }
            }
            if(TYPEKIND_FACT.equals(relationableTypeKind)){
                Fact targetFact=targetSpace.getFactById(relationableId);
                if(targetFact!=null){
                    relationsList= targetFact.getAllRelations();
                }
            }
            if(relationsList!=null){
                for(Relation currentRelation:relationsList){
                    RelationValueVO currentRelationValueVO=new RelationValueVO();
                    currentRelationValueVO.setId(currentRelation.getId());
                    currentRelationValueVO.setRelationTypeName(currentRelation.getType());
                    currentRelationValueVO.setDiscoverSpaceName(spaceName);

                    Relationable fromRelationable=currentRelation.getFromRelationable();
                    if(fromRelationable!=null){
                        RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
                        fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                        fromRelationableValueVO.setId(fromRelationable.getId());
                        if(fromRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        }
                        if(fromRelationable instanceof Fact){
                            Fact factRelationable=(Fact)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                    }

                    Relationable toRelationable=currentRelation.getToRelationable();
                    if(toRelationable!=null){
                        RelationableValueVO toRelationableValueVO=new RelationableValueVO();
                        toRelationableValueVO.setDiscoverSpaceName(spaceName);
                        toRelationableValueVO.setId(toRelationable.getId());
                        if(toRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            toRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        }
                        if(toRelationable instanceof Fact){
                            Fact factRelationable=(Fact)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            toRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setToRelationable(toRelationableValueVO);
                    }

                    List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                    currentRelationValueVO.setProperties(propertyValueVOList);
                    /* for better performance but get properties's value here
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                    */
                    resultRelationValueList.add(currentRelationValueVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return resultRelationValueList;
    }

    public static List<PropertyValueVO> getMeasurablePropertiesById(String spaceName,String measurableId){
        List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable=targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable!=null){
                List<Property> propertiesList=targetMeasurable.getProperties();
                if(propertiesList!=null){
                    for(Property currentProperty:propertiesList){
                        if(currentProperty.getPropertyType()!=null){
                            PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                            currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                            currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                            currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                            propertyValueVOList.add(currentPropertyValueVO);
                        }
                    }
                }
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return propertyValueVOList;
    }

    public static MeasurableValueVO getMeasurableValueById(String spaceName,String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable=targetSpace.getMeasurableById(measurableId);
            if(targetMeasurable!=null){
                MeasurableValueVO currentMeasurableValueVO=new MeasurableValueVO();
                currentMeasurableValueVO.setDiscoverSpaceName(spaceName);
                if(targetMeasurable instanceof Dimension){
                    Dimension dimensionRelationable=(Dimension)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(dimensionRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_DIMENSION);
                    currentMeasurableValueVO.setId(dimensionRelationable.getId());
                }
                if(targetMeasurable instanceof Fact){
                    Fact factRelationable=(Fact)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(factRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_FACT);
                    currentMeasurableValueVO.setId(factRelationable.getId());
                }
                if(targetMeasurable instanceof Relation){
                    Relation relationRelationable=(Relation)targetMeasurable;
                    currentMeasurableValueVO.setMeasurableTypeName(relationRelationable.getType());
                    currentMeasurableValueVO.setMeasurableTypeKind(TYPEKIND_RELATION);
                    currentMeasurableValueVO.setId(relationRelationable.getId());
                }
                List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                currentMeasurableValueVO.setProperties(propertyValueVOList);
                List<Property> propertiesList=targetMeasurable.getProperties();
                if(propertiesList!=null){
                    for(Property currentProperty:propertiesList){
                        if(currentProperty.getPropertyType()!=null){
                            PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                            currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                            currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                            currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                            propertyValueVOList.add(currentPropertyValueVO);
                        }
                    }
                }
                return currentMeasurableValueVO;
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static boolean createRelations(RelationableValueVO sourceRelationableValue,String relationTypeName,
                                          String relationDirection,List<PropertyValueVO> relationProperties,
                                          List<String> targetDimensionsIdList, List<String> targetFactsIdList){
        String discoverSpaceName=sourceRelationableValue.getDiscoverSpaceName();
        String relationableTypeKind=sourceRelationableValue.getRelationableTypeKind();
        String sourceRelationablId=sourceRelationableValue.getId();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            Relationable sourceRelationable=null;
            if(TYPEKIND_DIMENSION.equals(relationableTypeKind)){
                sourceRelationable=targetSpace.getDimensionById(sourceRelationablId);
            }
            if(TYPEKIND_FACT.equals(relationableTypeKind)){
                sourceRelationable=targetSpace.getFactById(sourceRelationablId);
            }
            if(sourceRelationable==null){
                return false;
            }else{
                Relationable targetRelationable=null;
                if(targetDimensionsIdList!=null) {
                    for (String currentDimensionId:targetDimensionsIdList) {
                        targetRelationable=targetSpace.getDimensionById(currentDimensionId);
                        if(targetRelationable!=null){
                            addRelationWithProperties(sourceRelationable,targetRelationable,relationTypeName,relationDirection,relationProperties);
                        }
                    }
                }
                if(targetFactsIdList!=null) {
                    for (String currentFactId:targetFactsIdList) {
                        targetRelationable=targetSpace.getFactById(currentFactId);
                        if(targetRelationable!=null){
                            addRelationWithProperties(sourceRelationable,targetRelationable,relationTypeName,relationDirection,relationProperties);
                        }
                    }
                }
            }
            return true;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    private static void addRelationWithProperties(Relationable sourceRelationable,
                                                  Relationable targetRelationable,String relationTypeName,String relationDirection,
                                                  List<PropertyValueVO> relationProperties) throws InfoDiscoveryEngineRuntimeException {
        try {
            Map<String, Object> propertiesNeedAddMap=new HashMap<String, Object>();
            for(PropertyValueVO currentNewPropertyValue:relationProperties){
                String propertyName=currentNewPropertyValue.getPropertyName();
                propertiesNeedAddMap.put(propertyName,getMeasurablePropertyValue(currentNewPropertyValue));
            }
            if (RELATION_DIRECTION_BOTH.equals(relationDirection)) {
                sourceRelationable.addFromRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
                sourceRelationable.addToRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
            if (RELATION_DIRECTION_FROM.equals(relationDirection)) {
                sourceRelationable.addFromRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
            if (RELATION_DIRECTION_TO.equals(relationDirection)) {
                sourceRelationable.addToRelation(targetRelationable, relationTypeName,propertiesNeedAddMap);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new InfoDiscoveryEngineRuntimeException();
        }
    }

    public static boolean removeMeasurableById(String spaceName,String measurableType,String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(measurableType)){
                return targetSpace.removeDimension(measurableId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(measurableType)){
                return targetSpace.removeFact(measurableId);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(measurableType)){
                return targetSpace.removeRelation(measurableId);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static RelationValueVO getRelationById(String spaceName,String relationId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Relation targetRelation = targetSpace.getRelationById(relationId);
            if(targetRelation!=null){
                RelationValueVO currentRelationValueVO=new RelationValueVO();
                currentRelationValueVO.setId(targetRelation.getId());
                currentRelationValueVO.setRelationTypeName(targetRelation.getType());
                currentRelationValueVO.setDiscoverSpaceName(spaceName);

                Relationable fromRelationable=targetRelation.getFromRelationable();
                if(fromRelationable!=null){
                    RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
                    fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                    fromRelationableValueVO.setId(fromRelationable.getId());
                    if(fromRelationable instanceof Dimension){
                        Dimension dimensionRelationable=(Dimension)fromRelationable;
                        fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                        fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                    }
                    if(fromRelationable instanceof Fact){
                        Fact factRelationable=(Fact)fromRelationable;
                        fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                        fromRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                    }
                    currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                }

                Relationable toRelationable=targetRelation.getToRelationable();
                if(toRelationable!=null){
                    RelationableValueVO toRelationableValueVO=new RelationableValueVO();
                    toRelationableValueVO.setDiscoverSpaceName(spaceName);
                    toRelationableValueVO.setId(toRelationable.getId());
                    if(toRelationable instanceof Dimension){
                        Dimension dimensionRelationable=(Dimension)toRelationable;
                        toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                        toRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                    }
                    if(toRelationable instanceof Fact){
                        Fact factRelationable=(Fact)toRelationable;
                        toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                        toRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                    }
                    currentRelationValueVO.setToRelationable(toRelationableValueVO);
                }

                List<PropertyValueVO> propertyValueVOList=new ArrayList<PropertyValueVO>();
                currentRelationValueVO.setProperties(propertyValueVOList);
                    /* for better performance but get properties's value here
                    List<Property> propertiesList=currentRelation.getProperties();
                    if(propertiesList!=null){
                        for(Property currentProperty:propertiesList){
                            if(currentProperty.getPropertyType()!=null){
                                PropertyValueVO currentPropertyValueVO=new PropertyValueVO();
                                currentPropertyValueVO.setPropertyName(currentProperty.getPropertyName());
                                currentPropertyValueVO.setPropertyType(currentProperty.getPropertyType().toString());
                                currentPropertyValueVO.setPropertyValue(currentProperty.getPropertyValue());
                                propertyValueVOList.add(currentPropertyValueVO);
                            }
                        }
                    }
                    */
                return currentRelationValueVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static boolean initCountriesAndRegionsDimensionData(String discoverSpaceName,String dimensionTypePerfix){
        String continentsDimensionTypeName=dimensionTypePerfix+"_"+"geo_Continents";
        String countriesAndRegionsDimensionTypeName=dimensionTypePerfix+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypePerfix+"_"+"geo_BelongsTo";
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            if(!targetSpace.hasDimensionType(continentsDimensionTypeName)){
                DimensionType continentsDimensionType= targetSpace.addDimensionType(continentsDimensionTypeName);
                TypeProperty continentChineseNameTypeProperty= continentsDimensionType.addTypeProperty("continentChineseName",PropertyType.STRING);
                continentChineseNameTypeProperty.setMandatory(true);
                TypeProperty continentEnglishNameTypeProperty=continentsDimensionType.addTypeProperty("continentEnglishName",PropertyType.STRING);
                continentEnglishNameTypeProperty.setMandatory(true);
                TypeProperty continentChineseFullNameTypeProperty=continentsDimensionType.addTypeProperty("continentChineseFullName",PropertyType.STRING);
                continentChineseFullNameTypeProperty.setMandatory(false);
            }
            if(!targetSpace.hasDimensionType(countriesAndRegionsDimensionTypeName)){
                DimensionType countriesAndRegionsDimensionType= targetSpace.addDimensionType(countriesAndRegionsDimensionTypeName);
                TypeProperty _2bitCodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("2bitCode",PropertyType.STRING);
                _2bitCodeTypeProperty.setMandatory(true);
                TypeProperty _3bitCodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("3bitCode",PropertyType.STRING);
                _3bitCodeTypeProperty.setMandatory(true);
                TypeProperty numberTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("number",PropertyType.STRING);
                numberTypeProperty.setMandatory(true);
                TypeProperty ISO3122_2CodeTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("ISO3122_2Code",PropertyType.STRING);
                ISO3122_2CodeTypeProperty.setMandatory(true);
                TypeProperty _EnglishNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("EnglishName",PropertyType.STRING);
                _EnglishNameTypeProperty.setMandatory(true);
                TypeProperty _ChineseNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("ChineseName",PropertyType.STRING);
                _ChineseNameTypeProperty.setMandatory(true);
                TypeProperty belongedContinentTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("belongedContinent",PropertyType.STRING);
                belongedContinentTypeProperty.setMandatory(false);
                TypeProperty capitalChineseNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("capitalChineseName",PropertyType.STRING);
                capitalChineseNameTypeProperty.setMandatory(false);
                TypeProperty capitalEnglishNameTypeProperty= countriesAndRegionsDimensionType.addTypeProperty("capitalEnglishName",PropertyType.STRING);
                capitalEnglishNameTypeProperty.setMandatory(false);
            }
            if(!targetSpace.hasRelationType(geoBelongsToRelationTypeName)){
                targetSpace.addRelationType(geoBelongsToRelationTypeName);
            }

            //Add Continents dimension
            Dimension _AsiaDimension=null;
            Dimension _EuropeDimension=null;
            Dimension _NorthAmericaDimension=null;
            Dimension _SouthAmericaDimension=null;
            Dimension _AfricaDimension=null;
            Dimension _OceaniaDimension=null;
            Dimension _AntarcticaDimension=null;

            File continentsInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"ContinentsInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(continentsInfoFile),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String[] continentsInfoValueArray = lineTxt.split("\\|");
                Dimension targetContinentDimension=DiscoverEngineComponentFactory.createDimension(continentsDimensionTypeName);
                targetContinentDimension.setInitProperty("continentChineseName",continentsInfoValueArray[0]);
                targetContinentDimension.setInitProperty("continentEnglishName",continentsInfoValueArray[2]);
                if(!"-".equals(continentsInfoValueArray[1])){
                    targetContinentDimension.setInitProperty("continentChineseFullName",continentsInfoValueArray[1]);
                }
                targetContinentDimension=targetSpace.addDimension(targetContinentDimension);
                if("Asia".equals(continentsInfoValueArray[2])){
                    _AsiaDimension=targetContinentDimension;
                }
                if("Europe".equals(continentsInfoValueArray[2])){
                    _EuropeDimension=targetContinentDimension;
                }
                if("North America".equals(continentsInfoValueArray[2])){
                    _NorthAmericaDimension=targetContinentDimension;
                }
                if("South America".equals(continentsInfoValueArray[2])){
                    _SouthAmericaDimension=targetContinentDimension;
                }
                if("Africa".equals(continentsInfoValueArray[2])){
                    _AfricaDimension=targetContinentDimension;
                }
                if("Oceania".equals(continentsInfoValueArray[2])){
                    _OceaniaDimension=targetContinentDimension;
                }
                if("Antarctica".equals(continentsInfoValueArray[2])){
                    _AntarcticaDimension=targetContinentDimension;
                }
            }
            br.close();

            File countriesAndRegionInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"ISO_3166_2_CountriesAndRegions.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(countriesAndRegionInfoFile),"UTF-8"));
            while ((lineTxt = br1.readLine()) != null) {
                String[] countriesAndRegionInfoValueArray = lineTxt.split("\\|");
                Dimension targetCountryOrRegionDimension=DiscoverEngineComponentFactory.createDimension(countriesAndRegionsDimensionTypeName);
                targetCountryOrRegionDimension.setInitProperty("2bitCode",countriesAndRegionInfoValueArray[0].trim());
                targetCountryOrRegionDimension.setInitProperty("3bitCode",countriesAndRegionInfoValueArray[1].trim());
                targetCountryOrRegionDimension.setInitProperty("number",countriesAndRegionInfoValueArray[2].trim());
                targetCountryOrRegionDimension.setInitProperty("ISO3122_2Code",countriesAndRegionInfoValueArray[3].trim());
                targetCountryOrRegionDimension.setInitProperty("EnglishName",countriesAndRegionInfoValueArray[4].trim());
                targetCountryOrRegionDimension.setInitProperty("ChineseName",countriesAndRegionInfoValueArray[5].trim());
                if(!"-".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.setInitProperty("belongedContinent",countriesAndRegionInfoValueArray[6].trim());
                }
                if(!"-".equals(countriesAndRegionInfoValueArray[7].trim())){
                    targetCountryOrRegionDimension.setInitProperty("capitalChineseName",countriesAndRegionInfoValueArray[7].trim());
                }
                if(!"-".equals(countriesAndRegionInfoValueArray[8].trim())){
                    targetCountryOrRegionDimension.setInitProperty("capitalEnglishName",countriesAndRegionInfoValueArray[8].trim());
                }

                targetCountryOrRegionDimension=targetSpace.addDimension(targetCountryOrRegionDimension);

                if("Asia".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AsiaDimension,geoBelongsToRelationTypeName);
                }
                if("Europe".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_EuropeDimension,geoBelongsToRelationTypeName);
                }
                if("North America".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_NorthAmericaDimension,geoBelongsToRelationTypeName);
                }
                if("South America".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_SouthAmericaDimension,geoBelongsToRelationTypeName);
                }
                if("Africa".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AfricaDimension,geoBelongsToRelationTypeName);
                }
                if("Oceania".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_OceaniaDimension,geoBelongsToRelationTypeName);
                }
                if("Antarctica".equals(countriesAndRegionInfoValueArray[6].trim())){
                    targetCountryOrRegionDimension.addToRelation(_AntarcticaDimension,geoBelongsToRelationTypeName);
                }
            }
            br1.close();
            return true;
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean initChineseAdministrativeDivisionDimensionData(String discoverSpaceName,String dimensionTypePerfix,String chinaDimensionId){
        String countriesAndRegionsDimensionTypeName=dimensionTypePerfix+"_"+"geo_CountriesAndRegions";
        String geoBelongsToRelationTypeName=dimensionTypePerfix+"_"+"geo_BelongsTo";

        String administrativeDivisionDimensionTypeName=dimensionTypePerfix+"_"+"geo_AdministrativeDivision";
        String provincialLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_ProvincialLevel";
        String cityLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_CityLevel";
        String districtLevelDimensionTypeName=dimensionTypePerfix+"_"+"geo_DistrictLevel";
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);

            Dimension _ChinaDimension=targetSpace.getDimensionById(chinaDimensionId);
            if(_ChinaDimension==null){
                return false;
            }
            if(!_ChinaDimension.getType().equals(countriesAndRegionsDimensionTypeName)){
                return false;
            }
            if(!targetSpace.hasDimensionType(administrativeDivisionDimensionTypeName)){
                DimensionType adminDivisionDimensionType =targetSpace.addDimensionType(administrativeDivisionDimensionTypeName);
                TypeProperty divisionCodeTypeProperty= adminDivisionDimensionType.addTypeProperty("divisionCode",PropertyType.STRING);
                divisionCodeTypeProperty.setMandatory(true);
                TypeProperty divisionNameTypeProperty= adminDivisionDimensionType.addTypeProperty("divisionName",PropertyType.STRING);
                divisionNameTypeProperty.setMandatory(true);
            }

            if(!targetSpace.hasDimensionType(provincialLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(provincialLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }
            if(!targetSpace.hasDimensionType(cityLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(cityLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }
            if(!targetSpace.hasDimensionType(districtLevelDimensionTypeName)){
                targetSpace.addChildDimensionType(districtLevelDimensionTypeName,administrativeDivisionDimensionTypeName);
            }

            Map<String,Dimension> existDimensionMap=new HashMap<>();

            File continentsInfoFile =new File(RuntimeEnvironmentHandler.getApplicationRootPath()+"China_MinistryOfCivilAffairs_AdministrativeDivision_Code.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(continentsInfoFile),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String administrativeDivision_CodeInfoStr=lineTxt.trim();
                String[] codeInfoValueArray = administrativeDivision_CodeInfoStr.split(" ");
                int infoLength=codeInfoValueArray.length;

                if(infoLength==2){
                    //省,自治区，直辖市，港澳台，钓鱼岛等
                    String divisionCode=codeInfoValueArray[0].trim();
                    String divisionName=codeInfoValueArray[1].trim();

                    Dimension targetProvincialLevelDimension=DiscoverEngineComponentFactory.createDimension(provincialLevelDimensionTypeName);
                    targetProvincialLevelDimension.setInitProperty("divisionCode",divisionCode);
                    targetProvincialLevelDimension.setInitProperty("divisionName",divisionName);

                    targetProvincialLevelDimension=targetSpace.addDimension(targetProvincialLevelDimension);
                    targetProvincialLevelDimension.addToRelation(_ChinaDimension,geoBelongsToRelationTypeName);

                    existDimensionMap.put(divisionName,targetProvincialLevelDimension);

                }
                if(infoLength==3){
                    String divisionCode=codeInfoValueArray[0].trim();
                    String divisionNameStr=codeInfoValueArray[2].trim();
                    String[] divisionNameArray=divisionNameStr.split("-");
                    if(divisionNameArray.length==2){
                        //市级
                        String cityLevelDivisionName=divisionNameArray[1].trim();
                        String parentLevelName=divisionNameArray[0].trim();

                        Dimension targetCityLevelDimension=DiscoverEngineComponentFactory.createDimension(cityLevelDimensionTypeName);
                        targetCityLevelDimension.setInitProperty("divisionCode",divisionCode);
                        targetCityLevelDimension.setInitProperty("divisionName",cityLevelDivisionName);

                        targetCityLevelDimension=targetSpace.addDimension(targetCityLevelDimension);
                        if(existDimensionMap.get(parentLevelName)!=null){
                            targetCityLevelDimension.addToRelation(existDimensionMap.get(parentLevelName),geoBelongsToRelationTypeName);
                        }
                        existDimensionMap.put(parentLevelName+"_"+cityLevelDivisionName,targetCityLevelDimension);
                    }
                    if(divisionNameArray.length==3){
                        //区级
                        String districtLevelDivisionName=divisionNameArray[2].trim();
                        String parentLevelName=divisionNameArray[0].trim()+"_"+divisionNameArray[1].trim();

                        Dimension targetDistrictLevelDimension=DiscoverEngineComponentFactory.createDimension(districtLevelDimensionTypeName);
                        targetDistrictLevelDimension.setInitProperty("divisionCode",divisionCode);
                        targetDistrictLevelDimension.setInitProperty("divisionName",districtLevelDivisionName);

                        targetDistrictLevelDimension=targetSpace.addDimension(targetDistrictLevelDimension);
                        if(existDimensionMap.get(parentLevelName)!=null){
                            targetDistrictLevelDimension.addToRelation(existDimensionMap.get(parentLevelName),geoBelongsToRelationTypeName);
                        }
                    }
                }
            }
            br.close();
            return true;
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static long countDimensionRelationsById(String spaceName,String dimensionId,String relationType,RelationDirection relationDirection){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Dimension targetDimension = targetSpace.getDimensionById(dimensionId);
            if (targetDimension == null) {
                return Long.MIN_VALUE;
            } else {
                List<Relation> relatedDataList = targetDimension.getSpecifiedRelations(relationType, relationDirection);
                return relatedDataList.size();
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return Long.MIN_VALUE;
    }

    public static boolean initMetaConfigDiscoverSpace(){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        boolean metaConfigSpaceAlreadyExist=DiscoverEngineComponentFactory.checkDiscoverSpaceExistence(metaConfigSpaceName);
        if(!metaConfigSpaceAlreadyExist){
            return DiscoverEngineComponentFactory.createInfoDiscoverSpace(metaConfigSpaceName);
        }else{
            return true;
        }
    }

    private static boolean recordTypeKindAliasName(String spaceName,String typeKind,String typeName,String typeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(TYPEKIND_AliasNameFactType)){
                FactType typeKindAliasNameFactType=metaConfigSpace.addFactType(TYPEKIND_AliasNameFactType);
                TypeProperty discoverSpaceProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace,PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty typeKindProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeKind,PropertyType.STRING);
                typeKindProperty.setMandatory(true);
                TypeProperty typeNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeName,PropertyType.STRING);
                typeNameProperty.setMandatory(true);
                TypeProperty typeAliasNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeAliasName,PropertyType.STRING);
                typeAliasNameProperty.setMandatory(true);
            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(TYPEKIND_AliasNameFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeKind,typeKind);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeName,typeName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeAliasName,typeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
                String recordKey=spaceName+"_"+typeKind+"_"+typeName;
                TYPEKIND_AliasNameMap.put(recordKey,typeAliasName);
                return true;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    private static void deleteTypeKindAliasName(String spaceName,String typeKind,String typeName) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                }
            }
            String recordKey=spaceName+"_"+typeKind+"_"+typeName;
            TYPEKIND_AliasNameMap.remove(recordKey);

            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            List<Fact> propertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(propertyAliasRecordFact!=null) {
                for (Fact currentRecordFact : propertyAliasRecordFact) {
                    String propertyName=currentRecordFact.getProperty(MetaConfig_PropertyName_TypePropertyName).getPropertyValue().toString();
                    String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+propertyName;
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                    TypeProperty_AliasNameMap.remove(propertyRecordKey);
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }

    private static boolean recordTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName,String typePropertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(TYPEPROPERTY_AliasNameFactType)){
                FactType typeKindAliasNameFactType=metaConfigSpace.addFactType(TYPEPROPERTY_AliasNameFactType);
                TypeProperty discoverSpaceProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_DiscoverSpace,PropertyType.STRING);
                discoverSpaceProperty.setMandatory(true);
                TypeProperty typeKindProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeKind,PropertyType.STRING);
                typeKindProperty.setMandatory(true);
                TypeProperty typeNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypeName,PropertyType.STRING);
                typeNameProperty.setMandatory(true);
                TypeProperty typePropertyNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypePropertyName,PropertyType.STRING);
                typePropertyNameProperty.setMandatory(true);
                TypeProperty typePropertyAliasNameProperty=typeKindAliasNameFactType.addTypeProperty(MetaConfig_PropertyName_TypePropertyAliasName,PropertyType.STRING);
                typePropertyAliasNameProperty.setMandatory(true);

            }
            Fact typeKind_AliasNameFact=DiscoverEngineComponentFactory.createFact(TYPEPROPERTY_AliasNameFactType);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_DiscoverSpace,spaceName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeKind,typeKind);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypeName,typeName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypePropertyName,typePropertyName);
            typeKind_AliasNameFact.setInitProperty(MetaConfig_PropertyName_TypePropertyAliasName,typePropertyAliasName);
            Fact resultRecord=metaConfigSpace.addFact(typeKind_AliasNameFact);
            if(resultRecord!=null){
                String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
                TypeProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
                return true;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    private static void deleteTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName) {
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypePropertyName, typePropertyName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1000000);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFact!=null) {
                for (Fact currentRecordFact : typeAliasRecordFact) {
                    metaConfigSpace.removeFact(currentRecordFact.getId());
                }
            }
            String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
            TypeProperty_AliasNameMap.remove(propertyRecordKey);
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }

    public static String getTypeKindAliasName(String spaceName,String typeKind,String typeName){
        String typeKindRecordKey=spaceName+"_"+typeKind+"_"+typeName;
        if(TYPEKIND_AliasNameMap.get(typeKindRecordKey)!=null){
            return TYPEKIND_AliasNameMap.get(typeKindRecordKey);
        }
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFactsList = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFactsList!=null) {
                if(typeAliasRecordFactsList.size()>0){
                    Fact targetFact=typeAliasRecordFactsList.get(0);
                    String typeKindAliasName=targetFact.getProperty(MetaConfig_PropertyName_TypeAliasName).getPropertyValue().toString();
                    TYPEKIND_AliasNameMap.put(typeKindRecordKey,typeKindAliasName);
                    return typeKindAliasName;
                }else{
                    TYPEKIND_AliasNameMap.put(typeKindRecordKey,"");
                }
            }else{
                TYPEKIND_AliasNameMap.put(typeKindRecordKey,"");
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return null;
    }

    public static String getTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName) {
        String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
        if(TypeProperty_AliasNameMap.get(propertyRecordKey)!=null){
            return TypeProperty_AliasNameMap.get(propertyRecordKey);
        }
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypePropertyName, typePropertyName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typePropertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typePropertyAliasRecordFact!=null) {
                if(typePropertyAliasRecordFact.size()>0){
                    Fact targetFact=typePropertyAliasRecordFact.get(0);
                    String typeKindAliasName=targetFact.getProperty(MetaConfig_PropertyName_TypePropertyAliasName).getPropertyValue().toString();
                    TypeProperty_AliasNameMap.put(propertyRecordKey,typeKindAliasName);
                    return typeKindAliasName;
                }else{
                    TypeProperty_AliasNameMap.put(propertyRecordKey,"");
                }
            }else{
                TypeProperty_AliasNameMap.put(propertyRecordKey,"");
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<RelationableValueVO> getSimilarRelationableConnectedSameDimensions(String spaceName,String sourceRelationableId,List<String> relatedDimensionsList,String filteringPattern){
        List<RelationableValueVO> relationableValueVOList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            List<Relationable> similarRelationableList=null;
            if("ALL".equals(filteringPattern)){
                similarRelationableList=ie.discoverSimilarRelationablesRelatedToSameDimensions(sourceRelationableId,relatedDimensionsList,InformationExplorer.FilteringPattern.AND);
            }
            if("ANY".equals(filteringPattern)){
                similarRelationableList=ie.discoverSimilarRelationablesRelatedToSameDimensions(sourceRelationableId,relatedDimensionsList,InformationExplorer.FilteringPattern.OR);
            }
            if(similarRelationableList!=null) {
                for (Relationable currentRelationable : similarRelationableList) {
                    RelationableValueVO currentRelationableValueVO=new RelationableValueVO();
                    if(currentRelationable instanceof Fact){
                        Fact currentFact=(Fact)currentRelationable;
                        currentRelationableValueVO.setDiscoverSpaceName(spaceName);
                        currentRelationableValueVO.setId(currentFact.getId());
                        currentRelationableValueVO.setRelationableTypeKind(TYPEKIND_FACT);
                        currentRelationableValueVO.setRelationableTypeName(currentFact.getType());
                        currentRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,TYPEKIND_FACT,currentFact.getType()));
                    }
                    if(currentRelationable instanceof Dimension){
                        Dimension currentDimension=(Dimension)currentRelationable;
                        currentRelationableValueVO.setDiscoverSpaceName(spaceName);
                        currentRelationableValueVO.setId(currentDimension.getId());
                        currentRelationableValueVO.setRelationableTypeKind(TYPEKIND_DIMENSION);
                        currentRelationableValueVO.setRelationableTypeName(currentDimension.getType());
                        currentRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,TYPEKIND_DIMENSION,currentDimension.getType()));
                    }
                    relationableValueVOList.add(currentRelationableValueVO);
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationableValueVOList;
    }

    public static boolean hasShortestPathBetweenTwoRelationables(String spaceName, String relationable1Id, String relationable2Id){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            InformationExplorer ie=targetSpace.getInformationExplorer();
            Stack<Relation> shortestPathRelationsStack=ie.discoverRelationablesShortestPath(relationable1Id,relationable2Id,RelationDirection.TWO_WAY);

            if(shortestPathRelationsStack!=null){
                return true;
            }else{
                return false;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return false;
    }

    public static void clearItemAliasNameCache(){
        TYPEKIND_AliasNameMap.clear();
        TypeProperty_AliasNameMap.clear();
    }

    public static void refreshItemAliasNameCache(){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(TYPEKIND_AliasNameFactType)){
                ExploreParameters typeKindRecordEP = new ExploreParameters();
                typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
                typeKindRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> typeAliasRecordFactsList = ie.discoverFacts(typeKindRecordEP);
                if(typeAliasRecordFactsList!=null){
                    String spaceName=null;
                    String typeKind=null;
                    String typeName=null;
                    for(Fact currentFact:typeAliasRecordFactsList){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _TypeKind=currentFact.getProperty(MetaConfig_PropertyName_TypeKind);
                        if(_TypeKind!=null){
                            typeKind=_TypeKind.getPropertyValue().toString();
                        }else{
                            typeKind=null;
                        }
                        Property _TypeName=currentFact.getProperty(MetaConfig_PropertyName_TypeName);
                        if(_TypeName!=null){
                            typeName=_TypeName.getPropertyValue().toString();
                        }else{
                            typeName=null;
                        }
                        Property _TypeAliasName=currentFact.getProperty(MetaConfig_PropertyName_TypeAliasName);
                        if(_TypeAliasName!=null){
                            String typeKindRecordKey=spaceName+"_"+typeKind+"_"+typeName;
                            String typeAliasName=_TypeAliasName.getPropertyValue().toString();
                            if(TYPEKIND_AliasNameMap.containsKey(typeKindRecordKey)) {
                                TYPEKIND_AliasNameMap.remove(typeKindRecordKey);
                            }
                            TYPEKIND_AliasNameMap.put(typeKindRecordKey,typeAliasName);
                        }
                    }
                }
            }

            if(metaConfigSpace.hasFactType(TYPEPROPERTY_AliasNameFactType)){
                ExploreParameters typeKindRecordEP = new ExploreParameters();
                typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
                typeKindRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> typePropertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
                if(typePropertyAliasRecordFact!=null) {
                    String spaceName=null;
                    String typeKind=null;
                    String typeName=null;
                    String typePropertyName=null;
                    for(Fact currentFact:typePropertyAliasRecordFact){
                        Property _DiscoverSpace=currentFact.getProperty(MetaConfig_PropertyName_DiscoverSpace);
                        if(_DiscoverSpace!=null){
                            spaceName=_DiscoverSpace.getPropertyValue().toString();
                        }else{
                            spaceName=null;
                        }
                        Property _TypeKind=currentFact.getProperty(MetaConfig_PropertyName_TypeKind);
                        if(_TypeKind!=null){
                            typeKind=_TypeKind.getPropertyValue().toString();
                        }else{
                            typeKind=null;
                        }
                        Property _TypeName=currentFact.getProperty(MetaConfig_PropertyName_TypeName);
                        if(_TypeName!=null){
                            typeName=_TypeName.getPropertyValue().toString();
                        }else{
                            typeName=null;
                        }
                        Property _TypePropertyName=currentFact.getProperty(MetaConfig_PropertyName_TypePropertyName);
                        if(_TypePropertyName!=null){
                            typePropertyName=_TypePropertyName.getPropertyValue().toString();
                        }else{
                            typePropertyName=null;
                        }
                        Property _TypePropertyAliasName=currentFact.getProperty(MetaConfig_PropertyName_TypePropertyAliasName);
                        if(_TypePropertyAliasName!=null){
                            String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
                            String typePropertyAliasName=_TypePropertyAliasName.getPropertyValue().toString();
                            if(TypeProperty_AliasNameMap.containsKey(propertyRecordKey)) {
                                TypeProperty_AliasNameMap.remove(propertyRecordKey);
                            }
                            TypeProperty_AliasNameMap.put(propertyRecordKey,typePropertyAliasName);
                        }
                    }
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
    }
}

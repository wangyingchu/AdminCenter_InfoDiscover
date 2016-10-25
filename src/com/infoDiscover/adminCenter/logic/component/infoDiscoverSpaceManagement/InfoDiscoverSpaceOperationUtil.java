package com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement;

import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.DimensionTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyTypeVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.PropertyValueVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.RelationTypeVO;
import com.infoDiscover.adminCenter.ui.util.ApplicationConstant;
import com.infoDiscover.infoDiscoverEngine.dataMart.*;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverAdminSpace;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticHelper;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangychu on 9/30/16.
 */
public class InfoDiscoverSpaceOperationUtil {
    public static final String TYPEKIND_DIMENSION="TYPEKIND_DIMENSION";
    public static final String TYPEKIND_FACT="TYPEKIND_FACT";
    public static final String TYPEKIND_RELATION="TYPEKIND_RELATION";

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
                    DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(currentDimensionType,dimensionsStatisticMetrics);
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
                    RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(currentRelationType, relationsStatisticMetrics);
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
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(currentDimensionType,dimensionsStatisticMetrics);
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
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(currentRelationType, relationsStatisticMetrics);
                return currentRelationTypeVO;
            }
        }finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static DimensionTypeVO retrieveDimensionTypeRuntimeInfo(DimensionType targetDimensionType,List<DataTypeStatisticMetrics> dimensionsStatisticMetrics){
        DimensionTypeVO targetDimensionTypeVO=new DimensionTypeVO();
        targetDimensionTypeVO.setTypeName(targetDimensionType.getTypeName());
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
                DimensionTypeVO currentDimensionTypeVO=retrieveDimensionTypeRuntimeInfo(currentDimensionType,dimensionsStatisticMetrics);
                childDimensionTypesVOList.add(currentDimensionTypeVO);
            }
        }
        return targetDimensionTypeVO;
    }

    private static RelationTypeVO retrieveRelationTypeRuntimeInfo(RelationType targetRelationType,List<DataTypeStatisticMetrics> relationsStatisticMetrics){
        RelationTypeVO targetRelationTypeVO=new RelationTypeVO();
        targetRelationTypeVO.setTypeName(targetRelationType.getTypeName());
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
                RelationTypeVO currentRelationTypeVO=retrieveRelationTypeRuntimeInfo(currentRelationType, relationsStatisticMetrics);
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

    public static boolean createDimensionType(String spaceName, String dimensionTypeName,String parentDimensionTypeName){
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

    public static boolean createRelationType(String spaceName, String relationTypeName,String parentRelationTypeName){
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

    public static boolean createFactType(String spaceName, String factTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            if(targetSpace.hasFactType(factTypeName)) {
                return false;
            }else{
                FactType targetFactType=targetSpace.addFactType(factTypeName);
                if(targetFactType!=null&&targetFactType.getTypeName().equals(factTypeName)){
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
                return targetSpace.removeDimensionType(dimensionTypeName);
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
                return targetSpace.removeRelationType(relationTypeName);
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
                return targetSpace.removeFactType(factTypeName);
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
                                             String propertyName,String propertyType,boolean isMandatory,boolean isReadOnly,boolean isNullable){
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
            return adminSpace.removeDimensionTypeProperty(dimensionTypeName,propertyName);
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
            return adminSpace.removeRelationTypeProperty(relationTypeName, propertyName);
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
            return adminSpace.removeFactTypeProperty(factTypeName,propertyName);
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
}

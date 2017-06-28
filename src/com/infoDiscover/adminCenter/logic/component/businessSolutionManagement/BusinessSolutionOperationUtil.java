package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.DimensionTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.FactTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.RelationTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.SolutionTypePropertyTypeDefinitionVO;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.InfoDiscoverSpaceOperationUtil;
import com.infoDiscover.adminCenter.ui.util.AdminCenterPropertyHandler;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.FactType;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.dataMart.TypeProperty;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangychu on 5/4/17.
 */
public class BusinessSolutionOperationUtil {

    public static final String BUSINESSSOLUTION_SolutionDefinitionFactType="BusinessSolution_SolutionDefinition";
    public static final String BUSINESSSOLUTION_SolutionFactTypeFactType="BusinessSolution_SolutionFactType";
    public static final String BUSINESSSOLUTION_SolutionDimensionTypeFactType="BusinessSolution_SolutionDimensionType";
    public static final String BUSINESSSOLUTION_SolutionRelationTypeFactType="BusinessSolution_SolutionRelationType";

    public static final String BUSINESSSOLUTION_SolutionTypePropertyFactType="BusinessSolution_SolutionTypePropertyType";

    public static final String MetaConfig_PropertyName_SolutionName="solutionName";
    public static final String MetaConfig_PropertyName_FactTypeName="factTypeName";
    public static final String MetaConfig_PropertyName_FactTypeAliasName="factTypeAliasName";

    public static final String MetaConfig_PropertyName_DimensionTypeName="dimensionTypeName";
    public static final String MetaConfig_PropertyName_DimensionTypeAliasName="dimensionTypeAliasName";
    public static final String MetaConfig_PropertyName_ParentDimensionTypeName="parentDimensionTypeName";

    public static final String MetaConfig_PropertyName_RelationTypeName="relationTypeName";
    public static final String MetaConfig_PropertyName_RelationTypeAliasName="relationTypeAliasName";
    public static final String MetaConfig_PropertyName_ParentRelationTypeName="parentRelationTypeName";

    public static final String MetaConfig_PropertyName_PropertyName="propertyName";
    public static final String MetaConfig_PropertyName_PropertyAliasName="propertyAliasName";
    public static final String MetaConfig_PropertyName_PropertyType="propertyType";
    public static final String MetaConfig_PropertyName_PropertyTypeKind="propertyTypeKind";
    public static final String MetaConfig_PropertyName_PropertyTypeName="propertyTypeName";
    public static final String MetaConfig_PropertyName_PropertySourceOwner="propertySourceOwner";
    public static final String MetaConfig_PropertyName_IsMandatoryProperty="isMandatory";
    public static final String MetaConfig_PropertyName_IsNullableProperty="isNullable";
    public static final String MetaConfig_PropertyName_IsReadOnlyProperty="isReadOnly";

    public static boolean checkBusinessSolutionExistence(String businessSolutionName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
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
        return false;
    }

    public static boolean createBusinessSolution(String businessSolutionName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                FactType solutionDefinitionFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                TypeProperty solutionNameProperty=solutionDefinitionFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
            }
            Fact solutionDefinitionFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDefinitionFactType);
            solutionDefinitionFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            Fact resultRecord=metaConfigSpace.addFact(solutionDefinitionFact);
            if(resultRecord!=null){
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

    public static List<String> getExistBusinessSolutions(){
        List<String> businessSolutionsList=new ArrayList<>();
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                for(Fact currentFact:solutionDefinitionRecordFactsList){
                    businessSolutionsList.add(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return businessSolutionsList;
    }

    public static boolean deleteBusinessSolutionDefinition(String businessSolutionName){
        String metaConfigSpaceName = AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDefinitionFactType)){
                ExploreParameters solutionDefinitionRecordEP = new ExploreParameters();
                solutionDefinitionRecordEP.setType(BUSINESSSOLUTION_SolutionDefinitionFactType);
                solutionDefinitionRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDefinitionRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDefinitionRecordFactsList = ie.discoverFacts(solutionDefinitionRecordEP);
                if(solutionDefinitionRecordFactsList!=null&&solutionDefinitionRecordFactsList.size()>0){
                    Fact targetBusinessSolution=solutionDefinitionRecordFactsList.get(0);
                     metaConfigSpace.removeFact(targetBusinessSolution.getId());
                    //also need delete other data related to this solution
                    //remove type properties
                    if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                        ExploreParameters solutionFactTypePropertyRecordEP = new ExploreParameters();
                        solutionFactTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                        solutionFactTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                        solutionFactTypePropertyRecordEP.setResultNumber(10000);
                        List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypePropertyRecordEP);
                        if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                            for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                                metaConfigSpace.removeFact(currentFact.getId());
                            }
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
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
        return false;
    }

    public static boolean checkSolutionFactTypeExistence(String businessSolutionName,String factTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_FactTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                solutionFactTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null&&solutionFactTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createBusinessSolutionFactType(String businessSolutionName,String factTypeName,String factAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                FactType solutionFactTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                TypeProperty solutionNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty factTypeNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_FactTypeName, PropertyType.STRING);
                factTypeNameProperty.setMandatory(true);

                TypeProperty factTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_FactTypeAliasName, PropertyType.STRING);
                factTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionFactTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionFactTypeFactType);
            solutionFactTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionFactTypeFact.setInitProperty(MetaConfig_PropertyName_FactTypeName,factTypeName);
            solutionFactTypeFact.setInitProperty(MetaConfig_PropertyName_FactTypeAliasName,factAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionFactTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionFactType(String businessSolutionName,String factTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_FactTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                solutionFactTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionFactTypePropertyRecordEP = new ExploreParameters();
                    solutionFactTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionFactTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionFactTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT), ExploreParameters.FilteringLogic.AND);
                    solutionFactTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, factTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionFactTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypePropertyRecordEP);
                    if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<FactTypeDefinitionVO> getBusinessSolutionFactTypeList(String businessSolutionName){
        List<FactTypeDefinitionVO> factTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionFactTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypeDefinitionRecordFactsList){
                        FactTypeDefinitionVO currentFactTypeDefinitionVO=new FactTypeDefinitionVO();
                        currentFactTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setTypeName(currentFact.getProperty(MetaConfig_PropertyName_FactTypeName).getPropertyValue().toString());
                        currentFactTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(MetaConfig_PropertyName_FactTypeAliasName).getPropertyValue().toString());
                        factTypeDefinitionList.add(currentFactTypeDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return factTypeDefinitionList;
    }

    public static List<DimensionTypeDefinitionVO> getBusinessSolutionDimensionTypeList(String businessSolutionName){
        List<DimensionTypeDefinitionVO> dimensionTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDimensionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionDimensionTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDimensionTypeDefinitionRecordFactsList){
                        DimensionTypeDefinitionVO currentDimensionTypeDefinitionVO=new DimensionTypeDefinitionVO();
                        currentDimensionTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeName(currentFact.getProperty(MetaConfig_PropertyName_DimensionTypeName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(MetaConfig_PropertyName_DimensionTypeAliasName).getPropertyValue().toString());
                        currentDimensionTypeDefinitionVO.setParentTypeName(currentFact.getProperty(MetaConfig_PropertyName_ParentDimensionTypeName).getPropertyValue().toString());
                        dimensionTypeDefinitionList.add(currentDimensionTypeDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return dimensionTypeDefinitionList;
    }

    public static boolean checkSolutionDimensionTypeExistence(String businessSolutionName,String dimensionTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionDimensionTypeRecordEP = new ExploreParameters();
                solutionDimensionTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionDimensionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDimensionTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DimensionTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                solutionDimensionTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypeDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypeRecordEP);
                if(solutionFactTypeDefinitionRecordFactsList!=null&&solutionFactTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createBusinessSolutionDimensionType(String businessSolutionName,String parentDimensionTypeName,String dimensionTypeName,String dimensionTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                FactType solutionFactTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                TypeProperty solutionNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty dimensionTypeNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_DimensionTypeName, PropertyType.STRING);
                dimensionTypeNameProperty.setMandatory(true);

                TypeProperty dimensionTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_DimensionTypeAliasName, PropertyType.STRING);
                dimensionTypeAliasNameProperty.setMandatory(true);

                TypeProperty parentDimensionTypeAliasNameProperty=solutionFactTypeFactType.addTypeProperty(MetaConfig_PropertyName_ParentDimensionTypeName, PropertyType.STRING);
                parentDimensionTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionDimensionTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
            solutionDimensionTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionDimensionTypeFact.setInitProperty(MetaConfig_PropertyName_ParentDimensionTypeName,parentDimensionTypeName);
            solutionDimensionTypeFact.setInitProperty(MetaConfig_PropertyName_DimensionTypeName,dimensionTypeName);
            solutionDimensionTypeFact.setInitProperty(MetaConfig_PropertyName_DimensionTypeAliasName,dimensionTypeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionDimensionTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionDimensionType(String businessSolutionName,String dimensionTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionDimensionTypeRecordEP = new ExploreParameters();
                solutionDimensionTypeRecordEP.setType(BUSINESSSOLUTION_SolutionDimensionTypeFactType);
                solutionDimensionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionDimensionTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DimensionTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                solutionDimensionTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionDimensionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypeRecordEP);
                if(solutionDimensionTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionDimensionTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionDimensionTypePropertyRecordEP = new ExploreParameters();
                    solutionDimensionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionDimensionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionDimensionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION), ExploreParameters.FilteringLogic.AND);
                    solutionDimensionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, dimensionTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionDimensionTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionDimensionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionDimensionTypePropertyRecordEP);
                    if(solutionDimensionTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionDimensionTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<RelationTypeDefinitionVO> getBusinessSolutionRelationTypeList(String businessSolutionName){
        List<RelationTypeDefinitionVO> relationTypeDefinitionList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                ExploreParameters solutionFactTypeRecordEP = new ExploreParameters();
                solutionFactTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionFactTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionFactTypeRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionFactTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionRelationTypeDefinitionRecordFactsList){
                        RelationTypeDefinitionVO currentRelationTypeDefinitionVO=new RelationTypeDefinitionVO();
                        currentRelationTypeDefinitionVO.setSolutionName(currentFact.getProperty(MetaConfig_PropertyName_SolutionName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeName(currentFact.getProperty(MetaConfig_PropertyName_RelationTypeName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setTypeAliasName(currentFact.getProperty(MetaConfig_PropertyName_RelationTypeAliasName).getPropertyValue().toString());
                        currentRelationTypeDefinitionVO.setParentTypeName(currentFact.getProperty(MetaConfig_PropertyName_ParentRelationTypeName).getPropertyValue().toString());
                        relationTypeDefinitionList.add(currentRelationTypeDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return relationTypeDefinitionList;
    }

    public static boolean checkSolutionRelationTypeExistence(String businessSolutionName,String relationTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                return false;
            }
            else{
                ExploreParameters solutionRelationTypeRecordEP = new ExploreParameters();
                solutionRelationTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionRelationTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionRelationTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_RelationTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                solutionRelationTypeRecordEP.setResultNumber(1);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null&&solutionRelationTypeDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createBusinessSolutionRelationType(String businessSolutionName,String parentRelationTypeName,String relationTypeName,String relationTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                FactType solutionRelationTypeFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                TypeProperty solutionNameProperty=solutionRelationTypeFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);

                TypeProperty relationTypeNameProperty=solutionRelationTypeFactType.addTypeProperty(MetaConfig_PropertyName_RelationTypeName, PropertyType.STRING);
                relationTypeNameProperty.setMandatory(true);

                TypeProperty relationTypeAliasNameProperty=solutionRelationTypeFactType.addTypeProperty(MetaConfig_PropertyName_RelationTypeAliasName, PropertyType.STRING);
                relationTypeAliasNameProperty.setMandatory(true);

                TypeProperty parentRelationTypeAliasNameProperty=solutionRelationTypeFactType.addTypeProperty(MetaConfig_PropertyName_ParentRelationTypeName, PropertyType.STRING);
                parentRelationTypeAliasNameProperty.setMandatory(true);
            }
            Fact solutionRelationTypeFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionRelationTypeFactType);
            solutionRelationTypeFact.setInitProperty(MetaConfig_PropertyName_SolutionName,businessSolutionName);
            solutionRelationTypeFact.setInitProperty(MetaConfig_PropertyName_ParentRelationTypeName,parentRelationTypeName);
            solutionRelationTypeFact.setInitProperty(MetaConfig_PropertyName_RelationTypeName,relationTypeName);
            solutionRelationTypeFact.setInitProperty(MetaConfig_PropertyName_RelationTypeAliasName,relationTypeAliasName);
            Fact resultRecord=metaConfigSpace.addFact(solutionRelationTypeFact);
            if(resultRecord!=null){
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

    public static boolean deleteBusinessSolutionRelationType(String businessSolutionName,String relationTypeName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                return false;
            }else{
                ExploreParameters solutionRelationTypeRecordEP = new ExploreParameters();
                solutionRelationTypeRecordEP.setType(BUSINESSSOLUTION_SolutionRelationTypeFactType);
                solutionRelationTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                solutionRelationTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_RelationTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                solutionRelationTypeRecordEP.setResultNumber(10000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionRelationTypeDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypeRecordEP);
                if(solutionRelationTypeDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionRelationTypeDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                    ExploreParameters solutionRelationTypePropertyRecordEP = new ExploreParameters();
                    solutionRelationTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                    solutionRelationTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, businessSolutionName));
                    solutionRelationTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION), ExploreParameters.FilteringLogic.AND);
                    solutionRelationTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, relationTypeName), ExploreParameters.FilteringLogic.AND);
                    solutionRelationTypePropertyRecordEP.setResultNumber(10000);
                    List<Fact> solutionRelationTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionRelationTypePropertyRecordEP);
                    if(solutionRelationTypePropertyDefinitionRecordFactsList!=null){
                        for(Fact currentFact:solutionRelationTypePropertyDefinitionRecordFactsList){
                            metaConfigSpace.removeFact(currentFact.getId());
                        }
                    }
                }
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean updateSolutionTypeAliasName(String solutionName, String solutionTypeKind,String solutionTypeName,String solutionTypeAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            String dataFactType=null;
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionDimensionTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionDimensionTypeFactType;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionFactTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionFactTypeFactType;
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)){
                if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionRelationTypeFactType)){
                    return false;
                }
                dataFactType=BUSINESSSOLUTION_SolutionRelationTypeFactType;
            }
            ExploreParameters solutionTypeRecordEP = new ExploreParameters();
            solutionTypeRecordEP.setType(dataFactType);
            solutionTypeRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DimensionTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_FactTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)) {
                solutionTypeRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_RelationTypeName, solutionTypeName), ExploreParameters.FilteringLogic.AND);
            }
            solutionTypeRecordEP.setResultNumber(100000);

            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> solutionTypeDefinitionRecordFactsList = ie.discoverFacts(solutionTypeRecordEP);
            for(Fact currentFact:solutionTypeDefinitionRecordFactsList){
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_DIMENSION.equals(solutionTypeKind)){
                    currentFact.updateProperty(MetaConfig_PropertyName_DimensionTypeAliasName,solutionTypeAliasName);
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_FACT.equals(solutionTypeKind)){
                    currentFact.updateProperty(MetaConfig_PropertyName_FactTypeAliasName,solutionTypeAliasName);
                }
                if(InfoDiscoverSpaceOperationUtil.TYPEKIND_RELATION.equals(solutionTypeKind)) {
                    currentFact.updateProperty(MetaConfig_PropertyName_RelationTypeAliasName,solutionTypeAliasName);
                }
            }
            return true;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static List<SolutionTypePropertyTypeDefinitionVO> getSolutionTypePropertiesInfo(String solutionName, String propertyTypeKind,String propertyTypeName){
        List<SolutionTypePropertyTypeDefinitionVO> solutionFactPropertyTypeDefinitionVOList=new ArrayList<>();
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){

                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);

                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionFactTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionFactTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionFactTypePropertyDefinitionRecordFactsList){
                        SolutionTypePropertyTypeDefinitionVO currentSolutionFactPropertyTypeDefinitionVO=new SolutionTypePropertyTypeDefinitionVO();
                        currentSolutionFactPropertyTypeDefinitionVO.setSolutionName(solutionName);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyTypeKind(propertyTypeKind);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyTypeName(propertyTypeName);
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyName(currentFact.getProperty(MetaConfig_PropertyName_PropertyName).getPropertyValue().toString());
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyAliasName(currentFact.getProperty(MetaConfig_PropertyName_PropertyAliasName).getPropertyValue().toString());
                        currentSolutionFactPropertyTypeDefinitionVO.setPropertyType(currentFact.getProperty(MetaConfig_PropertyName_PropertyType).getPropertyValue().toString());
                        Boolean isMandatory=(Boolean)currentFact.getProperty(MetaConfig_PropertyName_IsMandatoryProperty).getPropertyValue();
                        Boolean isNullable=(Boolean)currentFact.getProperty(MetaConfig_PropertyName_IsNullableProperty).getPropertyValue();
                        Boolean isReadOnly=(Boolean)currentFact.getProperty(MetaConfig_PropertyName_IsReadOnlyProperty).getPropertyValue();
                        currentSolutionFactPropertyTypeDefinitionVO.setMandatory(isMandatory.booleanValue());
                        currentSolutionFactPropertyTypeDefinitionVO.setNullable(isNullable.booleanValue());
                        currentSolutionFactPropertyTypeDefinitionVO.setReadOnly(isReadOnly.booleanValue());
                        if(currentFact.getProperty(MetaConfig_PropertyName_PropertySourceOwner)!=null) {
                            currentSolutionFactPropertyTypeDefinitionVO.setPropertySourceOwner(currentFact.getProperty(MetaConfig_PropertyName_PropertySourceOwner).getPropertyValue().toString());
                        }
                        solutionFactPropertyTypeDefinitionVOList.add(currentSolutionFactPropertyTypeDefinitionVO);
                    }
                }
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return solutionFactPropertyTypeDefinitionVOList;
    }

    public static boolean checkSolutionTypePropertyDefinitionExistence(String solutionName, String propertyTypeKind, String propertyTypeName, String propertyName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);

                if(solutionTypePropertyDefinitionRecordFactsList!=null&&solutionTypePropertyDefinitionRecordFactsList.size()>0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean createSolutionTypePropertyDefinition(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName,
                                                               String propertyAliasName,String propertyType,boolean isMandatory,boolean isReadOnly,boolean isNullable,String sourceOwner){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(!metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                FactType solutionTypePropertyFactType=metaConfigSpace.addFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                TypeProperty solutionNameProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_SolutionName, PropertyType.STRING);
                solutionNameProperty.setMandatory(true);
                TypeProperty propertyTypeKindProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertyTypeKind, PropertyType.STRING);
                propertyTypeKindProperty.setMandatory(true);
                TypeProperty propertyTypeNameProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertyTypeName, PropertyType.STRING);
                propertyTypeNameProperty.setMandatory(true);
                TypeProperty propertyNameProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertyName, PropertyType.STRING);
                propertyNameProperty.setMandatory(true);
                TypeProperty propertyAliasNameProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertyAliasName, PropertyType.STRING);
                propertyAliasNameProperty.setMandatory(true);
                TypeProperty propertyTypeProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertyType, PropertyType.STRING);
                propertyTypeProperty.setMandatory(true);
                TypeProperty propertySourceOwnerProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_PropertySourceOwner, PropertyType.STRING);
                propertySourceOwnerProperty.setMandatory(false);
                TypeProperty isMandatoryProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_IsMandatoryProperty, PropertyType.BOOLEAN);
                isMandatoryProperty.setMandatory(true);
                TypeProperty isNullableProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_IsNullableProperty, PropertyType.BOOLEAN);
                isNullableProperty.setMandatory(true);
                TypeProperty isReadOnlyProperty=solutionTypePropertyFactType.addTypeProperty(MetaConfig_PropertyName_IsReadOnlyProperty, PropertyType.BOOLEAN);
                isReadOnlyProperty.setMandatory(true);
            }
            Fact solutionTypePropertyFact=DiscoverEngineComponentFactory.createFact(BUSINESSSOLUTION_SolutionTypePropertyFactType);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_SolutionName,solutionName);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertyTypeKind,propertyTypeKind);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertyTypeName,propertyTypeName);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertyName,propertyName);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertyAliasName,propertyAliasName);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertyType,propertyType);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_IsMandatoryProperty,isMandatory);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_IsReadOnlyProperty,isReadOnly);
            solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_IsNullableProperty,isNullable);
            if(sourceOwner!=null) {
                solutionTypePropertyFact.setInitProperty(MetaConfig_PropertyName_PropertySourceOwner, sourceOwner);
            }
            Fact resultRecord=metaConfigSpace.addFact(solutionTypePropertyFact);
            if(resultRecord!=null){
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineDataMartException e) {
                e.printStackTrace();
            } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean deleteSolutionTypePropertyDefinition(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionTypePropertyDefinitionRecordFactsList){
                        metaConfigSpace.removeFact(currentFact.getId());
                    }
                }
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }

    public static boolean updateSolutionTypePropertyAliasName(String solutionName, String propertyTypeKind,String propertyTypeName, String propertyName,String propertyAliasName){
        String metaConfigSpaceName= AdminCenterPropertyHandler.getPropertyValue(AdminCenterPropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace=null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            if(metaConfigSpace.hasFactType(BUSINESSSOLUTION_SolutionTypePropertyFactType)){
                ExploreParameters solutionTypePropertyRecordEP = new ExploreParameters();
                solutionTypePropertyRecordEP.setType(BUSINESSSOLUTION_SolutionTypePropertyFactType);
                solutionTypePropertyRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_SolutionName, solutionName));
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeKind, propertyTypeKind), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyTypeName, propertyTypeName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_PropertyName, propertyName), ExploreParameters.FilteringLogic.AND);
                solutionTypePropertyRecordEP.setResultNumber(100000);
                InformationExplorer ie = metaConfigSpace.getInformationExplorer();
                List<Fact> solutionTypePropertyDefinitionRecordFactsList = ie.discoverFacts(solutionTypePropertyRecordEP);
                if(solutionTypePropertyDefinitionRecordFactsList!=null){
                    for(Fact currentFact:solutionTypePropertyDefinitionRecordFactsList){
                        currentFact.updateProperty(MetaConfig_PropertyName_PropertyAliasName,propertyAliasName);
                    }
                }
                return true;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return false;
    }
}

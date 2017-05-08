package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.FactTypeDefinitionVO;
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
    public static final String BUSINESSSOLUTION_SolutionTypePropertyFactType="BusinessSolution_SolutionTypePropertyType";

    public static final String MetaConfig_PropertyName_SolutionName="solutionName";
    public static final String MetaConfig_PropertyName_FactTypeName="factTypeName";
    public static final String MetaConfig_PropertyName_FactTypeAliasName="factTypeAliasName";

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
}

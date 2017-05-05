package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement;

import com.infoDiscover.adminCenter.logic.component.businessSolutionManagement.vo.FactTypeDefinitionVO;
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
    public static final String MetaConfig_PropertyName_SolutionName="solutionName";
    public static final String MetaConfig_PropertyName_FactTypeName="factTypeName";
    public static final String MetaConfig_PropertyName_FactTypeAliasName="factTypeAliasName";

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
                    return metaConfigSpace.removeFact(targetBusinessSolution.getId());
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

    //public retrieveFactTypePropertiesInfo()
}

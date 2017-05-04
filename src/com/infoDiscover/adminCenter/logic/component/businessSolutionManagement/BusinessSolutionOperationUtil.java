package com.infoDiscover.adminCenter.logic.component.businessSolutionManagement;

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
    public static final String MetaConfig_PropertyName_SolutionName="solutionName";

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
}

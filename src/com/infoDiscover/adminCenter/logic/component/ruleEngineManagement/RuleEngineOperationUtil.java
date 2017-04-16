package com.infoDiscover.adminCenter.logic.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.RuleEngineImpl;
import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.manager.database.RuleEngineFactManager;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingRuleEngineImpl;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun.
 */
public class RuleEngineOperationUtil {

    public static List<RuleVO> getExistingRulesList() {

        InfoDiscoverSpace infoDiscoverSpace = DataSpaceManager.getInfoDiscoverSpace();

        ExploreParameters ep = new ExploreParameters();
        ep.setType(RuleEngineDatabaseConstants.RuleFact);
        ep.setDefaultFilteringItem(new EqualFilteringItem(RuleEngineDatabaseConstants
                .FACT_DELETED, false));

        try {
            List<Fact> facts = executeFactQuery(infoDiscoverSpace, ep);
            List<RuleVO> ruleList = new ArrayList<>();
            for (Fact fact : facts) {
                ruleList.add(RuleEngineFactManager.getRuleVOFromFact(fact));
            }
            return ruleList;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            infoDiscoverSpace.closeSpace();
        }

        return null;
    }

    public static RuleVO getRuleById(String ruleId) {
        return new PropertyMappingRuleEngineImpl().getRule(ruleId);
    }

    public static boolean createRule(RuleVO rule) {
        try {
            new PropertyMappingRuleEngineImpl().createRule(rule);
            return true;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkRuleEngineDataSpaceExistence() {
        return new RuleEngineImpl().checkRuleEngineDataspaceExistence();
    }

    public static boolean checkRuleExistence(String ruleId) {
        return new PropertyMappingRuleEngineImpl().checkRuleExistence(ruleId);
    }

    public static boolean updateRule(RuleVO rule) {
        return new PropertyMappingRuleEngineImpl().updateRule(rule);
    }

    public static boolean hardDeleteRule(String ruleId) {
        return new PropertyMappingRuleEngineImpl().deleteRule(ruleId);
    }

    public static boolean softDeleteRule(String ruleId) {
        return new PropertyMappingRuleEngineImpl().deleteRule(ruleId, true);
    }

    public static List<Fact> executeFactQuery(InfoDiscoverSpace infoDiscoverSpace, ExploreParameters
            ep)
            throws
            InfoDiscoveryEngineRuntimeException, InfoDiscoveryEngineInfoExploreException {

        InformationExplorer ie = infoDiscoverSpace.getInformationExplorer();

        List<Fact> facts = ie.discoverFacts(ep);
        return facts;
    }


}

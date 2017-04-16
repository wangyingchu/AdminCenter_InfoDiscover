package com.infoDiscover.adminCenter.logic.component.ruleEngineManagement;

import com.info.discover.ruleengine.base.RuleEngineImpl;
import com.info.discover.ruleengine.base.vo.RuleVO;
import com.info.discover.ruleengine.manager.database.DataSpaceManager;
import com.info.discover.ruleengine.manager.database.RuleEngineDatabaseConstants;
import com.info.discover.ruleengine.manager.database.RuleEngineFactManager;
import com.info.discover.ruleengine.plugins.propertymapping.PropertyMappingRuleEngineImpl;
import com.info.discover.ruleengine.plugins.propertymapping.sample.SampleRuleConstants;
import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleContent;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;

import java.util.*;

/**
 * Created by sun.
 */
public class RuleEngineOperationUtil {

    public static List<RuleVO> getExistingRulesList() {

        InfoDiscoverSpace infoDiscoverSpace = DataSpaceManager.getRuleEngineInfoDiscoverSpace();

        ExploreParameters ep = new ExploreParameters();
        ep.setType(RuleEngineDatabaseConstants.RuleFact);
        ep.setDefaultFilteringItem(new EqualFilteringItem(RuleEngineDatabaseConstants
                .FACT_DELETED, false));

        try {
            List<Fact> facts = executeFactQuery(infoDiscoverSpace, ep);
            List<RuleVO> ruleList = new ArrayList<>();
            for (Fact fact : facts) {
                RuleVO rule = RuleEngineFactManager.getRuleVOFromFact(fact);
                if (rule.getRuleId().trim().equalsIgnoreCase(SampleRuleConstants.FACT_TYPE)) {
                    ruleList.add(0, rule);
                } else {
                    ruleList.add(rule);
                }
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

    public static boolean executeRule(String ruleId) {
        boolean result = false;

        PropertyMappingRuleEngineImpl engine = new PropertyMappingRuleEngineImpl();
        Map<String, List<Dimension>> results = new HashMap<String, List<Dimension>>();
        results = engine.executeRule(ruleId);
        Set<String> keySet = results.keySet();
        Iterator<String> it = keySet.iterator();

        RuleVO ruleVO = new RuleEngineImpl().getRule(ruleId);
        if (ruleVO != null){
            RuleContent contentVO = new RuleContent(ruleVO.getContent());
            if (contentVO != null) {
                InfoDiscoverSpace ids = DataSpaceManager.getInfoDiscoverSpace(contentVO.getSpaceName());
                while (it.hasNext()) {
                    String factRid = it.next();
                    List<Dimension> dimensionList = results.get(factRid);
                    try {
                        engine.linkFactToDimensionsByRelationType(ids, factRid,
                                dimensionList,
                                SampleRuleConstants.RELATION_TYPE);
                        result = true;
                    } catch (InfoDiscoveryEngineDataMartException e) {
                        e.printStackTrace();
                    } catch (InfoDiscoveryEngineRuntimeException e) {
                        e.printStackTrace();
                    }
                }
                ids.closeSpace();
            }
        }



        return result;
    }

}

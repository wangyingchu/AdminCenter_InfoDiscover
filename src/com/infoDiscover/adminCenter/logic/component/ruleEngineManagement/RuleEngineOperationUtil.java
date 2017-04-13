package com.infoDiscover.adminCenter.logic.component.ruleEngineManagement;

import com.infoDiscover.adminCenter.logic.component.ruleEngineManagement.vo.RuleVO;
import com.infoDiscover.common.util.JsonUtil;
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

    public static List<String> getExistingRulesList() {
        InfoDiscoverSpace infoDiscoverSpace = DiscoverEngineComponentFactory
                .connectInfoDiscoverSpace("RuleEngine");

        ExploreParameters ep = new ExploreParameters();
        ep.setType("Rule");

        try {
            List<Fact> facts = executeFactQuery(infoDiscoverSpace, ep);
            List<String> list = new ArrayList<>();
            for (Fact fact : facts) {
                list.add(fact.getProperty("name").getPropertyValue().toString());
            }
            return list;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            infoDiscoverSpace.closeSpace();
        }


        return null;
    }

    public static RuleVO getRuleByName(String ruleName) {
        InfoDiscoverSpace infoDiscoverSpace = DiscoverEngineComponentFactory
                .connectInfoDiscoverSpace("RuleEngine");

        ExploreParameters ep = new ExploreParameters();
        ep.setType("Rule");
        ep.setDefaultFilteringItem(new EqualFilteringItem("name", ruleName));

        try {
            List<Fact> factsList = executeFactQuery(infoDiscoverSpace, ep);
            if (factsList != null && factsList.size() > 0) {
                Fact fact = factsList.get(0);
                RuleVO rule = new RuleVO();
                rule.setType(fact.getProperty("type").getPropertyValue().toString());
                rule.setName(fact.getProperty("name").getPropertyValue().toString());
                rule.setDescription(fact.getProperty("description").getPropertyValue().toString());
                String content = fact.getProperty("content").getPropertyValue().toString();
                rule.setContent(content);
                rule.setSourceProperties(JsonUtil.getPropertyValues("sourceProperties",
                        content));
                rule.setTargetProperty(JsonUtil.getPropertyValues("targetProperty",
                        content));
                rule.setFactName(JsonUtil.getPropertyValues("sourceType", content));
                rule.setDimensionName(JsonUtil.getPropertyValues("targetType", content));
                return rule;
            }
            return null;
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } finally {
            infoDiscoverSpace.closeSpace();
        }
        return null;
    }

    private static String getSourceProperties(String content) {
       return JsonUtil.getPropertyValues("sourceProperties",
                content);
    }

    private static String getTargetProperty(String content) {
        return JsonUtil.getPropertyValues("targetProperty",
                content);
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

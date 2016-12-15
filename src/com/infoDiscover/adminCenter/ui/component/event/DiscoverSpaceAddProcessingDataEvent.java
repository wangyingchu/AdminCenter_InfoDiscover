package com.infoDiscover.adminCenter.ui.component.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.infoDiscover.adminCenter.logic.component.infoDiscoverSpaceManagement.vo.ProcessingDataVO;

/**
 * Created by wangychu on 12/15/16.
 */
public class DiscoverSpaceAddProcessingDataEvent implements Event {

    private ProcessingDataVO processingData;
    private String discoverSpaceName;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public ProcessingDataVO getProcessingData() {
        return processingData;
    }

    public void setProcessingData(ProcessingDataVO processingData) {
        this.processingData = processingData;
    }

    public DiscoverSpaceAddProcessingDataEvent(String discoverSpaceName){
        this.setDiscoverSpaceName(discoverSpaceName);
    }

    public interface DiscoverSpaceAddProcessingDataListener extends Listener {
        public void receivedDiscoverSpaceAddProcessingDataEvent(final DiscoverSpaceAddProcessingDataEvent event);
    }
}

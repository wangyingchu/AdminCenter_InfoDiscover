package com.infoDiscover.adminCenter.ui.component.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

/**
 * Created by wangychu on 12/15/16.
 */
public class OpenProcessingDataListEvent implements Event {

    private String discoverSpaceName;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public OpenProcessingDataListEvent(String discoverSpaceName){
        this.setDiscoverSpaceName(discoverSpaceName);
    }

    public interface OpenProcessingDataListListener extends Listener {
        public void receivedOpenProcessingDataListEvent(final OpenProcessingDataListEvent event);
    }
}

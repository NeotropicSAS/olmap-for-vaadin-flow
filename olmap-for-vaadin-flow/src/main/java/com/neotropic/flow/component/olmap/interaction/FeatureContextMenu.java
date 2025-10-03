/**
 * Copyright 2010-2022 Neotropic SAS <contact@neotropic.co>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.neotropic.flow.component.olmap.interaction;

import com.neotropic.flow.component.olmap.OlMap;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.shared.Registration;
import java.util.Objects;

/**
 * Feature context menu interaction dispatch by the map view port on context
 * menu.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class FeatureContextMenu {

    private final OlMap map;

    public FeatureContextMenu(OlMap map) {
        this.map = map;
    }

    public Registration addFeatureContextMenuListener(ComponentEventListener<FeatureContextMenuEvent> listener) {
        Objects.requireNonNull(map);
        return ComponentUtil.addListener(map, FeatureContextMenuEvent.class, listener);
    }

    /**
     * Feature context menu event dispatch by the map view port on context menu.
     */
    @DomEvent("map-feature-contextmenu")
    public static class FeatureContextMenuEvent extends ComponentEvent<OlMap> {

        private final String featureId;

        public FeatureContextMenuEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.featureId") String featureId) {
            super(source, fromClient);
            this.featureId = featureId;
        }

        public String getFeatureId() {
            return featureId;
        }
    }
}

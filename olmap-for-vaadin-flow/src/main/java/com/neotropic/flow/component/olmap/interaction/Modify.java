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

import com.neotropic.flow.component.olmap.InteractionType;
import com.neotropic.flow.component.olmap.OlMap;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;
import java.util.Objects;

/**
 * Interaction for modifying features geometries.
 * 
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Modify extends Interaction {
    private final OlMap map;
    
    public Modify(OlMap map) {
        super(InteractionType.Modify);
        Objects.requireNonNull(map);
        this.map = map;
    }
    
    public Registration addModifyEndListener(ComponentEventListener<ModifyEndEvent> listener) {
        return ComponentUtil.addListener(this.map, ModifyEndEvent.class, listener);
    }

    @Override
    public JsonObject getOptions() {
        return null;
    }
    
    @DomEvent("map-modify-modify-end")
    public static class ModifyEndEvent extends ComponentEvent<OlMap> {
        private final JsonObject features;

        public ModifyEndEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.features") JsonObject features) {
            super(source, fromClient);
            this.features = features;
        }
        
        public JsonObject getFeatures() {
            return features;
        }
    }
}

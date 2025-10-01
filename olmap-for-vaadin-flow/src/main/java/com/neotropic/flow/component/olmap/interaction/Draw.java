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

import com.neotropic.flow.component.olmap.GeometryType;
import com.neotropic.flow.component.olmap.InteractionType;
import com.neotropic.flow.component.olmap.OlMap;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonObject;

/**
 * Interaction for drawing feature geometries.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Draw extends Interaction {

    private final GeometryType type;
    private final OlMap map;

    public Draw(GeometryType type, OlMap map) {
        super(InteractionType.Draw);
        this.type = type;
        this.map = map;
    }

    public GeometryType getType() {
        return type;
    }

    @Override
    public JsonObject getOptions() {
        JsonObject options = Json.createObject();
        options.put("type", type.toString());
        return options;
    }

    public Registration addDrawEndListener(ComponentEventListener<DrawEndEvent> listener) {
        return ComponentUtil.addListener(map, DrawEndEvent.class, listener);
    }

    @DomEvent("map-draw-draw-end")
    public static class DrawEndEvent extends ComponentEvent<OlMap> {

        private final JsonObject feature;

        public DrawEndEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.feature") JsonObject feature) {
            super(source, fromClient);
            this.feature = feature;
        }

        public JsonObject getFeature() {
            return feature;
        }
    }
}

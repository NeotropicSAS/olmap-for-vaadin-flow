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
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Interaction for selecting vector features.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Select extends Interaction {

    private final OlMap map;

    public Select(OlMap map) {
        super(InteractionType.Select);
        this.map = map;
    }

    public Registration addSelectListener(ComponentEventListener<SelectEvent> listener) {
        Objects.requireNonNull(map);
        return ComponentUtil.addListener(this.map, SelectEvent.class, listener);
    }

    @Override
    public JsonObject getOptions() {
        return null;
    }

    @DomEvent("map-select-select")
    public static class SelectEvent extends ComponentEvent<OlMap> {

        private final List<String> featureDeselectedIds = new ArrayList();
        private final List<String> featureSelectedIds = new ArrayList();

        public SelectEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.deselectedIds") JsonArray deselectedIds,
                @EventData("event.detail.selectedIds") JsonArray selectedIds) {
            super(source, fromClient);
            for (int i = 0; i < deselectedIds.length(); i++) {
                if (deselectedIds.get(i).getType() == JsonType.STRING) {
                    featureDeselectedIds.add(deselectedIds.getString(i));
                }
            }
            for (int i = 0; i < selectedIds.length(); i++) {
                if (selectedIds.get(i).getType() == JsonType.STRING) {
                    featureSelectedIds.add(selectedIds.getString(i));
                }
            }
        }

        public List<String> getFeaturesDeselectedIds() {
            return featureDeselectedIds;
        }

        public List<String> getFeatureSelectedIds() {
            return featureSelectedIds;
        }
    }
}

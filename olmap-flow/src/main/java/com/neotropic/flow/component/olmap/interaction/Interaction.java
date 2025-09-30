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
import com.neotropic.flow.component.olmap.OlMapType;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.UUID;

/**
 * User actions that change the state of the map.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public abstract class Interaction implements OlMapType<JsonObject> {

    private final String id = UUID.randomUUID().toString();
    private final InteractionType interactionType;
    private boolean active = true;

    public Interaction(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract JsonObject getOptions();

    @Override
    public JsonObject toJsonValue() {
        JsonObject interaction = Json.createObject();
        interaction.put("id", id);
        interaction.put("type", interactionType.toString());
        if (getOptions() != null) {
            interaction.put("options", getOptions());
        }
        interaction.put("active", active);
        return interaction;
    }
}

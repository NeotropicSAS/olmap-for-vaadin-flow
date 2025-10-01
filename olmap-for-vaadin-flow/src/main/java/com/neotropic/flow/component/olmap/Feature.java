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
package com.neotropic.flow.component.olmap;

import elemental.json.Json;
import elemental.json.JsonObject;

/**
 * A feature represents a spatially bounded thing.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Feature implements OlMapType<JsonObject> {

    /**
     * Feature identifier.
     */
    private String id;
    /**
     * Geometry object.
     */
    private Geometry geometry;
    /**
     * Properties object.
     */
    private Properties properties;

    public Feature() {
    }

    public Feature(String id, Geometry geometry) {
        this.id = id;
        this.geometry = geometry;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final Geometry getGeometry() {
        return geometry;
    }

    public final void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public final void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public JsonObject toJsonValue() {
        JsonObject feature = Json.createObject();
        if (id != null) {
            feature.put("id", id);
        }
        if (geometry != null) {
            feature.put("geometry", geometry.toJsonValue());
        }
        if (properties != null) {
            feature.put("properties", properties.toJsonValue());
        }
        return feature;
    }

}

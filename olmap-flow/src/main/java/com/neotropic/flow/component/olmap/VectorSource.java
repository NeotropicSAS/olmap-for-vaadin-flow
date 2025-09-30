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

import com.vaadin.flow.component.page.PendingJavaScriptResult;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Provides a source of features for vector layers.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class VectorSource {

    private OlMap map;
    private final HashMap<String, Feature> features = new HashMap();

    public VectorSource() {
    }

    public PendingJavaScriptResult addFeature(Feature feature) {
        features.put(feature.getId(), feature);
        return map.getElement().callJsFunction("addFeature", feature.toJsonValue());
    }

    public void updateFeature(Feature feature) {
        map.getElement().callJsFunction("updateFeature", feature.toJsonValue());
    }

    public void removeFeature(Feature feature) {
        features.remove(feature.getId());
        map.getElement().callJsFunction("removeFeature", feature.toJsonValue());
    }

    public void animateFeature(Feature feature) {
        features.remove(feature.getId());
        map.getElement().callJsFunction("animateFeature", feature.toJsonValue());
    }

    public OlMap getMap() {
        return map;
    }

    public void setMap(OlMap map) {
        this.map = map;
    }

    public List<Feature> getFeatures() {
        return Arrays.asList(features.values().toArray(new Feature[0]));
    }

    public Feature getFeatureById(String id) {
        return features.get(id);
    }
}

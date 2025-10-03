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
import elemental.json.JsonArray;
import java.util.List;

/**
 * Array of positions of a LineString
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class LineStringCoordinates implements Coordinates {
    /**
     * Array de positions.
     */
    private List<PointCoordinates> coordinates;

    public LineStringCoordinates(List<PointCoordinates> coordinates) {
        this.coordinates = coordinates;
    }

    public List<PointCoordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<PointCoordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public JsonArray toJsonValue() {
        JsonArray lineStringCoordinates = Json.createArray();
        for (int i = 0; i < coordinates.size(); i++) {
            lineStringCoordinates.set(i, coordinates.get(i).toJsonValue());
        }
        return lineStringCoordinates;
    }

}

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

/**
 * Position of a Point geometry.
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class PointCoordinates implements Coordinates {
    /**
     * Point in x.
     */
    private double x;
    /**
     * Point in y.
     */
    private double y;

    public PointCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public JsonArray toJsonValue() {
        JsonArray coordinates = Json.createArray();
        coordinates.set(0, x);
        coordinates.set(1, y);
        return coordinates;
    }
}

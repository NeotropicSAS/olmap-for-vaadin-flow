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
package com.neotropic.flow.component.olmap.style;

import com.neotropic.flow.component.olmap.OlMapType;
import elemental.json.Json;
import elemental.json.JsonObject;

/**
 * Wrap text style for vector features.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Text implements OlMapType<JsonObject> {

    /**
     * Font style as CSS 'font' value. Default is '10px sans-serif'
     */
    private String font;
    /**
     * Text content.
     */
    private String text;
    /**
     * Fill style.
     */
    private Fill fill;
    /**
     * Fill style for the text background.
     */
    private Fill backgroundFill;
    /**
     * The minimum view zoom level above which this text will be visible.
     */
    private Double minZoom;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Fill getFill() {
        return fill;
    }

    public void setFill(Fill fill) {
        this.fill = fill;
    }

    public Fill getBackgroundFill() {
        return backgroundFill;
    }

    public void setBackgroundFill(Fill backgroundFill) {
        this.backgroundFill = backgroundFill;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Double getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(Double minZoom) {
        this.minZoom = minZoom;
    }
    
    @Override
    public JsonObject toJsonValue() {
        JsonObject text = Json.createObject();
        if (getText() != null) {
            text.put("text", getText());
        }
        if (getFill() != null) {
            text.put("fill", getFill().toJsonValue());
        }
        if (getBackgroundFill() != null) {
            text.put("backgroundFill", getBackgroundFill().toJsonValue());
        }
        if (getFont() != null) {
            text.put("font", getFont());
        }
        if (getMinZoom() != null) {
            text.put("minZoom", getMinZoom());
        }
        return text;
    }

}

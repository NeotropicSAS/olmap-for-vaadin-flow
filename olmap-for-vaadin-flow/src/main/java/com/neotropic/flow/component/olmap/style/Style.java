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
 * Wrap vector feature rendering style.
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class Style implements OlMapType<JsonObject> {

    /**
     * Image style.
     */
    private ImageStyle image;
    /**
     * Stroke style.
     */
    private Stroke stroke;
    /**
     * Text style.
     */
    private Text text;

    public ImageStyle getImage() {
        return image;
    }

    public void setImage(ImageStyle image) {
        this.image = image;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public JsonObject toJsonValue() {
        JsonObject style = Json.createObject();
        if (image != null) {
            style.put("image", image.toJsonValue());
        }
        if (stroke != null) {
            style.put("stroke", stroke.toJsonValue());
        }
        if (text != null) {
            style.put("text", text.toJsonValue());
        }
        return style;
    }
}

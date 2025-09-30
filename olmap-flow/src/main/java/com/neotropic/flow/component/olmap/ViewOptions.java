/**
 * Copyright 2010-2022 Neotropic SAS <contact@neotropic.co>.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neotropic.flow.component.olmap;
import com.neotropic.flow.component.olmap.OptionType.ViewOptionType;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import java.util.Arrays;
import java.util.Objects;

/**
 * Wrapper to OpenLayers View object options
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
public class ViewOptions implements OlMapTypeLiteral<ViewOptionType> {

    /**
     * The initial center for the view.
     */
    private Coordinate center;
    /**
     * Zoom level used to calculate the initial resolution for the view
     */
    private double zoom;
    
    private OlMap map;
    
    public ViewOptions(Coordinate center, double zoom) {
        this.center = center;
        this.zoom = zoom;
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
        if (map != null)
            map.getElement().setPropertyJson(OlMap.Property.VIEW_OPTIONS.getProperty(), toJsonValue(ViewOptionType.CENTER));
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        if (map != null)
            map.getElement().setPropertyJson(OlMap.Property.VIEW_OPTIONS.getProperty(), toJsonValue(ViewOptionType.ZOOM));
    }
    
    public OlMap getMap() {
        return map;
    }
    
    public void setMap(OlMap map) {
        this.map = map;
        if (this.map != null) {
            this.map.addMapMoveendListener(event -> {
                this.zoom = event.getZoom();
                this.center = event.getCenter();
            });
        }
    }
    
    @Override
    public JsonValue toJsonValue() {
        return toJsonValue(ViewOptionType.CENTER, ViewOptionType.ZOOM);
    }
    
    @Override
    public JsonValue toJsonValue(ViewOptionType... options) {
        JsonObject viewOptionsObject = Json.createObject();
        Arrays.asList(options).forEach(option -> {
            if (ViewOptionType.CENTER == option)
                viewOptionsObject.put(ViewOptionType.CENTER.getOption(), this.center.toJsonValue());
            else if (ViewOptionType.ZOOM == option)
                viewOptionsObject.put(ViewOptionType.ZOOM.getOption(), this.zoom);
        });
        return viewOptionsObject;
    }
    
    public Registration addViewChangeResolutionListener(ComponentEventListener<ViewChangeResolutionEvent> listener) {
        Objects.requireNonNull(this.map);
        return ComponentUtil.addListener(this.map, ViewChangeResolutionEvent.class, listener);
    }
    
    @DomEvent("view-change:resolution")
    public static class ViewChangeResolutionEvent extends ComponentEvent<OlMap> {
        private final double zoom;

        public ViewChangeResolutionEvent(OlMap source, boolean fromClient, 
            @EventData("event.detail.view.zoom") double zoom) {
            super(source, fromClient);
            this.zoom = zoom;
        }
        
        public double getZoom() {
            return zoom;
        }
    }
}

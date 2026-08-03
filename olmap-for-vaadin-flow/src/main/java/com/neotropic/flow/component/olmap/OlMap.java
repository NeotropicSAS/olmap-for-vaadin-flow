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

import com.neotropic.flow.component.olmap.interaction.Interaction;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main add-on Component class which wrap OpenLayers
 *
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
@Tag("ol-map")
@NpmPackage(value = "ol", version = "^10.6.1")
@CssImport("ol/ol.css")
@JsModule("./ol-map.ts")
public class OlMap extends Component implements HasSize {

    public enum Property {
        VIEW_OPTIONS("viewOptions"), //NOI18N
        TILE_LAYER_SOURCE("tileLayerSource"), //NOI18N
        MEASURING("measuring"); //NOI18N
        
        private final String property;

        private Property(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
    }
    /**
     * ol-map internal property
     */
    private final ViewOptions viewOptions;
    private final AbstractTileLayerSource tileLayerSource;

    private final List<VectorLayer> layers = new ArrayList();
    private final List<Interaction> interactions = new ArrayList();

    public OlMap(AbstractTileLayerSource tileLayerSource, ViewOptions viewOptions) {
        this.viewOptions = viewOptions;
        this.tileLayerSource = tileLayerSource;
        setSizeFull();
        addLoadCompleteListener(e -> {
            getElement().setPropertyJson(Property.VIEW_OPTIONS.getProperty(), this.viewOptions.toJsonValue());
            if (this.tileLayerSource != null)
                getElement().setPropertyJson(Property.TILE_LAYER_SOURCE.getProperty(), this.tileLayerSource.toJsonValue());
            this.viewOptions.setMap(this);
        });
    }
    
    public void setMeasuring(boolean measuring) {
        getElement().setProperty(Property.MEASURING.getProperty(), measuring);
    }
    
    public List<VectorLayer> getLayers() {
        return layers;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public ViewOptions getViewOptions() {
        return viewOptions;
    }

    public void addInteraction(Interaction interaction) {
        interactions.add(interaction);
        getElement().callJsFunction("addInteraction", interaction.toJsonValue());
    }

    public void updateInteraction(Interaction interaction) {
        getElement().callJsFunction("updateInteraction", interaction.toJsonValue());
    }

    public void removeInteraction(Interaction interaction) {
        interactions.remove(interaction);
        getElement().callJsFunction("removeInteraction", interaction.toJsonValue());
    }

    public Registration addLoadCompleteListener(ComponentEventListener<LoadCompleteEvent> listener) {
        return addListener(LoadCompleteEvent.class, listener);
    }

    public Registration addMapMoveendListener(ComponentEventListener<MapMoveEndEvent> listener) {
        return addListener(MapMoveEndEvent.class, listener);
    }

    public Registration addMapPointerMoveListener(ComponentEventListener<MapPointerMoveEvent> listener) {
        return addListener(MapPointerMoveEvent.class, listener);
    }

    public Registration addMapSingleClickListener(ComponentEventListener<MapSingleClickEvent> listener) {
        return addListener(MapSingleClickEvent.class, listener);
    }

    public Registration addMapViewportContextMenu(ComponentEventListener<MapViewportContextMenuEvent> listener) {
        return addListener(MapViewportContextMenuEvent.class, listener);
    }

    @DomEvent("load-complete")
    public static class LoadCompleteEvent extends ComponentEvent<OlMap> {

        public LoadCompleteEvent(OlMap source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    @DomEvent("map-moveend")
    public static class MapMoveEndEvent extends ComponentEvent<OlMap> {

        private final double zoom;
        private final Coordinate center;

        public MapMoveEndEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.view.zoom") double zoom,
                @EventData("event.detail.view.center") JsonArray center) {
            super(source, fromClient);
            this.zoom = zoom;
                this.center = new Coordinate(center.getNumber(0), center.getNumber(1));
        }

        public double getZoom() {
            return zoom;
        }

        public Coordinate getCenter() {
            return center;
        }
    }

    @DomEvent("map-pointermove")
    public static class MapPointerMoveEvent extends ComponentEvent<OlMap> {

        private final Coordinate coordinate;

        public MapPointerMoveEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.coordinate") JsonArray coordinate) {
            super(source, fromClient);
            this.coordinate = new Coordinate(coordinate.getNumber(0), coordinate.getNumber(1));
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }
    }

    @DomEvent("map-singleclick")
    public static class MapSingleClickEvent extends ComponentEvent<OlMap> {

        private final Coordinate coordinate;

        public MapSingleClickEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.coordinate") JsonArray coordinate) {
            super(source, fromClient);
            this.coordinate = new Coordinate(coordinate.getNumber(0), coordinate.getNumber(1));
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }
    }

    @DomEvent("map-viewport-contextmenu")
    public static class MapViewportContextMenuEvent extends ComponentEvent<OlMap> {

        private final Coordinate coordinate;

        public MapViewportContextMenuEvent(OlMap source, boolean fromClient,
                @EventData("event.detail.coordinate") JsonArray coordinate) {
            super(source, fromClient);
            this.coordinate = new Coordinate(coordinate.getNumber(0), coordinate.getNumber(1));
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }
    }
    
    public void requestVisibleAreaAsync(java.util.function.Consumer<JsonObject> callback) {
        getElement().callJsFunction("getVisibleAreaInfo").then(json -> {
            if (json instanceof JsonObject) {
                JsonObject info = (JsonObject) json;
                callback.accept((JsonObject) json);
            }
        });
    }
    
    public void onViewOptionsUpdated() {
        if (viewOptions != null) {
            getElement().setPropertyJson(Property.VIEW_OPTIONS.getProperty(), viewOptions.toJsonValue());
        }
    }
    
    
    public void addFeatures(JsonArray features) {
        getElement().callJsFunction("addFeatures", features);
    }

    public void clearFeatures() {
        getElement().callJsFunction("clearFeatures");
    }
    
    public void dispose() {
        getElement().callJsFunction("disposeMap");
    }




 
}

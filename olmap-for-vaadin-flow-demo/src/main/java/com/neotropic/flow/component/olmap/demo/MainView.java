package com.neotropic.flow.component.olmap.demo;

import com.neotropic.flow.component.olmap.Coordinate;
import com.neotropic.flow.component.olmap.Feature;
import com.neotropic.flow.component.olmap.GeometryType;
import com.neotropic.flow.component.olmap.OlMap;
import com.neotropic.flow.component.olmap.Point;
import com.neotropic.flow.component.olmap.PointCoordinates;
import com.neotropic.flow.component.olmap.Properties;
import com.neotropic.flow.component.olmap.TileLayerSourceOsm;
import com.neotropic.flow.component.olmap.VectorLayer;
import com.neotropic.flow.component.olmap.VectorSource;
import com.neotropic.flow.component.olmap.ViewOptions;
import com.neotropic.flow.component.olmap.interaction.Draw;
import com.neotropic.flow.component.olmap.interaction.Modify;
import com.neotropic.flow.component.olmap.style.Fill;
import com.neotropic.flow.component.olmap.style.Icon;
import com.neotropic.flow.component.olmap.style.Style;
import com.neotropic.flow.component.olmap.style.Text;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Julian David Camacho Erazo {@literal <julian.camacho@kuwaiba.org>}
 */
@Route
public class MainView extends Div {
    /* 
    * List of coordinates to center the map
    */
    private List<Coordinate> coordinates = new ArrayList<>();
    /*
    * current position in the coordinate list
    */
    private int currentPos = 0;

    public MainView() {}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Coordinates where the map can be centered
        // These coordinates are defined in EPSG:4326 format (latitude, longitude), which is the standard geographic coordinate system used by GPS.
        coordinates.add(new Coordinate(-74.297333, 4.570868));  //Colombia
        coordinates.add(new Coordinate(135.9075, 35.120833));  //Japan
        coordinates.add(new Coordinate(19.1343786, 51.9189046)); //Poland
        coordinates.add(new Coordinate(9.5155848, 55.9396761)); //Danmark

        //Set the center in (0,0) and the zoom (6.0)
        ViewOptions viewOptions = new ViewOptions(new Coordinate(0, 0), 6.0);
        //Create the map
        OlMap myMap = new OlMap(new TileLayerSourceOsm(), viewOptions);

        VectorLayer vectorLayer = new VectorLayer(myMap);
        VectorSource vectorSource = new VectorSource();
        
        vectorLayer.setSource(vectorSource);
        myMap.getLayers().add(vectorLayer);

        
        //When the map is ready, the nodes are drawn
        myMap.addLoadCompleteListener(event -> {
            event.unregisterListener();

            //Add the interactions
            
            //Interaction for modifying features geometries.
            Modify modify = new Modify(myMap);
            myMap.addInteraction(modify);
            
            //Interaction for drawing feature geometries.
            Draw drawPoint = new Draw(GeometryType.Point, myMap);
            drawPoint.setActive(false);
            myMap.addInteraction(drawPoint);

            //Create dataset example
            List<Feature> features = createFeatures();
            
            features.forEach(feature -> {
                //Draws the nodes
                vectorSource.addFeature(feature);
            });
        });

        Button btnCenter = new Button("Set new center");
        btnCenter.addClickListener(listener -> {
            Coordinate coordinate = coordinates.get(currentPos);
            currentPos++;
            if(currentPos > (this.coordinates.size() - 1))
                currentPos = 0;

            //Set the new center
            myMap.getViewOptions().setCenter(new Coordinate(coordinate.getX(), coordinate.getY()));
        });

        add(btnCenter, myMap);
        setSizeFull();
    }
    
    /**
    * Converts geographic coordinates from EPSG:4326 (latitude, longitude)
    * to EPSG:3857 (Web Mercator projection), which is the coordinate system
    * used by OpenLayers maps.
    *
    * @param coord The coordinate in EPSG:4326 format (latitude, longitude)
    * @return The converted coordinate in EPSG:3857 format (x, y)
    */
    private Coordinate to3857(Coordinate coord) {
        double lon = coord.getY();
        double lat = coord.getX();
        double x = lon * 20037508.34 / 180.0;
        double y = Math.log(Math.tan(Math.PI / 4.0 + (lat * Math.PI / 180.0) / 2.0)) * 20037508.34 / Math.PI;
        return new Coordinate(x, y);
    }

    /**
    * Generates a list of `Feature` objects with coordinates, names, and custom styles.
    *
    * This method creates a set of geographic features (`Feature`) that represent 
    * points on a map. 
    * 
    * @return A list of Feature.
    */
    private List<Feature> createFeatures(){
        List<Feature> features = new ArrayList<>();
        for(int i = 0 ; i < coordinates.size() ; i++){
            Feature feature = new Feature();
            feature.setId(UUID.randomUUID().toString());
            //node position
            Coordinate coord = coordinates.get(i);
            feature.setGeometry(new Point(new PointCoordinates(coord.getX(), coord.getY())));
            String nodeName = "Node " + (i + 1);

            //node properties
            Properties properties = new Properties() {
                @Override
                public JsonObject toJsonValue() {
                    JsonObject properties = Json.createObject();
                    Style style = new Style();

                    Icon image = new Icon();
                    image.setSrc("icons/location-pin.png");
                    style.setImage(image);

                    Text text = new Text();
                    text.setFont(String.format("12px sans-serif"));
                    text.setMinZoom(12.0);
                    text.setText(nodeName);

                    Fill fill = new Fill();
                    fill.setColor("white");
                    text.setFill(fill);

                    Fill backgroundFill = new Fill();
                    backgroundFill.setColor("gray");
                    text.setBackgroundFill(backgroundFill);

                    style.setText(text);

                    properties.put("style", style.toJsonValue());

                    backgroundFill.setColor("red");
                    properties.put("selectedStyle", style.toJsonValue());
                    return properties;
                }
            };

            feature.setProperties(properties);
            features.add(feature);
        }

        return features;
    }
}
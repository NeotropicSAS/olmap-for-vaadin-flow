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
        //Coordinates where the map can be centered
        coordinates.add(new Coordinate(4.570868, -74.297333));  //Colombia
        coordinates.add(new Coordinate(35.120833, 135.9075));  //Japan
        coordinates.add(new Coordinate(51.9189046, 19.1343786)); //Poland
        coordinates.add(new Coordinate(55.9396761, 9.5155848)); //Iceland
                
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
            myMap.getViewOptions().setCenter(new Coordinate(coordinate.getY(), coordinate.getX()));
        });
        
        add(btnCenter, myMap);
        setSizeFull();
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
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        List<String> names = new ArrayList<>();
        
        createDataSet(xCoordinates, yCoordinates, names);
        
        for(int i = 0 ; i < names.size() ; i++){
            Feature feature = new Feature();
            feature.setId(UUID.randomUUID().toString());
            //node position
            feature.setGeometry(new Point(new PointCoordinates(xCoordinates.get(i),yCoordinates.get(i))));
            String nodeName = names.get(i);
            
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

    /**
    * Populates the provided lists with predefined geographic coordinates and names.
    *
    * @param xCoordinates List to store latitude values.
    * @param yCoordinates List to store longitude values.
    * @param names List to store unique location identifiers.
    */
    private void createDataSet(List<Double> xCoordinates, List<Double> yCoordinates, List<String> names) {
        yCoordinates.add(4.270673388430168);
        yCoordinates.add(4.288593228897156);
        yCoordinates.add(4.23931266155472);
        yCoordinates.add(4.019752468879389);
        yCoordinates.add(3.9883818243436338);
        yCoordinates.add(4.006308054182497);
        yCoordinates.add(3.844958054267707);
        yCoordinates.add(3.7418572612339602);
        yCoordinates.add(3.589425262240951);
        yCoordinates.add(3.652194522094433);
        yCoordinates.add(3.6208104360979547);
        yCoordinates.add(3.477326617334043);
        yCoordinates.add(4.064565579311591);
        yCoordinates.add(3.7866852035761127);
        yCoordinates.add(4.463289381757008);
        yCoordinates.add(4.579730490268119);
        yCoordinates.add(4.4274576094226035);
        yCoordinates.add(4.275153387852939);
        yCoordinates.add(3.939082681147127);
        yCoordinates.add(3.710476335081225);
        yCoordinates.add(4.194509418202202);
        yCoordinates.add(4.360268370526995);
        yCoordinates.add(4.5752523334868584);
        yCoordinates.add(4.7051073982136415);
        yCoordinates.add(3.3831534376067367);
        yCoordinates.add(3.3248511528051523);
        yCoordinates.add(3.387638084254661);
        yCoordinates.add(3.5176836872901163);
        yCoordinates.add(3.894262742488735);
        yCoordinates.add(4.431936676140083);

        xCoordinates.add(-73.33545726349074);
        xCoordinates.add(-73.01199824036848);
        xCoordinates.add(-73.69485617807103);
        xCoordinates.add(-73.41182953283905);
        xCoordinates.add(-73.67688623234201);
        xCoordinates.add(-72.94910343031694);
        xCoordinates.add(-73.25459250771017);
        xCoordinates.add(-73.67688623234201);
        xCoordinates.add(-73.357919695652);
        xCoordinates.add(-72.70201667654298);
        xCoordinates.add(-73.038953158962);
        xCoordinates.add(-73.8431082303354);
        xCoordinates.add(-72.68853921724623);
        xCoordinates.add(-72.44594494990451);
        xCoordinates.add(-73.36241218208426);
        xCoordinates.add(-73.02996818609749);
        xCoordinates.add(-73.80716833887736);
        xCoordinates.add(-74.1351198484319);
        xCoordinates.add(-74.19801465848344);
        xCoordinates.add(-74.13961233486415);
        xCoordinates.add(-72.36508019412395);
        xCoordinates.add(-72.79635889162032);
        xCoordinates.add(-72.37406516698846);
        xCoordinates.add(-72.79186640518806);
        xCoordinates.add(-73.42081450570356);
        xCoordinates.add(-73.01199824036848);
        xCoordinates.add(-72.66607678508495);
        xCoordinates.add(-72.15393333180805);
        xCoordinates.add(-72.08205354889199);
        xCoordinates.add(-71.75410203933747);

        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
    }
    
}


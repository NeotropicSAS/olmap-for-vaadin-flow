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
package com.neotropic.flow.component.olmap.demo;

import com.neotropic.flow.component.olmap.Coordinate;
import com.neotropic.flow.component.olmap.Feature;
import com.neotropic.flow.component.olmap.GeometryType;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.neotropic.flow.component.olmap.OlMap;
import com.neotropic.flow.component.olmap.Point;
import com.neotropic.flow.component.olmap.PointCoordinates;
import com.neotropic.flow.component.olmap.Properties;
import com.neotropic.flow.component.olmap.TileLayerSourceOsm;
import com.neotropic.flow.component.olmap.VectorLayer;
import com.neotropic.flow.component.olmap.VectorSource;
import com.neotropic.flow.component.olmap.ViewOptions;
import com.neotropic.flow.component.olmap.interaction.Draw;
import com.neotropic.flow.component.olmap.interaction.FeatureContextMenu;
import com.neotropic.flow.component.olmap.interaction.Modify;
import com.neotropic.flow.component.olmap.interaction.Select;
import com.neotropic.flow.component.olmap.style.Fill;
import com.neotropic.flow.component.olmap.style.Icon;
import com.neotropic.flow.component.olmap.style.Style;
import com.neotropic.flow.component.olmap.style.Text;
import com.vaadin.flow.component.AttachEvent;
import elemental.json.Json;
import elemental.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * A sample OpenLayers component.
 * @author Johny Andres Ortega Ruiz {@literal <johny.ortega@kuwaiba.org>}
 */
@Route
//@CssImport("./styles/shared-styles.css")
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends Div {
  
    
    public MainView() {     
        
      
        
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        
        super.onAttach(attachEvent);
        
        
        ViewOptions viewOptions = new ViewOptions(new Coordinate(-73.19619018409087, 3.52889367525286), 8.290123234329437);
        OlMap myMap = new OlMap(new TileLayerSourceOsm(), viewOptions);
        
        VectorLayer vl = new VectorLayer(myMap);
        VectorSource vs = new VectorSource();
        
        vl.setSource(vs);
        myMap.getLayers().add(vl);
        
        myMap.addLoadCompleteListener(event -> {
            Modify modify = new Modify(myMap);
            
            myMap.addInteraction(modify);
            
            Draw drawPoint = new Draw(GeometryType.Point, myMap);
            drawPoint.setActive(false);
            myMap.addInteraction(drawPoint);
            
        });

        myMap.addMapMoveendListener(event -> {
            event.unregisterListener();

            List<Feature> features = createFeatures(40);
            
            features.forEach(feature -> {
                vs.addFeature(feature);
                //vs.updateFeature(feature);
            });
                
        });
        
        Select select = new Select(myMap);
        FeatureContextMenu featureContextMenu = new FeatureContextMenu(myMap);
        
        add(myMap);
        
        //add(new OlMap(new TileLayerSourceOsm(), viewOptions));
        setSizeFull();
    }
    
    
    private List<Feature> createFeatures(int n){
        
        List<Feature> features = new ArrayList<>();
        
        List<Double> yCoordinates = new ArrayList<>();
        List<Double> xCoordinates = new ArrayList<>();
        List<String> names = new ArrayList<>();

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

        names.add("5c6ffb56-91c6-4817-b819-9fcc1c5c9dc0");
        names.add("89c4f9c6-8d1d-420b-b6ff-8b609063e8dc");
        names.add("4c8eedb7-f4f7-4cf7-92f9-e83cea297138");
        names.add("ddd4ea3e-7891-4b48-bc26-86f449b1d630");
        names.add("ba0570fc-997a-4095-98eb-cdf354fb12c7");
        names.add("c97889f4-eb98-44c4-8a81-8289677ad7ad");
        names.add("3652cb39-77b3-4252-afef-bbfd4bc9ef44");
        names.add("7dc2b9b1-d855-4c1a-8cb9-03e23e0475c4");
        names.add("6b9a9c30-d005-48f8-883c-018c8cb1c60c");
        names.add("d6eda2a7-73c3-4f39-9487-072bf513ab90");
        names.add("495f844f-5425-4176-82ef-0b25814d9580");
        names.add("4f7cf6f7-ad24-4df7-979a-79b6fab3e47c");
        names.add("b8c3ad57-0f89-4863-a467-f2392c85add6");
        names.add("cd333bb8-bae7-4579-a32b-e7e90de225de");
        names.add("80b7ed48-297b-491a-9192-98a30463a75b");
        names.add("d3f14d5e-f266-4481-8fe8-ccc8ade3fee2");
        names.add("46eecf91-0c2c-42a0-9b74-7f21db101163");
        names.add("6936f0dc-a2ed-4264-ad7b-62878338660e");
        names.add("dbdee09c-452c-449c-8e12-049135947fd7");
        names.add("c9f21780-43ee-4504-a668-e7f3b34c8298");
        names.add("36231bf3-c624-438d-bde2-c914353f810b");
        names.add("bb1756fd-7e8a-455c-b74b-1d83cc3f6f8e");
        names.add("a79d4af6-0aa5-45b5-8567-e48d890c1753");
        names.add("e9c54f5a-3fc9-4ff9-8077-eac963c891e4");
        names.add("d0fe0b56-5402-4b67-8558-6937b9fb6ed4");
        names.add("478d2971-af84-4f3f-9bbf-6f2919246251");
        names.add("98e74508-ee67-4ff0-888c-a82287f69d08");
        names.add("51bc670a-a7ca-4ae1-b79e-1d964feb36ef");
        names.add("3ca3349b-d100-418f-8f32-1c6b954f41ff");
        names.add("e298b98b-adae-497f-8cb5-062753957fbf");
        
        for(int i = 0 ; i < names.size() ; i++){
            
            Feature f1 = new Feature();
            f1.setId(UUID.randomUUID().toString());
            //f1.setGeometry(new Point(new PointCoordinates(-76.599934 + (Math.random() - 0.5) / 1000, 2.457385 + (Math.random() - 0.5) / 1000)));

            f1.setGeometry(new Point(new PointCoordinates(xCoordinates.get(i),yCoordinates.get(i))));
            
            String nodeName = names.get(i);
            
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

            f1.setProperties(properties);
            
            features.add(f1);
        }
        
        return features;
    }
}

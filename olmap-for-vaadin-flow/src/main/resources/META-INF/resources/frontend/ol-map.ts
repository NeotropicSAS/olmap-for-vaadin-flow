/**
 * @license
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

import { LitElement, html } from 'lit';
import { customElement, property, query, state } from 'lit/decorators.js';
import OSM from 'ol/source/OSM';
import TileLayer from 'ol/layer/Tile';
import { Feature, Map as olMap, View } from 'ol';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Coordinate } from 'ol/coordinate';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { Circle, Geometry, GeometryCollection, LineString, Point, SimpleGeometry } from 'ol/geom';
import { Modify, Draw, Interaction } from 'ol/interaction';
import { Fill, Icon, Stroke, Style, Text, Circle as CircleStyle, RegularShape } from 'ol/style';
import { FeatureLike } from 'ol/Feature';
import BingMaps from 'ol/source/BingMaps';
import GeoJSON from 'ol/format/GeoJSON';
import { getVectorContext } from 'ol/render';
import { easeOut } from 'ol/easing';
import { unByKey } from 'ol/Observable';
import RenderEvent from 'ol/render/Event';
import { FrameState } from 'ol/PluggableMap';
import { Pixel } from 'ol/pixel';
import { altKeyOnly } from 'ol/events/condition';
import { getLength } from 'ol/sphere';

interface ViewOptionsProperty {
  center?: number[];
  zoom?: number;
}

interface FeatureObject {
  geometry?: GeometryObject | null;
  properties?: any | null;
  id: string | number;
}

interface GeometryObject {
  type?: GeometryType;
  coordinates: number[] | number[][];
}

type InteractionType = 'Select' | 'Draw' | 'Snap' | 'Modify';

interface InteractionObject {
  id: string | number,
  type: InteractionType,
  options?: any,
  active?: boolean
}

type TileLayerSourceType = 'OSM' | 'BingMaps';

interface TileLayerSourceObject {
  type?: TileLayerSourceType,
  [key: string]: any
}
/**
 * Supported geometry types.
 */
type GeometryType = 'Point' | 'LineString';

interface UnitOfLength {
  arithmeticOperator: string,
  operand2: number,
  numberOfDigits: number,
  translated: string
}
/**
 * ol-map elment.
 * @author Johny Andres Ortega Ruiz <johny.ortega@kuwaiba.org>
 */
@customElement('ol-map')
export class OlMap extends LitElement {
  @query('#map')
  target!: HTMLDivElement;

  map!: olMap;
  view: View;
  featuresSource: VectorSource;
  featuresLayer: VectorLayer<VectorSource>;
  tileLayer: TileLayer<any>;
  interactions: Map<string | number, Interaction> = new Map();
  selectedFeature: FeatureLike | null = null;
  pointermoveFeature: FeatureLike | null = null;
  styleFunction!: (feature: FeatureLike) => Style[];
  selectStyleFunction!: (feature: FeatureLike) => Style[];
  hitTolerance: number = 10;
  highlightSource: VectorSource = new VectorSource();

  @property({ attribute: false })
  measuring = false;
  measuringDraw!: Draw | null;

  private _viewOptions: ViewOptionsProperty = {};

  @property({ attribute: false })
  get viewOptions(): ViewOptionsProperty {
    return this._viewOptions;
  }
  set viewOptions(value: ViewOptionsProperty) {
    this._viewOptions = value;
    if (value.center) {
      this.view.setCenter(fromLonLat(value.center));
    }
    if (value.zoom) {
      this.view.setZoom(value.zoom);
    }
  }

  private _tileLayerSource: TileLayerSourceObject = {};

  @property({ attribute: false })
  get tileLayerSource(): TileLayerSourceObject {
    return this._tileLayerSource;
  }

  set tileLayerSource(value: TileLayerSourceObject) {
    this._tileLayerSource = value;
    switch (value.type) {
      case 'OSM':
        this.tileLayer.setSource(new OSM());
        break;
      case 'BingMaps':
        this.tileLayer.setSource(
          new BingMaps({
            key: value.key,
            imagerySet: value.imagerySet,
          })
        );
        break;
    }
  }

  constructor() {
    super();
    this.view = new View();
    this.tileLayer = new TileLayer();
    this.featuresSource = new VectorSource();
    this.featuresLayer = new VectorLayer();
    this.featuresLayer.setZIndex(2);
    this.init();
  }

  addPoint(coordinates: Array<number>, id: string | number, text: string): FeatureObject {
    const point: FeatureObject = {
      geometry: {
        type: 'Point',
        coordinates: coordinates
      },
      id: id,
      properties: {
        style: {
          image: {
            icon: {
              src: 'location-pin.png'
            }
          },
          text: {
            font: '12px sans-serif',
            text: text,
            //text: ['Point 1', 'bold 10px sans-serif', ' Line String 1', 'italic 10px sans-serif'],
            fill: {
              color: 'white'
            },
            backgroundFill: {
              color: 'gray'
            },
            minZoom: 12
          }
        },
        selectedStyle: {
          text: {
            backgroundFill: {
              color: 'red'
            }
          }
        }
      }
    }
    this.addFeature(point);
    return point;
  }

  init() {
    this.viewOptions = {
      center: [-77.2772226, 1.2135255],
      zoom: 20
    };
    this.tileLayerSource = {
      type: 'OSM',
    }
    // this.tileLayerSource = {
    //   type: 'BingMaps',
    //   key: '',
    //   imagerySet: 'CanvasDark'
    // }

    // this.addEventListener('load-complete', async () => {
    //   console.log('>>> load-complete');
    // });
    const point = this.addPoint([-77.2772226, 1.2135255], 0, 'Point 1');
    point.properties.style.text.text = 'Point 1*';
    this.updateFeature(point);
    this.animateFeature(point);

    const lineString: FeatureObject = {
      geometry: {
        type: 'LineString',
        coordinates: [[-77.2772226, 1.2135255], [-77.2782226, 1.2135255]]
      },
      id: 1,
      properties: {
        style: {
          text: {
            font: '12px sans-serif',
            text: 'Line String 1',
            // text: ['Line String 1', 'italic 10px sans-serif', ' Point 1', 'bold 10px sans-serif'],
            fill: {
              color: 'white'
            },
            backgroundFill: {
              color: 'gray'
            },
            minZoom: 12
          },
          stroke: {
            color: 'red',
            width: 2
          }
        },
        selectedStyle: {
          text: {
            backgroundFill: {
              color: 'yellow'
            }
          },
          stroke: {
            color: 'yellow',
            width: 2
          }
        }
      }
    };
    this.addFeature(lineString);
    // this.animateFeature(lineString);

    this.addEventListener('load-complete', async () => {
      this.addInteraction({
        id: 0,
        type: 'Modify'
      });
      // this.addInteraction({
      //   id: 1,
      //   type: 'Draw',
      //   options: {
      //     type: 'Point'
      //   }
      // });

      // this.addInteraction({
      //   id: 2,
      //   type: 'Draw',
      //   options: {
      //     type: 'LineString'
      //   }
      // });
    });
    this.measuring = true;
  }

  protected render(): unknown {
    return html`
           <div id="map" style="height: 100%; width: 100%;"></div>
       `;
  }

  protected createRenderRoot(): Element | ShadowRoot {
    return this;
  }

  protected firstUpdated(_changedProperties: globalThis.Map<string | number | symbol, unknown>): void {
    this.map = new olMap({
      controls: [],
      target: this.target,
      layers: [this.tileLayer],
      view: this.view,
    });
    this.featuresLayer.setSource(this.featuresSource);
    this.map.addLayer(this.featuresLayer);

    const styleTextFunction = (feature: FeatureLike) => {
      const style = new Style();
      const padding = 3;
      const offsetY = 10;
      const featurePropertyStyle = feature.get('style');
      if (featurePropertyStyle && featurePropertyStyle.text && featurePropertyStyle.text.minZoom <= (this.map.getView().getZoom() as number)) {
        style.setText(new Text({
          font: featurePropertyStyle.text.font,
          text: featurePropertyStyle.text.text,
          textBaseline: 'top',
          textAlign: 'center',
          fill: new Fill({ color: featurePropertyStyle.text.fill.color }),
          backgroundFill: new Fill({ color: featurePropertyStyle.text.backgroundFill.color }),
          padding: [padding, padding, padding, padding],
          offsetY: offsetY
        }));
      }
      return style;
    };
    const styleFeatureFunction = (feature: FeatureLike) => {
      const style = new Style();
      const featurePropertyStyle = feature.get('style');
      if (featurePropertyStyle) {
        if (feature.getGeometry()?.getType() === 'Point') {
          style.setImage(new Icon({
            anchor: [0.5, 1],
            src: featurePropertyStyle.image.icon.src,
          }));
        } else if (feature.getGeometry()?.getType() === 'LineString') {
          if (featurePropertyStyle.stroke) {
            style.setStroke(new Stroke({
              color: featurePropertyStyle.stroke.color,
              width: featurePropertyStyle.stroke.width
            }));
          }
        }
      }
      return style;
    }
    this.styleFunction = (feature: FeatureLike) => {
      return [styleFeatureFunction(feature), styleTextFunction(feature)];
    };
    this.featuresLayer.setStyle(this.styleFunction);

    this.selectStyleFunction = (feature: FeatureLike) => {
      const styles = [];
      const featurePropertyStyle = feature.get('selectedStyle');
      if (featurePropertyStyle) {
        const styleFeature = styleFeatureFunction(feature);
        styles.push(styleFeature);

        const styleText = styleTextFunction(feature);
        styles.push(styleText);
        if (featurePropertyStyle.text && styleText.getText()) {
          styleText.getText().setBackgroundFill(new Fill({ color: featurePropertyStyle.text.backgroundFill.color }));
        }
        if (feature.getGeometry()?.getType() === 'LineString') {
          const coordinates: number[][] = (feature.getGeometry() as SimpleGeometry).getCoordinates() as number[][];
          const geometries: SimpleGeometry[] = [];

          const radius = 5.5;
          coordinates.forEach(c => {
            geometries.push(new Circle(c, radius * (this.map.getView().getResolution() as number)))
          });
          styleFeature.setZIndex(1);
          const styleGeometries = new Style({
            geometry: new GeometryCollection(geometries),
            fill: new Fill({ color: 'white' }),
            stroke: new Stroke({ color: featurePropertyStyle.stroke.color }),
            zIndex: 2
          })
          styles.push(styleGeometries);
          if (featurePropertyStyle.stroke) {
            styleFeature.setStroke(new Stroke({
              color: featurePropertyStyle.stroke.color,
              width: featurePropertyStyle.stroke.width
            }));
          }
          styleText.setZIndex(3);
        }
      }
      return styles;
    }
    this.map.on('moveend', () => {
      this.dispatchEvent(new CustomEvent('map-moveend', {
        detail: {
          view: {
            zoom: this.view.getZoom(),
            center: toLonLat(this.view.getCenter() as Coordinate)
          }
        }
      }));
    });
    this.highlightSource = new VectorSource();
    const highlightLayer = new VectorLayer({ source: this.highlightSource });
    highlightLayer.setZIndex(1);
    this.map.addLayer(highlightLayer);
    this.map.on('pointermove', evt => {
      this.dispatchEvent(new CustomEvent('map-pointermove', {
        detail: {
          coordinate: toLonLat(evt.coordinate)
        }
      }));
      this.highlightSource.clear();
      if (this.pointermoveFeature !== null) {
        this.pointermoveFeature = null;
      }
      this.pointermoveFeature = this.getFeatureAtPixel(evt.pixel);
      if (this.pointermoveFeature) {
        const feature = (this.pointermoveFeature as Feature).clone();
        const color = 'rgba(135,206,235, 0.75)';
        if (feature.getGeometry()?.getType() === 'LineString') {
          feature.setStyle(new Style({
            stroke: new Stroke({ color: color, width: 7 }),
            fill: new Fill({ color: color })
          }));
        } else if (feature.getGeometry()?.getType() === 'Point') {
          feature.setStyle(new Style({
            geometry: new Circle(
              (feature.getGeometry() as SimpleGeometry).getCoordinates() as number[],
              25 * (this.map.getView().getResolution() as number)),
            stroke: new Stroke({ color: color, width: 7 }),
            fill: new Fill({ color: color })
          }));
        }
        this.highlightSource.addFeature(feature);
      }
    });
    this.map.on('singleclick', evt => {
      this.dispatchEvent(new CustomEvent('map-singleclick', {
        detail: {
          coordinate: toLonLat(evt.coordinate)
        }
      }));
      this.select(evt.pixel);
      if (this.selectedFeature) {
        this.dispatchEvent(new CustomEvent('map-select-select', {
          detail: {
            deselectedIds: [],
            selectedIds: [this.selectedFeature.getId()]
          }
        }));
      }
    });
    this.map.getViewport().addEventListener('contextmenu', evt => {
      evt.preventDefault();
      let selected: FeatureLike | null = null;
      this.map.getFeaturesAtPixel(this.map.getEventPixel(evt), { hitTolerance: 10 }).forEach(feature => {
        if (feature.getId() !== undefined) {
          selected = feature;
        }
        return true;
      });
      if (selected) {
        this.dispatchEvent(new CustomEvent('map-feature-contextmenu', {
          detail: {
            featureId: (selected as FeatureLike).getId()
          }
        }));
      } else {
        this.dispatchEvent(new CustomEvent('map-viewport-contextmenu', {
          detail: {
            coordinate: toLonLat(this.map.getEventCoordinate(evt))
          }
        }));
      }
    });
    this.view.on('change:resolution', () => this.dispatchEvent(new CustomEvent('view-change:resolution', {
      detail: {
        view: {
          zoom: this.view.getZoom()
        }
      }
    })));
    this.dispatchEvent(new CustomEvent('load-complete'));
  }

  getFeatureAtPixel(pixel: Pixel): FeatureLike | null {
    let featureAtPixel = null
    this.map.getFeaturesAtPixel(pixel, { hitTolerance: this.hitTolerance }).forEach(feature => {
      if (!this.highlightSource.hasFeature(feature as Feature) && feature.getId() !== undefined) {
        featureAtPixel = feature;
      }
      return true;
    });
    return featureAtPixel;
  }

  select(pixel: Pixel) {
    if (this.selectedFeature !== null) {
      (this.selectedFeature as Feature).setStyle(this.styleFunction);
      this.selectedFeature = null;
    }
    this.selectedFeature = this.getFeatureAtPixel(pixel);
    if (this.selectedFeature) {
      (this.selectedFeature as Feature).setStyle(this.selectStyleFunction);
    }
  }

  getFeature(featureObject: FeatureObject) {
    return this.featuresSource.getFeatureById(featureObject.id);
  }

  getLength(featureObject: FeatureObject) {
    return getLength(this.getFeature(featureObject)?.getGeometry() as Geometry);
  }

  toLonLat(coordinate: number[]) {
    return toLonLat(coordinate);
  }

  /**
   * https://openlayers.org/en/latest/examples/feature-animation.html
   */
  animateFeature(featureObject: FeatureObject) {
    const feature = this.featuresSource.getFeatureById(featureObject.id);
    if (feature) {
      const duration = 5000;
      const start = Date.now();

      let coordinates = undefined;
      if (featureObject && featureObject.geometry && featureObject.geometry.type && featureObject.geometry.coordinates) {
        if (featureObject.geometry.type === 'Point')
          coordinates = (feature.getGeometry() as Point).getCoordinates();
        if (featureObject.geometry.type === 'LineString') {
          coordinates = (feature.getGeometry() as LineString).getCoordinateAt(0.5);
        }
      }
      const geom = new Point(coordinates as number[]);

      const animate = (event: RenderEvent) => {
        const frameState: FrameState = event.frameState as FrameState;
        const elapsed = frameState.time - start;
        if (elapsed >= duration) {
          unByKey(listenerKey);
          return;
        }
        const vectorContext = getVectorContext(event);
        const elapsedRatio = elapsed / duration;
        // radius will be 5 at start and 30 at end.
        const radius = easeOut(elapsedRatio) * 25 + 5;
        const opacity = easeOut(1 - elapsedRatio);

        const style = new Style({
          image: new CircleStyle({
            radius: radius,
            stroke: new Stroke({
              color: 'rgba(255, 0, 0, ' + opacity + ')',
              width: 0.25 + opacity,
            }),
          }),
        });

        vectorContext.setStyle(style);
        vectorContext.drawGeometry(geom);
        // tell OpenLayers to continue postrender animation
        this.map.render();
      }
      const listenerKey = this.featuresLayer.on('postrender', animate);
    }
  }

  addFeature(featureObject: FeatureObject): boolean {
    const feature: Feature = new Feature();
    const coordinates = this.getCoordinates(featureObject);
    if (coordinates && featureObject.geometry && featureObject.geometry.type && featureObject.geometry.coordinates) {
      let geometry: Geometry | undefined = undefined;
      if (featureObject.geometry.type === 'Point')
        geometry = new Point(coordinates as number[]);
      if (featureObject.geometry.type === 'LineString')
        geometry = new LineString(coordinates as number[][]);
      feature.setGeometry(geometry);
    }
    feature.setId(featureObject.id);
    feature.setProperties(featureObject.properties, true);
    this.featuresSource.addFeature(feature);
    return true;
  }

  getCoordinates(featureObject: FeatureObject): number[] | number[][] | undefined {
    let coordinates = undefined;
    if (featureObject && featureObject.geometry && featureObject.geometry.type && featureObject.geometry.coordinates) {
      if (featureObject.geometry.type === 'Point')
        coordinates = fromLonLat(featureObject.geometry.coordinates as number[]);
      if (featureObject.geometry.type === 'LineString') {
        coordinates = [];
        (featureObject.geometry.coordinates as number[][]).forEach(coordinate => {
          coordinates.push(fromLonLat(coordinate as number[]));
        });
      }
    }
    return coordinates;
  }

  updateFeature(featureObject: FeatureObject) {
    const feature = this.featuresSource.getFeatureById(featureObject.id);
    if (feature) {
      feature.setProperties(featureObject.properties);
      const coordinates = this.getCoordinates(featureObject);
      if (coordinates)
        (feature.getGeometry() as SimpleGeometry).setCoordinates(coordinates);
    }
  }

  removeFeature(featureObject: FeatureObject) {
    const feature = this.featuresSource.getFeatureById(featureObject.id);
    if (feature)
      this.featuresSource.removeFeature(feature);
  }

  addInteraction(interaction: InteractionObject) {
    if (interaction) {
      if (interaction.type === 'Select') {
      } else if (interaction.type === 'Draw') {
        const draw = new Draw({
          source: this.featuresSource,
          type: interaction.options.type
        });
        if (interaction.active !== undefined)
          draw.setActive(interaction.active);
        draw.on('drawend', evt => {
          const geoJson = new GeoJSON();
          const encodedFeature = geoJson.writeFeature(evt.feature);
          const feature = JSON.parse(encodedFeature);
          if (feature.geometry.type === 'Point') {
            feature.geometry.coordinates = toLonLat(feature.geometry.coordinates);
          } else if (feature.geometry.type === 'LineString') {
            const lonLatCoordinates: Array<Array<number>> = [];
            (feature.geometry.coordinates as Array<Array<number>>).forEach(coordinates => lonLatCoordinates.push(toLonLat(coordinates)));
            feature.geometry.coordinates = lonLatCoordinates;
          }
          this.dispatchEvent(new CustomEvent('map-draw-draw-end', {
            detail: {
              feature: feature
            }
          }));
          // this.addPoint(feature.geometry.coordinates, 2,  'Point 2');
          // interaction.active = false;
          // this.updateInteraction(interaction);
        });
        this.map.addInteraction(draw);
        this.interactions.set(interaction.id, draw);
      } else if (interaction.type === 'Modify') {
        const modify = new Modify({
          source: this.featuresSource,
          deleteCondition: (evt) => {
            let featureAtPixel: FeatureLike | null = null
            this.map.getFeaturesAtPixel(evt.pixel, { hitTolerance: this.hitTolerance }).forEach(feature => {
              if (!this.highlightSource.hasFeature(feature as Feature)) {
                featureAtPixel = feature;
              }
              return true;
            });
            if (featureAtPixel && (featureAtPixel as FeatureLike).getId() === undefined)
              return altKeyOnly(evt)
            return false;
          },
          insertVertexCondition: (evt) => {
            const feature = this.getFeatureAtPixel(evt.pixel);
            if (feature && this.selectedFeature && feature.getId() === this.selectedFeature.getId())
              return true;
            return false;
          }
        });
        modify.on('modifyend', evt => {
          const geoJson = new GeoJSON();
          const encodedFeatures = geoJson.writeFeatures(evt.features.getArray() as Array<Feature>);
          const featureCollection = JSON.parse(encodedFeatures);
          const features: Array<any> = featureCollection.features;
          features.forEach(feature => {
            if (feature.geometry.type === 'Point')
              feature.geometry.coordinates = toLonLat(feature.geometry.coordinates);
            else if (feature.geometry.type === 'LineString') {
              const lonLatCoordinates: Array<Array<number>> = [];
              (feature.geometry.coordinates as Array<Array<number>>).forEach(coordinates => lonLatCoordinates.push(toLonLat(coordinates)));
              feature.geometry.coordinates = lonLatCoordinates;
            }
          });
          this.dispatchEvent(new CustomEvent('map-modify-modify-end', {
            detail: {
              features: featureCollection
            }
          }));
        });
        this.map.addInteraction(modify);
        this.interactions.set(interaction.id, modify);
      } else if (interaction.type === 'Snap') {
      }
    }
  }

  updateInteraction(interactionObject: InteractionObject) {
    const interaction = this.interactions.get(interactionObject.id);
    if (interaction) {
      if (interactionObject.active !== undefined) {
        interaction.setActive(interactionObject.active);
      }
    }
  }

  removeInteraction(interactionObject: InteractionObject) {
    const interaction = this.interactions.get(interactionObject.id);
    if (interaction) {
      this.interactions.delete(interactionObject.id);
      this.map.removeInteraction(interaction);
    }
  }

  updated(changedProperties: any) {
    changedProperties.forEach((_oldValue: any, propName: any) => {
      if (propName === 'measuring') {
        if (this.measuringDraw && this.measuring === false) {
          this.map.removeInteraction(this.measuringDraw);
          this.measuringDraw = null;
        }
        // this.measure({arithmeticOperator: '*', operand2: 39.37, numberOfDigits: 2, translated: 'in'});
      }
    });
  }

  measure(unitOfLength?: UnitOfLength) {
    if (this.measuring) {
      if (this.measuringDraw) {
        this.map.removeInteraction(this.measuringDraw);
        this.measuringDraw = null;
      }
      const style = new Style({
        fill: new Fill({
          color: 'rgba(255, 255, 255, 0.2)'
        }),
        stroke: new Stroke({
          color: 'rgba(0, 0, 0, 0.5)',
          lineDash: [10, 10],
          width: 2
        }),
        image: new CircleStyle({
          radius: 5,
          stroke: new Stroke({
            color: 'rgba(0, 0, 0, 0.7)'
          }),
          fill: new Fill({
            color: 'rgba(255, 255, 255, 0.2)'
          })
        }),
      });
      const labelStyle = new Style({
        text: new Text({
          font: '14px Calibri,sans-serif',
          fill: new Fill({
            color: 'rgba(255, 255, 255, 1)'
          }),
          backgroundFill: new Fill({
            color: 'rgba(0, 0, 0, 0.7)'
          }),
          padding: [3, 3, 3, 3],
          textBaseline: 'bottom',
          offsetY: -15
        }),
        image: new RegularShape({
          radius: 8,
          points: 3,
          angle: Math.PI,
          displacement: [0, 10],
          fill: new Fill({
            color: 'rgba(0, 0, 0, 0.7)'
          })
        })
      });
      this.measuringDraw = new Draw({
        source: this.featuresSource,
        type: 'LineString',
        style: feature => {
          const styles = [style];
          const geometry = feature.getGeometry() as SimpleGeometry;
          const type = geometry.getType();

          if (type === 'LineString') {
            labelStyle.setGeometry(new Point((geometry as LineString).getLastCoordinate()));
            const length = getLength(geometry);
            let text = `${length}`;
            if (unitOfLength) {
              if (unitOfLength.arithmeticOperator === '/') {
                text = `${(length / unitOfLength.operand2).toFixed(unitOfLength.numberOfDigits)} ${unitOfLength.translated}`;
              } else if (unitOfLength.arithmeticOperator === '*') {
                text = `${(length * unitOfLength.operand2).toFixed(unitOfLength.numberOfDigits)} ${unitOfLength.translated}`;
              }
            }
            labelStyle.getText().setText(text);
            styles.push(labelStyle);
          }
          return styles;
        }
      });
      this.map.addInteraction(this.measuringDraw);
    }
  }
}

declare global {
  interface HTMLElementTagNameMap {
    'ol-map': OlMap;
  }
}

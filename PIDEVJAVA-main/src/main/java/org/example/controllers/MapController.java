package org.example.controllers;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.models.Remorqueur;
import org.example.services.MapService;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class MapController {
    @FXML
    private VBox address;

    private MapPoint currentLocation;

    private CustomMapLayer customMapLayer;

    public void initialize() {
        MapView mapView = createMapView();
        address.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        fetchAndDisplayRemorqueurs();
    }

    private void fetchAndDisplayRemorqueurs() {
        MapService mapService = new MapService();
        List<Remorqueur> remorqueurs = mapService.fetchRemorqueurs();
        for (Remorqueur remorqueur : remorqueurs) {
            MapPoint point = new MapPoint(remorqueur.getLatitude(), remorqueur.getLongitude());
            customMapLayer.addMarker(point, Color.RED, remorqueur);
        }
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        customMapLayer = new CustomMapLayer();
        mapView.addLayer(customMapLayer);
        mapView.setCenter(33.8869, 9.5375);
        mapView.setZoom(6);
        mapView.setPrefSize(500, 400);
        return mapView;
    }

    private class CustomMapLayer extends MapLayer {
        private List<Node> markers = new ArrayList<>();

        public void addMarker(MapPoint point, Color color, Remorqueur remorqueur) {
            Circle marker = new Circle(5, color);
            marker.setUserData(point);
            marker.setOnMouseClicked(event -> displayRemorqueurInfo(remorqueur));
            getChildren().add(marker);
            markers.add(marker);
        }

        private void displayRemorqueurInfo(Remorqueur remorqueur) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Remorqueur Information");
            alert.setHeaderText(null);
            VBox content = new VBox();
            content.setSpacing(10);
            Label firstNameLabel = new Label("First Name: " + remorqueur.getFirstName());
            Label lastNameLabel = new Label("Last Name: " + remorqueur.getLastName());
            Label phoneNumberLabel = new Label("Phone Number: " + remorqueur.getPhoneNumber());

            double distance = calculateDistance(remorqueur.getLatitude(), remorqueur.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude());
            Label distanceLabel = new Label("kilometers far from you : " + distance + " km");
            content.getChildren().addAll(firstNameLabel, lastNameLabel, phoneNumberLabel, distanceLabel);
            alert.getDialogPane().setContent(content);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            alert.showAndWait();
        }
        private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            final int R = 6371;

            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c;

            return distance;
        }


        @Override
        protected void layoutLayer() {
            for (Node node : markers) {
                MapPoint point = (MapPoint) node.getUserData();
                Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
                node.setTranslateX(mapPoint.getX());
                node.setTranslateY(mapPoint.getY());
            }
        }
    }

    @FXML
    private void addMarkerOnClick() {
        MapPoint point = new MapPoint(36.901354, 10.190140);
        currentLocation = point;
        customMapLayer.addMarker(point,Color.BLUE,null);

    }

}
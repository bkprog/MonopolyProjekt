package Source;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class GridMapImages {
    private GridPane panelMapGrid;
    private ArrayList <GridMapImage> propertiesGridMap;
    private ArrayList <VBox> propbertiesBoxes;

    public GridMapImages(GridPane panelMapGrid, ArrayList <Properties> propertiesList,ArrayList <Player> playersList){
        panelMapGrid.setAlignment(Pos.CENTER);
        panelMapGrid.setVgap(10);
        panelMapGrid.setHgap(5);
        propertiesGridMap = new ArrayList<>();
        propbertiesBoxes = new ArrayList<>();
        this.panelMapGrid = panelMapGrid;
        for(Properties prop : propertiesList){
            GridMapImage newProperty;
            if(prop.getCountryName().equals("Grecja") || prop.getCountryName().equals("Włochy") ||
            prop.getCountryName().equals("Hiszpania") || prop.getCountryName().equals("Anglia") ||
            prop.getCountryName().equals("Benelux") || prop.getCountryName().equals("Szwecja") ||
            prop.getCountryName().equals("RFN") || prop.getCountryName().equals("Austria") ||
            prop.getNameProperty().startsWith("Linie Kolejowe") || prop.getNameProperty().equals("Elektrownia") ||
            prop.getNameProperty().equals("Wodociągi")){
                VBox propertyBox = new VBox();
                propertyBox.setAlignment(Pos.CENTER);
                propertyBox.setSpacing(10);
                Label PropertyOwnerNickname = new Label("");
                if(prop.getOwnerID() != 0)
                    newProperty = new GridMapImage(prop.getIDproperty(),playersList.get(prop.getOwnerID()-1).getPlayerName());
                else
                    newProperty = new GridMapImage(prop.getIDproperty(),"BW");

                if(prop.getCountryName().equals("Grecja") ||
                        prop.getCountryName().equals("Włochy")){

                    if (prop.getNameProperty().equals("Saloniki")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,0,0);
                    }
                    else if(prop.getNameProperty().equals("Ateny")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,1,0);
                    }

                    else if(prop.getNameProperty().equals("Neapol")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,2,0);
                    }

                    else if(prop.getNameProperty().equals("Mediolan")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,3,0);
                    }

                    else if(prop.getNameProperty().equals("Rzym")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,4,0);
                    }
                }

                else if(prop.getCountryName().equals("Hiszpania") ||
                        prop.getCountryName().equals("Anglia")){
                    if(prop.getNameProperty().equals("Barcelona")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,0,1);
                    }
                    else if(prop.getNameProperty().equals("Sewilla")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,1,1);
                    }
                    else if(prop.getNameProperty().equals("Madryt")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,2,1);
                    }
                    else if(prop.getNameProperty().equals("Liverpool")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,3,1);
                    }
                    else if(prop.getNameProperty().equals("Glasgow")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,4,1);
                    }
                    else if(prop.getNameProperty().equals("Londyn")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,5,1);
                    }
                }

                else if(prop.getCountryName().equals("Benelux") ||
                        prop.getCountryName().equals("Szwecja")){

                    if(prop.getNameProperty().equals("Rotterdam")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,0,2);
                    }
                    else if(prop.getNameProperty().equals("Bruksela")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,1,2);
                    }
                    else if(prop.getNameProperty().equals("Amsterdam")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,2,2);
                    }
                    else if(prop.getNameProperty().equals("Malmo")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,3,2);
                    }
                    else if(prop.getNameProperty().equals("Goteborg")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,4,2);
                    }
                    else if(prop.getNameProperty().equals("Sztokholm")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,5,2);
                    }
                }

                else if(prop.getCountryName().equals("RFN") ||
                        prop.getCountryName().equals("Austria")){

                    if(prop.getNameProperty().equals("Frankfurt")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,0,3);
                    }

                    else if(prop.getNameProperty().equals("Kolonia")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,1,3);
                    }

                    else if(prop.getNameProperty().equals("Bonn")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,2,3);
                    }
                    else if(prop.getNameProperty().equals("Insbruck")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,3,3);
                    }
                    else if(prop.getNameProperty().equals("Wieden")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,4,3);
                    }
                }
                else if(prop.getNameProperty().equals("Linie Kolejowe Południowe") ||
                        prop.getNameProperty().equals("Linie Kolejowe Wschodnie") ||
                        prop.getNameProperty().equals("Linie Kolejowe Północne") ||
                        prop.getNameProperty().equals("Linie Kolejowe Zachodnie") ||
                        prop.getNameProperty().equals("Wodociągi") ||
                        prop.getNameProperty().equals("Elektrownia")){

                    if(prop.getNameProperty().equals("Linie Kolejowe Południowe")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,0,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Wschodnie")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,1,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Północne")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,2,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Zachodnie")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,3,4);
                    }

                    else if(prop.getNameProperty().equals("Wodociągi")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,4,4);
                    }

                    else if(prop.getNameProperty().equals("Elektrownia")){
                        PropertyOwnerNickname.setText(newProperty.getPropertyOwner());
                        propertyBox.getChildren().addAll(newProperty.getPropImageView(),PropertyOwnerNickname);
                        propbertiesBoxes.add(propertyBox);
                        panelMapGrid.add(propertyBox,5,4);
                    }
                }
            }
        }
    }

    public GridPane getPanelMapGrid(){
        return this.panelMapGrid;
    }

}

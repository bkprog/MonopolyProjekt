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
    //private ArrayList <VBox> propbertiesBoxes;

    public GridMapImages(GridPane panelMapGrid, ArrayList <Properties> propertiesList,ArrayList <Player> playersList){
//        this.panelMapGrid = null;
        panelMapGrid.setAlignment(Pos.CENTER);
        panelMapGrid.setVgap(10);
        panelMapGrid.setHgap(5);
        propertiesGridMap = new ArrayList<>();
        this.panelMapGrid = panelMapGrid;
//        propbertiesBoxes = new ArrayList<>();

        for(Properties prop : propertiesList){
            GridMapImage newProperty;
            if(prop.getCountryName().equals("Grecja") || prop.getCountryName().equals("Włochy") ||
            prop.getCountryName().equals("Hiszpania") || prop.getCountryName().equals("Anglia") ||
            prop.getCountryName().equals("Benelux") || prop.getCountryName().equals("Szwecja") ||
            prop.getCountryName().equals("RFN") || prop.getCountryName().equals("Austria") ||
            prop.getNameProperty().startsWith("Linie Kolejowe") || prop.getNameProperty().equals("Elektrownia") ||
            prop.getNameProperty().equals("Wodociągi")){
                //VBox propertyBox = new VBox();
//                propertyBox.setAlignment(Pos.CENTER);
//                propertyBox.setSpacing(10);
                Label PropertyOwnerNickname = new Label("");
                if(prop.getOwnerID() != 0)
                    newProperty = new GridMapImage(prop.getIDproperty(),"Własność: " + playersList.get(prop.getOwnerID()-1).getPlayerName());
                else
                    newProperty = new GridMapImage(prop.getIDproperty(),"");

                if(prop.getCountryName().equals("Grecja") ||
                        prop.getCountryName().equals("Włochy")){

                    if (prop.getNameProperty().equals("Saloniki")){
                        panelMapGrid.add(newProperty.getBox(),0,0);
                    }
                    else if(prop.getNameProperty().equals("Ateny")){

                        panelMapGrid.add(newProperty.getBox(),1,0);
                    }

                    else if(prop.getNameProperty().equals("Neapol")){
                        panelMapGrid.add(newProperty.getBox(),2,0);
                    }

                    else if(prop.getNameProperty().equals("Mediolan")){
                        panelMapGrid.add(newProperty.getBox(),3,0);
                    }

                    else if(prop.getNameProperty().equals("Rzym")){
                        panelMapGrid.add(newProperty.getBox(),4,0);
                    }
                }

                else if(prop.getCountryName().equals("Hiszpania") ||
                        prop.getCountryName().equals("Anglia")){
                    if(prop.getNameProperty().equals("Barcelona")){
                        panelMapGrid.add(newProperty.getBox(),0,1);
                    }
                    else if(prop.getNameProperty().equals("Sewilla")){
                        panelMapGrid.add(newProperty.getBox(),1,1);
                    }
                    else if(prop.getNameProperty().equals("Madryt")){

                        panelMapGrid.add(newProperty.getBox(),2,1);
                    }
                    else if(prop.getNameProperty().equals("Liverpool")){

                        panelMapGrid.add(newProperty.getBox(),3,1);
                    }
                    else if(prop.getNameProperty().equals("Glasgow")){

                        panelMapGrid.add(newProperty.getBox(),4,1);
                    }
                    else if(prop.getNameProperty().equals("Londyn")){

                        panelMapGrid.add(newProperty.getBox(),5,1);
                    }
                }

                else if(prop.getCountryName().equals("Benelux") ||
                        prop.getCountryName().equals("Szwecja")){

                    if(prop.getNameProperty().equals("Rotterdam")){

                        panelMapGrid.add(newProperty.getBox(),0,2);
                    }
                    else if(prop.getNameProperty().equals("Bruksela")){

                        panelMapGrid.add(newProperty.getBox(),1,2);
                    }
                    else if(prop.getNameProperty().equals("Amsterdam")){

                        panelMapGrid.add(newProperty.getBox(),2,2);
                    }
                    else if(prop.getNameProperty().equals("Malmo")){

                        panelMapGrid.add(newProperty.getBox(),3,2);
                    }
                    else if(prop.getNameProperty().equals("Goteborg")){

                        panelMapGrid.add(newProperty.getBox(),4,2);
                    }
                    else if(prop.getNameProperty().equals("Sztokholm")){

                        panelMapGrid.add(newProperty.getBox(),5,2);
                    }
                }

                else if(prop.getCountryName().equals("RFN") ||
                        prop.getCountryName().equals("Austria")){

                    if(prop.getNameProperty().equals("Frankfurt")){

                        panelMapGrid.add(newProperty.getBox(),0,3);
                    }

                    else if(prop.getNameProperty().equals("Kolonia")){

                        panelMapGrid.add(newProperty.getBox(),1,3);
                    }

                    else if(prop.getNameProperty().equals("Bonn")){

                        panelMapGrid.add(newProperty.getBox(),2,3);
                    }
                    else if(prop.getNameProperty().equals("Insbruck")){

                        panelMapGrid.add(newProperty.getBox(),3,3);
                    }
                    else if(prop.getNameProperty().equals("Wieden")){

                        panelMapGrid.add(newProperty.getBox(),4,3);
                    }
                }
                else if(prop.getNameProperty().equals("Linie Kolejowe Południowe") ||
                        prop.getNameProperty().equals("Linie Kolejowe Wschodnie") ||
                        prop.getNameProperty().equals("Linie Kolejowe Północne") ||
                        prop.getNameProperty().equals("Linie Kolejowe Zachodnie") ||
                        prop.getNameProperty().equals("Wodociągi") ||
                        prop.getNameProperty().equals("Elektrownia")){

                    if(prop.getNameProperty().equals("Linie Kolejowe Południowe")){

                        panelMapGrid.add(newProperty.getBox(),0,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Wschodnie")){

                        panelMapGrid.add(newProperty.getBox(),1,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Północne")){

                        panelMapGrid.add(newProperty.getBox(),2,4);
                    }

                    else if(prop.getNameProperty().equals("Linie Kolejowe Zachodnie")){

                        panelMapGrid.add(newProperty.getBox(),3,4);
                    }

                    else if(prop.getNameProperty().equals("Wodociągi")){

                        panelMapGrid.add(newProperty.getBox(),4,4);
                    }

                    else if(prop.getNameProperty().equals("Elektrownia")){

                        panelMapGrid.add(newProperty.getBox(),5,4);
                    }
                }
            }
        }
//        panelMapGrid = this.panelMapGrid;
    }

    public GridPane getPanelMapGrid(){
        return this.panelMapGrid;
    }

    public void setOwnerGrid(ArrayList <Properties> propertiesList, ArrayList <Player> playersList){
        int index = 0;
        for(Properties prop : propertiesList){
            if(prop.getCountryName().equals("Grecja") ||
                    prop.getCountryName().equals("Włochy") ||
                    prop.getCountryName().equals("Hiszpania") ||
                    prop.getCountryName().equals("Anglia") ||
                    prop.getCountryName().equals("Benelux") ||
                    prop.getCountryName().equals("Szwecja") ||
                    prop.getCountryName().equals("RFN") ||
                    prop.getCountryName().equals("Austria") ||
            prop.getNameProperty().startsWith("Linie Kolejowe") ||
            prop.getNameProperty().equals("Elektrownia") ||
            prop.getNameProperty().equals("Wodociągi")){

                GridMapImage actualGMI = propertiesGridMap.get(index);

                if(prop.getOwnerID() != 0){
                    Label newOwner = new Label(playersList.get(prop.getOwnerID()-1).getPlayerName());
                    actualGMI.setPropertyInfo(newOwner);
                }
                else{
                    Label newOwner = new Label("BW");
                    actualGMI.setPropertyInfo(newOwner);
                }

                index++;
            }
        }
    }
}

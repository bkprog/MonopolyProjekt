package Client2;

import Source.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Client2 extends Application {
    ArrayList<Player> playersList = new ArrayList<>();
    ArrayList<Properties> propertiesList = new ArrayList<>();
    public static DataInputStream dIn;
    public static DataOutputStream dOut;
    private static Socket socket = null;
    public String playerNickname;
    int NubmerOfPlayersInGame;
    public int playersReady = 0;
    public int PlayerToursReady = 0;
    Player player = new Player();
    Properties property = new Properties();
    String respondServer = new String();
    Button readyButton = new Button("Ready");
    Label readyCheckInfo = new Label("Click on ready button when you are ready");
    Label clientConnected = new Label();
    Label player1 = new Label();
    Label player2 = new Label();
    Label player3 = new Label();
    Label player4 = new Label();
    Label p1 = new Label();
    Label p2 = new Label();
    Label p3 = new Label();
    Label p4 = new Label();
    Image pawnPlayer1 = new Image("/images/Pawns/pawn-1.png");
    Image pawnPlayer2 = new Image("/images/Pawns/pawn-2.png");
    Image pawnPlayer3 = new Image("/images/Pawns/pawn-3.png");
    Image pawnPlayer4 = new Image("/images/Pawns/pawn-4.png");
    ImageView pawnView1 = new ImageView(pawnPlayer1);
    ImageView pawnView2 = new ImageView(pawnPlayer2);
    ImageView pawnView3 = new ImageView(pawnPlayer3);
    ImageView pawnView4 = new ImageView(pawnPlayer4);
    VBox panelOponents = new VBox();
    VBox panelTourPlayer = new VBox();
    VBox panelTourPlayerInJail = new VBox();
    VBox box = new VBox();
    Button dicing = new Button("Dice!");
    Button readyTour = new Button("Ready");
    Label info = new Label("Click button to dice");
    Label diceedInfo = new Label();
    Button GetReadyTourButton = new Button("Get ready!");
    Label informationOponentPanel = new Label();
    HBox DiceBox = new HBox();
    Image blankDice = new Image("images/Dice/dice" + 0 + ".png");
    ImageView viewDice1 = new ImageView(blankDice);
    ImageView viewDice2 = new ImageView(blankDice);
    Dice dice = new Dice();
    PawnCords cordsPlayersMap = new PawnCords();
    Player TourPlayerProfile;
    int position = 1;
    VBox propertyInfo = new VBox();
    ArrayList <BlueRedCards> questionMarkList = initializeRandomCards();
    int buyPropertyMessage = 0;
    int isInPrisonMessage = 0;
    int playerPayedFinePrison = 0;

    public void startTask(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                runTask();
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void runTask(){
        try
        {
            dOut.writeUTF(playerNickname);
            while(true){
                String respond = dIn.readUTF();
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(respond.startsWith("ReadyCheck")){
                            readyButton.setVisible(true);
                            readyCheckInfo.setVisible(true);
                        }
                        else if(respond.startsWith("You"))
                            clientConnected.setText(respond);
                        else if(respond.startsWith("Purchase Property ")){
                            int propId = Integer.parseInt(respond.substring(18));
                            TourPlayerProfile.setCash(TourPlayerProfile.getCash() - propertiesList.get(propId-1).getBuyCost());
                            System.out.println("Player: " + TourPlayerProfile.getPlayerName() + " Bought property: " + propertiesList.get(propId-1).getNameProperty());
                            propertiesList.get(propId-1).setOwnerID(TourPlayerProfile.getPlayerNumber());
                        }
                        else if(respond.startsWith("UpdateMove ")){
                            int newPositionPlayer = Integer.parseInt(respond.substring(11));
                            System.out.println(TourPlayerProfile.getPlayerName() + " movead to property ID: " + newPositionPlayer);
                            TourPlayerProfile.setPropertyId(newPositionPlayer);

                            if (propertiesList.get(TourPlayerProfile.getPropertyId() - 1).getOwnerID() != 0
                                    && propertiesList.get(TourPlayerProfile.getPropertyId() - 1).getOwnerID() != TourPlayerProfile.getPlayerNumber()) {
                                System.out.println( TourPlayerProfile.getPlayerName() + " paying to " + playersList.get(propertiesList.get(newPositionPlayer-1).getOwnerID()-1).getPlayerName());
                                TourPlayerProfile.setCash(TourPlayerProfile.getCash() - propertiesList.get(newPositionPlayer-1).getPaymentForStay());
                                playersList.get(propertiesList.get(newPositionPlayer).getOwnerID()-1).setCash(playersList.get(propertiesList.get(newPositionPlayer-1).getOwnerID()-1).getCash() + propertiesList.get(newPositionPlayer-1).getPaymentForStay());
                            }

                            if(TourPlayerProfile.getPlayerNumber() == 1){
                                player1.setText("Player 1 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + propertiesList.get(newPositionPlayer-1).getNameProperty());
                                pawnView1.setX(cordsPlayersMap.getCorXProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                                pawnView1.setY(cordsPlayersMap.getCorYProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 2){
                                player2.setText("Player 2 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + propertiesList.get(newPositionPlayer-1).getNameProperty());
                                pawnView2.setX(cordsPlayersMap.getCorXProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                                pawnView2.setY(cordsPlayersMap.getCorYProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 3){
                                player3.setText("Player 3 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + propertiesList.get(newPositionPlayer-1).getNameProperty());
                                pawnView3.setX(cordsPlayersMap.getCorXProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                                pawnView3.setY(cordsPlayersMap.getCorYProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 4){
                                player4.setText("Player 4 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + propertiesList.get(newPositionPlayer-1).getNameProperty());
                                pawnView3.setX(cordsPlayersMap.getCorXProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                                pawnView3.setY(cordsPlayersMap.getCorYProperty(newPositionPlayer,TourPlayerProfile.getPlayerNumber()));
                            }

                            Properties actualProperty = propertiesList.get(newPositionPlayer-1);

                            if(playersList.get(actualProperty.getOwnerID()).getPlayerNumber() == 1){
                                player1.setText("Player 1 : " + playersList.get(actualProperty.getOwnerID()).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }

                            else if(playersList.get(actualProperty.getOwnerID()).getPlayerNumber() == 2){
                                player2.setText("Player 2 : " + playersList.get(actualProperty.getOwnerID()).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }

                            else if(playersList.get(actualProperty.getOwnerID()).getPlayerNumber() == 3){
                                player3.setText("Player 3 : " + playersList.get(actualProperty.getOwnerID()).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                            else if(playersList.get(actualProperty.getOwnerID()).getPlayerNumber() == 4){
                                player4.setText("Player 4 : " + playersList.get(actualProperty.getOwnerID()).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                        }
                        else if(respond.startsWith("PlayersInGame")){
                            NubmerOfPlayersInGame = Integer.parseInt(respond.substring(14));

                            if(NubmerOfPlayersInGame >= 2){
                                pawnView1.setVisible(true);
                                pawnView1.setFitWidth(20);
                                pawnView1.setFitHeight(20);
                                pawnView1.setX(cordsPlayersMap.getCorXProperty(1,1));
                                pawnView1.setY(cordsPlayersMap.getCorYProperty(1,1));

                                pawnView2.setVisible(true);
                                pawnView2.setFitWidth(20);
                                pawnView2.setFitHeight(20);
                                pawnView2.setX(cordsPlayersMap.getCorXProperty(1,2));
                                pawnView2.setY(cordsPlayersMap.getCorYProperty(1,2));

                                if(NubmerOfPlayersInGame >= 3){
                                    pawnView3.setVisible(true);
                                    pawnView3.setFitWidth(20);
                                    pawnView3.setFitHeight(20);
                                    pawnView3.setX(cordsPlayersMap.getCorXProperty(1,3));
                                    pawnView3.setY(cordsPlayersMap.getCorYProperty(1,3));
                                    if(NubmerOfPlayersInGame == 4){
                                        pawnView4.setVisible(true);
                                        pawnView4.setFitWidth(20);
                                        pawnView4.setFitHeight(20);
                                        pawnView4.setX(cordsPlayersMap.getCorXProperty(1,4));
                                        pawnView4.setY(cordsPlayersMap.getCorYProperty(1,4));
                                    }
                                }
                            }
                        }
                        else if(respond.startsWith("StartGame") && playersReady == NubmerOfPlayersInGame){
                            int playerId = Integer.parseInt(respond.substring(10));
                            TourPlayerProfile = playersList.get(playerId-1);
                            String tplayerNick = playersList.get(playerId-1).getPlayerName();

                            System.out.println(tplayerNick);
                            System.out.println(playerNickname);
                            System.out.println(playersList.get(0).getPlayerNumber() + " nick: " + playersList.get(0).getPlayerName());
                            System.out.println(playersList.get(1).getPlayerNumber() + " nick: " + playersList.get(1).getPlayerName());

                            box.setVisible(false);
                            panelOponents.setVisible(false);
                            panelTourPlayer.setVisible(false);

                            if(playerNickname.equals(tplayerNick)){
                                viewDice1.setImage(blankDice);
                                viewDice2.setImage(blankDice);
                                diceedInfo.setText("");
                                panelTourPlayer.setVisible(true);
                                readyTour.setVisible(false);
                                dicing.setVisible(true);
                            }
                            else{
                                panelOponents.setVisible(true);
                                GetReadyTourButton.setVisible(true);
                                informationOponentPanel.setText("");
                            }
                        }
                        else if(respond.startsWith("PlayerTourReady")){
                            PlayerToursReady++;
                            if(PlayerToursReady == NubmerOfPlayersInGame){
                                box.setVisible(false);
                                try{
                                    dOut.writeUTF("allPlayersTourReady");
                                }
                                catch(Exception ex){

                                }
                                PlayerToursReady = 0;
                            }
                        }
                        else if(respond.startsWith("PlayerReady")){
                            String respondPlayerReady = respond.substring(11) + " Is ready now!";
                            playersReady++;
                            clientConnected.setText(respondPlayerReady);
                            if(playersReady == NubmerOfPlayersInGame){
                                clientConnected.setVisible(false);
                                readyCheckInfo.setText("All players get ready game is starting...");
                                readyCheckInfo.setTextFill(Color.GREEN);
                            }
                        }
                        else if(respond.startsWith("Players")){
                            clientConnected.setText(respond);
                        }
                        else if(respond.startsWith("Nickname") && NubmerOfPlayersInGame == playersReady){
                            player.setPlayerName(respond.substring(8));
                            //System.out.println("nick: " + player.getPlayerName());
                        }
                        else if(respond.startsWith("NumberPlayer") && NubmerOfPlayersInGame == playersReady){
                            player.setPlayerNumber(Integer.parseInt(respond.substring(12)));
                            //System.out.println("playernumber: " + player.getPlayerNumber());
                        }
                        else if(respond.startsWith("CashPlayer") && NubmerOfPlayersInGame == playersReady){
                            player.setCash(Integer.parseInt(respond.substring(10)));
                            //System.out.println("cash: " + player.getCash());
                        }
                        else if(respond.startsWith("PropertyId") && NubmerOfPlayersInGame == playersReady){
                            player.setPropertyId(Integer.parseInt(respond.substring(10)));
                            String propertyName = propertiesList.get(player.getPropertyId()).getNameProperty();

                            if(player.getPlayerNumber() == 1){
                                String text = "Player 1 : " + player.getPlayerName() + " his cash: " + player.getCash() + "$" + " standing on: " + propertyName;
                                //System.out.println(text);
                                player1.setText(text);
                                p1.setText(text);
                                player1.setVisible(true);
                            }

                            if(player.getPlayerNumber() == 2){
                                String text = "Player 2 : " + player.getPlayerName() + " his cash: " + player.getCash() + "$" + " standing on: " + propertyName;
                                //System.out.println(text);
                                player2.setText(text);
                                p2.setText(text);
                                player2.setVisible(true);
                            }

                            if(player.getPlayerNumber() == 3){
                                String text = "Player 3 : " + player.getPlayerName() + " his cash: " + player.getCash() + "$" + " standing on: " + propertyName;
                                //System.out.println(text);
                                player3.setText(text);
                                p3.setText(text);
                                player3.setVisible(true);
                            }

                            if(player.getPlayerNumber() == 4){
                                String text = "Player 4 : " + player.getPlayerName() + " his cash: " + player.getCash() + "$" + " standing on: " + propertyName;
                                //System.out.println(text);
                                player4.setText(text);
                                p4.setText(text);
                                player4.setVisible(true);
                            }

                            playersList.add(player);
                            player = null;
                            player = new Player();
                            //System.out.println("property: " + respond.substring(10));
                        }
                        else if(respond.startsWith("PropertiesSettings ")){
                            if(respond.startsWith("PropertiesSettings Name")){
                                String convertedRespond = respond.substring(23);
                                property.setNameProperty(convertedRespond);
                            }
                            else if(respond.startsWith("PropertiesSettings payment4Stay")){
                                int convertedRespond = Integer.parseInt(respond.substring(31));
                                property.setPaymentForStay(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings id")){
                                int convertedRespond = Integer.parseInt(respond.substring(21));
                                property.setIDproperty(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings buyCost")){
                                int convertedRespond = Integer.parseInt(respond.substring(26));
                                property.setBuyCost(convertedRespond);
                            }
                            else if(respond.startsWith("PropertiesSettings Country")){
                                String convertedRespond = respond.substring(26);
                                property.setCountryName(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings Owner")){
                                int convertedRespond = Integer.parseInt(respond.substring(24));
                                property.setOwnerID(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings lvl1")){
                                int convertedRespond = Integer.parseInt(respond.substring(23));
                                property.setLvl1(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings lvl2")){
                                int convertedRespond = Integer.parseInt(respond.substring(23));
                                property.setLvl2(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings lvl3")){
                                int convertedRespond = Integer.parseInt(respond.substring(23));
                                property.setLvl3(convertedRespond);
                            }

                            else if(respond.startsWith("PropertiesSettings lvl4")){
                                int convertedRespond = Integer.parseInt(respond.substring(23));
                                property.setLvl4(convertedRespond);
                                propertiesList.add(property);
                                //System.out.println(property.getIDproperty() + " name: " + property.getNameProperty());
                                property = null;
                                property = new Properties();
                            }
                        }

                    }
                });
                System.out.println(respond);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showLogin(){
        Stage stage = new Stage();
        stage.setTitle("DolarBussines Client");
        stage.getIcons().add(new Image("images/DolarBussines.png"));

        TextField tx = new TextField("Player 1");
        tx.setAlignment(Pos.CENTER);
        tx.setMaxWidth(200);
        tx.setPadding(new Insets(10,0,10,0));

        Label info = new Label("");
        info.setTextFill(Color.RED);
        Label label = new Label("Enter your nickname");

        Button b = new Button("Submit and Connect");
        b.setAlignment(Pos.CENTER);

        b.setOnAction(e->{
            createConnection();
            if(socket != null){
                this.playerNickname = tx.getText();
                info.setText("You entered: " + this.playerNickname + " and sucessfully connected");
                showGame();
                stage.close();
            }
            else{
                info.setText("Failure connected\nEnsure server is working.");
            }

        });


        VBox vBox = new VBox();
        vBox.setSpacing(20);

        ObservableList list = vBox.getChildren();

        list.addAll(label, tx, b,info);

        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0,40,0,40));

        Scene sc = new Scene(vBox, 600, 300);

        stage.setScene(sc);
        stage.show();
    }

    public void showGame(){

        readyButton.setVisible(false);
        readyCheckInfo.setVisible(false);

        startTask();
        Stage stage = new Stage();
        stage.setTitle("DolarBussines Game");
        stage.getIcons().add(new Image("images/DolarBussines.png"));
        GridPane grp = new GridPane();

        Image map = new Image("/images/eurobussinesmap.png");

        ImageView mapView = new ImageView(map);
        mapView.setFitHeight(800);
        mapView.setFitWidth(800);
        Pane MapPane = new Pane();

        MapPane.getChildren().add(mapView);
        MapPane.getChildren().add(pawnView1);
        MapPane.getChildren().add(pawnView2);
        MapPane.getChildren().add(pawnView3);
        MapPane.getChildren().add(pawnView4);

        pawnView1.setVisible(false);
        pawnView2.setVisible(false);
        pawnView3.setVisible(false);
        pawnView4.setVisible(false);

        panelOponents.setVisible(false);
        panelTourPlayer.setVisible(false);


        grp.add(MapPane,0,0);
        grp.add(box,1,0);
        grp.add(panelOponents,1,0);
        grp.add(panelTourPlayer,1,0);
        grp.setAlignment(Pos.CENTER);
        grp.setHgap(50);
        box.setSpacing(10);
        Label label = new Label("You connected successfully! Welcone Sir " + this.playerNickname);
        label.setTextFill(Color.GREEN);
        ObservableList list = box.getChildren();
        clientConnected.setText(respondServer);
        list.addAll(label,clientConnected,readyCheckInfo,readyButton);
        readyButton.setOnAction(e->{
            readyCheckInfo.setText("Great you are ready, waiting for other players...");
            playersReady++;
            if(playersReady == NubmerOfPlayersInGame){
                clientConnected.setVisible(false);
                readyCheckInfo.setText("All players get ready game is starting...");
                readyCheckInfo.setTextFill(Color.GREEN);
            }

            readyButton.setVisible(false);
            try{
                dOut.writeUTF("ReadyCheck" + playerNickname);
            }
            catch(Exception ex){

            }

        });
        oponetsMoveScene();
        TourPlayerScene();
        box.setAlignment(Pos.CENTER);
        Scene scena = new Scene(grp,1200,800);
        stage.setScene(scena);
        stage.show();
    }

    public void TourPlayerScene(){
        DiceBox.setAlignment(Pos.CENTER);
        DiceBox.setSpacing(50);
        viewDice1.setFitHeight(50);
        viewDice1.setFitWidth(50);
        viewDice2.setFitHeight(50);
        viewDice2.setFitWidth(50);

        Button BuyPropertyBtn = new Button("Buy");
        Button NoBuyPropertyBtn = new Button("No thanks!");
        Label BuyPoropertyInfo = new Label("");

        VBox buyingPopertyBox = new VBox();
        HBox buyingPorpertyButtons = new HBox();

        buyingPopertyBox.setAlignment(Pos.CENTER);
        buyingPopertyBox.setSpacing(10);
        buyingPorpertyButtons.setAlignment(Pos.CENTER);
        buyingPorpertyButtons.setSpacing(50);

        ObservableList listPropertyBuying = buyingPorpertyButtons.getChildren();
        listPropertyBuying.addAll(BuyPropertyBtn,NoBuyPropertyBtn);

        buyingPopertyBox.getChildren().addAll(BuyPoropertyInfo,buyingPorpertyButtons);


        buyingPopertyBox.setVisible(false);

        propertyInfo.setAlignment(Pos.CENTER);
        propertyInfo.setSpacing(20);

        Image blankProperty = new Image("images/Properties/blank.png");
        ImageView propertyView = new ImageView(blankProperty);
        Label propertyInformation = new Label("You are ane not standing on Buyable property or payable");
        propertyView.setFitWidth(200);
        propertyView.setFitHeight(300);

        ObservableList propertyBox = propertyInfo.getChildren();
        propertyBox.addAll(propertyView,propertyInformation);

        ObservableList diceImages = DiceBox.getChildren();
        diceImages.addAll(viewDice1,viewDice2);

        ObservableList list = panelTourPlayer.getChildren();
        list.addAll(player1,player2,player3,player4,info,dicing,diceedInfo,DiceBox,propertyInfo,buyingPopertyBox,readyTour);

        panelTourPlayer.setAlignment(Pos.CENTER);
//        readyTour.setVisible(false);

        panelTourPlayer.setSpacing(10);
        dicing.setOnAction(e->{
            buyingPopertyBox.setVisible(true);
            int randomCard = dice.throwQuestionMarkCard();
            dice = new Dice();
            int dice1 = dice.throwfunction();
            int dice2 = dice.throwfunction();

            position = position + TourPlayerProfile.getPropertyId() + dice1 + dice2;
            if(position > 40){
                position = position - 40;
            }
            if(TourPlayerProfile.getPlayerNumber() == 1){
                pawnView1.setX(cordsPlayersMap.getCorXProperty(position,1));
                pawnView1.setY(cordsPlayersMap.getCorYProperty(position,1));
            }
            else if(TourPlayerProfile.getPlayerNumber() == 2){
                pawnView2.setX(cordsPlayersMap.getCorXProperty(position,2));
                pawnView2.setY(cordsPlayersMap.getCorYProperty(position,2));
            }
            else if(TourPlayerProfile.getPlayerNumber() == 3){
                pawnView3.setX(cordsPlayersMap.getCorXProperty(position,3));
                pawnView3.setY(cordsPlayersMap.getCorYProperty(position,3));
            }
            else if(TourPlayerProfile.getPlayerNumber() == 4){
                pawnView4.setX(cordsPlayersMap.getCorXProperty(position,4));
                pawnView4.setY(cordsPlayersMap.getCorYProperty(position,4));
            }


            Properties actualProperty = propertiesList.get(position-1);

            if(propertyCardLoadImage(position)){
                propertyInformation.setText("You are standing on buyable property Cost " + actualProperty.getNameProperty() + " is: " + actualProperty.getBuyCost());
                Image propertyCard = new Image("/images/Properties/property-" + position + ".png");
                propertyView.setImage(propertyCard);

            }
            else{
                if(position == 31){
                    if(TourPlayerProfile.getPlayerNumber() == 1){
                        pawnView1.setX(cordsPlayersMap.getCorXProperty(position,1));
                        pawnView1.setY(cordsPlayersMap.getCorYProperty(position,1));
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 2){
                        pawnView2.setX(cordsPlayersMap.getCorXProperty(position,2));
                        pawnView2.setY(cordsPlayersMap.getCorYProperty(position,2));
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 3){
                        pawnView3.setX(cordsPlayersMap.getCorXProperty(position,3));
                        pawnView3.setY(cordsPlayersMap.getCorYProperty(position,4));
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 4){
                        pawnView4.setX(cordsPlayersMap.getCorXProperty(position,4));
                        pawnView4.setY(cordsPlayersMap.getCorYProperty(position,4));
                    }
                    propertyView.setImage(blankProperty);
                    propertyInformation.setText("You go to jail!");
                    position = 11;
                }
                else if(position != 1 &&
                        position != 11 &&
                        position != 21 &&
                        position != 5  &&
                        position != 39){
                    String Cardname = propertiesList.get(position-1).getNameProperty();
                    System.out.println(propertiesList.get(position-1).getNameProperty());
                    if(Cardname.startsWith("Red")){
                        propertyInformation.setText("Red Questionmark Field!");
                        Image RedQuestionMarkCard = new Image("/images/RedBlueCards/RedCards/" + randomCard + ".png");
                        propertyView.setImage(RedQuestionMarkCard);
                    }
                    else if(Cardname.startsWith("Blue")){
                        propertyInformation.setText("Blue Questionmark Field!");
                        Image BlueQuestionMarkCard = new Image("/images/RedBlueCards/BlueCards/" + randomCard + ".png");
                        propertyView.setImage(BlueQuestionMarkCard);
                    }
                    else{
                        propertyInformation.setText("You are ane not standing on Buyable property or payable");
                        propertyView.setImage(blankProperty);
                    }
                }
                else{
                    propertyView.setImage(blankProperty);
                    propertyInformation.setText("You are ane not standing on Buyable property or payable");
                }
            }

            if(propertyBuyable(propertiesList.get(position-1).getNameProperty())){
                if(propertiesList.get(position-1).getOwnerID() == 0){
                    if(TourPlayerProfile.getCash() >= actualProperty.getBuyCost()){
                        BuyPoropertyInfo.setText("Do You want to Buy this property?");
                        BuyPropertyBtn.setVisible(true);
                        NoBuyPropertyBtn.setVisible(true);
                        buyingPopertyBox.setVisible(true);
                        BuyPropertyBtn.setOnAction(event->{

                            buyPropertyMessage = 1;
                            BuyPoropertyInfo.setText("");
                            readyTour.setVisible(true);
                            BuyPropertyBtn.setVisible(false);
                            NoBuyPropertyBtn.setVisible(false);
                            TourPlayerProfile.setCash(TourPlayerProfile.getCash() - actualProperty.getBuyCost());
                            actualProperty.setOwnerID(TourPlayerProfile.getPlayerNumber());
                            propertyInformation.setText("You have purchase " + actualProperty.getNameProperty() + " in country " + actualProperty.getCountryName() + " oponents will pay: " + actualProperty.getPaymentForStay() + "$");

                            if(TourPlayerProfile.getPlayerNumber() == 1){
                                player1.setText("Player 1 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 2){
                                player2.setText("Player 2 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 3){
                                player3.setText("Player 3 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                            else if(TourPlayerProfile.getPlayerNumber() == 4){
                                player4.setText("Player 4 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                            }
                        });
                        NoBuyPropertyBtn.setOnAction(actionEvent -> {
                            BuyPoropertyInfo.setText("");
                            readyTour.setVisible(true);
                            BuyPropertyBtn.setVisible(false);
                            NoBuyPropertyBtn.setVisible(false);
                        });
                    }
                    else{
                        propertyInformation.setText("You can't affor it!");
                        BuyPropertyBtn.setVisible(false);
                        NoBuyPropertyBtn.setVisible(false);
                        buyingPopertyBox.setVisible(false);
                        readyTour.setVisible(true);
                    }
                }
                else if(propertiesList.get(position-1).getOwnerID() == TourPlayerProfile.getPlayerNumber()){
                    BuyPropertyBtn.setVisible(false);
                    NoBuyPropertyBtn.setVisible(false);
                    buyingPopertyBox.setVisible(false);
                    readyTour.setVisible(true);
                    propertyInformation.setText("It's your home!");
                }
                else{
                    BuyPropertyBtn.setVisible(false);
                    NoBuyPropertyBtn.setVisible(false);
                    buyingPopertyBox.setVisible(false);
                    readyTour.setVisible(true);
                    System.out.println( TourPlayerProfile.getPlayerName() + " paying to " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName());
                    TourPlayerProfile.setCash(TourPlayerProfile.getCash() - actualProperty.getPaymentForStay());
                    playersList.get(actualProperty.getOwnerID()-1).setCash(playersList.get(actualProperty.getOwnerID()-1).getCash() + actualProperty.getPaymentForStay());
                    propertyInformation.setText("This property have owner: " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName() + " you have to pay him " + actualProperty.getPaymentForStay() + "$");

                    if(TourPlayerProfile.getPlayerNumber() == 1){
                        player1.setText("Player 1 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 2){
                        player2.setText("Player 2 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 3){
                        player3.setText("Player 3 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }
                    else if(TourPlayerProfile.getPlayerNumber() == 4){
                        player4.setText("Player 4 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }

                    if(playersList.get(actualProperty.getOwnerID()-1).getPlayerNumber() == 1){
                        player1.setText("Player 1 : " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()-1).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }

                    else if(playersList.get(actualProperty.getOwnerID()-1).getPlayerNumber() == 2){
                        player2.setText("Player 2 : " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()-1).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }

                    else if(playersList.get(actualProperty.getOwnerID()-1).getPlayerNumber() == 3){
                        player3.setText("Player 3 : " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()-1).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }
                    else if(playersList.get(actualProperty.getOwnerID()-1).getPlayerNumber() == 4){
                        player4.setText("Player 4 : " + playersList.get(actualProperty.getOwnerID()-1).getPlayerName() + " his cash: " + playersList.get(actualProperty.getOwnerID()-1).getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                    }

                }

            }
            else{
                BuyPropertyBtn.setVisible(false);
                NoBuyPropertyBtn.setVisible(false);
                readyTour.setVisible(true);
            }


            dicing.setVisible(false);
            String dd = "You have diced: " + dice1 + " and " + dice2 + " sum of dice: " + (dice1+dice2);
            Image imageDice1 = new Image("images/Dice/dice" + dice1 + ".png");
            Image imageDice2 = new Image("images/Dice/dice" + dice2 + ".png");
            viewDice1.setImage(imageDice1);
            viewDice2.setImage(imageDice2);
            diceedInfo.setText(dd);






        });

        readyTour.setOnAction(e->{
            PlayerToursReady++;
            SendPlayerTourReadyCurrentPlayer();
            if(PlayerToursReady == NubmerOfPlayersInGame){
                SendAllPlayersReady();
                PlayerToursReady = 0;
                diceedInfo.setText("");
                DiceBox.setVisible(false);
            }
//            panelTourPlayer.setVisible(false);
            readyTour.setVisible(false);
        });
    }

    public void oponetsMoveScene(){
        Label oponentsMoveInfo = new Label("Oponents move click ready button and wait wor your turn!");
        panelOponents.setAlignment(Pos.CENTER);

        ObservableList list = panelOponents.getChildren();
        list.addAll(p1,p2,p3,p4,oponentsMoveInfo,informationOponentPanel,GetReadyTourButton);

        panelOponents.setSpacing(10);
        GetReadyTourButton.setOnAction(e->{
            informationOponentPanel.setText("Waiting for others...");
//            GetReadyTourButton.setVisible(false);
            PlayerToursReady++;
            SendPlayerTourReady();
            if(PlayerToursReady == NubmerOfPlayersInGame){
                SendAllPlayersReady();
                PlayerToursReady = 0;
                informationOponentPanel.setText("");
            }
//            panelOponents.setVisible(false);
            GetReadyTourButton.setVisible(false);
        });
    }

    public void TourPlayerInJailScene(){
        panelTourPlayerInJail.setAlignment(Pos.CENTER);
        Label prisonInfo1 = new Label("You are in prison!");
        Label prisonInfo2 = new Label("You have two options Dice or Pay 400$");
        Label prisonInfo3 = new Label("Dice - if you thow double you will get out jail");
        Label prisonInfo4 = new Label("Pay - you will pay 400$ and you will get out of jail");
        Label prisonInfo5 = new Label("");
        prisonInfo5.setTextFill(Color.RED);
        Label prisonInfo6 = new Label("");
        prisonInfo5.setVisible(false);
        prisonInfo6.setVisible(false);
        HBox decisionButtons = new HBox();
        HBox diceImages = new HBox();
        Image dice1image = new Image("/images/Dice/dice0.png");
        Image dice2image = new Image("/images/Dice/dice0.png");
        ImageView dice1ViewPrison = new ImageView(dice1image);
        ImageView dice2ViewPrison = new ImageView(dice2image);
        dice1ViewPrison.setFitHeight(50);
        dice1ViewPrison.setFitWidth(50);
        dice2ViewPrison.setFitHeight(50);
        dice2ViewPrison.setFitWidth(50);
        diceImages.setAlignment(Pos.CENTER);
        diceImages.setSpacing(50);
        decisionButtons.setSpacing(70);
        Button diceButton = new Button("Dice!");
        Button payButton = new Button("Pay!");
        decisionButtons.getChildren().addAll(diceButton,payButton);

        ObservableList componentsList = panelTourPlayerInJail.getChildren();
        componentsList.addAll(prisonInfo1,prisonInfo2,prisonInfo3,prisonInfo4,prisonInfo5,diceImages,prisonInfo6,readyTour);

        payButton.setOnAction(actionEvent -> {
            if(TourPlayerProfile.getCash() >= 400){
                Properties actualProperty = propertiesList.get(TourPlayerProfile.getPropertyId()-1);
                payButton.setVisible(false);
                diceButton.setVisible(false);
                prisonInfo6.setText("Great you have payed 400$ and now you are free");
                TourPlayerProfile.setCash(TourPlayerProfile.getCash() - 400);
                TourPlayerProfile.setInJail(false);

                if(TourPlayerProfile.getPlayerNumber() == 1){
                    player1.setText("Player 1 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                }
                else if(TourPlayerProfile.getPlayerNumber() == 2){
                    player2.setText("Player 2 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                }
                else if(TourPlayerProfile.getPlayerNumber() == 3){
                    player3.setText("Player 3 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                }
                else if(TourPlayerProfile.getPlayerNumber() == 4){
                    player4.setText("Player 4 : " + TourPlayerProfile.getPlayerName() + " his cash: " + TourPlayerProfile.getCash() + "$" + " standing on: " + actualProperty.getNameProperty());
                }

                readyTour.setVisible(true);
                playerPayedFinePrison = 1;
                isInPrisonMessage = 0;
            }
            else{
                prisonInfo5.setText("You don't have 400$ you have to Dice!");
            }
        });

        diceButton.setOnAction(actionEvent -> {
            dice = new Dice();
            int dice1 = dice.throwfunction();
            int dice2 = dice.throwfunction();
            Image dicePrison1 = new Image("/images/Dice/" + dice1 + ".png");
            Image dicePrison2 = new Image("/images/Dice/" + dice2 + ".png");

            dice1ViewPrison.setImage(dicePrison1);
            dice2ViewPrison.setImage(dicePrison2);

            if(dice1 == dice2){
                prisonInfo6.setText("You have Double and now you are free");
                TourPlayerProfile.setInJail(false);
                isInPrisonMessage = 0;
            }
            else {
                prisonInfo6.setText("Good luck next time!");
                isInPrisonMessage = 1;
            }

            payButton.setVisible(false);
            diceButton.setVisible(false);
            readyTour.setVisible(true);
            playerPayedFinePrison = 0;
        });

        readyTour.setOnAction(actionEvent -> {
            PlayerToursReady++;
            SendPlayerTourReadyCurrentPlayer();
            if(PlayerToursReady == NubmerOfPlayersInGame){
                SendAllPlayersReady();
                PlayerToursReady = 0;
                diceedInfo.setText("");
                DiceBox.setVisible(false);
            }
//            panelTourPlayer.setVisible(false);
            readyTour.setVisible(false);
        });


    }


    public static void createConnection(){
        try {
            socket = new Socket("localhost", 2115);
            dOut = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());

        }
        catch (IOException exc) {
        }
    }
    public void SendPlayerTourReady(){
        try{
            dOut.writeUTF("PlayerTourReady");
        }
        catch (Exception ex){

        }
    }
    public void SendPlayerTourReadyCurrentPlayer(){
        try{
            dOut.writeUTF("PlayerTourReady 0 0 0 0 0 " + buyPropertyMessage + " " + TourPlayerProfile.getPlayerNumber() + " " + position);
        }
        catch (Exception ex){

        }
        buyPropertyMessage = 0;
    }
    public void SendAllPlayersReady(){
        try{
            dOut.writeUTF("allPlayersTourReady");
        }
        catch (Exception ex){

        }
    }

    public boolean propertyCardLoadImage(int propertyNumber){
        if(propertyNumber != 1 &&
                propertyNumber != 3 &&
                propertyNumber != 8 &&
                propertyNumber != 11 &&
                propertyNumber != 18 &&
                propertyNumber != 21 &&
                propertyNumber != 23 &&
                propertyNumber != 31 &&
                propertyNumber != 34 &&
                propertyNumber != 37)
            return true;
        else
            return false;
    }

    public static boolean propertyBuyable(String property){
        if(property.startsWith("Blue")
                || property.startsWith("Red") || property.startsWith("Podatek") ||
                property.startsWith("Parking") || property.startsWith("Wiezienie") ||
                property.startsWith("Go!") || property.startsWith("Idziesz") ||
                property.startsWith("Darmowy"))
            return false;
        else
            return true;
    }

    public static ArrayList<BlueRedCards> initializeRandomCards(){
        ArrayList<BlueRedCards> blueRedCardsInitial = new ArrayList<>();

        BlueRedCards card1 = new BlueRedCards(1,"Wracasz do Mardrytu",0,0,0,0,15);
        BlueRedCards card2 = new BlueRedCards(2,"Bank wyplaca ci procenty w wysokosci 100$",100,0,0,0,0);
        BlueRedCards card3 = new BlueRedCards(3,"Idziesz do Kolei Wschodnich, jezeli przechodzisz przez start otrymujesz 400$!",0,0,0,0,16);
        BlueRedCards card4 = new BlueRedCards(4,"Bank wyplaca ci nalezne odsetki w wyskości 300$",300,0,0,0,0);
        BlueRedCards card5 = new BlueRedCards(5,"Idziesz do Brukseli, jezeli przechodzisz przez start otrzymujesz 400$",0,0,0,0,24);
        BlueRedCards card6 = new BlueRedCards(6,"Piles w czasie pracy placisz 40$",0,40,0,0,0);
        BlueRedCards card7 = new BlueRedCards(7,"Idziesz do Neapolu, jezeli przechodzisz przez start otrzymujesz 400$",0,0,0,0,7);
        BlueRedCards card8 = new BlueRedCards(8,"Placisz oplate za szkole 300$",0,300,0,0,0);
        BlueRedCards card9 = new BlueRedCards(9,"Idziesz do wiezienia!",0,0,0,0,31);
        BlueRedCards card10 = new BlueRedCards(10,"Rozwiazales dobrze krzyzowke otrzymujesz 200$",200,0,0,0,0);
        BlueRedCards card11 = new BlueRedCards(11,"Wracasz na Start",0,0,0,0,1);
        BlueRedCards card12 = new BlueRedCards(12,"Cofasz sie o 3 pola.",0,0,0,3,0);
        BlueRedCards card13 = new BlueRedCards(13,"Idziesz 3 pola dalej",0,0,3,0,0);
        BlueRedCards card14 = new BlueRedCards(14,"Mandat za szybka jazde 30$.",0,30,0,0,0);
        BlueRedCards card15 = new BlueRedCards(15,"Bank pomylil sie na twoja korzysc otrzymujesz 400$.",400,0,0,0,0);
        BlueRedCards card16 = new BlueRedCards(16,"Wracasz do Wiednia",0,0,0,0,40);

        blueRedCardsInitial.add(card1);
        blueRedCardsInitial.add(card2);
        blueRedCardsInitial.add(card3);
        blueRedCardsInitial.add(card4);
        blueRedCardsInitial.add(card5);
        blueRedCardsInitial.add(card6);
        blueRedCardsInitial.add(card7);
        blueRedCardsInitial.add(card8);
        blueRedCardsInitial.add(card9);
        blueRedCardsInitial.add(card10);
        blueRedCardsInitial.add(card11);
        blueRedCardsInitial.add(card12);
        blueRedCardsInitial.add(card13);
        blueRedCardsInitial.add(card14);
        blueRedCardsInitial.add(card15);
        blueRedCardsInitial.add(card16);

        return blueRedCardsInitial;
    }
}

package Client2;

import Source.Dice;
import Source.Player;
import Source.Properties;
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
                        else if(respond.startsWith("PlayersInGame")){
                            NubmerOfPlayersInGame = Integer.parseInt(respond.substring(14));

                            if(NubmerOfPlayersInGame >= 2){
                                pawnView1.setVisible(true);
                                pawnView1.setFitWidth(20);
                                pawnView1.setFitHeight(20);
                                pawnView1.setX(730);
                                pawnView1.setY(740);

                                pawnView2.setVisible(true);
                                pawnView2.setFitWidth(20);
                                pawnView2.setFitHeight(20);
                                pawnView2.setX(760);
                                pawnView2.setY(740);

                                if(NubmerOfPlayersInGame >= 3){
                                    pawnView3.setVisible(true);
                                    pawnView3.setFitWidth(20);
                                    pawnView3.setFitHeight(20);
                                    pawnView3.setX(730);
                                    pawnView3.setY(770);
                                    if(NubmerOfPlayersInGame == 4){
                                        pawnView4.setVisible(true);
                                        pawnView4.setFitWidth(20);
                                        pawnView4.setFitHeight(20);
                                        pawnView4.setX(760);
                                        pawnView4.setY(770);
                                    }
                                }
                            }
                        }
                        else if(respond.startsWith("StartGame") && playersReady == NubmerOfPlayersInGame){
                            int playerId = Integer.parseInt(respond.substring(10));
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

        ObservableList diceImages = DiceBox.getChildren();
        diceImages.addAll(viewDice1,viewDice2);

        ObservableList list = panelTourPlayer.getChildren();
        list.addAll(player1,player2,player3,player4,info,dicing,diceedInfo,DiceBox,readyTour);

        panelTourPlayer.setAlignment(Pos.CENTER);
//        readyTour.setVisible(false);

        panelTourPlayer.setSpacing(10);

        dicing.setOnAction(e->{
            dice = new Dice();
            int dice1 = dice.throwfunction();
            int dice2 = dice.throwfunction();
            dicing.setVisible(false);
            String dd = "You have diced: " + dice1 + " and " + dice2 + " sum of dice: " + (dice1+dice2);
            Image imageDice1 = new Image("images/Dice/dice" + dice1 + ".png");
            Image imageDice2 = new Image("images/Dice/dice" + dice2 + ".png");
            viewDice1.setImage(imageDice1);
            viewDice2.setImage(imageDice2);
            diceedInfo.setText(dd);
            readyTour.setVisible(true);
        });

        readyTour.setOnAction(e->{
            PlayerToursReady++;
            SendPlayerTourReady();
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
    public void SendAllPlayersReady(){
        try{
            dOut.writeUTF("allPlayersTourReady");
        }
        catch (Exception ex){

        }
    }
}

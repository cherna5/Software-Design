package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.embed.swing.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

import java.util.Map.Entry;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Main extends Application {
    private NetworkConnectionClient connClient = null;
    private NetworkConnectionServer connServer = null;

    private Integer totalClients;
    private Integer clientNum = -1;
    private Text getReady = new Text();
    private Boolean turnPlayer = false;
    private GraphicsContext graphicsContext = null;
    private TextArea textArea = new TextArea();
    private TextField textField = new TextField();
    private HBox hmm;
    private Button go;
    private Stage mainStage;
    private ArrayList<String> playerPoints = new ArrayList<String>();

    private Button clearButton = new Button("Clear");
    private String nullNameOfPlayer = "";
    private String nameOfPlayer = "";

    //background for stages
    private Image waitingImage = new Image(getClass().getResourceAsStream("/pictures/waitingCanvas.jpg"), 800,500,true,true);
    private Image homeImage = new Image(getClass().getResourceAsStream("/pictures/canvasDrawing.jpeg"), 800,500,true,true);
    private Image loadingGif = new Image(getClass().getResourceAsStream("/pictures/loading.gif"));

    private ArrayList<Image> playerImageIcons = new ArrayList<>();
    private ArrayList<Button> playerButtonIcons = new ArrayList<>();
    private HashMap<Button, String> playerMapIcons = new HashMap<>();

    private Button howToPlay = new Button("How to play");
    private Button playButton = new Button("Play!");
    private Button nextRound = new Button("Who won?");
    private Button nextPlayer = new Button("Next Round");
    private Text inCaseNoOneWonText = new Text("");
    private ArrayList<RadioButton> levelButtons = new ArrayList<>();
    private HashMap<RadioButton, String> radioButtonStringHashMap = new HashMap<>();
    private ArrayList<ImageView> levelImages = new ArrayList<>();
    //members used to set the default settings for lobby

    private Canvas canvas = null;
    //private GraphicsContext graphicsContext;
    private Scanner x;
    private Random randomIndex = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private int levelChecker;
    private boolean outOfTime = false;

    //Implementation of the timer variables
    private static final Integer countDown = 30;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private AnimationTimer timer;
    private Duration duration;
    private static final int SECONDS_PER_DAY    = 86_400;
    private static final int SECONDS_PER_HOUR   = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private long lastTimerCall;

    private boolean isDrawer = false;
    //-------------------------------------------------------------------------------------
    // connection items
    //private NetworkConnection conn = null;
    private Integer turnCounter = 1;
    private int[] playerPointsServer = {0, 0, 0, 0};

    // header
    private Text header = new Text("SERVER");
    private Text serverOnOff = new Text("Turn On/Off Server: ");

    // turn server on and off
    private RadioButton On = new RadioButton("On");
    private RadioButton Off = new RadioButton("Off");
    private ToggleGroup OnOff = new ToggleGroup();

    // select the port
    private Text slctPort = new Text("Select Port: ");
    ObservableList<String> options =
            FXCollections.observableArrayList(
                    "5550", "5560", "5570", "5580"
            );
    private ComboBox getPort = new ComboBox(options);
    //-------------------------------------------------------------------------------------

    private void openFile() {
        try {
            //Compiling it on another persons computer will most likely result in an error
            //Hover over the file with the name "word.txt"
            //Right click on that .txt file
            //Then click "Copy Path"
            //Paste that path within the quotation marks: new File("paste here!!!");
            x = new Scanner(new File("/Users/cherna/Desktop/Demo/src/pictures/words.txt"));
        }
        catch(Exception e) {
            System.out.println("Could not find file");
        }
    }

    private void readFile() {
        while(x.hasNext()) {
            wordList.add(x.next().toUpperCase());
        }
    }


    private void closeFile() {
        x.close();
    }

    //---------------------FIRST SCENE-------------------------------------------------------------------------------
    private Scene homeStage(Stage mainStage){
        //the title of the game and entering name
        Text gameTitle = new Text("GuessIt Now");
        Text playerName = new Text("Enter Name: ");
        TextField playerNameField = new TextField();
        //editing the text
        playerName.setFill(Color.DARKSALMON);
        playerName.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 15));
        gameTitle.setFill(Color.DARKSALMON);
        gameTitle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 50));
        gameTitle.setRotate(-30);
        Effect glow = new Glow(2.0);
        gameTitle.setEffect(glow);

        //this allows it to be a pop-up text when you hover over the text
        gameTitle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gameTitle.setScaleX(1.5);
                gameTitle.setScaleY(1.5);
            }
        });
        gameTitle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gameTitle.setScaleX(1);
                gameTitle.setScaleY(1);
            }
        });

        //buttons that will be displayed on the home (login in) stage
        Button playButton = new Button("Start");

        //styling the buttons
        playButton.setStyle("-fx-font: 15 Verdana; -fx-base: #eebe92;");
        howToPlay.setStyle("-fx-font: 15 Verdana; -fx-base: #eebe92;");

        //instructions on how to play if the button is pressed
        howToPlay.setOnAction(e-> {
            Alert HowTo = new Alert(AlertType.INFORMATION);
            // HowTo.setHeaderText("How to play");
            HowTo.setTitle("How to play?");
            HowTo.setHeaderText(null);
            HowTo.setContentText("You will have to wait for 3 more players to join, and then the game will start! " +
                    "Once the game starts, you or the other 3 players will be chosen at random to draw a picture. " +
                    "Whoever is chosen has 1 minute to draw a word and have to input the word they are drawing. " +
                    "Once the player who was chosen to draw is done, the other three players have 30 seconds to guess the drawing. " +
                    "The player(s) to guess it right after 30 seconds, will win the round. " +
                    "The objective is to get to 5 points, each round won is 1 point.");
            HowTo.showAndWait();
        });

        //if the play button is pressed ...
        playButton.setOnAction(e->{
            //first check if the user inputted a name, if not error else go to a new scene
            Alert noName = new Alert(AlertType.ERROR);
            if(playerNameField.getText().equals(nullNameOfPlayer)){
                noName.setTitle(null);
                noName.setHeaderText(null);
                noName.setContentText("Please enter a name!");
                noName.showAndWait();
            }
            //new scene and close old one
            else{
                nameOfPlayer = playerNameField.getText();
                //System.out.println(nameOfPlayer);
                //go to the next scene, waiting scene
                mainStage.close();
                mainStage.setScene(selectIcon(mainStage));
                mainStage.show();
            }

        });

        //if Enter is pressed ...
        playerNameField.setOnAction(e -> {
            //first check if the user inputted a name, if not error else go to a new scene
            Alert noName = new Alert(AlertType.ERROR);
            if(playerNameField.getText().equals(nullNameOfPlayer)){
                noName.setTitle(null);
                noName.setHeaderText(null);
                noName.setContentText("Please enter a name!");
                noName.showAndWait();
            }
            //new scene and close old one
            else{
                nameOfPlayer = playerNameField.getText();
                //System.out.println(nameOfPlayer);
                //go to the next scene, waiting scene
                mainStage.close();
                mainStage.setScene(selectIcon(mainStage));
                mainStage.show();
            }
        });

        VBox nameBox = new VBox(playerName, playerNameField, playButton);

        //Creating a Grid Pane & editing the pane
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(690, 550);
        gridPane.setBackground(new Background(new BackgroundImage(homeImage,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(gameTitle, 3,25);
        gridPane.add(nameBox, 6,24);
        gridPane.add(howToPlay, 10, 65);

        return new Scene(gridPane);
    }

    //--------------------SECOND SCENE----------------------------------------------------------------------------------------
    private Scene selectIcon(Stage mainStage){
        //give the title of the player's name
        mainStage.setTitle("Welcome " + nameOfPlayer);
        Text chooseCharacterText = new Text("Choose your character!");

        chooseCharacterText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        chooseCharacterText.setFill(Color.SPRINGGREEN);
        chooseCharacterText.setStrokeWidth(1);
        chooseCharacterText.setStroke(Color.BLACK);

        //give the title of the player's name
        mainStage.setTitle("Welcome " + nameOfPlayer);
        ImageView loadScreen = new ImageView(loadingGif);
        loadScreen.setFitHeight(50);
        loadScreen.setFitWidth(50);
        loadScreen.setPreserveRatio(true);

        HBox buttonBox = new HBox();
        for(Button b : playerButtonIcons)
            buttonBox.getChildren().add(b);

        for(Button b : playerMapIcons.keySet()) {
            b.setOnAction(e -> {
                try {
                    connClient = createClient("127.0.0.1", "5580");
                    connClient.startConn();
                    if (clientNum < 5) {
                        mainStage.close();
                        mainStage.setScene(waitingLobby(mainStage, b));
                        mainStage.show();
                    }
                } catch (Exception o) {
                    o.printStackTrace();
                }
            });
        }

        //buttonBox.setPadding(new Insets(15, 12, 15, 12));
        VBox displayButtonAndText = new VBox(chooseCharacterText, buttonBox);
        displayButtonAndText.setAlignment(Pos.TOP_CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundImage(homeImage,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));

        gridPane.setMinSize(690, 550);
        gridPane.add(displayButtonAndText, 5,2);
        gridPane.add(loadScreen, 0,0);

        //Creating a scene object
        return new Scene(gridPane);
    }

//--------------------THIRD SCENE-----------------------------------------------------------------------------------------
    //NEEDS UPDATING
    /*
            picking someone to draw happens in this scene
     */

    private Scene waitingLobby(Stage mainStage, Button characterButton) {
        characterButton.setOnAction(e -> {/*Does Nothing*/});

        playButton.setOnAction(e -> {
            mainStage.close();
            mainStage.setScene(drawerScene(mainStage));
            mainStage.show();
        });

        playButton.setDisable(true);

        getReady.setText("Waiting for More Players");

        //Possible game mechanic (not likely)
        Text increasingLevels = new Text("Remember, as the game progresses, the game \n" +
                "gets slightly harder.");

        getReady.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        getReady.setFill(Color.DARKSALMON);
        getReady.setStrokeWidth(1);
        getReady.setStroke(Color.BLACK);

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(690, 550);

        VBox poop2 = new VBox(getReady);
        poop2.setSpacing(20);
        poop2.setAlignment(Pos.TOP_CENTER);

        HBox poop4 = new HBox(characterButton, playButton);
        poop4.setSpacing(200);
        poop4.setAlignment(Pos.CENTER);

        VBox root = new VBox(poop2, poop4, levelImages.get(levelChecker));
        root.setSpacing(40);
        characterButton.setText("This is you!");

        gridPane.setBackground(new Background(new BackgroundImage(homeImage,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));

        root.setAlignment(Pos.CENTER);
        gridPane.add(root, 100,100);

        return new Scene(gridPane,690, 550);
    }

/*
        THIS PART OF THE CODE WILL HAVE TO BE DETERMINED BY RANDOMIZATION.
            1.) ONE PLAYER WILL BE CHOSEN TO DRAW A PICTURE
                    * ONCE THE PICTURE IS DRAWN, SEND BUTTON WILL ALLOW THE OTHER PLAYERS TO SEE
                    * THEY WILL THEN SEE A TIMER, OTHER PLAYERS HAVE 30 SECONDS TO GUESS
            2.) THE REMAINING PLAYERS WILL BE SEE A TIMER & WILL WAIT FOR THE PICTURE TO BE DISPLAYED
                    * THEY WILL HAVE 30 SECONDS TO WAIT
                    * ONCE CANVAS IS DISPLAYED, THEY WILL HAVE 30 SECONDS TO GUESS
 */


    //---------------------DRAWER SCENE-------------------------------------------------------------------------------
    private Scene drawerScene(Stage mainStage) {
        canvas = new Canvas(400, 400);
        graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        //Implementing Timer-----------------------------------------------
        lastTimerCall = System.nanoTime();
        duration = Duration.seconds(countDown+1);

        IntegerProperty second = new SimpleIntegerProperty(countDown);

        Label timerLabel = new Label();
        timerLabel.textProperty().bind(second.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 4em;");

        second.set(countDown);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(countDown+1),
                        new KeyValue(second, 0)));
        timeline.playFromStart();
        //-------------------------------------------------------------------

        int wordIndex;
        Text drawThisWordText;
        Text playersChosenWord;
        Text waitingForPictureText = new Text("Wait until the drawer sends their picture");
        Text timeIsUp = new Text("");

        Button startDrawing = new Button("Draw!");
        Button clearCanvas = new Button("Clear");
        Button sendPicture = new Button("Send");
        Button skipButton = new Button("Skip");
        nextRound = new Button("Who won?");
        nextRound.setVisible(false);

        sendPicture.setDefaultButton(true);

        textField.setOnAction(e -> {
            if(!textField.getText().equals("")) {
                String message = "ZXCVB" + nameOfPlayer + ": " + textField.getText() + "\n";
                textField.clear();
                try {
                    connClient.send(message);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        skipButton.setOnAction(e -> {
            mainStage.setScene(drawerScene(mainStage));
            mainStage.show();
        });

        clearCanvas.setOnAction(e -> {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });

        VBox poop = new VBox();
        if(turnPlayer) {
            //Drawers will see the canvas
            canvas.setVisible(true);
            skipButton.setDisable(false);

            wordIndex = 1 + randomIndex.nextInt(wordList.size()-1);
            drawThisWordText = new Text("Draw this word! ");
            playersChosenWord = new Text(wordList.get(wordIndex) + " ");

            drawThisWordText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            drawThisWordText.setFill(Color.CORNFLOWERBLUE);
            drawThisWordText.setStrokeWidth(1);
            drawThisWordText.setStroke(Color.BLACK);

            playersChosenWord.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            playersChosenWord.setFill(Color.DARKGREEN);
            playersChosenWord.setStrokeWidth(1);
            playersChosenWord.setStroke(Color.BLACK);

            sendPicture.setOnAction(e -> {
                sendPicture.setDisable(true);
                timeline.stop();
                timeline.pause();

                nextRound.setVisible(true);

                SnapshotParameters parameters = new SnapshotParameters();
                WritableImage wi = new WritableImage(400, 400);
                WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), wi);
                BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", os);
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

                byte[] bytes = os.toByteArray();
                try {
                    connClient.send(bytes);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

//                try {
//                    Thread.sleep(1000);
//                    connClient.send("startguessing");
//                } catch (Exception hmm) {
//                    // TODO Auto-generated catch block
//                    hmm.printStackTrace();
//                }
            });

            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            skipButton.setDisable(true);
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }
                    });
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                    new EventHandler<MouseEvent>(){

                        @Override
                        public void handle(MouseEvent event) {
                            graphicsContext.lineTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }
                    });
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                    new EventHandler<MouseEvent>(){

                        @Override
                        public void handle(MouseEvent event) {

                        }
                    });

            nextRound.setOnAction(e -> {
                Stage nr = new Stage();
                nr.setScene(new Scene(nextRoundScene(mainStage)));
                nr.show();
            });


            nextPlayer.setOnAction(e -> {
                outOfTime = false;
                try {
                    //Thread.sleep(3000);
                    connClient.send("timesup");
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });

            nextPlayer.setVisible(false);


            // When timer hits zero, another drawer is chosen
            timeline.setOnFinished(e -> {
                canvas.setVisible(false);
                skipButton.setVisible(false);
                clearCanvas.setVisible(false);
                sendPicture.setVisible(false);

                outOfTime = true;
                nextPlayer.setVisible(true);
                nextRound.setVisible(true);

                timeIsUp.setText("Time is up!!!");
                timeIsUp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                timeIsUp.setFill(Color.DARKSALMON);
                timeIsUp.setStrokeWidth(1);
                timeIsUp.setStroke(Color.BLACK);

                inCaseNoOneWonText = new Text("If no one won, press this button");
            });

            second.set(countDown);
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(countDown+1),
                            new KeyValue(second, 0)));
            timeline.playFromStart();

            HBox yup = new HBox(nextRound, inCaseNoOneWonText);

            poop.getChildren().addAll(textArea, textField, timerLabel, timeIsUp, nextPlayer, yup);
        }
        else{
//          Guessers will not see the canvas during the round
//          They will also not see the buttons being used or...
//                the word being drawn.
            canvas.setVisible(false);
            skipButton.setVisible(false);
            clearCanvas.setVisible(false);
            sendPicture.setVisible(false);
            sendPicture.setDisable(false);
            drawThisWordText = new Text("");
            playersChosenWord = new Text("");
            poop.getChildren().addAll(textArea, textField, waitingForPictureText);
        }

        HBox poop0 = new HBox(sendPicture, skipButton, clearCanvas, howToPlay);
        HBox poop1 = new HBox(drawThisWordText, playersChosenWord);
        VBox poop2 = new VBox(canvas, poop1, poop0);
        HBox hmm = new HBox(poop2, poop);

        poop0.setSpacing(20);

        return new Scene(hmm, 690, 550);
    }

    //----------------------GUESSER SCENE---------------------------------------------------------------------------------
    private Scene Guesser(Stage mainStage){
        Button imageOfCanvas = new Button();
        imageOfCanvas.setMinSize(400,400);
        Text guessWord = new Text("Enter your guess here: ");
        guessWord.setFill(Color.DARKSALMON);
        guessWord.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 15));
        TextField guessIt = new TextField();


        /*
                BACK-END: please put the image from canvas HERE!
         */
        //imageOfCanvas.setGraphic(new ImageView());


        //sending the string they decided to send to compare if they win
        guessIt.setOnAction(e->{
            String theGuess = guessIt.getText();
            try {
                connClient.send(theGuess);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });

        VBox guessEm = new VBox(imageOfCanvas, guessWord,guessIt);
        return new Scene(guessEm, 400,450);

    }


    private Scene timer(Stage mainStage){
        Button goGuess = new Button("GUESSING?");
        lastTimerCall = System.nanoTime();
        duration = Duration.hours(72);
        mainStage.setTitle("Timer");

        goGuess.setDisable(true);

        //timer w/ binding
        IntegerProperty second = new SimpleIntegerProperty(countDown);

        timerLabel.textProperty().bind(second.asString());
        timerLabel.setTextFill(Color.WHITESMOKE);
        timerLabel.setStyle("-fx-font-size: 4em;");

        Button button = new Button("Start Timer");
        button.setOnAction(e->{
            if (timeline != null) {
                timeline.stop();
            }
            second.set(countDown);
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(countDown+1),
                            new KeyValue(second, 0)));
            timeline.playFromStart();
            goGuess.setDisable(false);
        });
        goGuess.setOnAction(e->{
            mainStage.close();
            mainStage.setScene(Guesser(mainStage));
            mainStage.show();
        });

        VBox timerB = new VBox(20);
        timerB.setAlignment(Pos.CENTER);
        //timerB.setPrefWidth(scene.getWidth());
        timerB.getChildren().addAll(button, timerLabel, goGuess);
        timerB.setLayoutY(30);

        timerB.setBackground(new Background(new BackgroundFill(
                Color.rgb(10, 10, 20), CornerRadii.EMPTY, Insets.EMPTY)));

        return new Scene(timerB, 200,300);
    }
//-------------------HELPER FUNCTIONS-------------------------------------------------------------------------------------------

    public Parent nextRoundScene(Stage mainStage) {
        Text winner = new Text("Winner: ");
        TextField field = new TextField();
        Button go = new Button("Go");

        go.setOnAction(e -> {
            try {
                connClient.send("Winner^" + field.getText());
                Stage stage = (Stage) go.getScene().getWindow();
                stage.close();
                //mainStage.setScene(drawerScene(mainStage));
                //go.setDisable(true);
                //Stage stage = (Stage) nextRound.getScene().getWindow();
                //Stage stage2 = (Stage) go.getScene().getWindow();
                //stage.close();
                //stage2.close();
                //mainStage.show();

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        VBox root = new VBox(20, winner, field, go);

        return root;
    }

    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,               //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,        //width of the rectangle
                canvasHeight);      //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

    }

    public void startServer() {
        String port = "5580";
        connServer = createServer(port);
        try{
            connServer.startConn();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void initPrivate() {
        openFile(); readFile(); closeFile();
        startServer();

        textArea.setEditable(false); textArea.setMaxWidth(500);

        for(int i = 1; i <= 3; i++)
            levelImages.add(new ImageView(new Image(getClass().getResourceAsStream("/pictures/Level" + i + ".jpg"), 100,100,true,true)));

        for(int i = 1; i <= 4; i++) {
            playerImageIcons.add(new Image(getClass().getResourceAsStream("/pictures/iconplayer" + i + ".jpg"), 100,100,true,true));
            playerButtonIcons.add(new Button());
            playerButtonIcons.get(i-1).setGraphic(new ImageView(playerImageIcons.get(i-1)));
            playerMapIcons.put(playerButtonIcons.get(i-1), "/pictures/iconplayer" + i + ".jpg");
        }
    }

    //---------------START OF THE ENTIRE OF PROJECT----------------------------------------------------------------------------------
    @Override
    public void start(Stage primaryStage) {
        initPrivate();

        primaryStage.setScene(homeStage(primaryStage));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception{
    }

    @Override
    public void stop() throws Exception{
        connClient.closeConn();
    }

    //------------------CLIENT DATA HANDLED--------------------------------------------------------------------------------------------
    private Client createClient(String ip, String port) {
        return new Client(ip, Integer.parseInt(port), data -> {
            Platform.runLater(()->{
                if (data.toString().intern().contains("NewClientSize")) {
                    String data_s = data.toString().intern();
                    String[] data_split = data_s.split(" ");
                    if (clientNum == -1) {
                        clientNum = Integer.parseInt(data_split[1]);
                        connClient.setClientNum(clientNum);
                        try {
                            connClient.send("NameOfPlayer^" + String.valueOf(clientNum) + "^" + nameOfPlayer);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    totalClients = Integer.parseInt(data_split[1]);
                    connClient.updateTotalClients(Integer.parseInt(data_split[1]));

                    if(clientNum > 4) {
                        Alert z = new Alert(AlertType.ERROR);
                        z.setHeaderText("Game Full");
                        z.setContentText("Sorry, the game is full.");
                        z.showAndWait();
                        try {
                            connClient.send("Full");
                            connClient.closeConn();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Platform.exit();
                    }
                    if (totalClients == 4) {
                        getReady.setText("Ready to Play! Click to Start");
                        playButton.setDisable(false);
                        if (clientNum == 1) {
                            turnPlayer = true;
                        }
                    }
                }

//                else if (data.toString().intern().contains("startguessing")) {
//                    timeline.playFromStart();
//                }

                else if (data.toString().intern().contains("ZXCVB")) {
                    String[] data_split = data.toString().intern().split("ZXCVB");
                    textArea.appendText(data_split[1]);
                }

                else if (data.toString().intern().equals("yourturn")) {
                    turnPlayer = true;
                    Stage stage = (Stage) canvas.getScene().getWindow();
                    stage.close();
                    Stage next = new Stage();
                    next.setScene(drawerScene(mainStage));
                    next.show();
                    //Stage stage = (Stage) nextRound.getScene().getWindow();
                    //Stage stage2 = (Stage) go.getScene().getWindow();
                    //stage.close();
                    //stage2.close();

                }

                else if (data.toString().intern().equals("notyourturn")) {
                    turnPlayer = false;
                    Stage stage = (Stage) canvas.getScene().getWindow();
                    stage.close();
                    Stage next = new Stage();
                    next.setScene(drawerScene(mainStage));
                    next.show();
                    //Stage stage = (Stage) nextRound.getScene().getWindow();
                    //Stage stage2 = (Stage) go.getScene().getWindow();
                    //stage.close();
                    //stage2.close();
                }

                else if (data.toString().intern().contentEquals("Connection Closed")) {
                    System.out.println("Connection has been closed.");
                }

                else if (data.toString().intern().contains("WinnerWinnerChickenDinner")) {
                    String[] data_split = data.toString().intern().split(" ");
                    String winner = data_split[1];

                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Next Round");
                    a.setHeaderText(winner + " won! They're up next.");
                    a.setContentText(
                            "Points: " + playerPoints.get(0) + ": " + playerPoints.get(1) + " " + playerPoints.get(2) + ": " + playerPoints.get(3) + " " + playerPoints.get(4) + ": " + playerPoints.get(5) + " " + playerPoints.get(6) + ": " + playerPoints.get(7)
                    );
                    a.showAndWait();
                }

                else if (data.toString().intern().contains("UpdatedPlayerPoints")) {
                    String[] data_split = data.toString().intern().split(" ");
                    String player1 = data_split[1];
                    String player1points = data_split[2];
                    String player2 = data_split[3];
                    String player2points = data_split[4];
                    String player3 = data_split[5];
                    String player3points = data_split[6];
                    String player4 = data_split[7];
                    String player4points = data_split[8];

                    playerPoints.clear();
                    playerPoints.add(player1);
                    playerPoints.add(player1points);
                    playerPoints.add(player2);
                    playerPoints.add(player2points);
                    playerPoints.add(player3);
                    playerPoints.add(player3points);
                    playerPoints.add(player4);
                    playerPoints.add(player4points);

                }

                else if (data.toString().intern().contains("GameOver")) {
                    String[] data_split = data.toString().intern().split(" ");
                    String finalWinner = data_split[1];
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setHeaderText(finalWinner + " won! Game Over.");
                    a.showAndWait();
                    Platform.exit();
                }

                else {
                    if (!turnPlayer) {
                        byte[] bytes = (byte[]) data;

                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                        try {
                            BufferedImage bi = ImageIO.read(bis);
                            Image img = SwingFXUtils.toFXImage(bi, null );
                            ImageView iv = new ImageView(img);

                            VBox tmp = new VBox(iv);
                            Stage tmpStage = new Stage();
                            tmpStage.setScene(new Scene(tmp));
                            tmpStage.show();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
    }

    //------------------SERVER DATA HANDLED--------------------------------------------------------------------------------------------
    private Server createServer(String port) {
        return new Server(Integer.parseInt(port), data-> {
            Platform.runLater(()->{
                if (data.toString().intern().equals("Full")) {
                    connServer.removeExtraClient();
                }

                else if (data.toString().intern().contains("NameOfPlayer")) {
                    String[] data_split = data.toString().intern().split("\\^");
                    //for (String s : data_split) {
                    //	System.out.println(s);
                    //}
                    connServer.addPlayerName(data_split[1], data_split[2]);
                }
                else if (data.toString().intern().contains("ZXCVB")) {
                    try {
                        connServer.send(data);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else if (data.toString().intern().contains("timesup")) {
                    ArrayList<Integer> players = new ArrayList<>();
                    int chooseRandomPlayerToDraw = 1 + randomIndex.nextInt(4);

                    for(int i = 1; i <= 4; i++)
                        players.add(i);


                    for(Integer player : players) {
                        if(player == chooseRandomPlayerToDraw) {
                            try {
                                connServer.sendOne("yourturn", chooseRandomPlayerToDraw + "");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                connServer.sendOne("notyourturn", player + "");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if (data.toString().intern().contains("Winner")) {
                    String[] data_split = data.toString().intern().split("\\^");
                    String winner = data_split[1];

                    HashMap<String, String> playerNames = connServer.getPlayerNames();
                    HashMap<String, String> reverse = new HashMap<String, String>();

                    for(Entry<String, String> entry : playerNames.entrySet()) {
                        reverse.put(entry.getValue(), entry.getKey());
                    }

                    if (!reverse.containsKey(winner)) {
                        // Error
                    }
                    else {
                        Integer i = Integer.parseInt(reverse.get(winner));
                        playerPointsServer[i - 1] += 1;

                        // update game
                        if (i == 1) {
                            try {
                                connServer.sendOne("yourturn", "1");
                                connServer.sendOne("notyourturn", "2");
                                connServer.sendOne("notyourturn", "3");
                                connServer.sendOne("notyourturn", "4");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        else if (i == 2) {
                            try {
                                connServer.sendOne("yourturn", "2");
                                connServer.sendOne("notyourturn", "1");
                                connServer.sendOne("notyourturn", "3");
                                connServer.sendOne("notyourturn", "4");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        else if (i == 3) {
                            try {
                                connServer.sendOne("yourturn", "3");
                                connServer.sendOne("notyourturn", "1");
                                connServer.sendOne("notyourturn", "2");
                                connServer.sendOne("notyourturn", "4");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        else if (i == 4) {
                            try {
                                connServer.sendOne("yourturn", "4");
                                connServer.sendOne("notyourturn", "1");
                                connServer.sendOne("notyourturn", "2");
                                connServer.sendOne("notyourturn", "3");

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        try {
                            Integer winCheck = 0;
                            Integer counter = 1;
                            String finalWinner = "";
                            for (int p : playerPointsServer) {
                                if (p == 5) {
                                    winCheck = 1;
                                    finalWinner = connServer.getPlayerNames().get(String.valueOf(counter));
                                }
                                counter += 1;
                            }
                            if (winCheck == 1) {
                                connServer.send("GameOver " + finalWinner);
                            }
                            else {
                                String points = "";
                                for (String s : reverse.keySet()) {
                                    points = points + s + " ";
                                    points = points + playerPointsServer[Integer.parseInt(reverse.get(s))-1] + " ";
                                    //System.out.println(s + ": " + playerPoints[Integer.parseInt(reverse.get(s))-1]);
                                }
                                //System.out.println(points);
                                connServer.send("UpdatedPlayerPoints " + points);
                                connServer.send("WinnerWinnerChickenDinner " + winner);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        connServer.send(data);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println("Error sending data.");
                    }
                }
            });
        });
    }
}
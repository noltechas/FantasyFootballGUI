package program;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PlayoffsGUI extends Application {
    private static List<Conference> conferences;
    private static ArrayList<Team> teams;
    private static final int IMAGE_SIZE = 136;
    private static final boolean SHOW_LABELS = true;

    public static void setConferences(List<Conference> conferences) {
        PlayoffsGUI.conferences = conferences;
        teams = new ArrayList<>();
        arrangeTeams();
    }

    public static void arrangeTeams(){
        // Sort all the teams in the conferences
        for (Conference conference : conferences) {
            conference.getTeams().sort((t1, t2) -> {
                if (t1.wins != t2.wins) {
                    return t2.wins - t1.wins; // Sort by wins in descending order
                } else if (t1.pointsPerGame != t2.pointsPerGame) {
                    return Double.compare(t2.pointsPerGame, t1.pointsPerGame); // Sort by points per game in descending order
                } else {
                    String name1 = t1.teamName.equals(t1.username) ? t1.username : t1.username + " (" + t1.teamName + ")";
                    String name2 = t2.teamName.equals(t2.username) ? t2.username : t2.username + " (" + t2.teamName + ")";
                    return name1.compareToIgnoreCase(name2); // Sort by name in ascending order, ignoring case
                }
            });
        }

        ArrayList<Team> conferenceWinners = new ArrayList<>();
        // Add the best team in each conference to the winners list
        for (Conference conference : conferences) {
            conferenceWinners.add(conference.getTeams().get(0));
            conference.getTeams().remove(0);
        }
        conferenceWinners.sort((t1, t2) -> {
            if (t1.wins != t2.wins) {
                return t2.wins - t1.wins; // Sort by wins in descending order
            } else if (t1.pointsPerGame != t2.pointsPerGame) {
                return Double.compare(t2.pointsPerGame, t1.pointsPerGame); // Sort by points per game in descending order
            } else {
                String name1 = t1.teamName.equals(t1.username) ? t1.username : t1.username + " (" + t1.teamName + ")";
                String name2 = t2.teamName.equals(t2.username) ? t2.username : t2.username + " (" + t2.teamName + ")";
                return name1.compareToIgnoreCase(name2); // Sort by name in ascending order, ignoring case
            }
        });
        teams.addAll(conferenceWinners);

        // Get the best PPG wildcards
        ArrayList<Team> wildcards = new ArrayList<>();
        for (Conference conference : conferences) {
            wildcards.addAll(conference.getTeams());
        }
        wildcards.sort((t1, t2) -> {
            if (t1.pointsPerGame != t2.pointsPerGame) {
                return Double.compare(t2.pointsPerGame, t1.pointsPerGame);
            } else if (t1.wins != t2.wins) {
                return t2.wins - t1.wins; // Sort by wins in descending order
            } else {
                String name1 = t1.teamName.equals(t1.username) ? t1.username : t1.username + " (" + t1.teamName + ")";
                String name2 = t2.teamName.equals(t2.username) ? t2.username : t2.username + " (" + t2.teamName + ")";
                return name1.compareToIgnoreCase(name2); // Sort by name in ascending order, ignoring case
            }
        });
        for(int i = 0; i < 8; i++){
            teams.add(wildcards.get(0));
            wildcards.remove(0);
        }

        wildcards.sort((t1, t2) -> {
            if (t1.wins != t2.wins) {
                return t2.wins - t1.wins; // Sort by wins in descending order
            } else if (t1.pointsPerGame != t2.pointsPerGame) {
                return Double.compare(t2.pointsPerGame, t1.pointsPerGame); // Sort by points per game in descending order
            } else {
                String name1 = t1.teamName.equals(t1.username) ? t1.username : t1.username + " (" + t1.teamName + ")";
                String name2 = t2.teamName.equals(t2.username) ? t2.username : t2.username + " (" + t2.teamName + ")";
                return name1.compareToIgnoreCase(name2); // Sort by name in ascending order, ignoring case
            }
        });
        for(int i = 0; i < 8; i++){
            teams.add(wildcards.get(0));
            wildcards.remove(0);
        }

        for(int i = 0; i < 24; i++){
            System.out.println(teams.get(i).getTeamName() + ": " + teams.get(i).getPointsPerGame() + ", " + teams.get(i).getWins());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bracket.png")));
        ImageView imageView = new ImageView(image);

        Group group = new Group();
        group.getChildren().add(imageView);
        double initialScale = 0.1; // Adjust this value as needed
        group.getTransforms().add(new Scale(initialScale, initialScale));

        Button switchButton = new Button("View Standings");
        switchButton.setOnAction(e -> {
            StandingsGUI standingsGUI = new StandingsGUI();
            try {
                standingsGUI.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Round titles
        if(SHOW_LABELS) {
            Image wildcardImage = new Image("file:./src/main/resources/Wildcard.png");
            ImageView wildcardImageView = new ImageView(wildcardImage);
            ImageView wildcardImageViewFlip = new ImageView(wildcardImage);
            wildcardImageView.setFitHeight(1000);
            wildcardImageView.setFitWidth(1000);
            wildcardImageViewFlip.setFitHeight(1000);
            wildcardImageViewFlip.setFitWidth(1000);
            wildcardImageView.setLayoutX(1270);
            wildcardImageView.setLayoutY(550);
            wildcardImageViewFlip.setLayoutX(13330);
            wildcardImageViewFlip.setLayoutY(550);
            group.getChildren().add(wildcardImageView);
            group.getChildren().add(wildcardImageViewFlip);

            Image sweet16Image = new Image("file:./src/main/resources/Sweet16.png");
            ImageView sweet16View = new ImageView(sweet16Image);
            ImageView sweet16ViewFlip = new ImageView(sweet16Image);
            int adjustment = 1537;
            sweet16View.setFitHeight(1000);
            sweet16View.setFitWidth(1000);
            sweet16ViewFlip.setFitHeight(1000);
            sweet16ViewFlip.setFitWidth(1000);
            sweet16View.setLayoutX(1270 + adjustment);
            sweet16View.setLayoutY(550);
            sweet16ViewFlip.setLayoutX(13330 - adjustment);
            sweet16ViewFlip.setLayoutY(550);
            group.getChildren().add(sweet16View);
            group.getChildren().add(sweet16ViewFlip);

            Image quarterImage = new Image("file:./src/main/resources/Quarter.png");
            ImageView quarterView = new ImageView(quarterImage);
            ImageView quarterViewFlip = new ImageView(quarterImage);
            int Yadjustment = 450;
            quarterView.setFitHeight(1000);
            quarterView.setFitWidth(1000);
            quarterViewFlip.setFitHeight(1000);
            quarterViewFlip.setFitWidth(1000);
            quarterView.setLayoutX(1270 + adjustment * 2);
            quarterView.setLayoutY(550 + Yadjustment);
            quarterViewFlip.setLayoutX(13330 - adjustment * 2);
            quarterViewFlip.setLayoutY(550 + Yadjustment);
            group.getChildren().add(quarterView);
            group.getChildren().add(quarterViewFlip);

            Image semiImage = new Image("file:./src/main/resources/Semi.png");
            ImageView semiView = new ImageView(semiImage);
            ImageView semiViewFlip = new ImageView(semiImage);
            semiView.setFitHeight(1000);
            semiView.setFitWidth(1000);
            semiViewFlip.setFitHeight(1000);
            semiViewFlip.setFitWidth(1000);
            semiView.setLayoutX(1270 + adjustment * 3);
            semiView.setLayoutY(550 + Yadjustment * 3);
            semiViewFlip.setLayoutX(13330 - adjustment * 3);
            semiViewFlip.setLayoutY(550 + Yadjustment * 3);
            group.getChildren().add(semiView);
            group.getChildren().add(semiViewFlip);

            Image champImage = new Image("file:./src/main/resources/Champ.png");
            ImageView champView = new ImageView(champImage);
            champView.setFitHeight(1200);
            champView.setFitWidth(1200);
            champView.setLayoutX(1270 + adjustment * 4 - 200);
            champView.setLayoutY(550 + Yadjustment * 3 - 200);
            group.getChildren().add(champView);
        }

        Font font = Font.loadFont(getClass().getResourceAsStream("/GillSans.ttc"), 60);
        for (int i = 0; i < 24; i++) {
            Team team = teams.get(i);

            Image avatarImage = new Image("file:" + team.avatarUrl);
            ImageView avatarImageView = new ImageView(avatarImage);
            avatarImageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            avatarImageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            Image leagueImage = new Image("file:" + team.leagueAvatarUrl);
            ImageView leagueImageView = new ImageView(leagueImage);
            leagueImageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            leagueImageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            Image divisionImage = new Image("file:" + team.divisionAvatarUrl);
            ImageView divisionImageView = new ImageView(divisionImage);
            divisionImageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            divisionImageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            Text teamNameText = new Text(" " + team.getTeamName() + " (" + team.getWins() + "-" + team.getLosses() + ") ");
            teamNameText.setFill(Color.WHITE); // Set the color for the team name
            teamNameText.setFont(font);

            Text ppgText;
            if(i < 16)
                ppgText = new Text(" +" + String.format("%.1f", team.getPointsPerGame()/10));
            else
                ppgText = new Text();
            ppgText.setFont(Font.font("Arial Black", 50));
            ppgText.setStyle("-fx-fill: #333333; -fx-stroke: #6d6d6d; -fx-stroke-width: 1");

            TextFlow textFlow;
            Label seedLabel;

            boolean left_side = i != 1 && i != 6 && i != 2 && i != 5 && i != 14 && i != 17 && i != 9 && i != 22 && i != 13 && i != 18 && i != 10 && i != 21;
            if(left_side) {
                textFlow = new TextFlow(teamNameText, ppgText);
                if(i != 15 && i != 16 && i != 23 && i != 12 && i != 19 && i != 11 && i != 20)
                    seedLabel = new Label((i + 1) + " ");
                else
                    seedLabel = new Label((i + 1) + "");
            }
            else {
                if(i != 14 && i != 17 && i != 9 && i != 22 && i != 13 && i != 18 && i != 10 && i != 21)
                    seedLabel = new Label(" " + (i + 1));
                else
                    seedLabel = new Label("" + (i + 1));
                textFlow = new TextFlow(ppgText, teamNameText);
            }
            textFlow.setTranslateY(30);
            textFlow.setTextAlignment(TextAlignment.RIGHT);
            seedLabel.setTextFill(Color.WHITE); // Set the color for the seed number
            seedLabel.setFont(Font.font("Arial Black", 35));
            seedLabel.setTextFill(Color.web("#B0B1B3"));

            GridPane gridPane = new GridPane();
            gridPane.setHgap(13); // Set the horizontal gap between elements

            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(1000); // Set this to the width you want

            // Add elements to the gridPane
            if(left_side) {
                gridPane.add(seedLabel, 0, 0);
                gridPane.add(leagueImageView, 1, 0);
                gridPane.add(divisionImageView, 2, 0);
                gridPane.add(avatarImageView, 3, 0);
                GridPane.setHgrow(textFlow, Priority.ALWAYS);
                gridPane.add(textFlow, 4, 0);
            } else {
                gridPane.getColumnConstraints().add(columnConstraints);
                GridPane.setHgrow(textFlow, Priority.ALWAYS);
                gridPane.add(textFlow, 0, 0);
                gridPane.add(avatarImageView, 1, 0);
                gridPane.add(divisionImageView, 2, 0);
                gridPane.add(leagueImageView, 3, 0);
                gridPane.add(seedLabel, 4, 0);
            }

            // Set the alignment
            gridPane.setAlignment(Pos.CENTER);

            // Set the gridPane as the graphic for the label
            Label label = new Label();
            label.setGraphic(gridPane);


            final int WILDCARD_X = 1088;
            final int BYE_X = 2630;
            final float WILDCARD_X_FLIP = 14510.5F-1490.25F;
            final float BYE_X_FLIP = 12980-1490.25F;
            if(i == 0) {
                label.setLayoutX(BYE_X);
                label.setLayoutY(1427);
            }
            else if(i == 1){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(1427);
            }
            else if(i == 2){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(3211);
            }
            else if(i == 3){
                label.setLayoutX(BYE_X);
                label.setLayoutY(3211);
            }
            else if(i == 4){
                label.setLayoutX(BYE_X);
                label.setLayoutY(4103);
            }
            else if(i == 5){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(4103);
            }
            else if(i == 6){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(2319);
            }
            else if(i == 7){
                label.setLayoutX(BYE_X);
                label.setLayoutY(2319);
            }
            else if(i == 8){
                label.setLayoutX(WILDCARD_X + 11.3);
                label.setLayoutY(2319);
            }
            else if(i == 9){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(2319);
            }
            else if(i == 10){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(4103);
            }
            else if(i == 11){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(4103);
            }
            else if(i == 12){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(3211);
            }
            else if(i == 13){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(3211);
            }
            else if(i == 14){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(1427);
            }
            else if(i == 15){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(1427);
            }
            else if(i == 16){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(1576);
            }
            else if(i == 17){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(1576);
            }
            else if(i == 18){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(3360);
            }
            else if(i == 19){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(3360);
            }
            else if(i == 20){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(4252);
            }
            else if(i == 21){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(4252);
            }
            else if(i == 22){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(2468);
            }
            else {
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(2468);
            }

            group.getChildren().add(label);
        }

        // Add a scroll event to handle zooming
        group.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 0.95;
            }

            // Calculate the zoom pivot point (relative to the scene)
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            // Convert the pivot point to be relative to the group
            Point2D pivotOnGroup = group.parentToLocal(mouseX, mouseY);

            // Apply the zoom
            Scale scale = new Scale(zoomFactor, zoomFactor, pivotOnGroup.getX(), pivotOnGroup.getY());
            group.getTransforms().add(scale);

            event.consume();
        });

        // Add a mouse event to handle dragging
        final Delta dragDelta = new Delta();
        group.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = mouseEvent.getSceneX() - group.getLayoutX();
            dragDelta.y = mouseEvent.getSceneY() - group.getLayoutY();
        });
        group.setOnMouseDragged(mouseEvent -> {
            group.setLayoutX(mouseEvent.getSceneX() - dragDelta.x);
            group.setLayoutY(mouseEvent.getSceneY() - dragDelta.y);
        });

        Pane pane = new Pane();
        pane.getChildren().add(group);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #313B4A"); // Set the background color on the root
        root.getChildren().add(pane);

        Scene scene = new Scene(root, 1600, 1600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add the button to the root of the scene
        root.getChildren().add(switchButton);
        StackPane.setAlignment(switchButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(switchButton, new Insets(0, 10, 10, 0)); // 10 px margin from the right and bottom
    }

    // Used to record the x and y offset of the mouse drag
    private static class Delta { double x, y; }
}

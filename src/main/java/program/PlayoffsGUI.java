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
import javafx.scene.input.KeyCode;
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
    private static int clickCount = 0;
    private static final int[] displayOrder = { 0, 7, 15, 16, 8, 23, 3, 4, 12, 19, 11, 20, 1, 6, 14, 17, 9, 22, 2, 5, 13, 18, 10, 21 };

    public static void setConferences(List<Conference> conferences) {
        PlayoffsGUI.conferences = conferences;
        teams = new ArrayList<>();
        arrangeTeams();
    }

    public static Team getTeamByUsername(String username){
        ArrayList<Team> allTeams = new ArrayList<>();
        for(Conference conference : conferences){
            allTeams.addAll(conference.getTeams());
        }
        for(Team team : allTeams){
            if(Objects.equals(team.getUsername(), username))
                return team;
        }
        return null;
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
        // Add the conference winners to the winners list

        conferenceWinners.add(getTeamByUsername("GogiPrufu"));
        conferenceWinners.add(getTeamByUsername("blazer86"));
        conferenceWinners.add(getTeamByUsername("JBohnenkamp84"));
        conferenceWinners.add(getTeamByUsername("MrGatchoAss"));
        conferenceWinners.add(getTeamByUsername("Figgzzz"));
        conferenceWinners.add(getTeamByUsername("BraydonMck")); // NOT CERTAIN
        conferenceWinners.add(getTeamByUsername("Chargersxx21"));
        conferenceWinners.add(getTeamByUsername("tosh612"));

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
        while(teams.size() < 16){
            if(!teams.contains(wildcards.get(0)))
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
        while(teams.size() < 24){
            if(!teams.contains(wildcards.get(0)))
                teams.add(wildcards.get(0));
            wildcards.remove(0);
        }

        for(int i = 0; i < 24; i++){
            System.out.println(teams.get(i).getTeamName() + ": " + teams.get(i).getPointsPerGame() + ", " + teams.get(i).getWins());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Image image;
        if(!SHOW_LABELS)
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/bracket.png")));
        else {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logobracket.png")));
        }
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

        Font font = Font.loadFont(getClass().getResourceAsStream("/GillSans.ttc"), 60);

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

        Scene scene = new Scene(root, 1600, 800);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.N) {
                displayNextTeam(group);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add the button to the root of the scene
        root.getChildren().add(switchButton);
        StackPane.setAlignment(switchButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(switchButton, new Insets(0, 10, 10, 0)); // 10 px margin from the right and bottom
    }

    // Used to record the x and y offset of the mouse drag
    private static class Delta { double x, y; }

    private void displayNextTeam(Group group) {
        if (clickCount < displayOrder.length) {
            int teamIndex = displayOrder[clickCount];
            Team team = teams.get(teamIndex);

            // The following is your existing team display code, adapted to use clickCount
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

            Font font = Font.loadFont(getClass().getResourceAsStream("/GillSans.ttc"), 60);
            Text teamNameText = new Text(" " + team.getTeamName() + " (" + team.getWins() + "-" + team.getLosses() + ") ");
            teamNameText.setFill(Color.WHITE);
            teamNameText.setFont(font);

            Text ppgText;
            if(teamIndex < 16)
                ppgText = new Text(" +" + String.format("%.1f", team.getPointsPerGame()/10));
            else
                ppgText = new Text();
            ppgText.setFont(Font.font("Arial Black", 50));
            ppgText.setStyle("-fx-fill: #333333; -fx-stroke: #ffdf0f; -fx-stroke-width: 2");

            TextFlow textFlow;
            Label seedLabel;

            boolean left_side = teamIndex != 1 && teamIndex != 6 && teamIndex != 2 && teamIndex != 5 && teamIndex != 14 && teamIndex != 17 && teamIndex != 9 && teamIndex != 22 && teamIndex != 13 && teamIndex != 18 && teamIndex != 10 && teamIndex != 21;
            if(left_side) {
                textFlow = new TextFlow(teamNameText, ppgText);
                if(teamIndex != 15 && teamIndex != 16 && teamIndex != 23 && teamIndex != 12 && teamIndex != 19 && teamIndex != 11 && teamIndex != 20)
                    seedLabel = new Label((teamIndex + 1) + " ");
                else
                    seedLabel = new Label((teamIndex + 1) + "");
            } else {
                if(teamIndex != 14 && teamIndex != 17 && teamIndex != 9 && teamIndex != 22 && teamIndex != 13 && teamIndex != 18 && teamIndex != 10 && teamIndex != 21)
                    seedLabel = new Label(" " + (teamIndex + 1));
                else
                    seedLabel = new Label("" + (teamIndex + 1));
                textFlow = new TextFlow(ppgText, teamNameText);
            }
            textFlow.setTranslateY(30);
            textFlow.setTextAlignment(TextAlignment.RIGHT);
            seedLabel.setTextFill(Color.WHITE);
            seedLabel.setFont(Font.font("Arial Black", 35));
            seedLabel.setTextFill(Color.web("#B0B1B3"));

            GridPane gridPane = new GridPane();
            gridPane.setHgap(13);

            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(1000);

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

            gridPane.setAlignment(Pos.CENTER);

            Label label = new Label();
            label.setGraphic(gridPane);

            final int WILDCARD_X = 1088;
            final int BYE_X = 2630;
            final float WILDCARD_X_FLIP = 14510.5F - 1490.25F;
            final float BYE_X_FLIP = 12980 - 1490.25F;

            // Replace 'i' with 'clickCount' in the following positioning logic
            if(teamIndex == 0) {
                label.setLayoutX(BYE_X);
                label.setLayoutY(1427);
            }
            else if(teamIndex == 1){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(1427);
            }
            else if(teamIndex == 2){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(3211);
            }
            else if(teamIndex == 3){
                label.setLayoutX(BYE_X);
                label.setLayoutY(3211);
            }
            else if(teamIndex == 4){
                label.setLayoutX(BYE_X);
                label.setLayoutY(4103);
            }
            else if(teamIndex == 5){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(4103);
            }
            else if(teamIndex == 6){
                label.setLayoutX(BYE_X_FLIP);
                label.setLayoutY(2319);
            }
            else if(teamIndex == 7){
                label.setLayoutX(BYE_X);
                label.setLayoutY(2319);
            }
            else if(teamIndex == 8){
                label.setLayoutX(WILDCARD_X + 11.3);
                label.setLayoutY(2319);
            }
            else if(teamIndex == 9){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(2319);
            }
            else if(teamIndex == 10){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(4103);
            }
            else if(teamIndex == 11){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(4103);
            }
            else if(teamIndex == 12){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(3211);
            }
            else if(teamIndex == 13){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(3211);
            }
            else if(teamIndex == 14){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(1427);
            }
            else if(teamIndex == 15){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(1427);
            }
            else if(teamIndex == 16){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(1576);
            }
            else if(teamIndex == 17){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(1576);
            }
            else if(teamIndex == 18){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(3360);
            }
            else if(teamIndex == 19){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(3360);
            }
            else if(teamIndex == 20){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(4252);
            }
            else if(teamIndex == 21){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(4252);
            }
            else if(teamIndex == 22){
                label.setLayoutX(WILDCARD_X_FLIP);
                label.setLayoutY(2468);
            }
            else if(teamIndex == 23){
                label.setLayoutX(WILDCARD_X);
                label.setLayoutY(2468);
            }

            group.getChildren().add(label);
        }
        clickCount++;
    }

}

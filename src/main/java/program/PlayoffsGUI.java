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
        teams.add(getTeamByUsername("Figgzzz"));
        teams.add(getTeamByUsername("GogiPrufu"));
        teams.add(getTeamByUsername("tosh612"));
        teams.add(getTeamByUsername("JBohnenkamp84"));
        teams.add(getTeamByUsername("BraydonMck"));
        teams.add(getTeamByUsername("Chargersxx21"));
        teams.add(getTeamByUsername("blazer86"));
        teams.add(getTeamByUsername("MrGatchoAss"));
        teams.add(getTeamByUsername("safety120"));
        teams.add(getTeamByUsername("KyleJacobTrussell"));
        teams.add(getTeamByUsername("BiggNas"));
        teams.add(getTeamByUsername("Noahcg8"));
        teams.add(getTeamByUsername("JaarnushFantasy"));
        teams.add(getTeamByUsername("nhallowell88"));
        teams.add(getTeamByUsername("Skip_Skunky"));
        teams.add(getTeamByUsername("DudeFootball"));
        teams.add(getTeamByUsername("00sheaD"));
        teams.add(getTeamByUsername("dbsmith24"));
        teams.add(getTeamByUsername("BleedGold"));
        teams.add(getTeamByUsername("bigshot626"));
        teams.add(getTeamByUsername("ctalpha"));
        teams.add(getTeamByUsername("isaiahm306"));
        teams.add(getTeamByUsername("polishsausage54"));
        teams.add(getTeamByUsername("dlittle2300"));

        teams.get(8).setScoreRoundOne(94.22 + teams.get(8).getPointsPerGame()/10); // FINAL
        teams.get(9).setScoreRoundOne(98.14 + teams.get(9).getPointsPerGame()/10); // FINAL
        teams.get(10).setScoreRoundOne(127.32 + teams.get(10).getPointsPerGame()/10); // FINAL
        teams.get(11).setScoreRoundOne(120.1 + teams.get(11).getPointsPerGame()/10); // FINAL
        teams.get(12).setScoreRoundOne(99.38 + teams.get(12).getPointsPerGame()/10); // FINAL
        teams.get(13).setScoreRoundOne(133.86 + teams.get(13).getPointsPerGame()/10); // FINAL
        teams.get(14).setScoreRoundOne(127.98 + teams.get(14).getPointsPerGame()/10); // FINAL
        teams.get(15).setScoreRoundOne(99.72 + teams.get(15).getPointsPerGame()/10); // FINAL
        teams.get(16).setScoreRoundOne(111.9); // FINAL
        teams.get(17).setScoreRoundOne(81.48); // FINAL
        teams.get(18).setScoreRoundOne(125.66); // FINAL
        teams.get(19).setScoreRoundOne(107.28); // FINAL
        teams.get(20).setScoreRoundOne(106.18); // FINAL
        teams.get(21).setScoreRoundOne(75.2); // FINAL
        teams.get(22).setScoreRoundOne(78.4); // FINAL
        teams.get(23).setScoreRoundOne(82.78); // FINAL

        for(int i = 0; i < teams.size(); i++){
            teams.get(i).setSeed(i+1);
        }

        for(int i = 0; i < 24; i++){
            System.out.println(teams.get(i).getSeed() + ". " + teams.get(i).getTeamName() + ": " + teams.get(i).getPointsPerGame() + ", " + teams.get(i).getWins());
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

        Objects.requireNonNull(getTeamByUsername("Figgzzz")).setScoreRoundTwo(129.5);
        Objects.requireNonNull(getTeamByUsername("00sheaD")).setScoreRoundTwo(105.9);

        Objects.requireNonNull(getTeamByUsername("MrGatchoAss")).setScoreRoundTwo(100.78);
        Objects.requireNonNull(getTeamByUsername("safety120")).setScoreRoundTwo(74.38);

        Objects.requireNonNull(getTeamByUsername("JBohnenkamp84")).setScoreRoundTwo(101.56);
        Objects.requireNonNull(getTeamByUsername("JaarnushFantasy")).setScoreRoundTwo(69.8);

        Objects.requireNonNull(getTeamByUsername("BraydonMck")).setScoreRoundTwo(81.08);
        Objects.requireNonNull(getTeamByUsername("Noahcg8")).setScoreRoundTwo(88.8);

        Objects.requireNonNull(getTeamByUsername("GogiPrufu")).setScoreRoundTwo(85.84);
        Objects.requireNonNull(getTeamByUsername("Skip_Skunky")).setScoreRoundTwo(127.1);

        Objects.requireNonNull(getTeamByUsername("blazer86")).setScoreRoundTwo(101.1);
        Objects.requireNonNull(getTeamByUsername("KyleJacobTrussell")).setScoreRoundTwo(110.04);

        Objects.requireNonNull(getTeamByUsername("tosh612")).setScoreRoundTwo(100.72);
        Objects.requireNonNull(getTeamByUsername("nhallowell88")).setScoreRoundTwo(83.92);

        Objects.requireNonNull(getTeamByUsername("Chargersxx21")).setScoreRoundTwo(85.64);
        Objects.requireNonNull(getTeamByUsername("BiggNas")).setScoreRoundTwo(58.68);

        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("00sheaD")), 0);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("safety120")), 1);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("JaarnushFantasy")), 2);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Noahcg8")), 3);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Skip_Skunky")), 4);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("KyleJacobTrussell")), 5);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("nhallowell88")), 6);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("BiggNas")), 7);

        Objects.requireNonNull(getTeamByUsername("Figgzzz")).setScoreRoundThree(119.06);
        Objects.requireNonNull(getTeamByUsername("MrGatchoAss")).setScoreRoundThree(97.92);

        Objects.requireNonNull(getTeamByUsername("JBohnenkamp84")).setScoreRoundThree(101.42);
        Objects.requireNonNull(getTeamByUsername("BraydonMck")).setScoreRoundThree(118.12);

        Objects.requireNonNull(getTeamByUsername("Skip_Skunky")).setScoreRoundThree(143.54);
        Objects.requireNonNull(getTeamByUsername("blazer86")).setScoreRoundThree(81.36);

        Objects.requireNonNull(getTeamByUsername("tosh612")).setScoreRoundThree(118.92);
        Objects.requireNonNull(getTeamByUsername("Chargersxx21")).setScoreRoundThree(134.02);

        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Figgzzz")), 8);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("MrGatchoAss")), 9);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("JBohnenkamp84")), 10);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("BraydonMck")), 11);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Skip_Skunky")), 12);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("blazer86")), 13);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("tosh612")), 14);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Chargersxx21")), 15);

        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Figgzzz")), 16);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("BraydonMck")), 17);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Skip_Skunky")), 18);
        displayAdvancedTeam(group, Objects.requireNonNull(getTeamByUsername("Chargersxx21")), 19);

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

            Text ppgText = new Text();
            if(teamIndex >= 16) {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundOne()));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
            else if (teamIndex >= 8) {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundOne() + team.getPointsPerGame()/10));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
            else{
                ppgText = new Text(String.format("%.2f", team.getScoreRoundTwo() + team.getPointsPerGame()/10));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
            ppgText.setFont(Font.font("Arial Black", 50));

            /*
            if(team.scoreRoundTwo > 0 && team.getSeed() <= 8) {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundTwo() + team.getPointsPerGame()/10));
            }
            else if(team.scoreRoundTwo > 0){
                ppgText = new Text(String.format("%.2f", team.getScoreRoundTwo()));
            }
            else {
                ppgText = new Text("");
            }
            ppgText.setStyle("-fx-fill: #ffdf0f;");
            ppgText.setFont(Font.font("Arial Black", 50));

             */


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

    private void displayAdvancedTeam(Group group, Team team, int slot){
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

        Text ppgText = new Text();
        if(slot < 8) {
            if (team.scoreRoundTwo > 0) {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundTwo()));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            } else {
                ppgText = new Text("");
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
        }
        else if(slot < 16) {
            if (team.getSeed() == 1 || team.getSeed() == 4 || team.getSeed() == 7 || team.getSeed() == 3){
                ppgText = new Text(String.format("%.2f", team.getScoreRoundThree() + team.getPointsPerGame()/10));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
            else {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundThree()));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
        }
        else if(slot < 20) {
            if (team.scoreRoundFour > 0) {
                ppgText = new Text(String.format("%.2f", team.getScoreRoundThree()));
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
            else if (team.getSeed() == 1 || team.getSeed() == 6){
                ppgText = new Text(" +" + String.format("%.1f", team.getPointsPerGame()/10));
                ppgText.setStyle("-fx-fill: #333333; -fx-stroke: #ffdf0f; -fx-stroke-width: 2");
            }
            else {
                ppgText = new Text("");
                ppgText.setStyle("-fx-fill: #ffdf0f;");
            }
        }
        ppgText.setFont(Font.font("Arial Black", 50));


        TextFlow textFlow;
        Label seedLabel;

        boolean left_side = team.getSeed()-1 != 1 && team.getSeed()-1 != 6 && team.getSeed()-1 != 2 && team.getSeed()-1 != 5 && team.getSeed()-1 != 14 && team.getSeed()-1 != 17 && team.getSeed()-1 != 9 && team.getSeed()-1 != 22 && team.getSeed()-1 != 13 && team.getSeed()-1 != 18 && team.getSeed()-1 != 10 && team.getSeed()-1 != 21;
        if(left_side) {
            textFlow = new TextFlow(teamNameText, ppgText);
            if(team.getSeed()-1 != 15 && team.getSeed()-1 != 16 && team.getSeed()-1 != 23 && team.getSeed()-1 != 12 && team.getSeed()-1 != 19 && team.getSeed()-1 != 11 && team.getSeed()-1 != 20)
                seedLabel = new Label((team.getSeed()-1 + 1) + " ");
            else
                seedLabel = new Label((team.getSeed()-1 + 1) + "");
        } else {
            if(team.getSeed()-1 != 14 && team.getSeed()-1 != 17 && team.getSeed()-1 != 9 && team.getSeed()-1 != 22 && team.getSeed()-1 != 13 && team.getSeed()-1 != 18 && team.getSeed()-1 != 10 && team.getSeed()-1 != 21)
                seedLabel = new Label(" " + (team.getSeed()));
            else
                seedLabel = new Label("" + (team.getSeed()));
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
        final double BYE_X = 2618;
        final float WILDCARD_X_FLIP = 14510.5F - 1490.25F;
        final float BYE_X_FLIP = 12980 - 1490.25F;

        if(slot == 0) {
            label.setLayoutX(BYE_X);
            label.setLayoutY(1576);
        }
        if(slot == 1) {
            label.setLayoutX(BYE_X+11);
            label.setLayoutY(2467.5);
        }
        if(slot == 2) {
            label.setLayoutX(BYE_X);
            label.setLayoutY(3360);
        }
        if(slot == 3) {
            label.setLayoutX(BYE_X);
            label.setLayoutY(4252);
        }
        if(slot == 4) {
            label.setLayoutX(BYE_X_FLIP);
            label.setLayoutY(1576);
        }
        if(slot == 5) {
            label.setLayoutX(BYE_X_FLIP);
            label.setLayoutY(2468);
        }
        if(slot == 6) {
            label.setLayoutX(BYE_X_FLIP);
            label.setLayoutY(3360);
        }
        if(slot == 7) {
            label.setLayoutX(BYE_X_FLIP);
            label.setLayoutY(4252);
        }
        if(slot == 8) {
            label.setLayoutX(4160);
            label.setLayoutY(1873.5);
        }
        if(slot == 9) {
            label.setLayoutX(4160);
            label.setLayoutY(2022);
        }
        if(slot == 10) {
            label.setLayoutX(4159.75);
            label.setLayoutY(3656.16);
        }
        if(slot == 11) {
            label.setLayoutX(4159.75);
            label.setLayoutY(3805);
        }
        if(slot == 12) {
            label.setLayoutX(WILDCARD_X_FLIP - 3060.85);
            label.setLayoutY(1873.25);
        }
        if(slot == 13) {
            label.setLayoutX(WILDCARD_X_FLIP - 3060.85);
            label.setLayoutY(2022);
        }
        if(slot == 14) {
            label.setLayoutX(WILDCARD_X_FLIP - 3060.85);
            label.setLayoutY(3656.16);
        }
        if(slot == 15) {
            label.setLayoutX(WILDCARD_X_FLIP - 3060.85);
            label.setLayoutY(3805);
        }
        if(slot == 16) {
            label.setLayoutX(5691);
            label.setLayoutY(2764.33);
        }
        if(slot == 17) {
            label.setLayoutX(5691);
            label.setLayoutY(2913);
        }
        if(slot == 18) {
            label.setLayoutX(8428.5);
            label.setLayoutY(2764.33);
        }
        if(slot == 19) {
            label.setLayoutX(8428.5);
            label.setLayoutY(2913);
        }

        group.getChildren().add(label);
    }

}

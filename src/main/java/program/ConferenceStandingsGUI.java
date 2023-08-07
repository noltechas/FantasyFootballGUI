package program;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.List;

public class ConferenceStandingsGUI extends Application {
    private static List<Conference> conferences;
    private static final int IMAGE_SIZE = 70;

    public static void setConferences(List<Conference> conferences) {
        ConferenceStandingsGUI.conferences = conferences;
    }

    @Override
    public void start(Stage primaryStage) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/GillSans.ttc"), 30); // Load the custom font

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1E1F24;"); // Set the background color

        Pagination pagination = new Pagination(conferences.size(), 0);
        pagination.setPageFactory((pageIndex) -> {
            Conference conference = conferences.get(pageIndex);
            VBox conferenceBox = new VBox();
            conferenceBox.setSpacing(10);
            conferenceBox.setAlignment(Pos.CENTER);

            // Get the first team's league avatar URL
            String leagueAvatarUrl = conference.division1.get(0).leagueAvatarUrl;
            Image leagueAvatar = new Image("file:" + leagueAvatarUrl);
            ImageView leagueAvatarView = new ImageView(leagueAvatar);
            leagueAvatarView.setFitHeight(IMAGE_SIZE * 3); // Make the conference logo bigger
            leagueAvatarView.setFitWidth(IMAGE_SIZE * 3); // Make the conference logo bigger

            // Add padding to the conference logo to move it down
            VBox.setMargin(leagueAvatarView, new Insets(-40, 0, 0, 0));

            conferenceBox.getChildren().add(leagueAvatarView);

            HBox divisionsBox = new HBox();
            divisionsBox.setSpacing(10);
            divisionsBox.setAlignment(Pos.CENTER);

            // Add margin to the divisions box to move it up
            VBox.setMargin(divisionsBox, new Insets(-160, 0, 0, 0));

            for (int i = 0; i < 2; i++) {
                GridPane divisionBox = new GridPane();
                divisionBox.setHgap(10);
                divisionBox.setVgap(5);
                divisionBox.setAlignment(Pos.CENTER);

                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.ALWAYS);
                divisionBox.getColumnConstraints().add(column1);

                String divisionName;
                String divisionAvatarUrl;
                List<Team> divisionTeams;
                if(i == 0) {
                    divisionName = "  " + conference.division1name;
                    divisionAvatarUrl = conference.division1avatar;
                    divisionTeams = conference.division1;
                } else {
                    divisionName = conference.division2name + "  ";
                    divisionAvatarUrl = conference.division2avatar;
                    divisionTeams = conference.division2;
                }

                Image divisionAvatar = new Image(divisionAvatarUrl);
                ImageView divisionAvatarView = new ImageView(divisionAvatar);
                divisionAvatarView.setFitHeight(IMAGE_SIZE);
                divisionAvatarView.setFitWidth(IMAGE_SIZE);

                Label divisionLabel = new Label(divisionName);
                divisionLabel.setFont(font); // Use the custom font
                divisionLabel.setTextFill(Color.WHITE);

                HBox divisionNameBox;
                if(i == 0)
                    divisionNameBox = new HBox(divisionAvatarView, divisionLabel);
                else
                    divisionNameBox = new HBox(divisionLabel, divisionAvatarView);
                divisionNameBox.setAlignment(Pos.CENTER); // Center the division name and avatar
                divisionBox.add(divisionNameBox, 0, 0, 3, 1); // Span 3 columns

                for (int j = 0; j < divisionTeams.size(); j++) {
                    Team team = divisionTeams.get(j);
                    String standings = team.getTeamName() + ": " + team.wins + "-" + team.losses + "; " + String.format("%.1f", team.pointsPerGame) + " PPG";

                    Image teamAvatar = new Image("file:" + team.avatarUrl);
                    ImageView teamAvatarView = new ImageView(teamAvatar);
                    teamAvatarView.setFitHeight(IMAGE_SIZE);
                    teamAvatarView.setFitWidth(IMAGE_SIZE);

                    Label teamLabel = new Label(standings);
                    teamLabel.setFont(font); // Use the custom font
                    teamLabel.setTextFill(j == 0 ? Color.GOLD : Color.WHITE); // Set the text color to gold if the team is the first in the division, otherwise set it to white

                    Label vsLabel = new Label("                          ");
                    vsLabel.setFont(font); // Use the custom font
                    vsLabel.setTextFill(Color.WHITE);

                    if (i == 0) { // Division 1
                        divisionBox.add(teamAvatarView, 0, j + 1);
                        divisionBox.add(teamLabel, 1, j + 1);
                        divisionBox.add(vsLabel, 2, j + 1);
                    } else { // Division 2
                        divisionBox.add(teamLabel, 1, j + 1);
                        divisionBox.add(teamAvatarView, 2, j + 1);
                        GridPane.setHalignment(teamLabel, HPos.RIGHT);
                    }
                }

                divisionsBox.getChildren().add(divisionBox);
            }

            conferenceBox.getChildren().add(divisionsBox);
            return conferenceBox;
        });

        root.getChildren().add(pagination);

        Button switchButton = new Button("View Playoff Bracket");
        switchButton.setOnAction(e -> {
            PlayoffsGUI playoffsGUI = new PlayoffsGUI();
            try {
                playoffsGUI.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        HBox buttonBox = new HBox(switchButton); // Create a new HBox to hold the button
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT); // Align the button to the bottom right of the HBox

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pagination);
        borderPane.setBottom(buttonBox);
        BorderPane.setAlignment(switchButton, Pos.BOTTOM_RIGHT);
        borderPane.setStyle("-fx-background-color: #1E1F24;"); // Set the background color

        Scene scene = new Scene(borderPane, 1600, 777);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

package program;

import javafx.application.Application;
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
import javafx.stage.Stage;

import java.util.List;

public class StandingsGUI extends Application {
    private static List<Team> teams;
    private static final int ITEMS_PER_PAGE = 16; // Adjust this value as needed
    private static final int COLUMNS = 2;
    private static final int IMAGE_SIZE = 50;

    public static void setTeams(List<Team> teams) {
        StandingsGUI.teams = teams;
    }

    @Override
    public void start(Stage primaryStage) {
        Pagination pagination = new Pagination((teams.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE, 0);
        pagination.setPageFactory(this::createPage);

        Button switchButton = new Button("View Conference Standings");
        switchButton.setOnAction(e -> {
            ConferenceStandingsGUI playoffsGUI = new ConferenceStandingsGUI();
            try {
                playoffsGUI.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pagination);
        borderPane.setBottom(switchButton);
        BorderPane.setAlignment(switchButton, Pos.BOTTOM_RIGHT);
        borderPane.setStyle("-fx-background-color: rgb(44,55,71);"); // Set the background color

        Scene scene = new Scene(borderPane, 1600, 777);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private GridPane createPage(int pageIndex) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/GillSans.ttc"), 30);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: rgb(44,55,71);"); // Set the background color

        int pageStartIndex = pageIndex * ITEMS_PER_PAGE;
        int pageEndIndex = Math.min(pageStartIndex + ITEMS_PER_PAGE, teams.size());
        for (int i = pageStartIndex; i < pageEndIndex; i++) {
            Team team = teams.get(i);
            String name = !team.teamName.equals(team.username) ? team.teamName : team.username;
            String standings = (i + 1) + ". " + name + ": " + team.wins + "-" + team.losses + "; " + String.format("%.1f", team.pointsPerGame) + " PPG";

            Image leagueImage = new Image("file:" + team.leagueAvatarUrl);
            ImageView leagueImageView = new ImageView(leagueImage);
            leagueImageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            leagueImageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            Image divisionImage = new Image("file:" + team.divisionAvatarUrl);
            ImageView divisionImageView = new ImageView(divisionImage);
            divisionImageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            divisionImageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            Image image = new Image("file:" + team.avatarUrl);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(IMAGE_SIZE); // Adjust the size as needed
            imageView.setFitWidth(IMAGE_SIZE); // Adjust the size as needed

            HBox hbox = new HBox(leagueImageView, divisionImageView, imageView); // Add all ImageView objects to the HBox

            Label label = new Label(standings, hbox); // Set the HBox as the graphic for the label
            label.setTextFill(Color.WHITE); // Set the text color to white
            label.setFont(font);

            int column = (i - pageStartIndex) / (ITEMS_PER_PAGE / COLUMNS);
            int row = (i - pageStartIndex) % (ITEMS_PER_PAGE / COLUMNS);

            grid.add(label, column, row);
        }

        return grid;
    }

}
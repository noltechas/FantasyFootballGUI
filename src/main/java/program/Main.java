package program;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import javafx.application.Application;
import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static java.lang.Math.max;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String[] leagueIds = {"985569785293467648", "985978467361583104", "985571036416544768", "985978629114970112", "985570334562721792", "985570243441487872", "985570133928132608", "985273817578659840"}; // Replace with your actual league IDs

        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<Conference> conferences = new ArrayList<>();

        for (String leagueId : leagueIds) {
            // Fetch rosters
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.sleeper.app/v1/league/" + leagueId + "/rosters"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            JSONArray rosters = new JSONArray(response.body());

            // Fetch users
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.sleeper.app/v1/league/" + leagueId + "/users"))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            JSONArray users = new JSONArray(response.body());

            // Fetch league info
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.sleeper.app/v1/league/" + leagueId))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            JSONObject league = new JSONObject(response.body());
            String conferenceName = league.getString("name");

            conferences.add(0,new Conference(conferenceName));

            Random random = new Random();

            // Get standings
            for (int i = 0; i < rosters.length(); i++) {
                JSONObject roster = rosters.getJSONObject(i);
                String ownerId = roster.getString("owner_id");

                // Find user data for this owner
                JSONObject user = null;
                for (int j = 0; j < users.length(); j++) {
                    JSONObject tempUser = users.getJSONObject(j);
                    if (tempUser.getString("user_id").equals(ownerId)) {
                        user = tempUser;
                        break;
                    }
                }

                if (user == null) {
                    continue; // Skip this roster if the user data could not be found
                }

                String username = user.getString("display_name");
                String teamName = username; // Use username as a fallback if team name is not available
                if (!user.isNull("metadata")) {
                    JSONObject metadata = user.getJSONObject("metadata");
                    if (!metadata.isNull("team_name")) {
                        teamName = metadata.getString("team_name");
                    }
                }

                JSONObject settings = roster.getJSONObject("settings");
                int wins = settings.getInt("wins");
                int losses = settings.getInt("losses");

                // Points per game is not directly available in the data, so we'll use a placeholder value
                double pointsPerGame = 0.0;

                String avatarUrl = "./src/main/resources/teams/" + username + "_avatar.png";
                String leagueAvatarUrl = "./src/main/resources/teams/" + username + "_leagueAvatar.png";
                String divisionAvatarUrl = "./src/main/resources/teams/" + username + "_divisionAvatar.png";

                // Simulate end of season
                float winPercentage = (float)0.15 + random.nextFloat() * ((float)0.85 - (float)0.15);
                wins = 0;
                for(int j = 0; j < 24; j++){
                    if(winPercentage > random.nextFloat())
                        wins++;
                }
                losses = 24 - wins; // Each team plays 24 games
                pointsPerGame = max(80,winPercentage*120) + random.nextGaussian() * 4.408873111054; // Average of 100 with a standard deviation of 10

                String division;
                division = String.valueOf(settings.getInt("division"));

                Team newTeam = new Team(username, teamName, wins, losses, pointsPerGame, avatarUrl, leagueAvatarUrl, divisionAvatarUrl, division);
                teams.add(newTeam);
                conferences.get(0).addTeam(newTeam);
            }

            conferences.get(0).setDivisions();

            JSONObject metadata = league.getJSONObject("metadata");
            conferences.get(0).division1name = metadata.getString("division_1");
            conferences.get(0).division2name = metadata.getString("division_2");
            conferences.get(0).division1avatar = metadata.getString("division_1_avatar");
            conferences.get(0).division2avatar = metadata.getString("division_2_avatar");
        }

        // Sort teams by wins, then points per game, then team name
        teams.sort(new Comparator<>() {
            public int compare(Team t1, Team t2) {
                if (t1.wins != t2.wins) {
                    return t2.wins - t1.wins; // Sort by wins in descending order
                } else if (t1.pointsPerGame != t2.pointsPerGame) {
                    return Double.compare(t2.pointsPerGame, t1.pointsPerGame); // Sort by points per game in descending order
                } else {
                    String name1 = t1.teamName.equals(t1.username) ? t1.username : t1.username + " (" + t1.teamName + ")";
                    String name2 = t2.teamName.equals(t2.username) ? t2.username : t2.username + " (" + t2.teamName + ")";
                    return name1.compareToIgnoreCase(name2); // Sort by name in ascending order, ignoring case
                }
            }
        });

        // Print sorted standings
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            if (team.teamName.equals(team.username)) {
                System.out.println((i + 1) + ". " + team.username + ": " + team.wins + "-" + team.losses + "; " + team.pointsPerGame);
            } else {
                System.out.println((i + 1) + ". " + team.username + " (" + team.teamName + "): " + team.wins + "-" + team.losses + "; " + team.pointsPerGame);
            }
        }

        // Set teams for GUIs
        StandingsGUI.setTeams(teams);
        PlayoffsGUI.setConferences(conferences);
        ConferenceStandingsGUI.setConferences(conferences);

        // Launch GUIs
        // Application.launch(StandingsGUI.class, new String[]{});
        // Application.launch(PlayoffsGUI.class, new String[]{});
        Application.launch(ConferenceStandingsGUI.class, new String[]{});

    }

}
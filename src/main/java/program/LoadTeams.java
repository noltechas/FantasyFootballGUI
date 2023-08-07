package program;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoadTeams {

    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        BufferedImage originalImage = ImageIO.read(is);

        if (originalImage != null) {
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = resizeImage(originalImage, type);
            ImageIO.write(resizedImage, "png", new File(destinationFile));
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
        BufferedImage resizedImage = new BufferedImage(600, 600, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 600, 600, null);
        g.dispose();

        return resizedImage;
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String[] leagueIds = {"985569785293467648", "985978467361583104", "985571036416544768", "985978629114970112", "985570334562721792", "985570243441487872", "985570133928132608", "985273817578659840"}; // Replace with your actual league IDs

        ArrayList<Team> teams = new ArrayList<>();

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
            JSONObject leagueMetadata = (JSONObject) league.get("metadata");
            String leagueAvatarUrl = "http://sleepercdn.com/avatars/" + league.get("avatar");
            String division1AvatarUrl = (String) leagueMetadata.get("division_1_avatar");
            String division2AvatarUrl = (String) leagueMetadata.get("division_2_avatar");

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
                System.out.println("Loading " + username);
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

                String avatarId = user.optString("avatar");
                JSONObject metaData = user.getJSONObject("metadata");
                String avatarUrl;
                if(metaData.has("avatar"))
                    avatarUrl = metaData.getString("avatar");
                else
                    avatarUrl = "https://sleepercdn.com/avatars/" + avatarId;

                if (isImageBlank(avatarId)) {
                    avatarUrl = "https://pbs.twimg.com/profile_images/1105899703592345600/dhRZDOCB_400x400.png";
                }
                int divisionNumber = (int) settings.get("division");
                String divisionAvatarUrl = division1AvatarUrl;
                if(divisionNumber == 2)
                    divisionAvatarUrl = division2AvatarUrl;

                // Simulate end of season
                wins = 0; // Random number between 0 and 24
                for(int j = 0; j < 24; j++){
                    wins += random.nextInt(2);
                }
                losses = 24 - wins; // Each team plays 24 games
                pointsPerGame = 100 + random.nextGaussian() * 10; // Average of 100 with a standard deviation of 10
                teams.add(new Team(username, teamName, wins, losses, pointsPerGame, avatarUrl, leagueAvatarUrl, divisionAvatarUrl, ""));
            }
        }

        int counter = 1;
        // Loop through your teams and call downloadImage() for each image URL
        for (Team team : teams) {
            System.out.println("Downloading team #" + counter);
            counter++;
            String avatarUrl = team.avatarUrl;
            String leagueAvatarUrl = team.leagueAvatarUrl;
            String divisionAvatarUrl = team.divisionAvatarUrl;

            downloadImage(avatarUrl, "./src/main/resources/teams/" + team.username + "_avatar.png");
            downloadImage(leagueAvatarUrl, "./src/main/resources/teams/" + team.username + "_leagueAvatar.png");
            downloadImage(divisionAvatarUrl, "./src/main/resources/teams/" + team.username + "_divisionAvatar.png");
        }
    }

    public static boolean isImageBlank(String imageUrl) throws IOException {
        URL url = new URL("https://sleepercdn.com/avatars/" + imageUrl);
        if(imageUrl.length() < 2)
            return true;
        InputStream is = url.openStream();
        BufferedImage image = ImageIO.read(is);
        return image == null;
    }
}

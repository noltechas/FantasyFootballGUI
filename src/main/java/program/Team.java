package program;

public class Team {
    String username;
    String teamName;
    int wins;
    int losses;
    double pointsPerGame;

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    String conference;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    String division;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getPointsPerGame() {
        return pointsPerGame;
    }

    public void setPointsPerGame(double pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLeagueAvatarUrl() {
        return leagueAvatarUrl;
    }

    public void setLeagueAvatarUrl(String leagueAvatarUrl) {
        this.leagueAvatarUrl = leagueAvatarUrl;
    }

    public String getDivisionAvatarUrl() {
        return divisionAvatarUrl;
    }

    public void setDivisionAvatarUrl(String divisionAvatarUrl) {
        this.divisionAvatarUrl = divisionAvatarUrl;
    }

    String avatarUrl;
    String leagueAvatarUrl;
    String divisionAvatarUrl;
    String divisionName;

    // Modify the constructor to take the avatar URL as a parameter
    public Team(String username, String teamName, int wins, int losses, double pointsPerGame, String avatarUrl, String leagueAvatarUrl, String divisionAvatarUrl, String divisionName) {
        this.username = username;
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
        this.pointsPerGame = pointsPerGame;
        this.avatarUrl = avatarUrl;
        this.leagueAvatarUrl = leagueAvatarUrl;
        this.divisionAvatarUrl = divisionAvatarUrl;
        this.divisionName = divisionName;
    }
}
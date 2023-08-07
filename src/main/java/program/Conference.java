package program;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Conference {

    public String name;
    public ArrayList<Team> teams;
    public ArrayList<Team> division1;
    public ArrayList<Team> division2;
    public String division1name;
    public String division2name;
    public String division1avatar;
    public String division2avatar;

    public Conference(String name){
        this.name = name;
        teams = new ArrayList<>();
        division1 = new ArrayList<>();
        division2 = new ArrayList<>();
    }

    public void addTeam(Team team){
        this.teams.add(team);
    }

    public ArrayList<Team> getTeams(){
        return this.teams;
    }

    public void setDivisions(){
        for(int i = 0; i < teams.size(); i++){
            if(Objects.equals(teams.get(i).divisionName, teams.get(0).divisionName))
                this.division1.add(teams.get(i));
            else
                this.division2.add(teams.get(i));
        }

        Comparator<Team> comparator = Comparator
                .comparing(Team::getWins)
                .thenComparing(Team::getPointsPerGame);

        this.division1.sort(comparator.reversed());
        this.division2.sort(comparator.reversed());
    }
}

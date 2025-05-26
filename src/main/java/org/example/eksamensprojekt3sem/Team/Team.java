package org.example.eksamensprojekt3sem.Team;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import org.example.eksamensprojekt3sem.Member.Member;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private long teamId;

    @Column(name = "name")
    @NotBlank(message = "Navn skal udfyldes")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Beskrivelse skal udfyldes")
    private String description;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members;

    public Team() {
    }

    public Team(long teamId, String name, String description, boolean active, List<Member> members) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.active = active;
        this.members = members;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}

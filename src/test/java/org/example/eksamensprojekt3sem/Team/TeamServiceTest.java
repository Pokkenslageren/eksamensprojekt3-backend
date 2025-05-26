package org.example.eksamensprojekt3sem.Team;

import org.example.eksamensprojekt3sem.Member.Member;
import org.example.eksamensprojekt3sem.Member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TeamService teamService;

    // Hent alle teams
    @Test
    void shouldReturnAllTeams() {
        Team t1 = new Team();
        Team t2 = new Team();
        when(teamRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Team> teams = teamService.getAllTeams();

        assertEquals(2, teams.size());
    }

    // Hent alle medlemmer
    @Test
    void shouldReturnAllMembers() {
        Member m1 = new Member();
        Member m2 = new Member();
        when(memberRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Member> members = teamService.getMembers();

        assertEquals(2, members.size());
    }

    // Tilføj medlem
    @Test
    void shouldAddMember() {
        Member member = new Member();
        member.setName("Test");

        when(memberRepository.save(member)).thenReturn(member);

        Member saved = teamService.addMember(member);

        assertEquals("Test", saved.getName());
    }

    // ✅ Opdater team
    @Test
    void shouldUpdateTeam() {
        Team existing = new Team();
        existing.setTeamId(1L);
        existing.setName("Old Team");
        existing.setDescription("Old desc");
        existing.setActive(true);

        Team updated = new Team();
        updated.setName("New Team");
        updated.setDescription("New desc");
        updated.setActive(false);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(teamRepository.save(any(Team.class))).thenReturn(existing);

        Optional<Team> result = teamService.updateTeam(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("New Team", result.get().getName());
        assertFalse(result.get().isActive());
    }

    // Fejl ved ugyldigt ID ved opdatering
    @Test
    void shouldThrowWhenUpdatingInvalidId() {
        Team team = new Team();
        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(0, team));
    }

    // Slet team
    @Test
    void shouldDeleteExistingTeam() {
        when(teamRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teamRepository).deleteById(1L);

        boolean result = teamService.deleteTeam(1L);

        assertTrue(result);
        verify(teamRepository).deleteById(1L);
    }

    // Forsøg på at slette team der ikke findes
    @Test
    void shouldNotDeleteNonExistingTeam() {
        when(teamRepository.existsById(99L)).thenReturn(false);

        boolean result = teamService.deleteTeam(99L);

        assertFalse(result);
        verify(teamRepository, never()).deleteById(anyLong());
    }

    // Fejl ved ugyldigt ID ved sletning
    @Test
    void shouldThrowWhenDeletingInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> teamService.deleteTeam(0));
    }
}

package org.example.eksamensprojekt3sem.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eksamensprojekt3sem.Session.SessionController.*;
import org.example.eksamensprojekt3sem.Team.Team;
import org.example.eksamensprojekt3sem.Team.TeamRepository;
import org.example.eksamensprojekt3sem.Exercise.Exercise;
import org.example.eksamensprojekt3sem.Exercise.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long teamId;
    private Long exerciseId;


    @BeforeEach
    void setUp() {
        // Gem test team og øvelse i H2-databasen
        Team team = new Team();
        team.setName("U13");
        team.setDescription("Hold for spillere under 13 år"); // <-- Tilføj dette
        teamId = teamRepository.save(team).getTeamId();

        Exercise exercise = new Exercise();
        exercise.setName("Dribling");
        exercise.setDescription("Drible gennem kegler");
        exerciseId = exerciseRepository.save(exercise).getExerciseId();
    }

    @Test
    void testCreateAndRetrieveSession() throws Exception {
        // Forbered DTO-objekter
        SessionCreateDTO sessionDTO = new SessionCreateDTO();
        sessionDTO.setDateTime(LocalDateTime.now().plusDays(1));
        sessionDTO.setLocation("Træningsbane 1");

        TeamRefDTO teamRef = new TeamRefDTO();
        teamRef.setTeamId(teamId);
        sessionDTO.setTeam(teamRef);

        SessionExerciseDTO exerciseDTO = new SessionExerciseDTO();
        exerciseDTO.setExerciseId(exerciseId);
        exerciseDTO.setOrderNum(1);

        SessionCreationRequest request = new SessionCreationRequest();
        request.setSession(sessionDTO);
        request.setExercises(List.of(exerciseDTO));

        // Udfør POST-request for at oprette session
        String response = mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exercises").exists())
                .andExpect(jsonPath("$.location").value("Træningsbane 1"))
                .andExpect(jsonPath("$.team.name").value("U13"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse svar og hent sessionId
        SessionDTO createdSession = objectMapper.readValue(response, SessionDTO.class);

        // Bekræft GET virker
        mockMvc.perform(get("/sessions/" + createdSession.getSessionId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(createdSession.getSessionId()))
                .andExpect(jsonPath("$.team.name").value("U13"));
    }
}

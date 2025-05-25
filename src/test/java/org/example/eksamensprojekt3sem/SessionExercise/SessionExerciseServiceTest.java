package org.example.eksamensprojekt3sem.SessionExercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionExerciseServiceTest {

    @Mock
    private SessionExerciseRepository sessionExerciseRepository;

    @InjectMocks
    private SessionExerciseService sessionExerciseService;

    // Tester at alle SessionExercise-objekter hentes korrekt
    @Test
    void shouldReturnAllSessionExercises() {
        SessionExercise s1 = new SessionExercise();
        SessionExercise s2 = new SessionExercise();
        when(sessionExerciseRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SessionExercise> result = sessionExerciseService.getAllSessionExercises();

        assertEquals(2, result.size());
    }

    // Tester at et enkelt SessionExercise hentes ud fra ID
    @Test
    void shouldReturnSessionExerciseById() {
        SessionExerciseId id = new SessionExerciseId(1L, 2L);
        SessionExercise se = new SessionExercise();
        when(sessionExerciseRepository.findById(id)).thenReturn(Optional.of(se));

        Optional<SessionExercise> result = sessionExerciseService.getSessionExerciseById(id);

        assertTrue(result.isPresent());
    }

    // Tester oprettelse af en ny SessionExercise
    @Test
    void shouldCreateSessionExercise() {
        SessionExercise se = new SessionExercise();
        se.setNotes("Test note");
        when(sessionExerciseRepository.save(se)).thenReturn(se);

        SessionExercise saved = sessionExerciseService.createSessionExercise(se);

        assertEquals("Test note", saved.getNotes());
    }

    // Tester opdatering af en eksisterende SessionExercise
    @Test
    void shouldUpdateSessionExercise() {
        SessionExerciseId id = new SessionExerciseId(1L, 2L);

        SessionExercise existing = new SessionExercise();
        existing.setOrderNum(1);
        existing.setNotes("Old");

        SessionExercise updated = new SessionExercise();
        updated.setOrderNum(2);
        updated.setNotes("Updated note");

        when(sessionExerciseRepository.findById(id)).thenReturn(Optional.of(existing));
        when(sessionExerciseRepository.save(existing)).thenReturn(existing);

        SessionExercise result = sessionExerciseService.updateSessionExercise(id, updated);

        assertEquals(2, result.getOrderNum());
        assertEquals("Updated note", result.getNotes());
    }

    // Tester at exception kastes hvis SessionExercise ikke findes ved opdatering
    @Test
    void shouldThrowWhenUpdatingNonExisting() {
        SessionExerciseId id = new SessionExerciseId(99L, 99L);
        SessionExercise update = new SessionExercise();

        when(sessionExerciseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionExerciseService.updateSessionExercise(id, update));
    }

    // Tester sletning af en SessionExercise
    @Test
    void shouldDeleteSessionExercise() {
        SessionExerciseId id = new SessionExerciseId(1L, 2L);

        doNothing().when(sessionExerciseRepository).deleteById(id);

        sessionExerciseService.deleteSessionExercise(id);

        verify(sessionExerciseRepository, times(1)).deleteById(id);
    }
}

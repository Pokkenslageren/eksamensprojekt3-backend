package org.example.eksamensprojekt3sem.Member;

import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    // Tester at alle medlemmer hentes korrekt fra repository
    @Test
    void shouldReturnAllMembers() {
        Member m1 = new Member();
        Member m2 = new Member();
        when(memberRepository.findAll()).thenReturn(List.of(m1, m2));

        List<Member> result = memberService.getAllMembers();

        assertEquals(2, result.size());
    }

    // Tester at en specifik medlem kan hentes ud fra ID
    @Test
    void shouldReturnMemberById() {
        Member m = new Member();
        m.setMemberId(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(m));

        Optional<Member> result = memberService.getMemberById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getMemberId());
    }

    // Tester at exception kastes ved ugyldigt ID
    @Test
    void shouldThrowWhenGettingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> memberService.getMemberById(0));
    }

    // Tester at et medlem kan tilføjes korrekt
    @Test
    void shouldAddMember() {
        Member m = new Member();
        m.setName("Anna");

        when(memberRepository.save(m)).thenReturn(m);

        Member saved = memberService.addMember(m);

        assertEquals("Anna", saved.getName());
    }

    // Tester at et medlem kan opdateres korrekt
    @Test
    void shouldUpdateMember() {
        Member existing = new Member();
        existing.setMemberId(1L);
        existing.setName("Old Name");

        Member updated = new Member();
        updated.setName("New Name");
        updated.setEmail("test@example.com");
        updated.setPhone("12345678");
        updated.setAddress("Main Street 1");
        updated.setDateOfBirth(new Date());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(memberRepository.save(any(Member.class))).thenReturn(existing);

        Optional<Member> result = memberService.updateMember(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        assertEquals("test@example.com", result.get().getEmail());
    }

    // Tester at exception kastes ved ugyldigt ID ved opdatering
    @Test
    void shouldThrowWhenUpdatingWithInvalidId() {
        Member m = new Member();
        assertThrows(IllegalArgumentException.class, () -> memberService.updateMember(0, m));
    }

    // Tester at et medlem slettes korrekt hvis ID findes
    @Test
    void shouldDeleteExistingMember() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        doNothing().when(memberRepository).deleteById(1L);

        boolean result = memberService.deleteMember(1L);

        assertTrue(result);
        verify(memberRepository).deleteById(1L);
    }

    // Tester at sletning ikke foretages hvis ID ikke findes
    @Test
    void shouldNotDeleteNonExistingMember() {
        when(memberRepository.existsById(99L)).thenReturn(false);

        boolean result = memberService.deleteMember(99L);

        assertFalse(result);
        verify(memberRepository, never()).deleteById(anyLong());
    }

    // Tester at exception kastes ved ugyldigt ID ved sletning
    @Test
    void shouldThrowWhenDeletingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> memberService.deleteMember(0));
    }

    // Tester søgning efter navn (case-insensitive)
    @Test
    void shouldFindByNameContainingIgnoreCase() {
        Member m = new Member();
        m.setName("Anders");

        when(memberRepository.findByNameContainingIgnoreCase("and")).thenReturn(List.of(m));

        List<Member> result = memberService.findByNameContainingIgnoreCase("and");

        assertEquals(1, result.size());
        assertEquals("Anders", result.get(0).getName());
    }

    // Tester søgning efter e-mail (case-insensitive)
    @Test
    void shouldFindByEmailContainingIgnoreCase() {
        Member m = new Member();
        m.setEmail("user@example.com");

        when(memberRepository.findByEmailContainingIgnoreCase("user")).thenReturn(List.of(m));

        List<Member> result = memberService.findByEmailContainingIgnoreCase("user");

        assertEquals(1, result.size());
        assertEquals("user@example.com", result.get(0).getEmail());
    }

    // Tester søgning efter betalingsstatus (enum)
    @Test
    void shouldFindByPaymentStatus() {
        Member m = new Member();
        m.setPaymentStatus(PaymentStatus.UNPAID);

        when(memberRepository.findByPaymentStatus(PaymentStatus.UNPAID)).thenReturn(List.of(m));

        List<Member> result = memberService.findByPaymentStatus(PaymentStatus.UNPAID);

        assertEquals(1, result.size());
        assertEquals(PaymentStatus.UNPAID, result.get(0).getPaymentStatus());
    }

    // Tester opdatering af betalingsstatus
    @Test
    void shouldSetPaymentStatus() {
        Member m = new Member();
        m.setMemberId(1L);
        m.setPaymentStatus(PaymentStatus.UNPAID);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(m));
        when(memberRepository.save(any(Member.class))).thenReturn(m);

        Optional<Member> result = memberService.setPaymentStatus(1L, PaymentStatus.PAID);

        assertTrue(result.isPresent());
        assertEquals(PaymentStatus.PAID, result.get().getPaymentStatus());
    }

    // Tester exception ved ugyldigt ID i setPaymentStatus
    @Test
    void shouldThrowWhenSettingPaymentStatusWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> memberService.setPaymentStatus(0, PaymentStatus.PAID));
    }
}

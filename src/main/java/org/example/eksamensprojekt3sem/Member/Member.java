package org.example.eksamensprojekt3sem.Member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.eksamensprojekt3sem.Enums.PaymentStatus;
import org.example.eksamensprojekt3sem.Team.Team;

import java.util.Date;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long memberId;

    @Column(name = "name")
    @NotBlank(message = "Navn skal udfyldes")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Email skal udfyldes")
    private String email;

    @Column(name = "phone")
    @NotBlank(message = "Telefonnummer skal udfyldes")
    private String phone;

    @Column(name = "address")
    @NotBlank(message = "Adresse skal udfyldes")
    private String address;

    @Column(name = "date_of_birth")
    @NotNull(message = "Dato skal udfyldes")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    @NotNull(message = "Betalingstatus skal udfyldes")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public Member() {
    }

    public Member(String name, long memberId, String email, String phone, String address, Date dateOfBirth, PaymentStatus paymentStatus, Team team) {
        this.name = name;
        this.memberId = memberId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.paymentStatus = paymentStatus;
        this.team = team;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

package com.isaiah.Church.Management.System.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "ministry_memberships",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"member_id", "ministry_id"}
        )
    }
)
public class MinistryMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Integer membershipId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ministry_id", nullable = false)
    private Ministry ministry;

    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    public MinistryMembership() {
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Ministry getMinistry() {
        return ministry;
    }

    public void setMinistry(Ministry ministry) {
        this.ministry = ministry;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }
}
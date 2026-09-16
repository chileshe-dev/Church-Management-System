package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.model.Ministry;
import com.isaiah.Church.Management.System.model.MinistryMembership;
import com.isaiah.Church.Management.System.repository.MemberRepository;
import com.isaiah.Church.Management.System.repository.MinistryMembershipRepository;
import com.isaiah.Church.Management.System.repository.MinistryRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MinistryMembershipService {

    private final MinistryMembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final MinistryRepository ministryRepository;

    public MinistryMembershipService(
            MinistryMembershipRepository membershipRepository,
            MemberRepository memberRepository,
            MinistryRepository ministryRepository) {

        this.membershipRepository = membershipRepository;
        this.memberRepository = memberRepository;
        this.ministryRepository = ministryRepository;
    }

    // Join a ministry
    public MinistryMembership joinMinistry(
            Integer memberId,
            Integer ministryId) {

        // Check member
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Member not found."));

        // Check ministry
        Ministry ministry = ministryRepository
                .findById(ministryId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Ministry not found."));

        // Prevent duplicate membership
        if (membershipRepository
                .existsByMemberMemberIdAndMinistryMinistryId(
                        memberId,
                        ministryId)) {

            throw new IllegalArgumentException(
                    "You have already joined this ministry.");
        }

        // Create membership
        MinistryMembership membership =
                new MinistryMembership();

        membership.setMember(member);
        membership.setMinistry(ministry);
        membership.setJoinedDate(LocalDate.now());

        return membershipRepository.save(membership);
    }

    // Get all ministries belonging to a member
    public List<MinistryMembership> getMemberMinistries(
            Integer memberId) {

        return membershipRepository
                .findByMemberMemberId(memberId);
    }

    // Get all members belonging to a ministry
    public List<MinistryMembership> getMinistryMembers(
            Integer ministryId) {

        return membershipRepository
                .findByMinistryMinistryId(ministryId);
    }

    // Leave a ministry
    public void leaveMinistry(
            Integer memberId,
            Integer ministryId) {

        MinistryMembership membership =
                membershipRepository
                        .findByMemberMemberIdAndMinistryMinistryId(
                                memberId,
                                ministryId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "You are not a member of this ministry."));

        membershipRepository.delete(membership);
    }
}
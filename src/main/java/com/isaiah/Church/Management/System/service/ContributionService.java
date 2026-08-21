package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.repository.ContributionRepository;
import com.isaiah.Church.Management.System.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService {

    private final ContributionRepository contributionRepository;
    private final MemberRepository memberRepository;

    public ContributionService(
            ContributionRepository contributionRepository,
            MemberRepository memberRepository) {

        this.contributionRepository = contributionRepository;
        this.memberRepository = memberRepository;
    }

    // ==========================
    // CREATE
    // ==========================

    public Contribution saveContribution(Contribution contribution) {

        Integer memberId = contribution.getMember().getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        contribution.setMember(member);

        return contributionRepository.save(contribution);
    }

    // ==========================
    // UPDATE
    // ==========================

    public Contribution updateContribution(
            Integer id,
            Contribution updatedContribution) {

        Contribution contribution =
                contributionRepository.findById(id).orElse(null);

        if (contribution == null) {
            return null;
        }

        Member member = memberRepository.findById(
                updatedContribution.getMember().getMemberId())
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        contribution.setContributionType(
                updatedContribution.getContributionType());

        contribution.setAmount(
                updatedContribution.getAmount());

        contribution.setContributionDate(
                updatedContribution.getContributionDate());

        contribution.setMember(member);

        return contributionRepository.save(contribution);
    }

    // ==========================
    // READ ALL
    // ==========================

    public List<Contribution> getAllContributions() {
        return contributionRepository.findAll();
    }

    // ==========================
    // READ ONE
    // ==========================

    public Contribution getContributionById(Integer id) {
        return contributionRepository.findById(id).orElse(null);
    }

    // ==========================
    // DELETE
    // ==========================

    public void deleteContribution(Integer id) {
        contributionRepository.deleteById(id);
    }
}
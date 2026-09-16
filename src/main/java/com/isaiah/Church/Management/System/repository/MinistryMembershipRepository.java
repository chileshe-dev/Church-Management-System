package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.MinistryMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MinistryMembershipRepository
        extends JpaRepository<MinistryMembership, Integer> {

    List<MinistryMembership> findByMemberMemberId(Integer memberId);

    List<MinistryMembership> findByMinistryMinistryId(Integer ministryId);

    Optional<MinistryMembership> findByMemberMemberIdAndMinistryMinistryId(
            Integer memberId,
            Integer ministryId);

    boolean existsByMemberMemberIdAndMinistryMinistryId(
            Integer memberId,
            Integer ministryId);
}
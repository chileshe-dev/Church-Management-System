package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContributionRepository
        extends JpaRepository<Contribution, Integer> {
   
    List<Contribution> findByMemberMemberId(Integer memberId);            

    @Query("SELECT COALESCE(SUM(c.amount),0) FROM Contribution c")
    Double getTotalContributionAmount();

    // Contributions grouped by type
    @Query("""
            SELECT c.contributionType, COUNT(c)
            FROM Contribution c
            GROUP BY c.contributionType
            """)
    List<Object[]> getContributionTypes();

    // Monthly contribution totals
    @Query("""
            SELECT MONTH(c.contributionDate),
                   SUM(c.amount)
            FROM Contribution c
            GROUP BY MONTH(c.contributionDate)
            ORDER BY MONTH(c.contributionDate)
            """)
    List<Object[]> getMonthlyContributions();
}
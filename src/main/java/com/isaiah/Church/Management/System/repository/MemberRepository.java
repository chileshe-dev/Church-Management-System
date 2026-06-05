package com.isaiah.Church.Management.System.repository;
import com.isaiah.Church.Management.System.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Member saveMember(Member member) {
        return repository.save(member);
    }

    // READ ALL
    public List<Member> getAllMembers() {
        return repository.findAll();
    }

    // READ ONE
    public Member getMemberById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public Member updateMember(Integer id, Member updatedMember) {

        Member member = repository.findById(id).orElse(null);

        if (member != null) {
            member.setFirstName(updatedMember.getFirstName());
            member.setLastName(updatedMember.getLastName());
            member.setEmail(updatedMember.getEmail());
            member.setPhone(updatedMember.getPhone());
            member.setAddress(updatedMember.getAddress());

            return repository.save(member);
        }

        return null;
    }

    // DELETE
    public void deleteMember(Integer id) {
        repository.deleteById(id);
    }
}
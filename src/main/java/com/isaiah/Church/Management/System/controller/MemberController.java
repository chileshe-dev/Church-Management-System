package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Member addMember(@RequestBody Member member) {
        return service.saveMember(member);
    }

    // READ ALL
    @GetMapping
    public List<Member> getAllMembers() {
        return service.getAllMembers();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Member getMember(@PathVariable Integer id) {
        return service.getMemberById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Member updateMember(
            @PathVariable Integer id,
            @RequestBody Member member) {

        return service.updateMember(id, member);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Integer id) {
        service.deleteMember(id);
    }
}
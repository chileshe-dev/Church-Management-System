package com.isaiah.Church.Management.System.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberDashboardController {

    @GetMapping("/member-dashboard")
    public String memberDashboard(HttpSession session) {

        Object memberAccountId =
                session.getAttribute("memberAccountId");

        // Member is not logged in
        if (memberAccountId == null) {
            return "redirect:/member-login.html";
        }

        // Member is logged in
        return "forward:/member-dashboard.html";
    }
}


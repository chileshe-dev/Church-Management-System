// =============================
// Church Management System
// Dashboard
// =============================

let currentUser = null;

let monthlyChart = null;
let typeChart = null;
let attendanceChart = null;


// ==========================================
// Load Logged-in User
// ==========================================

fetch("/users/me")
    .then(response => {

        if (!response.ok) {
            window.location.href = "/login.html";
            return;
        }

        return response.json();
    })

    .then(user => {

        if (!user) return;

        currentUser = user;

        document.getElementById("username").textContent = user.username;
        document.getElementById("role").textContent = user.role;

        configureMenu(user.role);

        loadPage("dashboard");

    })

    .catch(error => {

        console.error(error);

        window.location.href = "/login.html";

    });


// ==========================================
// Configure Sidebar
// ==========================================

function configureMenu(role) {

    // ADMIN sees everything
    if (role === "ADMIN") {
        return;
    }

    if (role === "PASTOR") {

        document.getElementById("usersMenu").style.display = "none";
        document.getElementById("contributionsMenu").style.display = "none";
        document.getElementById("ministriesMenu").style.display = "none";
    }

    if (role === "TREASURER") {

        document.getElementById("usersMenu").style.display = "none";
        document.getElementById("membersMenu").style.display = "none";
        document.getElementById("attendanceMenu").style.display = "none";
        document.getElementById("eventsMenu").style.display = "none";
        document.getElementById("ministriesMenu").style.display = "none";
    }

}


// ==========================================
// Page Initializers
// ==========================================

const pageInitializers = {

    dashboard: loadDashboard,

    members: () => {
        if (typeof initMembers === "function") initMembers();
    },

    contributions: () => {
        if (typeof initContributions === "function") initContributions();
    },

    events: () => {
        if (typeof initEvents === "function") initEvents();
    },

    attendance: () => {
        if (typeof initAttendance === "function") initAttendance();
    },

    ministries: () => {
        if (typeof initMinistries === "function") initMinistries();
    },

    users: () => {
        if (typeof initUsers === "function") initUsers();
    },

    reports: () => {
    if (typeof initReports === "function") initReports();
}

};


// ==========================================
// Load Page
// ==========================================

function loadPage(page) {

    fetch(`pages/${page}-content.html`)
        .then(response => response.text())
        .then(html => {

            document.getElementById("content").innerHTML = html;

            if (pageInitializers[page]) {
                pageInitializers[page]();
            }

        })
        .catch(console.error);

}


// ==========================================
// Dashboard
// ==========================================

function loadDashboard() {

    document.getElementById("today").textContent =
        new Date().toDateString();

    fetch("/dashboard")

        .then(response => response.json())

        .then(data => {

            document.getElementById("members").textContent = data.totalMembers;
            document.getElementById("contributions").textContent = data.totalContributions;
            document.getElementById("events").textContent = data.totalEvents;
            document.getElementById("attendance").textContent = data.totalAttendance;
            document.getElementById("ministries").textContent = data.totalMinistries;
            document.getElementById("users").textContent = data.totalUsers;

            loadMonthlyChart();
            loadContributionTypeChart();
            loadAttendanceChart();

        });

}


// ==========================================
// Monthly Chart
// ==========================================

function loadMonthlyChart() {

    fetch("/reports/monthly-contributions")

        .then(r => r.json())

        .then(data => {

            if (monthlyChart)
                monthlyChart.destroy();

            monthlyChart = new Chart(

                document.getElementById("monthlyChart"),

                {

                    type: "bar",

                    data: {

                        labels: data.map(x => x.month),

                        datasets: [{

                            label: "Amount",

                            data: data.map(x => x.amount)

                        }]

                    }

                }

            );

        });

}


// ==========================================
// Contribution Types
// ==========================================

function loadContributionTypeChart() {

    fetch("/reports/contribution-types")

        .then(r => r.json())

        .then(data => {

            if (typeChart)
                typeChart.destroy();

            typeChart = new Chart(

                document.getElementById("typeChart"),

                {

                    type: "pie",

                    data: {

                        labels: data.map(x => x.type),

                        datasets: [{

                            data: data.map(x => x.count)

                        }]

                    }

                }

            );

        });

}


// ==========================================
// Attendance Chart
// ==========================================

function loadAttendanceChart() {

    fetch("/reports/attendance-summary")

        .then(r => r.json())

        .then(data => {

            if (attendanceChart)
                attendanceChart.destroy();

            attendanceChart = new Chart(

                document.getElementById("attendanceChart"),

                {

                    type: "doughnut",

                    data: {

                        labels: data.map(x => x.status),

                        datasets: [{

                            data: data.map(x => x.total)

                        }]

                    }

                }

            );

        });

}


// ==========================================
// Sidebar Navigation
// ==========================================

document.querySelectorAll("[data-page]").forEach(link => {

    link.addEventListener("click", function (e) {

        e.preventDefault();

        loadPage(this.dataset.page);

    });

});


// ==========================================
// Logout
// ==========================================

document.getElementById("logoutBtn").addEventListener("click", () => {

    fetch("/logout", {
        method: "POST"
    })
    .then(() => {

        window.location.href = "/login.html";

    });

});
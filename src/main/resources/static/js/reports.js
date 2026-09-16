// ==========================================
// REPORTS
// ==========================================

let reportsMonthlyChart = null;
let reportsContributionTypeChart = null;
let reportsAttendanceChart = null;


// ==========================================
// Initialize Reports
// ==========================================

function initReports() {

    console.log("Reports initialized");

    loadReportData();

}


// ==========================================
// Load All Report Data
// ==========================================

function loadReportData() {

    loadMonthlyContributions();

    loadContributionTypes();

    loadAttendanceSummary();

}


// ==========================================
// Monthly Contributions
// ==========================================

function loadMonthlyContributions() {

    fetch("/reports/monthly-contributions")

        .then(response => {

            if (!response.ok) {
                throw new Error(
                    "Unable to load monthly contributions"
                );
            }

            return response.json();

        })

        .then(data => {

            console.log(
                "Monthly Contributions:",
                data
            );


            // Calculate total

            let total = 0;

            data.forEach(item => {

                total += Number(item.amount);

            });


            document.getElementById(
                "reportTotalContributions"
            ).textContent =
                "K " + total.toFixed(2);


            // Chart

            const canvas =
                document.getElementById(
                    "reportsMonthlyChart"
                );

            if (!canvas) return;


            if (reportsMonthlyChart) {

                reportsMonthlyChart.destroy();

            }


            reportsMonthlyChart =
                new Chart(

                    canvas,

                    {

                        type: "bar",

                        data: {

                            labels:
                                data.map(
                                    item => item.month
                                ),

                            datasets: [

                                {

                                    label:
                                        "Contributions (K)",

                                    data:
                                        data.map(
                                            item =>
                                                Number(
                                                    item.amount
                                                )
                                        )

                                }

                            ]

                        },

                        options: {

                            responsive: true,

                            maintainAspectRatio: false,

                            scales: {

                                y: {

                                    beginAtZero: true

                                }

                            }

                        }

                    }

                );

        })

        .catch(error => {

            console.error(
                "Monthly Contributions Error:",
                error
            );

        });

}


// ==========================================
// Contribution Types
// ==========================================

function loadContributionTypes() {

    fetch("/reports/contribution-types")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Unable to load contribution types"
                );

            }

            return response.json();

        })

        .then(data => {

            console.log(
                "Contribution Types:",
                data
            );


            // Total records

            let totalRecords = 0;

            data.forEach(item => {

                totalRecords += Number(item.count);

            });


            document.getElementById(
                "reportContributionRecords"
            ).textContent =
                totalRecords;


            // Chart

            const canvas =
                document.getElementById(
                    "reportsContributionTypeChart"
                );

            if (!canvas) return;


            if (reportsContributionTypeChart) {

                reportsContributionTypeChart.destroy();

            }


            reportsContributionTypeChart =
                new Chart(

                    canvas,

                    {

                        type: "pie",

                        data: {

                            labels:
                                data.map(
                                    item => item.type
                                ),

                            datasets: [

                                {

                                    data:
                                        data.map(
                                            item =>
                                                Number(
                                                    item.count
                                                )
                                        )

                                }

                            ]

                        },

                        options: {

                            responsive: true,

                            maintainAspectRatio: false

                        }

                    }

                );

        })

        .catch(error => {

            console.error(
                "Contribution Types Error:",
                error
            );

        });

}


// ==========================================
// Attendance Summary
// ==========================================

function loadAttendanceSummary() {

    fetch("/reports/attendance-summary")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Unable to load attendance summary"
                );

            }

            return response.json();

        })

        .then(data => {

            console.log(
                "Attendance Summary:",
                data
            );


            let present = 0;
            let total = 0;


            data.forEach(item => {

                const count =
                    Number(item.total);

                total += count;


                if (
                    item.status.toLowerCase() ===
                    "present"
                ) {

                    present += count;

                }

            });


            let percentage = 0;


            if (total > 0) {

                percentage =
                    Math.round(
                        (present / total) * 100
                    );

            }


            document.getElementById(
                "reportAttendancePercentage"
            ).textContent =
                percentage + "%";


            // Chart

            const canvas =
                document.getElementById(
                    "reportsAttendanceChart"
                );

            if (!canvas) return;


            if (reportsAttendanceChart) {

                reportsAttendanceChart.destroy();

            }


            reportsAttendanceChart =
                new Chart(

                    canvas,

                    {

                        type: "doughnut",

                        data: {

                            labels:
                                data.map(
                                    item =>
                                        item.status
                                ),

                            datasets: [

                                {

                                    data:
                                        data.map(
                                            item =>
                                                Number(
                                                    item.total
                                                )
                                        )

                                }

                            ]

                        },

                        options: {

                            responsive: true,

                            maintainAspectRatio: false

                        }

                    }

                );

        })

        .catch(error => {

            console.error(
                "Attendance Error:",
                error
            );

        });

}


// ==========================================
// Export Buttons
// ==========================================

document.addEventListener(
    "click",
    function (event) {

        if (
            event.target.closest("#exportPdfBtn")
        ) {

            alert(
                "PDF export will be added next."
            );

        }


        if (
            event.target.closest("#exportExcelBtn")
        ) {

            alert(
                "Excel export will be added next."
            );

        }

    }
);


function exportPDF() {

    window.location.href = "/reports/export/pdf";

}

function exportExcel() {

    alert("Excel export will be added next.");

}
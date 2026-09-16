// ===========================
// Global Variables
// ===========================

let editingAttendanceId = null;


// ===========================
// Initialize Attendance
// ===========================

function initAttendance() {

 loadAttendanceMembersDropdown() ;
    loadEventsDropdown();
    loadAttendance();

    document.getElementById("addAttendanceBtn").onclick = () => {

        editingAttendanceId = null;

        document.getElementById("attendanceModalTitle").innerText =
            "Mark Attendance";

        clearAttendanceForm();

        document.getElementById("attendanceModal").style.display = "block";

    };

    document.getElementById("cancelAttendanceBtn").onclick = () => {

        document.getElementById("attendanceModal").style.display = "none";

    };

    document.getElementById("saveAttendanceBtn").onclick =
        saveAttendance;

    document.getElementById("searchAttendance")
        .addEventListener("keyup", function () {

            const value = this.value.toLowerCase();

            document.querySelectorAll("#attendanceBody tr")
                .forEach(row => {

                    row.style.display =
                        row.innerText.toLowerCase().includes(value)
                            ? ""
                            : "none";

                });

        });

}


// ===========================
// Load Members
// ===========================
 function loadAttendanceMembersDropdown() {

    fetch("/members")
        .then(response => response.json())
        .then(data => {

            const select = document.getElementById("attendanceMember");

            select.innerHTML = "";

            data.forEach(member => {

                select.innerHTML += `

                    <option value="${member.memberId}">

                        ${member.firstName} ${member.lastName}

                    </option>

                `;

            });

        });

}


// ===========================
// Load Events
// ===========================

function loadEventsDropdown() {

    fetch("/events/attendance-eligible")
        .then(response => response.json())
        .then(data => {

            const select = document.getElementById("attendanceEvent");

            select.innerHTML = "";

            data.forEach(event => {

                select.innerHTML += `

                    <option value="${event.eventId}">

                        ${event.eventName}

                    </option>

                `;

            });

        });

}


// ===========================
// Load Attendance
// ===========================

function loadAttendance() {

    fetch("/attendance")
        .then(response => response.json())
        .then(data => {

            const body = document.getElementById("attendanceBody");

            body.innerHTML = "";

            data.forEach(attendance => {

                body.innerHTML += `

                    <tr>

                        <td>${attendance.attendanceId}</td>

                        <td>${attendance.member.firstName} ${attendance.member.lastName}</td>

                        <td>${attendance.event.eventName}</td>

                        <td>${attendance.status}</td>

                        <td>

                            <button
                                class="edit-btn"
                                onclick="editAttendance(${attendance.attendanceId})">

                                Edit

                            </button>

                            <button
                                class="delete-btn"
                                onclick="deleteAttendance(${attendance.attendanceId})">

                                Delete

                            </button>

                        </td>

                    </tr>

                `;

            });

        });

}


// ===========================
// Save Attendance
// ===========================

function saveAttendance() {

    const attendance = {

        member: {
            memberId:
                parseInt(
                    document.getElementById(
                        "attendanceMember"
                    ).value
                )
        },

        event: {
            eventId:
                parseInt(
                    document.getElementById(
                        "attendanceEvent"
                    ).value
                )
        },

        status:
            document.getElementById(
                "attendanceStatus"
            ).value

    };

    let url = "/attendance";
    let method = "POST";

    if (editingAttendanceId != null) {

        url = `/attendance/${editingAttendanceId}`;
        method = "PUT";
    }

    fetch(url, {

        method: method,

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(attendance)

    })
    .then(async response => {

        if (!response.ok) {

            const message = await response.text();

            throw new Error(
                message ||
                "Unable to save attendance."
            );
        }

        return response.json();

    })
    .then(() => {

        document.getElementById(
            "attendanceModal"
        ).style.display = "none";

        loadAttendance();

    })
    .catch(error => {

        console.error(
            "Error saving attendance:",
            error
        );

        alert(
            error.message ||
            "Unable to save attendance."
        );

    });
}


// ===========================
// Edit Attendance
// ===========================

function editAttendance(id) {

    fetch(`/attendance/${id}`)
        .then(response => response.json())
        .then(attendance => {

            editingAttendanceId = id;

            document.getElementById("attendanceModalTitle").innerText =
                "Edit Attendance";

            document.getElementById("attendanceMember").value =
                attendance.member.memberId;

            document.getElementById("attendanceEvent").value =
                attendance.event.eventId;

            document.getElementById("attendanceStatus").value =
                attendance.status;

            document.getElementById("attendanceModal").style.display =
                "block";

        });

}


// ===========================
// Delete Attendance
// ===========================

function deleteAttendance(id) {

    if (!confirm("Delete this attendance record?")) {

        return;

    }

    fetch(`/attendance/${id}`, {

        method: "DELETE"

    })
    .then(() => {

        loadAttendance();

    });

}


// ===========================
// Clear Form
// ===========================

function clearAttendanceForm() {

    document.getElementById("attendanceMember").selectedIndex = 0;

    document.getElementById("attendanceEvent").selectedIndex = 0;

    document.getElementById("attendanceStatus").selectedIndex = 0;

}
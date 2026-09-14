document.addEventListener("DOMContentLoaded", loadAttendance);

async function loadAttendance() {

    const loading = document.getElementById("loading");
    const error = document.getElementById("error");
    const noAttendance = document.getElementById("no-attendance");
    const attendanceList = document.getElementById("attendance-list");

    try {

        const response = await fetch("/member-attendance");

        // Member is not logged in
        if (response.status === 401) {
            window.location.href = "/member-login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Unable to load attendance history.");
        }

        const attendance = await response.json();

        loading.classList.add("hidden");

        // No attendance records
        if (attendance.length === 0) {
            noAttendance.classList.remove("hidden");
            return;
        }

        attendanceList.innerHTML = "";

        attendance.forEach(record => {

            const attendanceCard = document.createElement("div");

            attendanceCard.className = "attendance-card";

            attendanceCard.innerHTML = `
                <div class="attendance-header">
                    <h2>${escapeHtml(
                        record.event?.eventName || "Unknown Event"
                    )}</h2>

                    <span class="status ${getStatusClass(record.status)}">
                        ${escapeHtml(record.status || "Unknown")}
                    </span>
                </div>

                <div class="attendance-details">

                    <p>
                        <strong>📅 Date:</strong>
                        ${formatDate(record.event?.eventDate)}
                    </p>

                    <p>
                        <strong>📍 Location:</strong>
                        ${escapeHtml(
                            record.event?.location || "Location not specified"
                        )}
                    </p>

                </div>
            `;

            attendanceList.appendChild(attendanceCard);
        });

    } catch (err) {

        loading.classList.add("hidden");

        error.textContent =
            err.message || "Something went wrong.";

        error.classList.remove("hidden");
    }
}


function formatDate(dateString) {

    if (!dateString) {
        return "Date not specified";
    }

    const date = new Date(dateString + "T00:00:00");

    return date.toLocaleDateString("en-US", {
        weekday: "short",
        day: "numeric",
        month: "long",
        year: "numeric"
    });
}


function getStatusClass(status) {

    if (!status) {
        return "unknown";
    }

    return status.toLowerCase().replace(/\s+/g, "-");
}


function escapeHtml(value) {

    const div = document.createElement("div");

    div.textContent = value;

    return div.innerHTML;
}
// ===========================
// Global Variables
// ===========================

let editingEventId = null;


// ===========================
// Initialize Events
// ===========================

function initEvents() {

    loadEvents();

    document.getElementById("addEventBtn").onclick = () => {

        editingEventId = null;

        document.getElementById("eventModalTitle").innerText =
            "Add Event";

        clearEventForm();

        document.getElementById("eventModal").style.display =
            "block";

    };

    document.getElementById("cancelEventBtn").onclick = () => {

        document.getElementById("eventModal").style.display =
            "none";

    };

    document.getElementById("saveEventBtn").onclick =
        saveEvent;

    document.getElementById("searchEvent")
        .addEventListener("keyup", function () {

            const value = this.value.toLowerCase();

            document.querySelectorAll("#eventsBody tr")
                .forEach(row => {

                    row.style.display =
                        row.innerText.toLowerCase().includes(value)
                            ? ""
                            : "none";

                });

        });

}


// ===========================
// Load Events
// ===========================

function loadEvents() {

    fetch("/events")
        .then(response => response.json())
        .then(data => {

            const body = document.getElementById("eventsBody");

            body.innerHTML = "";

            data.forEach(event => {

                body.innerHTML += `

                    <tr>

                        <td>${event.eventId}</td>

                        <td>${event.eventName}</td>

                        <td>${event.eventDate}</td>

                        <td>${event.location}</td>

                        <td>${event.description}</td>

                        <td>

                            <button
                                class="edit-btn"
                                onclick="editEvent(${event.eventId})">

                                Edit

                            </button>

                            <button
                                class="delete-btn"
                                onclick="deleteEvent(${event.eventId})">

                                Delete

                            </button>

                        </td>

                    </tr>

                `;

            });

        });

}


// ===========================
// Save Event
// ===========================

function saveEvent() {

    const event = {

        eventName:
            document.getElementById("eventName").value,

        eventDate:
            document.getElementById("eventDate").value,

        location:
            document.getElementById("location").value,

        description:
            document.getElementById("description").value

    };

    let url = "/events";
    let method = "POST";

    if (editingEventId != null) {

        url = `/events/${editingEventId}`;

        method = "PUT";

    }

    fetch(url, {

        method: method,

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(event)

    })
    .then(() => {

        document.getElementById("eventModal").style.display =
            "none";

        loadEvents();

    });

}


// ===========================
// Edit Event
// ===========================

function editEvent(id) {

    fetch(`/events/${id}`)
        .then(response => response.json())
        .then(event => {

            editingEventId = id;

            document.getElementById("eventModalTitle").innerText =
                "Edit Event";

            document.getElementById("eventName").value =
                event.eventName;

            document.getElementById("eventDate").value =
                event.eventDate;

            document.getElementById("location").value =
                event.location;

            document.getElementById("description").value =
                event.description;

            document.getElementById("eventModal").style.display =
                "block";

        });

}


// ===========================
// Delete Event
// ===========================

function deleteEvent(id) {

    if (!confirm("Delete this event?")) {

        return;

    }

    fetch(`/events/${id}`, {

        method: "DELETE"

    })
    .then(() => {

        loadEvents();

    });

}


// ===========================
// Clear Form
// ===========================

function clearEventForm() {

    document.getElementById("eventName").value = "";

    document.getElementById("eventDate").value = "";

    document.getElementById("location").value = "";

    document.getElementById("description").value = "";

}
document.addEventListener("DOMContentLoaded", loadUpcomingEvents);

async function loadUpcomingEvents() {

    const loading = document.getElementById("loading");
    const error = document.getElementById("error");
    const noEvents = document.getElementById("no-events");
    const eventsList = document.getElementById("events-list");

    try {

        const response = await fetch("/member-events");

        if (response.status === 401) {
            window.location.href = "/member-login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Unable to load upcoming events.");
        }

        const events = await response.json();

        loading.classList.add("hidden");

        if (events.length === 0) {
            noEvents.classList.remove("hidden");
            return;
        }

        eventsList.innerHTML = "";

        events.forEach(event => {

            const eventCard = document.createElement("div");
            eventCard.className = "event-card";

            eventCard.innerHTML = `
                <div class="event-date">
                    ${formatDate(event.eventDate)}
                </div>

                <div class="event-content">

                    <h2>${escapeHtml(event.eventName)}</h2>

                    <p class="location">
                        📍 ${escapeHtml(event.location || "Location not specified")}
                    </p>

                    <p class="description">
                        ${escapeHtml(
                            event.description || "No description available."
                        )}
                    </p>

                </div>
            `;

            eventsList.appendChild(eventCard);
        });

    } catch (err) {

        loading.classList.add("hidden");

        error.textContent =
            err.message || "Something went wrong.";

        error.classList.remove("hidden");
    }
}


function formatDate(dateString) {

    const date = new Date(dateString + "T00:00:00");

    return date.toLocaleDateString("en-US", {
        weekday: "short",
        day: "numeric",
        month: "long",
        year: "numeric"
    });
}


function escapeHtml(value) {

    const div = document.createElement("div");

    div.textContent = value;

    return div.innerHTML;
}
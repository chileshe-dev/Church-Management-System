let myMinistryIds = new Set();

async function loadMinistries() {
    try {
        const response = await fetch("/member-ministries", {
            credentials: "include"
        });

        if (response.status === 401) {
            window.location.href = "/member-login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Failed to load ministries.");
        }

        const ministries = await response.json();

        renderMinistries(ministries);

    } catch (error) {
        console.error(error);

        document.getElementById("ministriesContainer").innerHTML = `
            <div class="empty-state">
                <i class="fa-solid fa-circle-exclamation"></i>
                <p>Unable to load ministries.</p>
            </div>
        `;
    }
}

async function loadMyMinistries() {
    try {
        const response = await fetch("/member-ministries/my", {
            credentials: "include"
        });

        if (response.status === 401) {
            window.location.href = "/member-login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Failed to load your ministries.");
        }

        const memberships = await response.json();

        myMinistryIds = new Set(
            memberships.map(membership =>
                membership.ministry.ministryId
            )
        );

        renderMyMinistries(memberships);

        // Reload available ministries so the buttons
        // correctly show the current membership status.
        loadMinistries();

    } catch (error) {
        console.error(error);

        document.getElementById("myMinistriesContainer").innerHTML = `
            <div class="empty-state">
                <i class="fa-solid fa-circle-exclamation"></i>
                <p>Unable to load your ministries.</p>
            </div>
        `;
    }
}

function renderMinistries(ministries) {

    const container = document.getElementById("ministriesContainer");

    if (ministries.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fa-solid fa-people-group"></i>
                <p>No ministries are currently available.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = "";

    ministries.forEach(ministry => {

        const joined = myMinistryIds.has(ministry.ministryId);

        const card = document.createElement("div");
        card.className = "ministry-card";

        card.innerHTML = `
            <div class="ministry-icon">
                <i class="fa-solid fa-people-group"></i>
            </div>

            <h3>${ministry.ministryName}</h3>

            <p class="ministry-description">
                ${ministry.description || "No description available."}
            </p>

            <p class="ministry-leader">
                <i class="fa-solid fa-user"></i>
                Leader: ${ministry.leaderName || "Not specified"}
            </p>

            ${
                joined
                ? `
                    <span class="joined-badge">
                        <i class="fa-solid fa-check"></i>
                        You are a member
                    </span>
                `
                : `
                    <button class="join-btn"
                            onclick="joinMinistry(${ministry.ministryId})">
                        <i class="fa-solid fa-user-plus"></i>
                        Join Ministry
                    </button>
                `
            }
        `;

        container.appendChild(card);
    });
}

function renderMyMinistries(memberships) {

    const container = document.getElementById("myMinistriesContainer");

    if (memberships.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fa-solid fa-people-group"></i>
                <p>You have not joined any ministries yet.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = "";

    memberships.forEach(membership => {

        const ministry = membership.ministry;

        const card = document.createElement("div");
        card.className = "ministry-card";

        card.innerHTML = `
            <div class="ministry-icon">
                <i class="fa-solid fa-hand-holding-heart"></i>
            </div>

            <h3>${ministry.ministryName}</h3>

            <p class="ministry-description">
                ${ministry.description || "No description available."}
            </p>

            <p class="ministry-leader">
                <i class="fa-solid fa-user"></i>
                Leader: ${ministry.leaderName || "Not specified"}
            </p>

            <p class="ministry-leader">
                <i class="fa-solid fa-calendar"></i>
                Joined: ${membership.joinedDate}
            </p>

            <button class="leave-btn"
                    onclick="leaveMinistry(${ministry.ministryId})">
                <i class="fa-solid fa-right-from-bracket"></i>
                Leave Ministry
            </button>
        `;

        container.appendChild(card);
    });
}

async function joinMinistry(ministryId) {

    if (!confirm("Are you sure you want to join this ministry?")) {
        return;
    }

    try {

        const response = await fetch(
            `/member-ministries/${ministryId}/join`,
            {
                method: "POST",
                credentials: "include"
            }
        );

        const result = await response.text();

        if (!response.ok) {
            alert(result);
            return;
        }

        alert("You have successfully joined the ministry.");

        await loadMyMinistries();

    } catch (error) {
        console.error(error);
        alert("Something went wrong while joining the ministry.");
    }
}

async function leaveMinistry(ministryId) {

    if (!confirm("Are you sure you want to leave this ministry?")) {
        return;
    }

    try {

        const response = await fetch(
            `/member-ministries/${ministryId}/leave`,
            {
                method: "DELETE",
                credentials: "include"
            }
        );

        const result = await response.text();

        if (!response.ok) {
            alert(result);
            return;
        }

        alert("You have left the ministry.");

        await loadMyMinistries();

    } catch (error) {
        console.error(error);
        alert("Something went wrong while leaving the ministry.");
    }
}

loadMyMinistries();
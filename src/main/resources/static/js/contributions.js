// ===========================
// Global Variables
// ===========================

let editingContributionId = null;


// ===========================
// Initialize Contributions
// ===========================

function initContributions() {

    loadContributionMembersDropdown();
    loadContributions();

    document.getElementById("addContributionBtn").onclick = () => {

        editingContributionId = null;

        document.getElementById("contributionModalTitle").innerText =
            "Add Contribution";

        clearContributionForm();

        document.getElementById("contributionModal").style.display = "block";

    };

    document.getElementById("cancelContributionBtn").onclick = () => {

        document.getElementById("contributionModal").style.display = "none";

    };

    document.getElementById("saveContributionBtn").onclick =
        saveContribution;

    document.getElementById("searchContribution")
        .addEventListener("keyup", function () {

            const value = this.value.toLowerCase();

            document.querySelectorAll("#contributionsBody tr")
                .forEach(row => {

                    row.style.display =
                        row.innerText.toLowerCase().includes(value)
                            ? ""
                            : "none";

                });

        });

}


// ===========================
// Load Members Dropdown
// ===========================

function loadContributionMembersDropdown() {

    fetch("/members")
        .then(response => response.json())
        .then(data => {

            const select = document.getElementById("memberSelect");

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
// Load Contributions
// ===========================

function loadContributions() {

    fetch("/contributions")
        .then(response => response.json())
        .then(data => {

            const body = document.getElementById("contributionsBody");

            body.innerHTML = "";

            data.forEach(c => {

                body.innerHTML += `

                    <tr>

                        <td>${c.contributionId}</td>

                        <td>${c.member.firstName} ${c.member.lastName}</td>

                        <td>${c.contributionType}</td>

                        <td>K ${Number(c.amount).toFixed(2)}</td>

                        <td>${c.contributionDate}</td>

                        <td>

                            <button
                                class="edit-btn"
                                onclick="editContribution(${c.contributionId})">

                                Edit

                            </button>

                            <button
                                class="delete-btn"
                                onclick="deleteContribution(${c.contributionId})">

                                Delete

                            </button>

                        </td>

                    </tr>

                `;

            });

        });

}


// ===========================
// Save Contribution
// ===========================

function saveContribution() {

    const contribution = {

        contributionType:
            document.getElementById("contributionType").value,

        amount:
            parseFloat(document.getElementById("amount").value),

        contributionDate:
            document.getElementById("contributionDate").value,

        member: {

            memberId:
                parseInt(document.getElementById("memberSelect").value)

        }

    };

    let url = "/contributions";
    let method = "POST";

    if (editingContributionId != null) {

        url = `/contributions/${editingContributionId}`;

        method = "PUT";

    }
    console.log(contribution);

   fetch(url, {
    method,
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify(contribution)
})
.then(response => {
    if (!response.ok) {
        throw new Error("Failed to save contribution");
    }
    return response.json();
})
.then(() => {
    document.getElementById("contributionModal").style.display = "none";
    loadContributions();
})
.catch(error => {
    console.error(error);
    alert("Failed to save contribution.");
});

}


// ===========================
// Edit Contribution
// ===========================

function editContribution(id) {

    fetch(`/contributions/${id}`)
        .then(response => response.json())
        .then(c => {

            editingContributionId = id;

            document.getElementById("contributionModalTitle").innerText =
                "Edit Contribution";

            document.getElementById("memberSelect").value =
                c.member.memberId;

            document.getElementById("contributionType").value =
                c.contributionType;

            document.getElementById("amount").value =
                c.amount;

            document.getElementById("contributionDate").value =
                c.contributionDate;

            document.getElementById("contributionModal").style.display =
                "block";

        });

}


// ===========================
// Delete Contribution
// ===========================

function deleteContribution(id) {

    if (!confirm("Delete this contribution?")) {

        return;

    }

    fetch(`/contributions/${id}`, {

        method: "DELETE"

    })
    .then(() => {

        loadContributions();

    });

}


// ===========================
// Clear Form
// ===========================

function clearContributionForm() {

    document.getElementById("amount").value = "";

    document.getElementById("contributionDate").value = "";

    document.getElementById("contributionType").selectedIndex = 0;

    document.getElementById("memberSelect").selectedIndex = 0;

}
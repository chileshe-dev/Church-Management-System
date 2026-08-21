// ===========================
// Global Variable
// ===========================

let editingMinistryId = null;


// ===========================
// Initialize
// ===========================

function initMinistries() {

    loadMinistries();

    document.getElementById("addMinistryBtn").onclick = () => {

        editingMinistryId = null;

        document.getElementById("ministryModalTitle").innerText =
            "Add Ministry";

        clearMinistryForm();

        document.getElementById("ministryModal").style.display =
            "block";

    };

    document.getElementById("cancelMinistryBtn").onclick = () => {

        document.getElementById("ministryModal").style.display =
            "none";

    };

    document.getElementById("saveMinistryBtn").onclick =
        saveMinistry;

    document.getElementById("searchMinistry")
        .addEventListener("keyup", function () {

            const value = this.value.toLowerCase();

            document.querySelectorAll("#ministriesBody tr")
                .forEach(row => {

                    row.style.display =
                        row.innerText.toLowerCase().includes(value)
                            ? ""
                            : "none";

                });

        });

}


// ===========================
// Load Ministries
// ===========================

function loadMinistries() {

    fetch("/ministries")
        .then(response => response.json())
        .then(data => {

            const body =
                document.getElementById("ministriesBody");

            body.innerHTML = "";

            data.forEach(ministry => {

                body.innerHTML += `

                <tr>

                    <td>${ministry.ministryId}</td>

                    <td>${ministry.ministryName}</td>

                    <td>${ministry.description}</td>

                    <td>${ministry.leaderName}</td>

                    <td>

                        <button
                            class="edit-btn"
                            onclick="editMinistry(${ministry.ministryId})">

                            Edit

                        </button>

                        <button
                            class="delete-btn"
                            onclick="deleteMinistry(${ministry.ministryId})">

                            Delete

                        </button>

                    </td>

                </tr>

                `;

            });

        });

}


// ===========================
// Save
// ===========================

function saveMinistry() {

    const ministry = {

        ministryName:
            document.getElementById("ministryName").value,

        description:
            document.getElementById("ministryDescription").value,

        leaderName:
            document.getElementById("leaderName").value

    };

    let url = "/ministries";
    let method = "POST";

    if (editingMinistryId != null) {

        url = `/ministries/${editingMinistryId}`;

        method = "PUT";

    }

    fetch(url, {

        method: method,

        headers: {

            "Content-Type":"application/json"

        },

        body: JSON.stringify(ministry)

    })
    .then(() => {

        document.getElementById("ministryModal").style.display =
            "none";

        loadMinistries();

    });

}


// ===========================
// Edit
// ===========================

function editMinistry(id){

    fetch(`/ministries/${id}`)
        .then(response => response.json())
        .then(ministry => {

            editingMinistryId = id;

            document.getElementById("ministryModalTitle").innerText =
                "Edit Ministry";

            document.getElementById("ministryName").value =
                ministry.ministryName;

            document.getElementById("ministryDescription").value =
                ministry.description;

            document.getElementById("leaderName").value =
                ministry.leaderName;

            document.getElementById("ministryModal").style.display =
                "block";

        });

}


// ===========================
// Delete
// ===========================

function deleteMinistry(id){

    if(!confirm("Delete this ministry?"))
        return;

    fetch(`/ministries/${id}`,{

        method:"DELETE"

    })
    .then(() => {

        loadMinistries();

    });

}


// ===========================
// Clear Form
// ===========================

function clearMinistryForm(){

    document.getElementById("ministryName").value = "";

    document.getElementById("ministryDescription").value = "";

    document.getElementById("leaderName").value = "";

}
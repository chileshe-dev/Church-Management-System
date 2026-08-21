// ===========================
// Global Variables
// ===========================

let editingMemberId = null;


// ===========================
// Initialize Members Page
// ===========================

function initMembers() {

    loadMembers();

    document.getElementById("addMemberBtn").onclick = () => {

        editingMemberId = null;

        document.getElementById("modalTitle").innerText = "Add Member";

        clearForm();

        document.getElementById("memberModal").style.display = "block";

    };

    document.getElementById("cancelMemberBtn").onclick = () => {

        document.getElementById("memberModal").style.display = "none";

    };

    document.getElementById("saveMemberBtn").onclick = saveMember;

    document.getElementById("searchMember").addEventListener("keyup", function () {

        const value = this.value.toLowerCase();

        document.querySelectorAll("#membersBody tr").forEach(row => {

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

function loadMembers() {

    fetch("/members")
        .then(response => response.json())
        .then(data => {

            const body = document.getElementById("membersBody");

            body.innerHTML = "";

            data.forEach(member => {

                body.innerHTML += `
                    <tr>
                        <td>${member.memberId}</td>
                        <td>${member.firstName}</td>
                        <td>${member.lastName}</td>
                        <td>${member.email}</td>
                        <td>${member.phone}</td>
                        <td>${member.address}</td>

                        <td>

                            <button
                                class="edit-btn"
                                onclick="editMember(${member.memberId})">

                                Edit

                            </button>

                            <button
                                class="delete-btn"
                                onclick="deleteMember(${member.memberId})">

                                Delete

                            </button>

                        </td>

                    </tr>
                `;

            });

        });

}


// ===========================
// Save Member
// ===========================

function saveMember() {

    const member = {

        firstName: document.getElementById("firstName").value,

        lastName: document.getElementById("lastName").value,

        email: document.getElementById("email").value,

        phone: document.getElementById("phone").value,

        address: document.getElementById("address").value

    };

    let url = "/members";
    let method = "POST";

    if (editingMemberId != null) {

        url = `/members/${editingMemberId}`;

        method = "PUT";

    }

    fetch(url, {

        method: method,

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(member)

    })
    .then(() => {

        document.getElementById("memberModal").style.display = "none";

        loadMembers();

    });

}


// ===========================
// Edit Member
// ===========================

function editMember(id) {

    fetch(`/members/${id}`)
        .then(response => response.json())
        .then(member => {

            editingMemberId = id;

            document.getElementById("modalTitle").innerText = "Edit Member";

            document.getElementById("firstName").value = member.firstName;

            document.getElementById("lastName").value = member.lastName;

            document.getElementById("email").value = member.email;

            document.getElementById("phone").value = member.phone;

            document.getElementById("address").value = member.address;

            document.getElementById("memberModal").style.display = "block";

        });

}


// ===========================
// Delete Member
// ===========================

function deleteMember(id) {

    if (!confirm("Delete this member?")) {

        return;

    }

    fetch(`/members/${id}`, {

        method: "DELETE"

    })
    .then(() => {

        loadMembers();

    });

}


// ===========================
// Clear Form
// ===========================

function clearForm() {

    document.getElementById("firstName").value = "";

    document.getElementById("lastName").value = "";

    document.getElementById("email").value = "";

    document.getElementById("phone").value = "";

    document.getElementById("address").value = "";

}
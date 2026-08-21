// ===========================
// Global Variable
// ===========================

let editingUserId = null;


// ===========================
// Initialize
// ===========================

function initUsers() {

    loadUsers();

    document.getElementById("addUserBtn").onclick = () => {

        editingUserId = null;

        document.getElementById("userModalTitle").innerText =
            "Add User";

        clearUserForm();

        document.getElementById("userModal").style.display =
            "block";

    };

    document.getElementById("cancelUserBtn").onclick = () => {

        document.getElementById("userModal").style.display =
            "none";

    };

    document.getElementById("saveUserBtn").onclick =
        saveUser;

    document.getElementById("searchUser")
        .addEventListener("keyup", function () {

            const value = this.value.toLowerCase();

            document.querySelectorAll("#usersBody tr")
                .forEach(row => {

                    row.style.display =
                        row.innerText.toLowerCase().includes(value)
                            ? ""
                            : "none";

                });

        });

}


// ===========================
// Load Users
// ===========================

function loadUsers() {

    fetch("/users")
        .then(response => response.json())
        .then(data => {

            const body = document.getElementById("usersBody");

            body.innerHTML = "";

            data.forEach(user => {

                body.innerHTML += `

                <tr>

                    <td>${user.userId}</td>

                    <td>${user.username}</td>

                    <td>${user.role}</td>

                    <td>

                        <button
                            class="edit-btn"
                            onclick="editUser(${user.userId})">

                            Edit

                        </button>

                        <button
                            class="delete-btn"
                            onclick="deleteUser(${user.userId})">

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

function saveUser() {

    const user = {

        username:
            document.getElementById("userUsername").value,

        password:
            document.getElementById("userPassword").value,

        role:
            document.getElementById("userRole").value

    };

    let url = "/users";
    let method = "POST";

    if (editingUserId != null) {

        url = `/users/${editingUserId}`;

        method = "PUT";

    }

    fetch(url, {

        method: method,

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(user)

    })
    .then(() => {

        document.getElementById("userModal").style.display =
            "none";

        loadUsers();

    });

}


// ===========================
// Edit
// ===========================

function editUser(id) {

    fetch(`/users/${id}`)
        .then(response => response.json())
        .then(user => {

            editingUserId = id;

            document.getElementById("userModalTitle").innerText =
                "Edit User";

            document.getElementById("userUsername").value =
                user.username;

            document.getElementById("userPassword").value =
                user.password;

            document.getElementById("userRole").value =
                user.role;

            document.getElementById("userModal").style.display =
                "block";

        });

}


// ===========================
// Delete
// ===========================

function deleteUser(id) {

    if (!confirm("Delete this user?"))
        return;

    fetch(`/users/${id}`, {

        method: "DELETE"

    })
    .then(() => {

        loadUsers();

    });

}


// ===========================
// Clear Form
// ===========================

function clearUserForm() {

    document.getElementById("userUsername").value = "";

    document.getElementById("userPassword").value = "";

    document.getElementById("userRole").selectedIndex = 0;

}
const memberLoginForm =
    document.getElementById("memberLoginForm");

console.log("Member login JS loaded.");
console.log("Login form:", memberLoginForm);

memberLoginForm.addEventListener(
    "submit",
    async function (e) {

        e.preventDefault();

        console.log("Login form submitted.");

        const username =
            document.getElementById("username").value.trim();

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("loginMessage");

        message.innerText = "Logging in...";

        console.log("Username:", username);

        try {

            const response =
                await fetch("/member-login", {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                });

            const result =
                await response.text();

            console.log("Status:", response.status);
            console.log("Response:", result);

          if (!response.ok) {

    message.innerText = result;

    return;
}

console.log("================================");
console.log("MEMBER LOGIN SUCCESSFUL");
console.log("Server response:", result);
console.log("================================");

message.innerText =
    "Login successful!";

window.location.href =
    "/member-dashboard";
    
} catch (error) {

    console.error(
        "Member login error:",
        error
    );

    message.innerText =
        "Could not connect to the server.";
}
    }
);
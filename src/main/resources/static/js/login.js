const form = document.getElementById("loginForm");

form.addEventListener("submit", async (e) => {

    e.preventDefault();

    const formData = new URLSearchParams();

    formData.append(
        "username",
        document.getElementById("username").value
    );

    formData.append(
        "password",
        document.getElementById("password").value
    );

    const response = await fetch("/login", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData,

        credentials: "include"

    });

    if (response.redirected) {

        window.location.href = response.url;

    } else {

        document.getElementById("errorMessage").innerText =
            "Invalid username or password.";

    }

});
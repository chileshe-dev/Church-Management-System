const registrationForm =
    document.getElementById("registrationForm");


registrationForm.addEventListener("submit", async function (e) {

    e.preventDefault();

    const password =
        document.getElementById("password").value;

    const confirmPassword =
        document.getElementById("confirmPassword").value;

    const message =
        document.getElementById("message");


    // Check passwords
    if (password !== confirmPassword) {

        message.innerText =
            "Passwords do not match.";

        return;
    }


    const registrationData = {

        firstName:
            document.getElementById("firstName").value,

        lastName:
            document.getElementById("lastName").value,

        email:
            document.getElementById("email").value,

        phone:
            document.getElementById("phone").value,

        address:
            document.getElementById("address").value,

        username:
            document.getElementById("username").value,

        password:
            password

    };


   try {

    const response = await fetch(
        "/member-registration",
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(registrationData)
        }
    );

    const result = await response.text();

    console.log("Status:", response.status);
    console.log("Response:", result);

    if (!response.ok) {

        message.innerText =
            "Registration failed: " + result;

        return;
    }

    message.innerText =
        "Registration successful! You can now log in.";

    registrationForm.reset();

} catch (error) {

    console.error(error);

    message.innerText =
        "Could not connect to the server.";
}});
async function loadMemberDashboard() {

    try {

        const response =
            await fetch(
                "/member-account/me",
                {
                    credentials: "include"
                }
            );


        if (!response.ok) {

            window.location.href =
                "/member-login.html";

            return;
        }


        const member =
            await response.json();


        document.getElementById(
            "memberName"
        ).innerText =
            `${member.firstName} ${member.lastName}`;


        document.getElementById(
            "memberUsername"
        ).innerText =
            member.username;


        document.getElementById(
            "memberStatus"
        ).innerText =
            member.status;


    } catch (error) {

        console.error(error);

        window.location.href =
            "/member-login.html";

    }

}


document.getElementById(
    "logoutBtn"
).addEventListener(
    "click",
    async function () {

        await fetch(
            "/member-login/logout",
            {
                method: "POST",
                credentials: "include"
            }
        );


        window.location.href =
            "/member-login.html";

    }
);


loadMemberDashboard();
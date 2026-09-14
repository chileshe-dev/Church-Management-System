document.addEventListener("DOMContentLoaded", function () {

    loadContributions();

    document
        .getElementById("contributionForm")
        .addEventListener("submit", makeContribution);

});


async function loadContributions() {

    try {

        const response =
            await fetch("/member-contributions", {
                method: "GET",
                credentials: "include"
            });

        if (!response.ok) {

            window.location.href =
                "/member-login.html";

            return;
        }

        const result =
            await response.json();

        // The backend now returns:
        // {
        //     contributions: [...],
        //     total: ...
        // }

        const contributions =
            result.contributions;

        const total =
            result.total;

        // Display total contribution
        const totalElement =
            document.getElementById("totalContributions");

        if (totalElement) {

            totalElement.innerText =
                `K ${Number(total).toFixed(2)}`;
        }


        const body =
            document.getElementById("contributionsBody");

        body.innerHTML = "";


        // No contributions
        if (contributions.length === 0) {

            body.innerHTML = `
                <tr>
                    <td colspan="4">
                        No contributions recorded yet.
                    </td>
                </tr>
            `;

            return;
        }


        // Display contributions
        contributions.forEach(contribution => {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${contribution.contributionId}</td>

                <td>${contribution.contributionType}</td>

                <td>
                    K ${Number(contribution.amount).toFixed(2)}
                </td>

                <td>${contribution.contributionDate}</td>
            `;

            body.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Error loading contributions:",
            error
        );

    }

}


async function makeContribution(e) {

    e.preventDefault();


    const message =
        document.getElementById(
            "contributionMessage"
        );


    const data = {

        contributionType:
            document.getElementById(
                "contributionType"
            ).value,

        amount:
            Number(
                document.getElementById(
                    "amount"
                ).value
            ),

        contributionDate:
            document.getElementById(
                "contributionDate"
            ).value

    };


    const paymentMethod =
        document.getElementById(
            "paymentMethod"
        ).value;


    try {

        // ==========================================
        // STEP 1: CREATE CONTRIBUTION
        // ==========================================

        const contributionResponse =
            await fetch(
                "/member-contributions",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify(data)

                }
            );


        const contributionResult =
            await contributionResponse.json();


        console.log(
            "Contribution status:",
            contributionResponse.status
        );

        console.log(
            "Contribution response:",
            contributionResult
        );


        if (!contributionResponse.ok) {

            message.innerText =
                contributionResult;

            return;
        }


        // ==========================================
        // GET NEW CONTRIBUTION ID
        // ==========================================

        const contributionId =
            contributionResult.contributionId;


        console.log(
            "New contribution ID:",
            contributionId
        );


        // ==========================================
        // STEP 2: CREATE PAYMENT
        // ==========================================

        const paymentResponse =
            await fetch(
                "/member-payments",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify({

                        contributionId:
                            contributionId,

                        method:
                            paymentMethod

                    })

                }
            );


        const paymentText =
            await paymentResponse.text();


        console.log(
            "Payment status:",
            paymentResponse.status
        );

        console.log(
            "Payment response:",
            paymentText
        );


        if (!paymentResponse.ok) {

            message.innerText =
                "Contribution was recorded, but payment could not be created.";

            return;
        }


        // ==========================================
        // PAYMENT SUCCESS
        // ==========================================

        const paymentResult =
            JSON.parse(paymentText);


        console.log(
            "Payment created:",
            paymentResult
        );


        message.innerText =
            "Contribution recorded successfully. Payment is pending.";


        // Reset form
        document
            .getElementById(
                "contributionForm"
            )
            .reset();


        // Reload contribution history
        loadContributions();


    } catch (error) {

        console.error(
            "Contribution/payment error:",
            error
        );


        message.innerText =
            "Could not connect to the server.";

    }

}
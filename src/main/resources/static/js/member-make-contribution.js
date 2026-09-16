document.addEventListener("DOMContentLoaded", function () {

    const dateInput =
        document.getElementById("contributionDate");

    // Automatically use today's date
    const today =
        new Date().toISOString().split("T")[0];

    dateInput.value = today;


    document
        .getElementById("contributionForm")
        .addEventListener(
            "submit",
            makeContribution
        );

});


async function makeContribution(e) {

    e.preventDefault();

    const message =
        document.getElementById(
            "contributionMessage"
        );


    message.innerText =
        "Processing contribution...";


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

        // ================================
        // STEP 1: CREATE CONTRIBUTION
        // ================================

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


        const contributionText =
            await contributionResponse.text();


        let contributionResult;

        try {

            contributionResult =
                JSON.parse(contributionText);

        } catch {

            contributionResult =
                contributionText;

        }


        if (!contributionResponse.ok) {

            message.innerText =
                contributionResult;

            return;
        }


        const contributionId =
            contributionResult.contributionId;


        // ================================
        // STEP 2: CREATE PAYMENT
        // ================================

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


        if (!paymentResponse.ok) {

            message.innerText =
                "Contribution was recorded, but payment could not be created.";

            console.error(
                "Payment error:",
                paymentText
            );

            return;
        }


        // ================================
        // SUCCESS
        // ================================

        message.innerText =
            "Contribution recorded successfully. Payment is pending.";


        document
            .getElementById(
                "contributionForm"
            )
            .reset();


        // Restore today's date
        document
            .getElementById(
                "contributionDate"
            )
            .value =
            new Date()
                .toISOString()
                .split("T")[0];


    } catch (error) {

        console.error(
            "Contribution error:",
            error
        );

        message.innerText =
            "Could not connect to the server.";

    }

}
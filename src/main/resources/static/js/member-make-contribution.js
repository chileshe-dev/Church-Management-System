
function setToday() {
    const dateInput = document.getElementById("contributionDate");
    if (!dateInput) return;

    const now = new Date();
    dateInput.value = new Date(
        now.getTime() - now.getTimezoneOffset() * 60000
    ).toISOString().split("T")[0];
}

document.addEventListener("DOMContentLoaded", function () {
    setToday();

    const form = document.getElementById("contributionForm");

    if (form) {
        form.addEventListener("submit", makeContribution);
    }
});

async function makeContribution(e) {
    e.preventDefault();

    const form = document.getElementById("contributionForm");
    const message = document.getElementById("contributionMessage");
    const submitButton = form.querySelector('button[type="submit"]');

    const paymentMethod = document.getElementById("paymentMethod").value;

    const data = {
        contributionType: document.getElementById("contributionType").value,
        amount: Number(document.getElementById("amount").value),
        contributionDate: document.getElementById("contributionDate").value
    };

    submitButton.disabled = true;
    message.textContent = "Recording contribution...";

    try {
        // Step 1: Record the contribution.
        const contributionResponse = await fetch("/member-contributions", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "same-origin",
            body: JSON.stringify(data)
        });

        const contributionText = await contributionResponse.text();
        let contributionResult;

        try {
            contributionResult = JSON.parse(contributionText);
        } catch {
            contributionResult = contributionText;
        }

        if (!contributionResponse.ok) {
            message.textContent =
                typeof contributionResult === "string"
                    ? contributionResult
                    : "Could not record the contribution.";
            return;
        }

        const contributionId = contributionResult.contributionId;

        if (!contributionId) {
            message.textContent =
                "Contribution response did not contain an ID. Check the server response.";
            return;
        }

        // Cash and bank transfers do not initiate an MTN sandbox request.
        if (paymentMethod !== "Mobile Money") {
            message.textContent =
                "Contribution recorded successfully. Payment method: " +
                paymentMethod +
                ". No MTN transaction was initiated.";

            form.reset();
            setToday();
            return;
        }

        // Step 2: Initiate the MTN sandbox payment.
        message.textContent =
            "Contribution recorded. Initiating MTN sandbox test...";

        const paymentResponse = await fetch("/member-payments", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "same-origin",
            body: JSON.stringify({
                contributionId: contributionId,
                method: paymentMethod
            })
        });

        const paymentText = await paymentResponse.text();
        let paymentResult;

        try {
            paymentResult = JSON.parse(paymentText);
        } catch {
            paymentResult = paymentText;
        }

        if (!paymentResponse.ok) {
            message.textContent =
                "Contribution recorded, but payment initiation failed. " +
                "Do not submit this contribution again. Check the server response.";

            console.error("Payment initiation error:", paymentResult);
            return;
        }

        displayPaymentResult(paymentResult);

        form.reset();
        setToday();

    } catch (error) {
        console.error("Contribution processing error:", error);

        message.textContent =
            "An error occurred: " +
            (error.message || String(error)) +
            ". Check your records before retrying.";

    } finally {
        submitButton.disabled = false;
    }
}

function displayPaymentResult(payment) {
    const message = document.getElementById("contributionMessage");
    message.replaceChildren();

    const heading = document.createElement("strong");
    heading.textContent = "MTN SANDBOX TEST";
    message.appendChild(heading);

    const details = document.createElement("p");
    details.textContent =
        "Payment ID: " + payment.paymentId +
        " | Status: " + payment.status +
        " | Reference: " + payment.transactionReference;
    message.appendChild(details);

    const note = document.createElement("p");
    note.textContent =
        "Sandbox simulation only. This does not confirm collection of real ZMW.";
    message.appendChild(note);

    const statusButton = document.createElement("button");
    statusButton.type = "button";
    statusButton.textContent = "Check Payment Status";

    statusButton.addEventListener("click", function () {
        refreshPaymentStatus(payment.paymentId, statusButton);
    });

    message.appendChild(statusButton);
}

async function refreshPaymentStatus(paymentId, button) {
    const message = document.getElementById("contributionMessage");

    button.disabled = true;
    button.textContent = "Checking...";

    try {
        const response = await fetch(
            "/member-payments/" +
            encodeURIComponent(paymentId) +
            "/refresh-status",
            {
                method: "POST",
                credentials: "same-origin"
            }
        );

        const text = await response.text();
        let result;

        try {
            result = JSON.parse(text);
        } catch {
            result = text;
        }

        if (!response.ok || typeof result !== "object" || result === null) {
            console.error("Status check error:", result);
            message.appendChild(
                document.createTextNode(" Status check failed. Please try again.")
            );
            return;
        }

        const statusLine = document.createElement("p");
        statusLine.textContent = "Latest payment status: " + result.status;
        message.appendChild(statusLine);

    } catch (error) {
        console.error("Status refresh error:", error);
        message.appendChild(
            document.createTextNode(" Could not contact the server to check status.")
        );

    } finally {
        button.disabled = false;
        button.textContent = "Check Payment Status";
    }
}

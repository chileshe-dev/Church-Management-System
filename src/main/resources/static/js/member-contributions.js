document.addEventListener("DOMContentLoaded", function () {

    loadContributions();

});


async function loadContributions() {

    try {

        const response =
            await fetch(
                "/member-contributions",
                {
                    method: "GET",
                    credentials: "include"
                }
            );


        if (!response.ok) {

            window.location.href =
                "/member-login.html";

            return;
        }


        const result =
            await response.json();


        const contributions =
            result.contributions || [];


        const total =
            result.total || 0;


        // ==========================
        // DISPLAY TOTAL
        // ==========================

        const totalElement =
            document.getElementById(
                "totalContributions"
            );


        if (totalElement) {

            totalElement.innerText =
                `K ${Number(total).toFixed(2)}`;

        }


        // ==========================
        // DISPLAY HISTORY
        // ==========================

        const body =
            document.getElementById(
                "contributionsBody"
            );


        body.innerHTML = "";


        if (contributions.length === 0) {

            body.innerHTML = `
                <tr>
                    <td colspan="6">
                        No contributions recorded yet.
                    </td>
                </tr>
            `;

            return;
        }


        contributions.forEach(
            contribution => {

                const row =
                    document.createElement("tr");


                row.innerHTML = `

                    <td>
                        ${contribution.contributionId}
                    </td>

                    <td>
                        ${contribution.contributionType}
                    </td>

                    <td>
                        K ${Number(
                            contribution.amount
                        ).toFixed(2)}
                    </td>

                    <td>
                        ${contribution.contributionDate}
                    </td>

                    <td>
                        ${contribution.paymentMethod || "—"}
                    </td>

                    <td>
                        ${contribution.status || "Pending"}
                    </td>

                `;


                body.appendChild(row);

            }
        );


    } catch (error) {

        console.error(
            "Error loading contributions:",
            error
        );

    }

}
document
    .getElementById("patient-details")
    .addEventListener("submit", function (event) {
        event.preventDefault();

        validate();

        if (validate()) {
            this.submit();
        }
    });

function validate() {
    const allElements = document.getElementById("patient-details").elements;

    let count = 0;

    for (let x of allElements) {
        if (x.tagName === "INPUT" && x.type === "number") {
            if (!notEmpty(x.value)) {
                count++;
            }
        }
    }
    if (count > 8) {
        alert("please provide atleast 5 details.." + count);
        window.location.reload();

        return false;
    }

    return true;
}

function notEmpty(value) {
    return value.trim() !== "";
}

const selectElements = document.querySelectorAll("select");

selectElements.forEach((select) => {
    select.addEventListener("change", () => {
        const selectedOption = select.options[select.selectedIndex];

        if (selectedOption.value == "") {
            selectedOption.style.color = "#000";
            select.style.borderColor = "#000";
        } else {
            select.style.borderColor = "#00df9a";
        }
    });
});
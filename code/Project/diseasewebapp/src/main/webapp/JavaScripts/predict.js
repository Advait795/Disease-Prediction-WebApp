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


//loader code

function loadHTML(url, targetElementId) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            document.getElementById(targetElementId).innerHTML = html;
        })
        .catch(error => console.error('error loading html:', error));

}

//load loader.html
loadHTML('loader.html', 'loader');

//form submission handling
document.getElementById('patient-details').addEventListener('submit', function (event) {
    event.preventDefault();

    var loader = document.getElementById('loader');
    loader.style.display = 'block';

    var form = document.getElementById('patient-details');
    var formData = new FormData(form);

    console.log(...formData.entries());


    // Convert FormData to URL parameters
    var urlParams = new URLSearchParams();
    formData.forEach((value, key) => {
        urlParams.append(key, value);
    });

    // Create the complete URL with parameters
    var url = `./api?${urlParams.toString()}`;

    console.log(url); // Log the URL to debug

    setTimeout(() => {
        fetch(url, {
            method: 'GET',
        })
            .then(response => response.json())
            .then(data => {
                loader.style.display = 'none';
                // window.location.href = 'result.html';

                if (data.redirectUrl) {
                    window.location.href = data.redirectUrl;
                } else {
                    console.error('No redirect URL found.');
                }

            })
            .catch(error => {
                loader.style.display = 'none';
                console.error('error:', error);

            })

    }, 3000);
})


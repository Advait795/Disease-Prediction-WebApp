const selectEle = document.querySelectorAll("select");

selectEle.forEach((select) => {
    select.addEventListener("change", () => {
        const selectedOp = select.options[select.selectedIndex];

        if (selectedOp.value == "") {
            selectedOp.style.color = "#000";
            select.style.borderColor = "#000";
        } else {
            select.style.borderColor = "#00df9a";
        }
    });
});

function loadhtml(url, targetEle) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            document.getElementById(targetEle).innerHTML = html;
        })
        .catch(error => console.error('error loading html:', error));

}

loadhtml('loader.html', 'loader');

document.getElementById('patient-details').addEventListener('submit', function (event) {
    event.preventDefault();

    let loader = document.getElementById('loader');
    loader.style.display = 'block';

    let form = document.getElementById('patient-details');
    let formData = new FormData(form);

    console.log(...formData.entries());

    let urlPar = new URLSearchParams();

    formData.forEach((value, key) => {
        urlPar.append(key, value);
    });
    let url = `./api?${urlPar.toString()}`;

    console.log(url);

    setTimeout(() => {
        fetch(url, {
            method: 'GET',
        })
            .then(response => response.json())
            .then(data => {
                loader.style.display = 'none';


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


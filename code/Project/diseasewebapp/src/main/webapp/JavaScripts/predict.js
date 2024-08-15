/*Author: Adwait Dalvi ad918
 * This javaScript file handles border color change of option tab when selected and unselected. 
Also once the form is submitted runs a loader page until receives a response.
*/


//for all select fields on the form 
const selectEle = document.querySelectorAll("select");

selectEle.forEach((select) => {
    select.addEventListener("change", () => {
        const selectedOp = select.options[select.selectedIndex];


        //switch its color if selected an option if unselect any switch back to default color
        if (selectedOp.value == "") {
            selectedOp.style.color = "#000";
            select.style.borderColor = "#000";
        } else {
            select.style.borderColor = "#00df9a";
        }
    });
});


//load html oncall with url and element id as input
function loadhtml(url, targetEle) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            document.getElementById(targetEle).innerHTML = html;
        })
        .catch(error => console.error('error loading html:', error));

}

//calling loader html
loadhtml('loader.html', 'loader');

//get all the values submitted through form
document.getElementById('patient-details').addEventListener('submit', function (event) {
    event.preventDefault();

    //establishing loader
    let loader = document.getElementById('loader');
    loader.style.display = 'block';

    //geting all values through form
    let form = document.getElementById('patient-details');
    let formData = new FormData(form);

    //creating url and appending it with key value pair
    let urlPar = new URLSearchParams();

    formData.forEach((value, key) => {
        urlPar.append(key, value);
    });

    let url = `./api?${urlPar.toString()}`;

    //setting timeout aswell as sending data to result.html file through url
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


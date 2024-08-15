const navToggle = document.querySelector('.nav-toggle');
const navMobile = document.querySelector('.nav-mobile');

navToggle.addEventListener('click', () => {
    navMobile.style.left = navMobile.style.left === '0px' ? '-100%' : '0px';
});

const typedText = document.querySelector('.typed-text');
const textArray = ['Hypertension.', 'Diabetes.', 'Stroke.'];
let textIndex = 0;
let charIndex = 0;
let speed = 100;
let backSpeed = 110;
let loop = true;
let delay = 1500;

function typeText() {
    if (charIndex < textArray[textIndex].length) {
        typedText.innerHTML += textArray[textIndex].charAt(charIndex);
        charIndex++;
        setTimeout(typeText, speed);
    } else {

        setTimeout(() => {
            setTimeout(backspaceText, backSpeed);
        }, delay);
    }
}

function backspaceText() {
    if (charIndex > 0) {
        typedText.innerHTML = typedText.innerHTML.substring(0, charIndex - 1);
        charIndex--;
        setTimeout(backspaceText, backSpeed);
    } else {
        textIndex = (textIndex + 1) % textArray.length;
        charIndex = 0;
        if (loop) {
            setTimeout(typeText, speed);
        }
    }
}

typeText();

document.getElementById('newsletter-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    console.log(email);

    fetch('./email', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'email': email
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                alert("Thank you! We have sent information to your email.");
            } else {
                alert("Oops! Something went wrong. Please try again.");
            }
        })
        .catch(error => {
            alert("Oops! Something went wrong. Please try again.");
        });
});



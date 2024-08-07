const navToggle = document.querySelector('.nav-toggle');
const navMobile = document.querySelector('.nav-mobile');

navToggle.addEventListener('click', () => {
    navMobile.style.left = navMobile.style.left === '0px' ? '-100%' : '0px';
});

const typedText = document.querySelector('.typed-text');
const textArray = ['BTB', 'BTC', 'SAAS'];
let textIndex = 0;
let charIndex = 0;
let speed = 120;
let backSpeed = 130;
let loop = true;

function typeText() {
    if (charIndex < textArray[textIndex].length) {
        typedText.innerHTML += textArray[textIndex].charAt(charIndex);
        charIndex++;
        setTimeout(typeText, speed);
    } else {
        setTimeout(backspaceText, backSpeed);
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
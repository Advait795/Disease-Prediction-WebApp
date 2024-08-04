
let d = "";
function clearUrl() {
    const url = new URL(window.location.href);
    url.search = "";
    window.history.replaceState(null, "", url);
}

window.onload = function () {
    const url = new URLSearchParams(window.location.search);
    const disease = url.get("disease");
    d = disease;
    const predictedClasses = JSON.parse(
        decodeURIComponent(url.get("prediction"))
    );
    const featuresPred = JSON.parse(
        decodeURIComponent(url.get("features"))
    );
    const totalCount = JSON.parse(decodeURIComponent(url.get("totalExamples")));
    const classCountOne = JSON.parse(decodeURIComponent(url.get("classCountOne")));

    let Pdisease = classCountOne / totalCount;

    const hypertension_features = {
        age: 1,
        sex: 2,
        cp: 3,
        trestbps: 4,
        chol: 5,
        fbs: 6,
        restecg: 7,
        thalach: 8,
        exang: 9,
        oldpeak: 10,
        slope: 11,
        ca: 12,
        thal: 13,
    };

    stroke_features = {
        sex: 1,
        age: 2,
        hypertension: 3,
        heart_disease: 4,
        ever_married: 5,
        work_type: 6,
        Residence_type: 7,
        avg_glucose_level: 8,
        bmi: 9,
        smoking_status: 10,
    };

    diabetes_features = {
        sex: 1,
        age: 2,
        hypertension: 3,
        heart_disease: 4,
        smoking_history: 5,
        bmi: 6,
        HbA1c_level: 7,
        blood_glucose_level: 8,
    };

    const featureData = featuresPred;
    const featuresKeys = Object.keys(featuresPred);

    console.log(featuresKeys);
    let mappingKey = {};

    if (disease == "Hypertension") {
        for (let key in hypertension_features) {
            console.log(key);

            value = hypertension_features[key];

            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
            }
        }
    } else if (disease == "Stroke") {
        for (let key in stroke_features) {
            console.log(key);

            value = stroke_features[key];

            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
            }
        }
    } else {
        for (let key in diabetes_features) {
            console.log(key);

            value = diabetes_features[key];

            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
            }
        }
    }

    console.log("mapping key :" + Object.keys(mappingKey));

    const calculations = document.getElementById("cal");
    const collapsibleText = document.getElementById(`chart`);

    calculations.innerHTML = `<p>Naive Bayes formula: <br />
        P(Disease|Symptoms) = P(Disease) * P(Symptoms|Disease) /
        P(Symptoms)<br/><br/>

        P(Disease) = ${classCountOne}/${totalCount} = ${Math.round(Pdisease * 100)}%<br/>
        </p>
        `

    collapsibleText.innerHTML += `
        <div class="chart-container">
            <canvas class="chart" id="polar-area-1"></canvas>
            <canvas class="chart" id="bar-chart-1"></canvas>
        </div>`;


    let probabilities = [];
    for (let symptom in mappingKey) {
        let probability = mappingKey[symptom];
        probabilities.push(probability);
        calculations.innerHTML += `<p>P(Symtom: ${symptom} | Disease) = ${Math.round(
            probability
        )}%</p>`;
    }

    const labels = Object.keys(mappingKey).map(
        (key) => `${key}: ${Math.round(mappingKey[key])}%`
    );
    const data = Object.values(mappingKey);
    const backgroundColor = generateColor(labels.length);
    const borderColor = generateColor(labels.length);

    const polarData = {
        labels: labels,
        datasets: [
            {
                label: "Symptoms Probability",
                data: data,
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 1,
            },
        ],
    };

    const polarCtx = document
        .getElementById(`polar-area-1`)
        .getContext("2d");
    new Chart(polarCtx, {
        type: "polarArea",
        data: polarData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                r: {
                    beginAtZero: true,
                    callback: function (value) {
                        return value + "%";
                    },
                },
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            let label = context.dataset.label || "";
                            if (label) {
                                label += ": ";
                            }
                            if (context.raw !== null) {
                                label += Math.round(context.raw) + "%";
                            }
                            return label;
                        },
                    },
                },
            },
        },
    });

    const barCtx = document.getElementById(`bar-chart-1`).getContext("2d");
    new Chart(barCtx, {
        type: "bar",
        data: polarData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    callback: function (value) {
                        return value + "%";
                    },
                },
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            let label = context.dataset.label || "";
                            if (label) {
                                label += ": ";
                            }
                            if (context.raw !== null) {
                                label += Math.round(context.raw) + "%";
                            }
                            return label;
                        },
                    },
                },
            },
        },
    });
};

document
    .getElementById("download-pdf")
    .addEventListener("click", function () {
        setTimeout(() => {
            const element = document.querySelector(".container");
            html2pdf()
                .from(element)
                .set({
                    margin: 0.12,
                    filename: `${d}_report.pdf`,
                    image: { type: "png", quality: 0.98 },
                    html2canvas: { scale: 2 },
                    jsPDF: {
                        unit: "in",
                        format: "a4",
                        orientation: "p",
                    },
                })
                .save();
        }, 1000);
    });

function generateColor(count) {
    const colors = [
        "rgba(255, 99, 132, 0.6)",
        "rgba(54, 162, 235, 0.6)",
        "rgba(255, 206, 86, 0.6)",
        "rgba(75, 192, 192, 0.6)",
        "rgba(153, 102, 255, 0.6)",
        "rgba(255, 159, 64, 0.6)",
        "rgba(255, 99, 132, 0.6)",
        "rgba(54, 162, 235, 0.6)",
        "rgba(255, 206, 86, 0.6)",
        "rgba(75, 192, 192, 0.6)",
        "rgba(153, 102, 255, 0.6)",
        "rgba(255, 159, 64, 0.6)",
        "rgba(54, 162, 235, 0.6)",
    ];
    return colors.slice(0, count);
}
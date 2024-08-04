
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

    const featureCounts = JSON.parse(decodeURIComponent(url.get("featureCounts")))




    //disease prediction
    const hypertensionPrediction = predictedClasses.Hypertension;
    const strokePrediction = predictedClasses.Stroke;
    const diabetesPrediction = predictedClasses.Diabetes;

    //features counts
    const hypertensionFeatures = featureCounts.Hypertension;
    const strokeFeatures = featureCounts.Stroke;
    const diabetesFeatures = featureCounts.Diabetes;

    let Pdisease = classCountOne / totalCount;

    const hypertension_features = {
        Age: 1,
        Sex: 2,
        Chest_Pain: 3,
        Trest_BPS: 4,
        Cholesterol: 5,
        Fasting_Blood_Sugar: 6,
        Rest_ECG: 7,
        Thalach: 8,
        Exercise_induced_Angina: 9,
        Oldpeak: 10,
        ST_Segment_Slope: 11,
        Cardiac_Arrhythmia: 12,
        Thalassemia: 13,
    };

    stroke_features = {
        Sex: 1,
        Age: 2,
        Hypertension: 3,
        Heart_Disease: 4,
        Marital_Status: 5,
        Work_Stress_Level: 6,
        Residence_Status: 7,
        Average_Sugar: 8,
        Body_Mass_Index: 9,
        Smoking: 10,
    };

    diabetes_features = {
        Sex: 1,
        Age: 2,
        Hypertension: 3,
        Heart_Disease: 4,
        Smoking_History: 5,
        Body_Mass_Index: 6,
        Hemoglobin_A1C: 7,
        Average_Sugar: 8,
    };

    const featureData = featuresPred;
    const featuresKeys = Object.keys(featuresPred);

    // console.log(featuresKeys);
    let mappingKey = {};
    let mappingFeaturesCounts = {};
    let prediction = "";

    if (disease == "Hypertension") {
        prediction = hypertensionPrediction;
        for (let key in hypertension_features) {
            // console.log(key);

            value = hypertension_features[key];

            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
                mappingFeaturesCounts[key] = hypertensionFeatures[value];
            }
        }
    } else if (disease == "Stroke") {
        prediction = strokePrediction;
        for (let key in stroke_features) {
            // console.log(key);

            value = stroke_features[key];


            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
                mappingFeaturesCounts[key] = strokeFeatures[value];
            }
        }
    } else {
        prediction = diabetesPrediction;
        for (let key in diabetes_features) {
            // console.log(key);

            value = diabetes_features[key];

            if (featureData[value] !== undefined) {
                mappingKey[key] = featureData[value];
                mappingFeaturesCounts[key] = diabetesFeatures[value];
            }
        }
    }

    console.log(hypertensionPrediction);

    // console.log("mapping key :" + Object.keys(mappingKey));

    const calculations = document.getElementById("cal");
    const collapsibleText = document.getElementById(`chart`);
    const header = document.getElementById('head');

    header.innerHTML = `<h2><span>${disease}</span> Test Report</h2>`

    calculations.innerHTML = `<p><span>Naive Bayes formula</span>: <br />
        P(Disease|Symptoms) = P(Disease) * P(Symptoms|Disease) /
        P(Symptoms)<br/><br/>

        <span>P(Disease|Symptoms)</span> : <span>${prediction}%</span><br/><br/>

        <span>P(Disease)</span>: ${classCountOne}/${totalCount} = <span>${Math.round(Pdisease * 100)}%</span><br/><br/>
        </p>`;

    collapsibleText.innerHTML += `
        <div class="chart-container">
            <canvas class="chart" id="polar-area-1"></canvas>
            <canvas class="chart" id="bar-chart-1"></canvas>
        </div>`;


    let probabilities = [];
    for (let symptom in mappingKey) {
        let probability = mappingKey[symptom];
        probabilities.push(probability);
        calculations.innerHTML += `<p>P(Symtom: <span>${symptom}</span> | Disease) : ${mappingFeaturesCounts[symptom]} / ${classCountOne}  = <span>${Math.round(
            probability
        )}%</span></p>`;
    }

    const labels = Object.keys(mappingKey).map(
        (key) => `${key}: ${Math.round(mappingKey[key])}%`
    );
    const data = Object.values(mappingKey);
    const backgroundColor = generateColor(labels.length);
    const borderColor = generateColor(labels.length);


    // const labels = [
    //     "Age: 60%",
    //     "Sex: 55%",
    //     "Chest_Pain: 70%",
    //     "Trest_BPS: 65%",
    //     "Cholesterol: 58%",
    //     "Fasting_Blood_Sugar: 62%",
    //     "Rest_ECG: 68%",
    //     "Thalach: 72%",
    //     "Exercise_induced_Angina: 60%",
    //     "Oldpeak: 55%",
    //     "ST_Segment_Slope: 65%",
    //     "Cardiac_Arrhythmia: 70%",
    //     "Thalassemia: 58%",
    // ];

    // const data = [60, 55, 70, 65, 58, 62, 68, 72, 60, 55, 65, 70, 58];

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
                    margin: 0.11,
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
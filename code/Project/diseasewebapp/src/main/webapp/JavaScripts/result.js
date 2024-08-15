function clearUrl() {
    const url = new URL(window.location.href);
    url.search = "";
    window.history.replaceState(null, "", url);
}

window.onload = function () {
    const url = new URLSearchParams(window.location.search);

    console.log(url);
    const predictedClasses = JSON.parse(
        decodeURIComponent(url.get("predictedClasses"))
    );
    console.log(predictedClasses);

    const featuresPred = JSON.parse(
        decodeURIComponent(url.get("featuresPred"))
    );
    console.log(featuresPred);

    const featuresInput = JSON.parse(
        decodeURIComponent(url.get("featureInput"))
    );
    console.log(featuresInput);

    const hypertension_features = featuresInput.Hypertension;

    stroke_features = featuresInput.Stroke;

    diabetes_features = featuresInput.Diabetes;

    const diseases = Object.keys(predictedClasses);

    const sortedDiseases = diseases.sort((a, b) => predictedClasses[b] - predictedClasses[a]);

    console.log(sortedDiseases);

    let c = 0;
    sortedDiseases.forEach((disease, index) => {
        console.log(disease);
        const prediction = predictedClasses[disease];
        const featureData = featuresPred[disease];

        let mappingKey = {};

        if (disease == "Hypertension") {
            for (let key in hypertension_features) {
                value = hypertension_features[key];

                if (featureData[value] !== undefined) {
                    mappingKey[key] = featureData[value];
                }
            }
        } else if (disease == "Stroke") {
            for (let key in stroke_features) {
                value = stroke_features[key];

                if (featureData[value] !== undefined) {
                    mappingKey[key] = featureData[value];
                }
            }
        } else {
            for (let key in diabetes_features) {
                value = diabetes_features[key];

                if (featureData[value] !== undefined) {
                    mappingKey[key] = featureData[value];
                }
            }
        }

        const collapsibleText = document.getElementById(
            `result-${index + 1}`
        );

        c++;

        const header = document.getElementById(`head-${c}`);

        console.log(`head-${c}`);

        header.innerHTML = `<span>${disease}</span> Report : (${prediction}%)<br>`;

        collapsibleText.innerHTML = `<h2>${disease.charAt(0).toUpperCase() + disease.slice(1)
            }</h2>
                <div id="reportBtn-div">
                    <h3>Predicted possibility: ${prediction}%</h3>
                    <button onclick="analyzeReport('${disease}')">Analyze Report</button>
                </div>

                <div class="chart-container">
                    <canvas class="chart" id="polar-area-${index + 1
            }"></canvas>
                    <canvas class="chart" id="bar-chart-${index + 1
            }"></canvas>
                </div>`;

        const disclaimer = document.getElementById(`head-${c}`);

        if (prediction > 65) {
            disclaimer.innerHTML += `<p class="disclaimer">"The predicted value is based on historical data and should not be considered a definitive diagnosis, please consult a qualified healthcare professional <a href="https://www.nhs.uk/">NHS</a>."</p>`;
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
                    label: "Systoms Probablity",
                    data: data,
                    backgroundColor: backgroundColor,
                    borderColor: borderColor,
                    borderWidth: 1,
                },
            ],
        };

        let polarCtx = document
            .getElementById(`polar-area-${index + 1}`)
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
                                if (context.parsed.y !== null) {
                                    label += Math.round(context.raw) + "%";
                                }
                                return label;
                            },
                        },
                    },
                },
            },
        });

        let barCtx = document
            .getElementById(`bar-chart-${index + 1}`)
            .getContext("2d");
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
                                if (context.parsed.y !== null) {
                                    label += Math.round(context.raw) + "%";
                                }
                                return label;
                            },
                        },
                    },
                },
            },
        });
    });
};

function analyzeReport(disease) {
    const url = new URLSearchParams(window.location.search);
    const predictedClasses = JSON.parse(
        decodeURIComponent(url.get("predictedClasses"))
    );
    const featuresPred = JSON.parse(
        decodeURIComponent(url.get("featuresPred"))
    );
    const totalExamples = JSON.parse(
        decodeURIComponent(url.get("totalCount"))
    );
    const classCountOne = JSON.parse(
        decodeURIComponent(url.get("ClassCountOne"))
    );

    const FeatureCounts = JSON.parse(
        decodeURIComponent(url.get("featureCounts"))
    );

    const FeatureInput = JSON.parse(
        decodeURIComponent(url.get("featureInput"))
    );


    const diseasePrediction = JSON.stringify({
        [disease]: predictedClasses[disease],
    });

    const diseaseFeatureCounts = JSON.stringify({
        [disease]: FeatureCounts[disease],
    });

    const diseaseFeatureInput = JSON.stringify({
        [disease]: FeatureInput[disease],
    });

    const diseaseFeatures = JSON.stringify(featuresPred[disease]);
    const diseaseTotalCount = JSON.stringify(totalExamples[disease]);
    const diseaseClassCount = JSON.stringify(classCountOne[disease]);

    window.location.href = `report.html?disease=${encodeURIComponent(
        disease
    )}&prediction=${encodeURIComponent(diseasePrediction)}
    &features=${encodeURIComponent(diseaseFeatures)}
    &totalExamples=${encodeURIComponent(diseaseTotalCount)}
    &classCountOne=${encodeURIComponent(diseaseClassCount)}
    &featureCounts=${encodeURIComponent(diseaseFeatureCounts)}
    &featureInput=${encodeURIComponent(diseaseFeatureInput)}`;
}

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
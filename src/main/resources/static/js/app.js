let currentPoiId = null;


const map = L.map("map").setView([49.245, 7.365], 13);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap"
}).addTo(map);

document.getElementById("open-login-button").addEventListener("click", function () {
    document.getElementById("login-form").style.display = "block";
});

document.getElementById("close-login").addEventListener("click", function () {
    document.getElementById("login-form").style.display = "none";
});
document.getElementById("close-register").addEventListener("click", function () {
    document.getElementById("register-form").style.display = "none";
});

document.getElementById("image-modal").addEventListener("click", function () {
    document.getElementById("image-modal").style.display = "none";
});
document.getElementById("submit-login").addEventListener("click", function () {
    const username = document.getElementById("login-username").value;
    const password = document.getElementById("login-password").value;

    if (!username || !password) {
        alert("Bitte Username und Passwort eingeben.");
        return;
    }

    fetch("api/users/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username: username, password: password})
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Anmeldung fehlgeschlagen. mit Status : " + response.status);
            }

            const token = response.headers.get("Authorization");
            localStorage.setItem("token", token);
            return response.json();
        })
        .then(function (data) {
            document.getElementById("login-form").style.display = "none";
            showLoggedInView(data);
        })
        .catch(function (error) {
            console.error(error.message);
            alert("Login fehlgeschlagen. Username oder Passwort falsch.");
        });
});



document.getElementById("register-button").addEventListener("click", function (){
    document.getElementById("register-form").style.display = "block";
})


document.getElementById("submit-register").addEventListener("click", function ()
    {
    const username = document.getElementById("reg-username").value;
    const password = document.getElementById("reg-password").value;
    const email = document.getElementById("reg-email").value;
    const firstname = document.getElementById("reg-firstname").value;
    const lastname = document.getElementById("reg-lastname").value;
    const street = document.getElementById("reg-street").value;
    const streetNr = document.getElementById("reg-streetnr").value;
    const zip = document.getElementById("reg-zip").value;
    const city = document.getElementById("reg-city").value;

    if (!username || !password || !email || !firstname || !lastname
        || !street || !streetNr || !zip || !city) {
        alert("Please fill in all fields.");
        return;
        }
    fetch("api/users/register",{
        method: "POST",
        headers: {"Content-Type" : "application/json"},
        body: JSON.stringify({username : username, password: password, email: email, firstname: firstname, lastname: lastname
        ,street: street, streetNr: streetNr, zip: zip, city: city})
    }).then(function (response)
    {
        if (!response.ok) {
            throw new Error("Registration failed with status " + response.status);
        }
        const token = response.headers.get("Authorization");
        localStorage.setItem("token", token);
        return response.json();
    }).then(function(data) {
        console.log("Registered successfully as", data.firstname, data.lastname);

        document.getElementById("register-form").style.display = "none";

        alert("Registrierung erfolgreich! Sie sind jetzt eingeloggt.");

        // Auto-login
       showLoggedInView(data);
    }).catch(function(error) {
        console.error(error.message);
        alert("Registration fehlgeschlagen. Der Benutzername ist möglicherweise bereits vergeben.");
    });
    });


function showLoggedInView(data) {
    document.getElementById("landing").style.display = "none";

    document.getElementById("main-content").style.display = "flex";
    document.getElementById("map").style.display = "block";
    document.getElementById("welcome-bar").style.display = "block";
    document.getElementById("tab-bar").style.display = "block";

    document.getElementById("welcome-name").textContent =
        data.firstname + " " + data.lastname;

    map.invalidateSize();
    loadPOIs();
}

function loadPOIs(){
    const token = localStorage.getItem("token");

    fetch("api/pois", {
        headers: {"Authorization" : token}
    }).then(function (response){
        return response.json();
    }).then(function (pois) {
        console.log("Got", pois.length, "POIs");

        pois.forEach(function (poi) {
            L.marker([poi.lat, poi.lon]).addTo(map).bindPopup(poi.name).on("click", function (){
                currentPoiId = poi.id;
                document.getElementById("poi-name").textContent = poi.name;
                showPoiAttribute("poi-phone-row", "poi-phone", poi.phone);
                showPoiAttribute("poi-amenity-row", "poi-amenity", poi.amenity);
                document.getElementById("poi-details").style.display = "block";
                loadRatings(poi.id);
            });
            });
    });
}
function loadRatings(poiId) {
    const token = localStorage.getItem("token");

    fetch("api/ratings/poi/" + poiId, {
        headers: {"Authorization": token}
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Could not load ratings. Status: " + response.status);
            }
            return response.json();
        })
        .then(function (ratings) {
            const tbody = document.getElementById("ratings-body");
            tbody.innerHTML = "";

            ratings.forEach(function (rating) {
                const row = createRatingRow(rating, rating.userFullName, false);
                tbody.appendChild(row);
            });
        })
        .catch(function (error) {
            console.error(error.message);
            alert("Bewertungen konnten nicht geladen werden.");
        });
}

function formatDate(isoString) {
    const d = new Date(isoString);
    return d.toLocaleString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
    });
}

document.getElementById("submit-rating").addEventListener("click", function() {
    const token = localStorage.getItem("token");
    const grade = parseInt(document.getElementById("rating-grade").value);
    const txt = document.getElementById("rating-txt").value.trim();
    const fileInput = document.getElementById("rating-image");
    const file = fileInput.files[0];

    if (!currentPoiId) {
        alert("Bitte wählen Sie zuerst eine Location aus.");
        return;
    }

    if (!grade || grade < 1 || grade > 5) {
        alert("Bitte wählen Sie eine Bewertung aus.");
        return;
    }

    if (txt.length === 0) {
        alert("Bitte geben Sie einen Kommentar ein.");
        return;
    }

    if (file) {
        const formData = new FormData();
        formData.append("file", file);

        fetch("api/images", {
            method: "POST",
            headers: {"Authorization": token},
            body: formData
        })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error("Image upload failed with status " + response.status);
                }
                return response.json();
            })
            .then(function(imageData) {
                submitRating(token, grade, txt, imageData.id);
            })
            .catch(function(error) {
                console.error(error.message);
                alert("Bild konnte nicht hochgeladen werden.");
            });
    } else {
        submitRating(token, grade, txt, null);
    }
});
function resetRatingForm() {
    document.getElementById("rating-txt").value = "";
    document.getElementById("rating-grade").value = "0";
    document.getElementById("rating-image").value = "";

    document.querySelectorAll("#star-picker span").forEach(function(star) {
        star.textContent = "☆";
    });
}

function submitRating(token, grade, txt, imageId) {
    fetch("api/ratings", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token
        },
        body: JSON.stringify({
            poiId: currentPoiId,
            grade: grade,
            txt: txt,
            imageId: imageId
        })
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Rating could not be saved. Status: " + response.status);
            }
            return response.json();
        })
        .then(function(data) {
            console.log("Rating saved:", data);
            loadRatings(currentPoiId);
            resetRatingForm();
        })
        .catch(function(error) {
            console.error(error.message);
            alert("Bewertung konnte nicht gespeichert werden.");
        });
}
function editMyRating(rating, txt, grade) {
    const token = localStorage.getItem("token");
    fetch("api/ratings/" + rating.id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": token
        },
        body: JSON.stringify({
            txt: txt,
            grade: grade
        })
    }).then(function (response) {
        if (!response.ok) {
            throw new Error("Rating could not be saved. Status: " + response.status);
        }
        console.log("Rating saved ");
        loadMyRatings();
    }).catch(function (error){
        console.error(error.message)
        alert("Bewertung konnte nicht gespeichert werden.");
    });
}
function loadMyRatings() {
    const token = localStorage.getItem("token");

    fetch("api/ratings/me", {
        headers: {"Authorization": token}
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Could not load own ratings. Status: " + response.status);
            }
            return response.json();
        })
        .then(function (ratings) {
            const tbody = document.getElementById("my-ratings-body");
            tbody.innerHTML = "";

            ratings.forEach(function (rating) {
                const row = createRatingRow(rating, rating.poiName, true);
                tbody.appendChild(row);
            });
        })
        .catch(function (error) {
            console.error(error.message);
            alert("Meine Bewertungen konnten nicht geladen werden.");
        });
}

    document.getElementById("show-my-ratings").addEventListener("click", function() {
    document.getElementById("map").style.display = "none";
    document.getElementById("poi-details").style.display = "none";
    document.getElementById("my-ratings").style.display = "block";
    loadMyRatings();
});

    document.getElementById("show-map").addEventListener("click", function() {
    document.getElementById("my-ratings").style.display = "none";
    document.getElementById("map").style.display = "block";
    map.invalidateSize();
});


document.getElementById("logout-button").addEventListener("click", function() {
    const token = localStorage.getItem("token");

    fetch("api/users/logout", {
        method: "POST",
        headers: { "Authorization": token }
    })
        .then(function() {
            localStorage.removeItem("token");
            location.reload();
        });
});

function showFullImage(imageId) {
    document.getElementById("image-modal-content").src = "/api/images/" + imageId;
    document.getElementById("image-modal").style.display = "flex";
}

function renderStars(grade) {
    let stars = "";
    for (let i = 1; i <= 5; i++) {
        if (i <= grade) {
            stars += "★";  // filled star
        } else {
            stars += "☆";  // empty star
        }
    }
    return stars;
}

function createRatingRow(rating, secondColumnText, showActionButtons) {
    const template = document.getElementById("rating-row-template");
    const row = template.content.firstElementChild.cloneNode(true);

    row.querySelector(".rating-date").textContent = formatDate(rating.createdAt);
    row.querySelector(".rating-person-or-location").textContent = secondColumnText;
    row.querySelector(".rating-comment").textContent = rating.txt;
    row.querySelector(".rating-grade").textContent = renderStars(rating.grade);

    const imageCell = row.querySelector(".rating-image");

    if (rating.imageId != null) {
        const image = document.createElement("img");
        image.src = "/api/images/" + rating.imageId;
        image.width = 50;
        image.alt = "Bewertungsbild";
        image.classList.add("rating-thumbnail");

        image.addEventListener("click", function () {
            showFullImage(rating.imageId);
        });

        imageCell.appendChild(image);
    }

    const actionCell = row.querySelector(".rating-action");
    if (showActionButtons) {
        const editButton = document.createElement("button");
        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        editButton.type = "button";
        deleteButton.textContent = "Löschen";
        editButton.textContent = "Bearbeiten";
        deleteButton.classList.add("rating-delete-button");
        editButton.classList.add("rating-edit-button");
        editButton.addEventListener("click", function (){
            const newTxt = prompt("Kommentar bearbeiten:", rating.txt);
            if(newTxt === null){
                return;
            }
            if(newTxt.trim() === ""){
                alert("Bitte geben Sie ein Kommentar !");
                return;
            }
            const newGrade = prompt("Grade bearbeiten:", rating.grade);
            if(newGrade < 1 || newGrade > 5){
                alert("Only between 1-5 allowed !");
                return;
            }
            if (newGrade === null){
                return;
            }
            editMyRating(rating,newTxt,newGrade);
            console.log("Old comment: ", rating.txt);
            console.log("new text: ", newTxt);
        });
        actionCell.appendChild(editButton);

        deleteButton.addEventListener("click", function () {
            if (confirm("Möchten Sie diese Bewertung wirklich löschen?")) {
                deleteRating(rating.id);
            }
        });

        actionCell.appendChild(deleteButton);
    } else {
        actionCell.remove();
    }

    return row;
}
function deleteRating(ratingId) {
    const token = localStorage.getItem("token");

    fetch("api/ratings/" + ratingId, {
        method: "DELETE",
        headers: {"Authorization": token}
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Rating could not be deleted. Status: " + response.status);
            }

            loadMyRatings();

            if (currentPoiId != null) {
                loadRatings(currentPoiId);
            }
        })
        .catch(function (error) {
            console.error(error.message);
            alert("Bewertung konnte nicht gelöscht werden.");
        });
}

document.querySelectorAll("#star-picker span").forEach(function(star) {
    star.addEventListener("click", function() {
        const value = parseInt(this.dataset.value);
        document.getElementById("rating-grade").value = value;

        document.querySelectorAll("#star-picker span").forEach(function(s, index) {
            s.textContent = (index < value) ? "★" : "☆";
        });
    });
});

function showPoiAttribute(rowId, valueId, value) {
    const row = document.getElementById(rowId);
    const valueElement = document.getElementById(valueId);

    if (value == null || String(value).trim() === "") {
        row.style.display = "none";
        valueElement.textContent = "";
    } else {
        row.style.display = "block";
        valueElement.textContent = value;
    }
}
document.getElementById("delete-account-button").addEventListener("click", function () {
    const token = localStorage.getItem("token");

    if (!confirm("Möchten Sie Ihren Account wirklich löschen? Alle Ihre Bewertungen werden ebenfalls gelöscht.")) {
        return;
    }

    fetch("api/users/me", {
        method: "DELETE",
        headers: { "Authorization": token }
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Account could not be deleted. Status: " + response.status);
            }

            localStorage.removeItem("token");
            alert("Account wurde gelöscht.");
            location.reload();
        })
        .catch(function (error) {
            console.error(error.message);
            alert("Account konnte nicht gelöscht werden.");
        });
});
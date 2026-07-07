let currentPoiId = null;

//     document.getElementById("open-login").addEventListener("click", function() {
//     document.getElementById("login-dialog").style.display = "block";
// });


const map = L.map("map").setView([49.245, 7.365], 13);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap"
}).addTo(map);

document.getElementById("login-button-inline").addEventListener("click", function () {
    const username =  document.getElementById("login-username-inline").value;
    const password = document.getElementById("login-password-inline").value;

    console.log("Username:", username);

    fetch("api/users/login",
        {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : JSON.stringify({username: username, password: password})
        }).then(function (response){
        const token = response.headers.get("Authorization");
        localStorage.setItem("token", token);
        return response.json();
    }).then(function (data) {
        console.log("Logged in as:", data.firstname, data.lastname);

        document.getElementById("map").style.display = "block";
        // document.getElementById("login-dialog").style.display = "none";
        // document.getElementById("register-dialog").style.display = "none";
        document.getElementById("landing").style.display="none";
        document.getElementById("show-my-ratings").style.display = "inline-block";
        document.getElementById("show-map").style.display = "inline-block";
        // document.getElementById("open-login").style.display = "none";

        document.getElementById("welcome-name").textContent = data.firstname + " " + data.lastname;
        document.getElementById("welcome-bar").style.display = "block";
        map.invalidateSize();

        loadPOIs();
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
        document.getElementById("welcome-name").textContent = data.firstname + " " + data.lastname;
        document.getElementById("welcome-bar").style.display = "block";
        document.getElementById("show-my-ratings").style.display = "inline-block";
        document.getElementById("show-map").style.display = "inline-block";
        document.getElementById("map").style.display = "block";
        document.getElementById("landing").style.display="none";
        map.invalidateSize();
        loadPOIs();
    }).catch(function(error) {
        console.error(error.message);
        alert("Registration failed. The username might already be taken.");
    });
    });



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
                document.getElementById("poi-phone").textContent = poi.phone || "unbekannt";
                document.getElementById("poi-amenity").textContent = poi.amenity;
                document.getElementById("poi-details").style.display = "block";
                loadRatings(poi.id);
            });
            });
    });
}
function loadRatings(poiId){
    const token = localStorage.getItem("token");
    fetch("api/ratings/poi/" + poiId,
        {
            headers: {"Authorization" : token}
        }).then(function (response){
            return response.json();
    }).then(function (ratings){
        const tbody = document.getElementById("ratings-body");
        tbody.innerHTML= "";
        ratings.forEach(function (rating){
        const row = document.createElement("tr");//table row
            let imageCell = "";
            if (rating.imageId != null) {
                imageCell = "<img src='/api/images/" + rating.imageId
                    + "' width='50' onclick='showFullImage(" + rating.imageId + ")'>";
            }
            row.innerHTML=
                "<td>" + formatDate(rating.createdAt) + "</td>"+
                "<td>" + rating.username  + "</td>"+
                "<td>" + rating.txt       + "</td>"+
                "<td>" + renderStars(rating.grade)+ "</td>"+
                "<td>" + imageCell + "</td>";
            tbody.appendChild(row);
        })
    })
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
    const txt = document.getElementById("rating-txt").value;
    const fileInput = document.getElementById("rating-image");
    const file = fileInput.files[0];

    if(file){
        const formData = new FormData();
        formData.append("file", file);
        fetch("api/images",{
            method : "POST",
            headers : {"Authorization": token},
            body : formData
        }).then(function (response){
            return response.json();
        }).then(function (imageData) {
            submitRating(token, grade, txt, imageData.id);
            document.getElementById("rating-grade").value = "0";
            document.querySelectorAll("#star-picker span").forEach(function(s) {
                s.textContent = "☆";
            });
        });
    } else {
        submitRating(token, grade, txt, null);
        document.getElementById("rating-grade").value = "0";
        document.querySelectorAll("#star-picker span").forEach(function(s) {
            s.textContent = "☆";
        });
    }
});

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
            return response.json();
        })
        .then(function(data) {
            console.log("Rating saved:", data);
            loadRatings(currentPoiId);

            document.getElementById("rating-txt").value = "";
            document.getElementById("rating-grade").value = "";
            document.getElementById("rating-image").value = "";
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

function loadMyRatings() {
    const token = localStorage.getItem("token");

    fetch("api/ratings/me", {
        headers: { "Authorization": token }
    })
        .then(function(response) { return response.json(); })
        .then(function(ratings) {
            const tbody = document.getElementById("my-ratings-body");
            tbody.innerHTML = "";

            ratings.forEach(function(rating) {
                const row = document.createElement("tr");
                let imageCell = "";
                if (rating.imageId != null) {
                    imageCell = "<img src='/api/images/" + rating.imageId
                        + "' width='50' onclick='showFullImage(" + rating.imageId + ")'>";
                }
                row.innerHTML =
                    "<td>" + formatDate(rating.createdAt) + "</td>" +
                    "<td>" + rating.poiName + "</td>" +
                    "<td>" + rating.txt + "</td>" +
                    "<td>" + renderStars(rating.grade) + "</td>" +
                    "<td>" + imageCell + "</td>";
                tbody.appendChild(row);
            });
        });
}
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
document.querySelectorAll("#star-picker span").forEach(function(star) {
    star.addEventListener("click", function() {
        const value = parseInt(this.dataset.value);
        document.getElementById("rating-grade").value = value;

        document.querySelectorAll("#star-picker span").forEach(function(s, index) {
            s.textContent = (index < value) ? "★" : "☆";
        });
    });
});
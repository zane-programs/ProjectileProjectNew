(function (window) {
    let decimalPlacesToRound;

    function buildQueryString(queryJson) {
        // I actually did write this myself, but I had to do a bit
        // of logical thinking to do so.
        return "?" + queryJson.map(k => encodeURIComponent(Object.keys(k)[0]) + "=" + encodeURIComponent(k[Object.keys(k)[0]])).join("&")
    }

    // see if the referrer is the form entry page
    function checkReferrerIsForm(referrer) {
        // this is necessary depending on whether or not it's running on port 80 or 443 (browsers
        // will omit the port if so). I think I might run a demo instance of this on my VPS behind
        // Nginx, and this would be necessary if so.
        let referrerWithoutPort = window.location.protocol + "//" + window.location.hostname + "/";
        let referrerWithPort = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/";

        return (referrer === referrerWithPort) || (referrer === referrerWithoutPort);
    }

    // get position at time (for button);
    async function getPosAtTime() {
        let queryString = buildQueryString([
            { launchVelocity: document.querySelector("#launchVelocity").value },
            { launchAngle: document.querySelector("#launchAngle").value },
            { time: document.querySelector("#timeInpt").value }
        ]);
        let posReq = await fetch("/api/getPositionAtTime" + queryString);
        let posJson = await posReq.json();

        document.querySelector("#xPos").innerText = posJson.x.toFixed(decimalPlacesToRound) + "m";
        document.querySelector("#yPos").innerText = posJson.y.toFixed(decimalPlacesToRound) + "m";

        document.querySelector("div#posContainer").classList.remove("hidden");
    }

    function onPageLoaded() {
        // if the last page visited was the form, then the "back" link should send the user back
        // using the built-in history.back function. If not, then have the link go back to the
        // form page using a regular link;
        decimalPlacesToRound = document.querySelector("#decimalPlacesToRound").value;

        let formIsReferrer = checkReferrerIsForm(document.referrer);
        document.querySelector("a#backLink").href = (formIsReferrer) ? "javascript:history.back();" : "/";

        // fake form (does nothing) for the sole purpose of built-in value checking
        // (e.g. you can't type in a negative number)
        document.querySelector("form").addEventListener("submit", getPosAtTime);
    }

    document.addEventListener("DOMContentLoaded", onPageLoaded); // when page loads, run onPageLoaded

})(window);
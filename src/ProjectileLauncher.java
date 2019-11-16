/**
 * Project: ProjectileMotionNew
 * Class: ProjectileLauncher
 * Due: November 19, 2019
 * <P>This is my projectile motion project. Please see README.md for more information.</P>
 * @author Zane St. John
 * @version 1.0
 */

import static spark.Spark.*; // All I learned about the Sparkjava framework came from its docs. (http://sparkjava.com/documentation)

public class ProjectileLauncher {
    // This is a technique I learned a few years ago in JavaScript. I can't
    // remember where I learned it, but at this point I don't think I need to
    // give anyone else credit for it.
    public static double roundToDecimals(double num, double places) {
        double placesQuantified = Math.pow(10, places); // ten to the (places)
        double numRounded = Math.round(num * placesQuantified) / placesQuantified;

        return numRounded;
    }

    // makes zeros for the step parameter in HTML (depending on user-selected decimal recision)
    public static String generateZeros(int zerosToMake) {
        String zerosToReturn = "";
        for (int i = 0; i < zerosToMake; i++) {
            zerosToReturn += "0";
        }
        return zerosToReturn;
    }

    public static void main(String[] args) {
        port(3000); // runs server on port 3000

        staticFiles.location("static"); // static files in "static" folder

        // PROJECTILE INFORMATION PAGE
        get("/projectile", (req, res) -> {
            // init projectile object
            Projectile projectile = new Projectile(Double.parseDouble(req.queryParams("launchVelocity")), Double.parseDouble(req.queryParams("launchAngle")));

            int decimalPlaces = Integer.parseInt(req.queryParams("decimalPlaces")); // user-requested decimal precision

            double flightTime = projectile.getFlightTime(); // flight time

            // using roundToDecimals function to round all values
            // values at launch (all values are zero, as start height is zero)
            double launchX = roundToDecimals(0,decimalPlaces);
            double launchY = roundToDecimals(0,decimalPlaces);;
            double launchTime = roundToDecimals(0,decimalPlaces);;

            // values at apex
            double apexX = roundToDecimals(projectile.getXPosAtTime(flightTime / 2), decimalPlaces);
            double apexY = roundToDecimals(projectile.getYPosAtTime(flightTime / 2), decimalPlaces);
            double apexTime = roundToDecimals(flightTime / 2, decimalPlaces);

            // values at return (we don't need a "returnTime" because it would have same value as flightTime
            double returnX = roundToDecimals(projectile.getXPosAtTime(flightTime), decimalPlaces);
            double returnY = roundToDecimals(projectile.getYPosAtTime(flightTime), decimalPlaces);

            // number of zeros for step parameter for time input (decimal places depending on what you rounded to)
            String stepZeros = generateZeros(decimalPlaces - 1);

            // render HTML (sorry, I know this is ugly)
            return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>Projectile</title><style>table{border-collapse: collapse;}table,th,td{border:1px solid #000;}.hidden{display:none;}</style><script type=\"text/javascript\" src=\"/js/app.js\"></script></head><body>" + "" +
                    "<h1>Projectile Results</h1>" +
                    "<strong>NOTE: all values are approximate</strong><br><br><strong>Rounded to " + decimalPlaces + " decimal places</strong><br>" +
                    "<strong>Flight Time: " + roundToDecimals(flightTime, decimalPlaces) + " seconds</strong><br><br>" +
                    "<table> <tr> <th>step</th> <th>x</th> <th>y</th> <th>time</th> </tr><tr> <td>launch</td><td>" + launchX +
                    "m</td><td>" + launchY +
                    "m</td><td>" + launchTime +
                    "s</td></tr><tr> <td>apex</td><td>" + apexX +
                    "m</td><td>" + apexY +
                    "m</td><td>" + apexTime +
                    "s</td></tr><tr> <td>return</td><td>" + returnX +
                    "m</td><td>" + returnY +
                    "m</td><td>" + roundToDecimals(flightTime, decimalPlaces) +
                    "s</td></tr></table><br>" +
                    "<form action=\"javascript:void(0);\"> <label for=\"timeInpt\"><strong>Get position at time: </strong></label> <input type=\"number\" id=\"timeInpt\" min=\"0\" step=\"0." + stepZeros + "1\" placeholder=\"Time (s)\" required><br><input type=\"submit\" value=\"Get position\"><br><div id=\"posContainer\" class=\"hidden\"><br><strong>x:</strong>&nbsp;<span id=\"xPos\"></span><br><strong>y:</strong>&nbsp;<span id=\"yPos\"></span> </div></form><br>" +
                    "<a id=\"backLink\" href=\"javascript:void(0);\">&lt;&nbsp;Try another projectile</a>" +
                    // hidden inputs for API usage (so that the webpage itself can use these values)
                    "<input type=\"hidden\" id=\"launchVelocity\" value=\"" + req.queryParams("launchVelocity") + "\">" +
                    "<input type=\"hidden\" id=\"launchAngle\" value=\"" + req.queryParams("launchAngle") + "\">" +
                    "<input type=\"hidden\" id=\"decimalPlacesToRound\" value=\"" + req.queryParams("decimalPlaces") + "\">" +
                    "</body></html>";
        });

        // API (for getting position at certain time)
        path("/api", () -> {
            // get position at given time
            get("/getPositionAtTime", (req, res) -> {
                // init projectile
                Projectile projectile = new Projectile(Double.parseDouble(req.queryParams("launchVelocity")), Double.parseDouble(req.queryParams("launchAngle")));
                double timeForPosition = Double.parseDouble(req.queryParams("time")); // time for requested position

                res.type("application/json"); // send JSON
                return "{\"x\":" + projectile.getXPosAtTime(timeForPosition) + ",\"y\":" + projectile.getYPosAtTime(timeForPosition) + "}";
            });
        });
    }
}
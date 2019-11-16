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

    public static String generateZeros(int zerosToMake) {
        String zerosToReturn = "";
        for (int i = 0; i < zerosToMake; i++) {
            zerosToReturn += "0";
        }
        return zerosToReturn;
    }

    // learned of exceptions from https://stackoverflow.com/a/8707928
    public static void main(String[] args) throws java.io.IOException {
//        System.out.println(get);

//        Projectile projectile = new Projectile(10,30);
//        System.out.println(projectile.getFlightTime());
//        System.out.println(projectile.getYPosAtTime((projectile.getFlightTime() / 2)));

        port(3000);

        staticFiles.location("static");

        get("/projectile", (req, res) -> {
            Projectile projectile = new Projectile(Double.parseDouble(req.queryParams("launchVelocity")), Double.parseDouble(req.queryParams("launchAngle")));

            int decimalPlaces = Integer.parseInt(req.queryParams("decimalPlaces"));

            double flightTime = projectile.getFlightTime();

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
                    "<input type=\"hidden\" id=\"launchVelocity\" value=\"" + req.queryParams("launchVelocity") + "\">" +
                    "<input type=\"hidden\" id=\"launchAngle\" value=\"" + req.queryParams("launchAngle") + "\">" +
                    "<input type=\"hidden\" id=\"decimalPlacesToRound\" value=\"" + req.queryParams("decimalPlaces") + "\">" +
                    "</body></html>";
        });

        path("/api", () -> {
            get("/getPositionAtTime", (req, res) -> {
                Projectile projectile = new Projectile(Double.parseDouble(req.queryParams("launchVelocity")), Double.parseDouble(req.queryParams("launchAngle")));
                double timeForPosition = Double.parseDouble(req.queryParams("time"));

                res.type("application/json");
                return "{\"x\":" + projectile.getXPosAtTime(timeForPosition) + ",\"y\":" + projectile.getYPosAtTime(timeForPosition) + "}";
            });
        });
    }
}
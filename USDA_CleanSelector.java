import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class USDA_CleanSelector {

    private static final String API_KEY = "c7aAYTcFYUcGoMKSzE2yY3qnQ5dGS7S9K8027z35";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // =========================
            // 🔹 INPUT
            // =========================
            System.out.print("Entrez un aliment : ");
            String query = scanner.nextLine();

            // =========================
            // 🔹 SEARCH (FOUNDATION ONLY)
            // =========================
            String searchJson = request(
                    "https://api.nal.usda.gov/fdc/v1/foods/search"
                            + "?query=" + URLEncoder.encode(query, "UTF-8")
                            + "&dataType=Foundation"
                            + "&pageSize=10"
                            + "&api_key=" + API_KEY
            );

            String[] names = extract(searchJson, "\"description\":\"", 10);
            String[] ids = extract(searchJson, "\"fdcId\":", 10);

            if (names.length == 0) {
                System.out.println("Aucun produit trouvé.");
                return;
            }

            // =========================
            // 🔹 AFFICHAGE PRODUITS
            // =========================
            System.out.println("\n--- PRODUITS ---");

            for (int i = 0; i < names.length; i++) {
                System.out.println((i + 1) + ". " + names[i]);
            }

            // =========================
            // 🔹 CHOIX
            // =========================
            System.out.print("\nChoisis un produit : ");
            int choice = scanner.nextInt();

            String fdcId = ids[choice - 1];

            // =========================
            // 🔹 PRODUIT FINAL
            // =========================
            String productJson = request(
                    "https://api.nal.usda.gov/fdc/v1/food/" + fdcId
                            + "?api_key=" + API_KEY
            );

            // =========================
            // 🔥 PROXIMATES
            // =========================
            System.out.println("\n--- PROXIMATES ---");

            printProximate(productJson, "Energy");
            printProximate(productJson, "Protein");
            printProximate(productJson, "Total lipid (fat)");
            printProximate(productJson, "Carbohydrate, by difference");
            printProximate(productJson, "Fiber, total dietary");
            printProximate(productJson, "Sugars");
            printProximate(productJson, "Water");

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        scanner.close();
    }

    // ======================================================
    // 🌐 REQUEST
    // ======================================================
    private static String request(String urlString) throws Exception {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();

        return sb.toString();
    }

    // ======================================================
    // 🔎 SEARCH EXTRACT
    // ======================================================
    private static String[] extract(String json, String key, int max) {

        String[] result = new String[max];
        int count = 0;
        int index = 0;

        while (count < max) {

            int start = json.indexOf(key, index);
            if (start == -1) break;

            start += key.length();
            int end = json.indexOf("\"", start);

            if (key.contains("fdcId")) {
                end = json.indexOf(",", start);
            }

            result[count++] = json.substring(start, end);
            index = end;
        }

        String[] finalResult = new String[count];
        System.arraycopy(result, 0, finalResult, 0, count);

        return finalResult;
    }

    // ======================================================
    // 🔥 PROXIMATES FIX (CORRECT USDA PARSING)
    // ======================================================
    private static void printProximate(String json, String nutrientName) {

        String search = "\"foodNutrients\":[";

        int index = json.indexOf(search);
        if (index == -1) {
            System.out.println(nutrientName + " : N/A");
            return;
        }

        int pos = index;

        while (true) {

            int nameIndex = json.indexOf("\"name\":\"", pos);
            if (nameIndex == -1) break;

            nameIndex += 8;
            int nameEnd = json.indexOf("\"", nameIndex);

            String name = json.substring(nameIndex, nameEnd);

            if (name.equals(nutrientName)) {

                int amountIndex = json.indexOf("\"amount\":", nameEnd);
                amountIndex += 10;

                int amountEnd = json.indexOf(",", amountIndex);

                String value = json.substring(amountIndex, amountEnd);

                System.out.println(nutrientName + " : " + value);
                return;
            }

            pos = nameEnd;
        }

        System.out.println(nutrientName + " : N/A");

    }
}
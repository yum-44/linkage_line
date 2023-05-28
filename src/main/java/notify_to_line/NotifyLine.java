package notify_to_line;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class NotifyLine {
    public static void main(String[] args) {
        NotifyLine lineNotify = new NotifyLine("YOUR_TOKEN");
        lineNotify.notify("通知です");
    }

    private final String token;
    public NotifyLine(String token) {
        this.token = token;
    }

    public void notify(String message) {
        HttpURLConnection connection = null;
        try {
            // lineAPIへPOSTする
            URL url = new URL("https://notify-api.line.me/api/notify");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "Bearer " + token);
            try (
                 OutputStream os = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(os)) {
                 writer.append("message=").append(URLEncoder.encode(message, "UTF-8")).flush();

                try (
                     InputStream is = connection.getInputStream();
                     BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
                    String res = r.lines().collect(Collectors.joining());

                    if ( ! res.contains("\"message\":\"ok\"") ) {
                        System.out.println(res);
                    }
                }
            }
        } catch ( Exception ignore ) {
            // エラー発生時は何もしない
        } finally {
            if ( connection != null ) {
                connection.disconnect();
            }
        }
    }
}
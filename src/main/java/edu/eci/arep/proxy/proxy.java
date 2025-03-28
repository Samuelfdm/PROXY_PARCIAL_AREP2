package edu.eci.arep.proxy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/")
public class proxy {

    private int aux = 0;
    private static final String USER_AGENT = "Mozilla/5.0";
    //private static final String[] SERVERS = new String[]{"http://localhost:8081/","http://localhost:8082/"};
    private static final String[] SERVERS = new String[]{"http://ec2-54-158-55-143.compute-1.amazonaws.com:8081", "http://ec2-54-175-71-84.compute-1.amazonaws.com:8081"};

    @GetMapping("factors")
    public ResponseEntity<?> factors(@RequestParam("value") String value) throws IOException {
        String serverUrl = roundRobinServer();
        String response = httpConnection(serverUrl + "factors?value=" + value);
        return ResponseEntity.ok(response);
    }

    @GetMapping("primes")
    public ResponseEntity<?> primes(@RequestParam("value") String value) throws IOException {
        String serverUrl = roundRobinServer();
        String response = httpConnection(serverUrl + "primes?value=" + value);
        return ResponseEntity.ok(response);
    }

    private synchronized String roundRobinServer() {
        String server = SERVERS[aux % SERVERS.length];
        aux++;
        return server;
    }

    public String httpConnection(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } else {
            return "GET request not worked";
        }
    }

}

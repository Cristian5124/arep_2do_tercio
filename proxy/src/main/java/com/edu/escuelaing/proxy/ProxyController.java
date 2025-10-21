package com.edu.escuelaing.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {

  private String server1;
  private String server2;
  private int current = 0;

  public ProxyController() {
    server1 = System.getenv("MATH_SERVICE_1");
    server2 = System.getenv("MATH_SERVICE_2");

    if (server1 == null) server1 = "http://localhost:8081";
    if (server2 == null) server2 = "http://localhost:8082";
  }

  @PostMapping("/lucassec")
  public String lucassec(@RequestParam String value) {
    String server = (current % 2 == 0) ? server1 : server2;
    current++;

    try {
      URL url = new URL(server + "/lucassec?value=" + value);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");

      BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream())
      );
      String line;
      String result = "";
      while ((line = in.readLine()) != null) {
        result += line;
      }
      in.close();
      return result;
    } catch (Exception e) {
      return "{\"status\":\"error\",\"result\":\"Error\"}";
    }
  }
}

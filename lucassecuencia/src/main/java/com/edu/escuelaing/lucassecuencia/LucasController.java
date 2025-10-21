package com.edu.escuelaing.lucassecuencia;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LucasController {

  @PostMapping("/lucassec")
  public String lucassec(@RequestParam(value = "value") int value) {
    if (value < 0) {
      return (
        "<div></div>" +
        "<div></div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_17273\">\"operation\":\"Secuencia de Lucas\",</div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_18201\">\"input\":\"" +
        value +
        "\",</div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_19084\"></div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_19171\">\"output\":\"Error: El valor debe ser mayor o igual a 0\"</div>" +
        "<div></div>"
      );
    }
    if (value > 50) {
      return (
        "<div></div>" +
        "<div></div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_17273\">\"operation\":\"Secuencia de Lucas\",</div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_18201\">\"input\":\"" +
        value +
        "\",</div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_19084\"></div>" +
        "<div id=\"yui_3_17_2_1_1698329855689_19171\">\"output\":\"Error: El valor es demasiado grande\"</div>" +
        "<div></div>"
      );
    }
    String sequence = calculateLucasSequence(value);
    return (
      "<div></div>" +
      "<div></div>" +
      "<div id=\"yui_3_17_2_1_1698329855689_17273\">\"operation\":\"Secuencia de Lucas\",</div>" +
      "<div id=\"yui_3_17_2_1_1698329855689_18201\">\"input\":\"" +
      value +
      "\",</div>" +
      "<div id=\"yui_3_17_2_1_1698329855689_19084\"></div>" +
      "<div id=\"yui_3_17_2_1_1698329855689_19171\">\"output\":\"" +
      sequence +
      "\"</div>" +
      "<div></div>"
    );
  }

  private String calculateLucasSequence(int n) {
    if (n == 0) {
      return "2";
    }
    if (n == 1) {
      return "2,1";
    }

    String result = "2,1";
    long a = 2;
    long b = 1;

    for (int i = 2; i <= n; i++) {
      long temp = a + b;
      result += "," + temp;
      a = b;
      b = temp;
    }
    return result;
  }
}
# Proyecto Secuencia de Lucas con Servidor Proxy
## Estudiante: Cristian David Polo Garrido

### Estructura
Básicamente tengo 4 clases principales y un archivo index.html que en conjunto permiten obtener la secuencia de lucas del valor que ingresa el usuario, como había que retornar un json del resultado de los valores, lo que hice fue retornar ese esquema json en el controlador. Mis clases para la lógica de la secuencia de lucas son:

`LucasApplication.java`:
```java
package com.edu.escuelaing.lucassecuencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LucasApplication {

  public static void main(String[] args) {
    SpringApplication.run(LucasApplication.class, args);
  }
}

```
`LucasController.java`:
```java
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
```

Para el servidor Proxy mis clases son:
``ProxyApplication.java`:
```java
package com.edu.escuelaing.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}

```
`ProxyController`:
```java
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
      return (
        "{\"status\":\"error\",\"result\":\"Error: " +
        e.getMessage() +
        " - Server: " +
        server +
        "\"}"
      );
    }
  }
}

```

### Ejecución Local

Ya de manera local, solo hay una instancia pero vemos que funciona al digitar los valores que queremos retornar en la secuencia:

Para esto hay que correr con los comandos `mvn clean compile package` y `mvn spring-boot:run` tanto el proxy como el lucassecuencia:

Ya que están corriendo los servicios, hay que tener en cuenta que establecí el puerto 8080 para el proxy y 8081 para el de lucassecuencia.

Finalmente la implementación local quedó así:

<img width="1364" height="348" alt="image" src="https://github.com/user-attachments/assets/38cc2e00-07da-4eb8-8590-79b3e15d180f" />

### Despliegue en AWS

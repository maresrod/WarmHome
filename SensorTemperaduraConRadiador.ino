#include <dummy.h>
#include <ArduinoJson.h>
#include <TimeLib.h>
#include "WiFi.h"
#include "AsyncUDP.h"

#include <DHT.h>

#define DHTPIN 4     // Pin donde está conectado el sensor

#define DHTTYPE DHT11   // Se usa el DHT 11

const char * ssid = "Grupo6xD";
const char * password = "12345678";

AsyncUDP udp;

StaticJsonDocument<200> jsonBuffer; //tamaño maximo de los datos

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(115200);
  Serial.println("Iniciando...");
  dht.begin();
  
  pinMode(5, OUTPUT);

  setTime (9, 15, 0, 7, 10, 2018); //hora minuto segundo dia mes año
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WiFi Failed");
    while (1) {
      delay(1000);
    }
  } if (udp.listen(1234)) {
    Serial.print("UDP Listening on IP: ");
    Serial.println(WiFi.localIP());
    udp.onPacket([](AsyncUDPPacket packet) {
      Serial.write(packet.data(), packet.length());
      Serial.println();
    });
  }

}
long tiempoUltimaLectura = 0;; //Para guardar el tiempo de la última lectura

void loop() {
  char texto[150];
   //temperatura
  if (millis() - tiempoUltimaLectura > 2000)
  {
    float h = dht.readHumidity(); //Leemos la Humedad
    double t = dht.readTemperature(); //Leemos la temperatura en grados Celsius
    float f = dht.readTemperature(true); //Leemos la temperatura en grados Fahrenheit
    //--------Enviamos las lecturas por el puerto serial-------------
    Serial.print("Humedad ");
    Serial.print(h);
    Serial.print(" %t");
    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.print(" *C ");
    Serial.print(f);
    Serial.println(" *F");
    tiempoUltimaLectura = millis();
    jsonBuffer["temperatura"] = t ; //actualizamos el tiempo de la última lectura
    jsonBuffer["humedad"] = h;
    
    if (t < 22) {
      digitalWrite(5, HIGH);
      delay(100);
    }
    if (t > 25) {
      digitalWrite(5, LOW);
      delay(100);
    }
    serializeJson(jsonBuffer, texto);
    udp.broadcastTo(texto, 1234);
  }
}

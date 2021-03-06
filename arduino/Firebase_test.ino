#include <ESP8266WiFi.h>
#include <ESP8266Firebase.h>
#include <DHT.h>

#define WIFI_ID "iPhone"
#define WIFI_PW "00000000"
#define FIREBASE_ID "SmartFarmer"
#define FIREBASE_PW "yiav0GXW3S5zmphWJn1RBZ4ZPcDkKMCVWXORAvJz"

int t = 20;
int h = 20;
int cdsValue = 20;
int m = 20;

Firebase firebase(FIREBASE_ID);

void setup() {
  Serial.begin(115200);
  Wifi_connect();
}

void loop() {
  firebase.setFloat("cage1/humidity", h);
  firebase.setFloat("cage1/light", cdsValue);
  firebase.setFloat("cage1/temp", t);
  firebase.setFloat("cage1/water", m);
  delay(600000);
}

void Wifi_connect(){
  Serial.println("--------------");
  Serial.println(WIFI_ID);
  WiFi.begin(WIFI_ID, WIFI_PW);
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("WiFi connected");
  Serial.print("MY IP address is : ");
  Serial.println(WiFi.localIP());
}

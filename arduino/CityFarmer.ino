/*아두이노 시작
 *
 * 1. 와이파이 연결
 * 2. 온도, 습도, 토양 수분, 조도 측정
 * 3. 데이터베이스에서 어플로 지정한 온도, 습도, 토양 수분, 조도 값 받아오기
 * 4. 데이터베이스에 전송(처음 값 보내고 1시간 뒤 다시 보냄)
 * 5. if문을 활용하여 온도, 습도, 토양 수분, 조도 조정 
 * 6. 아두이노 딥슬립모드 돌입(10분)
 */
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <DHT.h>

#define DHTPIN D2
#define DHTTYPE DHT22
#define light_relay D3
#define moisture_relay D4
#define pen_power D5
#define led_power D6
#define moisture_power D7
#define host "192.168.4.59"

DHT dht(DHTPIN, DHTTYPE);

const char* ssid = "iPhone";
const char* password = "00000000";

int h = 0;
int t = 0;
int cdsValue = 0;
String cage = "case1";
float moisture_percentage = 0;
int temp_goal = 0;
int temp_goal_min = 0;
int temp_goal_max = 0;
int moisture_goal = 0;
int moisture_goal_min = 0;
int count = 6;

void setup() {
  Serial.begin(115200);
  Wifi_connect();
  dht.begin();
  
  pinMode(A0, INPUT);    //온도, 습도, 조도, 토양 수분 값 받아오는 핀
  pinMode(light_relay, OUTPUT);   //조도 릴레이
  pinMode(moisture_relay, OUTPUT);   //토양 수분 릴레이
  pinMode(pen_power, OUTPUT);   //쿨링펜 작동
  pinMode(led_power, OUTPUT);   //LED 작동
  pinMode(moisture_power, OUTPUT);   //워터펌프 작동
}

void loop() {
  count++;
  //dht 작동
  temp_value_read();
  Serial.print("습도 : ");
  Serial.print(h);
  Serial.println("%");
  Serial.print("온도 : ");
  Serial.print(t);
  Serial.println("C");
  //dht 끝
  //조도센서 작동
  light_value_read();
  Serial.print("조도 : ");
  Serial.println(cdsValue);
  //조도센서 끝
  //토양 수분 센서 작동
  moisture_value_read();
  Serial.print("토양 수분 : ");
  Serial.print(moisture_percentage);
  Serial.println("%");
  //토양 수분 센서 끝
  
  //데이터베이스에 데이터 보내기 시작
  if(count >= 6) {
    WiFiClient client;
    HTTPClient http;

    if((WiFi.status() == WL_CONNECTED)) {
      Serial.print("[HTTP] 서버 연결을 시도합니다...");
      http.begin(client, "http://" host "/insert.php");
      http.addHeader("Content-Type", "application/x-www-form-urlencoded");

      Serial.print("[HTTP] 수집한 값의 POST 요청을 시도합니다...");
      String POSTBODY = String("case=case1");
      POSTBODY.concat(String("&temp="));
      POSTBODY.concat(t);
      POSTBODY.concat(String("&moisture="));
      POSTBODY.concat(h);
      POSTBODY.concat(String("&illuminance="));
      POSTBODY.concat(cdsValue);
      POSTBODY.concat(String("&water="));
      POSTBODY.concat(moisture_percentage);
      int httpCode = http.POST(POSTBODY);

      if(httpCode > 0) {
        Serial.printf("[HTTP] 응답 Code : %d\n", httpCode);
        if(httpCode == HTTP_CODE_OK) {
          const String& payload = http.getString();
          Serial.print("서버로부터 수신된 응답 : ");
          Serial.print(payload);
          Serial.print("");
          count = 0;
        }
      } else {
        Serial.printf("[HTTP] POST 요청이 실패했습니다. 오류 : %s\n", http.errorToString(httpCode).c_str());
      }
    http.end();
    }
  }
  
  //데이터베이스 전송 끝
  /*
   * 온도, 습도, 조도, 토양 수분 값 받아오기
   */
  temp_goal_min = temp_goal - 5;
  temp_goal_max = temp_goal + 5;
  moisture_goal_min = moisture_goal - 5;
  
  while( t <= temp_goal_max || t <= temp_goal_min ){
    while( moisture_percentage <= moisture_goal_min ){
      moisture_value_read();
      if(moisture_percentage <= moisture_goal_min){
        digitalWrite(moisture_power, HIGH);
      }else{
        digitalWrite(moisture_power, LOW);
        break;
      }
      delay(10000);
    }
    temp_value_read();
    if(t >= temp_goal_max){
      digitalWrite(pen_power, HIGH);
    }else if(t<= temp_goal_min){
      digitalWrite(led_power, HIGH);
    }else{
      digitalWrite(pen_power, LOW);
      digitalWrite(led_power, LOW);
    }
  }
  ESP.deepSleep(600e6);
}

void temp_value_read(){
  h = dht.readHumidity();
  t = dht.readTemperature();
  if (isnan(h) || isnan(t)){
    Serial.print("Error");
    return;
  }
}

void light_value_read(){
  digitalWrite(light_relay, HIGH);
  cdsValue = analogRead(A0);
  digitalWrite(light_relay, LOW);
}

void moisture_value_read(){
  digitalWrite(moisture_relay, HIGH);
  moisture_percentage = ( 100.00 - ( ( analogRead(A0) / 1023.00 ) * 100.00 ) );
  digitalWrite(moisture_relay, LOW);
}

void Wifi_connect(){
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("WiFi 연결");
}

/* 아두이노 시작
 *
 * 1. 와이파이 연결
 * 2. 온도, 습도, 토양 수분, 조도 측정
 * 3. 데이터베이스에 전송(처음 값 보내고 1시간 뒤 다시 보냄)
 * 4. 데이터베이스에서 어플로 지정한 온도, 습도, 토양 수분, 조도 값 받아오기
 * 5. if문을 활용하여 온도, 습도, 토양 수분, 조도 조정 
 * 6. 아두이노 딥슬립모드 돌입(10분)
 */
/* 핀 지정
 *  
 * D2 -> dht 센서
 * D3 -> 조도 센서 릴레이 핀
 * D4 -> 토양 수분 릴레이 핀
 * D5 -> 모터 펜 릴레이 핀
 * D6 -> LED 전원 릴레이 핀
 * D7 -> 워터 펌프 드라이버 B-1A 핀
 * D8 -> 워터 펌프 드라이버 B-2A 핀
 * D0 -> RES
 */
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <Arduino_JSON.h>
#include <DHT.h>

#define DHTPIN D2
#define DHTTYPE DHT22
#define light_relay D3
#define moisture_relay D4
#define pen_power D5
#define led_power D6
#define moisture_power_a D7
#define moisture_power_b D8


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
unsigned long lastTime = 0;
unsigned long timerDelay = 10000;
String temp_goal_str = "";
String moisture_goal_str = "";

String jsonBuffer;

WiFiClient client;
HTTPClient http;

void setup() {
  Serial.begin(115200);
  Wifi_connect();
  dht.begin();
  
  pinMode(A0, INPUT);    //온도, 습도, 조도, 토양 수분 값 받아오는 핀
  pinMode(light_relay, OUTPUT);   //조도 릴레이
  pinMode(moisture_relay, OUTPUT);   //토양 수분 릴레이
  pinMode(pen_power, OUTPUT);   //쿨링펜 작동
  pinMode(led_power, OUTPUT);   //LED 작동
  pinMode(moisture_power_a, OUTPUT);   //워터펌프 작동
  pinMode(moisture_power_b, OUTPUT);   //워터펌프 방향
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
    if((WiFi.status() == WL_CONNECTED)) {
      Serial.print("[HTTP] 서버 연결을 시도합니다...");
      http.begin(client, "http://172.20.10.3/insert.php");
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
  //온도, 토양 수분 값 받아오기 시작
  if((millis() - lastTime) > timerDelay) {
    if(WiFi.status() == WL_CONNECTED) {
      String serverPath = "http://172.20.10.3/jsongoal1.php";
      http.begin(client, serverPath);
      int httpCode = http.GET();
      jsonBuffer = http.getString();
      JSONVar myObject = JSON.parse(jsonBuffer);
      if(JSON.typeof(myObject) == "undefined") {
        Serial.println("파싱하지 못했습니다.");
        return;
      }
      http.end();

      int start_point = jsonBuffer.indexOf("temp\":\"");
      int end_point1 = jsonBuffer.indexOf("\",\"water");
      int end_point2 = jsonBuffer.indexOf("\"}]}");
      temp_goal_str = jsonBuffer.substring(start_point+7, end_point1);
      moisture_goal_str = jsonBuffer.substring(start_point+20, end_point2);
      Serial.println("목표 온도 : " + temp_goal_str);
      Serial.println("목표 수분 : " + moisture_goal_str);
    } else {
      Serial.println("WiFi에 연결되어 있지 않습니다.");
    }
    lastTime = millis();
  }

  temp_goal = temp_goal_str.toInt();
  moisture_goal = moisture_goal_str.toInt();
  //온도, 토양 수분 값 받아오기 끝
  //목표 온도, 토양 수분 맞추기 시작
  temp_goal_min = temp_goal - 5;
  temp_goal_max = temp_goal + 5;
  moisture_goal_min = moisture_goal - 50;
  
  while( t <= temp_goal_max || t <= temp_goal_min ){
    while( moisture_percentage <= moisture_goal_min ){
      moisture_value_read();
      if(moisture_percentage <= moisture_goal_min){
        digitalWrite(moisture_power_a, HIGH);
        digitalWrite(moisture_power_b, LOW);
      }else{
        digitalWrite(moisture_power_a, LOW);
        digitalWrite(moisture_power_b, LOW);
        break;
      }
      delay(5000);
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
  //목표 온도, 토양 수분 맞추기 끝
  ESP.deepSleep(600e6);   //아두이노 딥슬립 모드 돌입
}

void temp_value_read(){   //온도, 습도 받아오는 함수
  h = dht.readHumidity();
  t = dht.readTemperature();
  if (isnan(h) || isnan(t)){
    Serial.print("Error");
    return;
  }
}

void light_value_read(){    //조도 받아오는 함수
  digitalWrite(light_relay, HIGH);
  cdsValue = analogRead(A0);
  digitalWrite(light_relay, LOW);
}

void moisture_value_read(){   //토양 수분 받아오는 함수
  digitalWrite(moisture_relay, HIGH);
  moisture_percentage = ( 100.00 - ( ( analogRead(A0) / 1023.00 ) * 100.00 ) );
  digitalWrite(moisture_relay, LOW);
}

void Wifi_connect(){    //와이파이 연결하는 함수
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("WiFi 연결");
}

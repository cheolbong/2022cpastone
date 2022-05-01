/*아두이노 시작
 *
 * 1. 와이파이 연결
 * 2. 온도, 습도, 토양 수분, 조도 측정
 * 3. 데이터베이스에서 어플로 지정한 온도, 습도, 토양 수분, 조도 값 받아오기
 * 4. 데이터베이스에 전송
 * 5. if문을 활용하여 온도, 습도, 토양 수분, 조도 조정 
 * 6. 아두이노 딥슬립모드 돌입(30분)
 */
#include <ESP8266WiFi.h>
#include <DHT.h>

#define DHTPIN D2
#define DHTTYPE DHT11
#define light_relay D3
#define moisture_relay D4
#define pen_power D5
#define led_power D6
#define moisture_power D7

DHT dht(DHTPIN, DHTTYPE);

const char* ssid = "iPhone";
const char* password = "00000000";

int h;
int t;
int cdsValue;
float moisture_percentage;
int temp_goal;
int temp_goal_min;
int temp_goal_max;
int moisture_goal;
int moisture_goal_min;

void setup() {
  Serial.begin(115200);
  Wifi_connect();
  
  pinMode(A0, INPUT);    //온도, 습도, 조도, 토양 수분 값 받아오는 핀
  pinMode(temp_relay, OUTPUT);   //온도, 습도 릴레이
  pinMode(light_relay, OUTPUT);   //조도 릴레이
  pinMode(moisture_relay, OUTPUT);   //토양 수분 릴레이
  pinMode(pen_power, OUTPUT);   //쿨링펜 작동
  pinMode(led_power, OUTPUT);   //LED 작동
  pinMode(moisture_power, OUTPUT);   //워터펌프 작동
  
  dht.begin();
}

void loop() {
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
  /*
   * 온도, 습도, 조도, 토양 수분 값 받아오기
   */
  /*
   * 데이터 베이스 전송
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
  ESP.deepSleep(3600e6);
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
  Serial.println("--------------");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("WiFi connected");
  Serial.print("MY IP address is : ");
  Serial.println(WiFi.localIP());
}

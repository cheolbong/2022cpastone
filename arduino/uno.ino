int pen = 0;
int led = 0; 

void setup() {
  Serial.begin(115200);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, INPUT);
  pinMode(7, INPUT);
}

void loop() {
  pen = digitalRead(6);
  led = digitalRead(7);
  if(pen == 1) {
    digitalWrite(2, HIGH);
    digitalWrite(3, LOW);
  } else {
    digitalWrite(2, LOW);
    digitalWrite(3, LOW);
  }
  if(led == 1) {
    digitalWrite(4, HIGH);
    digitalWrite(5, LOW);
  } else {
    digitalWrite(4, LOW);
    digitalWrite(5, LOW);
  }
  delay(10000);
}

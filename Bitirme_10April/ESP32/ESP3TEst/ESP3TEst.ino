#include "BluetoothSerial.h"
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

#include <Adafruit_BMP085.h>
#include <Wire.h>

#include "MAX30100_PulseOximeter.h"
#define REPORTING_PERIOD_MS 1000

#include <Adafruit_ADXL345_U.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>


BluetoothSerial SerialBT;

Adafruit_BMP085 bmp;
PulseOximeter pox;
Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified(12345);

char incoming = 'm';
char incoming2 = 'm';
int outsideTemp = 30 ;
int airPressure = 101160;
int heartRate = 65;
int Spo2 = 95;
int bodyTemp = 35;
int humidity = 40;

uint32_t tsLastReport = 0;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");

  if (!bmp.begin()) {
    Serial.println("Could not find a valid BMP085/BMP180 sensor, check wiring!");
    while (1) {}
  }

  if (!pox.begin()) {
    Serial.println("FAILED");
    for (;;);
  }

  if (!accel.begin())
  {
    Serial.println("Ooops, no ADXL345 detected ... Check your wiring!");
    while (1);
  }
}

void loop() {

  // BMP 180 
  //Outside Temperature
  outsideTemp = int(bmp.readTemperature()); //24
  String newOutsideTemp = addZeros(outsideTemp);
  //Air Pressure
  airPressure = int(bmp.readSealevelPressure());// 101160
  String newAirPressure = addZeros(airPressure); 

  // MAX 30100
  pox.update();
  //HeartRate
  heartRate = int(pox.getHeartRate()); //65
  //Serial.print("Heart Rate: " + heartRate);
  if (heartRate < 55 ) {
    heartRate = random(55,70);
  }
  String newHeartRate = addZeros(heartRate);
  //SPO2
  Spo2 = int(pox.getSpO2()); //95
  //Serial.print("Spo2: " + Spo2);
  if (Spo2 < 90) {
    Spo2 = random(90, 99);
  }
  String newSpo2 = addZeros(Spo2);

  bodyTemp = random(34, 42);
  String newBodyTemp = addZeros(bodyTemp);
  humidity = random(20, 75);
  String newHumidity = addZeros(humidity);
  
  String sent =  String(newOutsideTemp) + String(newAirPressure) + String(newHeartRate) + String(newSpo2) + String(newBodyTemp) + String(newHumidity); //10
  
  Serial.println("Sent : " + sent);
  Serial.println();

  if (SerialBT.available()) {
    incoming = SerialBT.read();
    //Serial.println(incoming);
    if (incoming == 'm') {
      SerialBT.println(sent);
    }
    //Serial.write(SerialBT.read());
    delay(100);
  }
}


String addZeros (int result){
  int tempLen = resultLength(result);
  String newResult = String(result);
  for (int i = tempLen ; i<10 ; i++){
    newResult += "_";
  }
  return newResult;
}

int resultLength (int result){
  int resLen = (String(result).length());
  return resLen;
}

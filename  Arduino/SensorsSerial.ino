#include <SpacebrewYun.h>
#include <Wire.h> //The I2C library

//this sketch outputs in the following format:
// accelerometer  forceSensor  rpm  GPS coordinate
// the GPS will be added to the databases table by the users app installed on their phone

float accelResult[3];
float scale = 230; //found from the z axis since we know that it will have 1G of force on it
int fsrPin = 0; // the FSR and 10K pulldown are connected to a0
int fsrReading; // the analog reading from the FSR resistor divider
int fsrVoltage; // the analog reading converted to voltage
unsigned long fsrResistance;
unsigned long fsrConductance;
long fsrForce; // Finally, the resistance converted to force

//Function for writing a byte to an address on an I2C device
void writeTo(byte device, byte toAddress, byte val) {
  Wire.beginTransmission(device);
  Wire.write(toAddress);
  Wire.write(val);
  Wire.endTransmission();
}

//Function for reading num bytes from addresses on an I2C device
void readFrom(byte device, byte fromAddress, int num, byte result[]) {
  Wire.beginTransmission(device);
  Wire.write(fromAddress);
  Wire.endTransmission();
  Wire.requestFrom((int)device, num);
  int i = 0;
  while (Wire.available()) {
    result[i] = Wire.read();
    i++;
  }
}


//Function for reading the accelerometers
void getAccelerometerReadings(float accelResult[]) {
  byte buffer[6];
  readFrom(0x53, 0x32, 6, buffer);
  accelResult[0] = abs(((((int)buffer[1]) << 8 ) | buffer[0]) / scale); //Yes, byte order different from gyros'
  accelResult[1] = abs(((((int)buffer[3]) << 8 ) | buffer[2]) / scale);
  accelResult[2] = abs(((((int)buffer[5]) << 8 ) | buffer[4]) / scale);
}

//Function for reading the force resistor and converting it into newtons 
void getForceReadings(int fsrReadingInput) {
  fsrReading = fsrReadingInput;
  // analog voltage reading ranges from about 0 to 1023 which maps to 0V to 5V (= 5000mV)
  fsrVoltage = map(fsrReading, 0, 1023, 0, 5000);

  //FSR = ((Vcc - V) * R) / V
  fsrResistance = 5000 - fsrVoltage;
  fsrResistance *= 10000;
  fsrResistance /= fsrVoltage;
  fsrConductance = 1000000;
  fsrConductance /= fsrResistance;


  //approximate the force
  if (fsrConductance <= 1000) {
    fsrForce = fsrConductance / 80;

  } else {
    fsrForce = fsrConductance - 1000;
    fsrForce /= 30;
    Serial.println(fsrForce);
  }
}


void setup() {
  Wire.begin();            //Open I2C communications as master
  Serial.begin(9600);    //Open serial communications to the PC to see what's happening

  writeTo(0x53, 0x31, 0x0B); //Set accelerometer to 11bit, +/-4g
  writeTo(0x53, 0x2D, 0x08); //Set accelerometer to measure mode

}

void loop() {

  //output the greatest accelerometer reading from either the x or y direction in G's
  getAccelerometerReadings(accelResult);
  if (accelResult[0] > accelResult[1]) {
    Serial.print(accelResult[0]);
    Serial.print("\t"); //create a tabe as a delimiter (easily read by mySQL)
    //Serial.println('G');
  } else {
    Serial.print(accelResult[1]);
    Serial.print("\t");
    //Serial.println('G');
  }

  getForceReadings(analogRead(fsrPin));
  Serial.println(fsrForce);

}



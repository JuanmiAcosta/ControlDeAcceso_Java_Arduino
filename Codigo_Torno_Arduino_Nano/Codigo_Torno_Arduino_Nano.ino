#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>

// Definir los pines para el LED RGB

#define R 2  // Pin para el color rojo
#define G 3  // Pin para el color verde
#define B 4  // Pin para el color azul

// Definimos el torno

#define T 6

// Definimos los pines del lector

#define RST 9
#define SS 10

// Variables de prueba

const int NUM_CLIENTES = 2;  //NÚMERO DE PRUEBA

MFRC522 mfrc522(SS, RST);  // Creamos una instancia del lector

byte LecturaUID[4];
size_t arrayLength = 4;  //sizeof(LecturaUID) / sizeof(byte);
char charArray[9];       //[arrayLength * 2 + 1]; // +1 para el carácter nulo al final

//--------------------------------------------------------
byte Tarjeta[4] = { 0xAA, 0xBC, 0xFB, 0x3F };
byte Llavero[4] = { 0xE9, 0x8E, 0x14, 0xB4 };
//--------------------------------------------------------


// Función para encender el LED con un color específico
void encenderColor(int rojo, int verde, int azul) {
  analogWrite(R, rojo);
  analogWrite(G, verde);
  analogWrite(B, azul);
}

void modoEspera() {
  encenderColor(0, 0, 255);
}

void abrirTorno() {
  encenderColor(0, 255, 0);
  digitalWrite(T, LOW);
  delay(5000);  // Esperar 1 segundo
  digitalWrite(T, HIGH);
  modoEspera();
}

void denegarEntrada() {
  encenderColor(255, 0, 0);
  delay(5000);  // Esperar 1 segundo
  modoEspera();
}

void bytesToCharArray(const byte* byteArray, size_t length, char* charArray) {
  for (size_t i = 0; i < length; i++) {
    sprintf(charArray + i * 2, "%02X", byteArray[i]);
  }
}

void stringToByteArray(const String& inputString, byte* byteArray, size_t length) {
  for (size_t i = 0; i < length; i++) {
    String hexByte = inputString.substring(i * 2, i * 2 + 2);
    byteArray[i] = strtol(hexByte.c_str(), NULL, 16);
  }
}



void setup() {

  Serial.begin(9600);

  // Configurar los pines como salidas
  pinMode(R, OUTPUT);
  pinMode(G, OUTPUT);
  pinMode(B, OUTPUT);

  pinMode(T, OUTPUT);
  digitalWrite(T, HIGH);

  //Al torno se le suministra tensión normalmente, es a la hora de abrir cuando se establece el "LOW"

  modoEspera();

  while (!Serial)
    ;

  //Serial.print("Creada instancia del lector e iniciada\n");

  SPI.begin();
  mfrc522.PCD_Init();
}

void loop() {

  if (Serial.available() > 0) {
    String input = Serial.readStringUntil('\n');
    if (input == "j_active") {  
      abrirTorno();

    } else if (input == "j_inactive") {  
      denegarEntrada();

    /*} else if (input == "j_unique") { //El UID de esa tarjeta ya es usado por alguien
      encenderColor(255, 0, 255); 
      delay(4000); 
      modoEspera();*/

    } else if (input == "j_start") {  
      int counter = 0;

      while (counter < 10) {
        if (mfrc522.PICC_IsNewCardPresent()) {
          if (mfrc522.PICC_ReadCardSerial()) {

            encenderColor(255, 255, 255);  // Blanco para indicar lectura
            delay(2000);
            
            for (byte i = 0; i < mfrc522.uid.size; i++) {
              LecturaUID[i] = mfrc522.uid.uidByte[i];
            }

            bytesToCharArray(LecturaUID, arrayLength, charArray);

            // Crear un objeto String a partir del array de caracteres
            String resultString(charArray);

            Serial.println(resultString + "A\n"); //TERMINA EN A DE ASIGNAR

            mfrc522.PICC_HaltA();

            modoEspera();

            counter = 11;
          }
        }

        delay(1000);
        counter++;

      }
    }
  }


  if (!mfrc522.PICC_IsNewCardPresent()) {
    return;
  } else {
    //Serial.print("Tarjeta detectada\n");
  }

  if (!mfrc522.PICC_ReadCardSerial()) {
    return;
  } else {
    //Serial.print("Lectura exitosa\n");
  }

  for (byte i = 0; i < mfrc522.uid.size; i++) {
    LecturaUID[i] = mfrc522.uid.uidByte[i];
  }

  bytesToCharArray(LecturaUID, arrayLength, charArray);

  // Crear un objeto String a partir del array de caracteres
  String resultString(charArray);

  Serial.println(resultString + "C\n"); //TERMINA EN C DE COMPROBAR

  mfrc522.PICC_HaltA();
}




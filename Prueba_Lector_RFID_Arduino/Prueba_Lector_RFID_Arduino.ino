#include <Arduino.h>
#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN 9
#define SS_PIN 10

#define RED 8
#define GREEN 7
#define BLUE 6

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Creamos una instancia del lector

const int NUM_CLIENTES = 2;
const int vueltas = 10;

byte LecturaUID[4];
byte Tarjeta[4] = { 0xAA, 0xBC, 0xFB, 0x3F };
byte Llavero[4] = { 0xE9, 0x8E, 0x14, 0xB4 };

struct Cliente {
  String nombre;
  byte UID[4];
  bool pagado;
};

// Definición de algunos clientes (con el formato de UID que mencionaste)
Cliente clientes[] = {
  { "Juan Miguel", { 0xAA, 0xBC, 0xFB, 0x3F }, true },
  { "Alvaro", { 0xE9, 0x8E, 0x14, 0xB4 }, false }
};

bool comparaUID(byte lectura[], byte usuario[]) {
  for (byte i = 0; i < 4; i++) {
    /*
    Serial.print("Comparando byte ");
    Serial.print(i);
    Serial.print(": lectura[i] = 0x");
    Serial.print(lectura[i], HEX);
    Serial.print(", usuario[i] = 0x");
    Serial.println(usuario[i], HEX);
    */

    if (lectura[i] != usuario[i]) {
      Serial.println("No coinciden.");
      return false;
    } else {
      Serial.println("Coinciden.");
    }
  }
  //Serial.println("Comparacion completa: Coinciden todos los bytes.");
  return true;
}

// Función para verificar si el UID de lectura pertenece a algún cliente
Cliente verificaCliente(byte lectura[], Cliente clientes[], int numClientes) {
  Cliente clienteNoEncontrado;  // Cliente por defecto si no se encuentra ninguno
  clienteNoEncontrado.nombre = "N/A";
  clienteNoEncontrado.pagado = false;

  for (int i = 0; i < numClientes; i++) {
    if (comparaUID(lectura, clientes[i].UID)) {
      return clientes[i];
    }
  }
  return clienteNoEncontrado;
}

// Función para verificar si un cliente ha pagado
bool clienteHaPagado(Cliente clienteEncontrado) {
  return clienteEncontrado.pagado;
}

void setup() {
  Serial.begin(9600);
  pinMode(RED, OUTPUT);
  pinMode(GREEN, OUTPUT);
  pinMode(BLUE, OUTPUT);

  while (!Serial)
    ;

  SPI.begin();
  mfrc522.PCD_Init();
}

void loop() {

  if (Serial.available() > 0){
    char input = Serial.read();
    if (input == 'r'){
      digitalWrite(BLUE,HIGH);//SIMULAR QUE VAMOS A LEER UNA TARJETA
      delay(5000);
      Serial.println("UID GRABADO a DNI ");
      digitalWrite(BLUE,LOW);
    }
  }

  if (!mfrc522.PICC_IsNewCardPresent()) {
    return;
  } else {
    Serial.print("Tarjeta detectada\n");
  }

  if (!mfrc522.PICC_ReadCardSerial()) {
    return;
  } else {
    Serial.print("Lectura exitosa\n");
  }

  Serial.print("CARD UID: ");

  for (byte i = 0; i < mfrc522.uid.size; i++) {
    if (mfrc522.uid.uidByte[i] < 0 < 10) {
      Serial.print(" 0x");
    } else {
      Serial.print(" ");
    }
    Serial.print(mfrc522.uid.uidByte[i], HEX);
    LecturaUID[i] = mfrc522.uid.uidByte[i];
  }

  Serial.print("\n");

  Cliente clienteEncontrado = verificaCliente(LecturaUID, clientes, NUM_CLIENTES);

  if (clienteEncontrado.nombre != "N/A") {
    Serial.print("Cliente encontrado : " + clienteEncontrado.nombre + "\n");
    if (clienteHaPagado(clienteEncontrado)) {
      Serial.print("Mensualidad activa \n");
      digitalWrite(GREEN, HIGH);
      delay(3000);
      digitalWrite(GREEN, LOW);
      delay(1000);
    } else {
      Serial.print("Mensualidad inactiva \n");
      digitalWrite(RED, HIGH);
      delay(3000);
      digitalWrite(RED, LOW);
      delay(1000);
    }
  } else {
    Serial.print("Cliente no encontrado\n");
  }

  Serial.print("\n");

  mfrc522.PICC_HaltA();
}

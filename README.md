# JavaApp_Comm_Arduino 📓
  
En esta práctica se probará la comunicación por puerto serie entre una aplicación java, y el microcontrolador Arduino. Esta es la última parte para poder finalizar el desarrollo de una aplicación de gestión de gimnasios. Esta comunicación es esencial para llevar a cabo un control de acceso en los mismos.

![Imagen representativa](https://github.com/JuanmiAcosta/JavaApp_Comm_Arduino/blob/main/JavaApp.png?raw=true)

En primer lugar se desarrollará el script que ejecutará en bucle el microcontrolador ( en este caso Arduino UNO ), y se cableará en primera instancia y para hacer pruebas todos los módulos necesarios a una protoboard. Este programa estará en constante comunicación con la aplicación Java que será la encargada de recibir el UID de las tarjetas y corroborar en la base de datos si el cliente tiene una mensualidad, y si esta está activa o no.

![Imagen representativa](https://github.com/JuanmiAcosta/JavaApp_Comm_Arduino/blob/main/frag_ard.png?raw=true)

La aplicación Java tendrá la única parte "activa" de activar en el microcontrolador la función de asignar el UID de la nueva tarjeta a un cliente.🤔

## Tecnologías utilizadas 🛠️ / Lenguajes de programación 👀

* c++
* Java
* Arduino IDE
* Java NetBeans

## Cómo empezar 🫡

1. Clona el repositorio
2. Instala las dependencias
3. Ejecuta el proyecto

* Además instala Arduino IDE y pásale el programa al microcontrolador, el cableado debería ser algo así además de un posible servomotor que abra una puerta, leds indicativos, una pantalla LCD con información...

![Imagen representativa](https://github.com/JuanmiAcosta/JavaApp_Comm_Arduino/blob/main/conexionado.jpg?raw=true)







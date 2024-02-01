# Control de acceso con Arduino Nano y Java 📓

## Vídeo explicativo del sistema 🥶

[![Video Demostrativo](https://img.youtube.com/vi/Pq_Xorr02ok/0.jpg)](https://youtu.be/Pq_Xorr02ok)
  
Esta práctica consiste en tener un control de acceso basado en el microcontrolador Arduino Nano y un lector RFID (chip RC522) que se pueda comunicar con una aplicación Java que conlleve una Base de Datos, en este caso relacional.

El programa se divide en dos bloque claros, en la rutina (void loop) del microcontrolador que se encarga de escuchar los comandos de la aplicación ( "j_active" -> abrir el torno por ejemplo ), y en la parte de Java que también se encarga de recibir cierta información del microcontrolador, y además realizar ciertas acciones con la Base de Datos.

Para realizar este control de acceso he necesitado dos dependencias:
 - mysql-connector-j-8.0.32
 - PanamaHitek_Arduino-2.8.2

Este diagrama de actividad resume de manera simple las acciones de los dos programas, por un lado los mensajes comenzados en "j" son los que manda Java por puerto serie, y los que empiezan por "c" son los mandados por el microcontrolador:

<p align="center">
  <img src="https://github.com/JuanmiAcosta/ControlDeAcceso_Java_Arduino/blob/main/Diagrama%20de%20actividad%20(Rutina%20torno).png?raw=true" alt="Imagen representativa">
</p>

Además en la siguiente imagen se especifican los pines usados en el microcontrolador Arduino Nano, y un esquemático de las conexiones:

![Imagen representativa](https://github.com/JuanmiAcosta/ControlDeAcceso_Java_Arduino/blob/main/Pines%20Arduino%20Nano.png?raw=true)
<br>
![Imagen representativa](https://github.com/JuanmiAcosta/ControlDeAcceso_Java_Arduino/blob/main/circuito.png?raw=true)

Por último recomendar la creación de una PCB para un acabado más profesional, este es un ejemplo pero se puede realizar de diversas maneras:

![Imagen representativa](https://github.com/JuanmiAcosta/ControlDeAcceso_Java_Arduino/blob/main/pcb.png?raw=true)

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
* Asegúrate de establecer los mismos Baudios (9600) y el mismo puerto (COM3 por ejemplo) tanto en Java como en Arduino.





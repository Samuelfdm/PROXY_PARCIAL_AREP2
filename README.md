* # Math Services - Proxy

## Descripción del Proyecto
Este proyecto implementa un servicio distribuido para investigar los factores de números enteros y los números primos.

![img.png](src%2Fmain%2Fresources%2Fimg%2Fimg.png)

El sistema está compuesto por:
1. **Proxy Service**: Se encarga de distribuir las solicitudes de búsqueda entre los servidores mediante un algoritmo de balanceo de carga (Round-Robin).
2. **Math Service**: Servicio backend que ejecuta las operaciones y retorna los resultados.

## Arquitectura del Proyecto
El sistema sigue una arquitectura basada en microservicios con balanceo de carga, utilizando **tres instancias en AWS EC2**:
- **1 instancia** ejecutando el **Proxy Service**.
- **2 instancias** ejecutando el **Math Service**.

## Requisitos Previos

- Cuenta en **AWS**.
- Instancia EC2 con **Java 17+ y Maven** instalados.
- Puertos abiertos en el **Security Group**:
- **8080** (para el Proxy Service).
- **8081 y 8082** (para Math Services).

## Cómo Correr el Proyecto Localmente
1. Clonar los repositorios desde GitHub:
```bash
git clone https://github.com/Samuelfdm/PROXY_PARCIAL_AREP2.git
cd PROXY_PARCIAL_AREP2

git clone https://github.com/Samuelfdm/MATHSERVICE_PARCIAL_AREP2.git
cd MATHSERVICE_PARCIAL_AREP2
```
2. Compilar y empaquetar los servicios en cada uno:
```bash
mvn clean package
```
3. Iniciar las instancias de **Math Service** en diferentes puertos:
```bash
java -jar target/mathservices-0.0.1-SNAPSHOT.jar --server.port=8081
java -jar target/mathservices-0.0.1-SNAPSHOT.jar --server.port=8082
```
4. Iniciar el **Proxy Service**:
```bash
java -jar target/proxy-0.0.1-SNAPSHOT.jar --server.port=8080
```
5. Probar los servicios accediendo a:
```bash
http://localhost:8080/factors?value=15
http://localhost:8080/primes?value=100
```

![img_1.png](src%2Fmain%2Fresources%2Fimg%2Fimg_1.png)
![img_2.png](src%2Fmain%2Fresources%2Fimg%2Fimg_2.png)
![img_3.png](src%2Fmain%2Fresources%2Fimg%2Fimg_3.png)

## Cómo Desplegar en AWS EC2
- Crear **tres instancias EC2**.
- En cada instancia, instalar Java y Maven:
```bash
Actualizar paquetes
sudo yum update -y

Instalar Git
sudo yum install git -y

Instalar Java 17 (Amazon Corretto)
sudo yum install java-17-amazon-corretto -y

O para instalar Java 21:
sudo yum install java-21-amazon-corretto -y

Verificar la versión instalada
java -version
git --version

Instalar maven
sudo yum install maven -y

Verificar la instalación
mvn -version
cambiar a la version 21 si se requiere
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```
- Clonar los repositorios en cada instancia:
```bash
git clone https://github.com/Samuelfdm/PROXY_PARCIAL_AREP2.git
cd PROXY_PARCIAL_AREP2

primera instancia math
git clone https://github.com/Samuelfdm/MATHSERVICE_PARCIAL_AREP2.git
cd MATHSERVICE_PARCIAL_AREP2

segunda instancia math
git clone https://github.com/Samuelfdm/MATHSERVICE_PARCIAL_AREP2.git
cd MATHSERVICE_PARCIAL_AREP2

```

### 2. Desplegar Math Services en dos instancias
En la primera instancia EC2 (Math Service 1):
```bash
mvn clean package
java -jar target/mathservices-0.0.1-SNAPSHOT.jar
```
En la segunda instancia EC2 (Math Service 2):
```bash
mvn clean package
java -jar target/mathservices-0.0.1-SNAPSHOT.jar
```

### 3. Desplegar el Proxy Service en la tercera instancia
- Modificar el archivo `Proxy.java` para que use las IPs públicas de las instancias de Math Services:
```java
private static final String[] SERVERS = new String[]{
"http://IP_EC2_MATH1:8081/",
"http://IP_EC2_MATH2:8081/"
};
```
- Compilar y ejecutar el Proxy Service:
```bash
mvn clean package
java -jar target/proxy-0.0.1-SNAPSHOT.jar
```

## Funcionamiento



## Autor

- Samuel Felipe Díaz Mamanche

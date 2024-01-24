mvn clean package -DskipTests

# construir la imagen en la ruta que deseemos en este caso . es la raiz
docker build .   

ENTRYPOINT ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]  esto se ejecuta cuando hacemos docker run y no docker build

# para ver la imagenes

docker images 

# para ver los contenedores que estan corriendo
docker ps  

# el primer puerto es el puerto que se expone y el segundo es el de la app,-d es para separar la terminal del contenedor
docker run -d -p 8001:8001 1becc6477fae   

# detener contendor
docker stop nombre_contenedor  

# el -t le da un nombre al contenedor
docker build -t usuario .  

# con -f le indicamos donde esta el docker file
docker build -t usuarios . -f .\msvc-usuarios\Dockerfile  

# Docker proporciona un solo comando que eliminará cualquier recurso (imágenes, contenedores, volúmenes y redes) que estén pendientes (no asociados con un contenedor)
docker system prune

# Para eliminar adicionalmente los contenedores detenidos y todas las imágenes no utilizadas (no solo aquellas pendientes), añada el indicador -a al comando:
docker system prune -a

# detener un contenedor
docker stop 1becc6477fae  

# levantar un contenedor
docker start  1becc6477fae
# levantar un contenedor pero de formar attatch con la terminal
docker start -a  1becc6477fae 
# para adjuntar el contenedor con la terminar y ver logs
docker attatch 1becc6477fae  

# para ver los logs del contenedor levantado
docker logs 1becc6477fae 

# para eliminar un contenedor , no una imagen
docker rm 1becc6477fae 

# eliminar todos los conteneddores que estan dentenidos
docker container prune 

# para eliminar una imagen
docker rmi usuarios  

# elimina todas las imagenes que estan stopped
docker image prune 

# cuando el contenedor pare se va eliminar automaticamente
docker run -d -p 8001:8001 1becc6477fae --rm 

# modo interactivo y entrar a la consola del contenedor
docker run -p 8001:8001 --rm -it 84abc9e3e208 /bin/sh
docker run --rm -it usuarios /bin/sh

# copiar del pc al contenedor
docker cp .\Login.java 4c28b548fa9f/:app/Login.java    

# copiar del contenedor a nuestra maquina
docker cp 4c28b548fa9f/:app/Login.java .\Login2.java  

docker run -p 8001:8001 --rm -d usuarios

# copiar logs del contenedor al pc
docker cp 627c68193308:/app/logs .\logs  

# inspeccionar una imagen
docker image inspect usuarios  

# inspeccionar un contenedor
docker container inspect 627c68193308  

# darle una version a la imagen
docker build -t usuarios:v2 . -f .\msvc-usuarios\Dockerfile 

# darle un nombre al contenedor
docker run -p 8001:8001 --rm -d --name msvc-usuarios  usuarios:v2  


********************* comunicaciones y redes

# crear una red
docker network create spring  

# listar las redes
docker network ls   

# crear un contenedor con la red llamada spring
docker run -p8001:8001 -d --rm --name msvc-usuarios --network spring usuarios  

# descargar mysql
docker pull mysql:8 

# crear contenedor con imagen de mysql
docker run -d -p 3307:3306 --name baseDatosMysql --network spring -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=msvc_usuarios mysql   

-e indica que es una variable de ambiente enviroment

# acceder al bash de un contenedor -it terminar interactivo
docker exec -it baseDatosMysql bash  

# conectar la base de datos en el contenedor
mysql -h localhost -u root -p 

# crear un volumen para que los datos de la base de datos puede persistir si el contenedor se eliminar
docker run -d -p 3307:3306 --name baseDatosMysql --network spring -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=msvc-usuarios -v data-mysql:/var/lib/mysql mysql
# AGREGAR VARIABLE DE ENTORONO AL CONTENEDOR Y CAMBIAR EL PUERTO
docker run -p 8001:8001 -env PORT=8090 -d --rm --name msvc-usuarios --network spring usuarios  

docker run -p 8001:8001 --env-file .\msvc-usuarios\.env -d --rm --name msvc-usuarios --network spring usuarios


***************************
Docker commpose
# levantar contenedores con docker compose
docker-compose up -d
# baja todos los contenedores y los volumenes
docker-compose down -v  

# construir las imagenes y luego crear el contenedor
docker-compose up --build -d 

# crear una copia de una imagen existente
docker tag docker-micros-msvc-curso  


/*****Conectar instancia AWS

# Conectarse a una instancia ecs
ssh -i "spring-cloud-docker.pem" ec2-user@ec2-54-87-202-119.compute-1.amazonaws.com


# copiar archivo a nuestra instancia
scp -i "spring-cloud-docker.pem" docker-compose-cursos.yml ec2-user@ec2-3-91-186-20.compute-1.amazonaws.com:/home/ec2-user


scp -i "spring-cloud-docker.pem" ec2-user@ec2-3-80-103-110.compute-1.amazonaws.com

***************************** Instalar docker


#  Actualizar todos los paquetes de nuestra instancia
sudo yum update -y   

# Instalas extras de aws
sudo amazon-linux-extras install docker

https://www.cyberciti.biz/faq/how-to-install-docker-on-amazon-linux-2/
https://saturncloud.io/blog/how-to-install-dockercompose-on-amazon-ec2-linux-2-and-run-a-9kb-dockercompose-file/

# instalar docker
sudo yum install docker

# Add group membership for the default ec2-user so you can run all docker commands without using the sudo command:
sudo usermod -a -G docker ec2-user
id ec2-user
# Reload a Linux user's group assignments to docker w/o logout
newgrp docker
Need docker-compose too? Try any one of the following command:
# 1. Get pip3
sudo yum install python3-pip

# 2. Then run any one of the following
sudo pip3 install docker-compose # with root access

# OR #

pip3 install --user docker-compose # without root access for security reasons


******************************************
Installing Docker
# Before installing Docker-Compose, we need to install Docker. In your instance, run the following commands:

sudo yum update -y
sudo yum install -y docker
#Start the Docker service:

sudo service docker start
#A#dd the ec2-user to the Docker group so you can execute Docker commands without using sudo:

sudo usermod -a -G docker ec2-user
#Log out and log back in again to pick up the new Docker group permissions.


#Installing Docker-Compose
#To install Docker-Compose, use the following commands:

sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#Next, set the permissions:

sudo chmod +x /usr/local/bin/docker-compose
#Verify the installation:

docker-compose --version

sudo service docker start

sudo docker-compose up -d


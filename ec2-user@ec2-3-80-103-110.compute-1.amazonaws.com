version: "3.9"
services:
  msvc-usuarios:
    container_name: msvc-usuarios
    image: lucho2024/docker-micros-msvc-usuarios
#    build:
#      context: ./
#      dockerfile: ./msvc-usuarios/Dockerfile
    ports:
      - "8001:8001"
    #env_file: ./msvc-usuarios/.env
    environment:
      PORT: 8001
      DB_HOST: baseDatosMysql:3306
      DB_DATABASE: msvc_usuarios
      DB_USERNAME: root
      DB_PASSWORD: root
      CURSOS_URL: msvc-cursos:8002
    networks:
      - spring
    depends_on:
      - baseDatosMysql
    restart: always
  baseDatosMysql:
    container_name: baseDatosMysql
    image: mysql:8
    ports:
      - "3307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: msvc_usuarios
    volumes:
      - data-mysql:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    restart: always
    networks:
      - spring

volumes:
  data-mysql:

networks:
  spring:

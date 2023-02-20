# JavaSpringboot_MySQL_Thymeleaf
Estructura de base de datos MySQL:

create database tareas;
use tareas;
create table Usuario (
	NIF char(9) primary key,
    nombre varchar(30),
    apellidos varchar(50),
    pw varchar(200),
    Activo tinyint
);
CREATE TABLE ROLES (
	id int auto_increment primary key,
	NIF char(9),
	foreign key (nif) references usuario(nif),
	ROL varchar(50) NOT NULL
);
  create table tarea (
	id int primary key auto_increment,
    nombre varchar(200),
    descripcion varchar(1000),
    estado tinyint,
	NIF char(9),
    foreign key (nif) references usuario(nif)
  );

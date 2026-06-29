CREATE DATABASE mundo_nodo 	
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE mundo_nodo;

CREATE USER 'nodo_admin'@'localhost' IDENTIFIED BY 'mundoNodo2026*';

GRANT ALL PRIVILEGES ON mundo_nodo.* TO 'nodo_admin'@'localhost';






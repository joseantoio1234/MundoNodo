 USE mundo_nodo;

 CREATE TABLE usuarios (
     idusuario SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
     email VARCHAR(50) NOT NULL,
     password VARCHAR(100) NOT NULL,
     nombre VARCHAR(20) NOT NULL,
     apellidos VARCHAR(30) NOT NULL,
     nif CHAR(9) NOT NULL,
     telefono CHAR(9),
     direccion VARCHAR(40) NOT NULL,
     codigo_postal CHAR(5) NOT NULL,
     localidad VARCHAR(40) NOT NULL,
     provincia VARCHAR(30) NOT NULL,
     ultimo_acceso DATETIME,
     avatar VARCHAR(30),
    
     PRIMARY KEY (idusuario),
     UNIQUE KEY email_unico (email)
 ) ENGINE=InnoDB;

 CREATE TABLE productos (
     idproducto SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
     idcategoria TINYINT UNSIGNED NOT NULL,
     nombre VARCHAR(100) NOT NULL,
     descripcion MEDIUMTEXT,
     precio DECIMAL(6,2) UNSIGNED NOT NULL,
     marca VARCHAR(40) NOT NULL,
     imagen VARCHAR(30) DEFAULT 'default.jpg',
     PRIMARY KEY (idproducto)
 ) ENGINE=InnoDB;


 CREATE TABLE categorias (
     idcategoria TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
     nombre VARCHAR(40) NOT NULL,
     PRIMARY KEY (idcategoria)
 ) ENGINE=InnoDB;

 CREATE TABLE IF NOT EXISTS pedidos (
     idpedido SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
     idusuario SMALLINT UNSIGNED NOT NULL,
     fecha DATETIME NOT NULL,
     total DECIMAL(8,2) NOT NULL,
     estado VARCHAR(20) DEFAULT 'pendiente',
     PRIMARY KEY (idpedido),
     CONSTRAINT fk_pedidos_usuarios 
     FOREIGN KEY (idusuario) REFERENCES usuarios(idusuario)
     ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE=InnoDB;

 ALTER TABLE productos
 ADD CONSTRAINT fk_productos_categorias
 FOREIGN KEY (idcategoria) REFERENCES categorias(idcategoria)
 ON DELETE CASCADE ON UPDATE CASCADE;

 CREATE TABLE IF NOT EXISTS lineaspedidos (
     idlinea SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
     idpedido SMALLINT UNSIGNED NOT NULL,
     idproducto SMALLINT UNSIGNED NOT NULL,
     cantidad TINYINT UNSIGNED NOT NULL,
     PRIMARY KEY (idlinea)
 ) ENGINE=InnoDB;

 ALTER TABLE lineaspedidos
 ADD CONSTRAINT fk_lineas_pedidos
 FOREIGN KEY (idpedido) REFERENCES pedidos(idpedido)
 ON DELETE CASCADE ON UPDATE CASCADE;

  ALTER TABLE lineaspedidos
  ADD CONSTRAINT fk_lineas_productos
  FOREIGN KEY (idproducto) REFERENCES productos(idproducto)
  ON DELETE RESTRICT ON UPDATE CASCADE;

USE mundo_nodo;

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `idcategoria` tinyint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL,
  `imagen` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT 'default.jpg',
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES 
(1,'Placas Base','default.jpg'),
(2,'Procesadores','default.jpg'),
(3,'Discos Duros','default.jpg'),
(4,'Intel Placas','14259451186728691.jpg'),
(5,'Amd Placas','14259451301832677.jpg'),
(7,'Memoria Ram','10905.jpg'),
(9,'Gráficas','44415-tarjetasgraficas.jpg'),
(11,'Sata','14259455771489321.jpg'),
(13,'Ssd','14259455894536146.jpg'),
(14,'Externos','default.jpg'),
(18,'NVidia','14259458625293893.jpg'),
(19,'Intel Socket 1150','14259444204055460.jpg'),
(20,'Amd Socket AM3','14259449062732937.jpg'),
(21,'Ddr3','default.jpg'),
(22,'Ddr4','default.jpg'),
(23,'Otros','default.jpg'),
(24,'Cajas','default.jpg'),
(25,'Fuentes','default.jpg'),
(26,'Perifericos','default.jpg'),
(27,'Servicios','default.jpg'),
(28,'Sobremesa','default.jpg'),
(29,'Amd','14259458772039704.jpg'),
(30,'Portatiles','default.jpg');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `productos` (
  `idproducto` smallint NOT NULL AUTO_INCREMENT,
  `idcategoria` tinyint NOT NULL,
  `nombre` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL,
  `descripcion` mediumtext CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci,
  `precio` decimal(6,2) unsigned NOT NULL,
  `marca` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL,
  `imagen` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT 'default.jpg',
  PRIMARY KEY (`idproducto`),
  CONSTRAINT `fk_productos_categorias` FOREIGN KEY (`idcategoria`) REFERENCES `categorias` (`idcategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES 
(1,19,'Procesador - Intel Celeron G1840 2.8Ghz Box','Gran rendimiento con un coste realmente ajustado',40.00,'Intel','intelsocket/14258595863011386'),
(2,19,'Procesador - Intel Core i3-4160 3.6Ghz','Ventajas: Temperaturas, velocidad, precio, juego sin cuello de botella.',115.00,'Intel','intelsocket/14258596261238775'),
(3,19,'Procesador - Intel Core i7-4790 3.6Ghz Box','Un gran procesador con el que podrás hacer de todo, sin miedo a sobrecargas.',307.00,'Intel','intelsocket/14258596456403026'),
(4,4,'Asrock H81M-DGS R2.0','ASRock tiene el compromiso de utilizar materiales de alta calidad para construir las mejores placas base, por eso todos los capacitadores de esta placa base están construidos con capacitadores 100% sólidos. Estos capacitadores sólidos proporcionan larga durabilidad y rendimiento ultra estable.',50.00,'ASRock','intelplacas/14258596788795874'),
(5,4,'Asus Z97-P','Placa base Z97 ATX con M.2, un sonido cristalino con Crystal Sound 2 y 5X Protection para un rendimiento y entretenimiento avanzado. Calidad comprobada - 5X Protection. Más de1000 dispositivos compatible. Más de 7000 horas de validación.',104.00,'Asus','intelplacas/14258597882993929'),
(6,21,'Corsair Value Select DDR3','Corsair se ha construído su reputación sobre memorias de alto rendimiento que brillan con luz propia en situaciones de mayor exigencia. Pero no todos los ordenadores están hechos para un uso tan extremo. Corsair nos trae memorias de la misma fiabilidad creadas para ordenadores estandar como por ejemplo para oficinas.',23.00,'Corsair','ddr/1425859809743367'),
(7,21,'Kingston HyperX Fury Blue DDR3','Métete de lleno en el juego con HyperX® FURY. Sea cual sea tu nivel podrás estar a la altura, ya que FURY reconoce de forma automática la plataforma anfitriona y realiza automáticamente un overclock a la frecuencia más alta publicada (hasta 1866 MHz), lo que te aporta toda la potencia que necesitas en tus partidas. FURY, que tiene un precio asequible, presenta un disipador de calor de diseño asimétrico disponible en negro, azul y rojo y, por primera vez en la historia de la línea HyperX, se encuentra disponible también en blanco, con una placa de circuito impreso negra. Están probadas al cien por cien y respaldadas por una garantía de por vida, soporte técnico gratuito y la legendaria fiabilidad de Kingston.',76.00,'Nullware','ddr/14258598408685564'),
(8,13,'Corsair Force Series GS 240GB SSD','SSD Unformatted Capacity  240 GB. Max Sequential R/W (ATTO)555 MB/s sequential read - 525 MB/s sequential write. Max Random 4k Write (IOMeter 08) 90k IOPS (4k aligned). Interface SATA 6Gb/s.',160.00,'Corsair','ssd/14258598595531097'),
(9,11,'WD Green 2TB SATA3','A medida que aumentan las capacidades de los discos duros, también aumenta la energía necesaria para hacerlos funcionar. Los discos WD Caviar Green hacen posible que nuestros clientes, conscientes de la importancia del ahorro de energía, construyan sistemas con mayor capacidad y consigan el perfecto equilibrio entre el rendimiento del sistema, la fiabilidad garantizada y el ahorro de energía.',83.00,'WD','sata/14258598806748517'),
(10,11,'Toshiba DT01ACA100 1TB','La unidad de disco duro de 1 TB, 7200 RPM, 3,5 pulgadas (8,9 cm) de Toshiba es optimizado para que se utilice en ordenadores de sobremesa particulares o empresas respetuosos de bajo consumo de energía.\r\n\r\nLa serie DT01ACAxx usa tecnología de formato avanzado y ofrece la mejor tecnología de gestión de alimentación. Además, esta unidad de sobremesa proporciona un ahorro de energía en inactividad de hasta un 16% en comparación con el modelo anterior. Las unidades con interfaz SATA de 6 GB/s logra un rendimiento de 7200 RPM.',55.00,'Toshiba','sata/14258599051224205'),
(11,11,'Seagate NAS HDD 3TB SATA3','La última incorporación a la familia de discos duros Seagate. Los NAS HDD de Seagate®, que se adaptan con precisión a las necesidades de los pequeños sistemas NAS, proporcionan el mejor rendimiento y la mayor capacidad de almacenamiento para los sistemas NAS de 1 a 5 bahías. Se ha probado la compatibilidad de NAS HDD con los mejores proveedores de soluciones NAS del sector.',130.00,'Seagate','sata/14258599258789866'),
(12,24,'AeroCool VS-3 Advance Gaming','V-3 Avance se han diseñado con los jugadores en mente. El panel frontal está diseñado con características fuertes y es simplemente una llamativa pieza, entre muchos otros.\r\n\r\nCon los convertidores de disco duro incluido, en esta torre, el usuario puede instalar un máximo de 6x2.5 \"HDD o 3.5\" HDD, y cada uno actúa como soporte del ventilador para un ventilador de 12cm para ser montado en el lado opuesto del disco duro para enfriar el calor que genera HDD.',50.00,'AeroCool','cajas/14258599593061856'),
(13,24,'Corsair Carbide 500R ','Semitorre ATX con exteriores en color blaco e interior en color negro. Diseño para mejorar el flujo de aire ya que consta de metal mesh en paneles frontal, superior y lateral.',130.00,'Corsair','cajas/14258599963833145'),
(14,24,'Sharkoon T9 Value','Las carcasas de la serie de values T9 ATX en varios colores: Aparte de los principales montar junta, las plantillas de ranura y los mecanismos de bloqueo rápido también son de color de las características interiores.',52.00,'Sharkoon','cajas/1425860048176368'),
(15,24,'Thermaltake Urban S31','Semitorre ATX con interior y exterior en color negro, con un excelente sistema para ordenar los cables en el interior y conseguir así el mejor flujo de aire. Incluye dos ventiladores preinstalados y filtros antipolvo. Frontal efecto aluminio cepillado.',80.00,'Thermaltake','cajas/14258599593066781'),
(16,25,'AeroCool Strike-X Power 500W','Fuente de alimentación de 500W con certificación 80 PLUS BRONZE. Impactante diseño y alta eficiencia a todos sus niveles de carga. Cuenta con un ventilador de 139mm silencioso con control de velocidad inteligente. Ofrece estabilidad y redimiento\r\n',50.00,'AeroCool','fuentes/1425860068533324'),
(17,20,'AMD A6-5400K 3.60Ghz','Gran procesador que deja jugar a todas las novedades en hdmi con gran velocidad de procesamiento,tendras horas de entretenimiento al dia, genial en todos los aspectos.',45.00,'Amd','procesadores/14259002582733318'),
(18,20,'AMD A10-6790K 4.0Ghz','Presentamos uno de los modelos que forma parte de la nueva generación de APU \"Richland\". A10-6790k mejora su rendimiento respecto a las actuales APU, usando una nueva arquitectura de núcleos Steamroller e incrementando su rendimiento grafico gracias a la arquitectura GNC de AMD.',121.00,'Amd','procesadores/14259004443579828'),
(19,5,'Asrock FM2A78M-HD+','Esta placa base es compatible con ambas APU AMD FM2 y FM2+. Por favor, tenga en cuenta que algunas nuevas características sólo pueden ser compatibles con APU FM2+.',56.00,'Asrock','placasbase/14259007383287952'),
(20,5,'Asus A88X-PLUS','Motherboard A88X con FM2 + Socket impulsa gráficos integrados en todo APUs y cuenta con BIOS UEFI intuitiva. GPU Boost-Unlock integra un rendimiento gráfico hasta un 30% de mejora. ASUS 5X PROTECCIÓN - todo tipo de protección ofrece la mejor calidad, fiabilidad y durabilidad',82.00,'Asus','placasbase/142590093129469'),
(21,5,'MSI A88X-G45 Gaming Assasin´s Creed','Te presentamos la A88X-G45 Gaming de MSI, una placa BBase con socket AMD FM2+. Incluye Código de descaraga del juego Assasin´s Creed Liberation.Soporta Procesadores AMD Socket FM2 + / FM2 A-Series/Athlon ?. Soporta memoria XMP / AMP DDR3\r\n\r\n',130.00,'MSI','placasbase/14259011651887716'),
(22,21,'Crucial DDR3 1600 8GB','Te presentamos la memoria de 8GB DDR3 1600 CL11 de la prestigiosa marca Crucial. Te presentamos la memoria de 8GB DDR3 1600 CL11 de la prestigiosa marca Crucial.',68.00,'Crucial','memoriaram/14259017017189296'),
(23,21,'G.Skill Ripjaws X DDR3 2x4GB','Excelente kit de memorias de 8GB compuesto por 2 módulos de 4GB exactamente iguales, con latencias bajas para un rendimiento óptimo.Tipo: DDR3 Latencias: 11-11-11-30',71.00,'G.Skill','memoriaram/14259018903339060'),
(24,11,'Hitachi Deskstar 7K4000 4TB','Capacidad de disco duro 4000 GB. Tamaño de disco duro 88.9 mm (3.5 \"). Velocidad de rotación del disco duro 7200 RPM. Interfaz del disco duro Serial ATA III. Memoria temporal 64 MB. Transmisión de datos. Velocidad de transferencia de datos 6 Gbit/s.\r\n',190.00,'Hitachi','sata/14259024564658095'),
(25,14,'WD My Passport Ultra 2TB','El software de copia de seguridad automática WD SmartWare Pro le permite elegir cuándo y cómo hacer las copias de seguridad de sus archivos. Programe a su conveniencia las copias de seguridad automáticas de sus archivos. Elija entre realizar copias de seguridad por archivos o por carpetas. Utilice su cuenta de Dropbox para hacer copias de seguridad en la nube. Se necesita una cuenta de Dropbox para hacer copias de seguridad en la nube. Los servicios en la nube pueden cambiar, terminarse o interrumpirse en cualquier momento y pueden variar según el país.',103.00,'WD','discosduros/14259263687487137'),
(26,14,'Toshiba STOR.E Slim Mac 1TB','Almacene datos con seguridad en los nuevos discos duros externos STOR.E SLIM. Su diseño elegante, delgado y ligero lo convierte en un compañero perfecto. Transfiera sus archivos mediante SuperSpeed USB 3.0 y manténgalos protegidos con seguridad utilizando un bloqueo por contraseña. El software precargado NTI® Backup Now EZ? permite realizar copias de seguridad automatizadas con facilidad. Podrá utilizar STOR.E SLIM tanto en PC como en Mac gracias al controlador Tuxera NTFS para Mac (incluido).',73.00,'Toshiba','discosduros/14259265459128167'),
(27,14,'WD My Cloud 6TB','My Cloud 6TB Finalmente, una nube en exclusiva - Guárdelo todo en un único sitio y acceda a ello desde cualquier lugar con su ordenador Windows, Mac, teléfono inteligente o tableta. Proteja sus archivos con una copia de seguridad automática para todos sus ordenadores. Y con la carga directa de archivos desde sus dispositivos móviles, todos sus datos importantes se guardarán de forma segura en su nube personal.',361.00,'WD','discosduros/14259268460728273'),
(28,14,'WD My Passport AV-TV 1TB','My Passport AV-TV 1TB Conecte el disco de almacenamiento My Passport AV-TV a su televisor con grabador y disfrute de grabaciones de alta calidad, así como de una reproducción perfecta de películas, programas de televisión, eventos deportivos y mucho más.',98.00,'WD','discosduros/14259271214459522'),
(29,18,'Asus GeForce GT730 Silent','Te presentamos la ASUS 0dB GT730, sin ventilador, una tarjeta gráfica con DirectX ® 11 y soporte HDMI. Exclusivo diseño térmico 0dB disipa el calor de manera eficiente sin ningún tipo de ruido. HTPC listo el diseño con soporte de perfil bajo incluido.',64.00,'Asus','graficas/14259388993036489'),
(30,29,'MSI Radeon R9 270X Gaming','PRE overclockeado La mayoría de las tarjetas gráficas MSI Gaming vienen pre-overclockeado de fábrica. Esto simplemente significa que usted obtiene más rendimiento de su tarjeta, sin gastar tiempo en la comprobación de los relojes y estabilidad máximas. Las Tarjetas gráficas Pre-overclockeado simplemente dan más rendimiento.',200.00,'Msi','graficas/14259392971966117'),
(31,29,'Sapphire R9 280X Vapor-X Tri-X','Extraordinaria potencia gráfica la de esta nueva gráfica de gama alta de AMD, la R9 280x ahora versión Vapor-X y con sistema de ventilación Tri-X que otorga una disipación extraordinaria rebajando la temperatura hasta 27º sobre el disipador de serie, reduciendo además el ruido generado en 4dBi. Por si fuese poco, esta edición incluye overclock para aumentar el rendimiento.',253.00,'Sapphire','graficas/14259396172475971'),
(32,18,'Gigabyte GeForce GT 730','El uso de componentes de alta calidad en las tarjetas gráficas es el factor clave para tener un producto duradero, estable y confiable. GIGABYTE de nuevo establece un nuevo estándar con Núcleo de Ferrita de choque, de baja RDS (on) MOSFET y de más bajo ESR Condensadores Sólidos, proporcionando la estabilidad y confiabilidad de su solución de gráficos de gama alta. Además, GIGABYTE Ultra Durable 2 Edition tarjetas gráficas ahora cuentan con una topología de diseño sofisticado. Este diseño el poder del Estado-of-the-art de GIGABYTE ofrece la combinación de las características térmicas, eléctricas, señales digitales, circuitos de alimentación y colocación óptima de los componentes para un rendimiento gráfico mejorado.',69.00,'Gigabyte','graficas/14259400450995737'),
(33,26,'Acer V246HLbmd 24\"','Te presentamos el Acer V246HLbmd, un monitor con pantalla de 24\" LED y resolución Full HD. Tamaño de la pantalla 61 cm ( 24 \"). Pantalla del modo Full HD. Tecnología Panel Neumático trenzado de Cine ( Film TN ). Tiempo de respuesta 5 ms. Relación de aspecto 16:9. Ángulo de visión horizontal de 170 °. Ángulo de visión vertical 160 °',137.00,'Acer','otros/14259415615327000'),
(34,26,'Corsair Raptor M45 Óptico','Está pensado para mejorar el rendimiento del juego. Incluye un potente sensor óptico de 5.000 DPI, diseñado para juegos, un rápido tiempo de respuesta, almohadillas de desplazamiento de PTFE y una rueda de desplazamiento para darle todo el control. Puede personalizarlo a su estilo de juego con el sistema de peso ajustable, y siete botones programables para almacenar macros y otras funciones de juego esenciales a solo un clic.',30.00,'Corsair','otros/14259417302504633'),
(35,26,'Creative Inspire T3150','Escucha música sin cables con los T3150 Wireless, ahora disponibles con la tecnología inalámbrica Bluetooth. Los T3150 Wireless, compatibles con la mayoría de los teléfonos móviles y portátiles que incluyen el perfil Bluetooth estéreo, añade una forma de conexión adicional que se suma a su conexión con cables. Los satélites T3150 Wireless incluyen la función Creative IFP (Image Focusing Plate), que proporciona un nivel acústico óptimo más amplio con una mejora de la direccionalidad acústica, y aporta a la música más protagonismo manteniendo la precisión tonal, mientras un subwoofer orientado hacia abajo y sintonizado de forma personalizada, deleitará tus sentidos. Los T3150 inalámbricos vienen con un mando a distancia con cable y botón de encendido/apagado y ajuste de volumen para un uso más sencillo.',52.00,'Creative','otros/14259418824986234'),
(36,26,'Rapoo 8000 Teclado','Compacto y fiable. El diseño compacto y elegante, ofrece comodidad y ahorro de espacio. Resistente a salpicaduras. Diseño del teclado resistente a salpicaduras. No tendrá que volver a preocuparse por su teclado si derrama algún liquido sobre el accidentalmente.',29.00,'Rapoo','otros/14259421014843590'),
(37,28,'Apple iMac i5','Nuevo iMac (Late 2013) Nuevo diseño ultrafino. El nuevo iMac solo mide 5 mm de grosor, pero ofrece una potencia increíble. Lo último en tecnología. Con procesadores Intel de 4.ª generación y la rapidísima conexión Wi-Fi 802.11ac, el iMac más fino hasta la fecha también es el más potente. Espectacular pantalla panorámica. Se ha rediseñado para disminuir los reflejos en un 75% y hacerte sentir aún más cerca de tus contenidos. \r\n',1259.00,'Apple','sobremesa/14259847107819125'),
(38,28,'PcCom Gaming Battle','Una vez más Pc Componentes sorprende a la industria de la informática con el lanzamiento de los nuevos PcCom, la nueva línea de Pcs de sobremesa para juegos creada tras un amplio trabajo de investigación para ofrecer el mayor rendimiento y optimización en el juego para nuestros clientes. Ensamblados por nuestros expertos de montaje, los nuevos PcCom ofrecen un rendimiento increíble, con máxima velocidad, capacidad de ampliación y un amplio abanico de posibilidades dentro de la gama, en la que podrá elegir el producto óptimo según las necesidades y el tipo de Pc que está buscando.',460.00,'PcCom','sobremesa/14259855332532378'),
(39,30,'Asus X553MA Intel Celeron N2840','Portátiles de 15,6\" diseñados pensando en el usuario que utiliza su ordenador a diario.Diseño elegante disponible en una amplia gama de colores expresivos. Funciones centradas en el usuario, como Smart Gesture e IceCool. Audio de alta calidad mejorado con la tecnología SonicMaster de ASUS.',269.00,'Asus','portatiles/14260653307209582'),
(40,30,'MSI GP60 2PE-422XES i5','Te presentamos el GP60 2PE (Leopard) 422XES un portátil con procesador Intel Core i5, 4GB de RAM y 500GB de disco duro. Procesador Intel® Core? i5 de la 4º generación NVIDIA GeForce 840M 2GB VRAM DDR3. Exclusiva tecnología Cooler Boost. Tecnología Matrix display que proporciona una mejor experiencia visual para el gaming extremo.  Teclado SteelSeries hecho para gamers. Diseño exclusivo Audio Boost que proporciona un sonido cristalino y claro.\r\n',659.00,'MSI','portatiles/14260656670443987'),
(41,30,'MacBook Pro Retina i7/16GB','El MacBook Pro con pantalla Retina sorprende porque es asombrosamente fino y ligero. Pero lo increíble de verdad es que un portátil así sea, además, tan y tan potente. Conseguir semejante rendimiento en un diseño como este no ha sido fácil. En absoluto. Cada milímetro está fabricado y montado con la máxima precisión. Y al diseñarlo hemos tenido que tomar decisiones arriesgadas. Un ejemplo: hemos sustituido viejas tecnologías como el disco duro giratorio y las unidades de disco óptico, que tanto ocupan, por opciones de alto rendimiento como el almacenamiento flash. ¿Por qué? Porque es mucho más rápido y fiable y ocupa hasta un 90% menos. Con todo esto, no es extraño que el MacBook Pro sea tan versátil y cómodo de llevar.',1880.00,'Apple','portatiles/14260660787342896'),
(42,27,'Garantía 6 meses','NullWare te ofrece una extensión de garantía, para productos nuevos comprados desde la web. Esta garantía extiende la de fábrica ofreciéndote mayor seguridad a tus productos. ',100.00,'Nullware','otros/14260675109793995'),
(43,27,'Montaje','Montaje de ordenadores con Configuración establecida.\r\n\r\nEn el montaje se incluyen todos los materiales necesarios para un montaje de calidad profesional, como por ejemplo bridas de sujección, pasta térmica de gran calidad para los procesadores, etc. Todos los accesorios de cada componente y embalajes son enviados junto con el equipo montado.',30.00,'Nullware','otros/14260700800476322'),
(44,27,'Análisis PC','En NullWare te ofrecemos realizar un análisis de compatibilidad y mejora, destinado a la compra de ordenadores completos. El análisis consiste en una revisión exaustiva de la configuración y realización de pruebas con los mejores programas tester. Dependiendo del resultado del análisis un experto se pondrá en contacto contigo para asesorarte sobre componentes más eficaces y con mejor rendimiento, siempre teniendo en cuenta una relación calidad/Precio',30.00,'Nullware','servicios/14260704823374595'),
(45,22,'G.Skill RipJaws 4 DDR4','G.SKILL Ripjaws 4 series DDR4 es una memoria diseñada para la última plataforma Intel X99! Da igual si procesas grandes cantidades de datos o un juegos, Todos los kits de memoria Ripjaws 4 se prueban con una amplia gama de software para garantizar el máximo rendimiento y estabilidad. Haz de Ripjaws 4 la opción ideal para tu sistema DDR4!',230.00,'G.Skill','ddr/1426158266734276');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE usuarios ADD COLUMN avatar VARCHAR(255) DEFAULT 'default-avatar.png';


select * from usuarios;
select * from pedidos where idusuario = 7;
# Progreso de estudio — Programación III (examen martes 24/11/2026)

Documento de traspaso: si se abre una conversación nueva en este proyecto, leer esto primero.

## Quién es el estudiante y qué enfrenta
- Estudiante UNA Costa Rica, EIF206 Programación III, profesora Kristel Duarte. Sabe poco C++; Java y bases de datos desde cero. Windows, IntelliJ IDEA Community, MySQL 8 + Workbench ya instalados y funcionando.
- Asistió solo julio–agosto (incapacitado el resto). Examen: "Examen Único Comprensivo", totalmente práctico: desarrollar un sistema aplicando todo el semestre. La profesora NO responderá dudas sobre el formato; no se sabe si es Swing o JavaFX, si entran MySQL/SP/hilos/sockets, ni si se permite internet/material. Hay que prepararse para el peor caso: entra todo y posiblemente sin internet.
- Contenido del curso (clonable): https://github.com/SiandiAB/progra-iii.git (clases, labs, `guias/quiz-final/index.html` con 8 bloques: git, java, poo, swing, eventos, arq, bd, hilos). Lab 8 (clase 13) ≈ examen final Veterinaria.
- Repo de práctica del estudiante: https://github.com/yochi-prog06/claudepractivas.git (proyecto IntelliJ plano, sin Maven; paquete `dia1`).

## Plan de 4 días (convención del estudiante: la madrugada del 21 cuenta como día 1)
- Día 1 — Java + POO (hecho): clase abstracta, herencia, interfaz, Comparable, equals/hashCode, genéricos `Gestor<T>`, lambdas/Streams/Optional, record, enum, aliasing/copia defensiva.
- Día 2 — GUI + capas + TXT (hecho): Swing (JFrame, layouts, eventos, JTable, JOptionPane), Vista→Controller→Service→Repositorio (interfaz), `RepositorioActividadTxt`, Factory. Ya agregó `eliminar` en todas las capas.
- Día 3 (domingo 22/11, en curso) — MySQL/JDBC: BD `actividades_db`, usuarios `act_definer` (dueño) y `act_app` (solo EXECUTE), 4 procedimientos (listar, insertar, actualizar_inscritos, eliminar), `RepositorioActividadMySQL` con CallableStatement, contraseña por variable de entorno `ACT_DB_PASSWORD=App#2026`. El estudiante ya lo ejecutó sin errores y la ventana carga datos desde MySQL.
- Día 4 (lunes 23/11) — hilos y sockets (~1 h) + simulacro cronometrado de 3 h 20 min con un enunciado nuevo.

## Pendiente / siguiente
- Explicar con calma las piezas de JDBC y del script SQL que el estudiante dice no entender (Connection, DriverManager, CallableStatement, ResultSet, try-with-resources, DEFINER, DELIMITER, SIGNAL, GRANT).
- Kit "sin internet": guardar el JAR de `mysql-connector-j` (carpeta `lib/` del repo o copia local), repo clonado, plantilla de proyecto que ya funcione, checklist de configuración (librería + variable de entorno + línea del repositorio).
- Practicar el examen de la Veterinaria (enunciado en los docs del proyecto) con MySQL.
- Hilos/sockets y simulacro el lunes.

## Notas técnicas verificadas
- Todo el código de los días 1–3 se compiló y probó (JDK 21; MySQL probado con MariaDB 10.11 y su driver, la URL de entrega es `jdbc:mysql://localhost:3306/actividades_db`).
- Detalles de código del estudiante: usar `return` tras el aviso de error en eventos; `listar()` devuelve copia (ordenar la copia no cambia la lista interna).

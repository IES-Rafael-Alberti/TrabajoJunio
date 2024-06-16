package GBD

import GDatos.GestionDatos
import datosTabla.Alumno
import datosTabla.AlumnoCEData
import datosTabla.AlumnoRAData
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class GestionBD: IBD {
    private val filePath = "src/jvmMain/kotlin/archivos/un1-PlanificaciónyDiario PRO - 2223 -v1 - 3EV Actividades_Instrumentos.csv"
    private val url = "jdbc:h2:~/test"  // URL de conexión a la base de datos H2
    private val driver = "org.h2.Driver"  // Driver JDBC para H2

    init {
        try {
            Class.forName(driver)
        } catch (e: ClassNotFoundException) {
            throw SQLException("Error al cargar el driver de la base de datos: ${e.message}")
        }
    }

    override fun inicializarBaseDatos() {
        crearTablaAlumno()
        crearTablaAlumnoRA()
        crearTablaAlumnoCE()
    }

    override fun almacenarDatosEnBD(): String {
        inicializarBaseDatos()

        // Obtener los datos de los alumnos
        val (alumnos, alumnosRA, alumnosCE) = obtenerDatosAlumnos()

        // Insertar datos en la tabla datosTabla.Alumno
        insertarAlumnos(alumnos)

        // Insertar datos en la tabla AlumnoRA
        insertarAlumnosRA(alumnosRA)

        // Insertar datos en la tabla AlumnoCE
        insertarAlumnosCE(alumnosCE)

        return "Registros añadidos con éxito"
    }

    // Función para borrar el contenido de la base de datos
    override fun borrarContenidoBaseDatos() {
        // Borrar datos de la tabla AlumnoCE
        borrarDatosTabla("AlumnoCE")

        // Borrar datos de la tabla AlumnoRA
        borrarDatosTabla("AlumnoRA")

        // Borrar datos de la tabla datosTabla.Alumno
        borrarDatosTabla("datosTabla.Alumno")

        println("Contenido de la base de datos borrado exitosamente.")
    }

    // Función para borrar datos de una tabla específica
    override fun borrarDatosTabla(nombreTabla: String) {
        val connection = getConnection()

        try {
            val statement = connection.createStatement()
            val sql = "DELETE FROM $nombreTabla"
            val rowsAffected = statement.executeUpdate(sql)

            println("Se han borrado $rowsAffected registros de la tabla $nombreTabla.")
        } catch (e: SQLException) {
            throw e
        } finally {
            connection.close()
        }
    }

    override fun mostrarDatosDesdeBD() {
        // Conectar a la base de datos y consultar los datos
        getConnection().use { connection ->
            val query = """
                SELECT a.id AS idAlumno, a.nombre AS nombreAlumno, 
                       ar.idModulo, ar.idRA, ar.desc AS descRA, ar.porcentaje AS porcentajeRA, ar.nota AS notaRA,
                       ace.idCE, ace.desc AS descCE, ace.porcentaje AS porcentajeCE, ace.nota AS notaCE
                FROM datosTabla.Alumno a
                LEFT JOIN AlumnoRA ar ON a.id = ar.idAlumno
                LEFT JOIN AlumnoCE ace ON a.id = ace.idAlumno AND ar.idRA = ace.idRA
                ORDER BY a.id, ar.idRA, ace.idCE
            """

            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            var currentAlumnoId: String? = null
            var currentRAId: Int? = null
            var currentAlumnoName: String? = null
            var currentModulo: String? = null
            var currentPORa: Double? = null
            var currentNotaRA: Double? = null

            while (resultSet.next()) {
                val idAlumno = resultSet.getString("idAlumno")
                val nombreAlumno = resultSet.getString("nombreAlumno")
                val idModulo = resultSet.getString("idModulo")
                val idRA = resultSet.getInt("idRA")
                val descRA = resultSet.getString("descRA")
                val porcentajeRA = resultSet.getDouble("porcentajeRA")
                val notaRA = resultSet.getDouble("notaRA")
                val idCE = resultSet.getString("idCE")
                val descCE = resultSet.getString("descCE")
                val porcentajeCE = resultSet.getDouble("porcentajeCE")
                val notaCE = resultSet.getDouble("notaCE")

                if (idAlumno != currentAlumnoId) {
                    if (currentAlumnoId != null) {
                        // Imprimir los criterios de evaluación del alumno anterior
                        println()
                    }
                    // Imprimir nuevo alumno
                    println("┌──────────────────────────────┬────────────────────────────────┐")
                    println("│ ${idAlumno.padEnd(28)} │ ${nombreAlumno.padEnd(30)} │")
                    println("└──────────────────────────────┴────────────────────────────────┘")
                    currentAlumnoId = idAlumno
                    currentAlumnoName = nombreAlumno
                }

                if (idModulo != currentModulo) {
                    // Imprimir módulo
                    println("┌──────────────────────────────┬────────────────────────────────┐")
                    println("│ ${idModulo?.padEnd(28) ?: "".padEnd(28)} │                                │")
                    println("└──────────────────────────────┴────────────────────────────────┘")
                    currentModulo = idModulo
                }

                if (idRA != currentRAId) {
                    if (currentRAId != null) {
                        // Imprimir criterio de evaluación anterior
                        println()
                    }
                    // Imprimir nuevo RA
                    println("┌──────────────────────────────┬────────────────────┬───────────────────┐")
                    println("│ ${descRA?.padEnd(28) ?: "".padEnd(28)} │ ${porcentajeRA.toString().padEnd(20)} │ ${notaRA.toString().padEnd(17)} │")
                    println("└──────────────────────────────┴────────────────────┴───────────────────┘")
                    currentRAId = idRA
                    currentPORa = porcentajeRA
                    currentNotaRA = notaRA
                }

                // Imprimir criterio de evaluación
                if (idCE != null) {
                    println("┌──────────────────────────────┬────────────────────┬───────────────────┐")
                    println("│ ${descCE?.padEnd(28) ?: "".padEnd(28)} │ ${porcentajeCE.toString().padEnd(20)} │ ${notaCE.toString().padEnd(17)} │")
                    println("└──────────────────────────────┴────────────────────┴───────────────────┘")
                }
            }

            // Imprimir los criterios de evaluación del último alumno
            println()
            resultSet.close()
            statement.close()
        }
    }

    override fun getConnection(): Connection {
        val user = "sa"
        val password = ""
        return DriverManager.getConnection(url, user, password)
    }

    override fun crearTablaAlumno() {
        val connection = getConnection()

        val sql = """
            CREATE TABLE IF NOT EXISTS datosTabla.Alumno (
                id VARCHAR(3) PRIMARY KEY,
                nombre VARCHAR(255)
            )
        """
        val statement = connection.createStatement()
        statement.executeUpdate(sql)

        statement.close()
        connection.close()
    }

    override fun crearTablaAlumnoRA() {
        val connection = getConnection()

        val sql = """
            CREATE TABLE IF NOT EXISTS AlumnoRA (
                idAlumno VARCHAR(3),
                idModulo VARCHAR(3),
                idRA INT,
                desc VARCHAR(255),
                porcentaje DOUBLE,
                nota DOUBLE,
                PRIMARY KEY (idAlumno, idModulo, idRA)
            )
        """
        val statement = connection.createStatement()
        statement.executeUpdate(sql)

        statement.close()
        connection.close()
    }

    override fun crearTablaAlumnoCE() {
        val connection = getConnection()

        val sql = """
            CREATE TABLE IF NOT EXISTS AlumnoCE (
                idAlumno VARCHAR(3),
                idModulo VARCHAR(3),
                idRA INT,
                idCE VARCHAR(1),
                desc VARCHAR(255),
                porcentaje DOUBLE,
                nota DOUBLE,
                PRIMARY KEY (idAlumno, idModulo, idRA, idCE)
            )
        """
        val statement = connection.createStatement()
        statement.executeUpdate(sql)

        statement.close()
        connection.close()
    }

    override fun insertarAlumnos(alumnos: List<Alumno>) {
        val connection = getConnection()

        val sql = "INSERT INTO datosTabla.Alumno (id, nombre) VALUES (?, ?)"
        val preparedStatement = connection.prepareStatement(sql)

        for (alumno in alumnos) {
            preparedStatement.setString(1, alumno.id)
            preparedStatement.setString(2, alumno.nombre)
            preparedStatement.addBatch()
        }

        preparedStatement.executeBatch()

        preparedStatement.close()
        connection.close()
    }

    override fun insertarAlumnosRA(alumnosRA: List<AlumnoRAData>) {
        val connection = getConnection()

        val sql = "INSERT INTO AlumnoRA (idAlumno, idModulo, idRA, desc, porcentaje, nota) VALUES (?, ?, ?, ?, ?, ?)"
        val preparedStatement = connection.prepareStatement(sql)

        for (alumnoRA in alumnosRA) {
            preparedStatement.setString(1, alumnoRA.idAlumno)
            preparedStatement.setString(2, alumnoRA.idModulo)
            preparedStatement.setInt(3, alumnoRA.idRA)
            preparedStatement.setString(4, alumnoRA.desc)
            preparedStatement.setDouble(5, alumnoRA.porcentaje ?: 0.0)
            preparedStatement.setDouble(6, alumnoRA.nota ?: 0.0)
            preparedStatement.addBatch()
        }

        preparedStatement.executeBatch()

        preparedStatement.close()
        connection.close()
    }

    override fun insertarAlumnosCE(alumnosCE: List<AlumnoCEData>) {
        val connection = getConnection()
        val sql = "INSERT INTO AlumnoCE (idAlumno, idModulo, idRA, idCE, desc, porcentaje, nota) VALUES (?, ?, ?, ?, ?, ?, ?)"
        val preparedStatement = connection.prepareStatement(sql)

        for (alumnoCE in alumnosCE) {
            preparedStatement.setString(1, alumnoCE.idAlumno)
            preparedStatement.setString(2, alumnoCE.idModulo)
            preparedStatement.setInt(3, alumnoCE.idRA)
            preparedStatement.setString(4, alumnoCE.idCE)
            preparedStatement.setString(5, alumnoCE.desc)
            preparedStatement.setDouble(6, alumnoCE.porcentaje ?: 0.0)
            preparedStatement.setDouble(7, alumnoCE.nota ?: 0.0)
            preparedStatement.addBatch()
        }

        preparedStatement.executeBatch()

        preparedStatement.close()
        connection.close()
    }

    override fun obtenerDatosAlumnos(): Triple<List<Alumno>, List<AlumnoRAData>, List<AlumnoCEData>> {
        val gestionDatos= GestionDatos()
        val iniciales = gestionDatos.almacenarIniciales()
        val nombres = gestionDatos.almacenarNombres()
        val modulo = gestionDatos.obtenerModulo()
        val ra = gestionDatos.obtenerRA()
        val porcentajeRA = gestionDatos.obtenerPorcentajeRA()
        val notasRA = gestionDatos.obtenerNotasRA()
        val ces = gestionDatos.obtenerCEs()
        val porcentajesCE = gestionDatos.obtenerPorcentajesCE()
        val notasCEAlumnos = gestionDatos.notasCEAlumnos()

        val alumnos = mutableListOf<Alumno>()
        val alumnosRA = mutableListOf<AlumnoRAData>()
        val alumnosCE = mutableListOf<AlumnoCEData>()

        // Crear instancias de datosTabla.Alumno
        for (i in iniciales.indices) {
            alumnos.add(Alumno(iniciales[i], nombres[i]))
        }

        // Crear instancias de datosTabla.AlumnoRAData
        for (i in iniciales.indices) {
            val idRA = ra?.toIntOrNull() ?: 0
            alumnosRA.add(
                AlumnoRAData(
                    iniciales[i],
                    modulo ?: "",
                    idRA,
                    null,  // Descripcion RA
                    porcentajeRA,
                    notasRA[i]
                )
            )
        }

        // Crear instancias de datosTabla.AlumnoCEData
        for (i in iniciales.indices) {
            val idRA = ra?.toIntOrNull() ?: 0
            for (j in ces.indices) {
                alumnosCE.add(
                    AlumnoCEData(
                        iniciales[i],
                        modulo ?: "",
                        idRA,
                        ces[j],
                        null,  //Descripcion CE
                        porcentajesCE[j],
                        notasCEAlumnos[i][j]
                    )
                )
            }
        }

        return Triple(alumnos, alumnosRA, alumnosCE)
    }

}





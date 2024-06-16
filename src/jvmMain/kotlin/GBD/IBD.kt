package GBD

import datosTabla.Alumno
import datosTabla.AlumnoCEData
import datosTabla.AlumnoRAData
import java.sql.Connection

interface IBD {
    fun inicializarBaseDatos()
    fun almacenarDatosEnBD(): String
    fun borrarContenidoBaseDatos()
    fun borrarDatosTabla(nombreTabla: String)
    fun mostrarDatosDesdeBD()
    fun getConnection(): Connection
    fun crearTablaAlumno()
    fun crearTablaAlumnoRA()
    fun crearTablaAlumnoCE()
    fun insertarAlumnos(alumnos: List<Alumno>)
    fun insertarAlumnosRA(alumnosRA: List<AlumnoRAData>)
    fun insertarAlumnosCE(alumnosCE: List<AlumnoCEData>)
    fun obtenerDatosAlumnos(): Triple<List<Alumno>, List<AlumnoRAData>, List<AlumnoCEData>>
}
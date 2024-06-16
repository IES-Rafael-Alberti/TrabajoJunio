package datosTabla

data class AlumnoRAData(
    val idAlumno: String,
    val idModulo: String,
    val idRA: Int,
    val desc: String?,
    val porcentaje: Double?,
    val nota: Double?
)

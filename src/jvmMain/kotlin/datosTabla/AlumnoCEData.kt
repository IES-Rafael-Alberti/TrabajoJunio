package datosTabla

data class AlumnoCEData(
    val idAlumno: String,
    val idModulo: String?,
    val idRA:String,
    val idCE: String,
    val desc: String?,
    val porcentaje: Double?,
    val nota: Double?
)
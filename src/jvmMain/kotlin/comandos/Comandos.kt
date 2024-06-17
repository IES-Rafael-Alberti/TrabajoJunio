package comandos

import GBD.GestionBD
import GDatos.GestionDatos
import GDatos.GestionDatos.Companion.mostrarDatos

/**
 * Clase para manejar comandos de usuario y realizar diversas acciones en función de los comandos introducidos.
 */
class Comandos: IComando {
    private val filePath = "src/jvmMain/kotlin/archivos/un2-PlanificaciónyDiario PRO - 2223 -v1 - 3EV Actividades_Instrumentos.csv"
    private val gestionDatos = GestionDatos()
    private val modulo = gestionDatos.obtenerModulo()
    private val BD = GestionBD()

    private val comando1 = "-pi"
    private val comando2 = "-pi $filePath -mo $modulo"
    private val comando3 = "-pi $filePath -bd"
    private val comando4 = "-bd q"
    private val comando5 = "-bd qi"
    private val comando6 = "-bd d"
    private val comando7 = "exit"

    private val OPCIONES = setOf(comando1, comando2, comando3, comando4, comando5, comando6, comando7)

    /**
     * Solicita al usuario que ingrese un comando.
     * @return El comando ingresado por el usuario.
     */
    override fun pedirComando(): String? {
        val comando = readLine()
        return comando
    }

    /**
     * Lee y ejecuta comandos en un bucle hasta que se ingrese el comando "exit".
     */
    override fun leerComando() {
        var comando: String? = null

        while (comando != "exit") {
            comando = pedirComando()

            if (comando !in OPCIONES) {
                println("Comando no válido")
            } else {
                when (comando) {
                    "-pi" -> {
                        // Llama al método mostrarDatos() de GDatos.GestionDatos y maneja la salida correctamente
                        try {
                            mostrarDatos(gestionDatos)
                        } catch (e: Exception) {
                            println("Error al ejecutar mostrarDatos(): ${e.message}")
                        }
                    }
                    "-pi $filePath -mo $modulo" -> {

                    }
                    "-pi $filePath -bd" -> {
                        try {
                            BD.almacenarDatosEnBD()
                        } catch (e: Exception) {
                            println("Error al ejecutar mostrarDatos(): ${e.message}")
                        }
                    }
                    "-bd q" -> {
                        try {
                            BD.mostrarDatosDesdeBD()
                        } catch (e: Exception) {
                            println("Error al ejecutar mostrarDatos(): ${e.message}")
                        }
                    }
                    "-bd qi" -> {

                    }
                    "-bd d" -> {
                        BD.borrarContenidoBaseDatos()
                    }
                    "exit" -> {
                        println("Saliste del programa")
                    }
                }
            }
        }
    }

}
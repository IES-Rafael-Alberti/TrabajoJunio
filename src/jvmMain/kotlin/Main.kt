import GFicheros.GestionFichero
import comandos.Comandos

fun main(){
    val rellenarFichero=GestionFichero()
    rellenarFichero.rellenarDatos()

    val iniciarComandos= Comandos()
    iniciarComandos.leerComando()

}

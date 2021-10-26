package mx.tecnm.tepic.ladm_u2_ejercicio5_jugadordados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var proxJugador = 0
    var hiloJ1: HiloJugador? = null
    var hiloJ2: HiloJugador? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tirada1J1.visibility = View.INVISIBLE
        tirada2J1.visibility = View.INVISIBLE
        totalJ1.visibility = View.INVISIBLE
        tirada1J2.visibility = View.INVISIBLE
        tirada2J2.visibility = View.INVISIBLE
        totalJ2.visibility = View.INVISIBLE
        txtGanador.visibility = View.INVISIBLE

        hiloJ1 = HiloJugador( this , 1 , tirada1J1 , tirada2J1 , dadoJ1T1 , dadoJ1T2 , totalJ1 )
        hiloJ2 = HiloJugador( this , 2 , tirada1J2 , tirada2J2 , dadoJ2T1 , dadoJ2T2 , totalJ2 )
        hiloJ1!!.otroJugador = hiloJ2
        hiloJ2!!.otroJugador = hiloJ1

        hiloJ1!!.start()
        hiloJ2!!.start()

        botonIniciar.setOnClickListener {
            tirada1J1.visibility = View.INVISIBLE
            tirada2J1.visibility = View.INVISIBLE
            totalJ1.visibility = View.INVISIBLE
            tirada1J2.visibility = View.INVISIBLE
            tirada2J2.visibility = View.INVISIBLE
            totalJ2.visibility = View.INVISIBLE
            txtGanador.visibility = View.INVISIBLE
            botonIniciar.visibility = View.INVISIBLE
            proxJugador = 1
        }
    }
}


class HiloJugador(p: MainActivity , jugador: Int , txtTirada1: LinearLayout , txtTirada2: LinearLayout ,
                  txtTiro1: TextView , txtTiro2: TextView , txtTotal: TextView): Thread()
{
    val activity = p
    val jugador = jugador
    var tirosRealizados = 0
    var total = 0
    val txtT1 = txtTiro1
    val txtT2 = txtTiro2
    val txtT = txtTotal
    val txtTirada1 = txtTirada1
    val txtTirada2 = txtTirada2
    var otroJugador: HiloJugador? = null


    override fun run() {
        super.run()
        while( true )
        {
            if( activity.proxJugador == jugador )
            {
                var numDado = Math.round( (Math.random() * 5 + 1).toFloat() )
                tirosRealizados++
                when( tirosRealizados ) {
                    1 -> {
                        total = numDado
                        activity.runOnUiThread {
                            txtT1.setText( numDado.toString() )
                            activity.txtGanador.setText( "Tiro ${tirosRealizados} Jugador ${jugador}" )
                            activity.txtGanador.visibility = View.VISIBLE
                            txtTirada1.visibility = View.VISIBLE
                        }
                        activity.proxJugador = otroJugador!!.jugador
                    }
                    2 -> {
                        total += numDado
                        activity.runOnUiThread {
                            txtT2.setText( numDado.toString() )
                            txtT.setText( total.toString() )
                            activity.txtGanador.setText( "Tiro ${tirosRealizados} Jugador ${jugador}" )
                            txtTirada2.visibility = View.VISIBLE
                            txtT.visibility = View.VISIBLE
                        }
                        activity.proxJugador = otroJugador!!.jugador
                    }
                    3 -> {
                        tirosRealizados = 0
                        otroJugador!!.tirosRealizados = 0
                        activity.proxJugador = 0
                        if( total > otroJugador!!.total  )
                        {
                            activity.runOnUiThread {
                                activity.txtGanador.setText( "El ganador es el jugador ${jugador}" )
                                activity.botonIniciar.visibility = View.VISIBLE
                            }
                        }
                        else if( total == otroJugador!!.total )
                        {
                            activity.runOnUiThread {
                                activity.txtGanador.setText( "Juego empatado" )
                                activity.botonIniciar.visibility = View.VISIBLE
                            }
                        }
                        else{
                            activity.runOnUiThread {
                                activity.txtGanador.setText( "El ganador es el jugador ${otroJugador!!.jugador}" )
                                activity.botonIniciar.visibility = View.VISIBLE
                            }
                        }

                    } // CASO 3
                } // WHEN
                sleep( 1000 )
            }
            sleep( 1000 )
        }
    }
}
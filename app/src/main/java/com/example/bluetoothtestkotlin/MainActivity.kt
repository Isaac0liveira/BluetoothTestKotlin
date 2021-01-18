package com.example.bluetoothtestkotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var BA: BluetoothAdapter
    private lateinit var pareados: Set<BluetoothDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BA = BluetoothAdapter.getDefaultAdapter()
        if (BA.equals(null)) {
            Toast.makeText(this, "Bluetooth não suportado!", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (BA.isEnabled) {
            bt_ligado.isChecked = true
        }

        bt_nome.text = getDefaultBluetoothAdapter()

        bt_ligado.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (!isChecked) {
                    BA.disable()
                    bt_ligado.isChecked = false
                    bt_visivel.isChecked = false
                    Toast.makeText(this, "Desligado!", Toast.LENGTH_SHORT).show()
                } else {
                    bt_ligado.isChecked = true
                    val intentLigado = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intentLigado, 0)
                    Toast.makeText(this, "Ligado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        bt_visivel.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (!isChecked) {
                    BA.disable()
                    bt_ligado.isChecked = false
                    bt_visivel.isChecked = false
                    Toast.makeText(this, "Desligado!", Toast.LENGTH_SHORT).show()
                } else {
                    bt_ligado.isChecked = true
                    bt_visivel.isChecked = true
                    val intentVisivel = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                    startActivityForResult(intentVisivel, 0)
                    Toast.makeText(this, "Ligado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        bt_pesquisar.setOnClickListener { listar() }
    }

    private fun listar() {
        pareados = BA.bondedDevices
        val lista = arrayListOf<Any>()
        for(bt: BluetoothDevice in pareados){
            lista.add(bt.name)
        }

        Toast.makeText(this, "Mostrando Dispositivos Pareados", Toast.LENGTH_SHORT).show()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }

    @SuppressLint("HardwareIds")
    fun getDefaultBluetoothAdapter(): String? {
        if(BA.equals(null)){
            BA = BluetoothAdapter.getDefaultAdapter()
        }
        var nome = BA.name
        if(nome == null){
            nome = BA.address
        }
        return nome
    }
}
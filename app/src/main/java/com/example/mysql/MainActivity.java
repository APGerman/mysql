package com.example.mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvrecordatorios;
    Button Agregar,VerRecordatorios;
    ArrayList<String> listarecordatorios;
    ArrayList<Integer> listaid;
    private static final String UrlGetAll = GeneralUrl.Urlgeneral+"getall.php";
    private static final String UrlGetToday = GeneralUrl.Urlgeneral+"gettoday.php";
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvrecordatorios = findViewById(R.id.lvrecordatorios);
        Agregar = findViewById(R.id.btnagregar);
        VerRecordatorios = findViewById(R.id.btnrecordatorios);

        listarecordatorios = new ArrayList<String>();
        listaid = new ArrayList<Integer>();

        getData();

        lvrecordatorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String elegido = lvrecordatorios.getItemAtPosition(position).toString();
                int IdElegido = listaid.get(position);

                Intent i = new Intent(view.getContext(), RecordatorioSeleccionado.class);
                i.putExtra("idelegido", IdElegido);
                startActivity(i);
            }
        });


    }

    public void moverAgregar(View view){
        Intent i = new Intent(this, AgregarRecordatorio.class);
        startActivity(i);
    }

    public void CambioLista(View view){
        getData();
    }

    public void getData() {
        listarecordatorios = new ArrayList<String>();
        listaid = new ArrayList<Integer>();
        if (VerRecordatorios.getText().toString().toLowerCase().equals("ver recordatorios de hoy")) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlGetToday, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            listarecordatorios.add(object.getString("recordatorio"));
                            listaid.add(object.getInt("id"));
                        }

                    } catch (Exception e) {

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listarecordatorios);
                    lvrecordatorios.setAdapter(adapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            VerRecordatorios.setText("Ver Todos los recordatorios");
        }
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlGetAll, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            listarecordatorios.add(object.getString("recordatorio"));
                            listaid.add(object.getInt("id"));
                        }

                    } catch (Exception e) {

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listarecordatorios);
                    lvrecordatorios.setAdapter(adapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            );
            Volley.newRequestQueue(MainActivity.this).add(stringRequest);
            VerRecordatorios.setText("Ver recordatorios de hoy");
        }
    }
}
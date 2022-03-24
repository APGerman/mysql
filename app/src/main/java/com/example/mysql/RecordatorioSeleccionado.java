package com.example.mysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecordatorioSeleccionado extends AppCompatActivity {

    TextView tvrecordatorioseleccionado, tvfechaseleccionada;
    Button btnvolverseleccionado, btneditarseleccionado, btneliminarseleccionado;
    int IdElegido;
    String recordatorio, fecha;
    String UrlgetOne;
    private static final String UrlDelete = GeneralUrl.Urlgeneral+"delete.php";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio_seleccionado);

        tvrecordatorioseleccionado = findViewById(R.id.tvrecordatorioseleccionado);
        tvfechaseleccionada = findViewById(R.id.tvfechaseleccionada);
        btneditarseleccionado = findViewById(R.id.btneditarseleccionado);
        btneliminarseleccionado = findViewById(R.id.btneliminarseleccionado);
        btnvolverseleccionado = findViewById(R.id.btnvolverseleccionado);

        IdElegido =getIntent().getIntExtra("idelegido",-1);
        UrlgetOne = GeneralUrl.Urlgeneral+"getone.php?id="+ IdElegido;
        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlgetOne, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        recordatorio = object.getString("recordatorio");
                        fecha = object.getString("fecha");
                    }

                } catch (Exception e) {

                }
                tvrecordatorioseleccionado.setText(recordatorio);
                tvfechaseleccionada.setText(fecha);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordatorioSeleccionado.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        Volley.newRequestQueue(RecordatorioSeleccionado.this).add(stringRequest);
    }


    public void VolverRecordatorios(View View){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void IrEditar(View view){
        Intent i = new Intent(view.getContext(), EditarRecordatorio.class);
        i.putExtra("idelegido", IdElegido);
        startActivity(i);
    }

    public void DeleteData(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea eliminar");
        builder.setMessage("Esta seguro que desea eliminar la nota: "+ tvrecordatorioseleccionado.getText());
        builder.setPositiveButton("Eliminar",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, UrlDelete, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RecordatorioSeleccionado.this, "Borrado", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id",String.valueOf(IdElegido));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
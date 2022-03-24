package com.example.mysql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AgregarRecordatorio extends AppCompatActivity {

    EditText etrecordatorioguardar, etfechaguardar;
    Button btnguardar;
    String recordatorio;
    String fecha;

    DatePickerDialog.OnDateSetListener onDateSetListener;

    RequestQueue requestQueue;
    private static final String UrlInsert = GeneralUrl.Urlgeneral+"save.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_recordatorio);

        requestQueue = Volley.newRequestQueue(this);

        etrecordatorioguardar = findViewById(R.id.etrecordatorioguardar);
        etfechaguardar = findViewById(R.id.etfechaguardar);
        btnguardar = findViewById(R.id.btnguardar);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        etfechaguardar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarRecordatorio.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener, year, month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                }
                return true;
            }
        });

         onDateSetListener = new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month= month+1;
                etfechaguardar.setText(year+"-"+month+"-"+day);
             }
         };


    }

    public void SaveData(View view) {
        if (etrecordatorioguardar.getText().toString().isEmpty() || etfechaguardar.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Todos los campos son obligatorios");
            builder.setNegativeButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            recordatorio = etrecordatorioguardar.getText().toString();
            fecha = etfechaguardar.getText().toString();


            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, UrlInsert, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(AgregarRecordatorio.this, "Insertado", Toast.LENGTH_SHORT).show();
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
                    params.put("recordatorio",recordatorio);
                    params.put("fecha",fecha);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }


    public void VolverGuardar(View view){
        finish();
    }

}
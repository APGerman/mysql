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
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarRecordatorio extends AppCompatActivity {

    EditText etrecordatorioedit, etfechaedit;
    Button btneditar, btnvolver;
    int IdElegido;
    String recordatorio, fecha;
    private static final String UrlEdit = GeneralUrl.Urlgeneral+"edit.php";
    String UrlgetOne;
    RequestQueue requestQueue;
    DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_recordatorio);

        etrecordatorioedit = findViewById(R.id.etrecordatorioedit);
        etfechaedit = findViewById(R.id.etfechaedit);
        btneditar = findViewById(R.id.btneditar);
        btnvolver = findViewById(R.id.btnvolver);
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
                etrecordatorioedit.setText(recordatorio);
                etfechaedit.setText(fecha);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarRecordatorio.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        Volley.newRequestQueue(EditarRecordatorio.this).add(stringRequest);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        etfechaedit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_UP == motionEvent.getAction()){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditarRecordatorio.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener, year, month,day);
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
                etfechaedit.setText(year+"-"+month+"-"+day);
            }
        };
    }

    public void EditData(View view) {
        if (etrecordatorioedit.getText().toString().isEmpty() || etfechaedit.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Todos los campos son obligatorios");
            builder.setNegativeButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            String recordatorio = etrecordatorioedit.getText().toString();
            String fecha = etfechaedit.getText().toString();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, UrlEdit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(EditarRecordatorio.this, "Actualizado", Toast.LENGTH_SHORT).show();
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
                    params.put("recordatorio",recordatorio);
                    params.put("fecha",fecha);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

            Intent i = new Intent(view.getContext(), RecordatorioSeleccionado.class);
            i.putExtra("idelegido", IdElegido);
            startActivity(i);
        }
    }

    public void VolverEdit(View view){
        finish();
    }
}

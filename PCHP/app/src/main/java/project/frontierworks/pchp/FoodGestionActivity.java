package project.frontierworks.pchp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class FoodGestionActivity extends AppCompatActivity implements View.OnClickListener{

    private String UPDATE_URL_WATER = "http://192.168.0.5/pruebau/upgradeW.php";
    private String UPDATE_URL_FOOD = "http://192.168.0.5/pruebav/upgradeW.php";

    private TextView pasoW, pasoF;

    private String updateW = "1";
    private String updateF = "1";
    private Button foodbtn;
    private Button waterbtn;
    private String KEY_BOMBAESTADO = "estadoBomba";
    private String KEY_MOTORESTADO = "estadoMotor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_gestion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foodbtn = (Button) findViewById(R.id.foodbtn);
        waterbtn = (Button) findViewById(R.id.waterbtn);

        pasoW = (TextView) findViewById(R.id.textView6);
        pasoW.setText(updateW);

        pasoF = (TextView) findViewById(R.id.textView7);
        pasoF.setText(updateF);


        foodbtn.setOnClickListener(this);
        waterbtn.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    private void upgradeFood(){
        // Mostrar el dialogo del proceso
        final ProgressDialog loading = ProgressDialog.show(this, "Realizando...", "Espere por favor..." ,false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL_FOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Detener el dialogo de proceso
                loading.dismiss();
                // Mostrando el mensaje de la respuesta
                Toast.makeText(FoodGestionActivity.this, "Hecho", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Descartar el dialogo de proceso
                loading.dismiss();
                // Showing Toas
                Toast.makeText(FoodGestionActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{

                String pasado = pasoF.getText().toString().trim();
                Map<String,String> params = new Hashtable<>();

                //Agregando los parametros
                params.put(KEY_MOTORESTADO, pasado);
                return params;
            }
        };

        //Creando una cola de solicutdes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private void upgradeWater(){
        // Mostrar el dialogo del proceso
        final ProgressDialog loading = ProgressDialog.show(this, "Realizando...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL_WATER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Destacar el dialogo del proceso.
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(FoodGestionActivity.this, "Realizado correctamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el dialogo del proceso
                loading.dismiss();
                // Showing toast
                Toast.makeText(FoodGestionActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{

                String pasado = pasoW.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_BOMBAESTADO, pasado);
                return params;
            }
        };

        // Creacion de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view){

        if (view == foodbtn){
            upgradeFood();
        }

        if (view == waterbtn){
            upgradeWater();
        }
    }
}

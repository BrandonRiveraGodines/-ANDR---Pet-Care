package project.frontierworks.pchp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mvc.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import project.frontierworks.pchp.activity.LoginActivity;

public class AddPetActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView image_view_hdd;
    private EditText etCalendar;
    private EditText etNombre;
    private EditText etEstatura;
    private EditText etPeso;
    private Button btn_add_pet;
    private Button btnBuscar;
    private Spinner spinner;
    private Bitmap bitmap;
    private TextView tv_reference;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL = "http://192.168.43.90/android_login_api/pet_add/upload.php";

    private String KEY_NOMBRE = "nombre";
    private String KEY_FECHANACIMIENTO ="fechanacimiento";
    private String KEY_ESTATURA = "estatura";
    private String KEY_SEXO = "sexo";
    private String KEY_PESO = "peso";
    private String KEY_FOTOGRAFIA = "fotografia";
    private String KEY_IDDUENO = "iddueno";
    // private String KEY_IDDUENO = "iddueno";

    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Bundle parametros = this.getIntent().getExtras();
        String datos = parametros.getString("uid");

        //LoginActivity objeto_id = (LoginActivity)getIntent().getExtras().getSerializable("id");
        //LoginActivity objeto_nombre = (LoginActivity)getIntent().getExtras().getSerializable("name");
        //LoginActivity objeto_email = (LoginActivity)getIntent().getExtras().getSerializable("email");

        image_view_hdd = (ImageView) findViewById(R.id.image_view_bdd);
        etCalendar = (EditText) findViewById(R.id.etCalendar);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etEstatura = (EditText) findViewById(R.id.etEstatura);
        etPeso = (EditText) findViewById(R.id.etPeso);
        btn_add_pet = (Button) findViewById(R.id.btn_add_pet);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        spinner = (Spinner) findViewById(R.id.spinner);
        tv_reference = (TextView) findViewById(R.id.tv_reference);
        tv_reference.setText(datos);

        btnBuscar.setOnClickListener(this);
        btn_add_pet.setOnClickListener(this);

        etCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mCurrentDate = Calendar.getInstance();
                mYear = mCurrentDate.get(Calendar.YEAR);
                mMonth = mCurrentDate.get(Calendar.MONTH);
                mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddPetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCurrentDate.set(Calendar.YEAR, year);
                        mCurrentDate.set(Calendar.MONTH, month);
                        mCurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        etCalendar.setText(sdf.format(mCurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Selecciona la fecha");
                mDatePicker.show();
            }
        });

        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady();
            }
        });

        etCalendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady();
            }
        });

        etEstatura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady();
            }
        });

        etPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady();
            }
        });
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        // Mostrar el dialogo del proceso
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Destacar el dialogo de proceso
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(AddPetActivity.this, "Subidó correctamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Descartar el dialogo de progreso.
                loading.dismiss();
                //Showing Toast
                Toast.makeText(AddPetActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir vits a cadena
                String imagen = getStringImagen(bitmap);
                //Obtener el nombre de la imagen
                String nombre = etNombre.getText().toString().trim();
                //Obtener el nombre de la imagen
                String fechanacimiento = etCalendar.getText().toString().trim();
                //Obtener la estatura de la imagen
                String estatura = etEstatura.getText().toString().trim();
                //Obtener el sexo del perro.
                String sexo = spinner.getSelectedItem().toString().trim();
                //Obtener el peso del perro
                String peso = etPeso.getText().toString().trim();
                //Obtener el id del dueño
                String id_pasado = tv_reference.getText().toString().trim();

                // Creación de parámetros
                Map<String, String> params = new Hashtable<String, String>();

                // Agregando de parámetros
                params.put(KEY_NOMBRE, nombre);
                params.put(KEY_FECHANACIMIENTO, fechanacimiento);
                params.put(KEY_ESTATURA, estatura);
                params.put(KEY_SEXO, sexo);
                params.put(KEY_PESO, peso);
                params.put(KEY_FOTOGRAFIA, imagen);
                params.put(KEY_IDDUENO, id_pasado);

                return params;
            }
        };

        //Creacion de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    public void enableSubmitIfReady(){
        if (etNombre.getText().toString().length()>0 && etCalendar.getText().toString().length() > 0 && etEstatura.getText().toString().length()>0 && etPeso.getText().toString().length()>0 && spinner.toString().length()>0){
            btn_add_pet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                // Cómo obtener el mapa de bits de la Galeria
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Configuración del mapa de bits en ImageView
                image_view_hdd.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view){

        if (view == btnBuscar){
            showFileChooser();
        }

        if (view == btn_add_pet){
            uploadImage();
            Intent i = new Intent(AddPetActivity.this, NavActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void onPickImage(View view){
        ImagePicker.pickImage(this, "Selecciona tu imagen: ");
    }
}




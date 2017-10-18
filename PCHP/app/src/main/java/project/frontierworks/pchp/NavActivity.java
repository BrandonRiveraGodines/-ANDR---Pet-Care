package project.frontierworks.pchp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import project.frontierworks.pchp.app.AppConfigPet;
import project.frontierworks.pchp.app.AppController;
import project.frontierworks.pchp.helper.SQLiteHandler;
import project.frontierworks.pchp.helper.SessionManager;

import project.frontierworks.pchp.activity.LoginActivity;

import static java.sql.Types.NULL;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NavActivity.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
    private MenuItem btnLogout;

    private URL url;
    private ProgressDialog pDialog;

    private ImageView image_view_bdd;
    private TextView etNombre, etCalendar, etEstatura, etPeso;
    private TextView spinner;

    private ImageLoader imageLoader;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        txtName = (TextView) header.findViewById(R.id.navHeaderUser);
        txtEmail = (TextView) header.findViewById(R.id.navHeaderEmail);
        btnLogout = (MenuItem) findViewById(R.id.nav_out);
        image_view_bdd = (ImageView) findViewById(R.id.image_view_bdd);
        etNombre = (TextView) findViewById(R.id.etNombre);
        etCalendar = (TextView) findViewById(R.id.etCalendar);
        etEstatura = (TextView) findViewById(R.id.etEstatura);
        etPeso = (TextView) findViewById(R.id.etPeso);
        spinner = (TextView) findViewById(R.id.spinner);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite.
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        final String uid = user.get("uid");

        txtName.setText(name);
        txtEmail.setText(email);

        getPet(uid);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavActivity.this, AddPetActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
                finish();
            }
        });
    }

    private void getPet(final String uid) {
        // Tag uset to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Obteniendo información...");
        showDialog();


        StringRequest strReq = new StringRequest(Method.POST, AppConfigPet.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String nombre = user.getString("nombre");
                        String fechanacimiento = user.getString("fechanacimiento");
                        String estatura = user.getString("estatura");
                        String sexo = user.getString("sexo");
                        String peso = user.getString("peso");
                        String fotografia = user.getString("fotografia");
                        String iddueno = user.getString("iddueno");

                        etNombre.setText(nombre);
                        etCalendar.setText(fechanacimiento);
                        etEstatura.setText(estatura);
                        etPeso.setText(peso);
                        spinner.setText(sexo);
                        Picasso.with(getApplicationContext()).load(fotografia).into(image_view_bdd);

                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.setVisibility(View.INVISIBLE);


                        // loadImage(fotografia);
                        //image_view_bdd.setImageUrl(fotografia, mImageLoader);


                    } else {
                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("iddueno", uid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(NavActivity.this, NavActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        //Launching the login activity
        Intent intent = new Intent(NavActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dog) {

        } else if (id == R.id.nav_food) {
            item.setChecked(true);
            Intent i = new Intent(NavActivity.this, FoodGestionActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_cam) {
            item.setChecked(true);
            Intent i = new Intent(NavActivity.this, CamControlActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_team) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Equipo").setMessage("Alvarado Losoya Salvador Alejandro\nCorrales Herrera Mayra Jacquieline\nIñiguez Gonzalez Edith\nRivera Godines Brandon").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        } else if (id == R.id.nav_out) {
            logoutUser();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


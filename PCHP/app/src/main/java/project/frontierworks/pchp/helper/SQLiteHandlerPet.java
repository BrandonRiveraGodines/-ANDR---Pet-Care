package project.frontierworks.pchp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by brand on 04/07/2017.
 */

public class SQLiteHandlerPet extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandlerPet.class.getSimpleName();

    //Al static variables
    //Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "mascota";

    // Login Table Columns names
    private static final String KEY_ID = "idmascota";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_FECHANACIMIENTO = "fechanacimiento";
    private static final String KEY_ESTATURA = "estatura";
    private static final String KEY_SEXO = "sexo";
    private static final String KEY_PESO = "peso";
    private static final String KEY_FOTOGRAFIA = "fotografia";
    private static final String KEY_IDDUENO = "iddueno";

    public SQLiteHandlerPet (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NOMBRE + " TEXT, "
                + KEY_FECHANACIMIENTO + " TEXT, " + KEY_ESTATURA + " TEXT, "
                + KEY_SEXO + " TEXT, " + KEY_PESO + " TEXT, " + KEY_FOTOGRAFIA + " TEXT, "
                + KEY_IDDUENO + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    public void addPet(String nombre, String fechanacimiento, String estatura, String sexo, String peso, String fotografia, String iddueno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, nombre);
        values.put(KEY_FECHANACIMIENTO, fechanacimiento);
        values.put(KEY_ESTATURA, estatura);
        values.put(KEY_SEXO, sexo);
        values.put(KEY_PESO, peso);
        values.put(KEY_FOTOGRAFIA, fotografia);
        values.put(KEY_IDDUENO, iddueno);

        // Inserting row
        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "Nuevo usuario fue añadido: " + id);

    }
    /**
     * Getting pet data fro database
     */
    public HashMap<String, String> getPetDetails(){
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("nombre", cursor.getString(1) );
            user.put("fechanacimiento", cursor.getString(2));
            user.put("estatura", cursor.getString(3));
            user.put("sexo", cursor.getString(4));
            user.put("peso", cursor.getString(5));
            user.put("fotografia", cursor.getString(6));
            user.put("iddueno", cursor.getString(7));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Obteniendo mascota de SQLite: "+ user.toString());

        return user;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Se eliminaron todos los usuarios de SQLite");
    }
}











































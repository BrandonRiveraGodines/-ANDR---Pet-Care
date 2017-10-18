package project.frontierworks.pchp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by brand on 03/07/2017.
 */

public class SessionManagerPet {
    private static String TAG = SessionManagerPet.class.getSimpleName();

    // Shared PReferences
    SharedPreferences prefPet;

    Editor editor;
    Context _context;

    // Shared pref mode;
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "FortrainWorksAddPet";

    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_FECHANACIMIENTO = "fechanacimiento";
    private static final String KEY_ESTATURA = "estatura";
    private static final String KEY_SEXO = "sexo";
    private static final String KEY_PESO = "peso";
    private static final String KEY_FOTOGRAFIA = "fotografia";
    private static final String KEY_IDDUENO = "iddueno";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    public SessionManagerPet(Context context){
        this._context = context;
        prefPet = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefPet.edit();
    }

    public void setLogin(String nombre, String fechanacimiento, String estatura, String sexo, String peso, String fotografia, String iddueno, boolean isLoggedIn){
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_FECHANACIMIENTO, fechanacimiento);
        editor.putString(KEY_ESTATURA, estatura);
        editor.putString(KEY_SEXO, sexo);
        editor.putString(KEY_PESO, peso);
        editor.putString(KEY_FOTOGRAFIA, fotografia);
        editor.putString(KEY_IDDUENO, iddueno);
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();

        Log.d(TAG, "User login session modified");
    }

    public boolean isLoggedIn(){
        return prefPet.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}

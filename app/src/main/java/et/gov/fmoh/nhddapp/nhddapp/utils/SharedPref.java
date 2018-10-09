package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences settingSharedPref;

    public SharedPref(Context context) {
        settingSharedPref = context.getSharedPreferences("fileName", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = settingSharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public Boolean loadNightModeState(){
        Boolean state = settingSharedPref.getBoolean("NightMode",false);
        return state;
    }

    public void setNCoDVersion (String version ){
        SharedPreferences.Editor editor = settingSharedPref.edit();
        editor.putString("NCoDVersion",version);
        editor.commit();
    }

    public String getNCoDVersion(){
        String version = settingSharedPref.getString("NCoDVersion",null);
        return version;
    }

    public void setHMISIndicatorVersion (String version ){
        SharedPreferences.Editor editor = settingSharedPref.edit();
        editor.putString("HMISIndicatorVersion",version);
        editor.commit();
    }

    public String getHMISIndicatorVersion(){
        String version = settingSharedPref.getString("HMISIndicatorVersion",null);
        return version;
    }

    //ncodtype - mini, compact, and extended
    public void setNcodType(String ncodType){
        SharedPreferences.Editor editor = settingSharedPref.edit();
        editor.putString("NcodType",ncodType);
        editor.commit();
    }

    public String getNcodType(){
        String version = settingSharedPref.getString("NcodType",null);
        return version;
    }
}

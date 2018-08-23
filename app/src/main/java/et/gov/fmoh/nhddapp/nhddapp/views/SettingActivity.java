package et.gov.fmoh.nhddapp.nhddapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.helpers.SharedPref;

public class SettingActivity extends AppCompatActivity {
    SharedPref sharedPref;

    private Switch aSwitch ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        setTheme(sharedPref.loadNightModeState() ? R.style.DarkTheme : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        aSwitch =findViewById(R.id.nightMode_switch);
        if(sharedPref.loadNightModeState()){
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setNightModeState(isChecked);
                restartApp();
            }
        });

    }

    public void restartApp(){
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
        finish();
    }
}

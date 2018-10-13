package et.gov.fmoh.nhddapp.nhddapp.utils;

import android.text.TextUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class GenerateColor <T>{

    public int getColor(T item) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        // generate random color
        return generator.getColor(item);
    }

    public TextDrawable getTextDrawable (String itemName, int color){
        if (TextUtils.isEmpty(itemName))
            return null;

        //get first letter of each String item
        String firstLetter = String.valueOf(itemName.charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);

        return drawable;
    }
}

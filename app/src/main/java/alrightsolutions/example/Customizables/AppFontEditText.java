package alrightsolutions.example.Customizables;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by JohnConnor on 30-Jul-16.
 */
public class AppFontEditText extends EditText{
    public AppFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/lato.ttf"));

    }
}

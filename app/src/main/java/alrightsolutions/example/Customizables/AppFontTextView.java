package alrightsolutions.example.Customizables;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by JohnConnor on 30-Jul-16.
 */
public class AppFontTextView extends TextView {
    public AppFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/lato.ttf"));

    }
}

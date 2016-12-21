package alrightsolutions.example.Customizables;

import android.animation.Animator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.ViewGroup;

/**
 * Created by JohnConnor on 10-Oct-16.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomAnimation extends Transition {
    @Override
    public void captureStartValues(TransitionValues transitionValues) {

    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {

    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return super.createAnimator(sceneRoot, startValues, endValues);

    }
}

package alrightsolutions.example.Customizables;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * Created by JohnConnor on 29-Sep-16.
 */

public class OverlapDecoration extends RecyclerView.ItemDecoration {
    public OverlapDecoration() {
        super();
    }

    private final static int vertOverlap = -90;

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();


        final int position = params.getViewAdapterPosition();

        if (position > 0) {
            outRect.set(0, vertOverlap, 0, 0);

        } else {
            outRect.setEmpty();
        }

    }

}
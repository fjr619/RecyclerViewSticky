package android.support.v7.widget;

public class RecyclerViewHelper {

    public static int convertPreLayoutPositionToPostLayout(RecyclerView recyclerView, int position) {
        return recyclerView.mRecycler.convertPreLayoutPositionToPostLayout(position);
    }
}
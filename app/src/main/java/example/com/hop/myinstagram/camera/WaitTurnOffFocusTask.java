package example.com.hop.myinstagram.camera;

import android.os.AsyncTask;

import example.com.hop.myinstagram.utils.Wait;

/**
 * Created by Hop on 10/07/2015.
 */
public class WaitTurnOffFocusTask extends AsyncTask<Void, Void, Void> {
    DrawingView drawingView;

    public WaitTurnOffFocusTask(DrawingView drawingView) {
        this.drawingView = drawingView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Wait.oneSec();
        drawingView.setAfterFocus(true);
        drawingView.setFocusFinished(false);
        return null;
    }
}

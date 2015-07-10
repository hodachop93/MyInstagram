package example.com.hop.myinstagram.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera.Face;
import android.util.Log;
import android.view.View;

/**
 * Created by Hop on 10/07/2015.
 */
public class DrawingView extends View {
    boolean haveFace;
    Paint paint;
    boolean onTouch;
    boolean focusFinished;
    boolean afterFocus;
    float x, y;

    Face[] detectedFaces;

    public DrawingView(Context mContext) {
        super(mContext);
        haveFace = false;
        onTouch = false;
        focusFinished = false;
        afterFocus = false;

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    public void setFocusFinished(boolean finished) {
        this.focusFinished = finished;
    }

    public void setHaveFace(boolean haveFace) {
        this.haveFace = haveFace;
    }

    public void setOnTouch(boolean onTouch) {
        this.onTouch = onTouch;
    }

    public void setDetectedFaces(Face[] detectedFaces) {
        this.detectedFaces = detectedFaces;
    }

    public void setAfterFocus(boolean afterFocus) {
        this.afterFocus = afterFocus;
    }

    public void setCoordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (haveFace) {
            drawFaceRectangle(canvas, detectedFaces);
        }

        if (onTouch) {
            drawFocusCircle(canvas);
        }

    }

    private void drawFocusCircle(Canvas canvas) {
        if (focusFinished) {
            paint.setColor(Color.BLUE);
        } else if (afterFocus) {
            paint.setColor(Color.TRANSPARENT);

        } else {
            paint.setColor(Color.WHITE);
        }
        canvas.drawCircle(x, y, 60, paint);

    }

    private void drawFaceRectangle(Canvas canvas, Face[] detectedFaces) {
        if (detectedFaces.length == 0)
            return;

        paint.setColor(Color.GREEN);
        //Toa do cua driver camera trong may se nam trong khoang tu (-1000,-1000) to (1000,1000)
        //Con toa do cua UI do ta design nam tu (0,0) den (width)

        //Vi chung ta da xoay camera preview screen theo huong doc doc nen width thay doi thanh height va nguoc lai
        int viewWidth = getHeight(); //width o day dc xet cho man hinh nam ngang, vd = 960
        int viewHeight = getWidth(); //height o day dc xet cho man hinh nam ngang, vd = 750

        for (int i = 0; i < detectedFaces.length; i++) {
            //Xac dinh toa do hinh chu nhat nhan dien khuon mat, toa do nay o trong 1 o vuong [2000,2000]
            int left = detectedFaces[i].rect.left;
            int top = detectedFaces[i].rect.top;
            int right = detectedFaces[i].rect.right;
            int bottom = detectedFaces[i].rect.bottom;

            //Chuyen toa do sang he toa do tuong ung voi kich thuoc cua man hinh preview cua chung ta
            left = (left + 1000) * viewWidth / 2000;
            top = (top + 1000) * viewHeight / 2000;
            right = (right + 1000) * viewWidth / 2000;
            bottom = (bottom + 1000) * viewHeight / 2000;
            Log.d("DetectFace", " " + left + " " + top + " " + right + " " + bottom + " " + detectedFaces.length);

            //Chuyen width height cua man hinh thanh thang dung.
            int new_vWidth = viewHeight; //width o day cho man hinh thang dung vd = 720
            int new_vHeight = viewWidth; //height o day cho man hinh nam ngang vd = 960

            //Xoay cac toa do lai theo huong thang dung
            int l = bottom;
            int t = left;
            int r = top;
            int b = right;

            //Chuyen goc toa do sang ben trai, vi khi xoay xong thi goc toa do dang nam ben phai
            int new_left = new_vWidth - l;
            int new_right = new_vWidth - r;
            int new_top = t;
            int new_bottom = b;

            canvas.drawRect(new_left, new_top, new_right, new_bottom, paint);
        }

    }


}

package example.com.hop.myinstagram.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 26/06/2015.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraPreview.class.getSimpleName();
    private SurfaceHolder mSurfaceHolder;
    private Size mPreviewSize;
    private Size mPictureSize;
    private Camera mCamera;
    private List<Size> mSupportedPreviewSizes;
    private List<Size> mSupportedPictureSizes;
    private Context mContext;
    private static int orientation;
    private List<String> supportedFlashModes;
    private Face[] detectedFaces;
    private DrawingView drawingView;
    private RelativeLayout mCameraPreview;

    private FaceDetectionListener faceDetectionListener;
    private Camera.AutoFocusCallback autoFocusCallback;
    

    private float focusAreaSize = FOCUS_AREA_SIZE;

    public static final float FOCUS_AREA_SIZE = 20f;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }


    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPictureSizes = mCamera.getParameters().getSupportedPictureSizes();
            requestLayout();

            Activity mActivity = (Activity) mContext;
            mCameraPreview = (RelativeLayout) mActivity.findViewById(R.id.cameraPreview);
            if (drawingView == null) {
                drawingView = new DrawingView(mContext);
                mCameraPreview.addView(drawingView);
            }


            drawingView.invalidate();

            createTouchToFocus();
            createFaceDetection();

            mCamera.startFaceDetection();
            /*// get Camera parameters
            Camera.Parameters params = mCamera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();

            //Set focus mode is auto focus
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                mCamera.setParameters(params);
            }*/
            Log.d(TAG, "setCamera duoc goi");
        }
    }

    private void createTouchToFocus() {
        autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    drawingView.setFocusFinished(true);
                    drawingView.invalidate();
                    WaitTurnOffFocusTask task = new WaitTurnOffFocusTask(drawingView);
                    task.execute();
                }
            }
        };

        mCameraPreview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    drawingView.setOnTouch(true);
                    drawingView.setFocusFinished(false);
                    drawingView.setAfterFocus(false);
                    float x = event.getX();
                    float y = event.getY();

                    mCamera.cancelAutoFocus();

                    Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
                    Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f);

                    Camera.Parameters params = mCamera.getParameters();

                    List<Camera.Area> focusList = new ArrayList<Camera.Area>();
                    focusList.add(new Camera.Area(focusRect, 1000));

                    List<Camera.Area> meteringList = new ArrayList<Camera.Area>();
                    meteringList.add(new Camera.Area(meteringRect, 1000));

                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    params.setFocusAreas(focusList);
                    params.setMeteringAreas(meteringList);

                    mCamera.setParameters(params);
                    mCamera.autoFocus(autoFocusCallback);

                    drawingView.setCoordinate(x, y);
                    drawingView.invalidate();
                }
                    return true;
            }
        });
    }

    private void createFaceDetection() {
        //Khoi tao FaceDetectionListener
        faceDetectionListener = new FaceDetectionListener() {
            @Override
            public void onFaceDetection(Face[] faces, Camera camera) {
                if (faces.length == 0) {
                    drawingView.setHaveFace(false);
                } else {
                    drawingView.setHaveFace(true);
                    drawingView.setDetectedFaces(faces);
                    detectedFaces = faces;

                    //Set focus area using the first detected face
                    List<Camera.Area> focusList = new ArrayList<Camera.Area>();
                    for (int i = 0; i < detectedFaces.length; i++) {
                        Camera.Area face = new Camera.Area(faces[i].rect, 1);
                        focusList.add(face);
                    }
                        /*Camera.Area firstFace = new Camera.Area(faces[0].rect, 1);
                        focusList.add(firstFace);*/

                    Camera.Parameters params = mCamera.getParameters();
                    int maxface = params.getMaxNumDetectedFaces();
                    int maxFocus = params.getMaxNumFocusAreas();
                    int maxMetering = params.getMaxNumMeteringAreas();


                    if (params.getMaxNumFocusAreas() > 0)
                        params.setFocusAreas(focusList);

                    List<Camera.Area> meteringList = new ArrayList<Camera.Area>();
                    meteringList.add(new Camera.Area(faces[0].rect, 1));
                    if (params.getMaxNumMeteringAreas() > 0)
                        params.setMeteringAreas(meteringList);

                    mCamera.setParameters(params);

                }
                drawingView.invalidate();
            }
        };
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = width * 4 / 3;
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, height, width);
            mPictureSize = getOptimalPictureSize(mSupportedPictureSizes, height, width);
        }


        Log.d(TAG, "onMeasure duoc goi");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                setCameraDisplayOrientation((Activity) mContext, 0, mCamera);
                getFlashSupported();
                mCamera.setFaceDetectionListener(faceDetectionListener);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
        Log.d(TAG, "surfaceCreated duoc goi");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mSurfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            if (mCamera != null) {
                Camera.Parameters parameters = mCamera.getParameters();
                int widthPreview, heightPreview, widthPicture, heightPicture;
                /*
                Các điện thoại có camera nằm ngang trong khi máy thì thẳng đứng.
                do đó để preview đúng với hướng nhìn thì chúng ta đã quay nó đi 90 độ,
                theo hướng này thì width < height.
                size gốc của camera sẽ theo hướng năm ngang nên width of camera > height of camera
                 */

                widthPreview = mPreviewSize.width;
                heightPreview = mPreviewSize.height;

                widthPicture = mPictureSize.width;
                heightPicture = mPictureSize.height;

                parameters.setPreviewSize(widthPreview, heightPreview);
                parameters.setPictureSize(widthPicture, heightPicture);
                parameters.setRotation(orientation);
                mCamera.setParameters(parameters);
            }
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            //mCamera.autoFocus(autoFocusCallback);
            mCamera.startFaceDetection();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        Log.d(TAG, "surfacedChanged duoc goi");
    }

    /**
     * Chú ý: Vì camera của máy thường nằm ngang nên width > height, do đó nếu ứng dụng của ta là portrait thì
     * width preview > height preview ==> khi truyền vào thì ta sẽ truyền thành w = height preview, h = width preview
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * Chú ý: Vì camera của máy thường nằm ngang nên width > height, do đó nếu ứng dụng của ta là portrait thì
     * width preview > height preview ==> khi truyền vào thì ta sẽ truyền thành w = height preview, h = width preview
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Size getOptimalPictureSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        //Orientation of camera
        orientation = result;
        camera.setDisplayOrientation(result);


    }

    public static int getOrientationOfCamera() {
        return orientation;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            mCamera.release();
        }
    }

    private void getFlashSupported() {
        Camera.Parameters parameters = mCamera.getParameters();
        supportedFlashModes = parameters.getSupportedFlashModes();
    }

    public void turnOffFlash() {
        if (supportedFlashModes != null) {
            //Tat flash neu duoc
            if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
        }
    }

    public void turnOnFlash() {
        if (supportedFlashModes != null) {
            //Bat flash neu duoc
            if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * Convert touch position x:y to {@link Camera.Area} position -1000:-1000 to 1000:1000.
     */
    private Rect calculateTapArea(float x, float y, float coefficient) {

        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int left = clamp((int) x - areaSize / 2, 0, mCameraPreview.getWidth() - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, mCameraPreview.getHeight() - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        int width = mCameraPreview.getWidth();
        int height = mCameraPreview.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        matrix.postScale(width / 2000f, height / 2000f);
        matrix.postTranslate(width / 2f, height / 2f);
        matrix.invert(matrix);
        matrix.mapRect(rectF);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

}

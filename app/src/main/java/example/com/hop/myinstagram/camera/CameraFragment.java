package example.com.hop.myinstagram.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import example.com.hop.myinstagram.R;

/**
 * Created by Hop on 25/06/2015.
 */
public class CameraFragment extends Fragment {
    private static final String TAG = Fragment.class.getSimpleName();
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private ImageView btnCapture;
    RelativeLayout cameraPreviewLayout;
    RelativeLayout shutterCamera;
    LinearLayout topbar;
    ImageView changeCameraBtn, flashImgView;

    private OrientationEventListener mOrientationEventListener;
    private int orientationOfImage;

    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //Goc ma anh can quay de tra lai dung huong
            int rotation = orientationOfImage + CameraPreview.getOrientationOfCamera();
            saveImageTask(data, rotation);
            resetCam();

            Intent intent = new Intent(getActivity(), FilterPhotoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putByteArray("data", data);
            bundle.putInt("rotation", rotation);
            intent.putExtra("ImagePackage", bundle);
            startActivity(intent);

        }
    };


    private void resetCam() {
        mCamera.startPreview();
        mCameraPreview.setCamera(mCamera);
    }

    private void saveImageTask(byte[] data, int rotation) {
        SaveImageTask task = new SaveImageTask(this.getActivity());
        task.execute(data, rotation);
    }


    FrameLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screen_width = displaymetrics.widthPixels;
        int screen_height = displaymetrics.heightPixels;
        int statusBar_height = getStatusBarHeight();
        View rootView = inflater.inflate(R.layout.photo_camera_fragment, null, false);

        flashImgView = (ImageView) rootView.findViewById(R.id.camera_flash);

        topbar = (LinearLayout) rootView.findViewById(R.id.topbar);
        RelativeLayout.LayoutParams topbarLayoutParams = (RelativeLayout.LayoutParams) topbar.getLayoutParams();

        cameraPreviewLayout = (RelativeLayout) rootView.findViewById(R.id.cameraPreview);
        RelativeLayout.LayoutParams prviewLayoutParams = (RelativeLayout.LayoutParams) cameraPreviewLayout.getLayoutParams();
        prviewLayoutParams.height = screen_width * 4 / 3;

        shutterCamera = (RelativeLayout) rootView.findViewById(R.id.shutter_camera);
        RelativeLayout.LayoutParams shutterLayoutParams = (RelativeLayout.LayoutParams) shutterCamera.getLayoutParams();
        shutterLayoutParams.height = screen_height - screen_width - screen_height / 12 - statusBar_height - topbarLayoutParams.height;
        cameraPreviewLayout.requestLayout();

        TextView txtView = (TextView) rootView.findViewById(R.id.title);
        txtView.setText("PHOTO");

        ImageView left_navigation = (ImageView) rootView.findViewById(R.id.left_navigation);
        left_navigation.setImageResource(R.drawable.nav_cancel);
        left_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        layout = (FrameLayout) rootView.findViewById(R.id.layout_camera);

        // Create camera preview instance annd display it
        mCameraPreview = new CameraPreview(this.getActivity(), mCamera);
        mCameraPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(mCameraPreview);
        mCameraPreview.setKeepScreenOn(true);

        btnCapture = (ImageView) rootView.findViewById(R.id.btn_capture);

        return rootView;
    }

    private void initialize() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });
        mCamera = getCameraInstance();
        mCamera.startPreview();
        mCameraPreview.setCamera(mCamera);

        mOrientationEventListener = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                orientationOfImage = round(orientation);
            }
        };

        mOrientationEventListener.enable();

        if (mCamera == null) {
            Toast.makeText(this.getActivity(), "No camera detected!!!", Toast.LENGTH_LONG).show();
            return;
        }

        flashImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flashImgView.isSelected()){
                    flashImgView.setSelected(false);
                    mCameraPreview.turnOffFlash();
                } else{
                    flashImgView.setSelected(true);
                    mCameraPreview.turnOnFlash();
                }
            }
        });


        //Kiem tra tinh trang flash khi resume
        if (flashImgView.isSelected()){
            mCameraPreview.turnOnFlash();

        } else{
            mCameraPreview.turnOffFlash();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private int round(int orientation) {
        if (orientation > 315 || orientation <= 45)
            return 0;
        else if (orientation > 45 && orientation <= 135)
            return 90;
        else if (orientation > 135 && orientation <= 225)
            return 180;
        else
            return 270;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            mCameraPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        mOrientationEventListener.disable();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        int numOfCamera = Camera.getNumberOfCameras();
        if (numOfCamera > 0) {
            try {
                c = Camera.open(0); // attempt to get a Camera instance. 0 is back camera
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
            }
        }

        return c; // returns null if camera is unavailable
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}

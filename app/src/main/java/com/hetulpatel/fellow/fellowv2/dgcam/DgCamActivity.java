package com.hetulpatel.fellow.fellowv2.dgcam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.hetulpatel.fellow.fellowv2.FontTextView;
import com.hetulpatel.fellow.fellowv2.R;
import com.hetulpatel.fellow.fellowv2.drawer.DrawerMain;
import com.hetulpatel.fellow.fellowv2.driveservice.BaseDemoActivity;
import com.hetulpatel.fellow.fellowv2.driveservice.CreateFileInFolderActivity;
import com.hetulpatel.fellow.fellowv2.driveservice.CreateFolderActivity;
import com.hetulpatel.fellow.fellowv2.model.ComplexPreferences;
import com.hetulpatel.fellow.fellowv2.model.TinyDB;
import com.hetulpatel.fellow.fellowv2.model.user;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

import static com.hetulpatel.fellow.fellowv2.driveservice.BaseDemoActivity.EXISTING_FOLDER_ID;

public class DgCamActivity extends Activity implements SensorEventListener {
	private Camera mCamera;
	private CameraPreview mPreview;
	private SensorManager sensorManager = null;
	private int orientation;
	private ExifInterface exif;
	private int deviceHeight;
	private Button back;
	private FontTextView title;
	private Button ibCapture;
	private FrameLayout flBtnContainer;
	private File sdRoot;
	private String dir;
	private String fileName;
	private ImageView rotatingImage;
	private int degrees = -1;
    private String course;
    private int semester;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();

        course= bundle.getString("title");

        // Setting all the path for the image
		sdRoot = Environment.getExternalStorageDirectory();

		semester = new TinyDB(DgCamActivity.this).getInt("semester");
		dir = "/fellow/sem"+semester+"/"+course.toLowerCase()+"/";


		ibCapture = (Button) findViewById(R.id.ibCapture);
        back = (Button) findViewById(R.id.back);
		flBtnContainer = (FrameLayout) findViewById(R.id.flBtnContainer);
        title = (FontTextView) findViewById(R.id.camera_title);

        title.setText(course);

		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Selecting the resolution of the Android device so we can create a
		// proportional preview
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		deviceHeight = display.getHeight();

		// Add a listener to the Capture button
		ibCapture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCamera.takePicture(null, null, mPicture);
			}
		});

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createrPDF();
				/*Intent i = new Intent(DgCamActivity.this,CreateFileInFolderActivity.class);
				startActivity(i);*/
				finish();

            }
    });


	}

    private void createrPDF() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code

                String path = Environment.getExternalStorageDirectory().toString()+"/fellow/sem"+semester;
                Log.d("PATH: ", path);
                File dir = new File(path +"/"+course.toLowerCase());
                List<File> files = getListFiles(dir);


                if(files.size()>0) {

                    try {

                        Document document=new Document();

                        PdfWriter.getInstance(document,new FileOutputStream(path+"/pdfs/"+course.toLowerCase()+".pdf"));
                        document.open();

                        int indentation = 0;

                        Log.d("FILES NUMBER", files.size()+"");
						Image image = Image.getInstance (files.get(0).getAbsolutePath());


                        for (int i = 0; i < files.size(); i++) {
							image = Image.getInstance(files.get(i).getAbsolutePath());
							Log.d("FILE" + i,files.get(i).getAbsolutePath());

							/*float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                    - document.rightMargin() - indentation) / image.getWidth()) * 100;
                            image.scalePercent(scaler);*/
							image.setRotationDegrees(-90);
							document.setPageSize(image.rotate());
							document.newPage();
                            image.setAbsolutePosition(0,0);
							document.add(image);
                        }

                        document.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }  catch (DocumentException e2){
                        e2.printStackTrace();
                    }

                }
            }
        });
    }

    private List<File> getListFiles(File parentDir) {
        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().endsWith(".jpg")) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }





    private void createCamera() {
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Setting the right parameters in the camera
		Camera.Parameters params = mCamera.getParameters();
		params.setPictureSize(1280, 720);
		params.setPictureFormat(PixelFormat.JPEG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            params.setJpegQuality(85);
        }
        mCamera.setParameters(params);

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

		// Calculating the width of the preview so it is proportional.
		float widthFloat = (float) (deviceHeight) * 16 / 9;
		int width = Math.round(widthFloat);

		// Resizing the LinearLayout so we can make a proportional preview. This
		// approach is not 100% perfect because on devices with a really small
		// screen the the image will still be distorted - there is place for
		// improvment.
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(deviceHeight-200, width-200);
		preview.setLayoutParams(layoutParams);

		// Adding the camera preview after the FrameLayout and before the button
		// as a separated element.
		preview.addView(mPreview, 0);
	}


	@Override
	protected void onResume() {
		super.onResume();

		// Test if there is a camera on the device and if the SD card is
		// mounted.
		if (!checkCameraHardware(this)) {
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		} else if (!checkSDCard()) {
			Intent i = new Intent(this, NoSDCard.class);
			startActivity(i);
			finish();
		}

		// Creating the camera
		createCamera();

		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// release the camera immediately on pause event
		releaseCamera();

		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.removeViewAt(0);
		createrPDF();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private boolean checkSDCard() {
		boolean state = false;

		String sd = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sd)) {
			state = true;
		}

		return state;
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			// attempt to get a Camera instance
			c = Camera.open();
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}

		// returns null if camera is unavailable
		return c;
	}

	private PictureCallback mPicture = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

			// Replacing the button after a photho was taken.

			// File name of the image that we just took.
			fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";

			// Creating the directory where to save the image. Sadly in older
			// version of Android we can not get the Media catalog name
			File mkDir = new File(sdRoot, dir);
			mkDir.mkdirs();

			// Main file where to save the data that we recive from the camera
			File pictureFile = new File(sdRoot, dir + fileName);

			try {
				FileOutputStream purge = new FileOutputStream(pictureFile);
				purge.write(data);
				purge.close();
			} catch (FileNotFoundException e) {
				Log.d("DG_DEBUG", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
			}

			// Adding Exif data for the orientation. For some strange reason the
			// ExifInterface class takes a string instead of a file.
			try {
				exif = new ExifInterface("/sdcard/" + dir + fileName);
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + orientation);
				exif.saveAttributes();

                Toast.makeText(DgCamActivity.this,"Saved",Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * Putting in place a listener so we can get the sensor data only when
	 * something changes.
	 */
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
					} else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
					} else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
					}
				}

			}

		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 * 
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation(float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	/**
	 * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}


	private class Uploadfile extends BaseDemoActivity {

		private DriveId mFolderDriveId;
		private SharedPreferences pref;

		@Override
		public void onConnected(Bundle connectionHint) {
			super.onConnected(connectionHint);

			Log.d("ON CONNECTED","TRUE");

			pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

			Drive.DriveApi.fetchDriveId(getGoogleApiClient(), pref.getString("folderid",""))
					.setResultCallback(idCallback);
		}

		final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
			@Override
			public void onResult(DriveApi.DriveIdResult result) {
				if (!result.getStatus().isSuccess()) {
					showMessage("Cannot find DriveId. Are you authorized to view this file?");
					return;
				}
				mFolderDriveId = result.getDriveId();
				Drive.DriveApi.newDriveContents(getGoogleApiClient())
						.setResultCallback(driveContentsCallback);
			}
		};

		final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
				new ResultCallback<DriveApi.DriveContentsResult>() {
					@Override
					public void onResult(DriveApi.DriveContentsResult result) {
						if (!result.getStatus().isSuccess()) {
							showMessage("Error while trying to create new file contents");
							return;
						}
						DriveFolder folder = mFolderDriveId.asDriveFolder();
						MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
								.setTitle("New file")
								.setMimeType("text/plain")
								.setStarred(true).build();
						folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
								.setResultCallback(fileCallback);
					}
				};

		final private ResultCallback<DriveFolder.DriveFileResult> fileCallback =
				new ResultCallback<DriveFolder.DriveFileResult>() {
					@Override
					public void onResult(DriveFolder.DriveFileResult result) {
						if (!result.getStatus().isSuccess()) {
							showMessage("Error while trying to create the file");
							return;
						}
						showMessage("Created a file: " + result.getDriveFile().getDriveId());
					}
				};
	}

}
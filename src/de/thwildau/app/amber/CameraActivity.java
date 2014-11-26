package de.thwildau.app.amber;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class CameraActivity extends Activity {
	int TAKE_PHOTO_CODE = 0;
	public static int count=0;
	private static final int PICK_IMAGE = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		//here,we are making a folder named picFolder to store pics taken by the camera using this application
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/"; 
		File newdir = new File(dir); 
		newdir.mkdirs();

		Button capture = (Button) findViewById(R.id.btnCapture);
		capture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
				count++;
				String file = dir+count+".jpg";
				File newfile = new File(file);
				try {
					newfile.createNewFile();
				} catch (IOException e) {}       
				
///*keine ahnung was das machen soll*/				
//				Intent intent = new Intent(Intent.ACTION_PICK);
//			    intent.setType("image/*");
//			    startActivityForResult(intent, SELECT_PICTURE);			    
///*open media*/
//				Intent intent = new Intent();
//				intent.setType("image/*");
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
///*open camera*/
//				Uri outputFileUri = Uri.fromFile(newfile);
//				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
				
				
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ImageView imageView2;
		imageView2 = (ImageView)findViewById(R.id.imageView2);


		if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			//        Uri _uri = data.getData();
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			//        //User had pick an image.
			//        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
			//        cursor.moveToFirst();
			//
			//        //Link to the image
			//        final String imageFilePath = cursor.getString(0);
			//        cursor.close();
		}
		//    super.onActivityResult(requestCode, resultCode, data);
		//
		//    if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
		//        Log.d("CameraDemo", "Pic saved");


	}
}

//}

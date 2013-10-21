package a.ourtexteditor;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditorpageActivity extends Activity implements
		OnItemSelectedListener {

	Spinner s1;
	EditText box;
	
	String FILENAME = "Untitled" ;
	String string="";
	String menu[] = { "Menu", "Save", "mail", "Quit", "Clear all", "new",
			"open" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editorpage);
		s1 = (Spinner) findViewById(R.id.spinner1);
		box = (EditText) findViewById(R.id.editText1);
		
		s1.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, menu));
		s1.setOnItemSelectedListener(this);
		
	}


	public void checkandsave() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {

			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (mExternalStorageAvailable == mExternalStorageWriteable
				&& mExternalStorageAvailable == true) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path, FILENAME+".txt");
			try {
				// Make sure the Pictures directory exists.
				path.mkdirs();
				
				OutputStream os = new FileOutputStream(file);
				os.write(string.getBytes());
				os.close();
				MediaScannerConnection.scanFile(this,
						new String[] { file.toString() }, null,
						new MediaScannerConnection.OnScanCompletedListener() {
							public void onScanCompleted(String path, Uri uri) {
								Log.i("ExternalStorage", "Scanned " + path
										+ ":");
								Log.i("ExternalStorage", "-> uri=" + uri);

							}

						});
				if (file.exists()) {
					
					Toast t = Toast.makeText(this, "File has been Saved",
							Toast.LENGTH_LONG);
					t.show();
				} else {
					Toast t1 = Toast.makeText(this, "Couldn't save the file",Toast.LENGTH_LONG);
					t1.show();
				}
			} catch (IOException e) {
				// Unable to create file, likely because external storage is
				// not currently mounted.
				Log.w("ExternalStorage", "Error writing " + file, e);

			}

		}
	}

	public void checkandopen()
	{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		String content="";
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {

			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (mExternalStorageAvailable == mExternalStorageWriteable
				&& mExternalStorageAvailable == true) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path, FILENAME+".txt");
			if(file.exists()) try {
	    		InputStream istream = new FileInputStream(file);
				InputStreamReader inputreader = new InputStreamReader(istream);
				BufferedReader bufferedreader = new BufferedReader(inputreader);
				String line;
				while((line = bufferedreader.readLine()) != null) {
					content += line + "\n";
				}
	    		istream.close();
	    		
	    	} catch (IOException e) {
				
	    		e.printStackTrace();
	    		
	    	}
    	} else {
    		
    	}
    	box.setText(content);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editorpage, menu);
		return true;
	}

	public void calldialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.dialog_layout, null))
				.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								calldialog2();
								
							}
						})
				.setNeutralButton("Dont save",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();

							}
						}); 
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	public void calldialog2()
	{ final Dialog dialog2= new Dialog(this);
	dialog2.setContentView(R.layout.dialog2);
	
	
	final EditText namebox = (EditText) dialog2.findViewById(R.id.filename);
	final Button confirm = (Button) dialog2.findViewById(R.id.b1);
	final Button Cancel = (Button) dialog2.findViewById(R.id.b2);
	confirm.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	FILENAME = namebox.getText().toString();
			if(FILENAME.equals(""))
			{
				FILENAME = "Untitled";
			}
			checkandsave();
			
            
        }
         });
	Cancel.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog2.cancel();
			
		}
	});
	dialog2.setTitle("Save");
				
	
	dialog2.show();
		
	}
	public void calldialog3()
	{
		final Dialog dialog2= new Dialog(this);
		dialog2.setContentView(R.layout.dialog2);
		
		
		final EditText namebox = (EditText) dialog2.findViewById(R.id.filename);
		final Button confirm = (Button) dialog2.findViewById(R.id.b1);
		final Button Cancel = (Button) dialog2.findViewById(R.id.b2);
		confirm.setOnClickListener(new View.OnClickListener(){

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	FILENAME = namebox.getText().toString();
				if(FILENAME.equals(""))
				{
					FILENAME = "Untitled";
				}
				checkandsave();
				
	            
	        }
	         });
		Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.cancel();
				
			}
		});
		dialog2.setTitle("Save");
		
					
		
		dialog2.show();
		
		finish();
	}
	public void calldialog4()
	{	
		checkandopen();
		final Dialog dialog2= new Dialog(this);
		dialog2.setContentView(R.layout.dialog2);
		
		
		final EditText namebox = (EditText) dialog2.findViewById(R.id.filename);
		final Button confirm = (Button) dialog2.findViewById(R.id.b1);
		final Button Cancel = (Button) dialog2.findViewById(R.id.b2);
		confirm.setOnClickListener(new View.OnClickListener(){

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	FILENAME = namebox.getText().toString();
							
				checkandopen();
	            
	        }
	         });
		Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2.cancel();
				
			}
		});
		dialog2.setTitle("Open");
		
					
		
		dialog2.show();

		
		
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		int pos = s1.getSelectedItemPosition();
		// TODO Auto-generated method stub
		switch (pos) {
		case 0:
			break;
		case 1:string = box.getText().toString();
		calldialog2();
			break;
		case 2:
			break;
		case 3:calldialog3();
	
		break;
		case 4:
			box.setText(" ");
			break;
		case 5:calldialog1();
		
		box.setText(" ");
		break;
		case 6:calldialog4();
			
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

	}

}

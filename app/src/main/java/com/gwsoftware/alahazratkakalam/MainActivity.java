package com.gwsoftware.alahazratkakalam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.BuildConfig;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.gwsoftware.alahazratkakalam.activity.BooksListActivity;
import com.gwsoftware.alahazratkakalam.activity.HamaraIslamActivity;
import com.gwsoftware.alahazratkakalam.activity.NoteActivity;
import com.gwsoftware.alahazratkakalam.activity.QuranActivity;
import com.gwsoftware.alahazratkakalam.adapter.MyListAdapter;
import com.gwsoftware.alahazratkakalam.asyncktask.GetNewVersionCode;
import com.gwsoftware.alahazratkakalam.fragments.CollectionsFragment;
import com.gwsoftware.alahazratkakalam.fragments.HomeFragment;
import com.gwsoftware.alahazratkakalam.interfaces.AlertCallback;
import com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.AppUpdate;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, recyclerViewCallback {

    DatabaseReference databaseReference;
    AhApplication ahApplication;
    FirebaseDatabase firebaseDatabase;
    AdView adView;
    ProgressBar progressBar;
    Fragment fragment = null;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ahApplication = AhApplication.getInstance();
        navigation = findViewById(R.id.navigation);
        progressBar = findViewById(R.id.progress_circular);
        navigation.setOnNavigationItemSelectedListener(MainActivity.this);
        adView = findViewById(R.id.adView);
        Utils.loadAdView(this, adView);
        new AppUpdate().checkAppUpdate(this);




        /*ApiUtil.getServiceClass().getAllPost().enqueue(new Callback<List<ApiObject>>() {

            @Override
            public void onResponse(Call<List<ApiObject>> call, Response<List<ApiObject>> response) {
                if(response.isSuccessful()){
                    List<ApiObject> postList = response.body();
                    Log.e("Response...", "Returned count " + postList.size());
                   // NewAdapter adapter = new NewAdapter(getApplicationContext(), postList);
                   // recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ApiObject>> call, Throwable t) {
                //showErrorMessage();
                Log.d("Response", "error loading from API");
            }

        });*/
        //   new DownloadingTask().execute();
        new GetNewVersionCode(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isDataLoaded) {
            loadDataFromFirebase();
            //parseDataAndLoadFragment(Utils.readFileFromJson(this,"aalahazrat copy.json"));
        }
    }


    boolean isDataLoaded = false;

    private void loadDataFromFirebase() {
        try {
            isDataLoaded = true;
            if (firebaseDatabase == null) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.setPersistenceEnabled(true);
                databaseReference = firebaseDatabase.getReference();
            }
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    parseDataAndLoadFragment(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                    Log.e("onCancelled", "Failed to read value.", error.toException());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDataAndLoadFragment(Object resultData) {
        try {

            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(gson.toJson(resultData));
            //JSONObject jsonObject = new JSONObject((resultData));
            if (jsonObject != null && jsonObject.has("data")) {
                DataObjectModel dataObjectModel = gson.fromJson(jsonObject.getJSONObject("data").toString(), DataObjectModel.class);
                ahApplication.setDataObjectModel(dataObjectModel);
                MyListAdapter myListAdapter = new MyListAdapter(MainActivity.this, dataObjectModel);
                loadFragment(new HomeFragment());
                progressBar.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new CollectionsFragment();
                break;


        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void recyclerViewItemClicked(int position, View view) {
        String tag = (String) view.getTag().toString().trim();
        switch (tag) {
            case "Quran":
                Intent quranIntent = new Intent(this, QuranActivity.class);
                startActivity(quranIntent);
                break;
            case "Books":
                Intent intent = new Intent(this, BooksListActivity.class);
                intent.putExtra("category", "books");
                startActivity(intent);
                break;
            case "Hamara Islam":
                Intent intent1 = new Intent(MainActivity.this, HamaraIslamActivity.class);
                intent1.putExtra("category", Constants.HAMARA_ISLAM);
                startActivity(intent1);
                break;
            case "More...":
                //Utils.showAlert(this, "Comming Soon...", null, false, "Ok");

                Intent calenderIntent = new Intent(MainActivity.this, BooksListActivity.class);
                calenderIntent.putExtra("category", "more");
                startActivity(calenderIntent);

                break;
            case "Contact Us":
                try {
                    Intent contactUsInent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "aalahazratkakalam@gmail.com"));
                    contactUsInent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    //intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(contactUsInent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Share App":
                shareApp();
                break;
            case "Naats":
                Intent naatsIntent = new Intent(MainActivity.this, HamaraIslamActivity.class);
                naatsIntent.putExtra("category", Constants.NAATS);
                startActivity(naatsIntent);
                break;
            case "Note":
                Intent noteIntent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(noteIntent);
                break;
            case "Speeches":
                Utils.showAlert(this, "Please download the new application to get this", new AlertCallback() {
                    @Override
                    public void alertCallback(boolean value) {
                        final String appPackageName = "com.mfsoftware.alahazratkakalam"; // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                }, false, "Download new App");
                break;
            default:
                Utils.showAlert(this, "Please update the app...", null, false, "Ok");
                break;
        }

    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
            e.printStackTrace();
        }
    }

    private static class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        ProgressDialog mProgressDialog;

        public DownloadTask(Context context) {
            this.context = context;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/testing");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

       /* @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }*/

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Backup Downloaded", Toast.LENGTH_SHORT).show();
            Intent mStartActivity = new Intent(context, MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment != null && fragment instanceof CollectionsFragment) {
                fragment = new HomeFragment();
                navigation.setSelectedItemId(R.id.navigation_home);
                loadFragment(fragment);
            } else {
                Utils.showAlert(this, "Do You Want To Exit ?", new AlertCallback() {
                    @Override
                    public void alertCallback(boolean value) {

                        if (value) {
                            finishAndRemoveTask();
                        }

                    }
                }, true, "Yes");
            }
        }
        return true;
    }
}



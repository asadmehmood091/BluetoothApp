package signalstrengthmeter;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    Handler h2 = new Handler();
    RecyclerView recyclerview;
    AdapterClass adapter;
    ArrayList<DataModel> itemslist = new ArrayList<>();
    DataModel dataModel;
    Snackbar snackbar;
    LinearLayoutManager mLayoutManager;
    ArrayList<String> mMacAddress = new ArrayList<>();
    boolean isScanning = true;
    private AdView mAdView;
    TextView scanning ;
    ImageView bluetoothicon;
    FloatingActionButton floatingActionButton;
    Animation blinking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        Permissions();

        if(checkLocationPermission()){
//            new AlertDialog.Builder(ListActivity.this)
//                    .setTitle("Location permission")
//                    .setMessage("in order to use Bluetooth meter app device must allow location permissions.")
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //Prompt the user once explanation has been shown
//                            ActivityCompat.requestPermissions(ListActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    MY_PERMISSIONS_REQUEST_LOCATION);
//                        }
//                    })
//                    .create()
//                    .show();
        }

        recyclerview = findViewById(R.id.recyclerview);
        scanning = findViewById(R.id.scanning);
        bluetoothicon = findViewById(R.id.bluetoothIcon);
        floatingActionButton  = findViewById(R.id.floatingbtn);
        blinking = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blinking);
        bluetoothicon.startAnimation(blinking);

        loadAds();
        if(BTAdapter.enable())
        { startListening();
        }else showSnakebar();
    }



    public void startListening() {

//        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 240);
//        startActivityForResult(i,1);

//        if (BTAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            startActivity(discoverableIntent);
//        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BTAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BTAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);
        BTAdapter.startDiscovery();

//        registerReceiver(receiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));

//        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                scanning.append(deviceName+deviceHardwareAddress);
//            }
//        }
        // set discoverable if necessary


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new AdapterClass(getApplicationContext(), itemslist);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
    }




    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(ListActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                return true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dotsitems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                Share();
                break;
            }
            case R.id.action_help: {
                startActivity(new Intent(getApplicationContext(),help.class));
                break;
            }
            case R.id.action_about: {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            }
            case R.id.action_privacy: {
                break;
            }
            case R.id.action_ourApps: {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Sigmax+Applications")));
                break;
            }
        }
        return true;
    }

    public void Share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void PlayPause(View view) {

        if (isScanning) {
            floatingActionButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            isScanning = false;
            clearrecyclerview();
            scanning.setText("Scanning Stoped.");
            scanning.setTextColor(0xFFE82323);
            bluetoothicon.clearAnimation();
            BTAdapter.cancelDiscovery();
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_pause_black_24dp);
            isScanning = true;
            startListening();
            scanning.setText("Scanning started...");
            scanning.setTextColor(0xFF4CAF50);
            BTAdapter.startDiscovery();
            bluetoothicon.startAnimation(blinking);
        }
    }

    void loadAds() {
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    protected void onResume() {

        super.onResume();
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        h2.postDelayed(r2, 0);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    Runnable r2 = new Runnable() {
        @Override
        public void run() {

            h2.postDelayed(r2, 0);
            BTAdapter.startDiscovery();
        }
    };


    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (action.equals(BTAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BTAdapter.EXTRA_SCAN_MODE, BTAdapter.ERROR);

                if (mode == BTAdapter.SCAN_MODE_NONE) {
                    showSnakebar();
                    clearrecyclerview();

                } else if (mode == BTAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    snackbar.dismiss();
                }
            }


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                if (rssi > -90 && rssi < -30) {

                    if (mMacAddress.contains(device.getAddress())) {
                        dataModel = new DataModel(device.getName(), device.getAddress(), rssi + "dBm");
                        itemslist.set(mMacAddress.indexOf(device.getAddress()), dataModel);
                        adapter.notifyItemChanged(mMacAddress.indexOf(device.getAddress()));
                    } else {
                        mMacAddress.add(device.getAddress());
                        updateList(device, rssi);
                    }
                }
            }
        }
    };


    private void Permissions() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    0);
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    1);
        }
    }


    public void updateList(BluetoothDevice device, int rssi) {

        dataModel = new DataModel(device.getName(), device.getAddress(), rssi + "dBm");
        itemslist.add(dataModel);
        adapter.notifyDataSetChanged();
        Snackbar.make(recyclerview, "New Device Found " , Snackbar.LENGTH_SHORT).show();

    }

    void showSnakebar() {
        snackbar = Snackbar.make(recyclerview, "Turn Device bluetooth on", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 000);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    void clearrecyclerview() {
        mMacAddress.clear();
        itemslist.clear();
        adapter.notifyDataSetChanged();
    }


}
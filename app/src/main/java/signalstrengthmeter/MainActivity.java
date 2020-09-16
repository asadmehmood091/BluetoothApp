package signalstrengthmeter;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;

import signalstrengthmeter.database.BluetoothDataSource;

public class MainActivity extends AppCompatActivity implements AdapterClass.ItemClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String TAG = "MainActivity";
    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    InterstitialAd mInterstitialAd;
    private AdView mAdView;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Handler h2 = new Handler();
    RecyclerView recyclerview;
    AdapterClass adapter;
    ArrayList<DataModel> itemslist = new ArrayList<>();
    DataModel dataModel;
    Snackbar snackbar;
    LinearLayoutManager mLayoutManager;
    ArrayList<String> mMacAddress = new ArrayList<>();
    TextView pairedDevice, homedeviceName, homeconnected;
    public ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private BluetoothDataSource mDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("default", 0);
        editor = sharedpreferences.edit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        Permissions();

        mDataSource=new BluetoothDataSource(this);
//        mDataSource.open();
  //      mDataSource.seedDatabase();

        if (checkLocationPermission()) {
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
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        loadAds();
        if (BTAdapter.enable()) {
            startListening();
        } else showSnakebar();

    }

    public void startListening() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BTAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BTAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, filter);
        BTAdapter.startDiscovery();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                return true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
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

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
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
                startActivity(new Intent(getApplicationContext(), help.class));
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

    public void AddNew(View view) {
        customDialog();
//        startActivity(new Intent(getApplicationContext(), ListActivity.class));
    }


    public void Home_CardView(View view) {
        if (sharedpreferences.getBoolean("home_set", false)) {
            sharedpreferences.getString("home_mac", null);

        } else {
            showDevicesDailog();
        }

    }



    void loadAds() {
        mAdView = findViewById(R.id.adMainView);

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
                        devices.add(device);
                        mMacAddress.add(device.getAddress());
                        updateList(device, rssi);
                    }
                }
            }
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(getApplicationContext(),"BOND_BONDED",Toast.LENGTH_SHORT).show();
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(getApplicationContext(),"BOND_BONDING",Toast.LENGTH_SHORT).show();
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(getApplicationContext(),"BOND_NONE",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void updateList(BluetoothDevice device, int rssi) {

        dataModel = new DataModel(device.getName(), device.getAddress(), rssi + "dBm");
        itemslist.add(dataModel);
        adapter.notifyDataSetChanged();
        Snackbar.make(recyclerview, "New Device Found ", Snackbar.LENGTH_SHORT).show();

    }

    public void showDevicesDailog() {
        Dialog mDialog;
        mDialog = new Dialog(MainActivity.this);
        mDialog.setTitle("Home Device");
        mDialog.setContentView(R.layout.show_devices);
        recyclerview = mDialog.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new AdapterClass(getApplicationContext(), itemslist, this);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);
        adapter.setItemClickListener(this);
        if (BTAdapter.enable()) {
            startListening();
        } else showSnakebar();

        // paired device
        pairedDevice = mDialog.findViewById(R.id.pairedDevice);
        homedeviceName = findViewById(R.id.homedeviceName);
        homeconnected = findViewById(R.id.homeconnected);
        if (BTAdapter.isEnabled()) {
            Set<BluetoothDevice> devices = BTAdapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                String devicename = device.getName();
                String macAddress = device.getAddress();
                pairedDevice.setText("\t" + devicename.toUpperCase() + "\t\t" + macAddress);
                homedeviceName.setText(devicename.toUpperCase());
                homeconnected.setText("Connected");
                homeconnected.setTextColor(Color.parseColor("#32CD32"));

            }
        }


        mDialog.show();
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


    public void customDialog() {
        Dialog mDialog;
        mDialog = new Dialog(MainActivity.this);
        mDialog.setContentView(R.layout.custom_layout);
        TextView custom_home_title, custom_car_title, custom_earphone_title, custom_desktop_title, custom_custom_title;
        ImageView custom_home, custom_car, custom_earphone, custom_desktop, custom_custom;
        custom_home_title = mDialog.findViewById(R.id.custom_home_title);
        custom_car_title = mDialog.findViewById(R.id.custom_car_title);
        custom_earphone_title = mDialog.findViewById(R.id.custom_earphone_title);
        custom_desktop_title = mDialog.findViewById(R.id.custom_desktop_title);
        custom_custom_title = mDialog.findViewById(R.id.custom_custom_title);
        custom_home = mDialog.findViewById(R.id.custom_home);
        custom_car = mDialog.findViewById(R.id.custom_car);
        custom_earphone = mDialog.findViewById(R.id.custom_earphone);
        custom_desktop = mDialog.findViewById(R.id.custom_desktop);
        custom_custom = mDialog.findViewById(R.id.custom_custom);

        custom_home_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
            }
        });
        custom_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Home Icon", Toast.LENGTH_SHORT).show();
            }
        });
        custom_car_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Car", Toast.LENGTH_SHORT).show();
            }
        });
        custom_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Car Icon", Toast.LENGTH_SHORT).show();
            }
        });
        custom_earphone_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Earphone", Toast.LENGTH_SHORT).show();
            }
        });
        custom_earphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Earphone Icon", Toast.LENGTH_SHORT).show();
            }
        });

        custom_desktop_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Computer", Toast.LENGTH_SHORT).show();
            }
        });
        custom_desktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked desktop Icon", Toast.LENGTH_SHORT).show();
            }
        });
        custom_custom_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Custom", Toast.LENGTH_SHORT).show();
            }
        });
        custom_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You CLicked Add Icon", Toast.LENGTH_SHORT).show();
            }
        });

        mDialog.show();
    }

    @Override
    public void onItemClick(int position) {
        //first cancel discovery because its very memory intensive.
        BTAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = devices.get(position).getName();
        String deviceAddress = devices.get(position).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with " + deviceName);
            devices.get(position).createBond();
            Toast.makeText(getApplicationContext(), "Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }


}


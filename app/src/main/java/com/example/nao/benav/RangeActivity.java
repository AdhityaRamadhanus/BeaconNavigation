package com.example.nao.benav;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.RemoteException;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.nao.benav.Trilateration.IndoorNav;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;

import java.util.ArrayList;
import java.util.Collection;


public class RangeActivity extends TileViewActivity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<Beacon> Beacons;
    private ArrayList<String> BeaconInfo;
    private ArrayAdapter<String> BeaconsAdapter;

    private ListView Beaconlist;
    //private ArrayList<Double> Distances;
    private double[] distances;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

        /*Initialize Marker View*/
        MarkerView imageView = (MarkerView) findViewById(R.id.mapView);
        imageView.setImage(ImageSource.resource(R.drawable.micello));
        imageView.setPin(new PointF(100f, 100f));

        /*Initialize Beacon List etc */
        Beaconlist = (ListView) findViewById(R.id.list_beacon);
        Beacons = new ArrayList<Beacon>();
        BeaconInfo = new ArrayList<String>();
        BeaconsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,BeaconInfo){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                text.setTextColor(Color.BLACK);

                return view;
            }
        };
        Beaconlist.setAdapter(BeaconsAdapter);

        distances = new double[3];
        distances[0]=0;
        distances[1]=1;
        distances[2]=2;

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    IndoorNav indoorNav = new IndoorNav();
                    //!!!POSISI BEACON MASIH HARDCODE
                    double positions[][] = {{0.0,0.0},{4.0,0.0},{0.0,4.0}};
                    for (Beacon b : beacons) {
                        if (!Beacons.contains(b)) {
                            Beacons.add(b);
                            BeaconInfo.add("Beacon ID : " + b.getId2() + " , Distance : " + b.getDistance());
                        } else {
                            BeaconInfo.set(Beacons.indexOf(b), "Beacon ID : " + b.getId2() + " , Distance : " + b.getDistance());
                            distances[b.getId2().toInt()-1]=b.getDistance();
                        }
                    }
                    notifAdapter();
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("CB10023F-A318-3394-4199-A8730C7C1AEC", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line,final int textid) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(line);
            }
        });
    }
    private void notifAdapter() {
        runOnUiThread(new Runnable() {
            public void run() {
                BeaconsAdapter.notifyDataSetChanged();
            }
        });
    }
}

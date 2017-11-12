package tw.com.abc.mybletest1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;

    // Log TAG
    private static String TAG= "geoff";
    private static int REQUEST_ENABLE_BT = 123;
    //定義掃描時間
    private static final long SCAN_PERIOD = 10000;
    private Handler mHandler;
    private boolean mScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    0);
        } else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init() {

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }

        mHandler = new Handler();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {

        }
    }


    public void stopScan(View view) {
        scanLeDevice(true);
    }

    public void scanDevices(View view) {
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(BluetoothDevice device, int i, byte[] bytes) {
                    //
                    // Log.i(TAG, "onLeScan: " +device.getName());

                    String name =device.getName();
                    String mac =device.getAddress();
                    String state="";
                    String type="";

                    switch ( device.getBondState()){
                        case BluetoothDevice.BOND_BONDED:
                            state = "bonded"; break;
                        case BluetoothDevice.BOND_BONDING:
                            state = "bonding"; break;
                        case BluetoothDevice.BOND_NONE:
                            state = "none"; break;
                    }

                    switch ( device.getType()){
                        case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                            state = "classic"; break;
                        case BluetoothDevice.DEVICE_TYPE_DUAL:
                            state = "dual"; break;
                        case BluetoothDevice.BOND_NONE:
                            state = "none"; break;
                    }
                }

            };

}

package tw.com.abc.mybletest1;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BLEService extends Service {
    private String TAG ="geoff";
    public  final  LocalBinder mBinder = new LocalBinder();
    private BluetoothDevice device;
    private BluetoothGatt gatt;


    //BLE 處理的核心:
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothProfile.STATE_CONNECTED){
                Log.i(TAG, "onConnectionStateChange: CONNECTED");
                // 啟動服務
                gatt.discoverServices();
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                Log.i(TAG, "onConnectionStateChange: DISCONNECTED");
            } if(newState == BluetoothProfile.STATE_DISCONNECTING){
                Log.i(TAG, "onConnectionStateChange: DISCONNECTING");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };


    public class LocalBinder extends Binder{
        BLEService getScrvice(){return  BLEService.this;}
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        gatt.disconnect();
        gatt.close();

    }

    @Override
    public IBinder onBind(Intent intent) {
        device = (BluetoothDevice) intent.getParcelableExtra("device");

        gatt =device.connectGatt(this,false,mGattCallback);


       return mBinder;
    }
}

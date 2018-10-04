package test.com.shoushi.ble;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import test.com.shoushi.Entity.User;
import test.com.shoushi.utils.HexDump;

public class BlueToothConnect {

    //Debug
    private static final String TAG = BlueToothConnect.class.getName();

    private Context context;

    public static int STATE_DISCONNECTED = 0;
    public static int STATE_CONNECTING = 1;
    public static int STATE_CONNECTED = 2;
    public static int STATE_DISCONNECTING = 3;

    //Action
    public static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public static String ACTION_GATT_CONNECTING = "ACTION_GATT_CONNECTING";
    public static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public static String ACTION_GATT_DISCONNECTING = "ACTION_GATT_DISCONNECTING";
    public static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public static String ACTION_BLUETOOTH_DEVICE = "ACTION_BLUETOOTH_DEVICE";
    public static String ACTION_SCAN_FINISHED = "ACTION_SCAN_FINISHED";
    public final static String ACTION_DATA_CHANGE = "ACTION_DATA_CHANGE";

    //Member fields
    private BluetoothGatt mBluetoothGatt;
    private List<BluetoothDevice> mScanLeDeviceList;
    private boolean isScanning;
    private boolean isConnect = false;
    // private String mBluetoothDeviceAddress;
    private int mConnState = STATE_DISCONNECTED;
    private static final long SCAN_PERIOD = 5 * 1000;

    private OnLeScanListener mOnLeScanListener;
    private OnConnectListener mOnConnectListener;
    private OnServicesDiscoveredListener mOnServicesDiscoveredListener;
    private OnDataAvailableListener mOnDataAvailableListener;
    public static final String DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION = "00002902-0000-1000-8000-00805F9B34FB";

    private String con_mac;

    public String getCon_mac() {
        return con_mac;
    }

    private List<BluetoothDevice> mac_list = new ArrayList<>();

    public void setCon_mac(String con_mac) {
        this.con_mac = con_mac;
    }

    private boolean misyuan=false;

    public BlueToothConnect(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        Log.e("开启蓝牙状态++__", initialize() + "");
        if (initialize()) {
            enableBluetooth(true);
        } else {
            Globals.toastor.showToast("开启蓝牙失败");
        }

    }

    /**
     * Check for your device to support Ble
     *
     * @return true is support    false is not support
     */
    public boolean isSupportBle() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return If return true, the initialization is successful.
     */
    public boolean initialize() {
        //For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager.
        if (Globals.mBluetoothManager == null) {
            Globals.mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (Globals.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
//        Globals.mBluetoothAdapter = Globals.mBluetoothManager.getAdapter();
        Globals.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (Globals.mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to initialize BluetoothAdapter.");
            return false;
        }
        return true;
    }

    /**
     * Turn on or off the local Bluetooth adapter;do not use without explicit
     * user action to turn on Bluetooth.
     *
     * @param enable if open ble
     * @return if ble is open return true
     */
    public boolean enableBluetooth(boolean enable) {
        if (enable) {
            if (!Globals.mBluetoothAdapter.isEnabled()) {
                Globals.mBluetoothAdapter.enable();
                return true;
            }
            return true;
        } else {
            if (Globals.mBluetoothAdapter.isEnabled()) {
                return Globals.mBluetoothAdapter.disable();
            }
            return false;
        }
    }

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     *
     * @return true if the local adapter is turned on
     */
    public boolean isEnable() {
        return Globals.mBluetoothAdapter.isEnabled();
    }

    /**
     * Scan Ble device.
     *
     * @param enable     If true, start scan ble device.False stop scan.
     * @param scanPeriod scan ble period time
     */
    public void scanLeDevice(final boolean enable, long scanPeriod) {
        if (enable) {
            if (isEnable()) {

            } else {
                enableBluetooth(true);
            }
            ScanDevice(scanPeriod);
        } else {
            isScanning = false;
            // Globals.mBluetoothAdapter.stopLeScan(mScanCallback);
            stopScan();
            broadcastUpdate(ACTION_SCAN_FINISHED);
//            if (mScanLeDeviceList != null) {
//                mScanLeDeviceList.clear();
//                mScanLeDeviceList = null;
//            }
//            mBluetoothAdapter.getBluetoothLeScanner().stopScan(mScanCallback);
        }

    }

    private void ScanDevice(long scanPeriod) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
                isScanning = false;
                //Globals.mBluetoothAdapter.stopLeScan(mScanCallback);
                broadcastUpdate(ACTION_SCAN_FINISHED);
                Log.e("停止扫描了========","停止扫描了========");
                System.out.println("停止扫描了========");
            }
        }, scanPeriod);
        if (mScanLeDeviceList == null) {
            mScanLeDeviceList = new ArrayList<>();
        }
        mScanLeDeviceList.clear();
        isScanning = true;
        Log.e("开始扫描了","开始扫描了");
        Globals.mBluetoothAdapter.startLeScan(mScanCallback);
        System.out.println("开始扫描了==========");
    }

    public void ConnectscanLeDevice(boolean enable) {
        if (isEnable()) {
            if (enable) {
                //Stop scanning after a predefined scan period.
                isScanning = true;
                Log.e("开始扫描了-----", "--");
                Globals.mBluetoothAdapter.startLeScan(ConScanCallback);
            } else {
                isScanning = false;
                Globals.mBluetoothAdapter.stopLeScan(ConScanCallback);
//                Globals.YDEVICESTATETOAST=Globals.STATENOSHOW;
                broadcastUpdate(ACTION_SCAN_FINISHED);
            }
        }
    }

    /**
     * Device scan callback
     */
    private final BluetoothAdapter.LeScanCallback ConScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("连接扫描蓝牙设备=======", device.getName() + "==" + device.getAddress() + "===" + rssi + "==state==" + device.getBondState());
            if (device.getName() == null||mac_list.contains(device)) {
               // Log.e("过滤掉了","过滤");
                return;
            }
//            if () {
//                return;
//            }
           // Log.e("上一次的地址",con_mac);
                Log.e("是不是重连的地址","是不是");
                Log.e("信号值",rssi+"");
//                if (rssi<=-80){
////                    Globals.toastor.showToast("连接失败,信号不稳定");
////                    Globals.YDEVICESTATETOAST=Globals.STATESHOW;
//                    return;
//                }
                if (con_mac.equals(device.getAddress()))
                    mac_list.add(device);
                //connect(con_mac);
                Log.e("扫描的地址",device.getName());
                Log.e("扫描的地址名字我连接的那一个蓝牙",device.getName());
                Globals.toothConnect.ConnectTODevice(con_mac);

//            if (device.getName() != null && device.getName().contains("Lock")) {
//                if (con_mac.equals(device.getAddress())) {
//                    if (mac_list.contains(device)) {
//
//                    } else {
//                        mac_list.add(device);
//                        connect(con_mac);
//                    }
//                }
//            }
        }
    };


    public void stopScan() {
        isScanning = false;
        Globals.mBluetoothAdapter.stopLeScan(mScanCallback);
        Globals.mBluetoothAdapter.stopLeScan(ConScanCallback);
//        Globals.YDEVICESTATETOAST=Globals.STATENOSHOW;
        broadcastUpdate(ACTION_SCAN_FINISHED);
        if (mScanLeDeviceList != null) {
            mScanLeDeviceList.clear();
            //mScanLeDeviceList = null;
        }
    }

    /**
     * Scan Ble device.
     *
     * @param enable If true, start scan ble device.False stop scan.
     */
    public void scanLeDevice(boolean enable) {
        this.scanLeDevice(enable, SCAN_PERIOD);
    }

    /**
     * If Ble is scaning return true, if not return false.
     *
     * @return ble whether scanning
     */
    public boolean isScanning() {
        return isScanning;
    }

    /**
     * Get scan ble devices
     *
     * @return scan le device list
     */
    public List<BluetoothDevice> getScanLeDevice() {
        return mScanLeDeviceList;
    }

    /**
     * 连接设备
     *
     * @param address
     */
    public void ConnectTODevice(String address) {
        if (Globals.toothConnect!=null){
            Globals.toothConnect.stopScan();
        }
        Log.e("连接扫描了-----", "--" + address);



        //mac_list.clear();
//        Globals.toothConnect.close();
        BluetoothDevice device = Globals.mBluetoothAdapter.getRemoteDevice(address);

        String bleName =device.getName();


//        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//            Log.e("配对设备连接-----", "--");
//            mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
//        } else {
        // mac_list.clear();
        //  ConnectscanLeDevice(true);
        connect(address);
    }
    //
    // }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the BluetoothGattCallback#onConnectionStateChange.
     */

    private long start_time;

    public boolean connect(final String address) {
        // Globals.mBluetoothAdapter.stopLeScan(ConScanCallback);


        //close();
        Log.e("连接设备了-----", "---");
        if (Globals.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        //Previously connected device.  Try to reconnect.
        if (mBluetoothGatt != null && con_mac != null && address.equals(con_mac)) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnState = STATE_CONNECTING;
//                Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                return true;

            } else {
                return false;
            }
        }
        final BluetoothDevice device = Globals.mBluetoothAdapter.getRemoteDevice(address.toUpperCase());
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        device.connectGatt(context, false, mGattCallback);

        //We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        start_time = System.currentTimeMillis();
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        con_mac = address;
        mConnState = STATE_CONNECTING;
//        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the BluetoothGattCallback#onConnectionStateChange.
     */
    public void disconnect() {
        if (Globals.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized.");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        Log.e("主动关闭连接了----", "---");
        isConnect = false;
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the BluetoothGattCallback#onCharacteristicRead.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (Globals.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized.");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}, specific service UUID
     * and characteristic UUID. The read result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt,
     * android.bluetooth.BluetoothGattCharacteristic, int)} callback.
     *
     * @param serviceUUID        remote device service uuid
     * @param characteristicUUID remote device characteristic uuid
     */
    public synchronized void readCharacteristic(String serviceUUID, String characteristicUUID) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service =
                    mBluetoothGatt.getService(UUID.fromString(serviceUUID));
            BluetoothGattCharacteristic characteristic =
                    service.getCharacteristic(UUID.fromString(characteristicUUID));
            mBluetoothGatt.readCharacteristic(characteristic);
            Log.e(TAG, "接收的蓝牙数据========" + ((characteristic.getValue())));
            if (characteristic.getValue() != null) {
                String ReadData = HexDump.toHexString(characteristic.getValue());
                Intent intent = new Intent("READ_LOCK_DATA");
                intent.putExtra("data", ReadData);
                intent.putExtra("uuid", characteristic.getUuid().toString());
                context.sendBroadcast(intent);
                Log.e(TAG, "接收的蓝牙数据========" + (HexDump.toHexString(characteristic.getValue())));
            }
        }
    }

    public void readCharacteristic(String address, String serviceUUID, String characteristicUUID) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service =
                    mBluetoothGatt.getService(UUID.fromString(serviceUUID));
            BluetoothGattCharacteristic characteristic =
                    service.getCharacteristic(UUID.fromString(characteristicUUID));
            mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    /**
     * Write data to characteristic, and send to remote bluetooth le device.
     *
     * @param serviceUUID        remote device service uuid
     * @param characteristicUUID remote device characteristic uuid
     * @param value              Send to remote ble device data.
     */
    public boolean writeCharacteristic(String serviceUUID, String characteristicUUID, byte[] value) {
        Log.e("写的数据", HexDump.BytetohexString(value));
        if (mBluetoothGatt != null) {
            boolean setOnNotification = false;
            BluetoothGattService service =
                    mBluetoothGatt.getService(UUID.fromString(serviceUUID));
            if (service == null) {
                Log.e(TAG, "writeCharacteristic()---writeCharacteristic----->获取服务失败！");
                return false;
            }
            BluetoothGattCharacteristic characteristic =
                    service.getCharacteristic(UUID.fromString(characteristicUUID));
            if (characteristic != null) {
                characteristic.setValue(value);
                setOnNotification = mBluetoothGatt.writeCharacteristic(characteristic);
                Globals.oldtime=new Date();
                Log.e(TAG, "writeCharacteristic()---writeCharacteristic----->写入数据！" + value + ":" + setOnNotification);
            }

            return setOnNotification;
        }
        return false;
    }


    /**
     * Write value to characteristic, and send to remote bluetooth le device.
     *
     * @param characteristic remote device characteristic
     * @param value          New value for this characteristic
     * @return if write success return true
     */
    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, String value) {
        return writeCharacteristic(characteristic, value.getBytes());
    }

    /**
     * Writes a given characteristic and its values to the associated remote device.
     *
     * @param characteristic remote device characteristic
     * @param value          New value for this characteristic
     * @return if write success return true
     */
    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value) {
        if (mBluetoothGatt != null) {
            characteristic.setValue(value);
            return mBluetoothGatt.writeCharacteristic(characteristic);
        }
        return false;
    }

    public void setCharacteristicNotification(String serviceUUID, String characteristicUUID,
                                              boolean enabled) {
        boolean setOnNotification = false;
        if (Globals.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "---------------------->BluetoothAdapter not initialized");
            return;
        }
        BluetoothGattService service =
                mBluetoothGatt.getService(UUID.fromString(serviceUUID));
        if (service == null) {
            Log.e(TAG, "---------------------->获取服务失败！");
            return;
        }
        BluetoothGattCharacteristic characteristic =
                service.getCharacteristic(UUID.fromString(characteristicUUID));
        if (characteristic != null) {
            Log.e(TAG, "---------------------->获取通知特征值成功！");
        }
        setOnNotification = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

//        if(setOnNotification) {
//            List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
//            if (descriptorList != null && descriptorList.size() > 0) {
//                for (BluetoothGattDescriptor descriptor : descriptorList) {
//                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                    mBluetoothGatt.writeDescriptor(descriptor);
//                }
//            }
//        }
        if (setOnNotification != false) {
            Log.e(TAG, "---------------------->设置通知特征值成功！");
        } else {
            Log.e(TAG, "---------------------->设置通知特征值失败！false");
            //  return;
        }
//        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                UUID.fromString(DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION));
//        if (descriptor == null) {
//            Log.e("通知设置失败=======", "==" + descriptor);
//            return;
//        }
//        if (descriptor != null) {
//            descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
//                    BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
//        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
//                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//        mBluetoothGatt.writeDescriptor(descriptor);
//        if (setOnNotification != false) {
//            Log.e(TAG, "---------------------->描述写入成功！");
////            context.sendBroadcast(new Intent("ACTION_NOTIFY_SUCCESS").putExtra("address", con_mac));
//        } else {
//            Log.e(TAG, "---------------------->描述写入失败！false");
//        }
    }

    public boolean isConnect() {
        return isConnect;
    }

    public BluetoothDevice getConnectDevice() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getDevice();
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }

    /**
     * Device scan callback
     */
    private final BluetoothAdapter.LeScanCallback mScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (mScanLeDeviceList.contains(device))
                return;
            Log.e("蓝牙设备=======", device.getName() + "==" + device.getAddress() + "===" + rssi + scanRecord);
            if (device.getName() != null) {
                if (device.getName().contains("cc")) {
                    boolean flag = false;
                    for (User.ListBean user : Globals.dlist) {//做过滤，被绑定过的设备不显示
                        if (user.getMac().equals(device.getAddress())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        mScanLeDeviceList.add(device);
                        if (mOnLeScanListener != null) {
                            mOnLeScanListener.onLeScan(device, rssi, scanRecord);
                        }
                        broadcastUpdate(ACTION_BLUETOOTH_DEVICE, device);
                    }

                }
            }
//            mScanLeDeviceList.add(device);


        }
    };
    /**
     * Implements callback methods for GATT events that the app cares about.  For example,
     * connection change and services discovered.
     */


    Timer timerClose = new Timer();
    // 停止定时器
    private void stopTimers(){
        if(timerClose != null){
            timerClose.cancel();
            // 一定设置为null，否则定时器不会被回收
            timerClose = null;
        }
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt,final int status,final int newState) {
            if (mOnConnectListener != null) {
                mOnConnectListener.onConnect(gatt, status, newState);
            }
            String intentAction;
            final String address = gatt.getDevice().getAddress();

            timerClose.schedule(new TimerTask() {

                @Override
                public void run() {
                   Log.e("===状态码===","+"+newState );
                  if (newState==0){
                      stopTimers();
                      broadcastUpdate("ACTION_GATT_DISCONNECTED");
                  }

                }
            }, 1000, 5000);


            if (newState == BluetoothProfile.STATE_DISCONNECTED) {//已断开连接
                Log.e("已断开连接", "已断开连接");
                stopTimer();
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                misyuan=false;
//                if(isConnect){
                if (mac_list!=null){
                    mac_list.clear();
                }

                broadcastUpdate(intentAction, address);
                mBluetoothGatt.discoverServices();
                isConnect = false;






            } else if (newState == BluetoothProfile.STATE_CONNECTING) {//连接中
                Log.e("连接中", "连接中");
                isConnect = false;
                misyuan=false;
                intentAction = ACTION_GATT_CONNECTING;
                mConnState = STATE_CONNECTING;
                mBluetoothGatt.discoverServices();
                Log.i(TAG, "Connecting to GATT server.");
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功 去获取服务
                Log.e("接成功 去获取服务", "接成功 去获取服务");

                stopScan();
                intentAction = ACTION_GATT_CONNECTED;
                mConnState = STATE_CONNECTED;
                boolean flag = mBluetoothGatt.discoverServices();
                Log.e("连接成功了===========", "去获取服务了" + flag);

              // 初始化定时器

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mGattCallback.onReadRemoteRssi(gatt, status, newState);
                    }
                }, 5000, 2000);




                Log.e("服务挂了没有", isServiceRunning(context, "BaseService") + "");
                broadcastUpdate(intentAction, address);
                mBluetoothGatt.discoverServices();
//                if(flag){
//                    isConnect = true;
//                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED,con_mac);
//                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {//表示断开中
                Log.e("表示断开中", "表示断开中");
                isConnect = false;
                intentAction = ACTION_GATT_DISCONNECTING;
                mConnState = STATE_DISCONNECTING;
                Log.i(TAG, "Disconnecting from GATT server.");
                stopTimer();
                broadcastUpdate(intentAction, address);
            }

        }
        Timer timer = new Timer();
        // 停止定时器
        private void stopTimer(){
            if(timer != null){
                timer.cancel();
                // 一定设置为null，否则定时器不会被回收
                timer = null;
            }
        }


        // New services discovered
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {

                Log.e("发现服务了===========", "--" + (System.currentTimeMillis() - start_time));
                isConnect = true;
                Log.e("服务挂了没有", isServiceRunning(context, "BaseService") + "");
                // broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, con_mac);
                //Globals.toothConnect.setCharacteristicNotification(Globals.SERVICE_UUID, Globals.WRITE_UUID, true);
                broadcastUpdate("FUWU", con_mac);

                for (int i = 0; i < mBluetoothGatt.getServices().size(); i++) {
                    Log.e("service=======", "===" + gatt.getServices().get(i).getUuid());
                }
            } else {
                Log.e(TAG, "onServicesDiscovered received: " + status);
            }
        }

        /**
         * 用来判断服务是否运行.
         * @param mContext
         * @param className 判断的服务名字
         * @return true 在运行 false 不在运行
         */
        public boolean isServiceRunning(Context mContext, String className) {
            boolean isRunning = false;
            ActivityManager activityManager = (ActivityManager)
                    mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> serviceList
                    = activityManager.getRunningServices(30);
            if (!(serviceList.size() > 0)) {
                return false;
            }
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(className) == true) {
                    isRunning = true;
                    break;
                }
            }
            return isRunning;
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            Log.e("接受的数据+_+Read_+_+", HexDump.toHexString(characteristic.getValue()));
            Log.e(TAG, "读取的内容address: " + gatt.getDevice().getAddress() + ",Write: " + HexDump.toHexString(characteristic.getValue()) + characteristic.getUuid());
            String ReadData = HexDump.toHexString(characteristic.getValue());
            Intent intent = new Intent("READ_LOCK_DATA");
            intent.putExtra("data", ReadData);
            intent.putExtra("uuid", characteristic.getUuid().toString());
            context.sendBroadcast(intent);
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener.onCharacteristicRead(gatt, characteristic, status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            String address = gatt.getDevice().getAddress();
            Log.i("--->address",address);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
//            Log.e(TAG, "接收的数据==============" + HexDump.toHexString(characteristic.getValue()));
            Log.e("接受的数据+_+Changed+_+", HexDump.BytetohexString(characteristic.getValue()));
            Intent intent = new Intent("DEVICE_GET_DATA");//接收到数据，发送给service进行判断处理
            intent.putExtra("data", characteristic.getValue());
            intent.putExtra("address", con_mac);
            intent.putExtra("uuid", characteristic.getUuid().toString());
            Log.e("uuid========", "===" + characteristic.getUuid());
            context.sendBroadcast(intent);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
//            int instence = Math.pow(10,((Math.abs(rssi) - 45) / (10 * 4.5)));
            Log.e("====rssi====", rssi + "：" + ":");
            Log.e("信号值", rssi + "");
            Log.e("===多少米===", getDistance(rssi) + "");
//            DeviceRess = rssi;
            Log.e("misyuan",misyuan+"");
//            if (rssi < -80&& misyuan==false) {
//                Log.e("进来了","进来了");
//                if (isConnect){
//                    broadcastUpdate("DEVICE_YUAN");
//                    misyuan=true;
//                }else {
//                    disconnect();
//                }
                if ( getDistance(rssi)>=10){
                    broadcastUpdate("DUANKAI");
                }

        }
    };

    private static final double A_Value=50;/**A - 发射端和接收端相隔1米时的信号强度*/
    private static final double n_Value=2.5;/** n - 环境衰减因子*/
    public static double getDistance(int rssi){
        int iRssi = Math.abs(rssi);
        double power = (iRssi-A_Value)/(10*n_Value);
        return Math.pow(10,power);
    }



//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
////                    if (!isConnectAlarm) {
////                    }
//                    break;
//                case 1:
//                    Log.e("重连关闭蓝牙了----------", "---");
//                    //close();
//                    ConnectTODevice(con_mac);//断开或者没连接上时候，重新连接设备
//                    break;
//            }
//        }
//    };

    public void ConnectCode(User.ListBean device) {
        Globals.choicedevice = device;
        Log.e("打印对象的数据", Globals.choicedevice.toString());
        Log.e("打印对象的数据", Globals.choicedevice.getJurisdiction());
        Log.e("打印对象的数据", Globals.choicedevice.getMac());
        Log.e("打印对象的数据", Globals.choicedevice.getNumber());
        Log.e("打印对象的数据", Globals.choicedevice.getEquipmentID() + "");
        Globals.toothConnect.ConnectTODevice(Globals.choicedevice.getMac());
    }

    public void ScanConnectCode(User.ListBean device) {
        mac_list.clear();
        Globals.choicedevice = device;
        Log.e("打印对象的数据", Globals.choicedevice.toString());
        Log.e("打印对象的数据", Globals.choicedevice.getJurisdiction());
        Log.e("打印对象的数据", Globals.choicedevice.getMac());
        Log.e("打印对象的数据", Globals.choicedevice.getNumber());
        Log.e("打印对象的数据", Globals.choicedevice.getEquipmentID() + "");
        con_mac = Globals.choicedevice.getMac();
        ConnectscanLeDevice(true);

    }

    public interface OnLeScanListener {
        void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }

    public interface OnConnectListener {
        void onConnect(BluetoothGatt gatt, int status, int newState);
    }

    public interface OnServicesDiscoveredListener {
        void onServicesDiscovered(BluetoothGatt gatt, int status);
    }

    public interface OnDataAvailableListener {
        void onCharacteristicRead(BluetoothGatt gatt,
                                  BluetoothGattCharacteristic characteristic, int status);

        void onCharacteristicChanged(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic);
    }

    public void setOnLeScanListener(OnLeScanListener l) {
        mOnLeScanListener = l;
    }

    public void setOnConnectListener(OnConnectListener l) {
        mOnConnectListener = l;
    }

    public void setOnServicesDiscoveredListener(OnServicesDiscoveredListener l) {
        mOnServicesDiscoveredListener = l;
    }

    public void setOnDataAvailableListener(OnDataAvailableListener l) {
        mOnDataAvailableListener = l;
    }


    public synchronized boolean writeRXCharacteristic(String serviceuuid, String RxCHARuuid, String value) {
        boolean flag = false;
        if (mBluetoothGatt == null) {
        } else {
            List<BluetoothGattService> services = mBluetoothGatt.getServices();
            for (BluetoothGattService bluetoothGattService : services) {
                System.out.println("uuid:"
                        + bluetoothGattService.getUuid().toString());
            }
            BluetoothGattService RxService = mBluetoothGatt
                    .getService(UUID.fromString(serviceuuid));
            if (RxService == null) {
                return flag;
            }
            BluetoothGattCharacteristic RxChar = RxService
                    .getCharacteristic(UUID.fromString(RxCHARuuid));
            if (RxChar == null) {
                return flag;
            }
            RxChar.setValue(value);
            boolean status = mBluetoothGatt.writeCharacteristic(RxChar);
            Log.d(TAG, "write TXchar - status=" + status);
        }
        return flag;
    }

    // 发送广播消息
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        System.out.println("发现广播了===========");
        context.sendBroadcast(intent);
    }

    // 发送广播消息
    private void broadcastUpdate(final String action, int value) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        context.sendBroadcast(intent);
    }

    // 发送广播消息
    private void broadcastUpdate(final String action, byte value[]) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        context.sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final String address) {
        final Intent intent = new Intent(action);
        Log.e("发送的广播", action);
        intent.putExtra("address", address);
        Log.e("发送广播了---------", address);
        context.sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, BluetoothDevice device) {
        final Intent intent = new Intent(action);
        intent.putExtra("name", device.getName());
        intent.putExtra("address", device.getAddress());
        context.sendBroadcast(intent);
    }

//    public boolean getRssiVal() {
//        if (mBluetoothGatt == null)
//            return false;
//        return mBluetoothGatt.readRemoteRssi();
//
//    }

}

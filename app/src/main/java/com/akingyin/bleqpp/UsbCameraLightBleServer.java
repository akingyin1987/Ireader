package com.akingyin.bleqpp;//package com.bleqpp;
//
//import android.annotation.TargetApi;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Build;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.quintic.libqpp.QppUsbCameraApi;
//import com.quintic.libqpp.iQppCallback;
//import com.zlcdgroup.nfcsdk.RfidConnectorInterface;
//import com.zlcdgroup.nfcsdk.SDKInterface;
//import java.util.List;
//
///**
// * Usb相机ble控制蓝牙灯光
// * @author king
// * @version V1.0
// * @ Description:
// * @ Date 2017/12/13 10:16
// */
//@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//public class UsbCameraLightBleServer implements SDKInterface, iQppCallback {
//
//
//
//  private volatile static UsbCameraLightBleServer mSingleMode;
//  public static UsbCameraLightBleServer getInstance(){
//    if (mSingleMode == null){
//      synchronized (UsbCameraLightBleServer.class){
//        if (mSingleMode == null){
//          mSingleMode = new UsbCameraLightBleServer();
//        }
//      }
//    }
//    return  mSingleMode;
//  }
//
//
//
//  private    Context    baseContext;
//
//  public static BluetoothGatt mBluetoothGatt = null;
//  protected static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
//  protected static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
//
//  private BluetoothGattCallback mGattCallback;
//
//  //当前连接的对象
//  private   BleDevice   mBleDevice;
//
//  private  UsbCameraLightBleServer(){
//
//  }
//
//  public   void    init(Context  mContext){
//    baseContext = mContext.getApplicationContext();
//    QppUsbCameraApi.setCallback(this);
//  }
//
//
//  public   void    showToast(String  msg){
//    Toast.makeText(baseContext,msg,Toast.LENGTH_SHORT).show();
//  }
//
//  public    void    cleanMac(){
//    KsiSharedStorageHelper.setBluetoothMac3(KsiSharedStorageHelper.getPreferences(baseContext),"");
//
//  }
//
//  public    void   scanBleDevice(final Context  context ){
//
//    final String   macaddr = KsiSharedStorageHelper.getBluetoothMac3(KsiSharedStorageHelper.getPreferences(baseContext));
//    if(TextUtils.isEmpty(macaddr)){
//      BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//
//          .setAutoConnect(true)
//          .setScanTimeOut(10000)
//          .build();
//      BleManager.getInstance().initScanRule(scanRuleConfig);
//      BleManager.getInstance().scan(new BleScanCallback() {
//        @Override public void onScanStarted(boolean b) {
//
//        }
//
//        @Override public void onScanning(BleDevice bleDevice) {
//
//        }
//
//        @Override public void onScanFinished(List<BleDevice> list) {
//          if(null == list || list.size() == 0){
//            showToast("扫描完毕，未扫到关联的设备");
//            return;
//          }
//          showListBleDevice(context,list);
//        }
//      });
//    }else{
//     List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
//     if(null != bleDevices && bleDevices.size() >0){
//       for (BleDevice bleDevice : bleDevices) {
//         if(TextUtils.equals(bleDevice.getMac(),macaddr)){
//           mBleDevice = bleDevice;
//           connectBleDevice(bleDevice);
//           return;
//         }
//       }
//     }
//      BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//          .setDeviceMac(macaddr)
//          .setAutoConnect(true)
//          .setScanTimeOut(10000)
//          .build();
//      BleManager.getInstance().initScanRule(scanRuleConfig);
//      BleManager.getInstance().scan(new BleScanCallback() {
//        @Override public void onScanStarted(boolean b) {
//
//        }
//
//        @Override public void onScanning(BleDevice bleDevice) {
//
//        }
//
//        @Override public void onScanFinished(List<BleDevice> list) {
//          if(null == list || list.size() == 0){
//            showToast("当前已匹配的设备未扫描到，请重选设备或再次刷新");
//            return;
//          }
//          for (BleDevice bleDevice : list) {
//            if(TextUtils.equals(bleDevice.getMac(),macaddr)){
//              mBleDevice = bleDevice;
//              connectBleDevice(bleDevice);
//            }
//          }
//        }
//      });
//    }
//
//  }
//
//  public   void   hideDialog(){
//    if(null != dialog && dialog.isShowing()){
//      dialog.dismiss();
//    }
//  }
//
//  AlertDialog dialog =null;
//  public    Dialog showListBleDevice(Context  context, final List<BleDevice>  items){
//    if(null != dialog && dialog.isShowing()){
//      dialog.dismiss();
//    }
//    String[]   strings = new String[items.size()];
//    for (int i = 0; i < items.size(); i++) {
//      BleDevice  bleDevice = items.get(i);
//      strings[i] = bleDevice.getName()+"  "+bleDevice.getMac();
//    }
//    dialog = new AlertDialog.Builder(context).setTitle("选择蓝牙")
//        .setSingleChoiceItems(strings, -1, new DialogInterface.OnClickListener() {
//          @Override public void onClick(DialogInterface dialog, int which) {
//           BleDevice   bleDevice = items.get(which);
//            dialog.dismiss();
//            KsiSharedStorageHelper.setBluetoothMac3(KsiSharedStorageHelper.getPreferences(baseContext),bleDevice.getMac());
//            connectBleDevice(bleDevice);
//          }
//        }).create();
//    dialog.show();
//    return  dialog;
//  }
//
//
//  private   BluetoothGattCharacteristic    writeGattCharacteristic;
//  private   BluetoothGattCharacteristic    readGattCharacteristic;
//  private   BluetoothGattCharacteristic    notifyGattCharacteristic;
//
//
//
//  /**
//   * 连接蓝牙设备
//   * @param bleDevice
//   */
//  public   void    connectBleDevice(BleDevice  bleDevice){
//    //当前未连接
//    BleManager.getInstance().removeConnectGattCallback(bleDevice);
//    if(!BleManager.getInstance().isConnected(bleDevice)){
//       BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
//         @Override public void onStartConnect() {
//
//         }
//
//         @Override public void onConnectFail(BleException e) {
//           showToast("连接蓝牙设备失败");
//         }
//
//         @Override
//         public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
//           mBluetoothGatt = bluetoothGatt;
//           mBleDevice = bleDevice;
//           //获取当前的所有服务
//           List<BluetoothGattService> bluetoothGattServices =  bluetoothGatt.getServices();
//           writeGattCharacteristic = null;
//           readGattCharacteristic  = null;
//           notifyGattCharacteristic = null;
//           for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
//             //便利当前服务所有特征
//             List<BluetoothGattCharacteristic> characteristicList =  bluetoothGattService.getCharacteristics();
//             if(null != characteristicList && characteristicList.size()>0){
//
//               for (BluetoothGattCharacteristic characteristic : characteristicList) {
//                 if(characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE){
//                    if(null != writeGattCharacteristic){
//                      writeGattCharacteristic = characteristic;
//                    }
//                 }
//                 if(characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_READ){
//                     if(null != readGattCharacteristic){
//                       readGattCharacteristic = characteristic;
//                     }
//                 }
//                 if(characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY){
//                    if(null != notifyGattCharacteristic){
//                      notifyGattCharacteristic = characteristic;
//                    }
//                 }
//               }
//             }
//           }
//           notifyData();
//         }
//
//         @Override
//         public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt,
//             int i) {
//             showToast("蓝牙连接已被断开");
//         }
//       });
//    }
//  }
//
//  /**
//   * 读取数据
//   */
//  public   void    readData(){
//    if(null != readGattCharacteristic){
//      BleManager.getInstance().read(mBleDevice,readGattCharacteristic.getService().getUuid().toString(),readGattCharacteristic.getUuid().toString(),mBleReadCallback);
//    }else{
//      showToast("当前连接已丢失或无读取权限");
//    }
//
//  }
//
//
//  public BleReadCallback   mBleReadCallback = new BleReadCallback() {
//    @Override public void onReadSuccess(byte[] bytes) {
//      System.out.println("onReadSuccess="+ HexUtil.encodeHexStr(bytes));
//    }
//
//    @Override public void onReadFailure(BleException e) {
//      showToast("读取数据失败"+e.toString());
//    }
//  };
//
//  @Override public void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar,
//      byte[] qppData) {
//
//  }
//
//
//
//
//  /**
//   * 数据写入
//   * @param datas
//   */
//  public  void   writeData(byte[] datas){
//    if(null != writeGattCharacteristic){
//      BleManager.getInstance().write(mBleDevice,readGattCharacteristic.getService().getUuid().toString(),writeGattCharacteristic.getUuid().toString(),datas,mBleWriteCallback);
//    }else{
//      showToast("当前连接已丢失或无写入权限");
//    }
//  }
//
//  private BleWriteCallback   mBleWriteCallback = new BleWriteCallback() {
//    @Override public void onWriteSuccess() {
//      showToast("写入数据成功");
//    }
//
//    @Override public void onWriteFailure(BleException e) {
//        showToast("写入数据失败");
//    }
//  };
//
//  /**
//   * 监听通知
//   */
//  public   void   notifyData(){
//    if(null != notifyGattCharacteristic){
//      BleManager.getInstance().notify(mBleDevice,notifyGattCharacteristic.getService().getUuid().toString(),notifyGattCharacteristic.getUuid().toString(),mBleNotifyCallback);
//    }else{
//      showToast("当前连接已丢失或无监听权限");
//    }
//  }
//
//  public BleNotifyCallback   mBleNotifyCallback = new BleNotifyCallback() {
//    @Override public void onNotifySuccess() {
//
//    }
//
//    @Override public void onNotifyFailure(BleException e) {
//
//    }
//
//    @Override public void onCharacteristicChanged(byte[] bytes) {
//
//    }
//  };
//
//  @Override public boolean initConnect() {
//
//    return null != mBleDevice && BleManager.getInstance().isConnected(mBleDevice);
//  }
//
//  @Override public void setAutoConnect(boolean b) {
//
//  }
//
//  @Override public boolean connectDestroy() {
//    BleManager.getInstance().destroy();
//    return false;
//  }
//
//  @Override public void onregistered(RfidConnectorInterface rfidConnectorInterface) {
//
//  }
//
//  @Override public void unregistered(RfidConnectorInterface rfidConnectorInterface) {
//
//  }
//
//
//  public BluetoothGattCallback   mBluetoothGattCallback = new BluetoothGattCallback() {
//    @Override public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//      super.onConnectionStateChange(gatt, status, newState);
//    }
//
//    @Override public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//      super.onServicesDiscovered(gatt, status);
//      if (QppUsbCameraApi.qppEnable(mBluetoothGatt)) {
//
//      } else {
//
//      }
//    }
//
//    @Override
//    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
//        int status) {
//      super.onCharacteristicRead(gatt, characteristic, status);
//    }
//
//    @Override public void onCharacteristicWrite(BluetoothGatt gatt,
//        BluetoothGattCharacteristic characteristic, int status) {
//      super.onCharacteristicWrite(gatt, characteristic, status);
//    }
//
//    @Override public void onCharacteristicChanged(BluetoothGatt gatt,
//        BluetoothGattCharacteristic characteristic) {
//
//      QppUsbCameraApi.updateValueForNotification(gatt, characteristic);
//    }
//
//    @Override public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
//        int status) {
//      QppUsbCameraApi.setQppNextNotify(gatt, true);
//    }
//  };
//}

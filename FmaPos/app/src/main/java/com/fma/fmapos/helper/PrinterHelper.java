package com.fma.fmapos.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.fma.fmapos.controller.ControllerSetting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

/**
 * Created by fmanda on 08/21/17.
 */

public class PrinterHelper {
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";
    Context context;
    String printerName = "EP5802AI";
    ControllerSetting setting;

    public PrinterHelper(Context context){
        this.context = context;
        setting = new ControllerSetting(this.context);
        this.printerName = setting.getCashierPrinter();
    }

    public boolean ConnectPrinter(){
        if (bluetoothAdapter==null) bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            if(!bluetoothAdapter.isEnabled()){
                return false;
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if( (pairedDevices.size() > 0) && (!this.printerName.equals(""))) {
                for(BluetoothDevice device : pairedDevices){
                    if(device.getName().equals(printerName)) {
                        bluetoothDevice = device;
                        break;
                    }
                }
                if (bluetoothDevice == null) return false;
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                bluetoothSocket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
                beginListenForData();
            }
            else{
                value+="No Printer Found";
                Toast.makeText(context, value, Toast.LENGTH_LONG).show();
                return false;
            }
        }catch(Exception ex){
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(context, value, Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void beginListenForData() {
        try {
            final Handler handler = new Handler();
            // this is the ASCII code for a newline character
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = inputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(
                                            readBuffer, 0,
                                            encodedBytes, 0,
                                            encodedBytes.length
                                    );
                                    // specify US-ASCII encoding
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    // tell the user data were sent to bluetooth printer device
                                    handler.post(new Runnable() {
                                        public void run() {
                                            Log.d("e", data);
                                        }
                                    });

                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
                }
            });
            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TestPrintData(byte [] paramArrayOfByte){
        if (paramArrayOfByte == null) return;

        if (!ConnectPrinter()) return;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (outputStream == null) return;

        try{
            outputStream.write(paramArrayOfByte);
            outputStream.close();
            bluetoothSocket.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public String getDoubleSeparator(){
        String str = "";
        while (str.length()<32){
            str += "=";
        }
        return str;
    }

    public String getSingleSeparator(){
        String str = "";
        while (str.length()<32){
            str += "-";
        }
        return str;
    }

    public void ClosePrinter() {
        try {
            outputStream.close();
            bluetoothSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void PrintLine(String lineStr, Boolean withLineBreak){
        if (withLineBreak) lineStr += "\n";
        try {
            outputStream.write(lineStr.getBytes());
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public void PrintLine(String lineStr){
        PrintLine(lineStr, Boolean.TRUE);
    }

    public String alignLeft() {
        final byte[] AlignLeft = {27, 97,48};
        String s = new String(AlignLeft);
        return s;
    }

    public String alignCenter() {
        final byte[] AlignCenter = {27, 97,49};
        String s = new String(AlignCenter);
        return s;
    }

    public String alignRight() {
        final byte[] AlignRight = {27, 97,50};
        String s = new String(AlignRight);
        return s;
    }

}




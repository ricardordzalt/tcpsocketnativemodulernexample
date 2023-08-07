package com.testnativetcp;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class TcpSocketModule extends ReactContextBaseJavaModule {

  private Socket socket;
  private InputStream inputStream;
  private OutputStream outputStream;
  
   TcpSocketModule(ReactApplicationContext context) {
       super(context);
   }



  @ReactMethod
  public void connectToSocket(final String host, final int port) {
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  socket = new Socket(host, port);
                  inputStream = socket.getInputStream();
                  outputStream = socket.getOutputStream();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
  }

  // Método para recibir información del socket y enviarla a JavaScript
  private void sendEventToJS(String eventName, String data) {
      getReactApplicationContext()
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit(eventName, data);
  }

  // Método para recibir información del socket en un hilo aparte
  private void receiveDataFromSocket() {
      new Thread(new Runnable() {
          @Override
          public void run() {
              byte[] buffer = new byte[1024];
              int bytesRead;

              try {
                  while ((bytesRead = inputStream.read(buffer)) != -1) {
                      final String data = new String(buffer, 0, bytesRead);
                      // Envía los datos a JavaScript en un hilo de UI
                      new Handler(Looper.getMainLooper()).post(new Runnable() {
                          @Override
                          public void run() {
                              sendEventToJS("socketDataEvent", data);
                          }
                      });
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
  }

  @ReactMethod
  public void connectAndReceive(final String host, final int port) {
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  socket = new Socket(host, port);
                  inputStream = socket.getInputStream();
                  outputStream = socket.getOutputStream();

                  // Iniciar la recepción de datos del socket en un hilo diferente
                  receiveDataFromSocket();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
  }

  @Override
  public String getName() {
    return "TcpSocketModule";
  }
}
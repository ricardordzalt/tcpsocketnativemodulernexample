import React from 'react';
import {NativeModules, Button, DeviceEventEmitter} from 'react-native';

const {TcpSocketModule} = NativeModules;

// Escuchar eventos del socket
DeviceEventEmitter.addListener('socketDataEvent', (data) => {
  console.log('Datos recibidos desde el socket:', data);
});

const NewModuleButton = () => {
  const onPress = () => {
    TcpSocketModule.connectAndReceive('192.168.100.7', 8899);
  };

  return (
    <>
      <Button
        title="Click to invoke your native module!"
        color="#841584"
        onPress={onPress}
      />
      <Button
        title="Click to invoke your native module!"
        color="#343444"
        onPress={onPress}
      />
      <Button
        title="Click to invoke your native module!"
        color="#123357"
        onPress={onPress}
      />
      <Button
        title="Click to invoke your native module!"
        color="#232349"
        onPress={onPress}
      />
    </>
  );
};

export default NewModuleButton;

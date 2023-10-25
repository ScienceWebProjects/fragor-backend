package com.filament.measurement.UDP;

import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Service
public class UDP {

    public DatagramPacket send(String messageToSend, int port, String ip, int timeout) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(timeout);
        byte[] sendData = messageToSend.getBytes();
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        InetAddress destinationIP = InetAddress.getByName(ip);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, port);
        socket.send(sendPacket);
        socket.receive(receivePacket);
        socket.close();
        return receivePacket;
    }
    public DatagramPacket scanLocalHost(String message,int port,int timeoutInMs,String exceptionMessage) {
        for (int i = 2; i < 254; i++) {
            try {
                return send(message, port, "192.168.0." + i, timeoutInMs);
            } catch (IOException e) {
                continue;
            }
        }
        throw new NotFound404Exception(exceptionMessage);
    }
}

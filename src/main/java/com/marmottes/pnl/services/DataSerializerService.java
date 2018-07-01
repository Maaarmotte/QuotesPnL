package com.marmottes.pnl.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class DataSerializerService {
    public void serialize(Object obj, String name) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(name + ".dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (IOException e) {
            log.error("Couldn't serialize object !", e);
        }
    }

    public Object deserialize(String name) {
        Object obj = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(name + ".dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            obj = objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            log.error("Couldn't deserialize object !", e);
        }

        return obj;
    }
}

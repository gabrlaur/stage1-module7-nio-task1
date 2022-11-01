package com.epam.mjc.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileReader {
    private static final Logger LOGGER = Logger.getLogger(FileReader.class.getName());

    public static Profile getDataFromFile(File file) {
        StringBuilder data = new StringBuilder();
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = accessFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    data.append((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, String.format("File not found %s", e));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("IO exception %s", e));
        }
        return getProfileFromString(data.toString());
    }

    public static Profile getProfileFromString(String data) {
        String[] rows = data.split("\n");
        String name = rows[0].split(": ")[1].trim();
        String age = rows[1].split(": ")[1].trim();
        String email = rows[2].split(": ")[1].trim();
        String phone = rows[3].split(": ")[1].trim();
        return new Profile(name, Integer.parseInt(age), email, Long.parseLong(phone));
    }

    public static void main(String[] args) {
        File file = new File("src/main/resources/Profile.txt");
        getDataFromFile(file);
    }
}

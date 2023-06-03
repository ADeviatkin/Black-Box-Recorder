package com.ad.blackboxrecorder.recording;

import android.util.Log;

import com.ad.blackboxrecorder.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record {
    private static final long DATA_SIZE = 5292000;  // ten minute 52920000
    FileOutputStream CurrentFileStream;
    private long currentFileSize = 0;
    private LocalDateTime creationDate;
    private String title;
    private File file;
    public boolean isPermanent;

    public FileOutputStream createNewFile(File outputDir) throws FileNotFoundException {
        File outputFile = new File(outputDir, title);
        CurrentFileStream = new FileOutputStream(outputFile);
        return CurrentFileStream;
    }
    public LocalDateTime extractDataTimeFromTitle(String title){
        try {
            String buf = title.split("\\.")[0].split(" ")[1];
            return LocalDateTime.parse(buf, DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
            return null;
        }
    }
    public Record(String fileName){
        title = fileName;
        if (title.charAt(0) == 'p'){
            isPermanent = true;
        }
        creationDate = extractDataTimeFromTitle(fileName);
        file = new File(MainActivity.directory, fileName);
    }
    public Record() throws FileNotFoundException {
        creationDate = LocalDateTime.now();
        title = "recording " + creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss")) + ".encraud";
        createNewFile(MainActivity.directory);
    }
    public void writeToFile(byte[] buffer ){
        try {
            CurrentFileStream.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentFileSize += buffer.length;
    }
    public void closeNewFile(){
        if(CurrentFileStream!=null){
            try {
                CurrentFileStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public long getFileSize(){
        return currentFileSize;
    }
    public long getDataSize(){
        return DATA_SIZE;
    }
    public long getStatus(){
        return Duration.between(LocalDateTime.now(), creationDate.plusHours(24)).toMinutes();
    }
    public String getTitle(){
        return title;
    }
    public File getFile() { return file;}
    public void makePermanent(){
        File from = new File(MainActivity.directory,"recording " + creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss")) + ".encraud");
        File to = new File(MainActivity.directory,"p-recording " + creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss")) + ".encraud");
        if(from.exists())
            from.renameTo(to);
        file = to;
    }
    public void removeRecording(){
        file.delete();
    }
    protected void finalize()
    {
        closeNewFile();
    }

}

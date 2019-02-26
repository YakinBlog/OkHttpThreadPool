package com.yakin.oktp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileRequest extends ObjectRequest {

    private File file;
    private InputStream fileStream;

    public FileRequest(File file) {
        this.file = file;
    }

    public FileRequest(String filePath) {
        this.file = new File(filePath);
    }

    public FileRequest(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public InputStream getContent() throws Exception {
        if(fileStream != null) {
            return fileStream;
        }
        return new FileInputStream(file);
    }

    @Override
    public long getContentLength() throws Exception {
        if(fileStream != null) {
            return fileStream.available();
        }
        if(file != null) {
            return file.length();
        }
        return 0;
    }
}

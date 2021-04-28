package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class fileObject {

    private String FileName;
    private String FileSize;
    private String href;
    private String FileID;

    public fileObject(String fileName, String fileSize, String href) {
        FileName = fileName;
        FileSize = fileSize;
        this.href = href;
    }

    private List<fileObject> FileList;

    public List<fileObject> getFileList() {
        return FileList;
    }

    public void setFileList(List<fileObject> fileList) {
        FileList = fileList;
    }

    @SerializedName("FileURI")
    private String URIfile;

    public String getFileName() {
        return FileName;
    }

    public String getFileSize() {
        return FileSize;
    }

    public String getHref() {
        return href;
    }

    public String getFileID() {
        return FileID;
    }

    public String getURIfile() {
        return URIfile;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }

    public void setURIfile(String URIfile) {
        this.URIfile = URIfile;
    }

    @Override
    public String toString() {
        return "fileObject{" +
                "FileName='" + FileName + '\'' +
                ", FileSize='" + FileSize + '\'' +
                ", href='" + href + '\'' +
                ", FileID='" + FileID + '\'' +
                ", FileList=" + FileList +
                ", URIfile='" + URIfile + '\'' +
                '}';
    }
}

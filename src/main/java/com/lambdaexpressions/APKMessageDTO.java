package com.lambdaexpressions;

public class APKMessageDTO {
    private String name;
    private String version;
    private byte[] apkFile;

    public APKMessageDTO() {
    }

    public APKMessageDTO(String name, String version, byte[] apkFile) {
        this.name = name;
        this.version = version;
        this.apkFile = apkFile;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public byte[] getApkFile() {
        return apkFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setApkFile(byte[] apkFile) {
        this.apkFile = apkFile;
    }
}
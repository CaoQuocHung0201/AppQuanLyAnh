package com.example.albumanh;

public class ObjectImage implements Comparable<ObjectImage> {
    private int Id;
    private byte[]hinh;
    private String ngayluu;

    public ObjectImage(int id, byte[] hinh, String ngayluu) {
        Id = id;
        this.hinh = hinh;
        this.ngayluu = ngayluu;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    public String getNgayluu() {
        return ngayluu;
    }

    public void setNgayluu(String ngayluu) {
        this.ngayluu = ngayluu;
    }

    @Override
    public int compareTo(ObjectImage objectImage) {
        return getNgayluu().compareTo(objectImage.getNgayluu());
    }
}

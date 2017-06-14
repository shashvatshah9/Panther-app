package com.crypt.panther;

/**
 * Created by mynameislt on 4/16/2017.
 */

class ImageUpload {
    public String name;
    public String url;

    public ImageUpload(String s, String s1) {
        name = s;
        url = s1;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}

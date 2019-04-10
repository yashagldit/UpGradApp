package com.upgrad.internshipapp;

public class Questions {
    String id,title,link,dat,oname,oimg,tags;

    public Questions(String id, String title, String link, String dat, String oname, String oimg, String tags) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.dat = dat;
        this.oname = oname;
        this.oimg = oimg;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDat() {
        return dat;
    }

    public String getOname() {
        return oname;
    }

    public String getOimg() {
        return oimg;
    }

    public String getTags() {
        return tags;
    }
}

package com.example.rakshit.miwok;

public class WordsList {

    private String miwok;
    private String eng;
    private int img_src = -1;

    public WordsList(String miwok, String eng)
    {
        this.miwok = miwok;
        this.eng = eng;
    }

    public WordsList(String miwok, String eng, int img_src)
    {
        this.miwok = miwok;
        this.eng = eng;
        this.img_src = img_src;
    }

    public String getmiwok() { return miwok; }

    public String geteng() { return eng; }

    public int getimg_src() { return img_src; }

    public boolean hasImg()
    {
        if(img_src==-1)
            return false;
        return true;
    }
}

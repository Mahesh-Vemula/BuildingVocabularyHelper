package com.mv.buildingVocabularyHelper.dto;


public class Collegiate {

    private String[] shortdef;

    private Meta meta;

    public String[] getShortdef() {
        return shortdef;
    }

    public void setShortdef(String[] shortdef) {
        this.shortdef = shortdef;
    }

    public Meta getMeta ()
    {
        return meta;
    }

    public void setMeta (Meta meta)
    {
        this.meta = meta;
    }

    @Override
    public String toString()
    {
        return "[ClassPojo for word: "+meta.getId()+"]";
        //return "ClassPojo [date = "+date+", def = "+def+", meta = "+meta+", fl = "+fl+", hwi = "+hwi+", shortdef = "+shortdef+", et = "+et+"]";
    }

}

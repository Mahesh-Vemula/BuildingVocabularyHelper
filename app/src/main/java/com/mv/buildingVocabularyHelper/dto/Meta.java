package com.mv.buildingVocabularyHelper.dto;

public class Meta
{
    private String[] stems;

    private String offensive;

    private String src;

    private String section;

    private String id;

    private String sort;

    private String uuid;

    public String[] getStems ()
    {
        return stems;
    }

    public void setStems (String[] stems)
    {
        this.stems = stems;
    }

    public String getOffensive ()
    {
        return offensive;
    }

    public void setOffensive (String offensive)
    {
        this.offensive = offensive;
    }

    public String getSrc ()
    {
        return src;
    }

    public void setSrc (String src)
    {
        this.src = src;
    }

    public String getSection ()
    {
        return section;
    }

    public void setSection (String section)
    {
        this.section = section;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getSort ()
    {
        return sort;
    }

    public void setSort (String sort)
    {
        this.sort = sort;
    }

    public String getUuid ()
    {
        return uuid;
    }

    public void setUuid (String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [stems = "+stems+", offensive = "+offensive+", src = "+src+", section = "+section+", id = "+id+", sort = "+sort+", uuid = "+uuid+"]";
    }
}
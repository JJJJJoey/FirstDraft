package gr.cityu.firstdraft;

public class ItemImageUpload {
    private String mName;
    private String mImageUrl;
    private String mImageDescription;
    private String mImageCategory;
    private String mImageTags;


    ItemImageUpload(){}

    public ItemImageUpload(String mName, String mImageUrl, String mImageDescription, String mImageCategory, String mImageTags) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mImageDescription = mImageDescription;
        this.mImageCategory = mImageCategory;
        this.mImageTags = mImageTags;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmImageDescription() {
        return mImageDescription;
    }

    public void setmImageDescription(String mImageDescription) {
        this.mImageDescription = mImageDescription;
    }

    public String getmImageCategory() {
        return mImageCategory;
    }

    public void setmImageCategory(String mImageCategory) {
        this.mImageCategory = mImageCategory;
    }

    public String getmImageTags() {
        return mImageTags;
    }

    public void setmImageTags(String mImageTags) {
        this.mImageTags = mImageTags;
    }
}

package gr.cityu.firstdraft;

public class UserInfo {

    private String mName;
    private String mLikes;
    private String mImageUrl;
    private String mUserId;

    UserInfo(){}

    public UserInfo(String mName,String mImageUrl, String mLikes) {
        this.mName = mName;
        this.mLikes = mLikes;
        this.mImageUrl = mImageUrl;
        this.mUserId = mUserId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLikes() {
        return mLikes;
    }

    public void setmLikes(String mLikes) {
        this.mLikes = mLikes;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}

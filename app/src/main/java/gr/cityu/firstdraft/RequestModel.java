package gr.cityu.firstdraft;

public class RequestModel {

    private String sender;
    private String receiver;
    private String itemId;

    RequestModel(){}

    public RequestModel(String sender, String receiver, String itemId) {
        this.sender = sender;
        this.receiver = receiver;
        this.itemId = itemId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}



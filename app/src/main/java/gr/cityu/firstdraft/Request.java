package gr.cityu.firstdraft;

public class Request {

    private String Sender;
    private String receiver;
    private String content;

    public Request(){}

    public Request(String sender, String receiver, String content) {
        Sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

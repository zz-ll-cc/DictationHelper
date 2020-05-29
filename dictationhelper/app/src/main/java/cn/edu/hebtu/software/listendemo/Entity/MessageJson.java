package cn.edu.hebtu.software.listendemo.Entity;

import java.util.List;

public class MessageJson {
    private float status;
    private List<Message> MessageList;
    private int MessageVersion;

    @Override
    public String toString() {
        return "MessageJson{" +
                "status=" + status +
                ", MessageList=" + MessageList +
                ", MessageVersion=" + MessageVersion +
                '}';
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public List<Message> getMessageList() {
        return MessageList;
    }

    public void setMessageList(List<Message> messageList) {
        MessageList = messageList;
    }

    public int getMessageVersion() {
        return MessageVersion;
    }

    public void setMessageVersion(int messageVersion) {
        MessageVersion = messageVersion;
    }
}

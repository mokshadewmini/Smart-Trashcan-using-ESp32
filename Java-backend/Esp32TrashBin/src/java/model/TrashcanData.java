package model;

public class TrashcanData {
    private String trashLevel;
    private String buzzerStatus;
    private String lidStatus;
    private String timestamp;

    // Constructor
    public TrashcanData(String trashLevel, String buzzerStatus, String lidStatus, String timestamp) {
        this.trashLevel = trashLevel;
        this.buzzerStatus = buzzerStatus;
        this.lidStatus = lidStatus;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getTrashLevel() {
        return trashLevel;
    }

    public void setTrashLevel(String trashLevel) {
        this.trashLevel = trashLevel;
    }

    public String getBuzzerStatus() {
        return buzzerStatus;
    }

    public void setBuzzerStatus(String buzzerStatus) {
        this.buzzerStatus = buzzerStatus;
    }

    public String getLidStatus() {
        return lidStatus;
    }

    public void setLidStatus(String lidStatus) {
        this.lidStatus = lidStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

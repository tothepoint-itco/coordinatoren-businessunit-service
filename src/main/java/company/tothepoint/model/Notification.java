package company.tothepoint.model;

import java.time.LocalDateTime;

class Notification {
    private String title;
    private LocalDateTime dateTimeStamp;

    Notification() {
    }

    Notification(String title) {
        this.title = title;
        this.dateTimeStamp = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(LocalDateTime dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }
}

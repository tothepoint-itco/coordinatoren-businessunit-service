package company.tothepoint.model.businessunit;

import company.tothepoint.model.Notification;

public class BusinessUnitDeletedNotification extends Notification {
    private String deletedBusinessUnitId;

    public BusinessUnitDeletedNotification() {
    }

    public BusinessUnitDeletedNotification(String title, String deletedBusinessUnitId) {
        super(title);
        this.deletedBusinessUnitId = deletedBusinessUnitId;
    }

    public String getDeletedBusinessUnitId() {
        return deletedBusinessUnitId;
    }

    public void setDeletedBusinessUnitId(String deletedBusinessUnitId) {
        this.deletedBusinessUnitId = deletedBusinessUnitId;
    }
}

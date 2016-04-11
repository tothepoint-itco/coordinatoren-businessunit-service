package company.tothepoint.model.businessunit;

import company.tothepoint.model.Notification;

public class BusinessUnitCreatedNotification extends Notification {
    private BusinessUnit createdBusinessUnit;

    public BusinessUnitCreatedNotification() {
    }

    public BusinessUnitCreatedNotification(String title, BusinessUnit createdBusinessUnit) {
        super(title);
        this.createdBusinessUnit = createdBusinessUnit;
    }

    public BusinessUnit getCreatedBusinessUnit() {
        return createdBusinessUnit;
    }

    public void setCreatedBusinessUnit(BusinessUnit createdBusinessUnit) {
        this.createdBusinessUnit = createdBusinessUnit;
    }
}

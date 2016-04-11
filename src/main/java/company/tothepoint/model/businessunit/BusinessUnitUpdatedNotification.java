package company.tothepoint.model.businessunit;

import company.tothepoint.model.Notification;

public class BusinessUnitUpdatedNotification extends Notification {
    private BusinessUnit updatedBusinessUnit;

    public BusinessUnitUpdatedNotification() {
    }

    public BusinessUnitUpdatedNotification(String title, BusinessUnit updatedBusinessUnit) {
        super(title);
        this.updatedBusinessUnit = updatedBusinessUnit;
    }

    public BusinessUnit getUpdatedBusinessUnit() {
        return updatedBusinessUnit;
    }

    public void setUpdatedBusinessUnit(BusinessUnit updatedBusinessUnit) {
        this.updatedBusinessUnit = updatedBusinessUnit;
    }
}

package company.tothepoint.model;

import java.util.Optional;

public class NewBusinessUnitNotification extends Notification {
    private BusinessUnit businessUnit;

    public NewBusinessUnitNotification() {

    }

    public NewBusinessUnitNotification(String title, BusinessUnit businessUnit) {
        super(title);
        this.businessUnit = businessUnit;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }
}

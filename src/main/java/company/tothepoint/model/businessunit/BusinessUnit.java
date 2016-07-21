package company.tothepoint.model.businessunit;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BusinessUnit {
    @Id
    private String id;

    @NotNull(message = "businessunit.error.naam.notnull")
    @Size(min = 1, max = 255, message = "businessunit.error.naam.size")
    private String naam;

    public BusinessUnit() {
    }

    public BusinessUnit(String naam) {
        this.naam = naam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}

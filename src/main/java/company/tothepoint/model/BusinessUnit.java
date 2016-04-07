package company.tothepoint.model;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BusinessUnit {
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 255)
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

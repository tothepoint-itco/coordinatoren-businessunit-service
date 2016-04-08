package company.tothepoint.controller;

import company.tothepoint.model.BusinessUnit;
import company.tothepoint.model.NewBusinessUnitNotification;
import company.tothepoint.repository.BusinessUnitRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business-units")
public class BusinessUnitController {
    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private String exchangeName;

    @Autowired
    private String routingKey;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BusinessUnit>> getAllBusinessUnits() {
        return new ResponseEntity<>(businessUnitRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BusinessUnit> getBusinessUnit(@PathVariable("id") String id) {
        Optional<BusinessUnit> businessUnitOption = Optional.ofNullable(businessUnitRepository.findOne(id));

        return businessUnitOption.map(businessUnit->
            new ResponseEntity<>(businessUnit, HttpStatus.OK)
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BusinessUnit> createBusinessUnit(@RequestBody BusinessUnit businessUnit) {
        BusinessUnit createdBusinessUnit = businessUnitRepository.save(businessUnit);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, new NewBusinessUnitNotification("businessUnitCreated", createdBusinessUnit));
        return new ResponseEntity<>(createdBusinessUnit, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<BusinessUnit> updateBusinessUnit(@PathVariable("id") String id, @RequestBody BusinessUnit businessUnit) {
        Optional<BusinessUnit> existingBusinessUnit = Optional.ofNullable(businessUnitRepository.findOne(id));

        return existingBusinessUnit.map(bu ->
            {
                bu.setNaam(businessUnit.getNaam());
                return new ResponseEntity<>(businessUnitRepository.save(bu), HttpStatus.OK);
            }
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<BusinessUnit> deleteBusinessUnit(@PathVariable("id") String id) {
        if (businessUnitRepository.exists(id)) {
            businessUnitRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

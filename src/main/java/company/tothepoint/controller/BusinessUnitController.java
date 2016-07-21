package company.tothepoint.controller;

import company.tothepoint.model.businessunit.*;
import company.tothepoint.repository.BusinessUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/business-units")
public class BusinessUnitController {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessUnitController.class);
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
        LOG.debug("GET /business-units getAllBusinessUnits() called!");

        List<BusinessUnit> businessUnits = businessUnitRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getNaam().compareTo(o2.getNaam()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(businessUnits, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BusinessUnit> getBusinessUnit(@PathVariable("id") String id) {
        LOG.debug("GET /business-units/"+id+" getBusinessUnit("+id+") called!");
        Optional<BusinessUnit> businessUnitOption = Optional.ofNullable(businessUnitRepository.findOne(id));

        return businessUnitOption.map(businessUnit->
            new ResponseEntity<>(businessUnit, HttpStatus.OK)
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BusinessUnit> createBusinessUnit(@Validated @RequestBody BusinessUnit businessUnit) {
        LOG.debug("POST /business-units createBusinessUnit(..) called!");
        BusinessUnit createdBusinessUnit = businessUnitRepository.save(businessUnit);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, new BusinessUnitCreatedNotification("businessUnitCreated", createdBusinessUnit));
        return new ResponseEntity<>(createdBusinessUnit, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<BusinessUnit> updateBusinessUnit(@PathVariable("id") String id, @Validated @RequestBody BusinessUnit businessUnit) {
        LOG.debug("PUT /business-units/"+id+" updateBusinessUnit("+id+", ..) called!");
        Optional<BusinessUnit> existingBusinessUnit = Optional.ofNullable(businessUnitRepository.findOne(id));

        return existingBusinessUnit.map(bu ->
            {
                bu.setNaam(businessUnit.getNaam());
                rabbitTemplate.convertAndSend(exchangeName, routingKey, new BusinessUnitUpdatedNotification("businessUnitUpdated", bu));
                return new ResponseEntity<>(businessUnitRepository.save(bu), HttpStatus.OK);
            }
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<BusinessUnit> deleteBusinessUnit(@PathVariable("id") String id) {
        LOG.debug("DELETE /business-units/"+id+" deleteBusinessUnit("+id+") called!");
        if (businessUnitRepository.exists(id)) {
            businessUnitRepository.delete(id);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, new BusinessUnitDeletedNotification("businessUnitDeleted", id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

package company.tothepoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.BusinessunitApplication;
import company.tothepoint.model.BusinessUnit;
import company.tothepoint.repository.BusinessUnitRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BusinessunitApplication.class)
@WebAppConfiguration
public class BusinessUnitControllerTest {
    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;


    private List<BusinessUnit> originalData;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        originalData = businessUnitRepository.findAll();
        businessUnitRepository.deleteAll();
    }

    @After
    public void tearDown() {
        businessUnitRepository.deleteAll();
        businessUnitRepository.save(originalData);
    }

    @Test
    public void listBusinessUnits() throws Exception {
        businessUnitRepository.save(new BusinessUnit("ToThePoint"));
        businessUnitRepository.save(new BusinessUnit("TBA"));

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("The business unit's unique identifier"),
                        fieldWithPath("[].naam").description("The business unit's name")
                )
        );

        this.mockMvc.perform(
                get("/business-units").accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getBusinessUnit() throws Exception {
        BusinessUnit businessUnit = businessUnitRepository.save(new BusinessUnit("ToThePoint"));

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The business unit's unique identifier"),
                        fieldWithPath("naam").description("The business unit's name")
                )
        );

        this.mockMvc.perform(
                get("/business-units/" + businessUnit.getId()).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void createBusinessUnit() throws Exception {
        Map<String, String> newBusinessUnit = new HashMap<>();
        newBusinessUnit.put("naam", "ToThePoint");

        ConstrainedFields fields = new ConstrainedFields(BusinessUnit.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("naam").description("The business unit's name")
                )
        );

        this.mockMvc.perform(
                post("/business-units").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(newBusinessUnit)
                )
        ).andExpect(status().isCreated());
    }

    @Test
    public void updateBusinessUnit() throws Exception {
        BusinessUnit originalBusinessUnit = businessUnitRepository.save(new BusinessUnit("Solution Architects"));

        Map<String, String> updatedBusinessUnit = new HashMap<>();
        updatedBusinessUnit.put("naam", "ToThePoint");

        ConstrainedFields fields = new ConstrainedFields(BusinessUnit.class);

        this.document.snippets(
                requestFields(
                        fields.withPath("naam").description("The business unit's name")
                )
        );

        this.mockMvc.perform(
                put("/business-units/" + originalBusinessUnit.getId()).contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(updatedBusinessUnit)
                )
        ).andExpect(status().isOk());
    }


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}

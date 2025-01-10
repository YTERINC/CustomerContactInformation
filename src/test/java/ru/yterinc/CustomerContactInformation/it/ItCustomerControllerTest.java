package ru.yterinc.CustomerContactInformation.it;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.repositories.CustomerRepository;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.util.CustomerValidator;
import ru.yterinc.CustomerContactInformation.util.DataUtils;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItCustomerControllerTest extends AbstractRestControllerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerValidator customerValidator;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test create developer functionality")
    public void givenCustomerDTO_whenCreateCustomer_thenSuccessResponse() throws Exception {
        //given
        CustomerDTO dto = DataUtils.getJohnDTOTransient();
        //when
        ResultActions resultActions = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("John")));
    }
    @Test
    @DisplayName("Test create developer functionality with error")
    public void givenCustomerDTO_whenCreateCustomer_thenErrorResponse() throws Exception {
        //given
        CustomerDTO dto = CustomerDTO.builder()
                .name("J")
                .build();
        //when
        ResultActions resultActions = mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("name - Name should be between 2 and 30 characters;")));
    }

    @Test
    @DisplayName("Test update customer functionality")
    public void givenCustomerDto_whenUpdateCustomer_thenSuccessResponse() throws Exception {
        //given
        String updateName = "updatedName";
        Customer customer = DataUtils.getJohnTransient();
        customerRepository.save(customer);
        CustomerDTO dto = DataUtils.getJohnDTOPersisted();
        dto.setName(updateName);
        //when
        ResultActions result = mockMvc.perform(put("/customer/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updateName)));
    }

    @Test
    @DisplayName("Test update customer with incorrect id")
    public void givenCustomerDto_whenUpdateCustomer_thenErrorResponse() throws Exception {
        //given
        CustomerDTO dto = DataUtils.getJohnDTOPersisted();

        //when
        ResultActions result = mockMvc.perform(put("/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer with id wasn't found")));
    }

    @Test
    @DisplayName("Test get customer by id functionality")
    public void givenId_whenGetById_thenSuccessResponse() throws Exception {
        //given
        Customer customer = DataUtils.getJohnTransient();
        customerRepository.save(customer);
        //when
        ResultActions result = mockMvc.perform(get("/customer/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("John")));
    }

    @Test
    @DisplayName("Test get customer by incorrect id functionality")
    public void givenIncorrectId_whenGetById_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(get("/customer/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer with id wasn't found")));
    }

    @Test
    @DisplayName("Test delete by id functionality")
    public void givenId_whenDelete_thenSuccessResponse() throws Exception {
        //given
        Customer customer = DataUtils.getJohnTransient();
        customerRepository.save(customer);
        //when
        ResultActions result = mockMvc.perform(delete("/customer/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        Customer obtainedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        assertThat(obtainedCustomer).isNull();
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test delete by incorrect id functionality")
    public void givenIncorrectId_whenDelete_thenErrorResponse() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(delete("/customer/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}

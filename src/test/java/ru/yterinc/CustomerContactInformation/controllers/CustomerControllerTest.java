package ru.yterinc.CustomerContactInformation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.util.CustomerNotFoundException;
import ru.yterinc.CustomerContactInformation.util.CustomerValidator;
import ru.yterinc.CustomerContactInformation.util.DataUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerValidator customerValidator;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Test create developer functionality")
    public void givenCustomerDTO_whenCreateCustomer_thenSuccessResponse() throws Exception {
        //given
        CustomerDTO dto = DataUtils.getJohnDTOTransient();
        Customer customer = DataUtils.getJohnPersisted();
        BDDMockito.given(customerService.addCustomer(any(Customer.class)))
                .willReturn(customer);
//        BDDMockito.doNothing().when(customerValidator).validate(any(CustomerDTO.class), any(BindingResult.class));

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
        Customer customer = DataUtils.getJohnPersisted();
        BDDMockito.given(customerService.addCustomer(any(Customer.class)))
                .willReturn(customer);

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
        CustomerDTO dto = DataUtils.getJohnDTOPersisted();
        Customer customer = DataUtils.getJohnPersisted();
        BDDMockito.given(customerService.updateCustomer(anyInt(), any(Customer.class)))
                .willReturn(customer);
        BDDMockito.given(customerService.findOneCustomer(anyInt())).willReturn(Optional.ofNullable(customer));
        //when
        ResultActions result = mockMvc.perform(put("/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("John")));
    }

    @Test
    @DisplayName("Test update customer with incorrect id")
    public void givenCustomerDto_whenUpdateCustomer_thenErrorResponse() throws Exception {
        //given
        CustomerDTO dto = DataUtils.getJohnDTOPersisted();
        Customer customer = DataUtils.getJohnPersisted();
        BDDMockito.given(customerService.updateCustomer(anyInt(), any(Customer.class)))
                .willReturn(customer);
        BDDMockito.given(customerService.findOneCustomer(anyInt())).willReturn(Optional.empty());
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
        BDDMockito.given(customerService.findOneCustomer(anyInt()))
                .willReturn(Optional.ofNullable(DataUtils.getJohnPersisted()));
        //when
        ResultActions result = mockMvc.perform(get("/customer/1")
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
        BDDMockito.given(customerService.findOneCustomer(anyInt()))
                .willThrow(new CustomerNotFoundException());
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
        BDDMockito.doNothing().when(customerService).deleteCustomer(anyInt());
        BDDMockito.given(customerService.findOneCustomer(anyInt())).willReturn(Optional.ofNullable(DataUtils.getJohnPersisted()));
        //when
        ResultActions result = mockMvc.perform(delete("/customer/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(customerService, times(1)).deleteCustomer(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test delete by incorrect id functionality")
    public void givenIncorrectId_whenDelete_thenErrorResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(customerService).deleteCustomer(anyInt());
        BDDMockito.given(customerService.findOneCustomer(anyInt())).willReturn(Optional.empty());
        //when
        ResultActions result = mockMvc.perform(delete("/customer/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(customerService, times(0)).deleteCustomer(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

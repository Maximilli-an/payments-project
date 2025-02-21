package com.payments.api;

import com.payments.dataModel.Payment;
import com.payments.BackendApplication;
import com.payments.service.PaymentService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
class PaymentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentApi paymentApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentApi).build();
    }

    @Test
    void testCreatePaymentSuccess() throws Exception {
        String cardNumber = "1234567891234567";
        String expectedHashedCardNum = DigestUtils.sha256Hex(cardNumber);

        Payment mockSavedPayment = new Payment();
        mockSavedPayment.setFirstName("Max");
        mockSavedPayment.setLastName("Nash");
        mockSavedPayment.setZipCode("67890");
        mockSavedPayment.setHashedCardNum(expectedHashedCardNum);

        when(paymentService.savePayment(any())).thenReturn(mockSavedPayment);

        String requestBody = """
        {
            "firstName": "Max",
            "lastName": "Nash",
            "zipCode": "67890",
            "cardNumber": "1234567891234567"
        }
        """;


        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Max"))
                .andExpect(jsonPath("$.data.lastName").value("Nash"))
                .andExpect(jsonPath("$.data.hashedCardNum").value(expectedHashedCardNum));
    }

    @Test
    void testMissingZipCodeCreatePayment() throws Exception {
        String requestBody = """
    {
        "firstName": "Jane",
        "lastName": "Smith",
        "cardNumber": "22223333444455556"
    }
    """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value(containsString("Failed to save your details:")));
    }

    @Test
    void testCreatePaymentInternalServerError() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(paymentService).savePayment(any());

        String requestBody = """
    {
        "firstName": "John",
        "lastName": "Smith",
        "zipCode": "67890",
        "cardNumber": "99998888777766665"
    }
    """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value(containsString("There has been an error on our side, please note:")));
    }

}

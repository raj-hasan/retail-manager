package manager.retail.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import manager.retail.BaseTest;
import manager.retail.main.RetailMessages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Locale;
/**
 * @CreatedBy Hasan on 26-Feb-2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RetailManagerController.class)
@AutoConfigureMockMvc
public class RetailManagerControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkStatus() throws Exception {
        this.mockMvc.perform(get("/").header("Accept-Language", Locale.getDefault())).andDo(print()).andExpect(status().isOk())
                .andExpect(result -> {result.toString().equals(RetailMessages.SUCCESS);});
    }
}

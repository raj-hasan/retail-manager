package manager.retail;

import manager.retail.datastore.ShopInMemoryArray;
import manager.retail.service.shop.ShopLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @CreatedBy Hasan on 26-Feb-2017.
 */
@ContextConfiguration({"/test-spring-context.xml"})
public class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ShopLocator shopLocator;

    @Autowired
    protected ShopInMemoryArray shopInMemoryArray;

}

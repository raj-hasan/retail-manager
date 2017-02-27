package manager.retail.models;

import com.google.maps.model.LatLng;
import manager.retail.BaseTest;
import manager.retail.datastore.ShopInMemoryArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @CreatedBy Hasan on 26-Feb-2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelTest extends BaseTest {

    @Test
    public void testShopModel() throws JsonProcessingException {
        Shop shop = new Shop();
        shop.setShopName("Test Shop");
        shop.setShopAddress(new Shop.ShopAddress("1234", 411014));
        shop.setShopLongitude(18.5793);
        shop.setShopLatitude(73.9823);

        String expected = "{\"shopName\" : \"Test Shop\",\"shopAddress\" : {\"number\": \"1234\",\"postCode\" : 411014}, \"shopLatitude\": 18.5793, \"shopLongitude\" : 73.9823}";
        String actual = objectMapper.writeValueAsString(shop);

        JSONAssert.assertEquals(expected, actual, false);

        Shop shop2 = new Shop();
        shop2.setShopName("Test Shop");
        shop2.setShopAddress(new Shop.ShopAddress("1234", 411014));
        shop2.setShopLongitude(18.5793);
        shop2.setShopLatitude(73.9823);
        assert(shop.equals(shop2));

        shop2.setShopName("test name");
        assert(!shop.equals(shop2));
    }

    /*@Test
    public void testLocationModel() {
        LatLng location1 = new LatLng(73.9823, 18.5793);
        LatLng location2 = new LatLng(73.9823, 18.5793);
        assert(location1.equals(location2));

        location1 = new LatLng(0.00, 0.000);
        location2 = new LatLng(0.0, 0.0);
        assert(location1.equals(location2));
    }*/

    @Test
    public void testShopListHolder() {
        ShopInMemoryArray shopInMemoryArray = new ShopInMemoryArray();
        Shop shop = new Shop();
        shop.setShopName("Test Shop");
        shop.setShopAddress(new Shop.ShopAddress("1234", 411014));
        shop.setShopLongitude(18.5793);
        shop.setShopLatitude(73.9823);
        shopInMemoryArray.add(shop);
        Assert.assertTrue(shop.equals(shopInMemoryArray.get(0)));
        Shop shop2 = new Shop();
        shop2.setShopName("Test Shop");
        shop2.setShopAddress(new Shop.ShopAddress("1234", 411014));
        shop2.setShopLongitude(18.5793);
        shop2.setShopLatitude(73.9823);
        shopInMemoryArray.add(shop2);

        Assert.assertTrue(shopInMemoryArray.getAll().size() == 2);

        shopInMemoryArray.remove(shop);
        Assert.assertTrue(shopInMemoryArray.getAll().size() == 1);
        Assert.assertTrue(shop2.equals(shopInMemoryArray.get(0)));

        shopInMemoryArray.remove(0);
        Assert.assertTrue(shopInMemoryArray.getAll().size() == 0);
    }
}

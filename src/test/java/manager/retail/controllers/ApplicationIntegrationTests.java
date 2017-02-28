package manager.retail.controllers;

import manager.retail.BaseTest;
import manager.retail.datastore.ShopInMemoryArray;
import manager.retail.main.RetailMessages;
import manager.retail.models.Shop;
import manager.retail.service.shop.ShopLocator;
import manager.retail.service.shop.ShopLocatorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

/**
 * @CreatedBy Hasan on 26-Feb-2017.
 */

public class ApplicationIntegrationTests extends BaseTest {
    @Before
    public void setUp() {
        ShopInMemoryArray shopInMemoryArray = new ShopInMemoryArray();
        ShopLocator shopLocatorService = new ShopLocatorImpl();
        shopLocatorService.setInMemoryArray(shopInMemoryArray);
        RetailManagerController retailManagerController = new RetailManagerController();
        retailManagerController.setShopLocator(shopLocatorService);
        retailManagerController.setObjectMapper(new ObjectMapper());
        RestAssuredMockMvc.standaloneSetup(retailManagerController);
    }

    @Test
    public void testApplicationStatus() {
        given().when().get("/shop/").then().statusCode(200);
    }

    @Test
    public void testAddShop() {
        // Bad request - Missing body
        given().when().post("/shop/add").then().statusCode(400);
        Shop shop = new Shop();
        shop.setShopName("My Shop");
        // Missing Address
        given().contentType("application/json").body(shop).when().post("/shop/add").then().statusCode(400);

        // Address is needed in a request body
        shop.setShopAddress(new Shop.ShopAddress("Address", 123));
        given().contentType("application/json").body(shop).when().post("/shop/add").then().statusCode(201);
    }

    @Test
    public void testResponseJson() {
        // Bad Request since request param is null
        given().when().get("/shop/find?customerLatitude=&customerLongitude").then().statusCode(400);
        // Bad Request since request param's value is invalid
        given().when().get("/shop/find?customerLatitude=qwertyu&customerLongitude=tyu").then().statusCode(400);
        // No shops has been added yet
        given().when().get("/shop/find?customerLatitude=10&customerLongitude=89").then().statusCode(200).assertThat(
                result -> {result.toString().equals(RetailMessages.NO_SHOPS_ADDED);}
        );
    }
}


package manager.retail.service;

import manager.retail.BaseTest;
import manager.retail.exception.RetailManagerException;
import manager.retail.main.Config;
import manager.retail.models.Shop;
import manager.retail.service.shop.ShopLocatorImpl;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @CreatedBy Hasan on 26-Feb-2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ShopLocatorTests extends BaseTest {

    LatLng locationToTest = null;

    @Before
    public void setData() throws Exception {
         locationToTest = setLocationFromGoogleApi("EON IT Park, Kharadi, Pune".concat(",").concat(411014 + ""));
    }

    @Test
    public void testGeoApiResolver() {
        String address = "EON IT Park, Kharadi, Pune".concat(",").concat(411014 + "");
        LatLng location = ShopLocatorImpl.geoApiResolver(address);
        assert (location.equals(locationToTest));
    }

    @Test(expected = RetailManagerException.class)
    public void testSave() {
        Shop shop = buildShop("Test Shop", "EON IT Park, Kharadi, Pune",
                411014,locationToTest.lat,locationToTest.lng);

        shopLocator.save(shop);
        assert (shopLocator.getAll().get(0).equals(shop));

    }


    @Test
    public void testNearest() {
        Shop s1 = buildShop("Shop 1", "Number 1", 1, 1.0, 1.0);
        Shop s2 = buildShop("Shop 2", "Number 2", 2, 2.0, 2.0);
        Shop s3 = buildShop("Shop 2", "Number 2", 2, -2.0, -2.0);
        Shop s4 = buildShop("Shop 2", "Number 2", 2, -2.0, 2.0);
        Shop s5 = buildShop("Shop 2", "Number 2", 2, 2.0, -2.0);
        shopInMemoryArray.add(s1);
        shopInMemoryArray.add(s2);
        shopInMemoryArray.add(s3);
        shopInMemoryArray.add(s4);
        shopInMemoryArray.add(s5);

        assert shopLocator.findNearest(new LatLng(0.0,0.0)).equals(s1);

        assert shopLocator.findNearest(new LatLng(2.0,-1.0)).equals(s1);

        assert shopLocator.findNearest(new LatLng(1.0,1.0)).equals(s1);

    }

    private Shop buildShop(String name, String number, int post, Double latitude, Double longitude) {
        Shop shop = new Shop();
        shop.setShopName(name);
        shop.setShopAddress(new Shop.ShopAddress(number, post));
        shop.setShopLatitude(latitude);
        shop.setShopLongitude(longitude);
        return shop;
    }

    private LatLng setLocationFromGoogleApi(String address) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(Config.GEO_API_KEY);
        GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
        LatLng location = result.geometry.location;
        return location;
    }
}

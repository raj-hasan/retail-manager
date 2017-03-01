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
        assert (isSameLocation(location,locationToTest));
    }

    @Test
    public void testSave() {
        Shop shop = buildShopObject("Test Shop", "EON IT Park, Kharadi, Pune",
                411014,locationToTest.lat,locationToTest.lng);

        shopLocator.save(shop);
        assert (shopLocator.getAll().get(0).equals(shop));
    }

    @Test
    public void testNearest() {
        Shop wagholi = buildShopObject("Shop  at Wagholi", "Number 1", 412207, 73.9823, 18.5793);
        Shop yarwada = buildShopObject("Shop at Yarwada", "Number 2", 2, 73.8796, 18.5529);
        Shop pimpri = buildShopObject("Shop at Pimpri", "Number 3", 3, 73.7997, 18.6298);
        Shop kolkata = buildShopObject("Shop at Kolkata", "Number 4", 700001, 88.3639, 22.5726);
        Shop bhopal = buildShopObject("Shop at Bhopal", "Number 5", 5, 77.4126, 23.2599);
        Shop mumbai = buildShopObject("Shop at Mumbai", "Number 6", 6, 72.8777, 19.076);
        shopInMemoryArray.add(wagholi);
        shopInMemoryArray.add(yarwada);
        shopInMemoryArray.add(pimpri);
        shopInMemoryArray.add(kolkata);
        shopInMemoryArray.add(bhopal);
        shopInMemoryArray.add(mumbai);

        //Latitude and Longitude of Viman Nagar is 73.9143	18.5679
        //Nearest shop from Viman Nagar should be at Yearwada
        assert shopLocator.findNearest(new LatLng(73.9143,18.5679)).equals(yarwada);
        //Latitude and Longitude of Aurangabad is 75.3433	19.8762
        //Nearest shop from Aurangabad should be at Wagholi
        assert shopLocator.findNearest(new LatLng(75.3433,19.8762)).equals(wagholi);
        //Latitude and Longitude of Mumbai is 72.8777	19.076
        //Nearest shop from Mumbai should be at Munbai itself
        assert shopLocator.findNearest(new LatLng(72.8777,19.076)).equals(mumbai);
    }

    private Shop buildShopObject(String name, String number, int post, Double latitude, Double longitude) {
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

    private boolean isSameLocation(LatLng l1, LatLng l2){
        if(l1==null || l2==null){
            return false;
        }
        if(l1.lat==l2.lat && l1.lng==l2.lng){
            return true;
        }
        else{
            return false;
        }
    }
}

package manager.retail.service.shop;

import manager.retail.datastore.ShopInMemoryArray;
import manager.retail.main.Config;
import manager.retail.exception.RetailManagerException;
import manager.retail.main.RetailMessages;
import manager.retail.models.Shop;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;


/**
 * @CreatedBy Hasan on 25-Feb-2017.
 */
public class ShopLocatorImpl implements ShopLocator {
    private static final Logger logger = LoggerFactory.getLogger(ShopLocatorImpl.class);

    @Autowired
    private ShopInMemoryArray inMemoryArray;

    @Override
    public void save(Shop shop) {
        validate(shop);
        // Query string for the Geo API
        Shop.ShopAddress shop_address = shop.getShopAddress();
        StringBuilder address = new StringBuilder(shop_address.getNumber()).append(",").append(shop_address.getPostCode());
        // Get the latitude & longitude
        LatLng location = geoApiResolver(address.toString());
        shop.setShopLatitude(location.lat);
        shop.setShopLongitude(location.lng);
        // Add into in-memory data store
        inMemoryArray.add(shop);
    }

    @Override
    public Shop findNearest(LatLng location) {
        List<Shop> shops = inMemoryArray.getAll();
        if(shops == null || shops.isEmpty()) {
            logger.info(RetailMessages.NO_SHOPS_ADDED);
            throw new RetailManagerException(RetailMessages.NO_SHOPS_ADDED, HttpStatus.OK);
        }
        // Minimum is 0.0 since distance with itself will be 0
        double nearest = calculateDistance(location, new LatLng(shops.get(0).getShopLatitude(),shops.get(0).getShopLongitude()));
        double temp;
        Shop nearest_shop = shops.get(0);

        for(int i=1; i < shops.size() ; i++) {
            temp = calculateDistance(location, new LatLng(shops.get(i).getShopLatitude(),shops.get(i).getShopLongitude()));
            /** If distance of shops are equal, the first one found is returned
             *  Since '<' is used in comparison
             */
            if (temp < nearest) {
                nearest = temp;
                nearest_shop = shops.get(i);
            }
        }

        if(nearest == 0.0) {
            logger.info("Found shop with an exact location match.");
        }
        return nearest_shop;
    }

    @Override
    public List<Shop> getAll() {
        return inMemoryArray.getAll();
    }

    public static LatLng geoApiResolver(String address_in_one_line) {
        GeoApiContext context = new GeoApiContext().setApiKey(Config.GEO_API_KEY);
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address_in_one_line).await()[0];
            LatLng location = result.geometry.location;
            return location;
        } catch (Exception e) {
            logger.error("Error while fetching data from google geo api.", e);
            throw new RetailManagerException(e, "Error while retrieving location data for the shop. Please try again",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private Double calculateDistance(LatLng l1, LatLng l2) {
        Double diff_lat = l1.lat - l2.lng;
        Double diff_lon = l1.lat - l2.lng;

        return Math.sqrt(diff_lat * diff_lat + diff_lon * diff_lon);
    }

    private void validate(Shop shop) {
        if(shop == null || shop.getShopAddress() == null ||
                shop.getShopAddress().getNumber() == null|| shop.getShopAddress().getPostCode() ==  0) {
            logger.debug("Invalid shop - " + shop);
            throw new RetailManagerException("Invalid shop details. No Address found", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ShopInMemoryArray getInMemoryArray() {
        return inMemoryArray;
    }
    @Override
    public void setInMemoryArray(ShopInMemoryArray inMemoryArray) {
        this.inMemoryArray = inMemoryArray;
    }
}

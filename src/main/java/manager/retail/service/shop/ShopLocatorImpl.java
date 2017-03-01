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

        double nearest = calculateDistance(location, new LatLng(shops.get(0).getShopLatitude(),shops.get(0).getShopLongitude()));
        double temp;
        Shop nearest_shop = shops.get(0);

        for(int i=1; i < shops.size() ; i++) {
            temp = calculateDistance(location, new LatLng(shops.get(i).getShopLatitude(),shops.get(i).getShopLongitude()));
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
        double theta = l1.lng - l2.lng;
        double distance = Math.sin(deg2rad(l1.lat)) * Math.sin(deg2rad(l2.lat)) + Math.cos(deg2rad(l1.lat)) *
                Math.cos(deg2rad(l2.lat)) * Math.cos(deg2rad(theta));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        return distance * 60 * 1.1515;
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
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

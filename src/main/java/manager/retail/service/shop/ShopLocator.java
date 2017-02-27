package manager.retail.service.shop;

import com.google.maps.model.LatLng;
import manager.retail.datastore.ShopInMemoryArray;
import manager.retail.models.Shop;
import java.util.List;

/**
 * Retail Manager Shop's APIs
 * @CreatedBy Hasan on 26-Feb-2017
 */
public interface ShopLocator {
    void save(Shop shop);
    Shop findNearest(LatLng location);
    List<Shop> getAll();
    ShopInMemoryArray getInMemoryArray();
    void setInMemoryArray(ShopInMemoryArray inMemoryArray);
}

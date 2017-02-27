package manager.retail.datastore;

import manager.retail.models.Shop;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shop in-memory array to store details of the shops
 * @CreatedBy Hasan on 22-Feb-2017
 */
public class ShopInMemoryArray {
    @NotNull
    private List<Shop> shops = Collections.synchronizedList(new ArrayList<>());

    public Shop get(int index) {
        return shops.get(index);
    }

    public void add(Shop Shop) {
        shops.add(Shop);
    }

    public void remove(Shop Shop) {
        shops.remove(Shop);
    }

    public void remove(int index) {
        shops.remove(index);
    }

    public List<Shop> getAll() {
        return Collections.unmodifiableList(shops);
    }
}

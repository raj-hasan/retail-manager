package manager.retail.models;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
* @CreatedBy Hasan on 22-Feb-2017
* Model class for Shop data. Shop class has below attributes -
* shopName
* shopAddress.number
* shopAddress.postCode
* shopLongitude
* shopLatitude
*/

public class Shop {
    private String shopName;
    @NotNull
    private ShopAddress shopAddress;
    private Double shopLatitude;
    private Double shopLongitude;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopAddress getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(ShopAddress shopAddress) {
        this.shopAddress = shopAddress;
    }

    public Double getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(Double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public Double getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(Double shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (!shopName.equals(shop.shopName)) return false;
        if (shopAddress != null && !shopAddress.equals(shop.shopAddress)) return false;
        if (shopLatitude != shop.shopLatitude) return false;
        return shopLongitude == shop.shopLongitude;
    }

    @Override
    public int hashCode() {
        int result = shopName.hashCode();
        result = 31 * result + (shopAddress != null ? shopAddress.hashCode() : 0);
        result = 31 * result + (shopLatitude != null ? shopLatitude.hashCode() : 0);
        result = 31 * result + (shopLongitude != null ? shopLongitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopName='" + shopName + '\'' +
                ", shopAddress=" + shopAddress +
                ", shopLatitude=" + shopLatitude +
                ", shopLongitude=" + shopLongitude +
                '}';
    }

    public static class ShopAddress {
        @NotNull
        private String number;
        @Min(1)
        private int postCode;

        public ShopAddress(){
            //default no-argument constructor
        }
        public ShopAddress(String number, int postCode) {
            this.number = number;
            this.postCode = postCode;
        }
        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getPostCode() {
            return postCode;
        }

        public void setPostCode(int postCode) {
            this.postCode = postCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ShopAddress that = (ShopAddress) o;

            if (number != null && !number.equals(that.number)) return false;
            return postCode == that.postCode;
        }

        @Override
        public int hashCode() {
            int result = number != null ? number.hashCode() : 0;
            result = 31 * result + postCode;
            return result;
        }

        @Override
        public String toString() {
            return "ShopAddress{" +
                    "number='" + number + '\'' +
                    ", postCode=" + postCode +
                    '}';
        }
    }
}

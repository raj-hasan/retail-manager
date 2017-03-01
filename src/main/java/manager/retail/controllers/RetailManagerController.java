package manager.retail.controllers;

import com.google.maps.model.LatLng;
import manager.retail.exception.RetailManagerException;
import manager.retail.main.RetailMessages;
import manager.retail.models.ServiceResponse;
import manager.retail.models.Shop;
import manager.retail.service.shop.ShopLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
RetailManager Rest Controller
@CreatedBy Hasan on 25-Feb-2017
*/

@RestController
@RequestMapping("/shop")
public class RetailManagerController {
    @Autowired
    ShopLocator shopLocator;
    @Autowired
    ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(RetailManagerController.class);

    @RequestMapping("/")
    public String status() {
        return RetailMessages.SUCCESS;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addShop(HttpServletResponse response, @Validated @RequestBody Shop shop){
        String msg;
        try {
            shopLocator.save(shop);
            msg = objectMapper.writeValueAsString(new ServiceResponse(true));
            response.setStatus(HttpStatus.CREATED.value());
        } catch (IOException exception) {
            logger.error(RetailMessages.SHOP_ADD_FAILED, exception);
            throw new RetailManagerException(exception, RetailMessages.NOT_PROCESSED, HttpStatus.OK);
        }
        return msg ;
    }

    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public String getNearestShop(@RequestParam("customerLongitude") String longitude,
                                 @RequestParam("customerLatitude") String latitude) {
        String result = null;
        try {
            LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            result = objectMapper.writeValueAsString(shopLocator.findNearest(location));
            return result;
        } catch (NumberFormatException e) {
            logger.error(RetailMessages.INVALID_LOCATION + " - "+longitude+ ", "+latitude, e);
            throw new RetailManagerException(e, RetailMessages.INVALID_LOCATION, HttpStatus.BAD_REQUEST);
        } catch (RetailManagerException rmse) {
            throw rmse;
        }
        catch (Exception e) {
            logger.error(RetailMessages.ERROR_SHOP + latitude + ", " +longitude , e);
            throw new RetailManagerException(e, RetailMessages.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    //Added this service for testing purpose
    /*@RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<Shop> getAllShop() {
        String result = null;
        try {
            return shopLocator.getAll();

        }
        catch (Exception e) {
            logger.error(RetailMessages.ERROR_SHOP , e);
            throw new RetailManagerException(e, RetailMessages.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }*/

    @ExceptionHandler(value = RetailManagerException.class)
    public String handler(RetailManagerException e, HttpServletResponse response) {
        response.setStatus(e.getHttpStatusCode().value());
        return e.getMessage();
    }

    public void setShopLocator(ShopLocator shopLocator) {
        this.shopLocator = shopLocator;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

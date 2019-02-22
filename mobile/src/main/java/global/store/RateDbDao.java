package global.store;

import global.AirWireRate;

/**
 * Created by akshaynexus on 3/3/18.
 */

public interface RateDbDao<T> extends AbstractDbDao<T>{

    AirWireRate getRate(String coin);


    void insertOrUpdateIfExist(AirWireRate airwireRate);

}

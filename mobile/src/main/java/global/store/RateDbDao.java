package global.store;

import global.CryptoDezireCashRate;

/**
 * Created by akshaynexus on 3/3/18.
 */

public interface RateDbDao<T> extends AbstractDbDao<T>{

    CryptoDezireCashRate getRate(String coin);


    void insertOrUpdateIfExist(CryptoDezireCashRate cryptodezirecashRate);

}

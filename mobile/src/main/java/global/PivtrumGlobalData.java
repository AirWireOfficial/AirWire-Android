package global;

import java.util.ArrayList;
import java.util.List;

import pivtrum.PivtrumPeerData;

/**
 * Created by akshaynexus on 7/2/17.
 */

public class PivtrumGlobalData {

    public static final String FURSZY_TESTNET_SERVER = "seed01.cryptodezirecash.com";

    public static final String[] TRUSTED_NODES = new String[]{"seed01.cryptodezirecash.com","seed02.cryptodezirecash.com","seed03.cryptodezirecash.com"};

    public static final List<PivtrumPeerData> listTrustedHosts(){
        List<PivtrumPeerData> list = new ArrayList<>();
        list.add(new PivtrumPeerData(FURSZY_TESTNET_SERVER,35601,55552));
        for (String trustedNode : TRUSTED_NODES) {
            list.add(new PivtrumPeerData(trustedNode,35601,55552));
        }
        return list;
    }

}

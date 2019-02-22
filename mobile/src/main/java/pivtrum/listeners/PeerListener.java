package pivtrum.listeners;

import pivtrum.PivtrumPeer;

/**
 * Created by akshaynexus on 6/17/17.
 */

public interface PeerListener {

    void onConnected(PivtrumPeer pivtrumPeer);

    void onDisconnected(PivtrumPeer pivtrumPeer);

    void onExceptionCaught(PivtrumPeer pivtrumPeer, Exception e);
}

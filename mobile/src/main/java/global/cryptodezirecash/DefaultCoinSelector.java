package global.cryptodezirecash;

import org.cryptodezirecashj.core.Coin;
import org.cryptodezirecashj.core.NetworkParameters;
import org.cryptodezirecashj.core.Sha256Hash;
import org.cryptodezirecashj.core.Transaction;
import org.cryptodezirecashj.core.TransactionConfidence;
import org.cryptodezirecashj.core.TransactionOutput;
import org.cryptodezirecashj.wallet.CoinSelection;
import org.cryptodezirecashj.wallet.CoinSelector;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a {@link CoinSelector} which attempts to get the highest priority
 * possible. This means that the transaction is the most likely to get confirmed. Note that this means we may end up
 * "spending" more priority than would be required to get the transaction we are creating confirmed.
 */
public class DefaultCoinSelector implements CoinSelector {

    private Map<OutputIndex,TransactionOutput> usedOutputs = new HashMap<>();

    public DefaultCoinSelector(List<TransactionOutput> usedOutputs) {
        for (TransactionOutput usedOutput : usedOutputs) {
            this.usedOutputs.put(
                   new OutputIndex(usedOutput.getParentTransactionHash(),usedOutput.getIndex()),
                    usedOutput
            );
        }
    }

    @Override
    public CoinSelection select(Coin target, List<TransactionOutput> candidates) {
        ArrayList<TransactionOutput> selected = new ArrayList<TransactionOutput>();
        // Sort the inputs by age*value so we get the highest "coindays" spent.
        // TODO: Consider changing the wallets internal format to track just outputs and keep them ordered.
        ArrayList<TransactionOutput> sortedOutputs = new ArrayList<TransactionOutput>(candidates);
        // When calculating the wallet balance, we may be asked to select all possible coins, if so, avoid sorting
        // them in order to improve performance.
        // TODO: Take in network parameters when instanatiated, and then test against the current network. Or just have a boolean parameter for "give me everything"
        if (!target.equals(NetworkParameters.MAX_MONEY)) {
            sortOutputs(sortedOutputs);
        }
        // Now iterate over the sorted outputs until we have got as close to the target as possible or a little
        // bit over (excessive value will be change).
        long total = 0;
        for (TransactionOutput output : sortedOutputs) {
            if (total >= target.value) break;
            // Only pick chain-included transactions, or transactions that are ours and pending.
            if(!shouldSelect(output))continue;
            if (!shouldSelect(output.getParentTransaction())) continue;
            selected.add(output);
            total += output.getValue().value;
        }
        // Total may be lower than target here, if the given candidates were insufficient to create to requested
        // transaction.
        return new CoinSelection(Coin.valueOf(total), selected);
    }

    private void sortOutputs(ArrayList<TransactionOutput> outputs) {
        Collections.sort(outputs, new Comparator<TransactionOutput>() {
            @Override
            public int compare(TransactionOutput a, TransactionOutput b) {
                int depth1 = a.getParentTransactionDepthInBlocks();
                int depth2 = b.getParentTransactionDepthInBlocks();
                Coin aValue = a.getValue();
                Coin bValue = b.getValue();
                BigInteger aCoinDepth = BigInteger.valueOf(aValue.value).multiply(BigInteger.valueOf(depth1));
                BigInteger bCoinDepth = BigInteger.valueOf(bValue.value).multiply(BigInteger.valueOf(depth2));
                int c1 = bCoinDepth.compareTo(aCoinDepth);
                if (c1 != 0) return c1;
                // The "coin*days" destroyed are equal, sort by value alone to get the lowest transaction size.
                int c2 = bValue.compareTo(aValue);
                if (c2 != 0) return c2;
                // They are entirely equivalent (possibly pending) so sort by hash to ensure a total ordering.
                BigInteger aHash = a.getParentTransactionHash().toBigInteger();
                BigInteger bHash = b.getParentTransactionHash().toBigInteger();
                return aHash.compareTo(bHash);
            }
        });
    }

    /**
     * Should select output
     * @param output
     * @return
     */
    private boolean shouldSelect(TransactionOutput output) {
        return !usedOutputs.containsKey(new OutputIndex(output.getParentTransactionHash(),output.getIndex()));
    }

    /** Sub-classes can override this to just customize whether transactions are usable, but keep age sorting. */
    protected boolean shouldSelect(Transaction tx) {
        if (tx != null) {
            return isSelectable(tx);
        }
        return true;
    }

    public boolean isSelectable(Transaction tx) {
        // Only pick chain-included transactions, or transactions that are ours and pending.
        TransactionConfidence confidence = tx.getConfidence();
        TransactionConfidence.ConfidenceType type = confidence.getConfidenceType();
        return type.equals(TransactionConfidence.ConfidenceType.BUILDING) ||
                //type.equals(TransactionConfidence.ConfidenceType.INSTANTX_LOCKED) || //TODO:InstantX
               type.equals(TransactionConfidence.ConfidenceType.PENDING) &&
               confidence.getSource().equals(TransactionConfidence.Source.SELF) &&
               // In regtest mode we expect to have only one peer, so we won't see transactions propagate.
               // TODO: The value 1 below dates from a time when transactions we broadcast *to* were counted, set to 0
               (confidence.numBroadcastPeers() > 1 || tx.getParams().getId().equals(NetworkParameters.ID_REGTEST));
    }

    private static class OutputIndex{
        private Sha256Hash parentHash;
        private long index;

        public OutputIndex(Sha256Hash parentHash, long index) {
            this.parentHash = parentHash;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OutputIndex that = (OutputIndex) o;

            if (index != that.index) return false;
            return parentHash.equals(that.parentHash);

        }

        @Override
        public int hashCode() {
            int result = parentHash.hashCode();
            result = 31 * result + (int) (index ^ (index >>> 32));
            return result;
        }
    }
}
package vnpay.vn.harabbit.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;

/**
 * @author sontt1
 * Date:9/28/2022
 * Time:4:00 PM
 */
public class MyRecoveryListener implements RecoveryListener {
    @Override
    public void handleRecovery(Recoverable recoverable) {
        if (recoverable instanceof Channel) {
            ((Channel) recoverable).getChannelNumber();
        }
    }

    @Override
    public void handleRecoveryStarted(Recoverable recoverable) {

    }

    @Override
    public void handleTopologyRecoveryStarted(Recoverable recoverable) {
        RecoveryListener.super.handleTopologyRecoveryStarted(recoverable);
    }
}

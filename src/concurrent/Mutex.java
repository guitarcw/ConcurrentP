package concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Mutex {
    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;
        }
    }
}

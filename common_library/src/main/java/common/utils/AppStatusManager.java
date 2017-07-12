package common.utils;

public class AppStatusManager {
    private boolean isForeground;

    private AppStatusManager() {
    }

    private static class SingletonInstance {
        private static final AppStatusManager INSTANCE = new AppStatusManager();
    }

    public static AppStatusManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public boolean isForeground() {
        return isForeground;
    }

    public void setForeground(boolean foreground) {
        isForeground = foreground;
    }
}
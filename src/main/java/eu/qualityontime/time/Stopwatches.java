package eu.qualityontime.time;

import org.slf4j.*;

public class Stopwatches {
  private static final Logger log = LoggerFactory.getLogger(HierarchicalStopwatch.class);
  private static ThreadLocal<HierarchicalStopwatch> threadLocal = null;

  public static void reset() {
    if (isDisabled())
      return;
    getThreadLocal().set(new HierarchicalStopwatch());
  }

  public static void start(String eventName) {
    if (isDisabled())
      return;

    HierarchicalStopwatch stopWatch = getThreadlocalInstance();
    if (stopWatch != null) {
      stopWatch.startEvent(eventName);
    }
  }

  public static void stop() {
    if (isDisabled())
      return;

    HierarchicalStopwatch stopWatch = getThreadlocalInstance();
    if (stopWatch != null) {
      stopWatch.stopEvent();
    }
  }

  public static String prettyPrint() {
    if (isDisabled())
      return null;

    HierarchicalStopwatch stopWatch = getThreadlocalInstance();
    return (stopWatch != null) ? stopWatch.internalPrettyPrint() : null;
  }

  public static HierarchicalStopwatch getThreadlocalInstance() {
    return getThreadLocal().get();
  }

  private static ThreadLocal<HierarchicalStopwatch> getThreadLocal() {
    if (threadLocal == null) {
      threadLocal = new ThreadLocal<HierarchicalStopwatch>() {
        @Override
        protected HierarchicalStopwatch initialValue() {
          return new HierarchicalStopwatch();
        }
      };
    }
    return threadLocal;
  }

  private static boolean isDisabled() {
    return log.isDebugEnabled() == false;
  }

}

package eu.qualityontime.time;

import java.util.*;

import com.google.common.collect.ImmutableSet;

public class HierarchicalStopwatch {
  //private static final Logger log = LoggerFactory.getLogger(HierarchicalStopwatch.class);
  private static ThreadLocal<HierarchicalStopwatch> threadLocal = null;

  private long eventIndex = 0;
  private LinkedList<Event> events = new LinkedList<Event>();
  private Set<Event> eventsDone = new TreeSet<Event>(new EventComparator());

  public void startEvent(String eventName) {
    eventIndex++;
    events.push(new Event(eventIndex, events.size(), eventName));
  }

  public void stopEvent() {
    if (events.size() == 0)
      return;

    Event event = events.pop();
    event.stop();
    eventsDone.add(event);
  }

  public String internalPrettyPrint() {
    StringBuilder sb = new StringBuilder();
    if (events.size() != 0) {
      sb.append("WARNING: same events have not been marked as closed, ");
      sb.append("the summary might contain wrong data...\n");
    }
    for (Event event : eventsDone) {
      sb.append(getTabs(event.getDepth()))
          .append(event.getName()).append(" : ").append(event.getTime()).append(" ms\n");
    }

    return sb.toString();
  }

  private static String getTabs(int count) {
    StringBuilder sb = new StringBuilder(count);
    for (int i = 0; i < count; i++) {
      sb.append("\t");
    }
    return sb.toString();
  }

  public static HierarchicalStopwatch getThreadlocalInstance() {
    return getThreadLocal().get();
  }

  private static ThreadLocal<HierarchicalStopwatch> getThreadLocal() {
    if (threadLocal == null) {
      threadLocal = new ThreadLocal<HierarchicalStopwatch>();
    }
    return threadLocal;
  }

  public class Event {

    private long index;
    private int depth;
    private String name;
    private long startTime;
    private long time;

    public Event(long index, int depth, String name) {
      this.index = index;
      this.depth = depth;
      this.name = name;
      startTime = System.currentTimeMillis();
    }

    public void stop() {
      time = System.currentTimeMillis() - startTime;
    }

    public long getIndex() {
      return index;
    }

    public int getDepth() {
      return depth;
    }

    public String getName() {
      return name;
    }

    public long getTime() {
      return time;
    }
  }

  private class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event left, Event right) {
      if (left == null && right == null)
        return 0;
      if (left == null)
        return 1;
      if (right == null)
        return -1;
      return (int) (left.getIndex() - right.getIndex());
    }
  }

  public Set<Event> getEventsDone() {
    return ImmutableSet.copyOf(eventsDone);
  }
}

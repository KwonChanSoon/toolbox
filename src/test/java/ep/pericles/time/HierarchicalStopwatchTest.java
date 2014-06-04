package ep.pericles.time;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class HierarchicalStopwatchTest {

  @Test
  public void stopwatches() {
    //System.out.println("stopwatches");
    Stopwatches.reset();
    run();
    HierarchicalStopwatch sw = Stopwatches.getThreadlocalInstance();
    assertFalse(sw.getEventsDone().isEmpty());
    //System.out.println(Stopwatches.prettyPrint());
  }

  public void run() {
    Stopwatches.start("run()");
    foo();
    Stopwatches.stop();
  }

  public void foo() {
    Stopwatches.start("foo()");
    bar();
    bar();
    Stopwatches.stop();
  }

  public void bar() {
    Stopwatches.start("bar()");
    bang();
    bang();
    bang();
    Stopwatches.stop();
  }

  public void bang() {
    Stopwatches.start("bang()");
    sleepRandomly();
    Stopwatches.stop();
  }

  public void sleepRandomly() {
    try {
      Thread.sleep(System.currentTimeMillis() % 20);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

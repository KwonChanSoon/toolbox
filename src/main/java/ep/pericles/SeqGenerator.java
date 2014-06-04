package ep.pericles;

import java.util.concurrent.atomic.AtomicLong;

public final class SeqGenerator {
  private static final SeqGenerator instance = new SeqGenerator();
  private final AtomicLong sequence = new AtomicLong();
  private final boolean negativeIncrements;

  public SeqGenerator() {
    this(0L, false);
  }

  public SeqGenerator(long init, boolean negativeIncrements) {
    sequence.set(init);
    this.negativeIncrements = negativeIncrements;
  }

  public static long nextNum() {
    return instance.next();
  }

  public long next() {
    long currVal = sequence.incrementAndGet();
    return negativeIncrements ? -1L * currVal : currVal;
  }
}

package ep.pericles.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Function;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import ep.pericles.base.Try.Recovery;

@RunWith(HierarchicalContextRunner.class)
public class TryTest {

  public class SuccessCase {
    @Test
    public void create() throws Exception {
      assertNotNull(Try.of(1));
      assertThat(Try.of(1)).isNotNull().isInstanceOf(Try.Success.class);
    }

    @Test
    public void get() throws Exception {
      assertThat(Try.of(1).get()).isEqualTo(1);
    }

    @Test
    public void question() throws Exception {
      assertThat(Try.of(1).isSuccess()).isEqualTo(true);
      assertThat(Try.of(1).isFailure()).isEqualTo(false);
    }

    @Test
    public void map() throws Exception {
      Try<String> actual = Try.of(1).map(toStringF);
      assertThat(actual.get()).isEqualTo("1");
    }

    @Test
    public void flatMap() throws Exception {
      Try<String> actual = Try.of(1).flatMap(toTryString);
      assertThat(actual.get()).isEqualTo("1");
    }

    @Test
    public void recover() throws Exception {
      Try<Integer> actual = Try.of(1).recover(new RecoverF());
      assertThat(actual.get()).isEqualTo(1);
    }

    @Test
    public void recoverWith() throws Exception {
      Try<Integer> actual = Try.of(1).recoverWith(new RecoverWithF());
      assertThat(actual.get()).isEqualTo(1);
    }
  }

  public class FailureCase {
    Try<Integer> t = Try.of(new IllegalArgumentException("Test failure"));

    @Test
    public void create() throws Exception {
      assertThat(t).isNotNull().isInstanceOf(Try.Failure.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void get() throws Exception {
      assertThat(t.get()).isEqualTo(1);
    }

    @Test
    public void question() throws Exception {
      assertThat(t.isSuccess()).isEqualTo(false);
      assertThat(t.isFailure()).isEqualTo(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void map() throws Exception {
      Try<String> actual = t.map(toStringF);
      assertThat(actual.isFailure()).isTrue();
      actual.get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void flatMap() throws Exception {
      Try<String> actual = t.flatMap(toTryString);
      assertThat(actual.isFailure()).isTrue();
      actual.get();
    }

    @Test
    public void recover() throws Exception {
      Try<Integer> actual = t.recover(new RecoverF());
      assertThat(actual.get()).isEqualTo(2);
    }

    @Test
    public void recoverWith() throws Exception {
      Try<Integer> actual = t.recoverWith(new RecoverWithF());
      assertThat(actual.get()).isEqualTo(2);
    }
  }

  private final class RecoverF implements Recovery<Integer> {
    @Override
    public Integer apply(Throwable i) {
      return 2;
    }
  }

  private final class RecoverWithF implements Recovery<Try<Integer>> {
    @Override
    public Try<Integer> apply(Throwable i) {
      return Try.of(2);
    }
  }

  private Function<Integer, String> toStringF = new Function<Integer, String>() {
    @Override
    public String apply(Integer i) {
      return "" + i;
    }
  };

  private Function<Integer, Try<String>> toTryString = new Function<Integer, Try<String>>() {
    @Override
    public Try<String> apply(Integer i) {
      return Try.of("" + i);
    }
  };

}

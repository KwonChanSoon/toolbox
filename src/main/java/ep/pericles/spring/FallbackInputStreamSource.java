package ep.pericles.spring;

import java.io.*;
import java.util.*;

import org.springframework.core.io.InputStreamSource;

import com.google.common.base.Preconditions;

public class FallbackInputStreamSource implements InputStreamSource {
  private List<InputStreamSource> inputStreams;

  public FallbackInputStreamSource(InputStreamSource... iss) {
    Preconditions.checkNotNull(iss);
    Preconditions.checkArgument(iss.length > 0);
    inputStreams = Arrays.asList(iss);
  }

  public FallbackInputStreamSource(List<InputStreamSource> iss) {
    Preconditions.checkNotNull(iss);
    Preconditions.checkArgument(iss.size() > 0);
    inputStreams = iss;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    for (InputStreamSource iss : inputStreams) {
      try {
        InputStream is = iss.getInputStream();
        if (null != is) {
          return is;
        }
      } catch (Exception e) {
        continue;
      }
    }
    throw new RuntimeException("No resource found!");
  }
}
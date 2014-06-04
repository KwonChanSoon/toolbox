package ep.pericles.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheTestHelper {
  @Cacheable(value = "TestCache")
  public String cachedMethodOneParam(String param1) {
    return param1;
  }

  @Cacheable(value = "TestCache")
  public String cachedMethodTwoParams(String param1, String param2) {
    return param1 + param2;
  }

  @Cacheable(value = "TestCache", key = "#param1")
  public String cachedMethodTwoParamsOneKey(String param1, String param2) {
    return param1;
  }

  @Cacheable(value = "TestCache")
  public String cachedMethodNoParam() {
    return "cachedMethodNoParam";
  }

  @Cacheable(value = "TestCache")
  public String cachedMethodNoParam2() {
    return "cachedMethodNoParam2";
  }

}

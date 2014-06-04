package eu.qualityontime.test;

/**
 * JUnit test category indicating test which are verifying SQL syntax only.
 * These test are not need to be run continously is Java development is made whihc is not
 * modifying any sql. Plus SQL based tests are always slow.
 */
public interface SqlSyntaxTestCategory {

}

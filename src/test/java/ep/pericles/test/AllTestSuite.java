package ep.pericles.test;

import org.junit.runner.RunWith;

import ep.pericles.test.FlexibleCategories.TestClassPrefix;
import ep.pericles.test.FlexibleCategories.TestClassSuffix;
import ep.pericles.test.FlexibleCategories.TestScanPackage;

/** MyTestSuite runs all slow tests, excluding all test which require a network connection. */
@RunWith(FlexibleCategories.class)
//@ExcludeCategory(CodictUpdateTestCategory.class)
//@IncludeCategory(SlowTestCategory.class)
@TestScanPackage("ep.pericles")
@TestClassPrefix("")
@TestClassSuffix("Test")
public class AllTestSuite {

}

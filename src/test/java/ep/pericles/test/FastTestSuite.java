package ep.pericles.test;

import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;

import ep.pericles.test.FlexibleCategories.TestClassPrefix;
import ep.pericles.test.FlexibleCategories.TestClassSuffix;
import ep.pericles.test.FlexibleCategories.TestScanPackage;

/** MyTestSuite runs all slow tests, e.g excluding all test which require a network or database connection. */
@RunWith(FlexibleCategories.class)
@ExcludeCategory(SlowTestCategory.class)
//@IncludeCategory(SlowTestCategory.class)
@TestScanPackage("ep.pericles")
@TestClassPrefix("")
@TestClassSuffix("Test")
public class FastTestSuite {

}

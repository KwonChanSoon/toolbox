package eu.qualityontime.test;

import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;

import eu.qualityontime.test.FlexibleCategories.TestClassPrefix;
import eu.qualityontime.test.FlexibleCategories.TestClassSuffix;
import eu.qualityontime.test.FlexibleCategories.TestScanPackage;

/** MyTestSuite runs all slow tests, e.g excluding all test which require a network or database connection. */
@RunWith(FlexibleCategories.class)
@ExcludeCategory(SlowTestCategory.class)
//@IncludeCategory(SlowTestCategory.class)
@TestScanPackage("eu.qualityontime")
@TestClassPrefix("")
@TestClassSuffix("Test")
public class FastTestSuite {

}

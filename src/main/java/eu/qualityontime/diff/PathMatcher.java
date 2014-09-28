package eu.qualityontime.diff;

class PathMatcher {
  public boolean match(String templateKey, String currentKey) {
    return templateKey.equals(cleanPath(currentKey));
  }

  private String cleanPath(String path) {
    return path
        .replaceAll("\\[\\d*\\]", "")
        .replaceAll("\\<(ADDED|REMOVED|UNTOUCHED)\\>", "")
        .replaceAll("\\{.*?\\}", "");
  }

}

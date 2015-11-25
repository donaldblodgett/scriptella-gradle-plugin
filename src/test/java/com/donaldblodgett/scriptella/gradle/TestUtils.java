package com.donaldblodgett.scriptella.gradle;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class TestUtils {
  public static String randomString() {
    return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(5, 10));
  }
}

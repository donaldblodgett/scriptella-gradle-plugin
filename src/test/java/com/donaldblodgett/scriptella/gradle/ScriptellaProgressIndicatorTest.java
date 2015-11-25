package com.donaldblodgett.scriptella.gradle;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.commons.lang3.RandomUtils;
import org.gradle.api.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptellaProgressIndicatorTest {
  @Mock
  private Logger logger;
  
  private ScriptellaProgressIndicator indicator;
  
  @Before
  public void setup() {
    initMocks(this);
    indicator = new ScriptellaProgressIndicator(logger, TestUtils.randomString());
  }
  
  @Test
  public void showWithNullLabel() {
    indicator.show(null, RandomUtils.nextDouble(0, 1));
    verify(logger, times(1)).info(any(String.class));
  }
  
  @Test
  public void showWithLabel() {
    indicator.show(TestUtils.randomString(), RandomUtils.nextDouble(0, 1));
    verify(logger, times(1)).info(any(String.class));
  }
  
  @Test
  public void onCompleteWithNullLabel() {
    indicator.onComplete(null);
    verify(logger, times(1)).info(any(String.class));
  }
  
  @Test
  public void onCompleteWithLabel() {
    indicator.onComplete(TestUtils.randomString());
    verify(logger, times(1)).info(any(String.class));
  }
}

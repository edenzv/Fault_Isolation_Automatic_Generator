package org.afeka.fi.tests;

import org.afeka.fi.backend.common.FiLogger;
import org.afeka.fi.backend.common.FiProperties;
import org.apache.logging.log4j.Logger;
import org.junit.Before;

import java.io.IOException;

public class FiCommonTest {
    protected Logger logger = FiLogger.getLogger(this.getClass());
    @Before
    public void init() throws IOException {
        FiProperties.init();
    }
}

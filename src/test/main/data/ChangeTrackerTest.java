package test.main.data;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.data.ChangeTracker;
import main.data.FeatureImpl;

public class ChangeTrackerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void ChangeTrackerShouldThrowIllegalArgumentExceptionNullItem() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("ChangeTracker:"));
        new ChangeTracker<FeatureImpl>(null);
    }
    
    @Test
    public void 
}

package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlStorageTest {
	static SqlStorage store;

	@BeforeClass
	public static void setUpBefore() throws ClassNotFoundException, SQLException {
		store = new SqlStorage(TestData.PROPERTIES());
	}
	
	@Test
	public void saveTestData() throws IOException {
		store.store(Arrays.asList(TestData.mfBar, TestData.mfFoo), Arrays.asList(TestData.mpBar2, TestData.mpFoo1));
	}
	
	@Test
	public void loadTestData() throws IOException, SQLException {
		Collection<MetaFeature> features = new ArrayList<>();
		Collection<MetaPerformance> performances = new ArrayList<>();
		store.restore(features, performances, TestData.ldtCreate.toLocalDate(), LocalDate.now());
		assertEquals(2, features.size());
		assertEquals(2, performances.size());
	}
}

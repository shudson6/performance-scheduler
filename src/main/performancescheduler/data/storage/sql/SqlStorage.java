package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

/**
 * Class used to connect to the database to load and store feature and performance data.
 * 
 * @author Steven Hudson
 */
public class SqlStorage implements AutoCloseable {
    protected DbConnectionService dbcs;
    
    /**
     * Get a new instance. Starts a new {@link DbConnectionService}.
     * @param props
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqlStorage(Properties props) throws ClassNotFoundException, SQLException {
    	dbcs = new DbConnectionService(props);
    	dbcs.start();
    }

    /**
     * Restores feature and/or performance data from the database. Data returned will be valid for at least part of the
     * date range marked by {@code start} (inclusive) and {@code end} (exclusive).
     * 
     * <p>{@code featureData} may not be {@code null} but {@code perfData} may; features may be loaded on their own but
     * loading performances requires loading the features to which they refer. If {@code perfData} is {@code null},
     * performances will simply not be loaded.
     * 
     * @param featureData collection to receive loaded features, not null
     * @param perfData collection to receive loaded performances
     * @param start the beginning of the date range, inclusive
     * @param end the end of the date range, exclusive
     * @throws IOException if the loader fails to get a {@code Statement} from the {@link DbConnectionService}
     * @throws SQLException if an exception occurs while reading the data
     */
    public void restore(Collection<? super MetaFeature> featureData, Collection<? super MetaPerformance> perfData,
    		LocalDate start, LocalDate end) throws IOException, SQLException {
        Objects.requireNonNull(featureData);
        new SqlLoader(dbcs).load(featureData, perfData, start, end);
    }

    /**
     * Stores the given data to the database. It is expected that in majority of cases {@code featureData} should
     * contain all feature instances referred to by elements of {@code performanceData}. However, this is not required;
     * either parameter may be null. Note that if a parameter is null, care should be taken to ensure that any data
     * dependencies in the database are not violated. Otherwise, an exception may occur.
     * 
     * @param featureData the features to be saved
     * @param performanceData the performances to be saved
     * @throws IOException if an exception is thrown during the save operation
     */
    public void store(Collection<MetaFeature> featureData, Collection<MetaPerformance> performanceData) 
            throws IOException {
        new SqlSaver(dbcs).save(featureData, performanceData);
    }
    
    @Override
    public void close() throws SQLException {
        dbcs.close();
    }
}

/****************************
 * Design:
 * App uses this class to interact with DB. This class uses another to create SQL commands (perhaps there is another
 * intervening class to execute those). The SQL command builder class likely needs to be specific to the DBMS. Command
 * builders use some ValueLister<> classes to get lists of values and ordering of columns to build the commands.
 * Lots of classes!
 */

package performancescheduler.data.storage.sql;

import java.util.Collection;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.Storage;

public class DbStorage implements Storage {

    @Override
    public Collection<Feature> restoreFeatureData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Performance> restorePerformanceData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void store(Collection<Feature> featureData, Collection<Performance> performanceData) {
        // TODO Auto-generated method stub
        
    }
    
}

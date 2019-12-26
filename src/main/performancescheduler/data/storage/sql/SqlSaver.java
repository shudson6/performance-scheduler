package performancescheduler.data.storage.sql;

import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

abstract class SqlSaver {
    private Collection<MetaFeature> mfins = new ArrayList<>();
    private Collection<MetaFeature> mfdel = new ArrayList<>();
    private Collection<MetaPerformance> mpins = new ArrayList<>();
    private Collection<MetaPerformance> mpdel = new ArrayList<>();
}

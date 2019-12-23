package performancescheduler.data.storage.sql;

import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

class SqlSaver {
    private Collection<MetaFeature> mfins = new ArrayList<>();
    private Collection<MetaFeature> mfdel = new ArrayList<>();
    private Collection<MetaPerformance> mpins = new ArrayList<>();
    private Collection<MetaPerformance> mpdel = new ArrayList<>();
    
    public SqlSaver(Collection<Feature> features, Collection<Performance> performances) {
        categorize(features, performances);
    }
    
    private void categorize(Collection<Feature> features, Collection<Performance> performances) {
        for (Feature f : features) {
            if (f instanceof MetaFeature) {
                if (((MetaFeature) f).getWrapped() != null) {
                    mfins.add((MetaFeature) f);
                } else {
                    mfdel.add((MetaFeature) f);
                }
            }
        }
        for (Performance p : performances) {
            if (p instanceof MetaPerformance) {
                if (((MetaPerformance) p).getWrapped() != null) {
                    mpins.add((MetaPerformance) p);
                } else {
                    mpdel.add((MetaPerformance) p);
                }
            }
        }
    }
}

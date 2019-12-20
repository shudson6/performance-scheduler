package performancescheduler.data.storage.sql;

class FeatureSqlGenerator {
    
    /********************
     * Create SQL of this form:
     * INSERT INTO featuredata (uuid, title, rating, runtime, is3d, cc, oc, da, created, changed) 
     * VALUES ('uuid', 'title', 'rating', runtime, is3d, cc, oc, da, 'created', 'changed'),
     *        ('uuid', 'title', 'rating', runtime, is3d, cc, oc, da, 'created', 'changed')
     * ON CONFLICT (uuid)
     * DO UPDATE SET title=EXCLUDED.title, rating=EXCLUDED.rating, runtime=EXCLUDED.runtime,
     *               is3d=EXCLUDED.is3d, cc=EXCLUDED.cc, oc=EXCLUDED.oc, da=EXCLUDED.da,
     *               created=EXCLUDED.created, changed=EXCLUDED.changed;
     */
}

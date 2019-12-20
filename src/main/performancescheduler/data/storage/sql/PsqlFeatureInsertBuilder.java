package performancescheduler.data.storage.sql;

import java.util.Collection;

import performancescheduler.data.storage.MetaFeature;

class PsqlFeatureInsertBuilder {
    /********************
     * Setting initial capacity of StringBuilder:
     * each values group is: uuid 32, 4 hyphens = 36
     *                       title up to 32     = 32
     *                       rating up to 4     =  4
     *                       5 bool 5 each      = 25
     *                       2 timestamps x 16  = 32
     *                       10 commas + ()     = 12 ===> 141
     * "INSERT INTO FEATUREDATA VALUES "        = 31
     * ON CONFLICT clause 19 + 102 + 95 + 23    = 239
     * So, there are 270 characters plus 141 * number of entries.
     * Good news! A delete entry is only max 84 characters.
     */
    static final int ENTRY_SIZE = 141;
    static final int SQL_SIZE = 270;
    static final String ON_CONFLICT = "ON CONFLICT (uuid) DO UPDATE SET title=EXCLUDED.title,rating=EXCLUDED.rating,"
            + "runtime=EXCLUDED.runtime,is3d=EXCLUDED.is3d,cc=EXCLUDED.cc,oc=EXCLUDED.oc,da=EXCLUDED.da,"
            + "created=EXCLUDED.created,changed=EXCLUDED.changed,active=EXCLUDED.active;";
    
    public String generateSQL(Collection<MetaFeature> features) {
        if (features == null || features.size() < 1) {
            return "";
        }
        // start the SQL in a StringBuilder
        StringBuilder str = new StringBuilder(SQL_SIZE + features.size() * ENTRY_SIZE);
        str.append("INSERT INTO FEATUREDATA VALUES ");
        features.stream().filter(f -> f.getTitle() != null).forEach(f -> str.append(ftrString(f)));
        str.replace(str.lastIndexOf(","), str.length(), " ");
        str.append(ON_CONFLICT);
        features.stream().filter(f -> f.getTitle() == null).forEach(f -> str.append(delString(f)));
        return str.toString();
    }
    
    private String delString(MetaFeature ftr) {
        return String.format("UPDATE FEATUREDATA SET active=false WHERE uuid='%s';", ftr.getUuid().toString());
    }
    
    private String ftrString(MetaFeature ftr) {
        return String.format("('%s','%s','%s',%d,%s,%s,%s,%s,'%s','%s'),", ftr.getUuid().toString(),
                ftr.getTitle(), ftr.getRating().toString(), ftr.getRuntime(), Boolean.toString(ftr.is3d()),
                Boolean.toString(ftr.hasClosedCaptions()), Boolean.toString(ftr.hasOpenCaptions()),
                Boolean.toString(ftr.hasDescriptiveAudio()), ftr.getCreatedTimestamp().toString(),
                ftr.getChangedTimestamp().toString());
    }
    
    /********************
     * Create SQL of this form:
     * INSERT INTO featuredata (uuid, title, rating, runtime, is3d, cc, oc, da, created, changed, active) 
     * VALUES ('uuid', 'title', 'rating', runtime, is3d, cc, oc, da, 'created', 'changed', active),
     *        ('uuid', 'title', 'rating', runtime, is3d, cc, oc, da, 'created', 'changed', active)
     * ON CONFLICT (uuid)
     * DO UPDATE SET title=EXCLUDED.title,rating=EXCLUDED.rating,runtime=EXCLUDED.runtime,is3d=EXCLUDED.is3d,
     *               cc=EXCLUDED.cc,oc=EXCLUDED.oc,da=EXCLUDED.da,created=EXCLUDED.created,changed=EXCLUDED.changed,
     *               active=EXCLUDED.active;
     */
}

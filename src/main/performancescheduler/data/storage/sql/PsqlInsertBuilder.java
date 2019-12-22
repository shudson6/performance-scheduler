package performancescheduler.data.storage.sql;

import java.util.Objects;

import performancescheduler.data.storage.MetaWrapper;

class PsqlInsertBuilder<T extends MetaWrapper<?>> extends SqlCommandBuilder<T> {
    private ValueLister<T> vl;
    private String tbl;
    
    public PsqlInsertBuilder(ValueLister<T> valueLister, String tableName) {
        Objects.requireNonNull(valueLister);
        Objects.requireNonNull(tableName);
        vl = valueLister;
        tbl = tableName;
    }
    
    @Override
    public boolean add(T toAdd) {
        if (toAdd != null && toAdd.getWrapped() != null) {
            return super.add(toAdd);
        }
        return false;
    }
    
    @Override
    protected String buildCommand() {
        if (getData().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(tbl);
        sb.append(" VALUES ");
        getData().forEach(f -> sb.append(vl.listValues(f) + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        sb.append(conflictClause());
        sb.append(";");
        return sb.toString();
    }
    
    private String conflictClause() {
        StringBuilder sb = new StringBuilder();
        sb.append("ON CONFLICT (");
        sb.append(SQL.COL_UUID);
        sb.append(") DO UPDATE SET ");
        // don't update the uuid or created fields
        vl.columnOrder().stream().filter(s -> !s.equals(SQL.COL_UUID) && !s.equals(SQL.COL_CREATED))
                .forEach(s -> sb.append(s + "=EXCLUDED." + s + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        // update the entry if the changed field is different (indicating there's actually something to update)
        sb.append(String.format("WHERE %1$s.%2$s=EXCLUDED.%2$s AND %1$s.%3$s<>EXCLUDED.%3$s", 
                tbl, SQL.COL_UUID, SQL.COL_CHANGED));
        return sb.toString();
    }
}

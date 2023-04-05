package dev.wony.jpa1.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;

public class MyH2Dialect extends H2Dialect {
    public MyH2Dialect() {
        super();
        // group concat
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StringType.INSTANCE));
    }
}

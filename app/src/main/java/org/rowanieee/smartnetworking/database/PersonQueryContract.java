package org.rowanieee.smartnetworking.database;

import android.provider.BaseColumns;

/**
 * Created by Nick on 5/16/2016.
 */
public final class PersonQueryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PersonQueryContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PersonQueryEntry implements BaseColumns {
        public static final String TABLE_NAME = "FOOD4";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_EMAIL = "EMAIL";
        public static final String COLUMN_PHOTO = "BASE64PHOTO";
        public static final String COLUMN_QRURL = "QRURL";
        public static final String COLUMN_COMPANY = "COMPANY";
        public static final String COLUMN_TITLE = "TITLE";
        public static final String COLUMN_PERSONAL_STATEMENT = "PERSONALSTATEMENT";
        public static final String COLUMN_CONNECTIONS = "CONNECTIONS";
    }
}

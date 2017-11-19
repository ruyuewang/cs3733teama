package com.teama.requestsubsystem;

import java.util.ArrayList;

public interface StaffInfoDataSource {

    /**
     * Returns an available staff member with the requested attributes
     * @param attrib
     * @return
     */
    default ServiceStaff findQualified(StaffAttrib attrib) {
        return null;
    }

    default void close() {

    }

    default ArrayList<ServiceStaff> getIntrStaff() {
        return null;
    }
}
package com.pineone.icbms.so.interfaces.database.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pineone.icbms.so.interfaces.database.model.CommonEntity;
import com.pineone.icbms.so.util.time.DateFormat;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Composite Virtual Object model for authoring.<BR/>
 * Created by uni4love on 2017. 1. 13..
 */
//@Data
@JsonPropertyOrder({"id","name", "description"})
@ToString
public class CompositeVirtualObjectForDB extends CommonEntity {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String functionalityId;

    @Getter @Setter
    private String aspectId;

    @Getter @Setter
    private List<VirtualObjectForDB> virtualObjectForDBList;

    @Getter @Setter
    private String type;

//    public List<VirtualObjectForDB> getVirtualObjectForDBList() {
//        return virtualObjectForDBList;
//    }
//
//    public void setVirtualObjectForDBList(List<VirtualObjectForDB> virtualObjectForDBList) {
//        this.virtualObjectForDBList = virtualObjectForDBList;
//    }

}

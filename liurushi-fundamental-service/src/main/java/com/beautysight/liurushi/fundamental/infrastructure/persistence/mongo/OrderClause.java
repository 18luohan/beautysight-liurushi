/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public final class OrderClause {

    private static final String ASC = "";
    private static final String DESC = "-";

    private static final int MAX_ORDER_FIELDS_NUM = 4;
    private List<Sortable> orderFields = new ArrayList<>(MAX_ORDER_FIELDS_NUM);

    public OrderClause addFixedAsc(String field) {
        checkState();
        orderFields.add(FixedSortable.newFixedAsc(field));
        return this;
    }

    public OrderClause addFixedDesc(String field) {
        checkState();
        orderFields.add(FixedSortable.newFixedDesc(field));
        return this;
    }

    public OrderClause addInvertibleAsc(String field) {
        checkState();
        orderFields.add(InvertibleSortable.newInvertibleAsc(field));
        return this;
    }

    public OrderClause addInvertibleDesc(String field) {
        checkState();
        orderFields.add(InvertibleSortable.newInvertibleDesc(field));
        return this;
    }

    public String orderClause() {
        Preconditions.checkState(!orderFields.isEmpty(), "No order fields");
        StringBuilder clause = new StringBuilder();
        for (Sortable sortable : orderFields) {
            clause.append(",").append(sortable.asQueryStr());
        }
        return clause.substring(1);
    }

    public String inverseOrderClause() {
        Preconditions.checkState(!orderFields.isEmpty(), "No order fields");
        StringBuilder clause = new StringBuilder();
        for (Sortable sortable : orderFields) {
            clause.append(",");
            if (sortable instanceof FixedSortable) {
                clause.append(sortable.asQueryStr());
            } else if (sortable instanceof InvertibleSortable) {
                InvertibleSortable invertibleSortable = (InvertibleSortable) sortable;
                clause.append(invertibleSortable.asInverseQueryStr());
            }
        }
        return clause.substring(1);
    }

    private void checkState() {
        Preconditions.checkState((orderFields.size() < MAX_ORDER_FIELDS_NUM), "Order fields num greater than %s", MAX_ORDER_FIELDS_NUM);
    }

    private static abstract class Sortable {
        String asQueryStr() {
            return order() + field();
        }

        protected abstract String order();

        protected abstract String field();
    }

    private static abstract class FixedSortable extends Sortable {
        private String field;

        private FixedSortable(String field) {
            this.field = field;
        }

        @Override
        protected String field() {
            return this.field;
        }

        static FixedAsc newFixedAsc(String field) {
            return new FixedAsc(field);
        }

        static FixedDesc newFixedDesc(String field) {
            return new FixedDesc(field);
        }
    }

    private static class FixedAsc extends FixedSortable {
        private FixedAsc(String field) {
            super(field);
        }

        @Override
        protected String order() {
            return ASC;
        }
    }

    private static class FixedDesc extends FixedSortable {
        private FixedDesc(String field) {
            super(field);
        }

        @Override
        protected String order() {
            return DESC;
        }
    }

    private static abstract class InvertibleSortable extends Sortable {
        protected FixedSortable fixedSortable;

        private InvertibleSortable(FixedSortable fixedSortable) {
            this.fixedSortable = fixedSortable;
        }

        @Override
        protected String order() {
            return fixedSortable.order();
        }

        @Override
        protected String field() {
            return fixedSortable.field();
        }

        String asInverseQueryStr() {
            return inverseOrder() + field();
        }

        protected abstract String inverseOrder();

        static InvertibleAsc newInvertibleAsc(String field) {
            return new InvertibleAsc(field);
        }

        static InvertibleDesc newInvertibleDesc(String field) {
            return new InvertibleDesc(field);
        }
    }

    private static class InvertibleAsc extends InvertibleSortable {
        private InvertibleAsc(String field) {
            super(FixedSortable.newFixedAsc(field));
        }

        @Override
        protected String inverseOrder() {
            return DESC;
        }
    }

    private static class InvertibleDesc extends InvertibleSortable {
        private InvertibleDesc(String field) {
            super(FixedSortable.newFixedDesc(field));
        }

        @Override
        protected String inverseOrder() {
            return ASC;
        }
    }

}

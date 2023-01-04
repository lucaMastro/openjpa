/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.trader.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Trade implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary identity of a committed trade.
     * Its value is generated by the persistence provider.
     * The application must not set or change this value.
     * Hence no setter method is provided.
     */
    @Id
    @GeneratedValue
    private Long id;

    @Column(precision=10,scale=2)
    private double price;
    private int volume;

    @OneToOne(cascade={CascadeType.MERGE,CascadeType.DETACH}, optional=false)
    private Ask ask;

    @OneToOne(cascade={CascadeType.MERGE,CascadeType.DETACH}, optional=false)
    private Bid bid;

    protected Trade() {

    }

    public Trade(Ask a, Bid b) {
        if (a == null)
            throw new NullPointerException("Can not create Trade with null Ask");
        if (b == null)
            throw new NullPointerException("Can not create Trade with null Bid");
        if (a.getSeller() == null)
            throw new NullPointerException("Can not create Trade with null trader for Ask");
        if (b.getBuyer() == null)
            throw new NullPointerException("Can not create Trade with null trader for Bid");
        if (a.getSeller().equals(b.getBuyer()))
            throw new IllegalArgumentException("Ask and Bid have same trader " + a.getSeller());
        if (!a.getStock().equals(b.getStock())) {
            throw new IllegalArgumentException("Stock (ask) " + a.getStock()
                    + " does not match Stock (bid) " + b.getStock());
        }
        ask = a;
        bid = b;
        ask.setTrade(this);
        bid.setTrade(this);
        price  = Math.max(a.getPrice(), b.getPrice());
        volume = Math.min(a.getVolume(), b.getVolume());
    }

    public Long getId() {
        return id;
    }

    public Trader getBuyer() {
        return bid.getBuyer();
    }

    public Trader getSeller() {
        return ask.getSeller();
    }

    public Stock getStock() {
        return ask.getStock();
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trade other = (Trade) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String toString() {
    	return "Trade-"+id;
    }
}

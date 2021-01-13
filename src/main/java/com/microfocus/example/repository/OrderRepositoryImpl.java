/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.repository;

import com.microfocus.example.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of Custom Order Repository
 * @author Kevin A. Lee
 */
@Transactional
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    private final OrderRepositoryBasic orderRepositoryBasic;

    @PersistenceContext
    EntityManager entityManager;

    public OrderRepositoryImpl(OrderRepositoryBasic orderRepositoryBasic) {
        this.orderRepositoryBasic = orderRepositoryBasic;
    }

    @SuppressWarnings("unchecked")
    public List<Order> findByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.user.id = ?1",
                Order.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Order> findByOrderNum(String orderNum) {
        Query query = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.orderNum LIKE ?1",
                Order.class);
        query.setParameter(1, orderNum);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public long countByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(o) FROM Order o WHERE o.user.id = ?1",
                Long.class);
        query.setParameter(1, userId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public long countNotShippedByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(o) FROM Order o WHERE o.user.id = ?1 AND o.shipped = false",
                Long.class);
        query.setParameter(1, userId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public void markOrderAsShippedById(UUID OrderId) {
        Query query = entityManager.createQuery(
                "UPDATE Order o SET o.shipped = true WHERE o.id = ?1");
        query.setParameter(1, OrderId);
        query.executeUpdate();
    }

    /*public Order save(OrderForm Order) {
        Order m =  new Order();
        m.setUser(Order.getUser());
        m.setText(Order.getText());
        return OrderRepositoryBasic.save(m);
    }*/

}

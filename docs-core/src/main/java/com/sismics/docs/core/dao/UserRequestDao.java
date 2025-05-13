package com.sismics.docs.core.dao;

import com.sismics.docs.core.model.jpa.UserRequest;
import com.sismics.util.context.ThreadLocalContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User request DAO.
 */
public class UserRequestDao {

    /**
     * Create a new user registration request.
     *
     * @param request The request to persist
     * @return Request ID
     */
    public String create(UserRequest request) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();

        if (request.getId() == null) {
            request.setId(UUID.randomUUID().toString());
        }

        System.out.println("[DAO] Persisting request with ID: " + request.getId());
        em.persist(request);

        return request.getId();
    }



    /**
     * Get all pending requests (status = "pending").
     *
     * @return List of pending user requests
     */
    public List<UserRequest> getPendingRequests() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select r from UserRequest r where r.status = :status order by r.createDate asc");
        q.setParameter("status", "pending");
        return q.getResultList();
    }

    /**
     * Update the status of a request.
     *
     * @param id     Request ID
     * @param status New status ("accepted", "rejected", etc.)
     */
    public void updateStatus(String id, String status) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select r from UserRequest r where r.id = :id");
        q.setParameter("id", id);
        try {
            UserRequest request = (UserRequest) q.getSingleResult();
            request.setStatus(status);
        } catch (NoResultException e) {
            // ignore silently or log warning
        }
    }

    /**
     * Get a request by ID.
     *
     * @param id Request ID
     * @return UserRequest or null
     */
    public UserRequest getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        try {
            return em.find(UserRequest.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }
}

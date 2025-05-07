package com.sismics.docs.core.service;

import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.UserRequestDao;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.UserRequest;

import jakarta.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service for user registration requests.
 */
public class UserRequestService {

    /**
     * Create a new guest registration request.
     */
    public String createRequest(String name, String email) {
        EntityManager em = getEntityManager();
        UserRequestDao dao = new UserRequestDao();

        UserRequest request = new UserRequest();
        request.setId(UUID.randomUUID().toString());
        request.setName(name);
        request.setEmail(email);
        request.setCreateDate(new Date());
        request.setStatus("pending");

        dao.create(request);
        return request.getId();
    }

    /**
     * Get all pending user registration requests.
     */
    public List<UserRequest> getPendingRequests() {
        return new UserRequestDao().getPendingRequests();
    }

    /**
     * Approve a request: create a new User account and mark request as accepted.
     */
    public void approveRequest(String requestId, String adminUserId) throws Exception {
        EntityManager em = getEntityManager();
        UserRequestDao requestDao = new UserRequestDao();
        UserDao userDao = new UserDao();

        UserRequest request = requestDao.getById(requestId);
        if (request == null || !"pending".equals(request.getStatus())) {
            throw new IllegalStateException("Invalid or already processed request");
        }

        // Create user from request
        User user = new User();
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setCreateDate(new Date());
        user.setPassword("changeme"); // TODO: set initial password or send email
        userDao.create(user, adminUserId);

        // Mark request as accepted
        requestDao.updateStatus(requestId, "accepted");
    }

    /**
     * Reject a request.
     */
    public void rejectRequest(String requestId) {
        new UserRequestDao().updateStatus(requestId, "rejected");
    }

    private EntityManager getEntityManager() {
        return com.sismics.util.context.ThreadLocalContext.get().getEntityManager();
    }
}

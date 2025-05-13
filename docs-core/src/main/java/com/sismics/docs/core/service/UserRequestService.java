package com.sismics.docs.core.service;

import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.UserRequestDao;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.UserRequest;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for user registration requests.
 */
public class UserRequestService {
    private static final Logger log = LoggerFactory.getLogger(UserRequestService.class);
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
    public void approveRequest(String requestId) throws Exception {
        log.info(">>> [APPROVE] Called with requestId={}", requestId);

        EntityManager em = getEntityManager();
        UserRequestDao requestDao = new UserRequestDao();
        UserDao userDao = new UserDao();

        UserRequest request = requestDao.getById(requestId);

        if (request == null) {
            log.warn("[APPROVE] No request found with ID {}", requestId);
        } else {
            log.info("[APPROVE] Found request: ID={}, status={}, email={}", request.getId(), request.getStatus(), request.getEmail());
        }

        if (request == null || !"pending".equals(request.getStatus())) {
            log.error("[APPROVE] Request invalid or already processed: ID={}, status={}", 
                    request != null ? request.getId() : "null",
                    request != null ? request.getStatus() : "null");
            throw new IllegalStateException("Invalid or already processed request");
        }

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setCreateDate(new Date());
        user.setPassword("changeme"); // TODO: implement secure password

        log.info("[APPROVE] Creating user from request email: {}", request.getEmail());
        userDao.create(user, request.getId());

        log.info("[APPROVE] Updating request status to 'accepted' for ID {}", requestId);
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

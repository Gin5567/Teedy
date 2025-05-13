package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.DocumentDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.model.jpa.Document;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.rest.resource.BaseResource;
import com.sismics.util.context.ThreadLocalContext;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * Admin Dashboard resource for user activity visualization.
 */
@Path("/admin/dashboard")
public class AdminDashboardResource extends BaseResource {

    private static final Logger LOGGER = Logger.getLogger(AdminDashboardResource.class.getName());

    /**
     * Get user activity summary (document upload history).
     */
    @GET
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("ADMIN")
    public Response getUserActivities() {
        LOGGER.info("📥 Received request to /admin/dashboard/activities");

        authenticate();
        LOGGER.info("🔐 Authentication successful");

        DocumentDao documentDao = new DocumentDao();
        List<Document> documents = documentDao.findAll(0, Integer.MAX_VALUE);
        LOGGER.info("📄 Fetched document list, count = " + documents.size());

        JsonArrayBuilder builder = Json.createArrayBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        UserDao userDao = new UserDao();

        for (Document doc : documents) {
            User user = userDao.getById(doc.getUserId());
            String creatorName = user != null ? user.getUsername().trim() : doc.getUserId();


            LOGGER.info("📄 " + doc.getTitle() + " uploaded by " + creatorName);

            builder.add(Json.createObjectBuilder()
                .add("documentId", doc.getId())
                .add("title", doc.getTitle())
                .add("creator", creatorName) // ✅ 显示用户名而不是 UUID
                .add("createDate", sdf.format(doc.getCreateDate()))
            );
        }

        LOGGER.info("✅ JSON response built successfully");

        return Response.ok(builder.build()).build();
    }
}

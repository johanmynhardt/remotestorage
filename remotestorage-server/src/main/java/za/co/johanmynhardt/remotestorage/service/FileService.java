package za.co.johanmynhardt.remotestorage.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Johan Mynhardt
 */
public interface FileService {

	@GET
	@Path("/{pathSegments:.*}")
	public Response process(@Context UriInfo uriInfo, @PathParam("pathSegments") List<PathSegment> pathSegments);

	@Produces(MediaType.APPLICATION_JSON)
	public Response getFolder(String path);
}

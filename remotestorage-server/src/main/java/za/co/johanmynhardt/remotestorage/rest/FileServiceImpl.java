package za.co.johanmynhardt.remotestorage.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import za.co.johanmynhardt.remotestorage.Config;
import za.co.johanmynhardt.remotestorage.impl.PropertyServiceImpl;
import za.co.johanmynhardt.remotestorage.model.Document;
import za.co.johanmynhardt.remotestorage.model.Folder;
import za.co.johanmynhardt.remotestorage.model.NodeType;
import za.co.johanmynhardt.remotestorage.service.FileService;
import za.co.johanmynhardt.remotestorage.service.PropertyService;

/**
 * @author Johan Mynhardt
 */
@RequestScoped
@Path("/")
public class FileServiceImpl implements FileService {
	private static final Logger LOG = Logger.getLogger(FileServiceImpl.class.getName());
	ObjectMapper mapper = new ObjectMapper();
	private PropertyService propertyService = new PropertyServiceImpl();

	@GET
	@Path("/{pathSegments:.*}")
	public Response process(@Context UriInfo uriInfo, @PathParam("pathSegments") List<PathSegment> pathSegments) {
		try {
			if (exists(uriInfo.getPath())) {
				return PathTool.isFolder(pathSegments) ? getFolder(uriInfo.getPath()) : getDocument(uriInfo.getPath());
			} else {
				LOG.warning("Path not found in storage: " + uriInfo.getPath());
				return Response.status(Response.Status.NOT_FOUND).entity("Not Found").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return errorResponse(e);
		}
	}

	@Produces(MediaType.APPLICATION_JSON)
	public Response getFolder(final String path) {
		Folder folder = new Folder() {

			File folder = getFolder();
			File[] fileList = folder.listFiles();

			File getFolder() {
				return getInternalPath(path);
			}

			@Override
			public List<Folder> getFolders() {
				return FileTool.getFolders(fileList);
			}

			@Override
			public List<Document> getDocuments() {
				return FileTool.getFiles(fileList);
			}

			@Override
			public String getName() {
				return folder.getName();
			}

			@Override
			public String getType() {
				return NodeType.FOLDER.name();
			}

			@Override
			public String getCurrentVersion() {
				return Long.toString(folder.lastModified());
			}
		};
		try {

			Map<String, String> folderContentMap = new TreeMap<String, String>();

			for (Folder folderInFolder : folder.getFolders()) {
				folderContentMap.put(folderInFolder.getName().concat("/"), folderInFolder.getCurrentVersion());
			}
			for (Document document : folder.getDocuments()) {
				folderContentMap.put(document.getName(), document.getCurrentVersion());
			}

			if (folderContentMap.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity("Not Found").build();
			} else return Response.ok().entity(mapper.writeValueAsString(folderContentMap)).build();
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
		}
	}

	/**
	 * A successful GET request to a document SHOULD be responded to with
	 * the full document contents in the body, the document's content type
	 * in a 'Content-Type' header, and the document's current version in an
	 * 'ETag' header.
	 *
	 * @param path
	 * @return
	 */
	private Response getDocument(final String path) {
		File file = getInternalPath(path);
		LOG.info("Returning document: " + path);
		return new ResponseBuilderImpl()
				.header("Content-Length", file.length())
				.tag(new EntityTag(Long.valueOf(file.lastModified()).toString()))
				.type(MediaType.WILDCARD_TYPE)
				.entity(file)
				.build();
	}

	private Response errorResponse(Exception exception) {
		//todo: handle different exception outcomes
		return Response.noContent().build();
	}

	private File getInternalPath(String remotePath) {
		return new File(propertyService.getProperty(Config.STORAGE_BASE, "/opt/remote-storage").concat("/").concat(remotePath));
	}

	private boolean exists(String remotePath) {
		return FileTool.exists(getInternalPath(remotePath).toString());
	}

	static class PathTool {
		public static boolean isFolder(List<PathSegment> pathSegments) {
			return pathSegments.get(pathSegments.size() - 1).getPath().trim().isEmpty();
		}
	}

	static class FileTool {
		public static boolean exists(String remotePath) {
			return new File(remotePath).exists();
		}

		public static List<Folder> getFolders(File[] files) {
			List<Folder> folders = new ArrayList<Folder>();
			for (final File file : getFiles(files, NodeType.FOLDER)) {
				File[] subFiles = file.listFiles();
				if (subFiles != null && subFiles.length > 0) {
					Folder folder = new Folder() {
						@Override
						public List<Folder> getFolders() {
							return null;
						}

						@Override
						public List<Document> getDocuments() {
							return null;
						}

						@Override
						public String getName() {
							return file.getName();
						}

						@Override
						public String getType() {
							return file.isDirectory() ? NodeType.FOLDER.name() : NodeType.FILE.name();
						}

						@Override
						public String getCurrentVersion() {
							return Long.toString(file.lastModified());
						}
					};
					folders.add(folder);
				}
			}

			return folders;
		}

		public static List<Document> getFiles(File[] files) {
			List<Document> documents = new ArrayList<Document>();
			for (final File file : getFiles(files, NodeType.FILE)) {
				Document document = new Document() {
					@Override
					public String getName() {
						return file.getName();
					}

					@Override
					public String getContentType() {
						return null;
					}

					@Override
					public String getContent() {
						return null;
					}

					@Override
					public String getCurrentVersion() {
						return Long.toString(file.lastModified());
					}
				};
				documents.add(document);
			}
			return documents;
		}

		private static List<File> getFiles(File[] files, NodeType nodeType) {
			List<File> fileList = new ArrayList<File>();
			for (File file : files) {
				if (NodeType.FOLDER == nodeType && file.isDirectory()) {
					fileList.add(file);
				} else if (NodeType.FILE == nodeType && file.isFile()) {
					fileList.add(file);
				}
			}
			return fileList;
		}

	}
}

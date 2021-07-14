package FileExplore;
import java.io.File;
import java.io.Serializable;

public class FileViewModel implements Serializable {
	private String url;
	private String fileName;
	private String path;
	private String dataModified;
	private boolean isFile;
	private String size;
	private File file;
	
	public FileViewModel(String url, String fileName, String path, String dataModified, boolean isFile, String size,
			File file) {
		super();
		this.url = url;
		this.fileName = fileName;
		this.path = path;
		this.dataModified = dataModified;
		this.isFile = isFile;
		this.size = size;
		this.file = file;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDataModified() {
		return dataModified;
	}

	public void setDataModified(String dataModified) {
		this.dataModified = dataModified;
	}

	public boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}

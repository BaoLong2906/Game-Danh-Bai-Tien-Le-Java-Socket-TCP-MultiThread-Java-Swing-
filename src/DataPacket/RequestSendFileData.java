package DataPacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RequestSendFileData extends DataPacket {
	
	public static String tag = "RequestSendFileData";
	private String path;
	private File file;
	private byte[] content;
	
	public RequestSendFileData(String path) {
		super(tag);
		// TODO Auto-generated constructor stub
		this.path = path;
		System.out.println("path: " + path);
		file = new File(path);
		try {
			content = Files.readAllBytes(file.toPath());
			System.out.println(content.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}

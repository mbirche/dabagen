package modelo;

import java.io.File;

public class MyFile {

	File file;

	public MyFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return file.getName();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}

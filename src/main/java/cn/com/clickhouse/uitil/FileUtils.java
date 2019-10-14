package cn.com.clickhouse.uitil;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

@Slf4j
public class FileUtils {
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    private FileUtils() {}
	public static String readClassPathFile(String fileName) {
		return readClassPathFile(fileName, DEFAULT_CHARSET);
	}
	
	public static String readClassPathFile(String fileName, String charset) {
	    return readClassPathFile(fileName, Charset.forName(charset));
	}
	

	public static String readClassPathFile(String fileName, Charset charset) {
	    try {
	        @Cleanup
	        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
	        
	        return readStream(in, charset);
        } catch (IOException e) {
            log.error("Read classpath file {} failed.",fileName);
            throw new RuntimeException("Read classpath file "+fileName+" failed.");
        }
	}
	

    public static String readFile(File file) {
        return readFile(file, DEFAULT_CHARSET);
    }
	

	public static String readFile(File file, String charset) {
	    return readFile(file, Charset.forName(charset));
	}
	

	public static String readFile(File file, Charset charset) {
	    if(file==null || !file.exists()) {
	        throw new RuntimeException("file "+file+" isn't exist.");
	    }
	    if(!file.isFile()) {
	        throw new RuntimeException("file "+file+" isn't a file.");
	    }
	    
	    try {
	        @Cleanup
            InputStream in = new FileInputStream(file);
	        return readStream(in, charset);
        } catch (IOException e) {
            log.error("Read file {} failed.",file);
            throw new RuntimeException("Read file "+file+" failed.");
        }
	}
	

	public static byte[] readClassPathFileToBinary(String fileName) throws IOException {
	    InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    IOUtils.copy(in, out);
	    
	    return out.toByteArray();
	}
	

	private static String readStream(InputStream in, Charset charset) throws IOException {
        @Cleanup
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        
        return new String(out.toByteArray(), charset);
	}

}
/**
 * 
 */
package org.wltea.analyzer.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.seg.CJKSegmenter;
import org.wltea.analyzer.seg.ISegmenter;
import org.wltea.analyzer.seg.LetterSegmenter;
import org.wltea.analyzer.seg.QuantifierSegmenter;

public class Configuration {

	private static String FILE_NAME = "ik/IkAnalyzer.cfg.xml";
	private static final String EXT_DICT = "ext_dict";
	private static final String EXT_STOP = "ext_stopwords";
	private static final Configuration CFG = new Configuration();
    private static ESLogger logger = null;
	private Properties props;

	private Configuration(){

        logger = Loggers.getLogger("ik-analyzer");
		props = new Properties();
        Environment environment=new Environment();
        File fileConfig= new File(environment.configFile(), FILE_NAME);
        InputStream input = null;// Configuration.class.getResourceAsStream(FILE_NAME);
        try {
            input = new FileInputStream(fileConfig);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(input != null){
			try {
				props.loadFromXML(input);
                logger.info("成功加载扩展配置文件:{}",FILE_NAME);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<String>(2);
		String extDictCfg = CFG.props.getProperty(EXT_DICT);
		if(extDictCfg != null){

			String[] filePaths = extDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
                        File file=new File("ik",filePath.trim());
						extDictFiles.add(file.toString());

					}
				}
			}
		}		
		return extDictFiles;		
	}

	public static List<String> getExtStopWordDictionarys(){
		List<String> extStopWordDictFiles = new ArrayList<String>(2);
		String extStopWordDictCfg = CFG.props.getProperty(EXT_STOP);
		if(extStopWordDictCfg != null){
			
			String[] filePaths = extStopWordDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
                        File file=new File("ik",filePath.trim());
						extStopWordDictFiles.add(file.toString());

					}
				}
			}
		}		
		return extStopWordDictFiles;		
	}

	public static List<ISegmenter> loadSegmenter(){
		Dictionary.getInstance();
		List<ISegmenter> segmenters = new ArrayList<ISegmenter>(4);
		segmenters.add(new QuantifierSegmenter());
		segmenters.add(new LetterSegmenter());
		segmenters.add(new CJKSegmenter());
		return segmenters;
	}
}

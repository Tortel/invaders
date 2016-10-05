package Invaders;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Score {
	private HighScore Highest;
	
	public Score(){
		Highest = new HighScore(0);
	}
	
	
	public void submitScore(int score){
		if(Highest.getScore()< score){
			Highest = new HighScore(score);
		}
		writeToFile();
		processScores();
	}	
	
	public void processScores(){
		try{
			Document dom;
			//get the factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse("test.txt");
			//get the root elememt
			Element docEle = dom.getDocumentElement();
			//get a nodelist of <Score> elements
			NodeList nl = docEle.getElementsByTagName("Score");
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {
					//get the element
					Element el = (Element)nl.item(i);
					//get the HighScore object
					HighScore e = getScore(el);
					//add it to list
					if(e.getScore() > Highest.getScore()){
						Highest = e;
					}
				}
			}
		}catch(Exception e){
			//exception if the file doesnt exist
			writeToFile();
			//e.printStackTrace();
		}
	}
	
	
	//Creats HighScore instances from the <score> tags
	private HighScore getScore(Element elem) {
		//Pull the values
		String name = getTextValue(elem,"name");
		int score = getIntValue(elem,"val");
		return new HighScore(score,name);
	}
	
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	
	private int getIntValue(Element ele, String tagName) {
		try {
			return Integer.parseInt(getTextValue(ele,tagName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void writeToFile(){
		File file = new File("test.txt");
		DataOutputStream writer = null;
		try {
			writer = new DataOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String a = "<Scores>\n<Score>\n<name>null</name>\n<val>";
		a+=+Highest.getScore();
		a+=	"</val>\n</Score>\n</Scores>";
		try {
			StringReader in=new StringReader(a);
			int x = -11;
			do{
				x=in.read();
				if(x!=-1){
					writer.write(x);
					//System.out.print(x);
				}
			}while(x!=-1);
			in.close();
			writer.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public HighScore getHighest() {
		return Highest;
	}
}
